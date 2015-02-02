package info.loenwind.oreshards;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

public class OreShardsConfig {
	

	private static final String RENDER_TYPE = "renderType";

	private static final String BASE_BLOCK_BG_TEXTURE = "baseBlockBgTexture";

	private static final String BASE_BLOCK_FG_TEXTURE = "baseBlockFgTexture";
	private static final String BASE_BLOCK_AFG_TEXTURE = "baseBlockAFgTexture";

	private static final String BASE_BLOCK_META = "baseBlockMeta";

	private static final String BASE_BLOCK = "baseBlock";

	private static final String DUMB_SHARD = "dumbShard";

	private static final String RESULT_SHARD = "resultShard";

	private static final String SHARD_CHANCE = "shardChance";

	private static final String SHARDS_IN_ORE = "shardsInOre";

	private static final String RESULT_FROM_SHARDS = "resultFromShards";

	private static final String RESULT_ITEM_TEXTURE = "resultItemTexture";
	private static final String RESULT_ITEM_TEXTURE_AFG = "resultItemTextureAlpha";
	private static final String RESULT_ITEM_TEXTURE_BG = "resultItemTextureBackground";

	private static final String RESULT_ITEM_META = "resultItemMeta";

	private static final String RESULT_ITEM = "resultItem";

	private static final String HOW_TO_COMBINE_FORE_AND_BACKGROUND_TEXTURE_0_5 = "How to combine fore- and background texture (0..5)";

	private static final String THE_NAME_OF_THE_TEXTURE_TO_USE_AS_BACKGROUND_FOR_THE_BLOCK = "The name of the texture to use as background for the block";

	private static final String THE_NAME_OF_THE_TEXTURE_TO_USE_AS_FOREGROUND_FOR_THE_BLOCK = "The name of the texture to use as foreground for the block";

	private static final String THE_BLOCK_TO_TAKE_MISC_DATA_FROM_HARDNESS_STEP_SOUND = "The block to take misc data from (hardness, step sound, ...)";

	private static final String REFERENCE_TO_THE_DUMB_SHARD = "Reference to the dumb shard";

	private static final String REFERENCE_TO_THE_ORE_SHARD = "Reference to the ore shard";

	private static final String CHANCE_TO_DROP_NTH_SHARD_AS_RESULT_FROM_THE_ORE_SHARD_ELSE_DUMB = "Chance to drop nth shard as 'result' from the OreShard (else dumb)";

	private static final String NUMBER_OF_SHARDS_TO_DROP_FROM_THE_ORE_SHARD_BLOCK_IN_TOTAL = "Number of shards to drop from the OreShard block in total";

	private static final String HOW_MANY_SHARDS_MAKE_UP_THE_RESULT = "How many shards make up the result?";

	private static final String THE_NAME_OF_ITS_TEXTURE = "The name of its texture";
	private static final String THE_NAME_OF_ITS_TEXTURE_AFG = "The name of texture that will provide the alpha channel for the foreground texture (optional)";
	private static final String THE_NAME_OF_ITS_TEXTURE_BG = "The name the background texture (should have an alpha channel; optional)";

	private static final String ITS_META_VALUE = "Its meta value";

	private static final String THE_BLOCK_ITEM_THESE_SHARDS_CRAFT_INTO = "The (block) item these shards craft into";

	public final static OreShardsConfig instance = new OreShardsConfig();

	public final static String CATEGORY_DUMB = "shards.shard_";
    public final static String CATEGORY_ORE = "ores.ore_";

    private static final String DEMO_DUMB = "democobble";
    private static final String DEMO_IRON = "demoiron";
	private static final String DEMO_DUMB_KEY = CATEGORY_DUMB + DEMO_DUMB;
	private static final String DEMO_IRON_KEY = CATEGORY_DUMB + DEMO_IRON;
    private static final String DEMO_ORE_KEY = CATEGORY_ORE + "demo";

