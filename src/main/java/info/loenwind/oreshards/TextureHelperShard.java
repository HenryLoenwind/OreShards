package info.loenwind.oreshards;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResourceManager;

import java.awt.image.BufferedImage;

@SideOnly(Side.CLIENT)
public class TextureHelperShard extends TextureHelperBase {

    private int renderType = 0;

    // texture => texture of the ore
    public TextureHelperShard(String texture, int renderType) {
        super(getDerivedName(texture, "shard"));
        this.oreTexture = texture;
        this.renderType = renderType;
    }

    @Override
    protected String getSubidentifier() {
    	return "shard";
    }

    @Override
    protected boolean createTexture(IResourceManager manager, BufferedImage[] ore_image) {
		
		int textureWidth = ore_image[0].getWidth();
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
            int[] new_data = createShardTexture(textureWidth, ore_data, renderType);

            // write the new image data to the output image buffer
            output_image.setRGB(0, y, textureWidth, textureWidth, new_data, 0, textureWidth);
        }
        
        return false;
	}


	private static final byte[] SHARDMASK = {
    	0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 0, 
    	0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 
    	0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 
    	0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 
    	0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 
    	0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 1, 0, 0, 0, 
    	0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 1, 2, 1, 0, 0, 
    	0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 1, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 
    	0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0
    };
    
    private static byte shardmask(int renderType, int size, int x, int y) {
    	// only one mask for now
    	return SHARDMASK[ textureXYTo16(size, x) + textureXYTo16(size, y) * 16 ];
    }
    
    protected int[] createShardTexture(int w, int[] ore_data, int renderType) {
        int[] new_data = new int[w * w];

        // where the magic happens
        for (int i = 0; i < ore_data.length; i += 1) {
            int x = (i % w);
            int y = (i - x) / w;

            byte action = shardmask(renderType, w, x, y);
            
            if (action == 0) {
            	new_data[i] = 0x00000000; 
            } else if (action == 1) {
            	new_data[i] = 0x7f000000;
            } else {
            	new_data[i] = ore_data[x + y * w] | 0xff000000;
            }
        }
        return new_data;
	}

}
