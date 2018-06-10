package wiresegal.psionup.common.entity

import com.teamwizardry.librarianlib.core.LibrarianLib
import com.teamwizardry.librarianlib.features.kotlin.*
import com.teamwizardry.librarianlib.features.network.PacketHandler
import com.teamwizardry.librarianlib.features.network.sendToAllAround
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.datasync.DataParameter
import net.minecraft.network.datasync.DataSerializers
import net.minecraft.network.datasync.EntityDataManager
import net.minecraft.potion.PotionEffect
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.util.EnumFacing
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.api.internal.Vector3
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PsiSoundHandler
import wiresegal.psionup.common.effect.ModPotions
import wiresegal.psionup.common.items.ModItems
import wiresegal.psionup.common.network.MessageSparkleSphere
import java.awt.Color
import java.util.*

open class EntityGaussPulseKt : EntityThrowable {
    var timeAlive: Int = 0
    var uuid: UUID? = null
    var ammo: AmmoStatus
        get() = AmmoStatus.values()[dataManager[AMMO_STATUS] % AmmoStatus.values().size]
        set(value) = dataManager.set(AMMO_STATUS, value.ordinal.toByte())

    constructor(worldIn: World) : super(worldIn) {
        this.setSize(0.0f, 0.0f)
    }

    constructor(worldIn: World, throwerIn: EntityLivingBase, ammo: AmmoStatus) : super(worldIn, throwerIn) {
        this.shoot(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0f, 4.5f, 0.0f)
        uuid = throwerIn.uniqueID
        this.ammo = ammo

        this.motionX -= throwerIn.motionX
        this.motionZ -= throwerIn.motionZ

        if (!throwerIn.onGround)
            this.motionY -= throwerIn.motionY
    }

    override fun entityInit() {
        this.dataManager.register(AMMO_STATUS, AmmoStatus.NOTAMMO.ordinal.toByte())
    }

    override fun writeEntityToNBT(tagCompound: NBTTagCompound) {
        super.writeEntityToNBT(tagCompound)
        if (uuid != null)
            tagCompound.setUniqueId(TAG_CASTER, uuid)
        tagCompound.setInteger(TAG_TIME_ALIVE, this.timeAlive)
        tagCompound.setDouble(TAG_LAST_MOTION_X, this.motionX)
        tagCompound.setDouble(TAG_LAST_MOTION_Y, this.motionY)
        tagCompound.setDouble(TAG_LAST_MOTION_Z, this.motionZ)
        tagCompound.setInteger(TAG_AMMO, this.ammo.ordinal)
    }

    override fun readEntityFromNBT(tagCompound: NBTTagCompound) {
        this.uuid = tagCompound.getUniqueId(TAG_CASTER)
        if (uuid!!.leastSignificantBits == 0L && uuid!!.mostSignificantBits == 0L) uuid = null
        this.timeAlive = tagCompound.getInteger(TAG_TIME_ALIVE)
        this.motionX = tagCompound.getDouble(TAG_LAST_MOTION_X)
        this.motionY = tagCompound.getDouble(TAG_LAST_MOTION_Y)
        this.motionZ = tagCompound.getDouble(TAG_LAST_MOTION_Z)
        this.ammo = AmmoStatus.values()[tagCompound.getInteger(TAG_AMMO) % AmmoStatus.values().size]
    }

    override fun onUpdate() {
        super.onUpdate()
        var timeAlive = this.ticksExisted

        val granularity = 1
        if (timeAlive > this.liveTime ||
                (prevPosX - posX) * (prevPosX - posX) +
                        (prevPosY - posY) * (prevPosY - posY) +
                        (prevPosZ - posZ) * (prevPosZ - posZ) < granularity)
            this.setDead()


        ++timeAlive

        val color = Color(ammo.color)
        val r = color.red.toFloat() / 255.0f
        val g = color.green.toFloat() / 255.0f
        val b = color.blue.toFloat() / 255.0f
        val x = this.posX
        val y = this.posY
        val z = this.posZ
        val lookOrig = Vector3(this.motionX, this.motionY, this.motionZ).normalize()

        for (i in 0..this.particleCount - 1) {
            val look = lookOrig.copy()
            val spread = 0.6
            val dist = 0.3
            look.x += (Math.random() - 0.5) * spread
            look.y += (Math.random() - 0.5) * spread
            look.z += (Math.random() - 0.5) * spread
            look.normalize().multiply(dist)
            Psi.proxy.sparkleFX(this.world, x, y, z, r, g, b, look.x.toFloat(), look.y.toFloat(), look.z.toFloat(), 1.2f, 12)
        }

    }

