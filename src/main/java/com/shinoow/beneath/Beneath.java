package com.shinoow.beneath;

import java.io.*;
import java.net.URL;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.shinoow.beneath.common.CommonProxy;
import com.shinoow.beneath.common.block.BlockTeleporterDeepDank;
import com.shinoow.beneath.common.block.tile.TileEntityTeleporterDeepDank;
import com.shinoow.beneath.common.entity.EntityHand;
import com.shinoow.beneath.common.entity.EntityShadow;
import com.shinoow.beneath.common.handler.BeneathEventHandler;
import com.shinoow.beneath.common.handler.BlockDecorationHandler;
import com.shinoow.beneath.common.handler.OreGenHandler;
import com.shinoow.beneath.common.network.PacketDispatcher;
import com.shinoow.beneath.common.world.WorldProviderDeepDank;
import com.shinoow.beneath.common.world.biome.BiomeDeepDank;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = Beneath.modid, name = Beneath.name, version = Beneath.version, dependencies = "required-after:Forge@[forgeversion,);after:grue@[1.3.4,)", acceptedMinecraftVersions = "[1.10.2]", guiFactory = "com.shinoow.beneath.client.config.BeneathGuiFactory", useMetadata = false, updateJSON = "https://raw.githubusercontent.com/Shinoow/The-Beneath/master/version.json", certificateFingerprint = "cert_fingerprint")
public class Beneath {

	public static final String version = "beneath_version";
	public static final String modid = "beneath";
	public static final String name = "The Beneath";

	@Metadata(modid)
	public static ModMetadata metadata;

	@Instance(modid)
	public static Beneath instance = new Beneath();

	@SidedProxy(clientSide = "com.shinoow.beneath.client.ClientProxy",
			serverSide = "com.shinoow.beneath.common.CommonProxy")
	public static CommonProxy proxy;

	public static Configuration cfg;

	static int startEntityId = 200;

	public static int dim, darkTimer, darkDamage, dungeonChance, shadowSpawnWeight, lakeChance, stalactiteChance, stalagmiteChance;
	public static String mode;
	public static boolean internalOreGen, keepLoaded, dimTeleportation, disableMobSpawning, useCraftingRecipe, teleportTorches, useDecorator;
	public static double red, green, blue, damageMultiplier, healthMultiplier;
	private static String[] craftingRecipe, fluidBlocks;

	public static List<Block> fluid_blocks = Lists.newArrayList();

	public static Biome deep_dank;

	public static DimensionType deep_dank_dim;

	public static Block teleporter;
	public static DamageSource darkness = new DamageSource("darkness").setDamageBypassesArmor();

	public static SoundEvent beneath_normal, beneath_muffled, beneath_drawnout, deepdank, dark1, dark2, scream;

