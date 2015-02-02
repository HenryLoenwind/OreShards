package info.loenwind.oreshards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class OreShardsBlock extends BlockOre {

    final private String id;
    final private Item result;
    final private Item dumb;
    final private Block baseBlock;
    final private int baseBlockMeta;
    final private String textureFG;
    final private String textureBG;
    final private List<Double> chances;
	final private String textureAFG;
    
	public OreShardsBlock(String id, Item result, Item dumb, Block baseBlock, int baseBlockMeta, String textureFG, String textureBG, String textureAFG, List<Double> chances) {
		super();
		this.id = id;
		this.result = result;
		this.dumb = dumb;
		this.baseBlock = baseBlock;
		this.baseBlockMeta = baseBlockMeta;
		this.textureFG = textureFG;
		this.textureBG = textureBG;
		this.textureAFG = textureAFG;
		this.chances = chances;
	}

	@Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta) {
        return baseBlock.canHarvestBlock(player, baseBlockMeta);
    }

    @Override
    public int getHarvestLevel(int meta) {
        return baseBlock.getHarvestLevel(baseBlockMeta);
    }

    @Override
    public String getHarvestTool(int meta) {
        return baseBlock.getHarvestTool(baseBlockMeta);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        int mymeta = world.getBlockMetadata(x, y, z);
        try {
            world.setBlock(x, y, z, baseBlock, baseBlockMeta, 0);
            for (int i = 0; i < 1 + rand.nextInt(3); i++)
            	baseBlock.randomDisplayTick(world, x, y, z, rand);
        } finally {
            world.setBlock(x, y, z, this, mymeta, 0);
        }
    }
    
    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
    	int mymeta = world.getBlockMetadata(x, y, z);
    	float t = this.blockHardness;
    	try {
        	world.setBlockMetadataWithNotify(x, y, z, baseBlockMeta, 0);
    		t = baseBlock.getBlockHardness(world, x, y, z);
    	} finally {
    		world.setBlockMetadataWithNotify(x, y, z, mymeta, 0);
    	}
    	return t;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        double extra = 1.0 + (fortune * 0.05);
        
        for (Double chance : chances) {
			if (world.rand.nextDouble() < chance * extra) {
				ret.add(new ItemStack(result));
			} else {
				ret.add(new ItemStack(dumb));
			}
		}
        
        return ret;
    }

	@Override
	public String getUnlocalizedName() {
		return "tile." + OreShardsMod.MODID + "_" + this.id;
	}

	@Override
	public void registerBlockIcons(IIconRegister register) {
		if (register instanceof TextureMap) { // should always be true (...but
			// you never know)
			TextureMap map = (TextureMap) register;
			
			TextureHelperBase new_texture = new TextureHelper(textureFG, textureAFG, textureBG, null, true);
			String name = new_texture.getDerivedName();
			TextureAtlasSprite existing_texture = map.getTextureExtry(name);
			
			if (existing_texture != null) {
				this.blockIcon = existing_texture;
			} else {
				map.setTextureEntry(name, new_texture);
				this.blockIcon = new_texture;
			}
		}
	}

    
    
}
