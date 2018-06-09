package wiresegal.psionup.common.block

import com.teamwizardry.librarianlib.features.base.block.BlockMod
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.features.base.block.ItemModBlock
import com.teamwizardry.librarianlib.features.base.item.IGlowingItem
import net.minecraft.block.SoundType
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.psionup.common.lib.LibNames

/**
 * @author WireSegal
 * Created at 9:56 PM on 7/4/16.
 */
class BlockPlateKt(name: String) : BlockMod(name, Material.IRON, *makeVariants(name)), IBlockColorProvider {
    companion object {
        fun makeVariants(name: String): Array<String> {
            return Array(16) {
                name + LibNames.Colors[it]
            }
        }

        val COLOR: PropertyEnum<EnumDyeColor> = PropertyEnum.create("color", EnumDyeColor::class.java)
    }

    override fun createItemForm(): ItemBlock? {
        return object : ItemModBlock(this), IGlowingItem {
            @SideOnly(Side.CLIENT)
            override fun transformToGlow(itemStack: ItemStack, model: IBakedModel): IBakedModel? {
                return IGlowingItem.Helper.wrapperBake(model, false, 1)
            }
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer? {
        return BlockRenderLayer.CUTOUT
    }

    init {
        setLightLevel(1f)
        this.setHardness(5.0f)
        this.setResistance(10.0f)
        this.soundType = SoundType.METAL
    }

    override fun createBlockState(): BlockStateContainer? {
        return BlockStateContainer(this, COLOR)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(COLOR).metadata
    }

    override fun getStateFromMeta(meta: Int): IBlockState? {
        return defaultState.withProperty(COLOR, EnumDyeColor.byMetadata(meta))
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun getMapColor(state: IBlockState): MapColor {
        return state.getValue(COLOR).mapColor
    }

    override val blockColorFunction: ((state: IBlockState, world: IBlockAccess?, pos: BlockPos?, tintIndex: Int) -> Int)?
        get() = { iBlockState, _, _, i -> if (i == 1) iBlockState.getValue(COLOR).mapColor.colorValue else 0xFFFFFF }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() = { itemStack, _ -> EnumDyeColor.byMetadata(itemStack.itemDamage).mapColor.colorValue }

}
