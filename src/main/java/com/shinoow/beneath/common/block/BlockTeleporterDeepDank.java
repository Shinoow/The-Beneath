package com.shinoow.beneath.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;
import com.shinoow.beneath.common.world.TeleporterDeepDank;

public class BlockTeleporterDeepDank extends Block implements ITileEntityProvider {

	public BlockTeleporterDeepDank() {
		super(Material.ROCK);
		setUnlocalizedName("teleporterbeneath");
		setCreativeTab(CreativeTabs.MISC);
		setHardness(20.0F);
		setResistance(Float.MAX_VALUE);
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return worldIn.provider.getDimension() == Beneath.dim ? -1.0F : super.getBlockHardness(blockState, worldIn, pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(playerIn instanceof EntityPlayerMP){
			EntityPlayerMP thePlayer = (EntityPlayerMP)playerIn;

			if (thePlayer.timeUntilPortal > 0)
				thePlayer.timeUntilPortal = thePlayer.getPortalCooldown();
			else if (thePlayer.dimension != Beneath.dim)
			{
				thePlayer.timeUntilPortal = 10;
				if(thePlayer.dimension == 1 && Beneath.dimTeleportation)
					thePlayer.setPositionAndUpdate(pos.getX(), pos.getY() + 1, pos.getZ());
				thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, Beneath.dim, new TeleporterDeepDank(thePlayer.mcServer.worldServerForDimension(Beneath.dim), pos, worldIn.provider));
			}
			else {
				thePlayer.timeUntilPortal = 10;
				if(Beneath.dimTeleportation){
					TileEntityTeleporterDeepDank tile = (TileEntityTeleporterDeepDank)worldIn.getTileEntity(pos);
					thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, tile.getDimension(), new TeleporterDeepDank(thePlayer.mcServer.worldServerForDimension(tile.getDimension()), pos, worldIn.provider));
				} else thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, 0, new TeleporterDeepDank(thePlayer.mcServer.worldServerForDimension(0), pos, worldIn.provider));
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		return new TileEntityTeleporterDeepDank();
	}
}