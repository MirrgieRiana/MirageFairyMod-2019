package miragefairy2019.modkt.impl.mana

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.modkt.api.mana.IManaType
import miragefairy2019.modkt.api.mana.ManaTypes
import net.minecraft.util.text.TextFormatting

fun ModInitializer.init() {
    onInstantiation {
        val values = mutableListOf<IManaType>()
        ManaTypes.shine = ManaType("shine", 0xC9FFFF, TextFormatting.WHITE).also { values += it }
        ManaTypes.fire = ManaType("fire", 0xCE0000, TextFormatting.RED).also { values += it }
        ManaTypes.wind = ManaType("wind", 0x00C600, TextFormatting.GREEN).also { values += it }
        ManaTypes.gaia = ManaType("gaia", 0x777700, TextFormatting.YELLOW).also { values += it }
        ManaTypes.aqua = ManaType("aqua", 0x0000E2, TextFormatting.BLUE).also { values += it }
        ManaTypes.dark = ManaType("dark", 0x191919, TextFormatting.DARK_GRAY).also { values += it }
        ManaTypes.values = values
    }
}


class ManaType(private val name: String, private val color: Int, private val textColor: TextFormatting) : IManaType {
    override fun getName() = name
    override fun getColor() = color
    override fun getTextColor() = textColor
}


val IManaType.displayName get() = text { translate("mirageFairy2019.mana.$name.name").color(textColor) }
