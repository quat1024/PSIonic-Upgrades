package wiresegal.psionup.common.core

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import wiresegal.psionup.common.items.ModItems

/**
 * @author WireSegal
 * Created at 1:17 PM on 3/20/16.
 */
class CreativeTab : CreativeTabs("psionup") {
    internal lateinit var list: List<ItemStack>

    init {
        this.setNoTitle()
        this.backgroundImageName = "psionup.png"
    }

    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.liquidColorizer)
    }

    override fun getTabIconItem(): Item {
        return this.iconItemStack.item
    }

    override fun hasSearchBar(): Boolean {
        return true
    }

    override fun displayAllRelevantItems(p_78018_1_: List<ItemStack>) {
        this.list = p_78018_1_
        this.addItem(ModItems.emptyColorizer)
        this.addItem(ModItems.liquidColorizer)
        this.addItem(ModItems.fakeCAD)
        this.addItem(ModItems.magazine)
        this.addItem(ModItems.socket)
    }

    private fun addItem(item: Item) {
        item.getSubItems(item, this, this.list)
    }

    private fun addBlock(block: Block) {
        this.addItem(Item.getItemFromBlock(block))
    }

    companion object {
        var INSTANCE = CreativeTab()
    }
}

