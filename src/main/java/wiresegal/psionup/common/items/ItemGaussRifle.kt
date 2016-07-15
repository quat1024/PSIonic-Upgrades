package wiresegal.psionup.common.items

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.core.handler.PsiSoundHandler
import wiresegal.psionup.client.core.handler.ModelHandler
import wiresegal.psionup.common.entity.EntityGaussPulse
import wiresegal.psionup.common.items.base.ItemMod

/**
 * @author WireSegal
 * Created at 10:10 PM on 7/13/16.
 */
class ItemGaussRifle(name: String) : ItemMod(name), ModelHandler.IItemColorProvider {
    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val data = PlayerDataHandler.get(playerIn)
        val ammo = findAmmo(playerIn)
        if (ammo != null || data.availablePsi > 0 || playerIn.capabilities.isCreativeMode) {
            if (!playerIn.capabilities.isCreativeMode) {
                if (ammo == null)
                    data.deductPsi(1000, 100, true)
                else {
                    ammo.stackSize--
                    if (ammo.stackSize == 0)
                        playerIn.inventory.deleteStack(ammo)
                }
            }

            playerIn.swingArm(hand)

            val status = if (ammo != null) {
                if (playerIn.capabilities.isCreativeMode)
                    EntityGaussPulse.AmmoStatus.DEPLETED
                else
                    EntityGaussPulse.AmmoStatus.AMMO
            } else
                EntityGaussPulse.AmmoStatus.NOTAMMO

            val proj = EntityGaussPulse(worldIn, playerIn, status)
            if (!worldIn.isRemote) worldIn.spawnEntityInWorld(proj)
            val look = playerIn.lookVec
            if (look.xCoord != 0.0 && look.zCoord != 0.0)
                playerIn.knockBack(playerIn, 0.5f, look.xCoord, look.zCoord)
            else
                playerIn.motionY += 0.5
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 1f, 1f)

            if (ammo != null && !playerIn.capabilities.isCreativeMode)
                playerIn.cooldownTracker.setCooldown(this, 50)
        }
        return ActionResult(EnumActionResult.SUCCESS, itemStackIn)
    }

    private fun findAmmo(player: EntityPlayer): ItemStack? {
        if (player.heldItemOffhand?.item == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.OFF_HAND)
        } else if (player.heldItemMainhand?.item == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.MAIN_HAND)
        } else {
            for (i in 0..player.inventory.sizeInventory - 1) {
                val itemstack = player.inventory.getStackInSlot(i)

                if (itemstack?.item == ModItems.gaussBullet) {
                    return itemstack
                }
            }

            return null
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor {
            itemStack, i ->
            if (i == 0)
                pulseColor(0xB87333)
            else if (i == 1)
                ICADColorizer.DEFAULT_SPELL_COLOR
            else 0xFFFFFF
        }
    }

    fun pulseColor(rgb: Int): Int {
        val add = (Math.sin(ClientTickHandler.ticksInGame * 0.2) * 24).toInt()
        val r = (rgb and (0xFF shl 16)) shr 16
        val b = (rgb and (0xFF shl 8)) shr 8
        val g = (rgb and (0xFF shl 0)) shr 0
        return (Math.max(Math.min(r + add, 255), 0) shl 16) or
               (Math.max(Math.min(b + add, 255), 0) shl 8) or
               (Math.max(Math.min(g + add, 255), 0) shl 0)
    }
}