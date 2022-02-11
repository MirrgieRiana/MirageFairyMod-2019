package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.hex
import miragefairy2019.libkt.item
import miragefairy2019.libkt.lowerCamelCase
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.toRgb
import miragefairy2019.mod.modules.oreseed.OreSeed
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.fairy.FairyTypes
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mana.sum
import miragefairy2019.mod3.playeraura.api.ApiPlayerAura
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.modkt.impl.fairy.ColorSet
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.mana
import mirrg.kotlin.formatAs
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import java.io.File

object DebugItems {
    val module: Module = {

        fun r(itemCreator: () -> Item, name: String, english: String, japanese: String) {
            val unlocalizedName = "debug_$name".lowerCamelCase
            item({ itemCreator() }, "debug_$name") {
                setUnlocalizedName(unlocalizedName)
                setCreativeTab { ApiMain.creativeTab }
                setCustomModelResourceLocation(model = ResourceLocation("book"))
            }
            onMakeLang { enJa("item.$unlocalizedName.name", "Debug: $english", "デバッグ：$japanese") }
        }

        r({ ItemDebugFairyList() }, "fairy_list", "Fairy List", "妖精一覧")
        r({ ItemDebugOreNameList() }, "ore_name_list", "Ore Name List", "鉱石辞書名一覧")
        r({ ItemDebugSkillResetUnlock() }, "skill_reset_unlock", "Skill Reset Unlock", "スキルリセット解禁")
        r({ ItemDebugPlayerAuraReset() }, "player_aura_reset", "Player Aura Reset", "プレイヤーオーラリセット")
        r({ ItemDebugGainFairyMasterExp() }, "gain_fairy_master_exp", "Gain Fairy Master Exp", "妖精経験値入手")
        r({ ItemDebugOreSeedStatistics() }, "ore_seed_statistics", "Ore Seed Statistics", "鉱石分布")

    }
}

private val Double.f0 get() = this formatAs "%.0f"
private val Double.f3 get() = this formatAs "%.3f"

private fun writeAction(player: EntityPlayer, fileName: String, text: String) {
    val file = File("debug").resolve(fileName)
    player.sendStatusMessage(textComponent { !"Saved to " + !file }, false)
    file.parentFile.mkdirs()
    file.writeText(text)
}

open class ItemDebug : Item() {
    init {
        maxStackSize = 1
    }
}

