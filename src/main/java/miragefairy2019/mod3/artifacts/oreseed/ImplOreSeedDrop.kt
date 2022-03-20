package miragefairy2019.mod3.artifacts.oreseed

import miragefairy2019.libkt.WeightedItem
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack

class OreSeedDropRegistry : IOreSeedDropRegistry {
    override val recipes = mutableListOf<IOreSeedDropHandler>()
    override fun register(handler: IOreSeedDropHandler) = run<Unit> { recipes.add(handler) }
    override fun getDropList(environment: OreSeedDropEnvironment) = recipes.mapNotNull { it.getDrop(environment) }
}

fun IOreSeedDropRegistry.register(
    type: EnumOreSeedType,
    shape: EnumOreSeedShape,
    weight: Double,
    output: Pair<() -> IBlockState, () -> ItemStack>,
    vararg requirements: IOreSeedDropRequirement
) {
    register(object : IOreSeedDropHandler {
        override fun getDrop(environment: OreSeedDropEnvironment): OreSeedDrop? {
            if (environment.type != type) return null
            if (environment.shape != shape) return null
            requirements.forEach {
                if (!it.test(environment)) return null
            }
            return WeightedItem(output.first, weight)
        }

        override fun getType() = type
        override fun getShape() = shape
        override fun getWeight() = weight
        override fun getRequirements() = requirements.flatMap { it.getDescriptions() }
        override fun getOutputItemStacks() = listOf(output.second())
    })
}

class OreSeedDropRegistryScope(val registry: IOreSeedDropRegistry) {
    operator fun EnumOreSeedType.invoke(block: TypedOreSeedDropRegistryScope.() -> Unit) = TypedOreSeedDropRegistryScope(this).block()
    inner class TypedOreSeedDropRegistryScope(val type: EnumOreSeedType) {
        fun register(
            shape: EnumOreSeedShape,
            weight: Double,
            output: Pair<() -> IBlockState, () -> ItemStack>,
            vararg generationConditions: IOreSeedDropRequirement
        ) {
            registry.register(type, shape, weight, output, *generationConditions)
        }
    }
}

operator fun IOreSeedDropRegistry.invoke(block: OreSeedDropRegistryScope.() -> Unit) = OreSeedDropRegistryScope(this).block()


object OreSeedDropRequirements {
    @JvmStatic // todo delete jvm
    fun minY(minY: Int): IOreSeedDropRequirement = object : IOreSeedDropRequirement {
        override fun test(environment: OreSeedDropEnvironment) = environment.blockPos.y >= minY
        override fun getDescriptions() = listOf("Min Y: $minY")
    }

    @JvmStatic // todo delete jvm
    fun maxY(maxY: Int): IOreSeedDropRequirement = object : IOreSeedDropRequirement {
        override fun test(environment: OreSeedDropEnvironment) = environment.blockPos.y <= maxY
        override fun getDescriptions() = listOf("Max Y: $maxY")
    }
}