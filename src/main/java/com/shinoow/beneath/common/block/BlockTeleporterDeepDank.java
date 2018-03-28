package com.shinoow.beneath.common.block;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;
import com.shinoow.beneath.common.network.PacketDispatcher;
import com.shinoow.beneath.common.network.server.TeleportMessage;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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
		if(worldIn.isRemote)
			if(playerIn.getDistanceSq(pos) <= 8.5) {
				if(playerIn.posY >= pos.getY() && playerIn.posY <= pos.getY()+1)
					PacketDispatcher.sendToServer(new TeleportMessage(pos));
				else playerIn.sendStatusMessage(new TextComponentString("You need to be at the same Y-level as the teleporter!"), true);
			} else playerIn.sendStatusMessage(new TextComponentString("You're standing too far away from the teleporter!"), true);

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		return new TileEntityTeleporterDeepDank();
	}
}