package miragefairy2019.lib

import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.unit
import miragefairy2019.libkt.x
import miragefairy2019.libkt.y
import mirrg.kotlin.atMost
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IContainerListener
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

// Api

interface IComponent {
    fun onInit() = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerBackgroundLayer(gui: GuiComponent, partialTicks: Float, mouseX: Int, mouseY: Int) = Unit

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouseX: Int, mouseY: Int) = Unit
}


// Implements

class WindowProperty(var value: Int = 0, val changeListener: () -> Unit = {})

class ContainerComponent : Container() {
    val components = mutableListOf<IComponent>()
    var width = 0
    var height = 0


    // Overrides

    fun init() {
        components.forEach { it.onInit() }
    }


    // Interact

    val interactInventories = mutableListOf<IInventory>()

    override fun canInteractWith(player: EntityPlayer) = interactInventories.all { it.isUsableByPlayer(player) }


    // Transfer

    class SlotGroup

    private val groupToSlots = mutableMapOf<SlotGroup, MutableList<ComponentSlot>>()
    private val slotToGroup = mutableMapOf<ComponentSlot, SlotGroup>()

    private val mapping = mutableMapOf<SlotGroup, MutableList<Pair<SlotGroup, Boolean>>>()

    fun addSlotTransferMapping(srcSlotGroup: SlotGroup, destSlotGroup: SlotGroup, isReversed: Boolean = false) {
        mapping.computeIfAbsent(srcSlotGroup) { mutableListOf() } += destSlotGroup to isReversed
    }

    fun getSlotTransferMapping(srcSlotGroup: SlotGroup): List<Pair<SlotGroup, Boolean>> = mapping[srcSlotGroup] ?: listOf()

    infix fun ComponentSlot.belongs(slotGroup: SlotGroup): ComponentSlot {
        val list = groupToSlots.computeIfAbsent(slotGroup) { mutableListOf() }
        list += this
        slotToGroup[this] = slotGroup
        return this
    }

    fun getComponentSlot(index: Int) = components.asSequence().filterIsInstance<ComponentSlot>().find { it.slot.slotNumber == index }
    fun getComponentSlots(slotGroup: SlotGroup): List<ComponentSlot> = groupToSlots[slotGroup] ?: listOf()
    fun getSlotGroup(componentSlot: ComponentSlot) = slotToGroup[componentSlot]

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val componentSlot = getComponentSlot(index) ?: return EMPTY_ITEM_STACK // スロットが存在しないなら終了
        if (!componentSlot.slot.hasStack) return EMPTY_ITEM_STACK // スロットが空なら終了

        val itemStack = componentSlot.slot.stack
        val itemStackOriginal = itemStack.copy()

        // 移動処理
        // itemStackを改変する
        val slotGroup = getSlotGroup(componentSlot)
        if (slotGroup != null) {
            val destComponentSlots = getSlotTransferMapping(slotGroup).map { (slotGroup, isReversed) ->
                val componentSlots = getComponentSlots(slotGroup)
                if (isReversed) componentSlots.reversed() else componentSlots
            }.flatten().map { it.slot }

            // 移動処理
            if (!mergeItemStack(itemStack, destComponentSlots).isChanged) return ItemStack.EMPTY

        }

        if (itemStack.isEmpty) { // スタックが丸ごと移動した
            componentSlot.slot.putStack(ItemStack.EMPTY)
        } else { // 部分的に残った
            componentSlot.slot.onSlotChanged()
        }

        if (itemStack.count == itemStackOriginal.count) return ItemStack.EMPTY // アイテムが何も移動していない場合は終了

        // スロットが改変を受けた場合にここを通過する

        componentSlot.slot.onTake(playerIn, itemStack)

