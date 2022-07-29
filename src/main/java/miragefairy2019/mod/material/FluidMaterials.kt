package miragefairy2019.mod.material

import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setFluidStateMapper
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.fluid
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.ja
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.BlockFluidClassic
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object FluidMaterials {
    lateinit var fluidMiragiumWater: () -> Fluid
    lateinit var blockFluidMiragiumWater: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMiragiumWater: () -> ItemBlock

    lateinit var fluidMirageFlowerExtract: () -> Fluid
    lateinit var blockFluidMirageFlowerExtract: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMirageFlowerExtract: () -> ItemBlock

    lateinit var fluidMirageFlowerOil: () -> Fluid
    lateinit var blockFluidMirageFlowerOil: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMirageFlowerOil: () -> ItemBlock

    lateinit var fluidMirageFairyBlood: () -> Fluid
    lateinit var blockFluidMirageFairyBlood: () -> BlockFluidMiragiumWater
    lateinit var itemFluidMirageFairyBlood: () -> ItemBlock

    val fluidMaterialsModule = module {

        // ユニバーサルバケツ
        onConstruction { FluidRegistry.enableUniversalBucket() }
        onMakeLang { ja("item.forge.bucketFilled.name", "%s入りバケツ") }


        // ミラジウムウォーター
        fluidMiragiumWater = fluid("miragium_water") {
            viscosity = 600
        }
        blockFluidMiragiumWater = block({ BlockFluidMiragiumWater(fluidMiragiumWater()) }, "miragium_water") {
            setUnlocalizedName("miragiumWater")
            setCreativeTab { Main.creativeTab }
            setFluidStateMapper()
            makeBlockStates(resourceName.path) { fluid }
        }
        itemFluidMiragiumWater = item({ ItemBlock(blockFluidMiragiumWater()) }, "miragium_water") {
            setCustomModelResourceLocation()
        }
        makeItemModel("miragium_water") { fluid }
        onMakeLang {
            enJa("fluid.miragium_water", "Miragium Water", "ミラジウムウォーター")
            enJa("tile.miragiumWater.name", "Miragium Water", "ミラジウムウォーター")
        }
        makeRecipe("miragium_water_pot") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairyPot"),
                    DataOreIngredient(ore = "container1000Water"),
                    DataOreIngredient(ore = "dustMiragium")
                ),
                result = DataResult(item = "miragefairy2019:filled_bucket", data = 0)
            )
        }


        // ミラージュエキス
        fluidMirageFlowerExtract = fluid("mirage_flower_extract") {
            viscosity = 1000
        }
        blockFluidMirageFlowerExtract = block({ BlockFluidMiragiumWater(fluidMirageFlowerExtract()) }, "mirage_flower_extract") {
            setUnlocalizedName("mirageFlowerExtract")
            setCreativeTab { Main.creativeTab }
            setFluidStateMapper()
            makeBlockStates(resourceName.path) { fluid }
        }
        itemFluidMirageFlowerExtract = item({ ItemBlock(blockFluidMirageFlowerExtract()) }, "mirage_flower_extract") {
            setCustomModelResourceLocation()
        }
        makeItemModel("mirage_flower_extract") { fluid }
        onMakeLang {
            enJa("fluid.mirage_flower_extract", "Mirage Extract", "ミラージュエキス")
            enJa("tile.mirageFlowerExtract.name", "Mirage Extract", "ミラージュエキス")
        }


        // ミラージュオイル
        fluidMirageFlowerOil = fluid("mirage_flower_oil") {
            viscosity = 1500
        }
        blockFluidMirageFlowerOil = block({ BlockFluidMiragiumWater(fluidMirageFlowerOil()) }, "mirage_flower_oil") {
            setUnlocalizedName("mirageFlowerOil")
            setCreativeTab { Main.creativeTab }
            setFluidStateMapper()
            makeBlockStates(resourceName.path) { fluid }
        }
        itemFluidMirageFlowerOil = item({ ItemBlock(blockFluidMirageFlowerOil()) }, "mirage_flower_oil") {
            setCustomModelResourceLocation()
        }
        makeItemModel("mirage_flower_oil") { fluid }
        onMakeLang {
            enJa("fluid.mirage_flower_oil", "Mirage Oil", "ミラージュオイル")
            enJa("tile.mirageFlowerOil.name", "Mirage Oil", "ミラージュオイル")
        }


        // 妖精の血
        fluidMirageFairyBlood = fluid("mirage_fairy_blood") {
            viscosity = 1000
        }
        blockFluidMirageFairyBlood = block({ BlockFluidMiragiumWater(fluidMirageFairyBlood()) }, "mirage_fairy_blood") {
            setUnlocalizedName("mirageFairyBlood")
            setCreativeTab { Main.creativeTab }
            setFluidStateMapper()
            makeBlockStates(resourceName.path) { fluid }
        }
        itemFluidMirageFairyBlood = item({ ItemBlock(blockFluidMirageFairyBlood()) }, "mirage_fairy_blood") {
            setCustomModelResourceLocation()
        }
        makeItemModel("mirage_fairy_blood") { fluid }
        onMakeLang {
            enJa("fluid.mirage_fairy_blood", "Mirage Fairy Blood", "妖精の血")
            enJa("tile.mirageFairyBlood.name", "irage Fairy Blood", "妖精の血")
        }

    }
}


fun ModScope.fluid(name: String, initializer: Fluid.() -> Unit = {}): () -> Fluid {
    lateinit var fluid: Fluid
    onRegisterFluid {
        fluid = Fluid(
            name,
            ResourceLocation(ModMirageFairy2019.MODID, "blocks/${name}_still"),
            ResourceLocation(ModMirageFairy2019.MODID, "blocks/${name}_flow"),
            ResourceLocation(ModMirageFairy2019.MODID, "blocks/${name}_overlay")
        )
        fluid.initializer()
        FluidRegistry.registerFluid(fluid)
        FluidRegistry.addBucketForFluid(fluid)
    }
    return { fluid }
}


@SideOnly(Side.CLIENT)
class FluidStateMapper(val resourceLocation: ResourceLocation) : StateMapperBase() {
    override fun getModelResourceLocation(blockState: IBlockState) = ModelResourceLocation(resourceLocation, "fluid")
}


class BlockFluidMiragiumWater(fluid: Fluid) : BlockFluidClassic(fluid, Material.WATER) {
    init {
        setHardness(100.0f)
        setLightOpacity(3)
    }
}