package miragefairy2019.mod.fairybox

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.common.toOreName
import miragefairy2019.lib.createGui
import miragefairy2019.lib.fairyCentrifugeCraftHandler
import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.FairyMaterials
import miragefairy2019.mod.artifacts.Fertilizer
import miragefairy2019.mod.artifacts.MirageFlower
import miragefairy2019.mod.artifacts.oreName
import miragefairy2019.mod.artifacts.sphereType
import mirrg.kotlin.castOrNull
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemBlock

object FairyBox {
    lateinit var blockFairyCentrifuge: () -> BlockFairyBoxBase
    lateinit var itemBlockFairyCentrifuge: () -> ItemBlock
    val module = module {

        // 妖精の家（空）
        run {
            val block = block({ BlockFairyBoxBase(4) { TileEntityFairyBoxEmpty() } }, "fairy_box") {
                setUnlocalizedName("fairyBox")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_box", y = facing.second)
                        }
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "fairy_box") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyBox.name", "Fairy Box", "妖精の家") }
            onMakeLang { enJa("tile.fairyBox.poem", "", "大きな刳りの木の中で") }
            tileEntity("fairy_box", TileEntityFairyBoxEmpty::class.java)
        }

        // 樹液の家
        run {
            val block = block({ BlockFairyBoxBase(4) { TileEntityFairyBoxResinTapper() } }, "fairy_resin_tapper") {
                setUnlocalizedName("fairyResinTapper")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_resin_tapper", y = facing.second)
                        }
                    )
                }
            }
            val item = item({ ItemBlock(block()) }, "fairy_resin_tapper") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyResinTapper.name", "Fairy Resin Tapper", "樹液取り職人スプルーツァの家") }
            onMakeLang { enJa("tile.fairyResinTapper.poem", "", "妖精だから、森に帰ります") }
            tileEntity("fairy_resin_tapper", TileEntityFairyBoxResinTapper::class.java)
        }

        // 分離機
        run {
            blockFairyCentrifuge = block({ BlockFairyBoxBase(4) { TileEntityFairyBoxCentrifuge() } }, "fairy_centrifuge") {
                setUnlocalizedName("fairyCentrifuge")
                setCreativeTab { Main.creativeTab }
                makeBlockStates {
                    DataBlockStates(
                        variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                            "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_centrifuge", y = facing.second)
                        }
                    )
                }
            }
            itemBlockFairyCentrifuge = item({ ItemBlock(blockFairyCentrifuge()) }, "fairy_centrifuge") {
                setCustomModelResourceLocation(variant = "facing=north")
            }
            onMakeLang { enJa("tile.fairyCentrifuge.name", "Fairy Centrifuge", "錬金術師グラヴィーチャの家") }
            onMakeLang { enJa("tile.fairyCentrifuge.poem", "`Super Strange Theory'", "“超変理論”") }
            tileEntity("fairy_centrifuge", TileEntityFairyBoxCentrifuge::class.java)
            onInit {
                Main.registerGuiHandler(GuiId.fairyBoxCentrifuge, object : ISimpleGuiHandler {
                    override fun GuiHandlerContext.onServer() = tileEntity?.castOrNull<TileEntityFairyBoxCentrifuge>()?.createContainer(player)
                    override fun GuiHandlerContext.onClient() = tileEntity?.castOrNull<TileEntityFairyBoxCentrifuge>()?.createContainer(player)?.createGui()
                }.guiHandler)
            }
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, "fairy_centrifuge"),
                DataShapedRecipe(
                    pattern = listOf(
                        "GbG",
                        "cBc",
                        "P#P"
                    ),
                    key = mapOf(
                        "#" to DataSimpleIngredient(item = "miragefairy2019:fairy_box"),
                        "G" to DataSimpleIngredient(item = "miragefairy2019:gravity_rod"),
                        "P" to DataSimpleIngredient(item = "miragefairy2019:pot"),
                        "B" to DataSimpleIngredient(item = "minecraft:bucket"),
                        "b" to DataSimpleIngredient(item = "minecraft:brewing_stand"),
                        "c" to DataOreIngredient(ore = "mirageFairy2019SphereChemical")
                    ),
                    result = DataResult(
                        item = "miragefairy2019:fairy_centrifuge"
                    )
                )
            )

            onAddRecipe {

                // 錬金術

                // グロウストーンダスト10＋ボウル→金3
                fairyCentrifugeCraftHandler {
                    process("融解", 30.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    process("沈殿", 40.0) { !Mana.DARK + !Erg.SPACE } // TODO translate
                    process("凝固", 30.0) { !Mana.GAIA + !Erg.FREEZE } // TODO translate
                    input("dustGlowstone".oreIngredient, 10)
                    input(Items.BOWL.ingredient, 1)
                    output("ingotGold".toOreName().copyItemStack() ?: cancel(), 3.0)
                }

                // 硫黄3＋赤石10→辰砂3
                fairyCentrifugeCraftHandler {
                    process("融解", 30.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    process("析出", 60.0) { !Mana.FIRE + !Erg.CHEMICAL } // TODO translate
                    process("洗浄", 10.0) { !Mana.DARK + !Erg.WATER } // TODO translate
                    input("dustSulfur".oreIngredient, 3)
                    input("dustRedstone".oreIngredient, 10)
                    output("dustCinnabar".toOreName().copyItemStack() ?: cancel(), 3.0)
                }

                // 石炭の粉64＋フリント8＋黒曜石→ダイヤモンド
                fairyCentrifugeCraftHandler {
                    process("圧縮", 10.0) { !Mana.GAIA + !Erg.CRAFT } // TODO translate
                    process("歪縮", 40.0) { !Mana.FIRE + !Erg.SPACE } // TODO translate
                    process("結晶成長", 50.0) { !Mana.SHINE + !Erg.CRYSTAL } // TODO translate
                    input("dustCoal".oreIngredient, 64)
                    input(Items.FLINT.ingredient, 8)
                    input("obsidian".oreIngredient, 1)
                    output("gemDiamond".toOreName().copyItemStack() ?: cancel(), 1.0)
                }


                // 樹液蒸留

                // 樹液16＋木炭＋ガラス瓶→シロップ
                fairyCentrifugeCraftHandler {
                    process("ろ過", 20.0) { !Mana.DARK + !Erg.WATER } // TODO translate
                    process("蒸留", 70.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    process("調味", 10.0) { !Mana.AQUA + !Erg.LIFE } // TODO translate
                    input("mirageFairyWoodResin".oreIngredient, 16)
                    input("gemCharcoal".oreIngredient, 1)
                    input(Items.GLASS_BOTTLE.ingredient, 1)
                    output(FairyMaterials.itemVariants.fairySyrup.createItemStack(), 1.0)
                }

                // 樹液＋砂糖→砂糖3
                fairyCentrifugeCraftHandler {
                    process("ろ過", 20.0) { !Mana.DARK + !Erg.WATER } // TODO translate
                    process("結晶化", 60.0) { !Mana.DARK + !Erg.CRYSTAL } // TODO translate
                    process("粉砕", 20.0) { !Mana.GAIA + !Erg.DESTROY } // TODO translate
                    input("mirageFairyWoodResin".oreIngredient, 1)
                    input(Items.SUGAR.ingredient, 1)
                    output(Items.SUGAR.createItemStack(), 3.0, 1.0)
                }

                // 樹液64＋燐灰石の粉2＋空き瓶→ミラージュエキス入り瓶
                fairyCentrifugeCraftHandler {
                    process("蒸留", 20.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    process("遠心分離", 40.0) { !Mana.GAIA + !Erg.SPACE } // TODO translate
                    process("オーラ破壊", 40.0) { !Mana.WIND + !Erg.ATTACK } // TODO translate
                    input("mirageFairyWoodResin".oreIngredient, 64)
                    input("dustApatite".oreIngredient, 2)
                    input(Items.GLASS_BOTTLE.ingredient, 1)
                    output(FairyMaterials.itemVariants.bottleMirageFlowerExtract.createItemStack(), 1.0)
                }

                // 樹液64＋辰砂の粉2＋空き瓶→ミラージュオイル入り瓶
                fairyCentrifugeCraftHandler {
                    process("蒸留", 20.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    process("遠心分離", 40.0) { !Mana.GAIA + !Erg.SPACE } // TODO translate
                    process("オーラ濃縮", 40.0) { !Mana.WIND + !Erg.LEVITATE } // TODO translate
                    input("mirageFairyWoodResin".oreIngredient, 64)
                    input("dustCinnabar".oreIngredient, 2)
                    input(Items.GLASS_BOTTLE.ingredient, 1)
                    output(FairyMaterials.itemVariants.bottleMirageFlowerOil.createItemStack(), 1.0)
                }

                // 樹液64＋骨粉8＋黒曜石→妖精のプラスチック
                fairyCentrifugeCraftHandler {
                    process("蒸留", 20.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    process("歪縮", 50.0) { !Mana.WIND + !Erg.SPACE } // TODO translate
                    process("重合", 30.0) { !Mana.FIRE + !Erg.CHEMICAL } // TODO translate
                    input("mirageFairyWoodResin".oreIngredient, 64)
                    input(Items.DYE.createItemStack(metadata = 15).ingredient, 8)
                    input("obsidian".oreIngredient, 1)
                    output(FairyMaterials.itemVariants.fairyPlastic.createItemStack(), 1.0, 1.0)
                    output(FairyMaterials.itemVariants.fairyPlasticWithFairy.createItemStack(), 0.01)
                }


                // マグマクリーム分解

                // マグマクリーム2＋雪玉→スライムボール
                fairyCentrifugeCraftHandler {
                    process("混合", 20.0) { !Mana.DARK + !Erg.DESTROY } // TODO translate
                    process("凝固", 50.0) { !Mana.FIRE + !Erg.CHEMICAL } // TODO translate
                    process("抽出", 30.0) { !Mana.GAIA + !Erg.CRAFT } // TODO translate
                    input(Items.MAGMA_CREAM.ingredient, 2)
                    input(Items.SNOWBALL.ingredient, 1)
                    output(Items.SLIME_BALL.createItemStack(), 1.0)
                }

                // マグマクリーム2＋コンクリートパウダー→ブレイズパウダー
                fairyCentrifugeCraftHandler {
                    process("混合", 20.0) { !Mana.DARK + !Erg.DESTROY } // TODO translate
                    process("凝固", 50.0) { !Mana.FIRE + !Erg.CHEMICAL } // TODO translate
                    process("抽出", 30.0) { !Mana.GAIA + !Erg.CRAFT } // TODO translate
                    input(Items.MAGMA_CREAM.ingredient, 2)
                    input(Blocks.CONCRETE_POWDER.ingredient!!, 1)
                    output(Items.BLAZE_POWDER.createItemStack(), 1.0)
                }


                // 妖精変成

                // R2蜘蛛精＋ミラジウムウォーター＋土→蜘蛛の目
                fairyCentrifugeCraftHandler {
                    process("形状抽出", 30.0) { !Mana.FIRE + !Erg.KNOWLEDGE } // TODO translate
                    process("材料溶解", 30.0) { !Mana.GAIA + !Erg.CHEMICAL } // TODO translate
                    process("分子合成", 40.0) { !Mana.SHINE + !Erg.LIFE } // TODO translate
                    input("mirageFairy2019FairySpiderRank2".oreIngredient, 1)
                    input("container1000MiragiumWater".oreIngredient, 1)
                    input("dirt".oreIngredient, 1)
                    output(Items.SPIDER_EYE.createItemStack(), 1.0, 1.0)
                }

                // R2ウィザースケルトン精＋ミラジウムウォーター＋スケルトンの頭＋石炭の粉10→ウィザースケルトンの頭
                fairyCentrifugeCraftHandler {
                    process("形状抽出", 30.0) { !Mana.FIRE + !Erg.KNOWLEDGE } // TODO translate
                    process("材料溶解", 30.0) { !Mana.GAIA + !Erg.CHEMICAL } // TODO translate
                    process("分子合成", 40.0) { !Mana.SHINE + !Erg.LIFE } // TODO translate
                    input("mirageFairy2019FairyWitherSkeletonRank2".oreIngredient, 1)
                    input("container1000MiragiumWater".oreIngredient, 1)
                    input(Items.SKULL.createItemStack(metadata = 0).ingredient, 1)
                    input("dustCoal".oreIngredient, 10)
                    output(Items.SKULL.createItemStack(metadata = 1), 1.0)
                }

                // シュルカー精＋ミラジウムウォーター＋エンドストーン16＋ラブラドライト＋空間のスフィア→シュルカーの殻
                fairyCentrifugeCraftHandler {
                    process("形状抽出", 30.0) { !Mana.FIRE + !Erg.KNOWLEDGE } // TODO translate
                    process("材料溶解", 30.0) { !Mana.GAIA + !Erg.CHEMICAL } // TODO translate
                    process("分子合成", 40.0) { !Mana.SHINE + !Erg.LIFE } // TODO translate
                    input("mirageFairy2019FairyShulkerRank1".oreIngredient, 1)
                    input("container1000MiragiumWater".oreIngredient, 1)
                    input("endstone".oreIngredient, 16)
                    input("gemLabradorite".oreIngredient, 3)
                    input(Erg.SPACE.sphereType.oreName.oreIngredient, 1)
                    output(Items.SHULKER_SHELL.createItemStack(), 1.0, 0.5)
                }


                // 選鉱

                // 砂利＋ボウル＋水→色々
                fairyCentrifugeCraftHandler {
                    process("粒度選別", 40.0) { !Mana.GAIA + !Erg.SENSE } // TODO translate
                    process("比重選別", 40.0) { !Mana.GAIA + !Erg.WATER } // TODO translate
                    process("乾燥", 20.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    input("gravel".oreIngredient, 1)
                    input(Items.BOWL.ingredient, 1)
                    input("container1000Water".oreIngredient, 1)
                    output(Items.FLINT.createItemStack(), 1.0) // 100% フリント
                    output(Blocks.SAND.createItemStack(), 0.1, 1.0) // 10% 砂
                    output(Blocks.STONE.createItemStack(metadata = 0), 0.1, 1.0) // 10% 石
                    output(Blocks.STONE.createItemStack(metadata = 1), 0.1, 1.0) // 10% 花崗岩
                    output(Blocks.STONE.createItemStack(metadata = 3), 0.1, 1.0) // 10% 閃緑岩
                    output(Blocks.STONE.createItemStack(metadata = 5), 0.1, 1.0) // 10% 安山岩
                    output("dustMagnetite".toOreName().copyItemStack() ?: cancel(), 0.05, 2.0) // 5% 磁鉄鉱
                    output("crystalCertusQuartz".toOreName().copyItemStack() ?: EMPTY_ITEM_STACK, 0.02, 2.0) // 2% ケルタスクォーツ
                    output("ingotGold".toOreName().copyItemStack() ?: cancel(), 0.001, 2.0) // 0.1% 金
                }

                // 土＋ボウル＋水→色々
                fairyCentrifugeCraftHandler {
                    process("粒度選別", 40.0) { !Mana.GAIA + !Erg.SENSE } // TODO translate
                    process("比重選別", 40.0) { !Mana.GAIA + !Erg.WATER } // TODO translate
                    process("乾燥", 20.0) { !Mana.GAIA + !Erg.FLAME } // TODO translate
                    input("dirt".oreIngredient, 1)
                    input(Items.BOWL.ingredient, 1)
                    input("container1000Water".oreIngredient, 1)
                    output(Blocks.DIRT.createItemStack(metadata = 1), 1.0) // 100% 荒い土
                    output(Blocks.SAND.createItemStack(), 0.1, 1.0) // 10% 砂
                    output(Items.CLAY_BALL.createItemStack(), 0.1, 1.0) // 10% 粘土
                    output(Fertilizer.itemFertilizer().createItemStack(), 0.1, 1.0) // 10% 肥料
                    output(Items.WHEAT_SEEDS.createItemStack(), 0.05, 2.0) // 5% 小麦の種
                    output(Items.MELON_SEEDS.createItemStack(), 0.01, 2.0) // 1% スイカの種
                    output(Items.PUMPKIN_SEEDS.createItemStack(), 0.01, 2.0) // 1% カボチャの種
                    output(Items.BEETROOT_SEEDS.createItemStack(), 0.01, 2.0) // 1% ビートルートの種
                    output(MirageFlower.itemMirageFlowerSeeds().createItemStack(), 0.01, 2.0) // 1% ミラージュフラワーの種
                }

            }
        }

    }
}
