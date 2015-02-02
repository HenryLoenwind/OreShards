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
public class TextureHelperDense extends TextureHelperBase {

    private int renderType = 0;

    // texture => texture of the ore
    // base => texture of the stone block the ore is based on
    public TextureHelperDense(String texture, String base, int renderType) {
        super(getDerivedName(texture, "dense" + renderType));
        this.oreTexture = texture;
        this.baseTexture = base;
        this.renderType = renderType;
    }

	@Override
	protected String getSubidentifier() {
		return "dense" + renderType;
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
            int[] new_data = createDenseTexture(textureWidth, ore_data, stone_data, renderType);

            // write the new image data to the output image buffer
            output_image.setRGB(0, y, textureWidth, textureWidth, new_data, 0, textureWidth);
        }
        
        return false;
	}

	protected int[] createDenseTexture(int w, int[] ore_data, int[] stone_data, int renderType) {
        int[] new_data = new int[w * w];

        // we need to work out which pixels should be considered 'ore pixels' and which should be 'base pixels'
        boolean[] same = new boolean[w * w];
        for (int i = 0; i < ore_data.length; i += 1) {
            if (getAlpha(ore_data[i]) == 0) {   // if the ore texture pixel is transparent, overwrite with the corresponding stone pixel
                same[i] = true;
                ore_data[i] = stone_data[i];
            } else if (ore_data[i] == stone_data[i]) {
                same[i] = true;
            } else {
                int r = Math.abs(getRed(ore_data[i]) - getRed(stone_data[i]));
                int g = Math.abs(getGreen(ore_data[i]) - getGreen(stone_data[i]));
                int b = Math.abs(getBlue(ore_data[i]) - getBlue(stone_data[i]));

                same[i] = (r + g + b) < 20; // check to see if the two pixels are not exactly the same but 'close'
            }

            new_data[i] = ore_data[i];
        }

        int[] dx;
        int[] dy;

        //allows for different convolution filters
        switch (renderType) {
            default:
            case 0:
                dx = new int[]{-1, 2, 3};
                dy = new int[]{-1, 0, 1};
                break;
            case 1:
                dx = new int[]{-1, 1, 0, 0, -1, -1, 1, 1, -2, 2, 0, 0};
                dy = new int[]{0, 0, -1, 1, -1, 1, -1, 1, 0, 0, -2, 2};
                break;
            case 2:
                dx = new int[]{-1, 0, 1};
                dy = new int[]{-1, 0, 1};
                break;
            case 3:
                dx = new int[]{-2, 2, 1, 1};
                dy = new int[]{1, 1, -2, 2};
            case 4:
                dx = new int[]{-6, -3, 3, 6};
                dy = new int[]{0, 0, 0, 0};
                break;
            case 5:
                dx = new int[]{-5, -5, 5, 5};
                dy = new int[]{-5, 5, -5, 5};
                break;
            case 6:
                dx = new int[]{0, 1, 2, 3};
                dy = new int[]{0, -3, 2, -1};
                break;
            case 7:
                dx = new int[]{-1, 1, 0, 0};
                dy = new int[]{0, 0, -1, 1};
                break;
        }


        // where the magic happens
        for (int i = 0; i < ore_data.length; i += 1) {
            int x = (i % w);
            int y = (i - x) / w;

            // if the pixel an ore pixel, we don't need to do anything so continue
            if (!same[i])
                continue;

            // use our convolution filter to see if we can find an ore pixel nearby
            for (int j = 0; j < dx.length; j++) {
                final int new_x = x + dx[j];
                final int new_y = y + dy[j];

                if (new_x >= 0 && new_x < w && new_y >= 0 && new_y < w) // is valid pixel location
                    if (!same[new_x + new_y * w]) { // is it an ore pixel?
                        new_data[i] = ore_data[new_x + new_y * w];
                        break;
                    }
            }
        }
        return new_data;
    }

}
