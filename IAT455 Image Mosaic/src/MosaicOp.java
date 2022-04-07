/*
 * MosaicOp performs functions on images to manipulate to create a mosaic effect.
 */
package src;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.MosaicPanel;

public class MosaicOp {
/*
 * Tile size can be anything divisible by the size of the square source image. The size of the current source is 3547 and 1024.
 * 1024 is divisible by are 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, and 1024.
 * 1280 is divisible by are 1, 2, 4, 5, 8, 10, 16, 20, 32, 40, 64, 80, 128, 160, 256, 320, 640, and 1280.
 */
	
//	public final static int TILE_SIZE = 50;

	private ArrayList<TileImage> tiles; // tile images used for mosaic
	private ArrayList<Color> tileColorAvgs; // tile images used for mosaic
//	ArrayList<BufferedImage> srcTiles; // src image split up into tiles
	private BufferedImage src; // image to turn into a mosaic
	private ArrayList<Color> srcColorAvgs; //array of averages for each tile from src image
	private TileImage controlImage; //control for comparing images (average colors)
	private int width, height;
	private int tileSize = 0;
	
	private int rows, cols, numTiles; // number of rows == number of cols
	
	public MosaicOp(BufferedImage src, ArrayList<TileImage> tiles, int tileSize) {
		this.tiles = tiles;
		srcColorAvgs = new ArrayList<Color>();
		this.src = src; // should be divisible by TILE_SIZE
		width = src.getWidth();
		height = src.getHeight();
		this.tileSize = tileSize;
		
		rows = src.getHeight(null)/tileSize;
		cols = rows;
		
		
	}
	
	public BufferedImage getMosaicImage() {
		// 1. Store tile (of src img) averages in array ---- COMPLETE in srcColorAvgs
		// 2. Store averages of tile images in array ---- COMPLETE in tileColorAvgs
		// 3. Determine tile to be placed in each tile of src img 
		// 4. Place each tile into image
		
		tileColorAvgs = getColorAveragesOfImages(tiles);
		// average color of tile images test print
//		printTileAverages(tiles); //prints rgb values 
		
//		BufferedImage mosaicImage = mosaicTest1(src, TILE_SIZE); 	//Test 1
		srcColorAvgs = calculateSrcColorAvgs(src, tileSize);	//calculates srcColorAvgs
//		printColorAverages(srcAverageTileColors);
		
		
		//Test with a blue image
//		ArrayList<TileImage> controlTile = new ArrayList<TileImage>();
//		controlTile.add(controlImage);
//		ArrayList<Color> controlCol = new ArrayList<Color>();
//		controlCol = getColorAveragesOfImages(controlTile);
		
		ArrayList<TileImage> newListOfTiles = computeBestMatches(tiles, srcColorAvgs);
//		printControlAndComputedListOfTiles(controlImage, newListOfTiles);
		
		BufferedImage mosaicImage = placeTilesIntoImage(src, newListOfTiles, tileSize);
		return mosaicImage;
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
						red += Util.getRed(rgba);
						green += Util.getGreen(rgba);
						blue += Util.getBlue(rgba);
					}
					
				}
				red = Util.clip(red/tileSize);
				green = Util.clip(green/tileSize);
				blue = Util.clip(blue/tileSize);
				int rgba = new Color(red, green, blue).getRGB();	
				
