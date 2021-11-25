package miragefairy2019.mod3.placeditem

import miragefairy2019.libkt.block
import miragefairy2019.libkt.invoke
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.tileEntityRenderer
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.main.api.ApiMain.side
import miragefairy2019.mod3.placeditem.api.ApiPlacedItem
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard
import java.util.function.Consumer

object ModulePlacedItem {
    @JvmStatic // TODO jvm
    fun init(erMod: EventRegistryMod) {

        erMod {

            block(ModMirageFairy2019.MODID, "placed_item", { BlockPlacedItem() }) {
                unlocalizedName = "placedItem"
                ApiPlacedItem.blockPlacedItem = this
            }

            tileEntity(ModMirageFairy2019.MODID, "placed_item", TileEntityPlacedItem::class.java)

            tileEntityRenderer(TileEntityPlacedItem::class.java, { TileEntityRendererPlacedItem() })

        }

        // キーバインディング
        erMod.initKeyBinding.register(Runnable {
            if (side.isClient) {
                object : Any() {
                    @SideOnly(Side.CLIENT)
                    fun run() {
                        ApiPlacedItem.keyBindingPlaceItem = KeyBinding("miragefairy2019.placeItem", KeyConflictContext.IN_GAME, Keyboard.KEY_Z, "miragefairy2019 (MirageFairy2019)")
                    }
                }.run()
            }
        })

        // ネットワークメッセージ登録
        erMod.registerNetworkMessage.register(Runnable {
            ApiMain.simpleNetworkWrapper.registerMessage(PacketPlaceItem::class.java, MessagePlaceItem::class.java, 0, Side.SERVER)
        })

        // キーリスナー
        erMod.init.register(Consumer {
            if (side.isClient) {
                object : Any() {
                    @SideOnly(Side.CLIENT)
                    fun run() {
                        ClientRegistry.registerKeyBinding(ApiPlacedItem.keyBindingPlaceItem)
                        MinecraftForge.EVENT_BUS.register(object : Any() {
                            @SubscribeEvent
                            fun accept(event: InputUpdateEvent) {
                                while (ApiPlacedItem.keyBindingPlaceItem.isPressed) {

                                    // プレイヤー判定
                                    val player = event.entityPlayer
                                    if (player.isSpectator) return // スペクテイターの場合無効
                                    if (player !is EntityPlayerSP) return

                                    // 視線判定
                                    val result = player.rayTrace(player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue, 0f) ?: return // レイトレースが失敗したら中止
                                    if (result.typeOfHit != RayTraceResult.Type.BLOCK) return // ブロックにヒットしなければ中止

                                    // 成立
                                    ApiMain.simpleNetworkWrapper.sendToServer(MessagePlaceItem(result.blockPos, result.sideHit))

                                }
                            }
                        })
                    }
                }.run()
            }
        })
    }
}
