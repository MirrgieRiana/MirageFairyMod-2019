package miragefairy2019.lib.gui

import miragefairy2019.libkt.IArgb
import miragefairy2019.libkt.PointInt
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.contains
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawString
import miragefairy2019.libkt.drawStringCentered
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.minus
import miragefairy2019.libkt.toArgb
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

val GuiContainer.x get() = (width - xSize) / 2
val GuiContainer.y get() = (height - ySize) / 2
val GuiContainer.rectangle get() = RectangleInt(x, y, xSize, ySize)
val GuiContainer.position get() = PointInt(x, y)


class EventContainer<T> {
    private val listeners = mutableListOf<T>()
    operator fun invoke(listener: T) = run { listeners += listener }
    fun fire(invoker: (T) -> Unit) = listeners.forEach { invoker(it) }
}

interface IComponent2 {

    @SideOnly(Side.CLIENT)
    fun drawScreen(gui: GuiContainer, mouse: PointInt, partialTicks: Float)

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerForegroundLayer(gui: GuiContainer, mouse: PointInt)

    @SideOnly(Side.CLIENT)
    fun drawGuiContainerBackgroundLayer(gui: GuiContainer, mouse: PointInt, partialTicks: Float)

    @SideOnly(Side.CLIENT)
    fun mouseClicked(gui: GuiContainer, mouse: PointInt, mouseButton: Int)

}

class ComponentEventDistributor(val rectangle: RectangleInt) : IComponent2 {

    val onScreenDraw = EventContainer<(gui: GuiContainer, mouse: PointInt, partialTicks: Float) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun drawScreen(gui: GuiContainer, mouse: PointInt, partialTicks: Float) = onScreenDraw.fire { it(gui, mouse - gui.position, partialTicks) }


    val onForegroundDraw = EventContainer<(gui: GuiContainer, mouse: PointInt) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerForegroundLayer(gui: GuiContainer, mouse: PointInt) = onForegroundDraw.fire { it(gui, mouse - gui.position) }


    @SideOnly(Side.CLIENT)
    override fun drawGuiContainerBackgroundLayer(gui: GuiContainer, mouse: PointInt, partialTicks: Float) = Unit


    val onMouseClicked = EventContainer<(gui: GuiContainer, mouse: PointInt, mouseButton: Int) -> Unit>()

    @SideOnly(Side.CLIENT)
    override fun mouseClicked(gui: GuiContainer, mouse: PointInt, mouseButton: Int) = onMouseClicked.fire { it(gui, mouse - gui.position, mouseButton) }

}

@SideOnly(Side.CLIENT)
abstract class GuiComponentBase(container: Container) : GuiContainer(container) {
    val components = mutableListOf<IComponent2>()

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        components.forEach { it.drawScreen(this, PointInt(mouseX, mouseY), partialTicks) }
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()
        components.forEach { it.drawGuiContainerBackgroundLayer(this, PointInt(mouseX, mouseY), partialTicks) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        components.forEach { it.drawGuiContainerForegroundLayer(this, PointInt(mouseX, mouseY)) }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        components.forEach { it.mouseClicked(this, PointInt(mouseX, mouseY), mouseButton) }
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }
}

fun GuiComponentBase.component(rectangle: RectangleInt, block: ComponentEventDistributor.() -> Unit) {
    components += ComponentEventDistributor(rectangle).apply { block() }
}


enum class TextAlignment { LEFT, CENTER, RIGHT }


fun ComponentEventDistributor.label(sFontRenderer: () -> FontRenderer, color: IArgb = 0xFF000000.toArgb(), align: TextAlignment = TextAlignment.LEFT, getText: () -> String) {
    onForegroundDraw { _, _ ->
        when (align) {
            TextAlignment.LEFT -> sFontRenderer().drawString(getText(), rectangle, color.argb)
            TextAlignment.CENTER -> sFontRenderer().drawStringCentered(getText(), rectangle, color.argb)
            TextAlignment.RIGHT -> sFontRenderer().drawStringRightAligned(getText(), rectangle, color.argb)
        }
    }
}

fun ComponentEventDistributor.tooltip(vararg text: String) = tooltip { listOf(*text) }

fun ComponentEventDistributor.tooltip(getText: () -> List<String>) = onScreenDraw { gui, mouse, _ ->
    if (mouse in rectangle) gui.drawHoveringText(getText(), mouse.x + gui.x, mouse.y + gui.y)
}

fun ComponentEventDistributor.button(onClick: (mouseButton: Int) -> Unit) = onMouseClicked { _, mouse, mouseButton ->
    if (mouse in rectangle) onClick(mouseButton)
} // TODO 枠
