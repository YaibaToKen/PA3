package com.mcmoddev.poweradvantage.blocks;

import com.mcmoddev.lib.block.MMDBlockWithTile;
import com.mcmoddev.poweradvantage.PowerAdvantage;
import com.mcmoddev.poweradvantage.tiles.TileInfiniteSteam;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInfiniteSteam extends MMDBlockWithTile<TileInfiniteSteam> {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockInfiniteSteam() {
		super(TileInfiniteSteam.class, () -> new TileInfiniteSteam(), Material.PISTON);
		setTranslationKey(PowerAdvantage.MODID + ".infinite_steam");
		//setRegistryName(new ResourceLocation(PowerAdvantage.MODID, name));
		this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing enumfacing = facing.getOpposite();

		if(enumfacing == EnumFacing.DOWN) {
			enumfacing = placer.getHorizontalFacing().getOpposite();
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}
}
