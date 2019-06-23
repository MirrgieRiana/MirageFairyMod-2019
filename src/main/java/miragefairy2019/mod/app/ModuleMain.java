package miragefairy2019.mod.app;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.api.ApiMain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleMain
{

	public static void init(EventRegistryMod erMod)
	{
		erMod.initCreativeTab.register(() -> {
			ApiMain.creativeTab = new CreativeTabs("mirageFairy2019") { // TODO itemGroup.mirageFairy2019
				@Override
				@SideOnly(Side.CLIENT)
				public ItemStack getTabIconItem()
				{
					return new ItemStack(Items.LEAD);
				}
			};
		});
		erMod.preInit.register(e -> {
			ApiMain.logger = e.getModLog();
			ApiMain.side = e.getSide();
		});
	}

}
