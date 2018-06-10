package wiresegal.psionup.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import wiresegal.psionup.common.effect.ModPotions;
import wiresegal.psionup.common.items.ModItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityGaussPulse extends EntityThrowable {
	public EntityGaussPulse(World world) {
		super(world);
		setSize(0f, 0f);
	}
	
	public EntityGaussPulse(World world, EntityLivingBase thrower, AmmoStatus ammo) {
		super(world, thrower);
		setSize(0f, 0f);
		
		this.uuid = thrower.getUniqueID();
		setAmmo(ammo);
		
		motionX -= thrower.motionX;
		if(!thrower.onGround) motionY -= thrower.motionY;
		motionZ -= thrower.motionZ;
	}
	
	public static DataParameter<Byte> AMMO_WATCHER;
	
	static final String TAG_CASTER = "CasterUUID";
	static final String TAG_AGE = "Age";
	static final String TAG_AMMO = "Ammo";
	
	static final int MAX_AGE = 600;
	static final int PARTICLE_COUNT = 5;
	
	int age = 0;
	UUID uuid = null;
	
	@Override
	protected void entityInit() {
		setAmmo(AmmoStatus.NOTAMMO);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		age++;
		
		if(age > MAX_AGE) setDead();
		
		if((prevPosX - posX) * (prevPosX - posX) + (prevPosY - posY) * (prevPosY - posY) * (prevPosZ - posZ) * (prevPosZ - posZ) < 1) {
			setDead();
		}
		
		//display particle trail
		
		int color = getAmmo().color;
		float r = ((color & 0xFF0000) >> 16) / 255f;
		float g = ((color & 0x00FF00) >>  8) / 255f;
		float b = (color & 0x0000FF) / 255f;
		
		Vector3 lookVector = new Vector3(motionX, motionY, motionZ).normalize();
		
		for(int i = 0; i < PARTICLE_COUNT; i++) {
			Vector3 randomizedLook = lookVector.copy();
			double spread = 0.6;
			randomizedLook.x += (Math.random() - 0.5) * spread;
			randomizedLook.y += (Math.random() - 0.5) * spread;
			randomizedLook.z += (Math.random() - 0.5) * spread;
			randomizedLook.normalize().multiply(0.3);
			Psi.proxy.sparkleFX(world, posX, posY, posZ, r, g, b, (float) randomizedLook.x, (float) randomizedLook.y, (float) randomizedLook.z, 1.2f, 12);
		}
	}
	
	@Override
	protected void onImpact(RayTraceResult ray) {
		if(world.isRemote) return;
		
		Entity hitEntity = ray.entityHit;
		if(hitEntity != null) {
			if(hitEntity.getCachedUniqueIdString().equals(thrower.getCachedUniqueIdString())) return;
			
			AmmoStatus ammo = getAmmo();
			if(ammo == AmmoStatus.AMMO) setAmmo(AmmoStatus.DEPLETED);
			boolean notAmmo = ammo == AmmoStatus.NOTAMMO;
			
			hitEntity.attackEntityFrom(new EntityDamageSourceIndirect("arrow", this, thrower).setProjectile(), notAmmo ? 2 : 8);
			
			if(hitEntity instanceof EntityLivingBase) {
				EntityLivingBase hitLiving = (EntityLivingBase) hitEntity;
				hitLiving.addPotionEffect(new PotionEffect(ModPotions.psishock, notAmmo ? 100 : 25));
			}
			
			if(hitEntity instanceof EntityEnderman) return;
		}
		
		posX = ray.hitVec.x;
		posY = ray.hitVec.y;
		posZ = ray.hitVec.z;
		ejectFacing = ray.sideHit;
		
		setDead();
	}
	
	EnumFacing ejectFacing = null;
	
	@Override
	public void setDead() {
		//TODO: It is definitely a bit weird to co-opt setDead for this purpose.
		//This will cause it to go off on like, /kill command. Is that not OK?
		super.setDead();
		
		playSound(PsiSoundHandler.compileError, 1, 1);
		
		if(world.isRemote) return;
		
		List<EntityLivingBase> nearbyLiving = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(new BlockPos(posX, posY, posZ)).grow(5), (living) -> 
			living != null && living != thrower && living.getPositionVector().squareDistanceTo(getPositionVector()) <= 25
		);
		
		List<EntityPlayer> nearbyPlayers = nearbyLiving.stream()
						.filter(e -> e instanceof EntityPlayer)
						.map(e -> (EntityPlayer) e)
						.filter(player -> !player.isPotionActive(ModPotions.psishock))
						.collect(Collectors.toList());
		
		AmmoStatus ammo = getAmmo();
		
		if(!nearbyLiving.isEmpty() || ammo != AmmoStatus.AMMO) {
			for(EntityLivingBase entity : nearbyLiving) {
				entity.attackEntityFrom(new EntityDamageSourceIndirect("arrow", this, thrower).setProjectile(), ammo == AmmoStatus.NOTAMMO ? 2 : 8);
			}
			
			for(EntityPlayer player : nearbyPlayers) {
				player.addPotionEffect(new PotionEffect(ModPotions.psishock, ammo == AmmoStatus.NOTAMMO ? 100 : 25));
			}
			
			//TODO: Sparkle sphere packet (used liblib's network channel)
			//PacketHandler.NETWORK.sendToAllAround(MessageSparkleSphere(positionVector, ammo), world, positionVector, 128.0)
		} else if(ammo == AmmoStatus.AMMO) {
			EntityItem itemEnt = new EntityItem(world, posX, posY, posZ, new ItemStack(ModItems.gaussBullet));
			Vector3 itemVelVec = new Vector3(motionX, motionY, motionZ).normalize().multiply(1 / 4.5d);
			if(ejectFacing == null) itemVelVec.multiply(-1);
			else {
				//Lovely!
				Vec3i facingVecCrappy = ejectFacing.getDirectionVec();
				Vector3 normal = new Vector3(facingVecCrappy.getX(), facingVecCrappy.getY(), facingVecCrappy.getZ());
				
				//TODO: This is sketchy.
				double doubleDot = 2 * itemVelVec.dotProduct(normal);
				Vector3 dotted = normal.copy().multiply(doubleDot);
				itemVelVec.sub(dotted).sub(normal.copy().multiply(.25));
			}
			
			itemEnt.motionX = itemVelVec.x;
			itemEnt.motionY = itemVelVec.y;
			itemEnt.motionZ = itemVelVec.z;
			itemEnt.setPickupDelay(40);
			world.spawnEntity(itemEnt);
		}
	}
	
	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.PLAYERS;
	}
	
	@Nullable
	@Override
	public EntityLivingBase getThrower() {
		EntityLivingBase superThrower = super.getThrower();
		if(superThrower != null) return superThrower;
		else if(uuid == null) return null;
		else return world.getPlayerEntityByUUID(uuid);
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0f;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		if(uuid != null) nbt.setUniqueId(TAG_CASTER, uuid);
		nbt.setInteger(TAG_AGE, age);
		nbt.setByte(TAG_AMMO, (byte) getAmmo().ordinal());
		//TODO Why does the original set the motionXYZ here? Doesn't Entity.writeToNBT do that?
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if(nbt.hasKey(TAG_CASTER)) {
			uuid = nbt.getUniqueId(TAG_CASTER);
		} else uuid = null;
		
		age = nbt.getInteger(TAG_AGE);
		
		byte ord = nbt.getByte(TAG_AMMO);
		if(ord >= AmmoStatus.values().length) setAmmo(AmmoStatus.NOTAMMO);
		else setAmmo(ord);
	}
	
	private void setAmmo(AmmoStatus toSet) {
		dataManager.set(AMMO_WATCHER, (byte) toSet.ordinal());
	}
	
	private void setAmmo(byte ord) {
		dataManager.set(AMMO_WATCHER, ord);
	}
	
	private AmmoStatus getAmmo() {
		byte ord = dataManager.get(AMMO_WATCHER);
		if(ord >= AmmoStatus.values().length) return AmmoStatus.NOTAMMO;
		else return AmmoStatus.values()[dataManager.get(AMMO_WATCHER)];
	}
	
	public static void createWatchers() {
		AMMO_WATCHER = EntityDataManager.createKey(EntityGaussPulse.class, DataSerializers.BYTE);
	}
	
	public enum AmmoStatus {
		NOTAMMO(ICADColorizer.DEFAULT_SPELL_COLOR),
		DEPLETED(0xB87333),
		AMMO(DEPLETED.color),
		BLOOD(0xFF0000),
		;
		
		public final int color;
		AmmoStatus(int color) {
			this.color = color;
		}
	}
}
