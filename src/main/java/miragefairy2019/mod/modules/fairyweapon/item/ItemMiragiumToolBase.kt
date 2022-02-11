package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.MagicScope
import miragefairy2019.mod.modules.fairyweapon.magic.EnumTargetExecutability
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorRayTrace
import miragefairy2019.mod.modules.fairyweapon.magic.UtilsMagic
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.skill.api.IMastery
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.ceil

fun MagicScope.fail(cursor: Vec3d, color: Int) = object : IMagicHandler {
    override fun onUpdate(itemSlot: Int, isSelected: Boolean) = UtilsMagic.spawnParticle(world, cursor, color)
}

abstract class ItemMiragiumToolBase(
    weaponManaType: EnumManaType,
    mastery: IMastery,
    additionalBaseStatus: Double
) : ItemFairyWeaponBase3(
    weaponManaType,
    mastery
) {
    val strength = createStrengthStatus(additionalBaseStatus, EnumErgType.SLASH)
    val extent = createExtentStatus(additionalBaseStatus, EnumErgType.SHOOT)
    val endurance = createEnduranceStatus(additionalBaseStatus, EnumErgType.SENSE)
    val production = createProductionStatus(additionalBaseStatus, EnumErgType.HARVEST)
    val cost = createCostStatus()

    val fortune = "fortune"({ double2.positive }) { !production * 0.03 }.setRange(0.0..100.0).setVisibility(Companion.EnumVisibility.DETAIL)
    val wear = "wear"({ percent2.positive }) { 1 / (25.0 + !endurance * 0.25) }.setVisibility(Companion.EnumVisibility.DETAIL)

    init {
        featureInformationList += "Right click: use magic" // TODO translate

        magic {
            val fairyType = findFairy(itemStack, player).orNull?.let { it.y!! } ?: ApiFairy.empty()!! // 妖精取得
            val selectorRayTrace = SelectorRayTrace(world, player, 0.0) // 視線判定
            if (fairyType.isEmpty) return@magic fail(selectorRayTrace.position, 0xFF00FF) // 妖精なし判定
            if (itemStack.itemDamage + ceil(!wear).toInt() > itemStack.maxDamage) return@magic fail(selectorRayTrace.position, 0xFF0000) // 材料なし判定
            val targets = selectorRayTrace.blockPos.let { if (selectorRayTrace.sideHit.isPresent) it.offset(selectorRayTrace.sideHit.get()) else it }.let { iterateTargets(this@magic, it) } // 対象判定
            if (!targets.hasNext()) return@magic fail(selectorRayTrace.position, 0x00FFFF) // ターゲットなし判定
            if (player.cooldownTracker.hasCooldown(this@ItemMiragiumToolBase)) return@magic fail(selectorRayTrace.position, 0xFFFF00) // クールダウン判定

            object : IMagicHandler { // 行使可能
                override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                    if (world.isRemote) { // クライアントワールドの場合、腕を振るだけ
                        player.swingArm(hand) // エフェクト
                        return EnumActionResult.SUCCESS
                    }

                    // 行使
                    var breakSound: SoundEvent? = null
                    var count = 0
                    run breakExecution@{
                        targets.forEach { target ->
                            val damage = world.rand.randomInt(!wear) // 耐久コスト
                            if (itemStack.itemDamage + damage > itemStack.maxDamage) return@breakExecution // 耐久不足なら終了

                            // 破壊成立
                            itemStack.damageItem(damage, player)
                            breakBlock(world, player, EnumFacing.UP, itemStack, target, world.rand.randomInt(!fortune), false)
                            val blockState = world.getBlockState(target)
                            breakSound = blockState.block.getSoundType(blockState, world, target, player).breakSound
                            count++
                        }
                    }

                    // 破壊時
                    if (count > 0) {
                        breakSound?.let { world.playSound(null, player.posX, player.posY, player.posZ, it, player.soundCategory, 1.0f, 1.0f) } // エフェクト
                        player.cooldownTracker.setCooldown(this@ItemMiragiumToolBase, (getCoolTime(this@magic)).toInt()) // クールタイム
                    }

                    // エフェクト
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.soundCategory, 1.0f, 1.0f)
                    player.spawnSweepParticles()

                    return EnumActionResult.SUCCESS
                }

                override fun onUpdate(itemSlot: Int, isSelected: Boolean) {
                    selectorRayTrace.doEffect(0xFFFFFF)
                    UtilsMagic.spawnParticleTargets(
                        world,
                        targets.asSequence().toList().map { Pair(Vec3d(it).addVector(0.5, 0.5, 0.5), EnumTargetExecutability.EFFECTIVE) }
                    )
                }
            }
        }
    }

    /**
     * このイテレータは破壊処理中に逐次的に呼び出されるパターンと、破壊前に一括で呼び出されるパターンがあります。
     * 内部で必ず[canBreak]による破壊可能判定を行わなければなりません。
     */
    abstract fun iterateTargets(magicScope: MagicScope, blockPosBase: BlockPos): Iterator<BlockPos>
    abstract fun getCoolTime(magicScope: MagicScope): Double

    @Suppress("SimplifyBooleanWithConstants")
    open fun canBreak(magicScope: MagicScope, blockPos: BlockPos) = true
            && magicScope.world.getBlockState(blockPos).getBlockHardness(magicScope.world, blockPos) >= 0 // 岩盤であってはならない
            && isEffective(magicScope.itemStack, magicScope.world.getBlockState(blockPos)) // 効果的でなければならない
}
