package miragefairy2019.mod.artifacts

import miragefairy2019.api.IPlaceAcceptorBlock
import miragefairy2019.api.IPlaceExchanger
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.modinitializer.tileEntityRenderer
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataUv
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.normal
import miragefairy2019.libkt.notEmptyOrNull
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.round

val dishModule = module {
    val blockDish = block({ BlockDish() }, "dish") {
        setUnlocalizedName("dish")
        setCreativeTab { Main.creativeTab }
        makeBlockStates { normal }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to "minecraft:blocks/quartz_block_top",
                    "top" to "minecraft:blocks/bone_block_top",
                    "main" to "minecraft:blocks/quartz_block_top"
                ),
                elements = listOf(
                    DataElement(
                        from = DataPoint(4.0, 1.0, 4.0),
                        to = DataPoint(12.0, 1.5, 12.0),
                        faces = DataFaces(
                            down = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            up = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#top"),
                            north = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            south = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            west = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            east = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main")
                        )
                    ),
                    DataElement(
                        from = DataPoint(6.0, 0.0, 6.0),
                        to = DataPoint(10.0, 1.0, 10.0),
                        faces = DataFaces(
                            down = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            up = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            north = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            south = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            west = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            east = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main")
                        )
                    )
                )
            )
        }
    }
    item({ ItemBlock(blockDish()) }, "dish") {
        setCustomModelResourceLocation()
        makeItemModel { block }
    }
    onMakeLang { enJa("tile.dish.name", "Dish", "皿") }
    tileEntity("dish", TileEntityDish::class.java)
    tileEntityRenderer(TileEntityDish::class.java, { TileEntityRendererDish() })
}

class BlockDish : BlockPlacedPedestal<TileEntityDish>(Material.CIRCUITS, { it as? TileEntityDish }) {
    init {
        // style
        soundType = SoundType.STONE

        // 挙動
        setHardness(0.8f)
        setHarvestLevel("pickaxe", -1)
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityDish()


    // 当たり判定

    private val boundingBox = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 2 / 16.0, 14 / 16.0)
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = boundingBox
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = NULL_AABB


    // アクション

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        val tileEntity = worldIn.getTileEntity(pos) as? TileEntityDish ?: return true
        if (playerIn.isSneaking) {
            tileEntity.standing = !tileEntity.standing
        } else {
            tileEntity.rotation += 45.0
            if (tileEntity.rotation >= 360) {
                tileEntity.rotation -= 360.0
            }
        }
        tileEntity.markDirty()
        tileEntity.sendUpdatePacket()
        return true
    }

    override fun onDeploy(world: World, blockPos: BlockPos, tileEntity: TileEntityDish, player: EntityPlayer, itemStack: ItemStack) {
        tileEntity.rotation = round(player.rotationYawHead.toDouble() / 45) * 45 // 角度調整
    }

}

class TileEntityDish : TileEntityPedestal() {
    var rotation = 0.0
    var standing = false

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        nbt.setDouble("rotation", rotation)
        nbt.setBoolean("standing", standing)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        rotation = nbt.getDouble("rotation")
        standing = nbt.getBoolean("standing")
    }
}

@SideOnly(Side.CLIENT)
class TileEntityRendererDish : TileEntityRendererPedestal<TileEntityDish>() {
    override fun transform(tileEntity: TileEntityDish) {
        GlStateManager.translate(0.5, 1.5 / 16.0 + 1 / 64.0, 0.5)
        GlStateManager.rotate((-tileEntity.rotation).toFloat(), 0f, 1f, 0f)
        if (tileEntity.standing) {
            GlStateManager.translate(0.0, 0.25, 0.0)
        } else {
            GlStateManager.rotate(90f, 1f, 0f, 0f)
        }
    }
}


// Common

abstract class BlockPedestal<T : TileEntityPedestal>(material: Material, private val validator: (TileEntity) -> T?) : BlockContainer(material), IPlaceAcceptorBlock {
    private fun getTileEntity(world: World, blockPos: BlockPos) = world.getTileEntity(blockPos)?.let { validator(it) }
    private fun getItemStack(world: World, blockPos: BlockPos) = getTileEntity(world, blockPos)?.itemStack


    // ドロップ

    // クリエイティブピックでの取得アイテム
    @Deprecated("Deprecated in Java")
    override fun getItem(world: World, blockPos: BlockPos, blockState: IBlockState): ItemStack = getItemStack(world, blockPos)?.notEmptyOrNull ?: super.getItem(world, blockPos, blockState)