    public void loadConfig(File file) {

    	Configuration config = new Configuration(file);

    	config.load();

    	// make the demo blocks
    	makeDemoConfig(config);

    	// go through all categories and add them to the registry if they
    	// match. dumbs first because they are referenced later
    	for (String cat : config.getCategoryNames()) {
    		if (cat.startsWith(CATEGORY_DUMB)) {
    			String id = cat.substring(CATEGORY_DUMB.length());

    			OreShardsRegistry.registerDumb(id,
    					config.get(cat, RESULT_ITEM, "", THE_BLOCK_ITEM_THESE_SHARDS_CRAFT_INTO).getString().trim(),
    					config.get(cat, RESULT_ITEM_META, 0, ITS_META_VALUE).getInt(0),
    					config.get(cat, RESULT_ITEM_TEXTURE, "", THE_NAME_OF_ITS_TEXTURE).getString().trim(),
    					config.get(cat, RESULT_ITEM_TEXTURE_AFG, "", THE_NAME_OF_ITS_TEXTURE_AFG).getString().trim(),
    					config.get(cat, RESULT_ITEM_TEXTURE_BG, "", THE_NAME_OF_ITS_TEXTURE_BG).getString().trim(),
    					config.get(cat, RESULT_FROM_SHARDS, 9, HOW_MANY_SHARDS_MAKE_UP_THE_RESULT).getInt(0));
    		}
    	}

    	for (String cat : config.getCategoryNames()) {
    		if (cat.startsWith(CATEGORY_ORE) && !DEMO_ORE_KEY.equals(cat)) {
    			String id = cat.substring(CATEGORY_ORE.length());

    			int shardsInOre = config.get(cat, SHARDS_IN_ORE, 9, NUMBER_OF_SHARDS_TO_DROP_FROM_THE_ORE_SHARD_BLOCK_IN_TOTAL).getInt(9);
    			
    			List<Double> chances = new ArrayList<Double>(shardsInOre);
    			for (int i = 1; i <= shardsInOre; i++) {
					chances.add(config.get(cat, SHARD_CHANCE + i, 0.0, CHANCE_TO_DROP_NTH_SHARD_AS_RESULT_FROM_THE_ORE_SHARD_ELSE_DUMB).getDouble(0.0));
				}
    			
    			OreShardsRegistry.registerOre(id,
    					config.get(cat, RESULT_SHARD, "", REFERENCE_TO_THE_ORE_SHARD).getString().trim(),
    					config.get(cat, DUMB_SHARD, "", REFERENCE_TO_THE_DUMB_SHARD).getString().trim(),
    					config.get(cat, BASE_BLOCK, "", THE_BLOCK_TO_TAKE_MISC_DATA_FROM_HARDNESS_STEP_SOUND).getString().trim(),
                        config.get(cat, BASE_BLOCK_META, 0, ITS_META_VALUE).getInt(0),
                        config.get(cat, BASE_BLOCK_FG_TEXTURE, "", THE_NAME_OF_THE_TEXTURE_TO_USE_AS_FOREGROUND_FOR_THE_BLOCK).getString().trim(),
                        config.get(cat, BASE_BLOCK_BG_TEXTURE, "", THE_NAME_OF_THE_TEXTURE_TO_USE_AS_BACKGROUND_FOR_THE_BLOCK).getString().trim(),
                        config.get(cat, BASE_BLOCK_AFG_TEXTURE, "", "The name of the texture to use as alpha channel for the foreground texture").getString().trim(),
    					shardsInOre,
    					chances
    					);
    		}
    	}

    	config.save();

    }

