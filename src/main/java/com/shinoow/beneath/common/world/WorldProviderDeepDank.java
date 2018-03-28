package com.shinoow.beneath.common.world;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.world.gen.ChunkProviderDeepDank;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderDeepDank extends WorldProvider
{
	/**
	 * creates a new world chunk manager for WorldProvider
	 */
	@Override
	public void init()
	{
		biomeProvider = new BiomeProviderSingle(Beneath.deep_dank);
		setDimension(Beneath.dim);
	}

	/**
	 * Return Vec3D with biome specific fog color
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
	{
		return new Vec3d(0, 0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks)
	{
		if(cameraEntity instanceof EntityLivingBase && ((EntityLivingBase)cameraEntity).getActivePotionEffect(MobEffects.NIGHT_VISION) != null)
			return new Vec3d(Beneath.red, Beneath.green, Beneath.blue);
		return new Vec3d(0,0,0);
	}

	@Override
	public IChunkGenerator createChunkGenerator()
	{
		return new ChunkProviderDeepDank(world, world.getSeed());
	}

	/**
	 * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
	 */
	@Override
	public boolean isSurfaceWorld()
	{
		return false;
	}

	/**
	 * Will check if the x, z position specified is alright to be set as the map spawn point
	 */
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		return false;
	}

	/**
	 * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
	 */
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks)
	{
		return 0;
	}

	/**
	 * True if the player can respawn in this dimension (true = overworld, false = nether).
	 */
	@Override
	public boolean canRespawnHere()
	{
		return false;
	}

	/**
	 * Returns true if the given X,Z coordinate should show environmental fog.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int x, int z)
	{
		return true;
	}

	@Override
	public String getSaveFolder() {
		return "The_Beneath";
	}

	@Override
	public DimensionType getDimensionType()
	{
		return Beneath.deep_dank_dim;
	}
}