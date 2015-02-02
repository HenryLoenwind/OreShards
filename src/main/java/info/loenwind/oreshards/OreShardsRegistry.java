package info.loenwind.oreshards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;

public class OreShardsRegistry {

	private static final List<Dumb> dumbs = new ArrayList();
	private static final List<Ore>  ores = new ArrayList();
	
	private static final Map<String, OreShard> shards = new HashMap();
	
	public static void registerDumb(String id, String item, int meta, String textureFG, String textureAFG, String textureBG, int count) {
		Dumb dumb = new Dumb(id, item, meta, textureFG, textureAFG, textureBG, count);
		dumbs.add(dumb);
	}

	public static void registerOre(String id, String resultShard, String dumbShard, 
			String baseBlock, int baseBlockMeta, 
			String baseBlockFgTexture, String baseBlockBgTexture, String baseBlockAFgTexture, 
			int shardsInOre, List<Double> chances) {
		Ore ore = new Ore(id, resultShard, dumbShard, baseBlock, baseBlockMeta, baseBlockFgTexture, baseBlockBgTexture, baseBlockAFgTexture, shardsInOre, chances);
		ores.add(ore);
	}

	public static void preInit() {
		for (Dumb dumb : dumbs) {
			if (itemExists(dumb.item)) {
				makeShard(dumb.id, dumb.textureFG, dumb.textureAFG, dumb.textureBG);
			} else {
				OreShardsMod.logger.info("Item '"+dumb.item+"' does not exists. Skipping shard generation for '"+dumb.id+"'.");
			}
		}
		for (Ore ore : ores) {
//			Item shard = makeShard(ore.id, ore.texture);
			Block baseBlock = GameData.getBlockRegistry().getObject(ore.baseBlock);
			if (baseBlock != null) {
				OreShard shard1 = getShard(ore.resultShard);
				OreShard shard2 = getShard(ore.dumb);
				if (shard1 != null && shard2 != null) {
					Block block = new OreShardsBlock(ore.id, shard1, shard2, baseBlock, ore.baseBlockMeta, ore.baseBlockFgTexture, ore.baseBlockBgTexture, ore.baseBlockAFgTexture, ore.chances);
					GameRegistry.registerBlock(block, OreShardsMod.MODID + "_ore_"+ore.id);
				}
			} else {
				OreShardsMod.logger.info("Base block '"+ore.baseBlock+"' does not exists. Skipping shard block generation for '"+ore.id+"'.");
			}
		}
	}

	private static boolean itemExists(String item) {
		return GameData.getItemRegistry().getObject(item) != null;
	}
	
	public static void init() {
		for (Dumb dumb : dumbs) {
			Item shard = getShard(dumb.id);
			if (shard != null) {
				ItemStack stack = new ItemStack(GameData.getItemRegistry().getObject(dumb.item), 1, dumb.meta);
				registerRecipe(shard, dumb.count, stack);
			}
		}
	}

	private static OreShard makeShard(String id, String textureFG, String textureAFG, String textureBG) {
		OreShard shard = new OreShard(id, textureFG, textureAFG, textureBG);
		GameRegistry.registerItem(shard, OreShardsMod.MODID + "_shard_" + id);
		shards.put(id, shard);
		return shard;
	}

	private static OreShard getShard(String id) {
		return shards.get(id);
	}

	private static void registerRecipe(Item shard, int count, ItemStack result) {
		switch (count) {
		case 1:
			GameRegistry.addShapelessRecipe(result, new Object[] { shard });
			break;
		case 2:
			GameRegistry.addShapelessRecipe(result, new Object[] { shard, shard });
			break;
		case 3:
			GameRegistry.addRecipe(result, new Object[]{ "AAA",               'A', shard });
			GameRegistry.addRecipe(result, new Object[]{ "A",   "A",   "A",   'A', shard });
			break;
		case 4:
			GameRegistry.addRecipe(result, new Object[]{"AA", "AA", 'A', shard });
			break;
		case 5:
			GameRegistry.addRecipe(result, new Object[]{ " A ", "AAA", " A ", 'A', shard });
			GameRegistry.addRecipe(result, new Object[]{ "A A", " A ", "A A", 'A', shard });
			break;
		case 6:
			GameRegistry.addRecipe(result, new Object[]{ "AAA", "   ", "AAA", 'A', shard });
			GameRegistry.addRecipe(result, new Object[]{ "A A", "A A", "A A", 'A', shard });
			GameRegistry.addRecipe(result, new Object[]{ "AAA", "AAA",        'A', shard });
			GameRegistry.addRecipe(result, new Object[]{ "AA",  "AA",  "AA",  'A', shard });
			break;
		case 7:
			GameRegistry.addRecipe(result, new Object[]{ "AAA", " A ", "AAA", 'A', shard });
			GameRegistry.addRecipe(result, new Object[]{ "A A", "AAA", "A A", 'A', shard });
			break;
		case 8:
			GameRegistry.addRecipe(result, new Object[]{ "AAA", "A A", "AAA", 'A', shard });
			break;
		case 9:
			GameRegistry.addRecipe(result, new Object[]{ "AAA", "AAA", "AAA", 'A', shard });
			break;

		default:
			break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static class Dumb {
		public final String textureBG;
		public final String textureAFG;
		public final String textureFG;
		public final String id;
		public final String item;
		public final int meta;
		public final int count;

		public Dumb(String id, String item, int meta, String textureFG, String textureAFG, String textureBG, int count) {
			this.id = id;
			this.item = item;
			this.meta = meta;
			this.count = count;
			this.textureFG = textureFG;
			this.textureAFG = textureAFG;
			this.textureBG = textureBG;
		}
	}

	private static class Ore {
		public final String baseBlockAFgTexture;
		public final String id;
		public final String resultShard;
		public final String dumb;
		public final String baseBlock;
		public final int baseBlockMeta;
		public final String baseBlockFgTexture;
		public final String baseBlockBgTexture;
		public final int shardsInOre;
		public final List<Double> chances;

		public Ore(String id, String resultShard, String dumb,
				String baseBlock, int baseBlockMeta, String baseBlockFgTexture, String baseBlockBgTexture, String baseBlockAFgTexture, int shardsInOre,
				List<Double> chances) {
			this.id = id;
			this.resultShard = resultShard;
			this.dumb = dumb;
			this.baseBlock = baseBlock;
			this.baseBlockMeta = baseBlockMeta;
			this.baseBlockFgTexture = baseBlockFgTexture;
			this.baseBlockBgTexture = baseBlockBgTexture;
			this.baseBlockAFgTexture = baseBlockAFgTexture;
			this.shardsInOre = shardsInOre;
			this.chances = chances;
		}
	}


}
