package com.shinoow.beneath.common.handler;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.shinoow.beneath.common.util.JsonHelper;

import net.minecraft.init.Blocks;

public class OreGenHandler {

	static List<OreEntry> oregen = Lists.newArrayList();

	public static void setupOregenFile(){

		File f = new File("config/beneath/oregen.json");

		if(!f.exists())
			generateDefault(f);
		else {
			JsonArray list = JsonHelper.ReadArrayFromFile(f);
			for(JsonElement e : list){
				if(e == null || !e.isJsonObject())
					continue;
				oregen.add(new OreEntry(e.getAsJsonObject()));
			}
		}
	}

	public static void saveOregenFile(){
		JsonArray l = new JsonArray();
		for(OreEntry e : oregen){
			JsonObject j = new JsonObject();
			e.writeToJson(j);
			l.add(j);
		}
		JsonHelper.WriteToFile(new File("config/beneath/oregen.json"), l);
	}

	private static void generateDefault(File f){
		OreEntry coal = new OreEntry(Blocks.COAL_ORE.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 32, 16, 10, 246);
		OreEntry iron = new OreEntry(Blocks.IRON_ORE.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 24, 16, 10, 246);
		OreEntry lapis = new OreEntry(Blocks.LAPIS_ORE.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 12, 8, 10, 246);
		OreEntry redstone = new OreEntry(Blocks.REDSTONE_ORE.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 12, 12, 10, 246);
		OreEntry gold = new OreEntry(Blocks.GOLD_ORE.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 8, 8, 10, 246);
		OreEntry diamond = new OreEntry(Blocks.DIAMOND_ORE.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 4, 8, 10, 246);
		OreEntry emerald = new OreEntry(Blocks.EMERALD_ORE.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 2, 4, 10, 246);
		OreEntry silverfish = new OreEntry(Blocks.MONSTER_EGG.getRegistryName().toString(), 0, Blocks.STONE.getRegistryName().toString(), 0, 48, 24, 10, 246);

		JsonArray list = new JsonArray();
		JsonObject jBlk = new JsonObject();
		coal.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		iron.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		lapis.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		redstone.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		gold.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		diamond.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		emerald.writeToJson(jBlk);
		list.add(jBlk);
		jBlk = new JsonObject();
		silverfish.writeToJson(jBlk);
		list.add(jBlk);

		for(JsonElement e : list){
			if(e == null || !e.isJsonObject())
				continue;
			oregen.add(new OreEntry(e.getAsJsonObject()));
		}

		JsonHelper.WriteToFile(f, list);
	}

	public static List<OreEntry> getOregen(){
		return oregen;
	}
}