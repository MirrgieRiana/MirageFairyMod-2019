package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.norm1
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.mod.formula4.status
import miragefairy2019.mod.magic4.api.MagicArguments
import miragefairy2019.mod.magic4.api.MagicHandler
import miragefairy2019.mod.magic4.boolean
import miragefairy2019.mod.magic4.float2
import miragefairy2019.mod.magic4.integer
import miragefairy2019.mod.magic4.magic
import miragefairy2019.mod.magic4.percent0
import miragefairy2019.mod.magic4.percent2
import miragefairy2019.mod.magic4.positive
import miragefairy2019.mod.magic4.world
import miragefairy2019.mod3.artifacts.FairyLog
import miragefairy2019.mod3.erg.api.EnumErgType.CRAFT
import miragefairy2019.mod3.erg.api.EnumErgType.DESTROY
import miragefairy2019.mod3.erg.api.EnumErgType.HARVEST
import miragefairy2019.mod3.erg.api.EnumErgType.LEVITATE
import miragefairy2019.mod3.erg.api.EnumErgType.LIFE
import miragefairy2019.mod3.erg.api.EnumErgType.SLASH
import miragefairy2019.mod3.erg.api.EnumErgType.WARP
import miragefairy2019.mod3.mana.api.EnumManaType.AQUA
import miragefairy2019.mod3.mana.api.EnumManaType.DARK
import miragefairy2019.mod3.mana.api.EnumManaType.FIRE
import miragefairy2019.mod3.mana.api.EnumManaType.GAIA
import miragefairy2019.mod3.mana.api.EnumManaType.SHINE
import miragefairy2019.mod3.mana.api.EnumManaType.WIND
import miragefairy2019.mod3.skill.EnumMastery.harvest
import mirrg.boron.util.UtilsMath
import mirrg.kotlin.atLeast
import mirrg.kotlin.atMost
import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

class ItemMiragiumAxe : ItemFairyWeaponFormula4() {
    val additionalReach = status("additionalReach", { 0.0 + (!WIND + +LEVITATE) / 10.0 atMost 30.0 }, { float2 })
    val range = status("range", { (3.0 + (!GAIA + +HARVEST) / 5.0).toInt() atMost 100 }, { integer })
    val power = status("power", { 27.0 + (!DARK + +DESTROY) / 1.0 }, { float2 })
    val breakSpeed = status("breakSpeed", { 2.0 + (!AQUA + +SLASH) / 30.0 }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !harvest / 100.0 }, { percent0 })
    val fortune = status("fortune", { 0.0 + (!SHINE + +LIFE) / 20.0 }, { float2 })
    val wear = status("wear", { 0.1 / (1.0 + (!FIRE + +CRAFT) / 20.0) * (cost / 50.0) }, { percent2 })
    val collection = status("collection", { !WARP >= 10 }, { boolean.positive })

    init {
        setHarvestLevel("axe", 2) // 鉄相当
        destroySpeed = 6.0f // 鉄相当
    }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックでブロックを破壊" // TODO translate

    override fun getMagic() = magic {
        val magicSelectorRayTrace = MagicSelectorRayTrace.createIgnoreEntity(world, player, additionalReach()) // 視線判定
        val magicSelectorPosition = magicSelectorRayTrace.magicSelectorPosition // 視点判定

        fun createCursor(color: Int) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) = magicSelectorPosition.doEffect(color)
        }

        if (!hasPartnerFairy) return@magic createCursor(0xFF00FF) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic createCursor(0xFF0000) // 耐久判定
        val blockPos = magicSelectorRayTrace.hitBlockPos ?: return@magic createCursor(0xFF8800) // 対象判定
        val targets = getTargets(blockPos)
        if (targets.isEmpty()) return@magic createCursor(0xFF8800) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic createCursor(0xFFFF00) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (world.isRemote) return EnumActionResult.SUCCESS
                val breakSounds = mutableListOf<SoundEvent>()
                var remainingPower = power()
                var nextCoolTime = 0.0

                run finishMining@{
                    targets.forEach { targetBlockPos ->
                        val blockState = world.getBlockState(targetBlockPos)
                        val hardness = blockState.getBlockHardness(world, targetBlockPos)
                        if (hardness < 0) return@finishMining // 岩盤にあたった
                        val powerCost = hardness.toDouble() atLeast 0.2 // 最低パワー0.2（葉と同じ）は消費

                        if (remainingPower < powerCost) return@finishMining // パワーが足りない
                        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@finishMining // 耐久が足りない

                        // 成立

                        remainingPower -= powerCost // パワー消費
                        weaponItemStack.damageItem(UtilsMath.randomInt(world.rand, wear()), player) // 耐久消費

                        // 音取得
                        blockState.block.getSoundType(blockState, world, blockPos, player).breakSound.let {
                            if (it !in breakSounds) breakSounds += it
                        }

                        nextCoolTime += 20.0 * powerCost / breakSpeed() / speedBoost() // クールタイム加算

                        // 破壊
                        FairyWeaponUtils.breakBlock(
                            world,
                            player,
                            magicSelectorRayTrace.rayTraceResult!!.sideHit,
                            weaponItemStack,
                            targetBlockPos,
                            UtilsMath.randomInt(world.rand, fortune()),
                            collection()
                        )

                    }
                }

                breakSounds.take(4).forEach { world.playSound(null, player.posX, player.posY, player.posZ, it, SoundCategory.PLAYERS, 1.0f, 1.0f) } // 効果音
                player.cooldownTracker.setCooldown(this@ItemMiragiumAxe, ceil(nextCoolTime).toInt() atLeast 20) // クールタイム発生

                return EnumActionResult.SUCCESS
            }

            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.doEffect(0xFFFFFF)
                spawnParticleTargets(world, targets, { Vec3d(it).addVector(0.5, 0.5, 0.5) }, { 0x00FF00 })
            }
        }
    }

    private fun MagicArguments.getTargets(blockPos: BlockPos): List<BlockPos> {
        val visited = mutableListOf<BlockPos>()
        val logBlockPosList = search(range(), visited, listOf(blockPos)) { isLog(it) } // rangeの分だけ原木を幅優先探索
        visited.clear()
        visited += logBlockPosList
        val leavesBlockPosList = extendSearch(4, visited, logBlockPosList) { isLeaves(it) } // 4マスまで葉を探す
        return (logBlockPosList + leavesBlockPosList).sortedByDescending { it norm1 blockPos } // 遠い順に返す
    }

    private fun MagicArguments.isLog(blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockLog -> true
        FairyLog.blockFairyLog() -> true
        else -> false
    }

    private fun MagicArguments.isLeaves(blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockLeaves -> true
        else -> false
    }

}
