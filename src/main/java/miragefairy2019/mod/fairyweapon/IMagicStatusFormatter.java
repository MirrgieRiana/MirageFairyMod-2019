package miragefairy2019.mod.fairyweapon;

import net.minecraft.util.text.ITextComponent;

public interface IMagicStatusFormatter<T> {

    public ITextComponent getDisplayValue(IMagicStatusFunction<T> function, IMagicStatusFunctionArguments arguments);

}