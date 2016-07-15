package wiresegal.psionup.common.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EntityDamageSourceIndirect
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.SpellContext
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PsiSoundHandler
import vazkii.psi.common.entity.EntitySpellGrenade
import wiresegal.psionup.common.effect.ModPotions
import wiresegal.psionup.common.effect.PotionPsishock
import wiresegal.psionup.common.effect.base.ModPotionEffect
import wiresegal.psionup.common.items.ModItems
import java.awt.Color
import java.util.*

open class EntityGaussPulse : EntityThrowable {
    var timeAlive: Int = 0
    var uuid: UUID? = null
    var ammo: AmmoStatus = AmmoStatus.NOTAMMO

    constructor(worldIn: World) : super(worldIn) {
        this.setSize(0.0f, 0.0f)
    }

    constructor(worldIn: World, throwerIn: EntityLivingBase, ammo: AmmoStatus) : super(worldIn, throwerIn) {
        this.setHeadingFromThrower(throwerIn, throwerIn.rotationPitch, throwerIn.rotationYaw, 0.0f, 1.5f, 1.0f)
        val speed = 3
        uuid = throwerIn.uniqueID
        this.ammo = ammo
        this.motionX *= speed
        this.motionY *= speed
        this.motionZ *= speed
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

        val granularity = 50
        if (timeAlive > this.liveTime || (Math.round(prevPosX * granularity) == Math.round(posX * granularity) &&
                Math.round(prevPosY * granularity) == Math.round(posY * granularity) &&
                Math.round(prevPosZ * granularity) == Math.round(posZ * granularity)))
            this.setDead()


        ++timeAlive

        val colorVal = ICADColorizer.DEFAULT_SPELL_COLOR

        val color = Color(colorVal)
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
            Psi.proxy.sparkleFX(this.worldObj, x, y, z, r, g, b, look.x.toFloat(), look.y.toFloat(), look.z.toFloat(), 1.2f, 12)
        }

    }

    open val liveTime: Int
        get() = 600

    open val particleCount: Int
        get() = 5

    override fun onImpact(pos: RayTraceResult) {
        val entity = pos.entityHit
        if (entity != null && entity is EntityLivingBase) {
            if (entity == thrower) return
            entity.addPotionEffect(ModPotionEffect(ModPotions.psishock, if (ammo == AmmoStatus.NOTAMMO) 100 else 25))
            entity.attackEntityFrom(EntityDamageSourceIndirect("arrow", this, thrower).setProjectile(), if (ammo == AmmoStatus.NOTAMMO) 2f else 8f)
            if (entity is EntityEnderman) return
            if (ammo == AmmoStatus.AMMO)
                ammo = AmmoStatus.DEPLETED
        }
        setDead()
    }

    override fun setDead() {
        super.setDead()

        playSound(PsiSoundHandler.compileError, 1f, 1f)

        if (!worldObj.isRemote && ammo == AmmoStatus.AMMO) {
            val item = EntityItem(worldObj, posX, posY, posZ, ItemStack(ModItems.gaussBullet))
            item.motionX = 0.0
            item.motionY = 0.0
            item.motionZ = 0.0
            item.setPickupDelay(40)
            worldObj.spawnEntityInWorld(item)
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
            val player = this.worldObj.getPlayerEntityByUUID(uuid)
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
    }

    enum class AmmoStatus {
        NOTAMMO, DEPLETED, AMMO
    }
}