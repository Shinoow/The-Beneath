package com.shinoow.beneath.common.world.gen;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.handler.OreGenHandler;
import com.shinoow.beneath.common.handler.OreEntry;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenDungeons;

public class ChunkProviderDeepDank implements IChunkGenerator
{
	protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
	protected static final IBlockState STONE = Blocks.STONE.getDefaultState();
	protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
	protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
	private final World world;
	private final Random rand;
	/** Holds the noise used to determine whether slowsand can be generated at a location */
	private double[] slowsandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] depthBuffer = new double[256];
	private double[] buffer;
	private NoiseGeneratorOctaves lperlinNoise1;
	private NoiseGeneratorOctaves lperlinNoise2;
	private NoiseGeneratorOctaves perlinNoise1;
	/** Determines whether slowsand or gravel can be generated at a location */
	private NoiseGeneratorOctaves slowsandGravelNoiseGen;
	/** Determines whether something other than nettherack can be generated at a location */
	private NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
	public NoiseGeneratorOctaves scaleNoise;
	public NoiseGeneratorOctaves depthNoise;
	private final WorldGenBush brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
	private final WorldGenBush redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
	private MapGenBase genDankCaves = new MapGenCavesDeepDank();
	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;

	public ChunkProviderDeepDank(World worldIn, long seed)
	{
		world = worldIn;
		rand = new Random(seed);
		lperlinNoise1 = new NoiseGeneratorOctaves(rand, 16);
		lperlinNoise2 = new NoiseGeneratorOctaves(rand, 16);
		perlinNoise1 = new NoiseGeneratorOctaves(rand, 16);
		slowsandGravelNoiseGen = new NoiseGeneratorOctaves(rand, 4);
		netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(rand, 4);
		scaleNoise = new NoiseGeneratorOctaves(rand, 16);
		depthNoise = new NoiseGeneratorOctaves(rand, 16);
		worldIn.setSeaLevel(63);

		net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextHell ctx =
				new net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextHell(lperlinNoise1, lperlinNoise2, perlinNoise1, slowsandGravelNoiseGen, netherrackExculsivityNoiseGen, scaleNoise, depthNoise);
		ctx = net.minecraftforge.event.terraingen.TerrainGen.getModdedNoiseGenerators(worldIn, rand, ctx);
		lperlinNoise1 = ctx.getLPerlin1();
		lperlinNoise2 = ctx.getLPerlin2();
		perlinNoise1 = ctx.getPerlin();
		slowsandGravelNoiseGen = ctx.getPerlin2();
		netherrackExculsivityNoiseGen = ctx.getPerlin3();
		scaleNoise = ctx.getScale();
		depthNoise = ctx.getDepth();
	}

	public void prepareHeights(int p_185936_1_, int p_185936_2_, ChunkPrimer primer)
	{
		int i = 4;
		int k = i + 1;
		int l = 33; //17
		int i1 = i + 1;
		buffer = getHeights(buffer, p_185936_1_ * i, 0, p_185936_2_ * i, k, l, i1);

		for (int j1 = 0; j1 < i; ++j1)
			for (int k1 = 0; k1 < i; ++k1)
				for (int l1 = 0; l1 < 32; ++l1)
				{
					double d0 = 0.125D;
					double d1 = buffer[((j1 + 0) * i1 + k1 + 0) * l + l1 + 0];
					double d2 = buffer[((j1 + 0) * i1 + k1 + 1) * l + l1 + 0];
					double d3 = buffer[((j1 + 1) * i1 + k1 + 0) * l + l1 + 0];
					double d4 = buffer[((j1 + 1) * i1 + k1 + 1) * l + l1 + 0];
					double d5 = (buffer[((j1 + 0) * i1 + k1 + 0) * l + l1 + 1] - d1) * d0;
					double d6 = (buffer[((j1 + 0) * i1 + k1 + 1) * l + l1 + 1] - d2) * d0;
					double d7 = (buffer[((j1 + 1) * i1 + k1 + 0) * l + l1 + 1] - d3) * d0;
					double d8 = (buffer[((j1 + 1) * i1 + k1 + 1) * l + l1 + 1] - d4) * d0;

					for (int i2 = 0; i2 < 8; ++i2)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int j2 = 0; j2 < 4; ++j2)
						{
							double d14 = 0.25D;
							double d15 = d10;
							double d16 = (d11 - d10) * d14;

							for (int k2 = 0; k2 < 4; ++k2)
							{
								IBlockState iblockstate = null;

								//                                if (l1 * 8 + i2 < j)
								//                                {
								//                                    iblockstate = LAVA;
								//                                }

								if (d15 > 0.0D)
									iblockstate = STONE;

								int l2 = j2 + j1 * 4;
								int i3 = i2 + l1 * 8;
								int j3 = k2 + k1 * 4;
								primer.setBlockState(l2, i3, j3, iblockstate);
								d15 += d16;
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
	}

	public void buildSurfaces(int p_185937_1_, int p_185937_2_, ChunkPrimer primer)
	{
		if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, p_185937_1_, p_185937_2_, primer, world)) return;
		int i = world.getSeaLevel() + 1;
		slowsandNoise = slowsandGravelNoiseGen.generateNoiseOctaves(slowsandNoise, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.03125D, 0.03125D, 1.0D);
		gravelNoise = slowsandGravelNoiseGen.generateNoiseOctaves(gravelNoise, p_185937_1_ * 16, 109, p_185937_2_ * 16, 16, 1, 16, 0.03125D, 1.0D, 0.03125D);
		depthBuffer = netherrackExculsivityNoiseGen.generateNoiseOctaves(depthBuffer, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

		for (int j = 0; j < 16; ++j)
			for (int k = 0; k < 16; ++k)
			{
				boolean flag = slowsandNoise[j + k * 16] + rand.nextDouble() * 0.2D > 0.0D;
				boolean flag1 = gravelNoise[j + k * 16] + rand.nextDouble() * 0.2D > 0.0D;
				int l = (int)(depthBuffer[j + k * 16] / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
				int i1 = -1;
				IBlockState iblockstate = STONE;
				IBlockState iblockstate1 = STONE;

				for (int j1 = 255; j1 >= 0; --j1)
					if (j1 < 255 - rand.nextInt(5) && j1 > rand.nextInt(5))
					{
						IBlockState iblockstate2 = primer.getBlockState(k, j1, j);

						if (iblockstate2.getBlock() != null && iblockstate2.getMaterial() != Material.AIR)
						{
							if (iblockstate2.getBlock() == Blocks.STONE)
								if (i1 == -1)
								{
									if (l <= 0)
									{
										iblockstate = AIR;
										iblockstate1 = STONE;
									}
									else if (j1 >= i - 4 && j1 <= i + 1)
									{
										iblockstate = STONE;
										iblockstate1 = STONE;

										if (flag1)
										{
											iblockstate = GRAVEL;
											iblockstate1 = STONE;
										}

										if (flag)
										{
											iblockstate = STONE;
											iblockstate1 = STONE;
										}
									}

									//                                    if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
									//                                    {
									//                                        iblockstate = LAVA;
									//                                    }

									i1 = l;

									if (j1 >= i - 1)
										primer.setBlockState(k, j1, j, iblockstate);
									else
										primer.setBlockState(k, j1, j, iblockstate1);
								}
								else if (i1 > 0)
								{
									--i1;
									primer.setBlockState(k, j1, j, iblockstate1);
								}
						} else
							i1 = -1;
					} else
						primer.setBlockState(k, j1, j, BEDROCK);
			}
	}

	@Override
	public Chunk generateChunk(int x, int z)
	{
		rand.setSeed(x * 341873128712L + z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		prepareHeights(x, z, chunkprimer);
		buildSurfaces(x, z, chunkprimer);
		genDankCaves.generate(world, x, z, chunkprimer);

		Chunk chunk = new Chunk(world, chunkprimer, x, z);
		Biome[] abiome = world.getBiomeProvider().getBiomes((Biome[])null, x * 16, z * 16, 16, 16);
		byte[] abyte = chunk.getBiomeArray();

		for (int i = 0; i < abyte.length; ++i)
			abyte[i] = (byte)Biome.getIdForBiome(abiome[i]);

		chunk.resetRelightChecks();
		return chunk;
	}

	private double[] getHeights(double[] p_73164_1_, int p_73164_2_, int p_73164_3_, int p_73164_4_, int p_73164_5_, int p_73164_6_, int p_73164_7_)
	{
		net.minecraftforge.event.terraingen.ChunkGeneratorEvent.InitNoiseField event = new net.minecraftforge.event.terraingen.ChunkGeneratorEvent.InitNoiseField(this, p_73164_1_, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) return event.getNoisefield();

		if(p_73164_1_ == null)
			p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];

		double d0 = 684.412D;
		double d1 = 2053.236D;
		noiseData4 = scaleNoise.generateNoiseOctaves(noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 1.0D, 0.0D, 1.0D);
		noiseData5 = depthNoise.generateNoiseOctaves(noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 100.0D, 0.0D, 100.0D);
		noiseData1 = perlinNoise1.generateNoiseOctaves(noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0 / 80.0D, d1 / 60.0D, d0 / 80.0D);
		noiseData2 = lperlinNoise1.generateNoiseOctaves(noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
		noiseData3 = lperlinNoise2.generateNoiseOctaves(noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
		int k1 = 0;
		int l1 = 0;
		double[] adouble1 = new double[p_73164_6_];
		int i2;

		for(i2 = 0; i2 < p_73164_6_; ++i2)
		{
			adouble1[i2] = Math.cos(i2 * Math.PI * 6.0D / p_73164_6_) * 2.0D;
			double d2 = i2;

			if(i2 > p_73164_6_ / 2)
				d2 = p_73164_6_ - 1 - i2;

			if(d2 < 4.0D)
			{
				d2 = 4.0D - d2;
				adouble1[i2] -= d2 * d2 * d2 * 10.0D;
			}
		}

		for(i2 = 0; i2 < p_73164_5_; ++i2)
			for(int k2 = 0; k2 < p_73164_7_; ++k2)
			{
				double d3 = (noiseData4[l1] + 256.0D) / 512.0D;

				if(d3 > 1.0D)
					d3 = 1.0D;

				double d4 = 0.0D;
				double d5 = noiseData5[l1] / 8000.0D;

				if(d5 < 0.0D)
					d5 = -d5;

				d5 = d5 * 3.0D - 3.0D;

				if(d5 < 0.0D)
				{
					d5 /= 2.0D;

					if(d5 < -1.0D)
						d5 = -1.0D;

					d5 /= 1.4D;
					d5 /= 2.0D;
					d3 = 0.0D;
				} else
				{
					if(d5 > 1.0D)
						d5 = 1.0D;

					d5 /= 6.0D;
				}

				d3 += 0.5D;
				d5 = d5 * p_73164_6_ / 16.0D;
				++l1;

				for(int j2 = 0; j2 < p_73164_6_; ++j2)
				{
					double d6 = 0.0D;
					double d7 = adouble1[j2];
					double d8 = noiseData2[k1] / 512.0D;
					double d9 = noiseData3[k1] / 512.0D;
					double d10 = (noiseData1[k1] / 10.0D + 1.0D) / 2.0D;

					if(d10 < 0.0D)
						d6 = d8;
					else if(d10 > 1.0D)
						d6 = d9;
					else
						d6 = d8 + (d9 - d8) * d10;

					d6 -= d7;
					double d11;

					if(j2 > p_73164_6_ - 4)
					{
						d11 = (j2 - (p_73164_6_ - 4)) / 3.0F;
						d6 = d6 * (1.0D - d11) + -10.0D * d11;
					}

					if(j2 < d4)
					{
						d11 = (d4 - j2) / 4.0D;

						if(d11 < 0.0D)
							d11 = 0.0D;

						if(d11 > 1.0D)
							d11 = 1.0D;

						d6 = d6 * (1.0D - d11) + -10.0D * d11;
					}

					p_73164_1_[k1] = d6;
					++k1;
				}
			}

		return p_73164_1_;
	}

	@Override
	public void populate(int x, int z)
	{
		BlockFalling.fallInstantly = true;
		if(!Beneath.internalOreGen)
			net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, world, rand, x, z, false);
		int i = x * 16;
		int j = z * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);

		if(!Beneath.internalOreGen){
			net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, world, rand, x, z, false);
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(world, rand, blockpos));
		}

		if (net.minecraftforge.event.terraingen.TerrainGen.decorate(world, rand, blockpos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.SHROOM))
		{
			if (rand.nextBoolean())
				brownMushroomFeature.generate(world, rand, blockpos.add(rand.nextInt(16) + 8, rand.nextInt(256), rand.nextInt(16) + 8));

			if (rand.nextBoolean())
				redMushroomFeature.generate(world, rand, blockpos.add(rand.nextInt(16) + 8, rand.nextInt(256), rand.nextInt(16) + 8));
		}

		if(Beneath.dungeonChance > 0)
			if(net.minecraftforge.event.terraingen.TerrainGen.populate(this, world, rand, x, z, false, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON))
				for(int j2 = 0; j2 < Beneath.dungeonChance; ++j2)
				{
					int i3 = rand.nextInt(16) + 8;
					int l3 = rand.nextInt(256);
					int l1 = rand.nextInt(16) + 8;
					new WorldGenDungeons().generate(world, rand, blockpos.add(i3, l3, l1));
				}

		if(Beneath.internalOreGen)
			for(OreEntry entry : OreGenHandler.getOregen())
			{
				WorldGenMinableDank worldgenminable = new WorldGenMinableDank(entry.getOre(), entry.size, entry.getSource());
				for(int k1 = 0; k1 < entry.veins; ++k1)
				{
					int l1 = rand.nextInt(16);
					int i2 = rand.nextInt(entry.maxY - entry.minY) + entry.minY;
					int j2 = rand.nextInt(16);
					worldgenminable.generate(world, rand, blockpos.add(l1, i2, j2));
				}
			}

		if(!Beneath.internalOreGen)
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Post(world, rand, blockpos));

		BlockFalling.fallInstantly = false;
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		if(Beneath.disableMobSpawning) return Collections.emptyList();
		Biome biome = world.getBiome(pos);
		return biome.getSpawnableList(creatureType);
	}

	@Override
	@Nullable
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z)
	{

	}

}