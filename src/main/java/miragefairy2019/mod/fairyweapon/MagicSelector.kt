package miragefairy2019.mod.fairyweapon

import miragefairy2019.libkt.axisAlignedBBOf
import miragefairy2019.mod.fairyweapon.FairyWeaponUtils.spawnParticleSphericalRange
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.ceil
import kotlin.math.floor

open class MagicSelector(val world: World)

class MagicSelectorPosition(world: World, val position: Vec3d) : MagicSelector(world) {
    fun getMagicSelectorCircle(radius: Double) = MagicSelectorCircle(world, position, radius)
    fun getMagicSelectorSphere(radius: Double) = MagicSelectorSphere(world, position, radius)
    fun doEffect(color: Int) = spawnParticle(world, position, color)
}

class MagicSelectorCircle(world: World, val position: Vec3d, val radius: Double) : MagicSelector(world) {
    fun doEffect() = spawnParticleSphericalRange(world, position, radius)
    class BlockEntry(val blockPos: BlockPos, val distanceSquared: Double)

    val blockPosList
        get() = (floor(position.x - radius).toInt()..ceil(position.x + radius).toInt()).flatMap { x ->
            val y = position.y.toInt()
            (floor(position.z - radius).toInt()..ceil(position.z + radius).toInt()).mapNotNull { z ->
                val dx = (x + 0.5) - position.x
                val dz = (z + 0.5) - position.z
                val distanceSquared = dx * dx + dz * dz
                if (distanceSquared <= radius * radius) BlockEntry(BlockPos(x, y, z), distanceSquared) else null
            }
        }
}

class MagicSelectorSphere(world: World, val position: Vec3d, val radius: Double) : MagicSelector(world) {
    fun doEffect() = spawnParticleSphericalRange(world, position, radius)
}

/**
 * レイトレースを行い、何もヒットしなかった場合は空中の座標を得ます。
 * @param rayTraceResult
 * ブロックとエンティティのうち近い方にヒットしたリザルト。
 * 空中の場合、null。
 */
class MagicSelectorRayTrace private constructor(world: World, val rayTraceResult: RayTraceResult?, val position: Vec3d) : MagicSelector(world) {
    companion object {
        fun createAllEntities(world: World, player: EntityPlayer, additionalReach: Double): MagicSelectorRayTrace {
            val rayTraceResult = FairyWeaponUtils.rayTrace(world, player, false, additionalReach)
            val position = rayTraceResult?.hitVec ?: getSight(player, additionalReach)
            return MagicSelectorRayTrace(world, rayTraceResult, position)
        }

        fun createIgnoreEntity(world: World, player: EntityPlayer, additionalReach: Double): MagicSelectorRayTrace {
            val rayTraceResult = FairyWeaponUtils.rayTraceIgnoreEntity(world, player, false, additionalReach)
            val position = rayTraceResult?.hitVec ?: getSight(player, additionalReach)
            return MagicSelectorRayTrace(world, rayTraceResult, position)
        }

        fun <E : Entity> createWith(world: World, player: EntityPlayer, additionalReach: Double, classEntity: Class<E>, filterEntity: (E) -> Boolean): MagicSelectorRayTrace {
            val rayTraceResult = FairyWeaponUtils.rayTrace<E>(world, player, false, additionalReach, classEntity) { filterEntity(it!!) }
            val position = rayTraceResult?.hitVec ?: getSight(player, additionalReach)
            return MagicSelectorRayTrace(world, rayTraceResult, position)
        }
    }

    val magicSelectorPosition get() = MagicSelectorPosition(world, position)
    val isHit get() = rayTraceResult != null
    val hitBlockPos get() = rayTraceResult?.blockPos
    val blockPos get() = hitBlockPos ?: BlockPos(position)
    val hitEntity get() = rayTraceResult?.entityHit
    val sideHit get() = rayTraceResult?.sideHit
}

/** ある点を中心とした球形の範囲のエンティティを選択します。 */
class MagicSelectorEntitiesInSphericalRange<E : Entity>(
    world: World,
    private val position: Vec3d,
    private val radius: Double,
    classEntity: Class<E>,
    predicate: (E) -> Boolean,
    maxTargetCount: Int
) : MagicSelector(world) {
    val entities = run {
        fun getAllEntities(): List<E> = world.getEntitiesWithinAABB(classEntity, axisAlignedBBOf(position).grow(radius)) { e ->
            // 区間との距離
            fun d(value: Double, min: Double, max: Double) = when {
                value < min -> min - value
                value < max -> 0.0
                else -> value - max
            }

            e!!
            val dx = d(position.x, e.posX - e.width / 2.0, e.posX + e.width / 2.0)
            val dy = d(position.y, e.posY, e.posY + e.width)
            val dz = d(position.z, e.posZ - e.width / 2.0, e.posZ + e.width / 2.0)
            dx * dx + dy * dy + dz * dz <= radius * radius
        }
        getAllEntities().filter(predicate).sortedBy { it.getDistanceSq(position.x, position.y, position.z) }.take(maxTargetCount)
    }

    fun effect() {
        spawnParticleSphericalRange(world, position, radius)
        spawnParticleTargets(world, entities.map { Pair(it.positionVector, EnumTargetExecutability.EFFECTIVE) })
    }
}
