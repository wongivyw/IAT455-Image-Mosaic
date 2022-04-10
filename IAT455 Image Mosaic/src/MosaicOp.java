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
	public BufferedImage computeAvgColorInImage(BufferedImage src, int tileSize, boolean includeBlack) {
		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

		int numTiles = src.getWidth()/tileSize;
		
		int posX, posY;
		for (int i = 0; i < numTiles; i++) { //y
			posY = tileSize*i; //top left corner of tile in y-axis
			for (int j = 0; j < numTiles; j++) { //x
				//for each tile
				posX = tileSize*j; //top left corner of the tile in x-axis
				int red = 0, green = 0, blue = 0;

				if (includeBlack) { // include black pixels in average calculation
					//find average color in tile and set the tile to that
					// for loop to iterate through each pixel in tile
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
				} else { // do not include black pixels in calculation
					
					int count = 0;
					for (int k = 0; k < tileSize; k++) { //y
						count=0;
						red=0;
						green=0;
						blue=0;
						for (int l = 0; l < tileSize; l++) {//x
							int rgba = src.getRGB(posX+l, posY+k);
							int r = Util.getRed(rgba);
							int g = Util.getGreen(rgba);
							int b = Util.getBlue(rgba);
							
							if (rgba != Color.black.getRGB() &&
									rgba != Color.black.getRGB() &&
									!(r==0 && g==0 && b==0)) {
								red += r;
								green += g;
								blue += b;
								count++;
							} //else {
//								System.out.println("Found black pixel!");
//							}
						}
					}
					red = Util.clip(red/count);
					green = Util.clip(green/count);
					blue = Util.clip(blue/count);	
				}
				
				int rgba = new Color(red, green, blue).getRGB();	
				// for loop to iterate through each pixel in tile
				for (int k = 0; k < tileSize; k++) { //y
					for (int l = 0; l < tileSize; l++) {//x
						result.setRGB(posX+l, posY+k, rgba); // use average color in tile
					}
				}
			}
		}
		return result;
	}
	
	public ArrayList<Color> calculateSrcColorAvgs(BufferedImage src, int tileSize) {
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
//			res.add(i.getAverageColorOfBkgRemoved());
		}
		return res;
	}
	
	// NOT TESTED
	public ArrayList<TileImage> computeBestMatches(ArrayList<TileImage> tiles, ArrayList<Color> srcColorAverages) {
		
		//sort tile image averages
//		ArrayList<TileImage> tilesSorted = sortByAscendingAvgTileColor(tiles);
		
		ArrayList<TileImage> finalTiles = new ArrayList<TileImage>();
		int rows = (int)Math.sqrt(srcColorAverages.size());
		int cols = rows;
//		for (Color avgC : srcColorAverages) {
			for (int i=0; i<srcColorAverages.size();i++) {
			//for each average in src tile...
			Color avgC=srcColorAverages.get(i);
			TileImage selectedTile;
			TileImage bestTile = findBestColorMatch(avgC, tiles)[0];
			TileImage second_bestTile = findBestColorMatch(avgC, tiles)[1];
			TileImage third_bestTile = findBestColorMatch(avgC, tiles)[2];
			
			boolean closest_equal_to_top=i>cols && finalTiles.get(i-1) !=null &&(finalTiles.get(i-cols) == bestTile);
			boolean closest_equal_to_left=i>0 && finalTiles.get(i-1) !=null &&(finalTiles.get(i-1) == bestTile);
//			boolean closest_equal_to_top_left=i>0 && j>0 && (reordered_tiles[i - 1][j-1] == closest);
//			
			
			boolean second_closest_equal_to_top=i>cols && finalTiles.get(i-1) !=null &&(finalTiles.get(i-cols) == second_bestTile);
			boolean second_closest_equal_to_left=i>0 && finalTiles.get(i-1) !=null &&(finalTiles.get(i-1) == second_bestTile);
			
			if(closest_equal_to_top || closest_equal_to_left ) {
//				if (second_closest_equal_to_top || second_closest_equal_to_left ||second_closest_equal_to_top_left ) {
//					reordered_tiles[i][j] = third_closest;
//				}
				if (second_closest_equal_to_top || second_closest_equal_to_left ) {
					selectedTile = third_bestTile;
				}
				else selectedTile = second_bestTile;;
				
			}
			else selectedTile = bestTile;
		
		
			if (selectedTile != null) finalTiles.add(selectedTile);
		}
		/*finalTiles is the reordered tiles 
		 * bestTile is the closest
		 * 
		 * */
			
//			System.out.println("Num final tiles " + finalTiles.size());

		return finalTiles;
	}
	
	//NOT TESTED
	// given a color and array of TileImages, returns the TileImage whose avg color is closes to the given color
	private TileImage[] findBestColorMatch(Color srcCol, ArrayList<TileImage> imgs) {
		if (imgs == null || imgs.isEmpty()) return null;
		
//		TileImage closestTile = imgs.get(0);
		TileImage closestTile, second_closestTile, third_closestTile;
		
		closestTile=second_closestTile= third_closestTile= imgs.get(0);
		int closestDiff, second_closestDiff, third_closestDiff;
		closestDiff= second_closestDiff= third_closestDiff= getColorDifference(srcCol, imgs.get(0).getAverageColor());
		
		for (TileImage tile : imgs) {
			Color tileCol = tile.getAverageColor();
			int currDiff = getColorDifference(srcCol, tileCol);
			if (currDiff <  closestDiff) {
				third_closestTile=second_closestTile;
				third_closestDiff = second_closestDiff;
				
				second_closestTile=closestTile;
				second_closestDiff = closestDiff;
				
				closestTile = tile;
				closestDiff = currDiff;
				
						
			}
		}
		TileImage[] closestTiles= {closestTile,second_closestTile,third_closestTile};
		return closestTiles;
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
//	Goal: to convert rgb to cie lab color space
	// NOT USED----
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
	
	
	public BufferedImage addGrid(BufferedImage img, int tileSize, int strokeWeight) {
		int w = img.getWidth();
		int h = img.getHeight();
		int c = Color.cyan.getRGB();
		int numTiles = h/tileSize;
		BufferedImage result = new BufferedImage(w, h, img.getType());
		
		//assign result to original image
		for (int g = 0; g < w; g++) {
			for (int f = 0; f < h; f++) {
				result.setRGB(g, f, img.getRGB(g, f));			
			}
		}

		// mark the outer border of image
		//top + bottom
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < strokeWeight; j++) {
				result.setRGB(i, j, c); //top
				result.setRGB(i, h-j-1, c);	//bottom			
			}
		}
		// left + right
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < strokeWeight; j++) {
				result.setRGB(j, i, c); //left
				result.setRGB(w-j-1, i, c);	//right		
			}
		}
		
		//mark the inner grid lines
		//horizontal grid lines
		for (int i = 1; i < numTiles; i++) { //y
			int posY = strokeWeight/2+(tileSize+strokeWeight/2)*i; //top left corner of tile in y-axis
			for (int j = 0; j < strokeWeight/2; j++) { //thickness of line
				for (int k = 0; k < w; k++) { //width of image
					result.setRGB(k, posY+j, c);
					result.setRGB(k, posY-j, c);
				}
			}
		}
			
		//vertical grid lines
		for (int i = 1; i < numTiles; i++) { //y
			int posX = strokeWeight/2+(tileSize+strokeWeight/2)*i; //top left corner of tile in x-axis
			for (int j = 0; j < strokeWeight/2; j++) { //thickness of line
				for (int k = 0; k < h; k++) { //height of image
					result.setRGB(posX+j, k, c);
					result.setRGB(posX-j, k, c);
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
