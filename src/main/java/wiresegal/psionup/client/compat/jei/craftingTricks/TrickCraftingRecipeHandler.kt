package wiresegal.psionup.client.compat.jei.craftingTricks

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper

class TrickCraftingRecipeHandler : IRecipeHandler<TrickCraftingRecipeJEI> {
    override fun getRecipeClass(): Class<TrickCraftingRecipeJEI> {
        return TrickCraftingRecipeJEI::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return "psionup:trickCrafting"
    }

    override fun getRecipeWrapper(recipe: TrickCraftingRecipeJEI): IRecipeWrapper {
        return recipe
    }

    override fun isRecipeValid(recipe: TrickCraftingRecipeJEI): Boolean {
        return recipe.inputs.size > 1 && recipe.outputs.size > 0
    }
}
