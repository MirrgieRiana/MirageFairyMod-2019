package miragefairy2019.mod.api.fairyweapon.item;

import miragefairy2019.mod3.fairy.api.IFairyType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public interface IItemFairyWeapon {

    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, @Nullable World world, List<String> tooltip, ITooltipFlag flag);

}