    // 破壊時ドロップ
    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        val tileEntity = getTileEntity(world, blockPos) ?: return super.breakBlock(world, blockPos, blockState)
        InventoryHelper.spawnItemStack(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), tileEntity.itemStack)
        world.updateComparatorOutputLevel(blockPos, this)
    }

    // シルクタッチ無効
    override fun canSilkHarvest(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer) = false


    // レンダリング

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false


    // アクション

    override fun place(world: World, blockPos: BlockPos, player: EntityPlayer, placeExchanger: IPlaceExchanger): Boolean {
        val tileEntity = getTileEntity(world, blockPos) ?: return false // 異常なTileだった場合は中止
        if (tileEntity.itemStack.isEmpty) { // 設置

            val itemStack = placeExchanger.deploy()
            if (itemStack.isEmpty) return false

            // アイテムを設置
            tileEntity.itemStack = itemStack

            onDeploy(world, blockPos, tileEntity, player, itemStack)

            tileEntity.markDirty()
            tileEntity.sendUpdatePacket()

            return true
        } else { // 撤去

            // アイテムを回収
            val itemStackContained = tileEntity.itemStack
            tileEntity.itemStack = ItemStack.EMPTY

            onHarvest(world, blockPos, tileEntity, player)

            tileEntity.markDirty()
            tileEntity.sendUpdatePacket()

            // アイテムを増やす
            placeExchanger.harvest(itemStackContained)

            return true
        }
    }

    open fun onDeploy(world: World, blockPos: BlockPos, tileEntity: T, player: EntityPlayer, itemStack: ItemStack) = Unit
    open fun onHarvest(world: World, blockPos: BlockPos, tileEntity: T, player: EntityPlayer) = Unit
}

abstract class BlockPlacedPedestal<T : TileEntityPedestal>(material: Material, validator: (TileEntity) -> T?) : BlockPedestal<T>(material, validator) {
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos) = super.canPlaceBlockAt(worldIn, pos) && canSustain(worldIn, pos)
    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) = checkForDrop(worldIn, pos, state)
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos) = checkForDrop(worldIn, pos, state)
    private fun canSustain(world: IBlockAccess, blockPos: BlockPos): Boolean = world.getBlockState(blockPos.down()).isSideSolid(world, blockPos.down(), EnumFacing.UP)
    private fun checkForDrop(world: World, blockPos: BlockPos, blockState: IBlockState) {
        if (blockState.block !== this) return // 呼び出されたブロックが自分でない場合は無視
        if (canSustain(world, blockPos)) return // その場に存在できる場合は何もしない
        if (world.getBlockState(blockPos).block !== this) return // 指定座標に自ブロックが居ない場合は無視
        dropBlockAsItem(world, blockPos, blockState, 0)
        world.setBlockToAir(blockPos)
    }
}

abstract class TileEntityPedestal : TileEntity() {
    var itemStacks: NonNullList<ItemStack> = NonNullList.withSize(1, ItemStack.EMPTY)
    var itemStack: ItemStack
        get() = itemStacks[0]
        set(itemStack) = run { itemStacks[0] = itemStack }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        ItemStackHelper.saveAllItems(nbt, itemStacks)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        itemStacks.fill(ItemStack.EMPTY)
        ItemStackHelper.loadAllItems(nbt, itemStacks)
    }


    override fun getUpdatePacket() = SPacketUpdateTileEntity(pos, 9, updateTag)

    override fun getUpdateTag() = writeToNBT(NBTTagCompound())

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
        super.onDataPacket(net, pkt)
    }

    fun sendUpdatePacket() = world.playerEntities.forEach { if (it is EntityPlayerMP) it.connection.sendPacket(updatePacket) }
}

@SideOnly(Side.CLIENT)
abstract class TileEntityRendererPedestal<T : TileEntityPedestal> : TileEntitySpecialRenderer<T>() {
    override fun render(tileEntity: T, x: Double, y: Double, z: Double, partialTicks: Float, destroyStage: Int, alpha: Float) {
        val itemStack = tileEntity.itemStack.notEmptyOrNull ?: return
        matrix {
            GlStateManager.translate(x, y, z)
            transform(tileEntity)
            renderItem(itemStack)
        }
    }

    private inline fun <O> matrix(block: () -> O): O {
        GlStateManager.pushMatrix()
        try {
            return block()
        } finally {
            GlStateManager.popMatrix()
        }
    }

    abstract fun transform(tileEntity: T)

    private fun renderItem(itemStack: ItemStack) {
        if (itemStack.isEmpty) return
        GlStateManager.disableLighting()
        GlStateManager.scale(0.5f, 0.5f, 0.5f)
        GlStateManager.pushAttrib()
        RenderHelper.enableStandardItemLighting()
        Minecraft.getMinecraft().renderItem.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.popAttrib()
        GlStateManager.enableLighting()
    }
}
