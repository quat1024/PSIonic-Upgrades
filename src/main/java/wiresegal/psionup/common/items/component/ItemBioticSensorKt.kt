package wiresegal.psionup.common.items.component

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import vazkii.psi.api.exosuit.IExosuitSensor
import vazkii.psi.api.exosuit.PsiArmorEvent
import vazkii.psi.api.internal.MathHelper
import vazkii.psi.client.core.handler.ClientTickHandler
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 5:44 PM on 7/13/16.
 */
class ItemBioticSensorKt(name: String) : ItemMod(name), IExosuitSensor, IItemColorProvider {

    companion object {

        val EVENT_BIOTIC = "${LibMisc.MOD_ID}.event.nearby_entities"

        val triggeredBioticsNonremote = hashMapOf<EntityPlayer, MutableList<EntityLivingBase>>()
        val triggeredBioticsRemote = hashMapOf<EntityPlayer, MutableList<EntityLivingBase>>()

        fun biotics(remote: Boolean) = if (remote) triggeredBioticsRemote else triggeredBioticsNonremote

        init {
            MinecraftForge.EVENT_BUS.register(this)
        }

        @SubscribeEvent
        fun onPlayerTick(e: LivingEvent.LivingUpdateEvent) {
            val range = 10.0

            val player = e.entityLiving
            if (player is EntityPlayer) {
                val triggeredBiotics = biotics(player.world.isRemote)
                var triggered = triggeredBiotics[player]
                if (triggered == null) {
                    triggered = mutableListOf()
                    triggeredBiotics.put(player, triggered)
                }

                val found = mutableListOf<EntityLivingBase>()
                val entities = player.world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(-range, -range, -range, range, range, range).offset(player.posX, player.posY, player.posZ))
                for (entity in entities) if (entity != player && MathHelper.pointDistanceSpace(player.posX, player.posY, player.posZ, entity.posX, entity.posY, entity.posZ) <= range) {
                    if (!triggered.contains(entity))
                        PsiArmorEvent.post(PsiArmorEvent(player, EVENT_BIOTIC, 0.0, entity))
                    found.add(entity)
                }

                triggered.clear()
                for (i in found) triggered.add(i)
            }

        }
    }

    init {
        setMaxStackSize(1)
    }

    override fun getColor(p0: ItemStack): Int {
        val add = Math.max((Math.sin(ClientTickHandler.ticksInGame * 0.1) * 96).toInt(), 0)
        val newColor =
                (add shl 16) or
                        (add shl 8) or
                        (add shl 0)
        return newColor
    }

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = { itemStack, i -> if (i == 1) getColor(itemStack) else 0xFFFFFF }

    override fun getEventType(p0: ItemStack): String {
        return EVENT_BIOTIC
    }
}