        return itemStackOriginal
    }


    // Window Property

    val windowProperties = mutableMapOf<Int, WindowProperty>()

    override fun addListener(listener: IContainerListener) {
        super.addListener(listener)
        windowProperties.forEach { (id, windowProperty) ->
            listener.sendWindowProperty(this, id, windowProperty.value)
        }
    }

    // TODO detectAndSendChanges()

    @SideOnly(Side.CLIENT)
    override fun updateProgressBar(id: Int, data: Int) {
        val windowProperty = windowProperties.get(id) ?: return
        windowProperty.value = data
        windowProperty.changeListener()
    }


    // Public化
    public override fun addSlotToContainer(slot: Slot): Slot = super.addSlotToContainer(slot)

}

fun container(block: ContainerComponent.() -> Unit): ContainerComponent {
    val container = ContainerComponent()
    container.block()
    container.init()
    return container
}

@SideOnly(Side.CLIENT)
class GuiComponent(val container: ContainerComponent) : GuiContainer(container) {

    // Overrides

    init {
        xSize = container.width
        ySize = container.height
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()
        container.components.forEach { it.drawGuiContainerBackgroundLayer(this, partialTicks, mouseX, mouseY) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        container.components.forEach { it.drawGuiContainerForegroundLayer(this, mouseX, mouseY) }
    }


    // Public化
    val fontRenderer: FontRenderer get() = super.fontRenderer

}

@SideOnly(Side.CLIENT)
fun ContainerComponent.createGui() = GuiComponent(this)


// Utils

class ComponentSlot(val container: ContainerComponent, val x: Int, val y: Int, slotCreator: (x: Int, y: Int) -> Slot) : IComponent {
    val slot = slotCreator(x + 1, y + 1)

    override fun onInit() = unit { container.addSlotToContainer(slot) }

    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, partialTicks: Float, mouseX: Int, mouseY: Int) = drawSlot(gui.x + x.toFloat(), gui.y + y.toFloat())
}

enum class Alignment { LEFT, CENTER, RIGHT }

class ComponentLabel(val x: Int, val y: Int, val alignment: Alignment, val color: Int = 0x404040, val textSupplier: () -> ITextComponent?) : IComponent {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouseX: Int, mouseY: Int) {
        when (alignment) {
            Alignment.LEFT -> textSupplier()?.let { gui.fontRenderer.drawString(it.formattedText, x, y, color) }
            Alignment.CENTER -> textSupplier()?.let { gui.fontRenderer.drawStringCentered(it.formattedText, x, y, color) }
            Alignment.RIGHT -> textSupplier()?.let { gui.fontRenderer.drawStringRightAligned(it.formattedText, x, y, color) }
        }
    }
}

class ComponentBackgroundLabel(val x: Int, val y: Int, val alignment: Alignment, val color: Int = 0x404040, val textSupplier: () -> ITextComponent?) : IComponent {
    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiComponent, partialTicks: Float, mouseX: Int, mouseY: Int) {
        when (alignment) {
            Alignment.LEFT -> textSupplier()?.let { gui.fontRenderer.drawString(it.formattedText, gui.x + x, gui.y + y, color) }
            Alignment.CENTER -> textSupplier()?.let { gui.fontRenderer.drawStringCentered(it.formattedText, gui.x + x, gui.y + y, color) }
            Alignment.RIGHT -> textSupplier()?.let { gui.fontRenderer.drawStringRightAligned(it.formattedText, gui.x + x, gui.y + y, color) }
        }
    }
}

class SlotResult(private val player: EntityPlayer, inventory: IInventory, slotIndex: Int, x: Int, y: Int) : Slot(inventory, slotIndex, x, y) {

    override fun isItemValid(stack: ItemStack) = false


    // craft

    private var removeCount = 0

    override fun decrStackSize(amount: Int): ItemStack {
        if (hasStack) removeCount += amount atMost stack.count
        return super.decrStackSize(amount)
    }

    override fun onTake(player: EntityPlayer, itemStack: ItemStack): ItemStack {
        onCrafting(itemStack)
        super.onTake(player, itemStack)
        return itemStack
    }

    override fun onCrafting(itemStack: ItemStack, amount: Int) {
        removeCount += amount
        onCrafting(itemStack)
    }

    override fun onCrafting(itemStack: ItemStack) {
        itemStack.onCrafting(player.world, player, removeCount)
        removeCount = 0
    }

}