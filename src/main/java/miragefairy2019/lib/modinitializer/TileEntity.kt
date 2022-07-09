package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName
import miragefairy2019.libkt.resourceLocation
import miragefairy2019.mod.Main
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

fun <T : TileEntity> ModInitializer.tileEntity(registerName: String, tileEntityClass: Class<T>) = onRegisterTileEntity {
    GameRegistry.registerTileEntity(tileEntityClass, ResourceName(modId, registerName).resourceLocation)
}

fun <T : TileEntity, R : TileEntitySpecialRenderer<T>> ModInitializer.tileEntityRenderer(tileEntityClass: Class<T>, rendererCreator: () -> R) = onRegisterTileEntityRenderer {
    if (Main.side.isClient) {
        object : Any() {
            @SideOnly(Side.CLIENT)
            fun run() {
                ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, rendererCreator())
            }
        }.run()
    }
}