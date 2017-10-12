package com.shinoow.beneath.common.handler;

import java.io.File;
import java.util.List;

import net.minecraft.init.Blocks;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shinoow.beneath.common.util.JsonHelper;

public class BlockDecorationHandler {

	static List<OreEntry> blockdeco = Lists.newArrayList();

	public static void setupBlockDecoFile(){

		File f = new File("config/beneath/blockdeco.json");

		if(!f.exists())
			generateDefault(f);
		else {
			JsonArray list = JsonHelper.ReadArrayFromFile(f);
			for(JsonElement e : list){
				if(e == null || !e.isJsonObject())
					continue;
				blockdeco.add(new OreEntry(e.getAsJsonObject()));
			}
		}
	}

	public static void saveBlockDecoFile(){
		JsonArray l = new JsonArray();
		for(OreEntry e : blockdeco){
			JsonObject j = new JsonObject();
			e.writeToJson(j);
			l.add(j);
		}
		JsonHelper.WriteToFile(new File("config/beneath/blockdeco.json"), l);
	}

	private static void generateDefault(File f){

		OreEntry dirt = new OreEntry(Blocks.DIRT.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 33, 24, 0, 256);
		OreEntry gravel = new OreEntry(Blocks.GRAVEL.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 33, 22, 0, 256);
		OreEntry granite = new OreEntry(Blocks.STONE.getRegistryName().toString(), 1, Blocks.STONE.getRegistryName().toString(), 0, 33, 24, 0, 160);
		OreEntry diorite = new OreEntry(Blocks.STONE.getRegistryName().toString(), 3, Blocks.STONE.getRegistryName().toString(), 0, 33, 24, 0, 160);
		OreEntry adesite = new OreEntry(Blocks.STONE.getRegistryName().toString(), 5, Blocks.STONE.getRegistryName().toString(), 0, 33, 24, 0, 160);

		JsonArray list = new JsonArray();
		JsonObject jBlk = new JsonObject();
		dirt.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		gravel.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		granite.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		diorite.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		adesite.writeToJson(jBlk);
		list.add(jBlk);

		for(JsonElement e : list){
			if(e == null || !e.isJsonObject())
				continue;
			blockdeco.add(new OreEntry(e.getAsJsonObject()));
		}

		JsonHelper.WriteToFile(f, list);
	}

	public static List<OreEntry> getBlockDeco(){
		return blockdeco;
	}
}