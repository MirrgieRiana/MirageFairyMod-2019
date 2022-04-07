package miragefairy2019.mod.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.Main
import net.minecraft.block.BlockRotatedPillar
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

object FairyWoodLog {
    lateinit var blockFairyWoodLog: () -> BlockFairyWoodLog
    lateinit var itemBlockFairyWoodLog: () -> ItemBlock
    val module = module {
        blockFairyWoodLog = block({ BlockFairyWoodLog() }, "fairy_wood_log") {
            setUnlocalizedName("fairyWoodLog")
            setCreativeTab { Main.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf("y" to Pair(null, null), "z" to Pair(90, null), "x" to Pair(90, 90)).associate { axis ->
                        "axis=${axis.first}" to DataBlockState("miragefairy2019:fairy_wood_log", x = axis.second.first, y = axis.second.second)
                    }
                )
            }
        }
        itemBlockFairyWoodLog = item({ ItemBlock(blockFairyWoodLog()) }, "fairy_wood_log") {
            setUnlocalizedName("fairyWoodLog")
            addOreName("logFairyWood")
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation(variant = "axis=y")
        }
    }
}

class BlockFairyWoodLog : BlockRotatedPillar(Material.WOOD) {
    init {

        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(2.0f)
        setHarvestLevel("axe", 0)

    }

    override fun canSustainLeaves(state: IBlockState, world: IBlockAccess, pos: BlockPos) = true
}