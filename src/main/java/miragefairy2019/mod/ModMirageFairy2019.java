package miragefairy2019.mod;

import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.InitializationContext;
import miragefairy2019.mod.modules.ModuleMain;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import miragefairy2019.mod.modules.fairyweapon.ModuleFairyWeapon;
import miragefairy2019.mod.modules.fertilizer.ModuleFertilizer;
import miragefairy2019.mod.modules.materialsfairy.ModuleMaterialsFairy;
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower;
import miragefairy2019.mod.modules.ore.ModuleOre;
import miragefairy2019.mod.modules.sphere.ModuleSphere;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModMirageFairy2019.MODID, name = ModMirageFairy2019.NAME, version = ModMirageFairy2019.VERSION)
public class ModMirageFairy2019
{

	public static final String MODID = "miragefairy2019";
	public static final String NAME = "MirageFairy2019";
	public static final String VERSION = "0.0.1";

	public EventRegistryMod erMod = new EventRegistryMod();

	public ModMirageFairy2019()
	{
		ModuleMain.init(erMod);
		ModuleFairy.init(erMod);
		ModuleFairyCrystal.init(erMod);
		ModuleFairyWeapon.init(erMod);
		ModuleFertilizer.init(erMod);
		ModuleMaterialsFairy.init(erMod);
		ModuleMirageFlower.init(erMod);
		ModuleOre.init(erMod);
		ModuleSphere.init(erMod);

		erMod.initCreativeTab.trigger().run();

	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

		erMod.preInit.trigger().accept(event);

		InitializationContext initializationContext = new InitializationContext(MODID, event.getSide(), ApiMain.creativeTab);

		erMod.registerBlock.trigger().accept(initializationContext);

		erMod.registerItem.trigger().accept(initializationContext);

		erMod.createItemStack.trigger().accept(initializationContext);

		erMod.hookDecorator.trigger().run();

	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

		erMod.init.trigger().accept(event);

		erMod.addRecipe.trigger().run();

		if (event.getSide().isClient()) erMod.registerItemColorHandler.trigger().run();

		erMod.registerTileEntity.trigger().run();

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

		erMod.postInit.trigger().accept(event);

	}

}