//				srcColorAvgs.add(new Color(rgba));
				
								
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
			}
		}
		return result;
	}
	
	private ArrayList<Color> calculateSrcColorAvgs(BufferedImage src, int tileSize) {
		ArrayList<Color> avgCols = new ArrayList<Color>();
		int numTiles = width/tileSize;
		
		int posX, posY;
		for (int i = 0; i < numTiles; i++) { //y
			posY = tileSize*i; //top left corner of tile in y-axis
			for (int j = 0; j < numTiles; j++) { //x
				//for each tile
				posX = tileSize*j; //top left corner of the tile in x-axis
				
				//find average color in tile
				int red = 0, green = 0, blue = 0;
				for (int k = 0; k < tileSize; k++) { //y
					red=0;
					green=0;
					blue=0;
					for (int l = 0; l < tileSize; l++) {//x
						int rgba = src.getRGB(posX+l, posY+k);
						red += Util.getRed(rgba);
						green += Util.getGreen(rgba);
						blue += Util.getBlue(rgba);
					}
					
				}
				red = Util.clip(red/tileSize);
				green = Util.clip(green/tileSize);
				blue = Util.clip(blue/tileSize);
				
				avgCols.add(new Color(red, green, blue));
			}
		}
		return avgCols;
	}
	
	private ArrayList<Color> getColorAveragesOfImages(ArrayList<TileImage> imgs) {
		ArrayList<Color> res = new ArrayList<Color>();
		if (imgs.isEmpty()) return null;
		for (TileImage i : imgs) {
			res.add(i.getAverageColor());
		}
		return res;
	}
	
	// NOT TESTED
	private ArrayList<TileImage> computeBestMatches(ArrayList<TileImage> tiles, ArrayList<Color> srcColorAverages) {
		
		//sort tile image averages
//		ArrayList<TileImage> tilesSorted = sortByAscendingAvgTileColor(tiles);
		
		ArrayList<TileImage> finalTiles = new ArrayList<TileImage>();
		for (Color avgC : srcColorAverages) {
			//for each average in src tile...
			TileImage bestTile = findBestColorMatch(avgC, tiles);
			if (bestTile != null) finalTiles.add(bestTile);
		}

		return finalTiles;
	}
	
	//NOT TESTED
	// given a color and array of TileImages, returns the TileImage whose avg color is closes to the given color
	private TileImage findBestColorMatch(Color srcCol, ArrayList<TileImage> imgs) {
		if (imgs == null || imgs.isEmpty()) return null;
		int diff = getColorDifference(srcCol, imgs.get(0).getAverageColor());
		TileImage closestTile = imgs.get(0);
		
		for (TileImage tile : imgs) {
			Color tileCol = tile.getAverageColor();
			int currDiff = getColorDifference(srcCol, tileCol);
			if (currDiff < diff) {
				diff = currDiff;
				closestTile = tile;				
			}
		}
		return closestTile;
	}
	
	//NOT TESTED
	//computes the difference between two colors c1 and c2 by comparing rgb values
	private int getColorDifference(Color c1, Color c2) {
		// we will compare red, green, and blue channels separately.
		// the difference will be the sum of the differences in each channel
		// return redDiff + greenDiff + blueDiff;
		int rgb1 = c1.getRGB();
		int rgb2 = c2.getRGB();
		
		int r1 = Util.getRed(rgb1);
		int r2 = Util.getRed(rgb2);
		int g1 = Util.getGreen(rgb1);
		
		int g2 = Util.getGreen(rgb2);
		int b1 = Util.getBlue(rgb1);
		int b2 = Util.getBlue(rgb2);
		
		int rgbDiff = Math.abs(rgb1 - rgb2);
		int redDiff = Math.abs(r1 - r2);
		int greenDiff = Math.abs(g1 - g2);
		int blueDiff = Math.abs(b1 - b2);
		
//		System.out.println("c1 rgb: " + c1.getRGB() + ", C2 rgb: " + c2.getRGB() + 
//				", rgb difference: " + rgbDiff);
//		System.out.println("c1: (" + r1 + ", " + g1 + ", " + b1 + "), c2: (" +
//				r2 + ", " + g2 + ", " + b2 + "), difference: (" + redDiff + ", " + 
//				greenDiff + ", " + blueDiff + ")");
//		System.out.println();
		
		
		return rgbDiff;
	}
	
//	SOURCE: https://stackoverflow.com/questions/4593469/java-how-to-convert-rgb-color-to-cie-lab
//	Goal: to convert rgb to 
	public static float[] fromRGB(int r, int g, int b) {
	    return ColorSpace.getInstance(ColorSpace.CS_CIEXYZ).fromRGB(new float[]{r / 255f, g / 255f, b / 255f});
	}

	//INCOMPLETE
	private BufferedImage placeTilesIntoImage(BufferedImage src, ArrayList<TileImage> tiles, int tileSize) {
		BufferedImage result = new BufferedImage(width, height, src.getType());
		
		if (tiles.isEmpty() || tiles == null) return result;
		//calculate num rows and cols by tiles
		int rows = (int)Math.sqrt(tiles.size());
		int cols = rows;
		//check that the tiles will fit into src
		if (src.getWidth() != tileSize*Math.sqrt(tiles.size()) ||
				src.getHeight() != tileSize*Math.sqrt(tiles.size())) 
			System.out.println("src does not match number of tiles");
		
		
		for (int i = 0; i < tiles.size(); i++) {
			//get top left location of next tile
			int row = i/cols; //even division
			int col = i%cols; //remainder 
			int x = col*tileSize;
			int y = row*tileSize;
			BufferedImage currImg = tiles.get(i).getImage();
			for (int j = 0; j < currImg.getWidth(); j++) {
				for (int k = 0; k < currImg.getHeight(); k++) {
					int rgb = currImg.getRGB(j, k);
					result.setRGB(x+j, y+k, rgb);
					
				}
			}
		}
		
		return result;
	}
	
	//helper print functions for testing
	private void printTileAverages(ArrayList<TileImage> tiles) {
		if (!tiles.isEmpty()) {
			for (TileImage t : tiles) {
				if (t.getAverageColor() == null) System.out.println("null tile");
				else {
					int avgCol = t.getAverageColor().getRGB();
					System.out.println(Util.getRed(avgCol) + ", " + Util.getGreen(avgCol) + ", " + Util.getBlue(avgCol));
				}
			}
		} else {
			System.out.println("Tiles is empty");
		}
	}
	
	private void printColorAverages(ArrayList<Color> cols) {
		if (!cols.isEmpty()) {
			for (Color c : cols) {
				if (c == null) System.out.println("null color");
				else {
					int avgCol = c.getRGB();
					System.out.println(Util.getRed(avgCol) + ", " + Util.getGreen(avgCol) + ", " + Util.getBlue(avgCol));
				}
			}
		} else {
			System.out.println("Tiles is empty");
		}
	}
	
	private void printControlAndComputedListOfTiles(TileImage control, ArrayList<TileImage> tilesInOrder) {
		Color cAvg = control.getAverageColor();
		System.out.println("Control: " + cAvg);
		for (TileImage t : tilesInOrder) {
			int diff = getColorDifference(cAvg, t.getAverageColor());
			System.out.println(t.getAverageColor() + "| DISTANCE: " + diff);
		}
	}
}
