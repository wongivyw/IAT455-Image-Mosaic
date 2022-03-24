package src;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MosaicOp {
//	2250 is divisible by 2, 3, 5, 6, 9, 10, 15, 18, 25, 30, 45, 50, 75, 90, 125, 150, 225, 250, 375, 450, 750, 1125
	public final static int TILE_SIZE = 90;

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
		return mosaicTest(src, TILE_SIZE); // change to return mosaic image 
	}
	
	/*this method splits the source image into tiles and stores them
	* in an arraylist
	*/
	private void splitImageIntoTiles() {
		
	}
	
	//testing without tile images. tile effect on original image
	private BufferedImage mosaicTest(BufferedImage src, int tileSize) {
		BufferedImage result = new BufferedImage(width, height, src.getType());

		int numTiles = width/tileSize;
		
		int posX, posY;
		for (int i = 0; i < numTiles; i++) { //y
			posY = tileSize*i; //top left corner of tile in y-axis
			for (int j = 0; j < numTiles; j++) { //x
				//for each tile
				posX = tileSize*j; //top left corner of the tile in x-axis
				
				//TEST 1
//				change the whole tile to the color of pixel in top left corner of tile
				int rgb = src.getRGB(posX, posY); //color of pixel in top left corner of tile
				
				//TEST 2
				//find most common color in tile and set the tile to that
				
				
				/* Algorithm:
				 * - Reduce source tile to lowest bits
				 * - Obtain color from the source tile
				 * - Color match with source tile color to color of tile images
				 * - Best match = replace source tile with tile image
				 */
				
				// for loop to iterate through the tiles
				for (int k = 0; k < tileSize; k++) { //y
					for (int l = 0; l < tileSize; l++) {//x
						result.setRGB(posX+l, posY+k, rgb);
					}
				}
				//change the color of the tile to the most common color
				
			}
		}
		
		return result;
	}
}
