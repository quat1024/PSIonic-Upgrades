package wiresegal.psionup.common.items

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.features.base.item.ItemMod
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.util.math.MathHelper
import vazkii.psi.api.PsiAPI
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ICADColorizer
import vazkii.psi.client.core.handler.ClientTickHandler
import vazkii.psi.common.Psi
import vazkii.psi.common.core.handler.PlayerDataHandler
import vazkii.psi.common.core.handler.PsiSoundHandler
import vazkii.psi.common.item.base.ModItems.cadBattery
import wiresegal.psionup.common.core.helper.FlowColors
import wiresegal.psionup.common.entity.EntityGaussPulse
import wiresegal.psionup.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 10:10 PM on 7/13/16.
 */
class ItemGaussRifleKt(name: String) : ItemMod(name), IItemColorProvider, IGlowingItem, FlowColors.IAcceptor {

    init {
        setMaxStackSize(1)
    }

    @SideOnly(Side.CLIENT)
    override fun transformToGlow(itemStack: ItemStack, model: IBakedModel): IBakedModel? {
        return IGlowingItem.Helper.wrapperBake(model, false, 0, 1)
    }

    override val itemColorFunction: ((stack: ItemStack, tintIndex: Int) -> Int)?
        get() = {
            stack, i ->
            if (i == 0)
                pulseColor(0xB87333)
            else if (i == 1) {
                val colorizer = FlowColors.getColor(stack)
                if (colorizer.isEmpty) 0 else Psi.proxy.getColorizerColor(colorizer).rgb
            } else 0xFFFFFF
        }

    override fun onItemRightClick(worldIn: World, playerIn: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val data = PlayerDataHandler.get(playerIn)
        val ammo = findAmmo(playerIn)
        val cad = PsiAPI.getPlayerCAD(playerIn)
        if (cad.isEmpty) return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand))

        if (playerIn.capabilities.isCreativeMode || data.availablePsi > 0 || (!ammo.isEmpty && data.availablePsi > 0)) {
            val wasEmpty = ammo.isEmpty
            var noneLeft = false

            if (!playerIn.capabilities.isCreativeMode) {
                if (ammo.isEmpty) {
                    val cadBattery = if (cad.isEmpty) 0 else (cad.item as ICAD).getStoredPsi(cad)
                    if (data.availablePsi + cadBattery < 625) noneLeft = true
                    data.deductPsi(625, (3 * playerIn.cooldownPeriod).toInt() + 10 + if (noneLeft) 50 else 0, true)
                } else {
                    data.deductPsi(200, 10, true)
                    ammo.count--
                }
            }

            playerIn.swingArm(hand)

            val status = if (!wasEmpty) {
                if (playerIn.capabilities.isCreativeMode)
                    EntityGaussPulse.AmmoStatus.DEPLETED
                else
                    EntityGaussPulse.AmmoStatus.AMMO
            } else if (noneLeft)
                EntityGaussPulse.AmmoStatus.BLOOD
            else
                EntityGaussPulse.AmmoStatus.NOTAMMO

            val proj = EntityGaussPulse(worldIn, playerIn, status)
            if (!worldIn.isRemote) worldIn.spawnEntity(proj)
            val look = playerIn.lookVec
            playerIn.motionX -= 0.5 * look.xCoord
            playerIn.motionY -= 0.25 * look.yCoord
            playerIn.motionZ -= 0.5 * look.zCoord
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ,
                    if (status == EntityGaussPulse.AmmoStatus.BLOOD) PsiSoundHandler.compileError else PsiSoundHandler.cadShoot,
                    SoundCategory.PLAYERS, 1f, 1f)

            if (!playerIn.capabilities.isCreativeMode)
                playerIn.cooldownTracker.setCooldown(this, (3 * playerIn.cooldownPeriod).toInt())
        }
        return ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand))
    }

    private fun findAmmo(player: EntityPlayer): ItemStack {
        if (player.heldItemOffhand?.item == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.OFF_HAND)
        } else if (player.heldItemMainhand?.item == ModItems.gaussBullet) {
            return player.getHeldItem(EnumHand.MAIN_HAND)
        } else {
            (0..player.inventory.sizeInventory - 1)
                    .map { player.inventory.getStackInSlot(it) }
                    .filter { !it.isEmpty && it.item == ModItems.gaussBullet }
                    .forEach { return it }

            return ItemStack.EMPTY
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
