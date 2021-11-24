package miragefairy2019.mod.modules.mirageflower

import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry
import miragefairy2019.mod.modules.ore.ModuleOre
import miragefairy2019.mod.modules.ore.material.EnumVariantMaterials1
import miragefairy2019.mod3.erg.api.ErgTypes
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.shineEfficiency
import net.minecraft.block.BlockFarmland
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.EnumSkyBlock
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary

fun getGrowRate(world: World, blockPos: BlockPos): Double {
    var rate = 0.04 // 何もしなくても25回に1回の割合で成長する

    // 人工光が当たっているなら加点
    rate *= world.getLightFor(EnumSkyBlock.BLOCK, blockPos).let {
        when {
            it >= 13 -> 1.2
            it >= 9 -> 1.1
            else -> 1.0
        }
    }

    // 太陽光が当たっているなら加点
    rate *= world.getLightFor(EnumSkyBlock.SKY, blockPos).let {
        when {
            it >= 15 -> 1.1
            it >= 9 -> 1.05
            else -> 1.0
        }
    }

    if (world.canSeeSky(blockPos)) rate *= 1.1 // 空が見えるなら加点

    // 地面加点
    rate *= world.getBlockState(blockPos.down()).let { blockState ->
        var bonus = 0.5

        // 妖精による判定
        run {
            val itemStack = blockState.block.getItem(world, blockPos, blockState)
            val list: List<ResourceLocation> = ApiFairyRegistry.getFairyRelationRegistry().fairySelector()
                .add(blockState)
                .add(itemStack)
                .select().toList()
            val value = list
                .mapNotNull { ApiFairyRegistry.getFairyRegistry().getFairy(it).orElse(null) }
                .map { getGrowRateInFloor(it.fairyType) }
                .max()
            if (value != null) bonus = bonus.coerceAtLeast(value)
        }

        // 特定ブロックによる判定
        if (blockState.block === Blocks.GRASS) bonus = bonus.coerceAtLeast(1.0)
        if (blockState.block === Blocks.DIRT) bonus = bonus.coerceAtLeast(1.1)
        if (blockState.block === Blocks.FARMLAND) {
            bonus = bonus.coerceAtLeast(1.2)
            if (blockState.getValue(BlockFarmland.MOISTURE) > 0) bonus = bonus.coerceAtLeast(1.3) // 耕土が湿っているなら加点
        }
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.APATITE_BLOCK)) bonus = bonus.coerceAtLeast(1.5)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.FLUORITE_BLOCK)) bonus = bonus.coerceAtLeast(2.0)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.SULFUR_BLOCK)) bonus = bonus.coerceAtLeast(1.5)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.CINNABAR_BLOCK)) bonus = bonus.coerceAtLeast(2.0)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MOONSTONE_BLOCK)) bonus = bonus.coerceAtLeast(3.0)
        if (blockState === ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MAGNETITE_BLOCK)) bonus = bonus.coerceAtLeast(1.2)

        bonus
    }

    // バイオーム加点
    rate *= world.getBiome(blockPos).let { biome ->
        when {
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) -> 1.3
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.MAGICAL) -> 1.3
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN) -> 1.2
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) -> 1.2
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) -> 1.1
            BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) -> 1.1
            else -> 1.0
        }
    }

    return rate
}

fun getGrowRateInFloor(fairyType: IFairyType) = fairyType.shineEfficiency * fairyType.erg(ErgTypes.crystal) / 100.0 * 3

fun getGrowRateTableMessage() = textComponent {
    listOf(
        !"===== Mirage Flower Grow Rate Table =====\n",
        ApiFairyRegistry.getFairyRegistry().fairies.toList()
            .map { Pair(it.fairyType, getGrowRateInFloor(it.fairyType)) }
            .filter { it.second > 1 }
            .sortedBy { it.second }
            .flatMap { format("%7.2f%%  ", it.second * 100) + !it.first!!.displayName + !"\n" },
        !"===================="
    ).flatten()
}

fun getGrowRateMessage(world: World, pos: BlockPos) = textComponent {
    !"===== Mirage Flower Grow Rate =====\n"
    !"Pos: ${pos.x} ${pos.y} ${pos.z}\n"
    !"Block: ${world.getBlockState(pos)}\n"
    !"Floor: ${world.getBlockState(pos.down())}\n"
    format("%.2f%%\n", getGrowRate(world, pos) * 100)
    !"===================="
}


class BlockMirageFlower : BlockMirageFlowerBase(Material.PLANTS) {  // Solidであるマテリアルは耕土を破壊する
    init {
        // meta
        defaultState = blockState.baseState.withProperty(AGE, 0)
        // style
        soundType = SoundType.GLASS
    }

    // state
    override fun getMetaFromState(state: IBlockState) = state.getValue(AGE).coerceIn(0, 3)
    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(AGE, meta.coerceIn(0, 3))
    override fun createBlockState() = BlockStateContainer(this, AGE)
    fun getState(age: Int): IBlockState = defaultState.withProperty(AGE, age)

    companion object {
        val AGE: PropertyInteger = PropertyInteger.create("age", 0, 3)

        val AABB_STAGE0 = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0)
        val AABB_STAGE1 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0)
        val AABB_STAGE2 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
        val AABB_STAGE3 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
    }
}
