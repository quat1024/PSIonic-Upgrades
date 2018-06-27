package wiresegal.psionup.common.gui.cadcase

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.SlotItemHandler
import vazkii.psi.api.cad.ICAD
import vazkii.psi.api.cad.ISocketable

class ContainerCADCaseKt(player: EntityPlayer, val stack: ItemStack) : Container() {
    val inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)

    private val slot0: Slot
    private val slot1: Slot

    init {
        val playerInventory = player.inventory

        slot0 = addSlotToContainer(SlotItemHandler(inventory, 0, 132, 7))


        slot1 = addSlotToContainer(SlotItemHandler(inventory, 1, 79, 7))

        val xs = 34
        val ys = 48

        for (i in 0..2)
            for (j in 0..8)
                addSlotToContainer(Slot(playerInventory, j + i * 9 + 9, xs + j * 18, ys + i * 18))

        for (k in 0..8)
            addSlotToContainer(object : Slot(playerInventory, k, xs + k * 18, ys + 58) {

                override fun canTakeStack(playerIn: EntityPlayer?): Boolean {
                    return stack.item != this@ContainerCADCase.stack.item
                }
            })

        for (k in 0..3) {
            val slot = equipmentSlots[k]

            addSlotToContainer(object : Slot(playerInventory, playerInventory.sizeInventory - 2 - k, xs - 27, ys + 18 * k) {

                override fun getSlotStackLimit(): Int {
                    return 1
                }

                override fun isItemValid(stack: ItemStack): Boolean {
                    return !stack.isEmpty && stack.item.isValidArmor(stack, slot, player)
                }

                @SideOnly(Side.CLIENT)
                override fun getSlotTexture(): String? {
                    return ItemArmor.EMPTY_SLOT_NAMES[slot.index]
                }
            })
        }

        addSlotToContainer(object : Slot(playerInventory, playerInventory.sizeInventory - 1, 205, 48) {

            @SideOnly(Side.CLIENT)
            override fun getSlotTexture(): String? {
                return "minecraft:items/empty_armor_slot_shield"
            }

            override fun canTakeStack(playerIn: EntityPlayer?): Boolean {
                return stack.item != this@ContainerCADCase.stack.item
            }
        })
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return true
    }

    override fun transferStackInSlot(playerIn: EntityPlayer?, index: Int): ItemStack {
        var itemstack: ItemStack = ItemStack.EMPTY
        val slot = inventorySlots[index]

        if (slot != null && slot.hasStack) {
            val itemstack1 = slot.stack
            itemstack = itemstack1.copy()

            val invStart = 1
            val hotbarStart = invStart + 28
            val invEnd = hotbarStart + 9

            if (index > invStart) {
                if (slot0.isItemValid(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                        return ItemStack.EMPTY // Inventory -> CAD slot
                } else if (slot1.isItemValid(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                        return ItemStack.EMPTY // Inventory -> Socket Slot
                }
            } else if (itemstack1.item is ItemArmor) {
                val armor = itemstack1.item as ItemArmor
                val armorSlot = 3 - armor.armorType.index
                if (!mergeItemStack(itemstack1, invEnd + armorSlot, invEnd + armorSlot + 1, true) && !mergeItemStack(itemstack1, invStart, invEnd, true))
                    return ItemStack.EMPTY // Assembler -> Armor+Inv+Hotbar

            } else if (!mergeItemStack(itemstack1, invStart, invEnd, true))
                return ItemStack.EMPTY // Assembler -> Inv+hotbar

            slot.onSlotChanged()

            if (itemstack1.isEmpty) slot.putStack(ItemStack.EMPTY)
            else if (itemstack1.count == itemstack.count)
                return ItemStack.EMPTY

            slot.onTake(playerIn, itemstack1)
        }

        return itemstack
    }

    companion object {

        private val equipmentSlots = arrayOf(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET)
    }

}
