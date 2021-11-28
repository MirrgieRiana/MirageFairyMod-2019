package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.lib.Configurator;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemVariantFairyMaterial extends ItemVariantMaterial {

    public final int tier;

    public ItemVariantFairyMaterial(String registryName, String unlocalizedName, int tier) {
        super(registryName, unlocalizedName);
        this.tier = tier;
    }

    //

    public Optional<Supplier<ItemStack>> osContainerItem = Optional.empty();

    public ItemStack getContainerItem() {
        return osContainerItem.map(Supplier<ItemStack>::get).orElse(ItemStack.EMPTY);
    }

    public static <V extends ItemVariantFairyMaterial> Function<Configurator<V>, Monad<Configurator<V>>> setterContainerItem(Optional<Supplier<ItemStack>> osContainerItem) {
        return c -> Monad.of(c)
                .peek(c2 -> c2.erMod.registerItem.register(ic -> c2.get().osContainerItem = osContainerItem));
    }

}
