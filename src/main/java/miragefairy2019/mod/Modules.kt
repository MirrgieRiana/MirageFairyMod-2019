package miragefairy2019.mod

import miragefairy2019.export.IngredientFactoryOreIngredientComplex
import miragefairy2019.jei.jeiModule
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.resourcemaker.DataOrIngredient
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.ja
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.artifactsModule
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.mod.fairy.Fairy
import miragefairy2019.mod.fairybox.FairyBox
import miragefairy2019.mod.fairyrelation.fairyRelationModule
import miragefairy2019.mod.fairyweapon.fairyWeaponModule
import miragefairy2019.mod.material.materialModule
import miragefairy2019.mod.pedestal.pedestalModule
import miragefairy2019.mod.placeditem.PlacedItem
import miragefairy2019.mod.playeraura.playerAuraModule
import miragefairy2019.mod.recipes.recipesModule
import miragefairy2019.mod.skill.skillModule
import miragefairy2019.mod.systems.systemsModule

val modules = module {

    // TODO move
    onMakeIngredientFactory {
        this["ore_dict_complex"] = IngredientFactoryOreIngredientComplex::class.java
    }

    // TODO move
    onMakeLang {

        enJa("tile.fairyLog.name", "Fairy Log", "妖精の樹洞")

        enJa("item.mirageFlowerSeeds.name", "Mirage Flower Seeds", "ミラージュフラワーの種")

        enJa("item.mirageFairyPot.name", "Fairy Pot", "妖精のポット")
        enJa("item.mirageFairyPotMiragiumWater.name", "Miragium Water Pot", "ミラジウムウォーター入りポット")
        enJa("item.mirageFairyPotMirageFlowerExtract.name", "Mirage Extract Pot", "ミラージュエキス入りポット")
        enJa("item.mirageFairyPotMirageFlowerOil.name", "Mirage Oil Pot", "ミラージュオイル入りポット")
        enJa("item.mirageFairyPotWater.name", "Water Pot", "水入りポット")
        enJa("item.mirageFairyPotLava.name", "Lava Pot", "溶岩入りポット")

        enJa("item.fairyStick.name", "Fairy Stick", "妖精のステッキ")
        enJa("item.fairyStick.poem", "", "頼みごとをしてみよう")

        enJa("item.spheres.name", "Sphere", "スフィア")
        enJa("item.spheres.format", "Sphere of %s", "%sのスフィア")

        enJa("fluid.miragium_water", "Miragium Water", "ミラジウムウォーター")
        enJa("tile.miragiumWater.name", "Miragium Water", "ミラジウムウォーター")
        enJa("fluid.mirage_flower_extract", "Mirage Extract", "ミラージュエキス")
        enJa("tile.mirageFlowerExtract.name", "Mirage Extract", "ミラージュエキス")
        enJa("fluid.mirage_flower_oil", "Mirage Oil", "ミラージュオイル")
        enJa("tile.mirageFlowerOil.name", "Mirage Oil", "ミラージュオイル")

        enJa("tile.fairyCollectionBox.name", "Fairy Collection Box", "妖精蒐集箱")

        enJa("tile.fairyWoodLog.name", "Fairy Wood Log", "妖精の原木")

        ja("item.forge.bucketFilled.name", "%s入りバケツ")

        enJa("mirageFairy2019.magic.status.strength.name", "Strength", "効果値")
        enJa("mirageFairy2019.magic.status.extent.name", "Extent", "拡散力")
        enJa("mirageFairy2019.magic.status.endurance.name", "Endurance", "持久力")
        enJa("mirageFairy2019.magic.status.production.name", "Production", "生産力")
        enJa("mirageFairy2019.magic.status.cost.name", "Cost", "コスト")
        enJa("mirageFairy2019.magic.status.pitch.name", "Pitch", "音程")
        enJa("mirageFairy2019.magic.status.maxHeight.name", "Max Height", "最大高さ")
        enJa("mirageFairy2019.magic.status.power.name", "Power", "パワー")
        enJa("mirageFairy2019.magic.status.fortune.name", "Fortune", "幸運")
        enJa("mirageFairy2019.magic.status.coolTime.name", "Cool Time", "クールタイム")
        enJa("mirageFairy2019.magic.status.wear.name", "Wear", "摩耗")
        enJa("mirageFairy2019.magic.status.additionalReach.name", "Additional Reach", "追加リーチ")
        enJa("mirageFairy2019.magic.status.collection.name", "Collection", "収集")
        enJa("mirageFairy2019.magic.status.damage.name", "Damage", "ダメージ")
        enJa("mirageFairy2019.magic.status.radius.name", "Radius", "半径")
        enJa("mirageFairy2019.magic.status.range.name", "Range", "範囲")
        enJa("mirageFairy2019.magic.status.maxTargetCount.name", "Max Target Count", "最大ターゲット数")
        enJa("mirageFairy2019.magic.status.looting.name", "Looting", "ドロップ増加")
        enJa("mirageFairy2019.magic.status.criticalRate.name", "Critical Rate", "クリティカル率")
        enJa("mirageFairy2019.magic.status.chargeTime.name", "Charge Time", "チャージタイム")
        enJa("mirageFairy2019.magic.status.extraItemDropRate.name", "Extra Drop Rate", "追加ドロップ率")
        enJa("mirageFairy2019.magic.status.maxHardness.name", "Max Hardness", "最大硬度")
        enJa("mirageFairy2019.magic.status.lightning.name", "Lightning", "雷属性")
        enJa("mirageFairy2019.magic.status.duration.name", "Duration", "持続時間")
        enJa("mirageFairy2019.magic.status.breakSpeed.name", "Break Speed", "破壊速度")
        enJa("mirageFairy2019.magic.status.speedUp.name", "Speed Up", "加速")
        enJa("mirageFairy2019.magic.status.coverRate.name", "Cover Rate", "カバー率")
        enJa("mirageFairy2019.magic.status.chargeSpeed.name", "Charge Speed", "チャージ速度")
        enJa("mirageFairy2019.magic.status.speed.name", "Speed", "速度")
        enJa("mirageFairy2019.magic.status.maxSpeed.name", "Max Speed", "最高速度")
        enJa("mirageFairy2019.magic.status.levelCost.name", "Level Cost", "レベルコスト")
        enJa("mirageFairy2019.magic.status.speedBoost.name", "Speed Boost", "速度ブースト")
        enJa("mirageFairy2019.magic.status.damageBoost.name", "Damage Boost", "ダメージブースト")
        enJa("mirageFairy2019.magic.status.durationBoost.name", "Duration Boost", "持続時間ブースト")

        enJa("itemGroup.mirageFairy2019", "MirageFairy2019", "MirageFairy2019")

        enJa("itemGroup.mirageFairy2019.bakedFairy", "MirageFairy2019: Baked Fairy", "MirageFairy2019：焼き妖精")

        enJa("mirageFairy2019.formula.source.cost.name", "Cost", "コスト")

        enJa("miragefairy2019.placeItem", "Place Item", "アイテムを置く")

        enJa("miragefairy2019.gui.duration.days", "days", "日")
        enJa("miragefairy2019.gui.duration.hours", "hours", "時間")
        enJa("miragefairy2019.gui.duration.minutes", "minutes", "分")
        enJa("miragefairy2019.gui.duration.seconds", "seconds", "秒")
        enJa("miragefairy2019.gui.duration.milliSeconds", "milli seconds", "ミリ秒")
        enJa("miragefairy2019.gui.playerAura.title", "Player Aura", "プレイヤーオーラ")
        enJa("miragefairy2019.gui.playerAura.poem.step1", "It's an appetizing scent.", "食欲をそそられる香りだ")
        enJa("miragefairy2019.gui.playerAura.poem.step2", "It's a familiar taste.", "食べ慣れた味だ")
        enJa("miragefairy2019.gui.playerAura.poem.step3", "I'm getting tired of this taste...", "この味にも飽きてきたな…")
        enJa("miragefairy2019.gui.playerAura.poem.step4", "The nutrition is biased...", "栄養が偏り気味だ…")
        enJa("miragefairy2019.gui.playerAura.poem.step5", "I want to eat something else...", "そろそろ他のものが食べたい…")

    }

    Main.mainModule(this)
    jeiModule(this)

    artifactsModule(this)
    pedestalModule(this)
    materialModule(this)
    systemsModule(this)
    recipesModule(this)

    playerAuraModule(this)
    PlacedItem.placedItemMdule(this)
    skillModule(this)
    Fairy.fairyModule(this)
    fairyWeaponModule(this)
    fairyRelationModule(this)
    FairyBox.fairyBoxModule(this)

    // TODO move

    // ミラジウムの板
    makeRecipe("miragium_plate") {
        DataShapedRecipe(
            pattern = listOf(
                "mm",
                "II",
                "II"
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "m" to WandType.MELTING.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 20
            )
        )
    }

    // 歪曲杖からミラジウムの板
    makeRecipe("miragium_plate_from_distortion_fairy_wand") {
        DataShapedRecipe(
            pattern = listOf(
                "d",
                "I",
                "I"
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "d" to WandType.DISTORTION.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 20
            )
        )
    }

    // リラジウム
    makeRecipe("lilagium_ingot") {
        DataShapedRecipe(
            pattern = listOf(
                "mLm",
                "LIL",
                "mLm"
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "L" to DataSimpleIngredient(
                    item = "minecraft:double_plant",
                    data = 1
                ),
                "m" to WandType.MELTING.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 19
            )
        )
    }

    // 融合のワンドからリラジウム
    makeRecipe("lilagium_ingot_from_fusion_fairy_wand") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "ingotMiragium"),
                DataSimpleIngredient(
                    item = "minecraft:double_plant",
                    data = 1
                ),
                DataSimpleIngredient(
                    item = "minecraft:double_plant",
                    data = 1
                ),
                WandType.FUSION.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 19
            )
        )
    }

    // 妖精の革
    makeRecipe("mirage_fairy_leather") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "leather"),
                DataOreIngredient(ore = "container1000MirageFlowerOil")
            ),
            result = DataResult(
                item = "miragefairy2019:fairy_materials",
                data = 14
            )
        )
    }

    // 歪曲のワンドからミラジウムの棒
    makeRecipe("miragium_rod_from_distortion_fairy_wand") {
        DataShapedRecipe(
            pattern = listOf(
                "d ",
                " I"
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "d" to WandType.DISTORTION.ingredientData
            ),
            result = DataResult(
                item = "miragefairy2019:materials",
                data = 12
            )
        )
    }

    // 妖精蒐集箱
    makeRecipe("fairy_collection_box") {
        DataShapedRecipe(
            pattern = listOf(
                "sls",
                "PLD",
                "sCs"
            ),
            key = mapOf(
                "L" to DataOreIngredient(ore = "logWood"),
                "P" to DataOreIngredient(ore = "paneGlass"),
                "D" to DataOreIngredient(ore = "doorWood"),
                "l" to DataOreIngredient(ore = "torch"),
                "C" to DataSimpleIngredient(
                    item = "minecraft:carpet",
                    data = 14
                ),
                "s" to DataOreIngredient(ore = "mirageFairy2019SphereSpace")
            ),
            result = DataResult(
                item = "miragefairy2019:fairy_collection_box"
            )
        )
    }

    // ブレイズロッドからブレイズパウダー　高効率
    makeRecipe("blaze_powder_from_blaze_rod") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataSimpleIngredient(
                    item = "minecraft:blaze_rod"
                )
            ),
            result = DataResult(
                item = "minecraft:blaze_powder",
                count = 3
            )
        )
    }

    // 妖精の原木
    makeRecipe("fairy_wood_log") {
        DataShapedRecipe(
            pattern = listOf(
                "ooo",
                "oLo",
                "ooo"
            ),
            key = mapOf(
                "L" to DataOreIngredient(ore = "logWood"),
                "o" to DataOreIngredient(ore = "container1000MirageFlowerOil")
            ),
            result = DataResult(
                item = "miragefairy2019:fairy_wood_log"
            )
        )
    }

    // 硫黄の粉→ファイヤーチャージ
    makeRecipe("fire_charge_from_sulfur_dust") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "gunpowder"),
                DataOreIngredient(ore = "dustSulfur"),
                DataOrIngredient(
                    DataSimpleIngredient(
                        item = "minecraft:coal",
                        data = 0
                    ),
                    DataSimpleIngredient(
                        item = "minecraft:coal",
                        data = 1
                    ),
                    DataOreIngredient(ore = "dustCoal"),
                    DataOreIngredient(ore = "dustCharcoal")
                ),
                DataOreIngredient(ore = "dustMiragium")
            ),
            result = DataResult(
                item = "minecraft:fire_charge",
                count = 3
            )
        )
    }

    // エリトラ
    makeRecipe("elytra_from_mirage_fairy_leather") {
        DataShapedRecipe(
            pattern = listOf(
                "SLS",
                "LLL",
                "LcL"
            ),
            key = mapOf(
                "c" to WandType.CRAFTING.ingredientData,
                "S" to DataOreIngredient(ore = "mirageFairy2019SphereLevitate"),
                "L" to DataOreIngredient(ore = "mirageFairyLeather")
            ),
            result = DataResult(
                item = "minecraft:elytra"
            )
        )
    }

    // 燐灰石の魔導芯棒
    makeRecipe("aqua_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " Gp",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "gemApatite"),
                "c" to WandType.CRAFTING.ingredientData,
                "p" to WandType.POLISHING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 4)
        )
    }

    // 骨＞破砕のワンド→骨粉
    makeRecipe("bone_meal_from_bone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "bone")
            ),
            result = DataResult(item = "minecraft:dye", data = 15, count = 4)
        )
    }

    // 粘土ブロック→粘土
    makeRecipe("clay_ball_from_clay") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataSimpleIngredient(item = "minecraft:clay")
            ),
            result = DataResult(item = "minecraft:clay_ball", count = 4)
        )
    }

    // 石→丸石
    makeRecipe("cobblestone_from_stone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "stone")
            ),
            result = DataResult(item = "minecraft:cobblestone", count = 1)
        )
    }

    // 磁鉄鉱の魔導芯棒
    makeRecipe("dark_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " Gp",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "gemMagnetite"),
                "c" to WandType.CRAFTING.ingredientData,
                "p" to WandType.POLISHING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 5)
        )
    }

    // エンチャントの瓶
    makeRecipe("experience_bottle") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "container250MirageFlowerExtract"),
                WandType.BREAKING.ingredientData,
                DataSimpleIngredient(item = "minecraft:ender_eye")
            ),
            result = DataResult(item = "minecraft:experience_bottle")
        )
    }

    // 任意のフェアリークリスタル→原種のフェアリークリスタル
    makeRecipe("fairy_crystal") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "mirageFairyCrystalAny")
            ),
            result = DataResult(item = "miragefairy2019:fairy_crystal", data = 0)
        )
    }

    // 妖精のステッキ
    makeRecipe("fairy_stick") {
        DataShapedRecipe(
            pattern = listOf(
                "  c",
                " s ",
                "s  "
            ),
            key = mapOf(
                "s" to DataOreIngredient(ore = "stickWood"),
                "c" to DataOreIngredient(ore = "mirageFairyCrystal")
            ),
            result = DataResult(item = "miragefairy2019:fairy_stick")
        )
    }

    // 辰砂の魔導芯棒
    makeRecipe("fire_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " Gp",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "gemCinnabar"),
                "c" to WandType.CRAFTING.ingredientData,
                "p" to WandType.POLISHING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 1)
        )
    }

    // 硫黄の魔導芯棒
    makeRecipe("gaia_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " Gp",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "gemSulfur"),
                "c" to WandType.CRAFTING.ingredientData,
                "p" to WandType.POLISHING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 3)
        )
    }

    // ガラスの魔導芯棒
    makeRecipe("glass_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " G ",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "blockGlass"),
                "c" to WandType.CRAFTING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 13)
        )
    }

    // グロウストーン→グロウストーンダスト
    makeRecipe("glowstone_dust_from_glowstone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "glowstone")
            ),
            result = DataResult(item = "minecraft:glowstone_dust", count = 4)
        )
    }

    // 丸石→砂利
    makeRecipe("gravel_from_cobblestone") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "cobblestone")
            ),
            result = DataResult(item = "minecraft:gravel", count = 1)
        )
    }

    // 火薬
    makeRecipe("gunpowder_from_saltpeter") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOrIngredient(
                    DataSimpleIngredient(item = "minecraft:coal", data = 1),
                    DataOreIngredient(ore = "dustCharcoal")
                ),
                DataOreIngredient(ore = "dustSulfur"),
                DataOreIngredient(ore = "gemSaltpeter"),
                DataOreIngredient(ore = "dustMiragium")
            ),
            result = DataResult(item = "minecraft:gunpowder", count = 3)
        )
    }

    // 水→氷
    makeRecipe("ice_from_water") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.FREEZING.ingredientData,
                DataOreIngredient(ore = "container1000Water")
            ),
            result = DataResult(item = "minecraft:ice", count = 1)
        )
    }

    // 黒曜石→溶岩
    makeRecipe("lava_from_obsidian") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.MELTING.ingredientData,
                DataOreIngredient(ore = "obsidian"),
                DataSimpleIngredient(item = "minecraft:bucket")
            ),
            result = DataResult(item = "minecraft:lava_bucket", count = 1)
        )
    }

    // 妖精の木の棒
    makeRecipe("mirage_fairy_wood_stick") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "stickWood"),
                DataOreIngredient(ore = "container250MirageFlowerOil")
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 9)
        )
    }

    // ミラージュフラワーの茎
    makeRecipe("mirage_flower_stick") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "mirageFairyStick"),
                DataOreIngredient(ore = "container1000MiragiumWater"),
                DataOreIngredient(ore = "mirageFairy2019FairyAbilityCrystal"),
                DataOreIngredient(ore = "leafMirageFlower"),
                DataOreIngredient(ore = "dustApatite")
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 7)
        )
    }

    // ミラジウムの粉＋妖精→ミラジウムナゲット
    makeRecipe("miragium_nugget_by_fairy") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataOreIngredient(ore = "dustMiragium"),
                DataOreIngredient(ore = "mirageFairyCrystal"),
                DataOreIngredient(ore = "mirageFairy2019FairyAbilityFlame")
            ),
            result = DataResult(item = "miragefairy2019:materials", data = 13, count = 2)
        )
    }

    // ミラジウムの粉＋紅蓮のワンド→ミラジウムナゲット
    makeRecipe("miragium_nugget_by_fairy_wand") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.MELTING.ingredientData,
                DataOreIngredient(ore = "dustMiragium")
            ),
            result = DataResult(item = "miragefairy2019:materials", data = 13, count = 3)
        )
    }

    // ミラジウムインゴット＋紅蓮のワンド→ミラジウムの棒
    makeRecipe("miragium_rod_by_fairy_wand") {
        DataShapedRecipe(
            pattern = listOf(
                "mI",
                "I "
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium"),
                "m" to WandType.MELTING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:materials", data = 12)
        )
    }

    // ミラジウムインゴット→ミラジウムの棒
    makeRecipe("miragium_rod_by_hand") {
        DataShapedRecipe(
            pattern = listOf(
                "  I",
                " I ",
                "I  "
            ),
            key = mapOf(
                "I" to DataOreIngredient(ore = "ingotMiragium")
            ),
            result = DataResult(item = "miragefairy2019:materials", data = 12)
        )
    }

    // ミラジウムウォーター入りポット
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

    // ネザーウォートブロック→ネザーウォート
    makeRecipe("nether_wart_from_nether_wart_block") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataSimpleIngredient(item = "minecraft:nether_wart_block")
            ),
            result = DataResult(item = "minecraft:nether_wart", count = 9)
        )
    }

    // 溶岩→黒曜石
    makeRecipe("obsidian_from_lava") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.HYDRATING.ingredientData,
                DataOreIngredient(ore = "container1000Lava")
            ),
            result = DataResult(item = "minecraft:obsidian", count = 1)
        )
    }

    // ネザークォーツブロック→ネザークォーツ
    makeRecipe("quartz_from_quartz_block") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "blockQuartz")
            ),
            result = DataResult(item = "minecraft:quartz", count = 4)
        )
    }

    // ネザークォーツの魔導芯棒
    makeRecipe("quartz_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " Gp",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "gemQuartz"),
                "c" to WandType.CRAFTING.ingredientData,
                "p" to WandType.POLISHING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 6)
        )
    }

    // 硝石
    makeRecipe("saltpeter") {
        DataShapedRecipe(
            pattern = listOf(
                "m",
                "D",
                "B"
            ),
            key = mapOf(
                "m" to WandType.MELTING.ingredientData,
                "D" to DataSimpleIngredient(item = "minecraft:dirt", data = 0),
                "B" to DataOreIngredient(ore = "container1000Water")
            ),
            result = DataResult(item = "miragefairy2019:materials", count = 1, data = 9)
        )
    }

    // 月長石の魔導芯棒
    makeRecipe("shine_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " Gp",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "gemMoonstone"),
                "c" to WandType.CRAFTING.ingredientData,
                "p" to WandType.POLISHING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 0)
        )
    }

    // 氷→雪
    makeRecipe("snow_from_ice") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.BREAKING.ingredientData,
                DataOreIngredient(ore = "ice")
            ),
            result = DataResult(item = "minecraft:snow", count = 1)
        )
    }

    // 雪ブロック→雪玉
    makeRecipe("snowball_from_snow") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataSimpleIngredient(item = "minecraft:snow")
            ),
            result = DataResult(item = "minecraft:snowball", count = 4)
        )
    }

    // 羊毛→糸
    makeRecipe("string_from_wool") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.CRAFTING.ingredientData,
                DataOreIngredient(ore = "wool")
            ),
            result = DataResult(item = "minecraft:string", count = 4)
        )
    }

    // 氷→水
    makeRecipe("water_from_ice") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.MELTING.ingredientData,
                DataOreIngredient(ore = "ice"),
                DataSimpleIngredient(item = "minecraft:bucket")
            ),
            result = DataResult(item = "minecraft:water_bucket", count = 1)
        )
    }

    // 加水のワンド→水
    makeRecipe("water_from_fairy_wand") {
        DataShapelessRecipe(
            ingredients = listOf(
                WandType.HYDRATING.ingredientData,
                DataSimpleIngredient(item = "minecraft:bucket")
            ),
            result = DataResult(item = "minecraft:water_bucket", count = 1)
        )
    }

    // 蛍石の魔導芯棒
    makeRecipe("wind_mana_rod") {
        DataShapedRecipe(
            pattern = listOf(
                " Gp",
                "GRG",
                "cG "
            ),
            key = mapOf(
                "R" to DataOreIngredient(ore = "rodMiragium"),
                "G" to DataOreIngredient(ore = "gemFluorite"),
                "c" to WandType.CRAFTING.ingredientData,
                "p" to WandType.POLISHING.ingredientData
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = 2)
        )
    }

}
