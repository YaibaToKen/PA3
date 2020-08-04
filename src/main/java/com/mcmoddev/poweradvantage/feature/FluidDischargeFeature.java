package com.mcmoddev.poweradvantage.feature;

import com.mcmoddev.lib.feature.FluidTankFeature;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidDischargeFeature extends FluidTankFeature implements ITickable {
	private final static int TICKS_PER_WORK = 30;
	private final TileEntity source;
	private int nextWork = TICKS_PER_WORK;

	public FluidDischargeFeature(String key, int capacity, TileEntity source) {
		super(key, capacity, (fs) -> true, (fs) -> true);
		this.source = source;
	}

	@Override
	public void update() {
		if ( nextWork > 0) {
			nextWork--;
			return;
		} else {
			nextWork = TICKS_PER_WORK;
		}

		for (EnumFacing f : EnumFacing.VALUES) {
			TileEntity te = source.getWorld().getTileEntity(source.getPos().offset(f));
			if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite())) {
				IFluidHandler fh = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, f.getOpposite());
				FluidStack z = fh.drain(1000, false);
				int x = getExternalTank().fill(z, false);
				getExternalTank().fill(fh.drain(x, true), true);
			}
		}
		
		if ( getExternalTank().getFluid() == null || getExternalTank().getFluidAmount() < 1000 ) return;
		
		BlockPos start = source.getPos().down();
		BlockFluidBase fb = (BlockFluidBase)getExternalTank().getFluid().getFluid().getBlock();
		
		if (fb.canDisplace(source.getWorld(), start)) {
			fb.displaceIfPossible(source.getWorld(), start);
			getExternalTank().drain(1000, true);
		}		
	}
}
