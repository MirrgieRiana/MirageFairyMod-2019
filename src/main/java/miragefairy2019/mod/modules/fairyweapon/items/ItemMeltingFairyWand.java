package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyCraftingToolBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMeltingFairyWand extends ItemFairyCraftingToolBase
{

	public ItemMeltingFairyWand()
	{
		super(Composite.empty()
			.add(Components.MIRAGIUM, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.flame), 1));
		this.setMaxDamage(32 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("金属を溶かすほどの情熱");

		super.addInformation(itemStack, world, tooltip, flag);

	}

}
