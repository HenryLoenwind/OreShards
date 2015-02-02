package info.loenwind.oreshards;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

public abstract class TextureHelperBase extends TextureAtlasSprite {

	public String oreTexture;
	public String baseTexture;
	public BufferedImage output_image = null;

	public String getDerivedName() {
		return getDerivedName(oreTexture, getSubidentifier());
	}

	public static String getDerivedName(String oreTexture, String subidentifier) {
	    String s1 = "minecraft";
	    String s2;
	
	    int ind = oreTexture.indexOf(58);
	
	    if (ind >= 0) {
	        if (ind > 1) {
	            s1 = oreTexture.substring(0, ind);
	        }
	        s2 = oreTexture.substring(ind + 1, oreTexture.length());
	    } else {
	    	s2 = oreTexture;
	    }
	
	    s1 = s1.toLowerCase();
	
	    return OreShardsMod.MODID + ":" + s1 + "/" + s2 + "_" + subidentifier;
	}

	public static ResourceLocation getBlockResource(String s2) {
	    String s1 = "minecraft";
	
	    int ind = s2.indexOf(58);
	
	    if (ind >= 0) {
	        if (ind > 1) {
	            s1 = s2.substring(0, ind);
	        }
	        s2 = s2.substring(ind + 1, s2.length());
	    }
	
	    s1 = s1.toLowerCase();
	    s2 = "textures/blocks/" + s2 + ".png";
	
	    return new ResourceLocation(s1, s2);
	}

	protected abstract String getSubidentifier();
	
	protected static int textureXYTo16(int size, int xy) {
		if (size == 16) {
			return xy;
		}
	
		// assume texture sizes are multiples of 16
		int factor = size / 16;
		int result = xy / factor;
		
		if (result >= 16) {
			return 15;
		} else {
			return result;
		}
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		return true;
	}

	public TextureHelperBase(String p_i1282_1_) {
		super(p_i1282_1_);
	}

	protected int getAlpha(int col) {
	    return (col & 0xff000000) >> 24;
	}

	protected int getRed(int col) {
	    return (col & 0x00ff0000) >> 16;
	}

	protected int getGreen(int col) {
	    return (col & 0x0000ff00) >> 8;
	}

	protected int getBlue(int col) {
	    return col & 0x000000ff;
	}

	protected int makeCol(int red, int green, int blue, int alpha) {
	    return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	protected int[] getBaseTexture(IResourceManager manager, int textureSize) {
		BufferedImage stone_image;
	
	    try {
	        IResource iresourceBase = manager.getResource(getBlockResource(baseTexture));
	
	        stone_image = ImageIO.read(iresourceBase.getInputStream());
	
	        if (stone_image.getWidth() != textureSize) {
	            List resourcePacks = manager.getAllResources(getBlockResource(baseTexture));
	            for (int i = resourcePacks.size() - 1; i >= 0; --i) {
	                IResource resource = (IResource) resourcePacks.get(i);
	                stone_image = ImageIO.read(resource.getInputStream());
	
	                if (stone_image.getWidth() == textureSize)
	                    break;
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	
	    if (stone_image.getWidth() != textureSize) {
	    	OreShardsMod.logger.error("Error generating texture '" + oreTexture + (baseTexture != null ? "' on '" + baseTexture : "") + "'. Unable to find base texture with same size.");
	        return null;
	    }
	
	    int[] stone_data = new int[textureSize * textureSize];
	    stone_image.getRGB(0, 0, textureSize, textureSize, stone_data, 0, textureSize);
		return stone_data;
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
	
	    int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels;
	    BufferedImage[] ore_image = new BufferedImage[1 + mp];
	    AnimationMetadataSection animation;
	
	    try {
	        IResource iresource = manager.getResource(getBlockResource(oreTexture));
	        ore_image[0] = ImageIO.read(iresource.getInputStream());
	        animation = (AnimationMetadataSection) iresource.getMetadata("animation");
	    } catch (IOException e) {
	    	OreShardsMod.logger.info("Failed to get texture for " + oreTexture);
	        e.printStackTrace();
	        return true;
	    }
	
	    if (createTexture(manager, ore_image)) {
	    	return true;
	    } else {
	        ore_image[0] = output_image;
	        this.loadSprite(ore_image, animation, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
	        OreShardsMod.logger.info("Succesfully generated texture '" + getDerivedName() + "' for '" + oreTexture + (baseTexture != null ? "' on '" + baseTexture : "") + "'.");
	        return false;
	    }
	}

	protected abstract boolean createTexture(IResourceManager manager, BufferedImage[] ore_image);

}