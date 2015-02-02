package info.loenwind.oreshards;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TextureHelper extends TextureHelperBase {


    private final String textureFG;
	private final String textureAFG;
	private final String textureBG;
	private final String textureABG;
	private final boolean noTransparency;

	@Override
	public String getDerivedName() {
		return getDerivedName(textureFG, textureAFG, textureBG, textureABG, noTransparency);
	}
	
	public static String getDerivedName(String textureFG, String textureAFG, String textureBG, String textureABG, boolean noTransparency) {
	    String s1 = textureFG + "_" + textureAFG + "_" + textureBG + "_" + textureABG + "_" + noTransparency;
	    
	    s1 = s1.replace("minecraft:", "");
	    s1 = s1.replace(OreShardsMod.MODID + ":", "");
	    s1 = s1.replace("_null_", "_"); // assume people won't create textures like A/null/B/C and A/B/null/C...
	    s1 = s1.replace(":", "-");
	    s1 = s1.toLowerCase();
	    
	    return OreShardsMod.MODID + ":" + s1;
	}

    public TextureHelper(String textureFG, String textureAFG, String textureBG, String textureABG, boolean noTransparency) {
        super(getDerivedName(textureFG, textureAFG, textureBG, textureABG, noTransparency));
        this.oreTexture = this.textureFG = textureFG;
        this.textureAFG = textureAFG;
        this.textureBG = textureBG;
        this.textureABG = textureABG;
        this.noTransparency = noTransparency;
    }

    @Override
    protected String getSubidentifier() {
    	return null;
    }
    
	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
	
	    int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels;
	    BufferedImage[] ore_image = new BufferedImage[1 + mp];
	
        ore_image[0] = mkTexture(manager);
        if (ore_image[0] == null) {
        	return true;
        }
        this.loadSprite(ore_image, null, (float) Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0F);
        OreShardsMod.logger.info("Succesfully generated texture '" + getDerivedName() + "' for '" + textureFG + (textureBG != null ? "' on '" + textureBG : "") + "'.");
        return false;
	}

    @Override
    protected boolean createTexture(IResourceManager manager, BufferedImage[] ore_image) {
        return false;
	}

protected BufferedImage getTexture(IResourceManager manager, String texture) {
	if (texture == null || texture.length() == 0) {
		return null;
	}
	ResourceLocation blockResource = getBlockResource(texture);
	try {
		IResource iResource = manager.getResource(blockResource);
		if (iResource == null) {
			return null;
		}
		BufferedImage image = ImageIO.read(iResource.getInputStream());
		return image;
	} catch (IOException e) {
		OreShardsMod.logger.error("Failed to load " + blockResource + ": " + e);
	}
	return null;
}

protected BufferedImage mkTexture(IResourceManager manager) {
	
	BufferedImage imageFG = getTexture(manager, textureFG);
	BufferedImage imageAFG = getTexture(manager, textureAFG);
	BufferedImage imageBG = getTexture(manager, textureBG);
	BufferedImage imageABG = getTexture(manager, textureABG);
	
	boolean skipFG = (imageFG == null);
	boolean skipAFG = skipFG | (imageAFG == null);
	boolean forceAFG = false;
	boolean skipBG = (imageBG == null);
	boolean skipABG = skipBG | (imageABG == null);
	boolean forceABG = false;
	
	if (noTransparency) {
		if (skipBG) {
			forceAFG = true;
			skipAFG = true;
		} else {
			forceABG = true;
			skipABG = true;
		}
	}
	
	if (skipFG && skipBG) {
		OreShardsMod.logger.error("Cannot generate texture from nothing, sorry...");
		return null;
	}
	
	int sizeMax = 0;
	
	if (!skipFG && (imageFG.getWidth() > sizeMax)) {
		sizeMax = imageFG.getWidth();
	}
	if (!skipAFG && (imageAFG.getWidth() > sizeMax)) {
		sizeMax = imageAFG.getWidth();
	}
	if (!skipBG && (imageBG.getWidth() > sizeMax)) {
		sizeMax = imageBG.getWidth();
	}
	if (!skipABG && (imageABG.getWidth() > sizeMax)) {
		sizeMax = imageABG.getWidth();
	}
	
	int[] dataFG = null;
	int[] dataAFG = null;
	int[] dataBG = null;
	int[] dataABG = null;
	
	if (!skipFG) {
		imageFG = resize(imageFG, sizeMax);
		dataFG = new int[sizeMax * sizeMax];
		imageFG.getRGB(0, 0, sizeMax, sizeMax, dataFG, 0, sizeMax);
	}
	if (!skipAFG) {
		imageAFG = resize(imageAFG, sizeMax);
		dataAFG = new int[sizeMax * sizeMax];
		imageAFG.getRGB(0, 0, sizeMax, sizeMax, dataAFG, 0, sizeMax);
	}
	if (!skipBG) {
		imageBG = resize(imageBG, sizeMax);
		dataBG = new int[sizeMax * sizeMax];
		imageBG.getRGB(0, 0, sizeMax, sizeMax, dataBG, 0, sizeMax);
	}
	if (!skipABG) {
		imageABG = resize(imageABG, sizeMax);
		dataABG = new int[sizeMax * sizeMax];
		imageABG.getRGB(0, 0, sizeMax, sizeMax, dataABG, 0, sizeMax);
	}
	
	if (!skipAFG || forceAFG) {
		if (forceAFG) {
			for (int i=0; i<(sizeMax*sizeMax); i++) {
			 	dataFG[i] = dataFG[i] | 0xFF000000;
			}
		} else {
			for (int i=0; i<(sizeMax*sizeMax); i++) {
				dataFG[i] = ((dataFG[i] & 0x00FFFFFF) | (dataAFG[i] & 0xFF000000)) & (dataFG[i] | 0x00FFFFFF);
			}
		}
		imageFG.setRGB(0, 0, sizeMax, sizeMax, dataFG, 0, sizeMax);
	}
	
	if (!skipABG || forceABG) {
		if (forceABG) {
			for (int i=0; i<(sizeMax*sizeMax); i++) {
			 	dataBG[i] = dataBG[i] | 0xFF000000;
			}
		} else {
			for (int i=0; i<(sizeMax*sizeMax); i++) {
				dataBG[i] = (dataBG[i] & 0x00FFFFFF) | (dataABG[i] & 0xFF000000);
			}
		}
		imageBG.setRGB(0, 0, sizeMax, sizeMax, dataBG, 0, sizeMax);
	}
	
	BufferedImage result = new BufferedImage (sizeMax, sizeMax, BufferedImage.TYPE_INT_ARGB);
	Graphics2D g2d = result.createGraphics();
	if (!skipBG) {
		g2d.drawImage(imageBG, null, 0, 0);
	}
	if (!skipFG) {
		g2d.drawImage(imageFG, null, 0, 0);
	}
	g2d.dispose ();
	
	return result; 
}

protected BufferedImage resize(BufferedImage image, int size) {
	if (image.getWidth() != size) {
		BufferedImage resized = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	  Graphics2D g = resized.createGraphics();
	  g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	  g.drawImage(image, 0, 0, size, size, 0, 0, image.getWidth(), image.getHeight(), null);
	  g.dispose();
	  return resized;
	} else {
		return image;
	}
}

}


