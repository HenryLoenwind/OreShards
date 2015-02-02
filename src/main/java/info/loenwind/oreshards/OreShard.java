package info.loenwind.oreshards;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class OreShard extends Item {

	private static final String TEXTURE_AFG = OreShardsMod.MODID + ":shardfg";
	private static final String TEXTURE_BG = OreShardsMod.MODID + ":shardbg";
	
	private final String textureFG;
	private final String textureAFG;
	private final String textureBG;
	
	public OreShard(String id, String textureFG, String textureAFG, String textureBG) {
		super();
		this.textureFG = textureFG;
		this.textureAFG = (textureAFG != null && !textureAFG.isEmpty() ? textureAFG : TEXTURE_AFG);
		this.textureBG = (textureBG != null && !textureBG.isEmpty() ? textureBG : TEXTURE_BG);
		setUnlocalizedName(OreShardsMod.MODID + "_" + id);
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister register) {
		if (register instanceof TextureMap) { // should always be true (...but
			// you never know)
			TextureMap map = (TextureMap) register;
			
			TextureHelperBase new_texture = new TextureHelper(this.textureFG, textureAFG, textureBG, null, false);
			String name = new_texture.getDerivedName();
			TextureAtlasSprite existing_texture = map.getTextureExtry(name);
			
			if (existing_texture != null) {
				this.itemIcon = existing_texture;
			} else {
				map.setTextureEntry(name, new_texture);
				this.itemIcon = new_texture;
			}
		}
	}

}