    private void pcfgs(Configuration config, String cat, String key, String comment) {
    	String value = config.get(cat, key, "", comment).getString().trim();
    	System.out.println("config.get(\""+cat+ "\", \""+key+"\", \""+value+"\", \""+comment+"\").getString().trim();");
    }
    private void pcfgi(Configuration config, String cat, String key, String comment) {
    	int value = config.get(cat, key, 0, comment).getInt();
    	System.out.println("config.get(\""+cat+ "\", \""+key+"\", "+value+", \""+comment+"\").getInt("+value+");");
    }
    private void pcfgd(Configuration config, String cat, String key, String comment) {
    	Double value = config.get(cat, key, 0.0, comment).getDouble();
    	System.out.println("config.get(\""+cat+ "\", \""+key+"\", "+value+", \""+comment+"\").getDouble("+value+");");
    }
    private void pcfgx() {
    	System.out.println();
    }
    
	private void makeDemoConfig(Configuration config) {
		if (!config.get("common", "configured", false, "Set this to true to prevent the mod from recreating the default config").getBoolean()) {

			config.get("common.howto", "line00", false, "(This config section is just to hold the documentation. It is not read.)");
			config.get("common.howto", "line01", false, "OreShards works by adding new ore blocks that drop shards. Those shards are then crafted into the original ore (or any item you like).");
			config.get("common.howto", "line02", false, "To add a new OreShard block you first must create 2 shards for it to drop. Shards can be shared between as many OreShard blocks as you like.");
			config.get("common.howto", "line03", false, "Each shard is a subsection in the 'shards' section. The subsection name must start with 'shard_', the remainder is the ID of that shard; you'll need that later.");
			config.get("common.howto", "line04", false, "The keys are: 'resultItem'---The item to craft from the shards. 'resultItemMeta'---The meta number of the item. 'resultFromShards'---How many shards are needed to "+
			                                            "craft one item. Valid values are 1 through 9. Look up the crafting patterns with NEI... 'resultItemTexture'---The name of the texture to create the shard's texture from.");
			config.get("common.howto", "line05", false, "Each OreShard block is a subsection in the 'ores' section. The subsection name must start with 'ore_', the remainder is the ID of that OreShard block. Note that you "+
			                                            "can create multiple OreShards for the same shards/ores.");
			config.get("common.howto", "line06", false, "The keys are:");
			config.get("common.howto", "line07", false, "'baseBlock'-keys: These control the visuals and in-game behaviour of the OreShard block. 'baseBlock'/'baseBlockMeta' is for in-game interaction like hardness, tool level "+
			                                            "and step sound. 'baseBlockFgTexture' and 'baseBlockBgTexture' are mixed (using 'baseBlockAFgTexture's alpha channel as the alpha channel for Fg) to form the texture.");
			config.get("common.howto", "line08", false, "'shard'-keys: These control what drops when the block is broken. 'shardsInOre' says how many shards are dropped---this number is fixed. For every shard the 'shardChanceX' "+
			                                            "decides if it is a 'resultShard' or a 'dumbShard'. The fortune enchantment increases this chance by .05.");
			config.get("common.howto", "line09", false, "");
			config.get("common.howto", "line10", false, "About the baseBlockAFgTextures that are included: orepattern0 is a fallback, a checker pattern of pixels from both textures. It does not look good with vanilla textures but "+
			                                            "may be ok for modded blocks or texture packs. orepattern1 uses the fg texture in the center. It gives a quite rich appearance. orepattern2 to orepattern5 use a masking that blocks about 2 thirds of the fg texture. With the vanilla textures, orepattern2 and orepattern3 look good, while orepattern5 looks like a very poor ore.");
			config.get("common.howto", "line11", false, "");
			config.get("common.howto", "line12", false, "About the crafting patterns: 1 and 2 are shapeless, 3 is thre in a row, 4 is 2 by 2, 5 is a star, 6 are two lines, 7 is an H in either orientation, 8 is hollow and 9 you "+
			                                            "have to guess.");
			
			// Some code to generate the code to generate the default config...
//			for (String cat : config.getCategoryNames()) {
//	    		if (cat.startsWith(CATEGORY_DUMB)) {
//	    			pcfgs(config, cat, "resultItem", "The (block) item these shards craft into");
//	    			pcfgi(config, cat, "resultItemMeta", "Its meta value");
//	    			pcfgs(config, cat, "resultItemTexture", "The name of its texture");
//	    			pcfgi(config, cat, "resultFromShards", "How many shards make up the result?");
//	    			pcfgx();
//	    		}
//	    	}
//
//	    	for (String cat : config.getCategoryNames()) {
//	    		if (cat.startsWith(CATEGORY_ORE)) {
//	    			pcfgi(config, cat, "shardsInOre", "Number of shards to drop from the OreShard block in total");
//	    			int shardsInOre = config.get(cat, "shardsInOre", 9, "Number of shards to drop from the OreShard block in total").getInt(9);
//	    			
//	    			for (int i = 1; i <= shardsInOre; i++) {
//	    				pcfgd(config, cat, "shardChance" + i, "Chance to drop nth shard as 'result' from the OreShard (else dumb)");
//					}
//
//	    			pcfgs(config, cat, "resultShard", "Reference to the ore shard");
//	    			pcfgs(config, cat, "dumbShard", "Reference to the dumb shard");
//	    			pcfgs(config, cat, "baseBlock", "The block to take misc data from (hardness, step sound, ...)");
//	    			pcfgi(config, cat, "baseBlockMeta", "Its meta value");
//	    			pcfgs(config, cat, "baseBlockFgTexture", "The name of the texture to use as foreground for the block");
//	    			pcfgs(config, cat, "baseBlockBgTexture", "The name of the texture to use as background for the block");
//	    			pcfgs(config, cat, BASE_BLOCK_AFG_TEXTURE, "The name of the texture to use as alpha channel for the foreground texture");
//	    			pcfgx();
//	    		}
//	    	}


			config.get("shards.shard_coal", "resultItem", "minecraft:coal_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_coal", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_coal", "resultItemTexture", "coal_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_coal", "resultFromShards", 4, "How many shards make up the result?").getInt(4);

			config.get("shards.shard_cobble", "resultItem", "minecraft:cobblestone", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_cobble", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_cobble", "resultItemTexture", "cobblestone", "The name of its texture").getString().trim();
			config.get("shards.shard_cobble", "resultFromShards", 9, "How many shards make up the result?").getInt(9);

			config.get("shards.shard_diamond", "resultItem", "minecraft:diamond_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_diamond", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_diamond", "resultItemTexture", "diamond_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_diamond", "resultFromShards", 9, "How many shards make up the result?").getInt(9);

			config.get("shards.shard_emerald", "resultItem", "minecraft:emerald_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_emerald", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_emerald", "resultItemTexture", "emerald_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_emerald", "resultFromShards", 2, "How many shards make up the result?").getInt(2);

			config.get("shards.shard_gold", "resultItem", "minecraft:gold_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_gold", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_gold", "resultItemTexture", "gold_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_gold", "resultFromShards", 5, "How many shards make up the result?").getInt(5);

			config.get("shards.shard_iron", "resultItem", "minecraft:iron_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_iron", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_iron", "resultItemTexture", "iron_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_iron", "resultFromShards", 4, "How many shards make up the result?").getInt(4);

			config.get("shards.shard_lapis", "resultItem", "minecraft:lapis_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_lapis", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_lapis", "resultItemTexture", "lapis_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_lapis", "resultFromShards", 8, "How many shards make up the result?").getInt(8);

			config.get("shards.shard_netherrack", "resultItem", "minecraft:netherrack", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_netherrack", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_netherrack", "resultItemTexture", "netherrack", "The name of its texture").getString().trim();
			config.get("shards.shard_netherrack", "resultFromShards", 9, "How many shards make up the result?").getInt(9);

			config.get("shards.shard_quartz", "resultItem", "minecraft:quartz_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_quartz", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_quartz", "resultItemTexture", "quartz_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_quartz", "resultFromShards", 7, "How many shards make up the result?").getInt(7);

			config.get("shards.shard_redstone", "resultItem", "minecraft:redstone_ore", "The (block) item these shards craft into").getString().trim();
			config.get("shards.shard_redstone", "resultItemMeta", 0, "Its meta value").getInt(0);
			config.get("shards.shard_redstone", "resultItemTexture", "redstone_ore", "The name of its texture").getString().trim();
			config.get("shards.shard_redstone", "resultFromShards", 3, "How many shards make up the result?").getInt(3);

			config.get("ores.ore_coal", "shardsInOre", 9, "Number of shards to drop from the OreShard block in total").getInt(9);
			config.get("ores.ore_coal", "shardChance1", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_coal", "shardChance2", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_coal", "shardChance3", 0.25, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.25);
			config.get("ores.ore_coal", "shardChance4", 0.25, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.25);
			config.get("ores.ore_coal", "shardChance5", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_coal", "shardChance6", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_coal", "shardChance7", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_coal", "shardChance8", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_coal", "shardChance9", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_coal", "resultShard", "coal", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_coal", "dumbShard", "cobble", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_coal", "baseBlock", "minecraft:coal_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_coal", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_coal", "baseBlockFgTexture", "coal_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_coal", "baseBlockBgTexture", "stone", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_coal", "baseBlockAFgTexture", "oreshards:orepattern0", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

			config.get("ores.ore_diamond", "shardsInOre", 9, "Number of shards to drop from the OreShard block in total").getInt(9);
			config.get("ores.ore_diamond", "shardChance1", 0.75, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.75);
			config.get("ores.ore_diamond", "shardChance2", 0.75, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.75);
			config.get("ores.ore_diamond", "shardChance3", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_diamond", "shardChance4", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_diamond", "shardChance5", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_diamond", "shardChance6", 0.25, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.25);
			config.get("ores.ore_diamond", "shardChance7", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_diamond", "shardChance8", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_diamond", "shardChance9", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_diamond", "resultShard", "diamond", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_diamond", "dumbShard", "cobble", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_diamond", "baseBlock", "minecraft:diamond_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_diamond", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_diamond", "baseBlockFgTexture", "diamond_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_diamond", "baseBlockBgTexture", "stone", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_diamond", "baseBlockAFgTexture", "oreshards:orepattern4", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

			config.get("ores.ore_emerald", "shardsInOre", 9, "Number of shards to drop from the OreShard block in total").getInt(9);
			config.get("ores.ore_emerald", "shardChance1", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_emerald", "shardChance2", 0.125, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.125);
			config.get("ores.ore_emerald", "shardChance3", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_emerald", "shardChance4", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_emerald", "shardChance5", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_emerald", "shardChance6", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_emerald", "shardChance7", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_emerald", "shardChance8", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_emerald", "shardChance9", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_emerald", "resultShard", "emerald", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_emerald", "dumbShard", "cobble", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_emerald", "baseBlock", "minecraft:emerald_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_emerald", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_emerald", "baseBlockFgTexture", "emerald_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_emerald", "baseBlockBgTexture", "stone", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_emerald", "baseBlockAFgTexture", "oreshards:orepattern5", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

			config.get("ores.ore_gold", "shardsInOre", 9, "Number of shards to drop from the OreShard block in total").getInt(9);
			config.get("ores.ore_gold", "shardChance1", 0.75, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.75);
			config.get("ores.ore_gold", "shardChance2", 0.75, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.75);
			config.get("ores.ore_gold", "shardChance3", 0.75, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.75);
			config.get("ores.ore_gold", "shardChance4", 0.25, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.25);
			config.get("ores.ore_gold", "shardChance5", 0.125, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.125);
			config.get("ores.ore_gold", "shardChance6", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_gold", "shardChance7", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_gold", "shardChance8", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_gold", "shardChance9", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_gold", "resultShard", "gold", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_gold", "dumbShard", "cobble", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_gold", "baseBlock", "minecraft:gold_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_gold", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_gold", "baseBlockFgTexture", "gold_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_gold", "baseBlockBgTexture", "stone", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_gold", "baseBlockAFgTexture", "oreshards:orepattern2", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

			config.get("ores.ore_iron", "shardsInOre", 9, "Number of shards to drop from the OreShard block in total").getInt(9);
			config.get("ores.ore_iron", "shardChance1", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_iron", "shardChance2", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_iron", "shardChance3", 0.25, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.25);
			config.get("ores.ore_iron", "shardChance4", 0.125, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.125);
			config.get("ores.ore_iron", "shardChance5", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_iron", "shardChance6", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_iron", "shardChance7", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_iron", "shardChance8", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_iron", "shardChance9", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_iron", "resultShard", "iron", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_iron", "dumbShard", "cobble", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_iron", "baseBlock", "minecraft:iron_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_iron", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_iron", "baseBlockFgTexture", "iron_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_iron", "baseBlockBgTexture", "stone", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_iron", "baseBlockAFgTexture", "oreshards:orepattern3", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

			config.get("ores.ore_lapis", "shardsInOre", 9, "Number of shards to drop from the OreShard block in total").getInt(9);
			config.get("ores.ore_lapis", "shardChance1", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_lapis", "shardChance2", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_lapis", "shardChance3", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_lapis", "shardChance4", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_lapis", "shardChance5", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_lapis", "shardChance6", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_lapis", "shardChance7", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_lapis", "shardChance8", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_lapis", "shardChance9", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_lapis", "resultShard", "lapis", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_lapis", "dumbShard", "cobble", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_lapis", "baseBlock", "minecraft:lapis_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_lapis", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_lapis", "baseBlockFgTexture", "lapis_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_lapis", "baseBlockBgTexture", "stone", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_lapis", "baseBlockAFgTexture", "oreshards:orepattern2", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

			config.get("ores.ore_quartz", "shardsInOre", 3, "Number of shards to drop from the OreShard block in total").getInt(3);
			config.get("ores.ore_quartz", "shardChance1", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_quartz", "shardChance2", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_quartz", "shardChance3", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_quartz", "resultShard", "quartz", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_quartz", "dumbShard", "netherrack", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_quartz", "baseBlock", "minecraft:quartz_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_quartz", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_quartz", "baseBlockFgTexture", "quartz_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_quartz", "baseBlockBgTexture", "netherrack", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_quartz", "baseBlockAFgTexture", "oreshards:orepattern1", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

			config.get("ores.ore_redstone", "shardsInOre", 5, "Number of shards to drop from the OreShard block in total").getInt(5);
			config.get("ores.ore_redstone", "shardChance1", 1.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(1.0);
			config.get("ores.ore_redstone", "shardChance2", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_redstone", "shardChance3", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_redstone", "shardChance4", 0.5, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.5);
			config.get("ores.ore_redstone", "shardChance5", 0.0, "Chance to drop nth shard as 'result' from the OreShard (else dumb)").getDouble(0.0);
			config.get("ores.ore_redstone", "resultShard", "redstone", "Reference to the ore shard").getString().trim();
			config.get("ores.ore_redstone", "dumbShard", "cobble", "Reference to the dumb shard").getString().trim();
			config.get("ores.ore_redstone", "baseBlock", "minecraft:redstone_ore", "The block to take misc data from (hardness, step sound, ...)").getString().trim();
			config.get("ores.ore_redstone", "baseBlockMeta", 0, "Its meta value").getInt(0);
			config.get("ores.ore_redstone", "baseBlockFgTexture", "redstone_ore", "The name of the texture to use as foreground for the block").getString().trim();
			config.get("ores.ore_redstone", "baseBlockBgTexture", "stone", "The name of the texture to use as background for the block").getString().trim();
			config.get("ores.ore_redstone", "baseBlockAFgTexture", "oreshards:orepattern2", "The name of the texture to use as alpha channel for the foreground texture").getString().trim();

		}
	}
}
