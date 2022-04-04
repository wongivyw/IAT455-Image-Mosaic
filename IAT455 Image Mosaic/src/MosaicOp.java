/*
 * MosaicOp performs functions on images to manipulate to create a mosaic effect.
 */
package src;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.MosaicPanel;

public class MosaicOp {
/*
 * Tile size can be anything divisible by the size of the square source image. The size of the current source is 5250x5250.
 * 	5250 is divisible by 3, 5, 6, 7, 10, 14, 15, 21, 25, 30, 35, 42, 50, 70, 75, 105, 125, 150, 175, 210, 250, 350, 375,
 *  525, 750, 875, 1050, 1750, 2625
 */
	
	public final static int TILE_SIZE = 50;

	ArrayList<TileImage> tiles; // tile images used for mosaic
	ArrayList<BufferedImage> srcTiles; // src image split up into tiles
	BufferedImage src; // image to turn into a mosaic
	
	int width, height;
	
	private int rows, cols, numTiles; // number of rows == number of cols
	
	public MosaicOp(BufferedImage src) {
		tiles = new ArrayList<TileImage>();
		this.src = src; // should be divisible by TILE_SIZE
		width = src.getWidth();
		height = src.getHeight();
		
		rows = src.getHeight(null)/TILE_SIZE;
		cols = rows;
		
	}
	
	public BufferedImage getMosaicImage() {
		return mosaicTest1(src, TILE_SIZE);
	}
	
	
	//testing without tile images. tile effect on original image
	private BufferedImage mosaicTest1(BufferedImage src, int tileSize) {
		BufferedImage result = new BufferedImage(width, height, src.getType());

		int numTiles = width/tileSize;
		
		int posX, posY;
		for (int i = 0; i < numTiles; i++) { //y
			posY = tileSize*i; //top left corner of tile in y-axis
			for (int j = 0; j < numTiles; j++) { //x
				//for each tile
				posX = tileSize*j; //top left corner of the tile in x-axis
				
				//TEST 1 -- completed
//				change the whole tile to the color of pixel in top left corner of tile
				int rgb = src.getRGB(posX, posY); //color of pixel in top left corner of tile
				
				//TEST 2 -- completed
				//find average color in tile and set the tile to that
				// for loop to iterate through each pixel in tile
				int red = 0, green = 0, blue = 0;
				for (int k = 0; k < tileSize; k++) { //y
					red=0;
					green=0;
					blue=0;
					for (int l = 0; l < tileSize; l++) {//x
						int rgba = src.getRGB(posX+l, posY+k);
						red += getRed(rgba);
						green += getGreen(rgba);
						blue += getBlue(rgba);
					}
					
				}
				red = clip(red/tileSize);
				green = clip(green/tileSize);
				blue = clip(blue/tileSize);
				int rgba = new Color(red, green, blue).getRGB();	
				
				//TEST 3:
				
								
				/* Algorithm:
				 * - Reduce source tile to lowest bits
				 * - Obtain color from the source tile
				 * - Color match with source tile color to color of tile images
				 * - Best match = replace source tile with tile image
				 */
				
				// for loop to iterate through each pixel in tile
				for (int k = 0; k < tileSize; k++) { //y
					for (int l = 0; l < tileSize; l++) {//x
//						result.setRGB(posX+l, posY+k, rgb); // use top left corner in tile
						result.setRGB(posX+l, posY+k, rgba); // use average color in tile

					}
				}
				//change the color of the tile to the most common color
				
			}
		}
		
		return result;
	}
	
	// helper methods
	protected int getRed(int pixel) {
		return (pixel >>> 16) & 0xFF;
	}

	protected int getGreen(int pixel) {
		return (pixel >>> 8) & 0xFF;
	}

	protected int getBlue(int pixel) {
		return pixel & 0xFF;
	}
    private int clip(int v) {
        v = v > 255 ? 255 : v;
        v = v < 0 ? 0 : v;
        return v;
    }
}
