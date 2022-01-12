package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.copy
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.OreIngredientComplex
import miragefairy2019.mod.lib.UtilsMinecraft
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.mana.div
import miragefairy2019.mod3.playeraura.api.IFoodAuraContainer
import miragefairy2019.modkt.impl.fairy.erg
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.util.Constants
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.Optional
import java.util.function.Predicate

lateinit var itemBakedFairy: () -> ItemBakedFairy

class ItemBakedFairy : ItemFood(0, 0.0f, false), IFoodAuraContainer {
    companion object {
        fun getFairy(itemStack: ItemStack): ItemStack? {
            if (!itemStack.hasTagCompound()) return null
            val nbt = itemStack.tagCompound!!
            if (!nbt.hasKey("Fairy", Constants.NBT.TAG_COMPOUND)) return null
            val fairy = nbt.getCompoundTag("Fairy")
            if (!fairy.hasKey("CombinedFairy", Constants.NBT.TAG_COMPOUND)) return null
            return ItemStack(fairy.getCompoundTag("CombinedFairy"))
        }

        fun setFairy(itemStack: ItemStack, itemStackFairy: ItemStack) {
            if (!itemStack.hasTagCompound()) itemStack.tagCompound = NBTTagCompound()
            val nbt = itemStack.tagCompound!!
            if (!nbt.hasKey("Fairy", Constants.NBT.TAG_COMPOUND)) nbt.setTag("Fairy", NBTTagCompound())
            val fairy = nbt.getCompoundTag("Fairy")
            fairy.setTag("CombinedFairy", itemStackFairy.writeToNBT(NBTTagCompound()))
        }
    }


    override fun getItemStackDisplayName(itemStack: ItemStack): String = getFairy(itemStack)?.fairyType?.let { UtilsMinecraft.translateToLocalFormatted("$unlocalizedName.format", it.displayName.formattedText) } ?: UtilsMinecraft.translateToLocal("$unlocalizedName.name")

    // TODO 専用クリエイティブタブ
    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return
        val itemStack = ItemStack(this)
        setFairy(itemStack, FairyTypes.instance.magentaGlazedTerracotta.main.createItemStack())
        items += itemStack
    }


    override fun getHealAmount(itemStack: ItemStack) = 1 + (getFairy(itemStack)?.fairyType?.let { it.erg(EnumErgType.LIFE).toInt() / 4 } ?: 0)
    override fun getSaturationModifier(itemStack: ItemStack) = getHealAmount(itemStack) * 0.1f
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 16
    override fun onItemUseFinish(itemStack: ItemStack, world: World, entity: EntityLivingBase): ItemStack {
        super.onItemUseFinish(itemStack, world, entity)
        return if (!itemStack.isEmpty) {
            if (!world.isRemote) entity.entityDropItem(ItemStack(Items.BOWL), 0.0f)
            itemStack
        } else {
            ItemStack(Items.BOWL)
        }
    }


    override fun getFoodAura(itemStack: ItemStack) = Optional.ofNullable(getFairy(itemStack)?.fairyType?.let { it.manaSet / (it.cost / 50.0) })
}

class RecipeFairyBaking : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        registryName = ResourceLocation(ModMirageFairy2019.MODID, "fairy_baking")
    }

    private data class MatchResult(var itemStackWand: ItemStack, var itemStackFairy: ItemStack)

    private fun match(inventoryCrafting: InventoryCrafting): MatchResult? {
        val used = (0 until inventoryCrafting.sizeInventory).map { false }.toMutableList()

        fun <T : Any> pull(function: (ItemStack) -> T?): T? {
            repeat(inventoryCrafting.sizeInventory) { i ->
                if (!used[i]) {
                    val itemStack: ItemStack = inventoryCrafting.getStackInSlot(i)
                    val result = function(itemStack)
                    if (result != null) {
                        used[i] = true
                        return result
                    }
                }
            }
            return null
        }

        fun find(predicate: (ItemStack) -> Boolean) = pull { if (predicate(it)) it else null }
        fun find(predicate: Predicate<ItemStack>) = find { predicate.test(it) }

        // 探索
        val itemStackWand = find(OreIngredientComplex("mirageFairy2019CraftingToolFairyWandMelting")) ?: return null // 紅蓮杖
        val itemStackFairy = find { it.item == Fairy.listItemFairy[0] } ?: return null // 妖精
        find(Ingredient.fromItem(Items.SUGAR)) ?: return null // 砂糖
        find(Ingredient.fromItem(Items.BOWL)) ?: return null // ボウル

        // 余りがあってはならない
        repeat(inventoryCrafting.sizeInventory) { i ->
            if (!used[i]) {
                if (!inventoryCrafting.getStackInSlot(i).isEmpty) {
                    return null
                }
            }
        }

        return MatchResult(itemStackWand, itemStackFairy)
    }


    override fun matches(inventoryCrafting: InventoryCrafting, world: World) = match(inventoryCrafting) != null

    override fun getCraftingResult(inventoryCrafting: InventoryCrafting): ItemStack {
        val result = match(inventoryCrafting) ?: return ItemStack.EMPTY
        val itemStackBakedFairy = ItemStack(itemBakedFairy())
        ItemBakedFairy.setFairy(itemStackBakedFairy, result.itemStackFairy.copy(1))
        return itemStackBakedFairy
    }

    override fun getRemainingItems(inventoryCrafting: InventoryCrafting): NonNullList<ItemStack> {
        val result = match(inventoryCrafting) ?: return NonNullList.create()
        val list = NonNullList.withSize(inventoryCrafting.sizeInventory, ItemStack.EMPTY)
        list.forEachIndexed { i, _ ->
            list[i] = ForgeHooks.getContainerItem(inventoryCrafting.getStackInSlot(i))
        }
        return list
    }

    override fun getRecipeOutput(): ItemStack = ItemStack.EMPTY
    override fun isDynamic() = true
    override fun canFit(width: Int, height: Int) = width * height >= 2
}
