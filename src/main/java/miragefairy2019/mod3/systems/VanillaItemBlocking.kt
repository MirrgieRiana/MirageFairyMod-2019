package miragefairy2019.mod3.systems

import miragefairy2019.lib.getLogger
import miragefairy2019.libkt.module
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main
import miragefairy2019.mod.configProperty
import mirrg.kotlin.castOrNull
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object VanillaItemBlocking {
    val logger = getLogger()
    val module = module {
        val disableVanillaWarpItems = configProperty { it.getBoolean("disableVanillaWarpItems", Main.categoryFeatures, false, "Disables vanilla items that allow warping.") }

        onInit {
            logger.info("disableVanillaWarpItems: ${disableVanillaWarpItems()}")
            if (disableVanillaWarpItems()) {
                MinecraftForge.EVENT_BUS.register(object {
                    @SubscribeEvent
                    fun handle(event: PlayerInteractEvent.RightClickItem) {
                        when (event.itemStack.item) {
                            Items.ENDER_PEARL,
                            Items.CHORUS_FRUIT
                            -> {
                                logger.info("Blocked: ${event.itemStack.displayName}(${event.itemStack}) by ${event.entityPlayer.name}(${event.entityPlayer.uniqueID})")
                                event.isCanceled = true
                                event.entityLiving.castOrNull<EntityPlayer>()?.sendStatusMessage(textComponent { "このアイテムは使用できません"() }, true) // TODO translate
                            }
                        }
                    }
                })
            }
        }
    }
}
