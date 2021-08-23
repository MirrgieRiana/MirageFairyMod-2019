package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper;
import miragefairy2019.mod.lib.Configurator;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.modkt.api.fairystickcraft.ApiFairyStickCraft;
import miragefairy2019.modkt.impl.fairy.ErgType;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionReplaceBlock;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionUseItem;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftRecipe;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.function.Function;
import java.util.function.Supplier;

import static miragefairy2019.mod.api.composite.ApiComposite.instance;
import static miragefairy2019.mod.api.composite.Components.*;
import static miragefairy2019.mod.api.fairy.ApiFairy.getComponentAbilityType;
import static miragefairy2019.mod.lib.Configurator.*;

public class Loader {

    @SuppressWarnings("unused")
    public static void init(EventRegistryMod erMod) {

        // 技巧のステッキ
        fairyWeapon(erMod, ItemFairyWandCrafting::new, "crafting_fairy_wand", "fairyWandCrafting")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandCrafting"))
                .bind(addOreName("mirageFairy2019FairyStick"))
                .bind(addComponent(instance(wood.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getCraft()))))
                .bind(setFairyWandStatusOfTier(1))
                .get();

        // 技巧のステッキ2
        fairyWeapon(erMod, ItemFairyWandCrafting::new, "crafting_fairy_wand_2", "fairyWandCrafting2")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandCrafting"))
                .bind(addOreName("mirageFairy2019FairyStick"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getCraft()))))
                .bind(setFairyWandStatusOfTier(2))
                .get();

        // 技巧のステッキ3
        fairyWeapon(erMod, ItemFairyWandCrafting::new, "crafting_fairy_wand_3", "fairyWandCrafting3")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandCrafting"))
                .bind(addOreName("mirageFairy2019FairyStick"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(quartz.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getCraft()))))
                .bind(setFairyWandStatusOfTier(3))
                .get();

        // 技巧のステッキ4
        fairyWeapon(erMod, ItemFairyWandCrafting::new, "crafting_fairy_wand_4", "fairyWandCrafting4")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandCrafting"))
                .bind(addOreName("mirageFairy2019FairyStick"))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getCraft()))))
                .bind(setFairyWandStatusOfTier(4))
                .get();

        // 潤いのステッキ
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "hydrating_fairy_wand", "fairyWandHydrating")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandHydrating"))
                .bind(addOreName("container1000Water"))
                .bind(addComponent(instance(wood.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getWater()))))
                .bind(setFairyWandStatusOfTier(1))
                .get();

        // 潤いのステッキ2
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "hydrating_fairy_wand_2", "fairyWandHydrating2")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandHydrating"))
                .bind(addOreName("container1000Water"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getWater()))))
                .bind(setFairyWandStatusOfTier(2))
                .get();

        // 潤いのステッキ3
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "hydrating_fairy_wand_3", "fairyWandHydrating3")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandHydrating"))
                .bind(addOreName("container1000Water"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(quartz.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getWater()))))
                .bind(setFairyWandStatusOfTier(3))
                .get();

        // 潤いのステッキ4
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "hydrating_fairy_wand_4", "fairyWandHydrating4")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandHydrating"))
                .bind(addOreName("container1000Water"))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getWater()))))
                .bind(setFairyWandStatusOfTier(4))
                .get();

        // 紅蓮のステッキ
        fairyWeapon(erMod, ItemFairyWandMelting::new, "melting_fairy_wand", "fairyWandMelting")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandMelting"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFlame()))))
                .bind(setFairyWandStatusOfTier(2))
                .get();

        // 紅蓮のステッキ2
        fairyWeapon(erMod, ItemFairyWandMelting::new, "melting_fairy_wand_2", "fairyWandMelting2")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandMelting"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(quartz.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFlame()))))
                .bind(setFairyWandStatusOfTier(3))
                .get();

        // 紅蓮のステッキ3
        fairyWeapon(erMod, ItemFairyWandMelting::new, "melting_fairy_wand_3", "fairyWandMelting3")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandMelting"))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFlame()))))
                .bind(setFairyWandStatusOfTier(4))
                .get();

        // 破壊のステッキ
        fairyWeapon(erMod, ItemFairyWandBreaking::new, "breaking_fairy_wand", "fairyWandBreaking")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandBreaking"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getBreaking()))))
                .bind(setFairyWandStatusOfTier(2))
                .get();

        // 破壊のステッキ2
        fairyWeapon(erMod, ItemFairyWandBreaking::new, "breaking_fairy_wand_2", "fairyWandBreaking2")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandBreaking"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(quartz.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getBreaking()))))
                .bind(setFairyWandStatusOfTier(3))
                .get();

        // 破壊のステッキ3
        fairyWeapon(erMod, ItemFairyWandBreaking::new, "breaking_fairy_wand_3", "fairyWandBreaking3")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandBreaking"))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getBreaking()))))
                .bind(setFairyWandStatusOfTier(4))
                .get();

        // 氷晶のステッキ
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "freezing_fairy_wand", "fairyWandFreezing")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandFreezing"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFreeze()))))
                .bind(setFairyWandStatusOfTier(2))
                .get();

        // 氷晶のステッキ2
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "freezing_fairy_wand_2", "fairyWandFreezing2")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandFreezing"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(quartz.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFreeze()))))
                .bind(setFairyWandStatusOfTier(3))
                .get();

        // 氷晶のステッキ3
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "freezing_fairy_wand_3", "fairyWandFreezing3")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandFreezing"))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFreeze()))))
                .bind(setFairyWandStatusOfTier(4))
                .get();

        // 珠玉のステッキ
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "polishing_fairy_wand", "fairyWandPolishing")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandPolishing"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(quartz.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getCrystal()))))
                .bind(setFairyWandStatusOfTier(3))
                .get();

        // 珠玉のステッキ2
        fairyWeapon(erMod, ItemFairyWeaponCraftingTool::new, "polishing_fairy_wand_2", "fairyWandPolishing2")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandPolishing"))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getCrystal()))))
                .bind(setFairyWandStatusOfTier(4))
                .get();

        // 召喚のステッキ
        fairyWeapon(erMod, ItemFairyWandSummoning::new, "summoning_fairy_wand", "fairyWandSummoning")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandSummoning"))
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(quartz.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getSubmission()))))
                .bind(setFairyWandStatusOfTier(3))
                .get();

        // 召喚のステッキ2
        fairyWeapon(erMod, ItemFairyWandSummoning::new, "summoning_fairy_wand_2", "fairyWandSummoning2")
                .bind(addOreName("mirageFairy2019CraftingToolFairyWandSummoning"))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getSubmission()))))
                .bind(setFairyWandStatusOfTier(4))
                .get();

        //

        // ミラジウムの剣
        Configurator<ItemFairyWeaponBase> miragiumSword = fairyWeapon(erMod, ItemFairyWeaponBase::new, "miragium_sword", "miragiumSword")
                .bind(addComponent(instance(miragium.get(), 2)))
                .bind(addComponent(instance(wood.get(), 0.5)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getAttack()))))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getSlash()))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // 妖精剣
        Configurator<ItemFairySword> fairySword = fairyWeapon(erMod, ItemFairySword::new, "fairy_sword", "fairySword")
                .bind(addComponent(miragiumSword))
                .bind(addComponent(instance(apatite.get(), 1)))
                .bind(addComponent(instance(fluorite.get(), 1)))
                .bind(addComponent(instance(sulfur.get(), 1)))
                .bind(addComponent(instance(cinnabar.get(), 1)))
                .bind(addComponent(instance(magnetite.get(), 1)))
                .bind(addComponent(instance(moonstone.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getAttack()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // ミラジウムの斧
        Configurator<ItemMiragiumAxe> miragiumAxe = fairyWeapon(erMod, ItemMiragiumAxe::new, "miragium_axe", "miragiumAxe")
                .bind(addComponent(instance(miragium.get(), 3)))
                .bind(addComponent(instance(wood.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getSlash()))))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFell()))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // ロッドベース
        Configurator<ItemFairyWeaponBase> magicWandBase = fairyWeapon(erMod, ItemFairyWeaponBase::new, "magic_wand_base", "magicWandBase")
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(fluorite.get(), 1)))
                .bind(addComponent(instance(miragium.get(), 4)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getKnowledge()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 光のロッド
        Configurator<ItemMagicWandLight> magicWandLight = fairyWeapon(erMod, ItemMagicWandLight::new, "light_magic_wand", "magicWandLight")
                .bind(addComponent(magicWandBase))
                .bind(addComponent(instance(apatite.get(), 3)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getLight()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 収集のロッド
        Configurator<ItemMagicWandCollecting> magicWandCollecting = fairyWeapon(erMod, ItemMagicWandCollecting::new, "collecting_magic_wand", "magicWandCollecting")
                .bind(addComponent(magicWandBase))
                .bind(addComponent(instance(obsidian.get(), 2)))
                .bind(addComponent(instance(fluorite.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getWarp()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // ライトニングロッド
        Configurator<ItemMagicWandLightning> magicWandLightning = fairyWeapon(erMod, ItemMagicWandLightning::new, "lightning_magic_wand", "magicWandLightning")
                .bind(addComponent(magicWandBase))
                .bind(addComponent(instance(gold.get(), 4)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getThunder()))))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getEnergy()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // オカリナベース
        Configurator<ItemFairyWeaponBase> ocarinaBase = fairyWeapon(erMod, ItemFairyWeaponBase::new, "ocarina_base", "ocarinaBase")
                .bind(addComponent(instance(miragium.get(), 1)))
                .bind(addComponent(instance(apatite.get(), 4)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getArt()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 魅惑のオカリナ
        Configurator<ItemOcarinaTemptation> ocarinaTemptation = fairyWeapon(erMod, ItemOcarinaTemptation::new, "temptation_ocarina", "ocarinaTemptation")
                .bind(addComponent(ocarinaBase))
                .bind(addComponent(instance(pyrope.get(), 4)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFood()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 鐘ベース
        Configurator<ItemBellBase> bellBase = fairyWeapon(erMod, ItemBellBase::new, "bell_base", "bellBase")
                .bind(addComponent(instance(miragium.get(), 0.5)))
                .bind(addComponent(instance(miragium.get(), 3)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getSubmission()))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // 花摘みの鐘
        Configurator<ItemBellFlowerPicking> bellFlowerPicking = fairyWeapon(erMod, () -> new ItemBellFlowerPicking(0.1, 0.1, 0.05), "flower_picking_bell", "bellFlowerPicking")
                .bind(addComponent(bellBase))
                .bind(addComponent(instance(miragium.get(), 0.5)))
                .bind(addComponent(instance(magnetite.get(), 0.5)))
                .bind(addComponent(instance(pyrope.get(), 2)))
                .bind(addComponent(instance(gold.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFell()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 花摘みの鐘 II
        Configurator<ItemBellFlowerPicking> bellFlowerPicking2 = fairyWeapon(erMod, () -> new ItemBellFlowerPicking(0.2, 0.2, 0.1), "flower_picking_bell_2", "bellFlowerPicking2")
                .bind(addComponent(bellFlowerPicking))
                .bind(addComponent(instance(miragium.get(), 4)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFell()))))
                .bind(setWeaponStatusOfTier(4))
                .get();

        // クリスマスの鐘
        Configurator<ItemBellChristmas> bellChristmas = fairyWeapon(erMod, ItemBellChristmas::new, "christmas_bell", "bellChristmas")
                .bind(addComponent(bellBase))
                .bind(addComponent(instance(miragium.get(), 0.5)))
                .bind(addComponent(instance(magnetite.get(), 0.5)))
                .bind(addComponent(instance(gold.get(), 10)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getChristmas()))))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getAttack()))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // ミラジウムの大鎌
        Configurator<ItemMiragiumScythe> miragiumScythe = fairyWeapon(erMod, ItemMiragiumScythe::new, "miragium_scythe", "miragiumScythe")
                .bind(addComponent(instance(miragium.get(), 3)))
                .bind(addComponent(instance(wood.get(), 1)))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getSlash()))))
                .bind(addComponent(instance(getComponentAbilityType(ErgType.Companion.getFell()))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // レシピ登録
        erMod.addRecipe.register(() -> {

            // 丸石＞紅蓮→焼き石
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019CraftingToolFairyWandMelting"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionReplaceBlock(
                            () -> Blocks.COBBLESTONE.getDefaultState(),
                            () -> Blocks.STONE.getDefaultState())))
                    .get());

        });

    }

    //

    private static <I extends Item> Monad<Configurator<I>> fairyWeapon(EventRegistryMod erMod, Supplier<I> sItem, String registryName, String unlocalizedName) {
        return item(erMod, sItem, new ResourceLocation(ModMirageFairy2019.MODID, registryName), unlocalizedName)
                .bind(setCreativeTab(() -> ApiMain.creativeTab()))
                .bind(c -> {

                    c.erMod.registerItem.register(b -> {
                        if (ApiMain.side().isClient()) {

                            // 搭乗妖精の描画
                            MinecraftForge.EVENT_BUS.register(new Object() {
                                @SubscribeEvent
                                public void accept(ModelBakeEvent event) {
                                    IBakedModel bakedModel = event.getModelRegistry().getObject(new ModelResourceLocation(c.get().getRegistryName(), null));
                                    event.getModelRegistry().putObject(new ModelResourceLocation(c.get().getRegistryName(), null), new BakedModelBuiltinWrapper(bakedModel));
                                }
                            });

                            ModelLoader.setCustomModelResourceLocation(c.get(), 0, new ModelResourceLocation(c.get().getRegistryName(), null));

                        }
                    });

                    return Monad.of(c);
                });
    }

    private static <I extends Item> Function<Configurator<I>, Monad<Configurator<I>>> addOreName(String oreName) {
        return c -> Monad.of(c)
                .bind(onCreateItemStack(i -> OreDictionary.registerOre(oreName, new ItemStack(i, 1, OreDictionary.WILDCARD_VALUE))));

    }

    private static <I extends ItemFairyWeaponBase, I2 extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> addComponent(Configurator<I2> mci) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.addComponent(mci.get().getComposite())));
    }

    private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> addComponent(IComponentInstance componentInstance) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.addComponent(componentInstance)));
    }

    private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> setWeaponStatusOfTier(int tier) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.setMaxDamage(getDurability(tier) - 1)));
    }

    private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> setFairyWandStatusOfTier(int tier) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.setMaxDamage(getDurability(tier) / 2 - 1)));
    }

    private static int getDurability(int tier) {
        if (tier == 1) return 32;
        if (tier == 2) return 64;
        if (tier == 3) return 128;
        if (tier == 4) return 256;
        throw new IllegalArgumentException("Illegal tier: " + tier);
    }

}