    open val liveTime: Int
        get() = 600

    open val particleCount: Int
        get() = 5

    override fun onImpact(pos: RayTraceResult) {
        if (world.isRemote) return

        val entity = pos.entityHit
        if (entity != null) {
            if (entity.cachedUniqueIdString == thrower?.cachedUniqueIdString) return

            if (ammo == AmmoStatus.AMMO)
                ammo = AmmoStatus.DEPLETED

            entity.attackEntityFrom(EntityDamageSourceIndirect("arrow", this, thrower).setProjectile(), if (ammo == AmmoStatus.NOTAMMO) 2f else 8f)
            if (entity is EntityLivingBase)
                entity.addPotionEffect(PotionEffect(ModPotions.psishock, if (ammo == AmmoStatus.NOTAMMO) 100 else 25))

            if (entity is EntityEnderman) return
        }

        posX = pos.hitVec.xCoord
        posY = pos.hitVec.yCoord
        posZ = pos.hitVec.zCoord
        ejectFacing = pos.sideHit

        setDead()
    }

    private var ejectFacing: EnumFacing? = null

    override fun setDead() {
        super.setDead()

        playSound(PsiSoundHandler.compileError, 1f, 1f)

        if (!world.isRemote) {
            val regularEntities = world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(posX - 5, posY - 5, posZ - 5, posX + 5, posY + 5, posZ + 5)) {
                (it?.positionVector?.squareDistanceTo(positionVector) ?: 50.0) <= 25 && it != thrower
            }

            val players = regularEntities
                    .filterIsInstance<EntityPlayer>()
                    .filterNot { it.isPotionActive(ModPotions.psishock) }

            if (regularEntities.isNotEmpty() || ammo != AmmoStatus.AMMO) {
                for (entity in players)
                    entity.addPotionEffect(PotionEffect(ModPotions.psishock, if (ammo == AmmoStatus.NOTAMMO) 100 else 25))
                for (entity in regularEntities)
                    entity.attackEntityFrom(EntityDamageSourceIndirect("arrow", this, thrower).setProjectile(), if (ammo == AmmoStatus.NOTAMMO) 2f else 8f)
                PacketHandler.NETWORK.sendToAllAround(MessageSparkleSphere(positionVector, ammo), world, positionVector, 128.0)
            } else if (ammo == AmmoStatus.AMMO) {
                val item = EntityItem(world, posX, posY, posZ, ItemStack(ModItems.gaussBullet))
                var vec = motionVec.normalize() / 4.5f
                val eject = ejectFacing
                if (eject != null) {
                    val normal = Vec3d(eject.directionVec)
                    vec -= normal * (2 * (vec dot normal)) - (normal * 0.25)
                } else
                    vec *= -1
                item.motionX = vec.xCoord
                item.motionY = vec.yCoord
                item.motionZ = vec.zCoord
                item.setPickupDelay(40)
                world.spawnEntity(item)
            }
        }
    }

    override fun getSoundCategory(): SoundCategory {
        return SoundCategory.PLAYERS
    }

    override fun getThrower(): EntityLivingBase? {
        val superThrower = super.getThrower()
        if (superThrower != null) {
            return superThrower
        } else {
            if (uuid == null) return null
            val player = this.world.getPlayerEntityByUUID(uuid)
            return player
        }
    }

    override fun getGravityVelocity(): Float {
        return 0.0f
    }

    companion object {
        private val TAG_CASTER = "caster"
        private val TAG_TIME_ALIVE = "timeAlive"
        private val TAG_LAST_MOTION_X = "lastMotionX"
        private val TAG_LAST_MOTION_Y = "lastMotionY"
        private val TAG_LAST_MOTION_Z = "lastMotionZ"
        private val TAG_AMMO = "ammo"

        val AMMO_STATUS: DataParameter<Byte> = EntityDataManager.createKey(EntityGaussPulse::class.java, DataSerializers.BYTE)

    }

    enum class AmmoStatus(val color: Int) {
        NOTAMMO(ICADColorizer.DEFAULT_SPELL_COLOR),
        DEPLETED(0xB87333),
        AMMO(DEPLETED.color),
        BLOOD(0xFF0000)
    }
}
