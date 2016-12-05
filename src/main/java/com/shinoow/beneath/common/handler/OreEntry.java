package com.shinoow.beneath.common.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonObject;
import com.shinoow.beneath.common.util.JsonHelper;

public class OreEntry {

	public String ore;
	public int oreMeta;
	public String source;
	public int srcMeta;
	public int veins;
	public int size;
	public int minY;
	public int maxY;

	public OreEntry(String id, int oreMeta, String source, int srcMeta, int veins, int size, int minY, int maxY){
		ore = id;
		this.oreMeta = oreMeta;
		this.source = source;
		this.srcMeta = srcMeta;
		this.veins = veins;
		this.size = size;
		this.minY = minY;
		this.maxY = maxY;
	}

	public IBlockState getOre(){
		IBlockState state = null;
		Block b = Block.REGISTRY.getObject(new ResourceLocation(ore));
		if(b != null)
			state = oreMeta < 0 ? b.getDefaultState() : b.getStateFromMeta(oreMeta);
			return state != null ? state : Blocks.STONE.getDefaultState();
	}

	public IBlockState getSource(){
		IBlockState state = null;
		Block b = Block.REGISTRY.getObject(new ResourceLocation(source));
		if(b != null)
			state = srcMeta < 0 ? b.getDefaultState() : b.getStateFromMeta(srcMeta);
			return state != null ? state : Blocks.STONE.getDefaultState();
	}

	public OreEntry(JsonObject json){
		readFromJson(json);
	}

	public void writeToJson(JsonObject json){
		json.addProperty("ore", ore);
		json.addProperty("oremeta", oreMeta);
		json.addProperty("source", source);
		json.addProperty("srcmeta", srcMeta);
		json.addProperty("veins", veins);
		json.addProperty("size", size);
		json.addProperty("miny", minY);
		json.addProperty("maxy", maxY);
	}

	public void readFromJson(JsonObject json){
		ore = JsonHelper.GetString(json, "ore", "minecraft:coal_ore");
		oreMeta = JsonHelper.GetNumber(json, "oremeta", -1).intValue();
		source = JsonHelper.GetString(json, "source", "minecraft:stone");
		srcMeta = JsonHelper.GetNumber(json, "srcmeta", -1).intValue();
		veins = JsonHelper.GetNumber(json, "veins", 32).intValue();
		size = JsonHelper.GetNumber(json, "size", 16).intValue();
		minY = JsonHelper.GetNumber(json, "miny", 10).intValue();
		maxY = JsonHelper.GetNumber(json, "maxy", 246).intValue();
	}
}