class ItemDebugFairyList : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (!world.isRemote) return EnumActionResult.SUCCESS

        fun getLang(lang: String) = ItemDebugFairyList::class.java.getResource("/assets/miragefairy2019/lang/$lang.lang")!!.readText().split("""\r\n?|\n""".toRegex())
            .filter { it.isNotBlank() }
            .map { it.split("=", limit = 2) }
            .filter { it.size == 2 }
            .associate { Pair(it[0], it[1]) }

        val enUs = getLang("en_us")
        val jaJp = getLang("ja_jp")

        writeAction(player, "fairyList.txt", FairyTypes.instance.variants.joinToString("") { (id, bundle) ->
            val variantRank1 = bundle.main
            val variantRank2 = bundle[1]
            val fairyTypeRank1 = variantRank1.type
            val fairyTypeRank2 = variantRank2.type
            fun color(selector: ColorSet.() -> Int) = variantRank1.colorSet.selector().toRgb().hex
            val motif = fairyTypeRank1.motif!!.resourcePath
            "|${
                listOf(
                    listOf(
                        id,
                        "&bold(){!FairyImage(#${color { skin }},#${color { bright }},#${color { dark }},#${color { hair }})}",
                        "CENTER:$motif&br()${enUs["mirageFairy2019.fairy.$motif.name"]!!}",
                        "CENTER:${jaJp["mirageFairy2019.fairy.$motif.name"]!!.replace("""(?<![ァ-ヶー])(?=[ァ-ヶー])""".toRegex(), "&br()")}",
                        "CENTER:${variantRank1.rare}",
                        "RIGHT:${fairyTypeRank1.cost.f0}"
                    ),
                    EnumManaType.values().map {
                        "RIGHT:${fairyTypeRank1.mana(it).f3}"
                    },
                    listOf(
                        "RIGHT:${fairyTypeRank1.manaSet.sum.f3}"
                    ),
                    EnumManaType.values().map {
                        val a1 = fairyTypeRank1.mana(it) / fairyTypeRank1.cost * 50
                        val a2 = fairyTypeRank2.mana(it) / fairyTypeRank2.cost * 50
                        "${if (a1 >= 10) "BGCOLOR(#FDD):" else if (a2 >= 10) "BGCOLOR(#DDF):" else ""}RIGHT:${a1.f3}"
                    },
                    listOf(
                        "RIGHT:${(fairyTypeRank1.manaSet.sum / fairyTypeRank1.cost * 50).f3}"
                    ),
                    EnumErgType.values().map {
                        val a1 = fairyTypeRank1.erg(it)
                        val a2 = fairyTypeRank2.erg(it)
                        "${if (a1 >= 10) "BGCOLOR(#FDD):" else if (a2 >= 10) "BGCOLOR(#DDF):" else ""}RIGHT:${a1.f3}"
                    }
                ).flatten().joinToString("|")
            }|\n"
        })

        return EnumActionResult.SUCCESS
    }
}

class ItemDebugOreNameList : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (!world.isRemote) return EnumActionResult.SUCCESS
        writeAction(player, "oreNameList.txt", OreDictionary.getOreNames()
            .sorted()
            .filter { OreDictionary.getOres(it).isNotEmpty() }
            .joinToString("") { "$it\n" })
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugSkillResetUnlock : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)
        skillContainer.variables.lastMasteryResetTime = null
        if (player is EntityPlayerMP) skillContainer.send(player)
        player.sendStatusMessage(textComponent { !"スキルポイント初期化が可能になりました" }, true) // TODO translate
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugPlayerAuraReset : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        if (player !is EntityPlayerMP) return EnumActionResult.SUCCESS
        val playerAuraHandler = ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(player)
        playerAuraHandler.onReset()
        playerAuraHandler.send()
        player.sendStatusMessage(textComponent { !"プレイヤーオーラをリセットしました" }, true) // TODO translate
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugGainFairyMasterExp : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        if (player !is EntityPlayerMP) return EnumActionResult.SUCCESS
        val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)
        ItemAstronomicalObservationBook.gainExp(player, if (player.isSneaking) -100 else 100)
        skillContainer.send(player)
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugOreSeedStatistics : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        val map = mutableMapOf<IBlockState, Int>()

        fun processChunk(chunkX: Int, chunkZ: Int) {
            val baseBlockPos = BlockPos(16 * chunkX, 0, 16 * chunkZ)
            (0 until 16).forEach { xi ->
                (0 until 256).forEach { yi ->
                    (0 until 16).forEach { zi ->
                        val blockState = world.getBlockState(baseBlockPos.add(xi, yi, zi))
                        when (blockState.block) {
                            OreSeed.blockOreSeed(),
                            OreSeed.blockOreSeedNether(),
                            OreSeed.blockOreSeedEnd()
                            -> map.compute(blockState) { _, value -> (value ?: 0) + 1 }
                        }
                    }
                }
            }
        }

        val chunkX = Math.floorDiv(blockPos.x, 16)
        val chunkZ = Math.floorDiv(blockPos.z, 16)

        (-4..4).forEach { x ->
            (-4..4).forEach { z ->
                processChunk(chunkX + x, chunkZ + z)
            }
        }

        // 鉱石生成確率表示
        player.sendStatusMessage(textComponent { !"===== Ore Seed | Chunk: (${chunkX - 4}, ${chunkZ - 4}) .. (${chunkX + 4}, ${chunkZ + 4}) =====" }, false)
        map.entries.sortedBy { it.key.block.getMetaFromState(it.key) }.sortedBy { Block.getIdFromBlock(it.key.block) }.forEach {
            player.sendStatusMessage(textComponent { !"${it.key}: ${it.value}" }, false)
        }
        player.sendStatusMessage(textComponent { !"====================" }, false)

        return EnumActionResult.SUCCESS
    }
}