	public static final ResourceLocation shadow_loot_table = LootTableList.register(new ResourceLocation(modid, "entities/shadow"));

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){

		metadata = event.getModMetadata();
		metadata.description = metadata.description +"\n\n\u00a76Supporters: "+getSupporterList()+"\u00a7r";
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new BeneathEventHandler());
		instance = this;

		cfg = new Configuration(new File(event.getModConfigurationDirectory(), "/beneath/beneath.cfg"));
		syncConfig();

		cfg.setCategoryComment(Configuration.CATEGORY_GENERAL, "CONFIG ALL THE THINGS! Any changes take effect after a Minecraft restart (though some can take effect immediately, with weird results).");

		deep_dank = new BiomeDeepDank();

		GameRegistry.register(deep_dank.setRegistryName(new ResourceLocation(modid, "the_beneath")));

		BiomeDictionary.registerBiomeType(deep_dank, Type.DEAD);

		deep_dank_dim = DimensionType.register("The Beneath", "_tb", dim, WorldProviderDeepDank.class, keepLoaded);

		DimensionManager.registerDimension(dim, deep_dank_dim);

		teleporter = new BlockTeleporterDeepDank();

		GameRegistry.register(teleporter.setRegistryName(new ResourceLocation(modid, "teleporterbeneath")));
		GameRegistry.register(new ItemBlock(teleporter).setRegistryName(new ResourceLocation(modid, "teleporterbeneath")));

		GameRegistry.registerTileEntity(TileEntityTeleporterDeepDank.class, "tileEntityTeleporterBeneath");

		beneath_normal = GameRegistry.register(new SoundEvent(new ResourceLocation(modid, "beneath.normal")).setRegistryName(new ResourceLocation(modid, "beneath.normal")));
		beneath_muffled = GameRegistry.register(new SoundEvent(new ResourceLocation(modid, "beneath.muffled")).setRegistryName(new ResourceLocation(modid, "beneath.muffled")));
		beneath_drawnout = GameRegistry.register(new SoundEvent(new ResourceLocation(modid, "beneath.drawnout")).setRegistryName(new ResourceLocation(modid, "beneath.drawnout")));
		deepdank = GameRegistry.register(new SoundEvent(new ResourceLocation(modid, "deepdank")).setRegistryName(new ResourceLocation(modid, "deepdank")));
		dark1 = GameRegistry.register(new SoundEvent(new ResourceLocation(modid, "dark1")).setRegistryName(new ResourceLocation(modid, "dark1")));
		dark2 = GameRegistry.register(new SoundEvent(new ResourceLocation(modid, "dark2")).setRegistryName(new ResourceLocation(modid, "dark2")));
		scream = GameRegistry.register(new SoundEvent(new ResourceLocation(modid, "scream")).setRegistryName(new ResourceLocation(modid, "scream")));

		registerEntityWithEgg(EntityShadow.class, "shadow", 1, 80, 3, true, 0, 0);
		EntityRegistry.registerModEntity(EntityHand.class, "hand", 2, instance, 80, 3, true);
		if(mode.equalsIgnoreCase("grue"))
			FMLInterModComms.sendMessage("grue", "registerDimensionWhitelistOverride", String.valueOf(dim));
		PacketDispatcher.registerPackets();
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
		if(internalOreGen){
			OreGenHandler.setupOregenFile();
			OreGenHandler.saveOregenFile();
		}
		if(useDecorator){
			BlockDecorationHandler.setupBlockDecoFile();
			BlockDecorationHandler.saveBlockDecoFile();
		}
		for(String str : fluidBlocks)
			fluid_blocks.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(str)));
		if(useCraftingRecipe)
			GameRegistry.addRecipe(createCraftingRecipe(craftingRecipe));
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		((BiomeDeepDank) deep_dank).setSpawnLists();
		proxy.postInit();
	}

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		FMLLog.log("The Beneath", Level.WARN, "Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if(eventArgs.getModID().equals("beneath"))
			syncConfig();
	}

	private static void syncConfig(){

		dim = cfg.get(Configuration.CATEGORY_GENERAL, "Dimension ID", 10, "Dimension ID for The Beneath.").getInt();
		mode = cfg.get(Configuration.CATEGORY_GENERAL, "Mode", Loader.isModLoaded("grue") ? "grue" : "darkness", "What mode The Beneath is set to. Current modes are:\ngrue: Grues spawn in the darkness\ndarkness: You take damage while in dark areas.\n"+TextFormatting.RED+"[Minecraft Restart Required]"+TextFormatting.RESET).getString();
		internalOreGen = cfg.get(Configuration.CATEGORY_GENERAL, "Internal Ore Generator", true, "Toggles whether or not to use the built-in Ore Generator. Can be disabled if you have another mod that handles Ore Generation.\n"+TextFormatting.RED+"[Minecraft Restart Required]"+TextFormatting.RESET).getBoolean();
		keepLoaded = cfg.get(Configuration.CATEGORY_GENERAL, "Keep Loaded", false, "Toggles whether or not The Beneath should be prevented from automatically unloading (might affect performance if enabled).\n"+TextFormatting.RED+"[Minecraft Restart Required]"+TextFormatting.RESET).getBoolean();
		dimTeleportation = cfg.get(Configuration.CATEGORY_GENERAL, "Additional Dimension Teleportation", false, "Toggles whether or not to allow teleporting back and forth between the Beneath and dimensions that aren't the Overworld").getBoolean();
		darkTimer = cfg.get(Configuration.CATEGORY_GENERAL, "Darkness damage timer", 5, "The amount of seconds before the darkness damages you (when the mode is set to darkness).\n[range: 1 ~ 10, default: 5]", 1, 10).getInt();
		darkDamage = cfg.get(Configuration.CATEGORY_GENERAL, "Darkness damage", 2, "The amount of damage (half hearts) you take from the darkness (when the mode is set to darkness).\n[range: 2 ~ 20, default: 2]", 2, 20).getInt();
		dungeonChance = cfg.get(Configuration.CATEGORY_GENERAL, "Dungeon spawn chance", 8, "The chance that a dungeon generates in The Beneath (same logic as the vanilla setting). Setting it to 0 stops dungeon generation.\n[range: 0 ~ 100, default: 8]", 0, 100).getInt();
		shadowSpawnWeight = cfg.get(Configuration.CATEGORY_GENERAL, "Shadow Spawn Weight", 50, "Spawn Weight for the shadows, increase to increase the chance of them spawning, or decrease to decrease the chance of them spawning.\n[range: 10 ~ 100, default: 50]", 10, 100).getInt();
		disableMobSpawning = cfg.get(Configuration.CATEGORY_GENERAL, "Disable Mob Spawning", false, "Toggles whether or not to stop mob spawning inside The Beneath.").getBoolean();
		useCraftingRecipe = cfg.get(Configuration.CATEGORY_GENERAL, "Use Crafting Recipe", false, "Toggles whether or not to use the configurable crafting recipe.\n"+TextFormatting.RED+"[Minecraft Restart Required]"+TextFormatting.RESET).getBoolean();
		craftingRecipe = cfg.get(Configuration.CATEGORY_GENERAL, "Beneath Teleporter Crafting Recipe", new String[]{"#&#", "&%&", "#&#", "#", "minecraft:stone:*", "&", "cobblestone", "%", "minecraft:nether_star"}, "Configurable crafting recipe for the Beneath Teleporter.\n"
				+ "The first 3 Strings in the array are the recipe formula, where each symbol represents an Item. The Items are defined by the character being in the array before the Item in question (check the default).\n"
				+ "Format for Items: modid:name:meta (where meta is optional, and * can be used to speficy the metadata wildcard). The OreDictionary can also be used, where you just specify the ore name (ingotIron for Iron Ingots, stone for Stone)\n"
				+TextFormatting.RED+"[Only used if Use Crafting Recipe is enabled]"+TextFormatting.RESET).getStringList();
		red = cfg.get(Configuration.CATEGORY_GENERAL, "Red Night Vision sky color", 1.1D, "Changes the red part of the Beneath sky color while affected by Night Vision. Client side only.\n[range: 0.0 ~ 10.0, default: 1.1]", 0.0D, 10.0D).getDouble();
		green = cfg.get(Configuration.CATEGORY_GENERAL, "Green Night Vision sky color", 0.0D, "Changes the green part of the Beneath sky color while affected by Night Vision. Client side only.\n[range: 0.0 ~ 10.0, default: 0.0]", 0.0D, 10.0D).getDouble();
		blue = cfg.get(Configuration.CATEGORY_GENERAL, "Blue Night Vision sky color", 1.5D, "Changes the red part of the Beneath sky color while affected by Night Vision. Client side only.\n[range: 0.0 ~ 10.0, default: 1.5]", 0.0D, 10.0D).getDouble();
		teleportTorches = cfg.get(Configuration.CATEGORY_GENERAL, "Teleporter Torches", true, "Whether or not torches should spawn on the platform generated when entering The Beneath.").getBoolean();
		damageMultiplier = cfg.get(Configuration.CATEGORY_GENERAL, "Damage Multiplier", 2.0D, "Sets how much mob damage is multiplied by inside The Beneath\n[range: 2.0 ~ 10.0, default: 2.0]", 2.0D, 10.0D).getDouble();
		healthMultiplier = cfg.get(Configuration.CATEGORY_GENERAL, "Health Multiplier", 2.0D, "Sets how much mob health is multiplied by inside The Beneath\n[range: 2.0 ~ 10.0, default: 2.0]", 2.0D, 10.0D).getDouble();
		fluidBlocks = cfg.get(Configuration.CATEGORY_GENERAL, "Lake Fluid Blocks", new String[]{"minecraft:water", "minecraft:lava"}, "Any fluid blocks added to this list will randomly generate as part of lakes inside The Beneath (format is \"modid:name\")\n"+TextFormatting.RED+"[Minecraft Restart Required]"+TextFormatting.RESET).getStringList();
		lakeChance = cfg.get(Configuration.CATEGORY_GENERAL, "Lake spawn chance", 10, "The chance that a lake generates in The Beneath (same logic as the vanilla setting). Setting it to 0 stops lake generation\n[range: 0 ~ 100, default: 10]", 0, 100).getInt();
		useDecorator = cfg.get(Configuration.CATEGORY_GENERAL, "Use Block Decorator", true, "Toggles whether or not to use the built-in Block Decorator (functions like the Ore Generator, except it runs before it, and is intended for things like dirt, gravel, stone types).\n"+TextFormatting.RED+"[Minecraft Restart Required]"+TextFormatting.RESET).getBoolean();
		stalactiteChance = cfg.get(Configuration.CATEGORY_GENERAL, "Stalactite spawn chance", 20, "The chance that a stalactite generates in The Beneath (higher number increases the chance, lower decreases it). Setting it to 0 stops stalactite generation\\n[range: 0 ~ 100, default: 20]", 0, 100).getInt();
		stalagmiteChance = cfg.get(Configuration.CATEGORY_GENERAL, "Stalagmite spawn chance", 20, "The chance that a stalagmite generates in The Beneath (higher number increases the chance, lower decreases it). Setting it to 0 stops stalagmite generation\\n[range: 0 ~ 100, default: 20]", 0, 100).getInt();

		darkTimer = MathHelper.clamp_int(darkTimer, 1, 10);
		darkDamage = MathHelper.clamp_int(darkDamage, 2, 20);
		shadowSpawnWeight = MathHelper.clamp_int(shadowSpawnWeight, 10, 100);
		red = MathHelper.clamp_double(red, 0, 10);
		green = MathHelper.clamp_double(green, 0, 10);
		blue = MathHelper.clamp_double(blue, 0, 10);
		damageMultiplier = MathHelper.clamp_double(damageMultiplier, 2, 10);
		healthMultiplier = MathHelper.clamp_double(healthMultiplier, 2, 10);
		stalactiteChance = MathHelper.clamp_int(stalactiteChance, 0, 100);
		stalagmiteChance = MathHelper.clamp_int(stalagmiteChance, 0, 100);

		if(mode.equalsIgnoreCase("grue") && !Loader.isModLoaded("grue"))
			mode = "darkness";

		if(cfg.hasChanged())
			cfg.save();
	}

	private static int getUniqueEntityId() {
		do
			startEntityId++;
		while (EntityList.ID_TO_CLASS.containsKey(startEntityId));

		return startEntityId;
	}

	private static void registerEntityWithEgg(Class<? extends Entity> entity, String name, int modid, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int primaryColor, int secondaryColor) {
		int id = getUniqueEntityId();
		EntityRegistry.registerModEntity(entity, name, modid, instance, trackingRange, updateFrequency, sendsVelocityUpdates, primaryColor, secondaryColor);
		EntityList.ID_TO_CLASS.put(id, entity);
	}

	private ShapedOreRecipe createCraftingRecipe(String[] data){
		Object[] recipe = new Object[data.length];

		recipe[0] = data[0];
		recipe[1] = data[1];
		recipe[2] = data[2];

		for(int i = 3; i < data.length; i++)
			recipe[i] = getObject(data[i]);

		return new ShapedOreRecipe(teleporter, recipe);
	}

	private Object getObject(String data){
		if(data.length() == 1)
			return data.charAt(0);
		if(data.lastIndexOf(":") == -1)
			return data;

		String[] stuff = data.split(":");
		Item item = Item.REGISTRY.getObject(new ResourceLocation(stuff[0], stuff[1]));

		if(item == null) return null;

		int meta = stuff.length == 3 ? stuff[2].equals("*") ? OreDictionary.WILDCARD_VALUE : Integer.valueOf(stuff[2]) : 0;
		return new ItemStack(item, 1, meta);
	}

	private String getSupporterList(){
		BufferedReader nameFile;
		String names = "";
		try {
			nameFile = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/Shinoow/AbyssalCraft/master/supporters.txt").openStream()));

			names = nameFile.readLine();
			nameFile.close();

		} catch (IOException e) {
			FMLLog.log("The Beneath", Level.ERROR, "Failed to fetch supporter list, using local version!");
			names = "Enfalas, Saice Shoop, Minecreatr";
		}

		return names;
	}
}