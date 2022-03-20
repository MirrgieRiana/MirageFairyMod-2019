package miragefairy2019.mod.magic4.api

import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.skill.api.IMastery
import net.minecraft.util.text.ITextComponent

interface FormulaArguments {
    val hasPartnerFairy: Boolean
    fun getRawMana(manaType: EnumManaType): Double
    fun getNormalizedMana(manaType: EnumManaType): Double
    fun getRawErg(ergType: EnumErgType): Double
    fun getNormalizedErg(ergType: EnumErgType): Double
    val cost: Double
    fun getSkillLevel(mastery: IMastery): Int
}

interface Formula<T> {
    fun calculate(formulaArguments: FormulaArguments): T
}

interface FormulaRenderer<T> {
    fun render(formulaArguments: FormulaArguments, formula: Formula<T>): ITextComponent
}

class MagicStatus<T>(val name: String, val formula: Formula<T>, val renderer: FormulaRenderer<T>)
