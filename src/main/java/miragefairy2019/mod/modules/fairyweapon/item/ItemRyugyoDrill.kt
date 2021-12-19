package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.grow
import miragefairy2019.libkt.positions
import miragefairy2019.libkt.range
import miragefairy2019.libkt.sortedByDistance
import miragefairy2019.mod3.magic.negative
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.skill.EnumMastery
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemRyugyoDrill(
    additionalBaseStatus: Double
) : ItemMiragiumToolBase(
    EnumManaType.GAIA,
    EnumMastery.harvest,
    additionalBaseStatus
) {
    val maxHardness = "maxHardness"({ double2.positive }) { 2.0 + !strength * 0.02 }.setRange(2.0..20.0).setVisibility(Companion.EnumVisibility.DETAIL)
    val range = "range"({ int.positive }) { (1 + !extent * 0.01).toInt() }.setRange(1..5).setVisibility(Companion.EnumVisibility.DETAIL)
    val coolTime = "coolTime"({ tick.negative }) { cost * 2.0 }.setVisibility(Companion.EnumVisibility.DETAIL)

    init {
        setHarvestLevel("pickaxe", 3)
        setHarvestLevel("shovel", 3)
        destroySpeed = 8.0f
    }

    override fun isEffective(itemStack: ItemStack, blockState: IBlockState) = super.isEffective(itemStack, blockState) || when {
        blockState.block === Blocks.SNOW_LAYER -> true
        blockState.material === Material.IRON -> true
        blockState.material === Material.ANVIL -> true
        blockState.material === Material.ROCK -> true
        blockState.material === Material.SNOW -> true
        else -> false
    }

    override fun Companion.MagicScope.getTargets(itemStack: ItemStack, world: World, blockPosBase: BlockPos) = blockPosBase.range.grow(!range, !range, !range).positions
        .filter { blockPos -> world.getBlockState(blockPos).getBlockHardness(world, blockPos) <= !maxHardness }
        .sortedByDistance(blockPosBase)

    override fun Companion.MagicScope.getCoolTime() = !coolTime
}