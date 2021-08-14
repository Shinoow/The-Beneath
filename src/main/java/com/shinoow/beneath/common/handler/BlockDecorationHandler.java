package com.shinoow.beneath.common.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.shinoow.beneath.Beneath;
import com.shinoow.beneath.common.util.JsonHelper;

import net.minecraft.init.Blocks;

public class BlockDecorationHandler {

	static List<OreEntry> blockdeco = new ArrayList<>();

	public static void setupBlockDecoFile(){

		File f = new File("config/beneath/blockdeco.json");

		if(!f.exists())
			generateDefault(f);
		else {
			try {
				blockdeco = Streams.stream(JsonHelper.ReadArrayFromFile(f)).filter(e -> e != null && e.isJsonObject()).map(e -> new OreEntry(e.getAsJsonObject())).collect(Collectors.toList());
			}
			catch(Exception e) {
				Beneath.LOGGER.error("An error occurred while reading blockdeco.json, will use the default values instead", e);
				generateDefault(f);
			}
		}
			
	}

	public static void saveBlockDecoFile(){
		JsonArray l = new JsonArray();
		blockdeco.stream().map(OreEntry::toJson).forEach(j -> l.add(j));
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

		blockdeco = Streams.stream(list).filter(e -> e != null && e.isJsonObject()).map(e -> new OreEntry(e.getAsJsonObject())).collect(Collectors.toList());

		JsonHelper.WriteToFile(f, list);
	}

	public static List<OreEntry> getBlockDeco(){
		return blockdeco;
	}
}