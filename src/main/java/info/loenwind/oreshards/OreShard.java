package info.loenwind.oreshards;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class OreShard extends Item {

	private final String texture;
	
	public OreShard(String id, String texture) {
		super();
		this.texture = texture;
		setUnlocalizedName(OreShardsMod.MODID + "_" + id);
		setCreativeTab(CreativeTabs.tabMisc);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister register) {
		if (register instanceof TextureMap) { // should always be true (...but
			// you never know)
			TextureMap map = (TextureMap) register;
			
			TextureHelperBase new_texture = new TextureHelperShard(this.texture, 0);
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
