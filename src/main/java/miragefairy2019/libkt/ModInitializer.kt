package miragefairy2019.libkt

import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.multi.ItemMulti
import miragefairy2019.mod.lib.multi.ItemVariant
import miragefairy2019.mod3.main.api.ApiMain.side
import net.minecraft.block.Block
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import java.util.function.Supplier

typealias Module = ModInitializer.() -> Unit

class ModInitializer {
    val onInstantiation = EventRegistry0()
    val onInitCreativeTab = EventRegistry0()

    val onPreInit = EventRegistry1<FMLPreInitializationEvent>()
    val onRegisterBlock = EventRegistry0()
    val onRegisterItem = EventRegistry0()
    val onCreateItemStack = EventRegistry0()
    val onHookDecorator = EventRegistry0()
    val onInitKeyBinding = EventRegistry0()

    val onInit = EventRegistry1<FMLInitializationEvent>()
    val onAddRecipe = EventRegistry0()
    val onRegisterItemColorHandler = EventRegistry0()
    val onRegisterTileEntity = EventRegistry0()
    val onRegisterTileEntityRenderer = EventRegistry0()
    val onInitNetworkChannel = EventRegistry0()
    val onRegisterNetworkMessage = EventRegistry0()

    val onPostInit = EventRegistry1<FMLPostInitializationEvent>()
}

class EventRegistry0 {
    private val list = mutableListOf<() -> Unit>()
    operator fun invoke(listener: () -> Unit) = run { list += listener }
    operator fun invoke() = list.forEach { it() }
}

class EventRegistry1<E> {
    private val list = mutableListOf<E.() -> Unit>()
    operator fun invoke(listener: E.() -> Unit) = run { list += listener }
    operator fun invoke(event: E) = list.forEach { it(event) }
}


// Item

class ItemInitializer<I : Item>(val modInitializer: ModInitializer, private val sItem: () -> I) : Supplier<I> {
    val item get() = sItem()
    override fun get(): I = item
}

fun <I : Item> ModInitializer.item(creator: () -> I, registryName: String, block: ItemInitializer<I>.() -> Unit): ItemInitializer<I> {
    lateinit var item: I
    onRegisterItem {
        item = creator()
        item.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.ITEMS.register(item)
    }
    return ItemInitializer(this) { item }.also { it.block() }
}

fun <I : Item> ItemInitializer<I>.setUnlocalizedName(unlocalizedName: String) = modInitializer.onRegisterItem { item.unlocalizedName = unlocalizedName }
fun <I : Item> ItemInitializer<I>.setCreativeTab(creativeTab: () -> CreativeTabs) = modInitializer.onRegisterItem { item.creativeTab = creativeTab() }
fun <I : Item> ItemInitializer<I>.setCustomModelResourceLocation(metadata: Int = 0) = modInitializer.onRegisterItem { item.setCustomModelResourceLocation(metadata) }
fun <I : Item> I.setCustomModelResourceLocation(modelName: String, metadata: Int = 0) {
    if (side.isClient) {
        ModelLoader.setCustomModelResourceLocation(this, metadata, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, modelName), "normal"))
    }
}

fun <I : Item> I.setCustomModelResourceLocation(metadata: Int = 0) {
    if (side.isClient) {
        ModelLoader.setCustomModelResourceLocation(this, metadata, ModelResourceLocation(registryName!!, "normal"))
    }
}

fun <I : Item> ItemInitializer<I>.addOreName(oreName: String, metadata: Int = 0) = modInitializer.onCreateItemStack { item.addOreName(oreName, metadata) }
fun <I : Item> I.addOreName(oreName: String, metadata: Int = 0) = OreDictionary.registerOre(oreName, ItemStack(this, 1, metadata))


// ItemVariant

class ItemVariantInitializer<I : ItemMulti<V>, V : ItemVariant>(val itemInitializer: ItemInitializer<I>, private val sItemVariant: () -> V) : Supplier<V> {
    val itemVariant get() = sItemVariant()
    override fun get(): V = itemVariant
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemInitializer<I>.itemVariant(
    creator: () -> V,
    metadata: Int,
    block: ItemVariantInitializer<I, V>.() -> Unit
): ItemVariantInitializer<I, V> {
    lateinit var itemVariant: V
    modInitializer.onRegisterItem {
        itemVariant = creator()
        item.registerVariant(metadata, itemVariant)
    }
    return ItemVariantInitializer(this) { itemVariant }.also { it.block() }
}

fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.addOreName(oreName: String) = itemInitializer.modInitializer.onCreateItemStack { itemVariant.addOreName(oreName) }
fun <V : ItemVariant> V.addOreName(oreName: String) = OreDictionary.registerOre(oreName, ItemStack(item, 1, metadata))
fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.createItemStack(amount: Int = 1): ItemStack = itemVariant.createItemStack(amount)


// Block

class BlockInitializer<B : Block>(val modInitializer: ModInitializer, private val sBlock: () -> B) : Supplier<B> {
    val block get() = sBlock()
    override fun get(): B = block
}

fun <B : Block> ModInitializer.block(creator: () -> B, registryName: String, block: BlockInitializer<B>.() -> Unit): BlockInitializer<B> {
    lateinit var block2: B
    onRegisterBlock {
        block2 = creator()
        block2.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.BLOCKS.register(block2)
    }
    return BlockInitializer(this) { block2 }.also { it.block() }
}

fun <B : Block> BlockInitializer<B>.setUnlocalizedName(unlocalizedName: String) = modInitializer.onRegisterItem { block.unlocalizedName = unlocalizedName }
fun <B : Block> BlockInitializer<B>.setCreativeTab(creativeTab: () -> CreativeTabs) = modInitializer.onRegisterItem { block.setCreativeTab(creativeTab()) }


// misc

fun <T : TileEntity> ModInitializer.tileEntity(registerName: String, clazz: Class<T>) {
    onRegisterTileEntity {
        GameRegistry.registerTileEntity(clazz, ResourceLocation(ModMirageFairy2019.MODID, registerName))
    }
}

fun <T : TileEntity, R : TileEntitySpecialRenderer<T>> ModInitializer.tileEntityRenderer(classTileEntity: Class<T>, creatorRenderer: () -> R) {
    onRegisterTileEntityRenderer {
        if (side.isClient) {
            object : Any() {
                @SideOnly(Side.CLIENT)
                fun run() {
                    ClientRegistry.bindTileEntitySpecialRenderer(classTileEntity, creatorRenderer())
                }
            }.run()
        }
    }
}
