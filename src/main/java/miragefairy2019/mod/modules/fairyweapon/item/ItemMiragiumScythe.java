package miragefairy2019.mod.modules.fairyweapon.item;

import static miragefairy2019.mod.api.fairyweapon.formula.ApiFormula.*;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.modules.fairyweapon.magic.EnumTargetExecutability;
import miragefairy2019.mod.modules.fairyweapon.magic.MagicExecutor;
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorRayTrace;
import miragefairy2019.mod.modules.fairyweapon.magic.UtilsMagic;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMiragiumScythe extends ItemFairyWeaponBase
{

	public IMagicStatus<Double> wear = registerMagicStatus("wear", formatterPercent1(),
		val(1 / 25.0));

	public IMagicStatus<Double> coolTime = registerMagicStatus("coolTime", formatterTick(),
		val(20));

	//

	public MagicExecutor getExecutor(ItemFairyWeaponBase item, World world, ItemStack itemStack, EntityPlayer player)
	{

		// 妖精取得
		IFairyType fairyType = findFairy(itemStack, player).map(t -> t.y).orElseGet(ApiFairy::empty);

		// 視線判定
		SelectorRayTrace selectorRayTrace = new SelectorRayTrace(world, player, 0);

		// 対象判定
		List<BlockPos> blockPoses;
		{
			List<BlockPos> blockPoses2;
			blockPoses2 = getTargets(world, selectorRayTrace.getBlockPos());
			if (blockPoses2.size() == 0) {
				if (selectorRayTrace.getSideHit().isPresent()) {
					blockPoses2 = getTargets(world, selectorRayTrace.getBlockPos().offset(selectorRayTrace.getSideHit().get()));
				}
			}
			blockPoses = blockPoses2;
		}

		// 妖精なし判定
		if (fairyType.isEmpty()) {
			return new MagicExecutor() {
				@Override
				public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
				{
					selectorRayTrace.doEffect(0xFF00FF);
				}
			};
		}

		// 材料なし判定
		if (itemStack.getItemDamage() + (int) Math.ceil(wear.get(fairyType)) > itemStack.getMaxDamage()) {
			return new MagicExecutor() {
				@Override
				public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
				{
					selectorRayTrace.doEffect(0xFF0000);
				}
			};
		}

		// ターゲットなし判定
		if (blockPoses.size() == 0) {
			return new MagicExecutor() {
				@Override
				public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
				{
					selectorRayTrace.doEffect(0x00FFFF);
				}
			};
		}

		// クールダウン判定
		if (player.getCooldownTracker().hasCooldown(item)) {
			return new MagicExecutor() {
				@Override
				public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
				{
					selectorRayTrace.doEffect(0xFFFF00);
				}
			};
		}

		// 発動可能
		return new MagicExecutor() {
			@Override
			public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
			{
				if (world.isRemote) return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

				// 音取得
				SoundEvent breakSound;
				{
					BlockPos blockPos = blockPoses.get(0);
					IBlockState blockState = world.getBlockState(blockPos);
					breakSound = blockState.getBlock().getSoundType(blockState, world, blockPos, player).getBreakSound();
				}

				int count = 0;
				for (BlockPos blockPos : blockPoses) {

					// 耐久コスト
					int damage = UtilsMath.randomInt(world.rand, wear.get(fairyType));

					// 耐久不足
					if (itemStack.getItemDamage() + damage > itemStack.getMaxDamage()) break;

					// 発動
					itemStack.damageItem(damage, player);
					breakBlock(world, player, EnumFacing.UP, itemStack, blockPos, 0, false);
					count++;

				}

				if (count > 0) {

					// エフェクト
					world.playSound(null, player.posX, player.posY, player.posZ, breakSound, player.getSoundCategory(), 1.0F, 1.0F);
					world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
					player.spawnSweepParticles();
					player.swingArm(hand);

					// クールタイム
					player.getCooldownTracker().setCooldown(item, (int) (double) coolTime.get(fairyType));

				}

				return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
			}

			@Override
			public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
			{
				selectorRayTrace.doEffect(0xFFFFFF);
				UtilsMagic.spawnParticleTargets(world, ISuppliterator.ofIterable(blockPoses)
					.map(t -> Tuple.of(new Vec3d(t), EnumTargetExecutability.EFFECTIVE)));
			}
		};
	}

	private List<BlockPos> getTargets(World world, BlockPos blockPos)
	{
		List<Tuple<BlockPos, Double>> tuples = new ArrayList<>();
		for (int xi = -2; xi <= 2; xi++) {
			for (int yi = -0; yi <= 0; yi++) {
				for (int zi = -2; zi <= 2; zi++) {
					BlockPos blockPos2 = blockPos.add(xi, yi, zi);
					IBlockState blockState = world.getBlockState(blockPos2);
					if (blockState.getMaterial() == Material.PLANTS ||
						blockState.getMaterial() == Material.LEAVES ||
						blockState.getMaterial() == Material.VINE ||
						blockState.getMaterial() == Material.GRASS ||
						blockState.getMaterial() == Material.CACTUS) {
						if (blockState.getBlockHardness(world, blockPos2) == 0) {
							tuples.add(Tuple.of(blockPos2, blockPos2.distanceSq(blockPos)));
						}
					}
				}
			}
		}
		return ISuppliterator.ofIterable(tuples)
			.sortedDouble(Tuple::getY)
			.map(Tuple::getX)
			.toList();
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformationFunctions(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		super.addInformationFunctions(itemStack, world, tooltip, flag);

		tooltip.add(TextFormatting.RED + "Right click: use magic");

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{

		// アイテム取得
		ItemStack itemStack = player.getHeldItem(hand);

		return getExecutor(this, world, itemStack, player).onItemRightClick(world, player, hand);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{

		// クライアントのみ
		if (!ApiMain.side().isClient()) return;

		// プレイヤー取得
		if (!(entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) entity;

		// アイテム取得
		if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

		getExecutor(this, world, itemStack, player).onUpdate(itemStack, world, entity, itemSlot, isSelected);

	}

}
