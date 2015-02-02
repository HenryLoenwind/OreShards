package info.loenwind.oreshards;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TextureHelperLight extends TextureHelperBase {

    private int renderType = 0;

    // texture => texture of the ore
    // base => texture of the stone block the ore is based on
    public TextureHelperLight(String texture, String base, int renderType) {
        super(getDerivedName(texture, "light" + renderType));
        this.oreTexture = texture;
        this.baseTexture = base;
        this.renderType = renderType;
    }

    @Override
    protected String getSubidentifier() {
    	return "light" + renderType;
    }
    
    @Override
    protected boolean createTexture(IResourceManager manager, BufferedImage[] ore_image) {
		
		int textureWidth = ore_image[0].getWidth();
		
		int[] stone_data = getBaseTexture(manager, textureWidth);
        if (stone_data == null) {
        	return true;
        }

        int h = ore_image[0].getHeight();

        // create an ARGB output image that will be used as our texture
        output_image = new BufferedImage(textureWidth, h, 2);

        // create some arrays t hold the pixel data
        // pixel data is in the form 0xaarrggbb
        int[] ore_data = new int[textureWidth * textureWidth];


        for (int y = 0; y < h; y += textureWidth) {
            // read the ARGB color data into our arrays
            ore_image[0].getRGB(0, y, textureWidth, textureWidth, ore_data, 0, textureWidth);

            // generate our new texture
            int[] new_data = createLightTexture(textureWidth, ore_data, stone_data, renderType);

            // write the new image data to the output image buffer
            output_image.setRGB(0, y, textureWidth, textureWidth, new_data, 0, textureWidth);
        }
        
        return false;
	}


	private static final byte[] MASK0 = {
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 
		1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 
		0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1
    };

	private static final byte[] MASK1 = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 
		0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 
		0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 
		0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 
		0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 
		0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 
		0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };

	private static final byte[] MASK2 = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 
		0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 
		0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
	
    private static byte shardmask(int renderType, int size, int x, int y) {
    	if (renderType == 1) {
    		return MASK1[ textureXYTo16(size, x) + textureXYTo16(size, y) * 16 ];
    	} else if (renderType == 2) {
    		return MASK2[ textureXYTo16(size, x) + textureXYTo16(size, y) * 16 ];
    	} else if (renderType == 3) {
    		return MASK2[ (15 - textureXYTo16(size, x)) + textureXYTo16(size, y) * 16 ];
    	} else if (renderType == 4) {
    		return MASK2[ (15 - textureXYTo16(size, x)) + (15 - textureXYTo16(size, y)) * 16 ];
    	} else if (renderType == 5) {
    		return MASK2[ textureXYTo16(size, x) + (15 - textureXYTo16(size, y)) * 16 ];
    	} else {
    		return MASK0[ textureXYTo16(size, x) + textureXYTo16(size, y) * 16 ];
    	}
    }
    
    protected int[] createLightTexture(int w, int[] ore_data, int[] stone_data, int renderType) {
        int[] new_data = new int[w * w];

        for (int i = 0; i < ore_data.length; i += 1) {
            int x = (i % w);
            int y = (i - x) / w;

            byte action = shardmask(renderType, w, x, y);
            
            if (action == 1) {
            	new_data[i] = ore_data[x + y * w]; 
            } else {
            	new_data[i] = stone_data[x + y * w];
            }
        }
        return new_data;
	}

}
