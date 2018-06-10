package wiresegal.psionup.common.spell.trick.botania

import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import vazkii.botania.api.mana.BurstProperties
import vazkii.botania.api.mana.ILens
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.api.sound.BotaniaSoundEvents
import vazkii.botania.common.entity.EntityManaBurst
import vazkii.botania.common.item.ItemManaGun
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.internal.Vector3
import vazkii.psi.api.spell.*
import vazkii.psi.api.spell.param.ParamVector
import wiresegal.psionup.api.enabling.ITrickEnablerComponent
import wiresegal.psionup.api.enabling.PieceComponentTrick
import wiresegal.psionup.api.enabling.botania.IBlasterComponent
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 9:01 AM on 4/1/16.
 */
class PieceTrickFormBurstKt(spell: Spell) : PieceComponentTrick(spell) {

    internal lateinit var position: SpellParam
    internal lateinit var ray: SpellParam

    override fun initParams() {
        position = ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false)
        ray = ParamVector("psi.spellparam.ray", SpellParam.GREEN, false, false)

        this.addParam(position)
        this.addParam(ray)
    }

    override fun addToMetadata(meta: SpellMetadata) {
        super.addToMetadata(meta)

        meta.addStat(EnumSpellStat.COST, 400)
        meta.addStat(EnumSpellStat.POTENCY, 150)
    }

    override fun acceptsPiece(component: ItemStack, cad: ItemStack, context: SpellContext, spell: Spell?, x: Int, y: Int): ITrickEnablerComponent.EnableResult? {
        if (component.item is IBlasterComponent)
            return ITrickEnablerComponent.EnableResult.fromBoolean(ManaItemHandler.requestManaExact(cad, context.caster, 120, true))
        return ITrickEnablerComponent.EnableResult.NOT_ENABLED
    }

    @Throws(SpellCompilationException::class)
    override fun executeIfAllowed(context: SpellContext): Any? {
        val posVec = getParamValue<Vector3>(context, position)
        val rayVec = getParamValue<Vector3>(context, ray)
        if (posVec == null || rayVec == null)
            throw SpellRuntimeException(SpellRuntimeException.NULL_VECTOR)
        else if (!context.isInRadius(posVec))
            throw SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS)
        val burst = getBurst(posVec, rayVec, context.caster.world, PsiAPI.getPlayerCAD(context.caster))
        if (!context.caster.world.isRemote) {
            context.caster.world.playSound(null, context.caster.posX, context.caster.posY, context.caster.posZ, BotaniaSoundEvents.manaBlaster, SoundCategory.PLAYERS, 0.6F, 1f)
            context.caster.world.spawnEntity(burst)
        }
        return null
    }

    fun getBurst(posVec: Vector3, rayVec: Vector3, world: World, cad: ItemStack): EntityManaBurst {
        val ray = rayVec.copy().normalize()

        val burst = EntityManaBurst(world)
        burst.setBurstSourceCoords(BlockPos(0, -1, 0))

        val yaw = -Math.atan2(ray.x, ray.z) * 180 / Math.PI - 180
        val pitch = Math.asin(ray.y) * 180 / Math.PI

        burst.setLocationAndAngles(posVec.x, posVec.y, posVec.z, yaw.toFloat() + 180, -pitch.toFloat())

        burst.posX -= MathHelper.cos(((yaw + 180) / 180.0F * Math.PI).toFloat()) * 0.16F
        burst.posY -= 0.1
        burst.posZ -= MathHelper.sin(((yaw + 180) / 180.0F * Math.PI).toFloat()) * 0.16F

        burst.setPosition(posVec.x, posVec.y, posVec.z)
        val f = 0.4
        val mx = MathHelper.sin((yaw / 180.0F * Math.PI).toFloat()) * MathHelper.cos((pitch / 180.0F * Math.PI).toFloat()) * f / 2
        val mz = -(MathHelper.cos((yaw / 180.0F * Math.PI).toFloat()) * MathHelper.cos((pitch / 180.0F * Math.PI).toFloat()) * f) / 2
        val my = MathHelper.sin((pitch / 180.0F * Math.PI).toFloat()) * f / 2
        burst.setMotion(mx, my, mz)

        val maxMana = 120
        val color = (cad.item as ICAD).getSpellColor(cad) + 0xf000000
        val ticksBeforeManaLoss = 60
        val manaLossPerTick = 4F
        val motionModifier = 5F
        val gravity = 0F
        val props = BurstProperties(maxMana, ticksBeforeManaLoss, manaLossPerTick, gravity, motionModifier, color)

        val lens = ItemManaGun.getLens(cad)
        if (lens != null && lens.item is ILens)
            (lens.item as ILens).apply(lens, props)


        burst.sourceLens = lens

        burst.color = props.color

        burst.mana = props.maxMana
        burst.startingMana = props.maxMana
        burst.minManaLoss = props.ticksBeforeManaLoss
        burst.manaLossPerTick = props.manaLossPerTick
        burst.gravity = props.gravity
        burst.setMotion(burst.motionX * props.motionModifier, burst.motionY * props.motionModifier, burst.motionZ * props.motionModifier)

        return burst
    }

    override fun requiredObjects(): Array<String> {
        return arrayOf("${LibMisc.MOD_ID}.requirement.form_burst")
    }
}
