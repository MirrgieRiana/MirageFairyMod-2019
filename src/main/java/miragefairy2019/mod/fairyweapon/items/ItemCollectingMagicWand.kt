package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.entities
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.lib.sphere
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent0
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil
import kotlin.math.floor

class ItemCollectingMagicWand : ItemFairyWeaponMagic4() {
    val speed = status("speed", { (5.0 + !Mana.WIND / 5.0 + !Erg.WARP / 2.0) }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.magicCombat / 100 }, { percent0 })
    val additionalReach = status("additionalReach", { 0.0 + (!Mana.GAIA + !Erg.SHOOT) / 5.0 atMost 30.0 }, { float2 })
    val radius = status("radius", { 4.0 + (!Mana.AQUA + !Erg.SENSE) / 20.0 atMost 20.0 }, { float2 })
    val maxTargetCount = status("maxTargetCount", { floor(20.0 * costFactor * costFactor).toInt() atLeast 1 }, { integer })
    val wear = status("wear", { 0.1 / (1.0 + (!Mana.FIRE + !Erg.SPACE) / 20.0) }, { percent0 })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックでアイテムを回収" // TODO translate

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, additionalReach()) // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定
        val rangeMagicSelector = cursorMagicSelector.sphere(radius())
        val targetsMagicSelector = rangeMagicSelector.entities(EntityItem::class.java, { true }, maxTargetCount())
        val targets = targetsMagicSelector.item.entities

        fun pass(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.PASS
            }
        }

        fun fail(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic fail(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic fail(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        if (targets.isEmpty()) return@magic pass(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic pass(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) {

                    var successed = 0
                    run a@{
                        targets.forEach { target ->

                            // ターゲットごとの消費確認
                            if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@a // 耐久がないなら中断

                            // ターゲットごとの効果
                            successed++
                            target.setPosition(player.posX, player.posY, player.posZ)
                            target.setNoPickupDelay()

                            // ターゲットごとの消費
                            weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久値の消費

                        }
                    }

                    if (successed > 0) {

                        // 行使ごとの消費
                        player.cooldownTracker.setCooldown(this@ItemCollectingMagicWand, ceil(successed * 20.0 / (speed() * speedBoost())).toInt()) // クールタイム

                        // 行使ごとのエフェクト
                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F) // 音

                    }

                } else {

                    // エフェクト
                    player.swingArm(hand) // 腕を振る

                }
                return EnumActionResult.SUCCESS
            }
        }
    }
}