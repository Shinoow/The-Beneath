package com.shinoow.beneath.common.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

import com.shinoow.beneath.common.entity.EntityShadow;

public class BiomeDeepDank extends Biome {

	public BiomeDeepDank() {
		super(new BiomeProperties("The Beneath").setRainDisabled());
		topBlock = Blocks.STONE.getDefaultState();
		fillerBlock = Blocks.STONE.getDefaultState();

	}

	public void setSpawnLists(){
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableMonsterList.add(new SpawnListEntry(EntityShadow.class,	50, 2, 4));
	}
}