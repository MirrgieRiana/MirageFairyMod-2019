package miragefairy2019.mod.modules.ore

import miragefairy2019.libkt.DataItemModel
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.MakeItemVariantModelScope
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.generated
import miragefairy2019.libkt.handheld
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeItemVariantModel
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.lib.ItemMultiMaterial
import miragefairy2019.mod.lib.ItemVariantMaterial
import miragefairy2019.mod.lib.setCustomModelResourceLocations
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.Fluid

object Ore {
    val module: Module = {

        // マテリアルアイテム
        item({ ItemSimpleMaterials() }, "materials") {
            setUnlocalizedName("materials")
            setCreativeTab { ApiMain.creativeTab }

            fun r(
                metadata: Int,
                registryName: String,
                unlocalizedName: String,
                oreName: String,
                modelSupplier: MakeItemVariantModelScope<ItemSimpleMaterials, ItemVariantSimpleMaterials>.() -> DataItemModel
            ) = itemVariant(registryName, { ItemVariantSimpleMaterials(it, unlocalizedName) }, metadata) {
                addOreName(oreName)
                makeItemVariantModel { modelSupplier() }
            }

            fun ItemVariantInitializer<ItemSimpleMaterials, ItemVariantSimpleMaterials>.fuel(burnTime: Int) = also { itemInitializer.modInitializer.onRegisterItem { itemVariant.burnTime = burnTime } }

            r(0, "apatite_gem", "gemApatite", "gemApatite", { generated })
            r(1, "fluorite_gem", "gemFluorite", "gemFluorite", { generated })
            r(2, "sulfur_gem", "gemSulfur", "gemSulfur", { generated })
            r(3, "miragium_dust", "dustMiragium", "dustMiragium", { generated })
            r(4, "miragium_tiny_dust", "dustTinyMiragium", "dustTinyMiragium", { generated })
            r(5, "miragium_ingot", "ingotMiragium", "ingotMiragium", { generated })
            r(6, "cinnabar_gem", "gemCinnabar", "gemCinnabar", { generated })
            r(7, "moonstone_gem", "gemMoonstone", "gemMoonstone", { generated })
            r(8, "magnetite_gem", "gemMagnetite", "gemMagnetite", { generated })
            r(9, "saltpeter_gem", "gemSaltpeter", "gemSaltpeter", { generated })
            r(10, "pyrope_gem", "gemPyrope", "gemPyrope", { generated })
            r(11, "smithsonite_gem", "gemSmithsonite", "gemSmithsonite", { generated })
            r(12, "miragium_rod", "rodMiragium", "rodMiragium", { handheld })
            r(13, "miragium_nugget", "nuggetMiragium", "nuggetMiragium", { generated })
            r(14, "nephrite_gem", "gemNephrite", "gemNephrite", { generated })
            r(15, "topaz_gem", "gemTopaz", "gemTopaz", { generated })
            r(16, "tourmaline_gem", "gemTourmaline", "gemTourmaline", { generated })
            r(17, "heliolite_gem", "gemHeliolite", "gemHeliolite", { generated })
            r(18, "labradorite_gem", "gemLabradorite", "gemLabradorite", { generated })
            r(19, "lilagium_ingot", "ingotLilagium", "ingotLilagium", { generated })
            r(20, "miragium_plate", "plateMiragium", "plateMiragium", { generated })
            r(21, "coal_dust", "dustCoal", "dustCoal", { generated }).fuel(1600)
            r(22, "charcoal_dust", "dustCharcoal", "dustCharcoal", { generated }).fuel(1600)

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }

    }
}

class ItemSimpleMaterials : ItemMultiMaterial<ItemVariantSimpleMaterials>() {
    override fun getItemBurnTime(itemStack: ItemStack) = getVariant(itemStack)?.burnTime ?: -1
}

class ItemVariantSimpleMaterials(registryName: String, unlocalizedName: String) : ItemVariantMaterial(registryName, unlocalizedName) {
    var burnTime: Int? = null
}

class BlockFluidMiragiumWater(fluid: Fluid) : BlockFluidClassic(fluid, Material.WATER) {
    init {
        setHardness(100.0f)
        setLightOpacity(3)
    }
}
