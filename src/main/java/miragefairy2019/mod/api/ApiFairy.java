package miragefairy2019.mod.api;

import static net.minecraft.util.text.TextFormatting.*;

import miragefairy2019.mod.api.fairy.IAbilityType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ApiFairy
{

	public static CreativeTabs creativeTab;

	public static Item[] itemFairyList;

	public static ItemStack itemStackFairyMain;

	public static enum EnumAbilityType implements IAbilityType
	{
		attack(DARK_RED),
		craft(GOLD),
		fell(DARK_RED),
		light(YELLOW),
		flame(RED),
		water(AQUA),
		crystal(AQUA),
		art(YELLOW),
		;

		private final TextFormatting colorText;

		private EnumAbilityType(TextFormatting colorText)
		{
			this.colorText = colorText;
		}

		@Override
		public String getName()
		{
			return name();
		}

		@Override
		public TextFormatting getTextColor()
		{
			return colorText;
		}

	}

}
