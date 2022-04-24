package miragefairy2019.mod.fairybox

import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.darkRed
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.artifacts.FairyMaterials
import mirrg.boron.util.UtilsMath
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.formatAs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB

class TileEntityFairyBoxResinTapper : TileEntityFairyBoxBase() {
    override fun getExecutor(): IFairyBoxExecutor {
        val blockState = world.getBlockState(pos)
        val block = blockState.block as? BlockFairyBoxBase ?: return super.getExecutor()
        val facing = block.getFacing(blockState)
        val blockPosOutput = pos.offset(facing)

        // 目の前にアイテムがある場合は行動しない（Lazy Chunk対策）
        if (world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPosOutput)).isNotEmpty()) return FailureFairyBoxExecutor(textComponent { "制作物があふれています"().darkRed }) // TODO translate

        // 排出面が塞がれている場合は行動しない
        if (world.getBlockState(blockPosOutput).isSideSolid(world, blockPosOutput, facing.opposite)) return FailureFairyBoxExecutor(textComponent { "妖精が入れません"().darkRed }) // TODO translate

        // 妖精の木のコンパイルに失敗した場合は抜ける
        val leaves = try {
            compileFairyTree(world, pos)
        } catch (e: TreeCompileException) {
            return FailureFairyBoxExecutor(e.description)
        }

        // 成立
        return object : IFairyBoxExecutor {
            override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
                if (world.isRemote) return true
                val times = 10000

                val auraCollectionSpeed = getAuraCollectionSpeed(world, leaves, times) atMost 120.0
                val baseCount = (auraCollectionSpeed / smallTreeAuraCollectionSpeed - 0.5) atLeast 0.0

                player.sendStatusMessage(textComponent { "オーラ吸収速度: ${auraCollectionSpeed formatAs "%.2f"} Folia, 生産速度: ${baseCount formatAs "%.2f"} 個/分"() }, true) // TODO translate
                return true
            }

            override fun onUpdateTick() {
                val times = 10

                val auraCollectionSpeed = getAuraCollectionSpeed(world, leaves, times) atMost 120.0
                val baseCount = (auraCollectionSpeed / smallTreeAuraCollectionSpeed - 0.5) atLeast 0.0

                val count = UtilsMath.randomInt(world.rand, baseCount)
                if (count > 0) FairyMaterials.itemVariants.fairyWoodResin.createItemStack(count).drop(world, blockPosOutput, motionless = true)
            }
        }
    }
}
