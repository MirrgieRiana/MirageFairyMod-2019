package miragefairy2019.jei

import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry
import mezz.jei.api.JEIPlugin
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeCategoryRegistration
import mezz.jei.api.recipe.IRecipeWrapper
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.client.Minecraft
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreIngredient

@JEIPlugin
class PluginMfa : IModPlugin {
    companion object {
        const val uid = "miragefairy2019.mfa"
    }

    override fun registerCategories(registry: IRecipeCategoryRegistration) {
        registry.addRecipeCategories(object : IRecipeCategory<IRecipeWrapper> {
            override fun getUid() = Companion.uid
            override fun getTitle() = "MFA"
            override fun getModName() = "MirageFairy2019"
            override fun getBackground() = object : IDrawable {
                override fun getWidth() = 160
                override fun getHeight() = 120
                override fun draw(minecraft: Minecraft, xOffset: Int, yOffset: Int) {
                    JeiUtilities.drawSlot(71f, 1f)
                }
            }

            override fun getIcon(): IDrawable? = registry.jeiHelpers.guiHelper.createDrawableIngredient(ItemStack(Items.WRITTEN_BOOK))
            override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: IRecipeWrapper, ingredients: IIngredients) {
                recipeLayout.itemStacks.init(0, true, 71, 1)
                recipeLayout.itemStacks.set(ingredients)
            }
        })
    }

    override fun register(registry: IModRegistry) {
        registry.addRecipes(mutableListOf<IRecipeWrapper>().apply {
            fun register(itemStacks: List<ItemStack>, content: String) = add(object : IRecipeWrapper {
                override fun getIngredients(ingredients: IIngredients) = ingredients.setInputLists(VanillaTypes.ITEM, listOf(itemStacks))
                override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
                    content.trimIndent().split("\n").forEachIndexed { i, it -> minecraft.fontRenderer.drawString(it, 4, 20 + 10 * i, 0x444444) }
                }
            })

            fun oreName(oreName: String, content: () -> String) = register(OreIngredient(oreName).matchingStacks.toList(), content())
            fun item(registerName: String, content: () -> String) = register(listOf(ItemStack(Item.getByNameOrId("${ModMirageFairy2019.MODID}:$registerName")!!)), content())

            oreName("mirageFairy2019TwinkleStone") {
                """
                【MFA-64897357：蛍】
                ｿﾃﾞｨｱ「ほら」
                
                「綺麗だな…」
                「虫みたいだ」
                
                ｿﾃﾞｨｱ「……ふふ、意外」
                「…えっ？」
                
                ｿﾃﾞｨｱ「星みたいって言うかなって」
                """
            }
            oreName("mirageFairy2019FairyMagentaglazedterracottaRank1") {
                """
                【MFA-34681526：美術展にて】
                ﾂｧｯﾛｰﾁｬ「大変！ﾃﾞｨｽﾍﾟﾝｾｰﾘｬが！」
                
                ﾃﾞｨｽﾍﾟﾝｾｰﾘｬ（……）
                
                ﾘﾗｰｷｬ「止まって！こっちじゃないの！」
                
                ﾃﾞｨｽﾍﾟﾝｾｰﾘｬ（……）
                
                （？）
                """
            }
            item("miragium_axe") {
                """
                【MFA-16487544：落とし物】
                （……）
                
                ﾙﾒﾘ「待って！」
                
                ﾙﾒﾘ「妖精さん！！」
                ﾙﾒﾘ「待って！」
                （……）
                
                ﾙﾒﾘ「あっ！」
                """
            }
            item("dish") {
                """
                【MFA-46805554：クリームまみれ】
                N-111615「そこに乗っちゃだめだよ！」
                （…？）
                
                （…………）
                N-111615「もう！」
                
                N-111615「……」
                
                N-111615「……そこが好きなの？」
                """
            }
        }, uid)
    }
}
