package ds.mods.worldscript;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockScripted extends Block {

	public BlockScripted(int par1, Material par2Material) {
		super(par1, par2Material);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityScripted();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		//Now we will set the registered block
		//And invoke a method
		TileEntityScripted tile = (TileEntityScripted) par1World.getBlockTileEntity(par2, par3, par4);
		tile.block = par1World.isRemote ? WorldScript.c_br.blocks.get(par6ItemStack.getItemDamage()) : WorldScript.s_br.blocks.get(par6ItemStack.getItemDamage());
	}

}
