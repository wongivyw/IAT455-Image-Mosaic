/*
 * Structure of of elements such as paint component, graphics2D, 
 * mouse interactions adapted from Ivy's IAT 265 Assignment 4
 */

package main;

 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import src.Animation;
import src.MosaicOp;
import src.MyColors;
import src.Screen;
import src.TileImage;
import src.Util;


public class MosaicPanel extends JPanel implements ActionListener {
	public final static int PAN_W = 1133;//1400;
	public final static int PAN_H = 744;//750;
	public final static int NUM_SRCIMG = 4;
	
	public final static int SCALED_IMAGE_SIZE = 1280;
	public final static int SCALED_TILE_IMAGE_SIZE = 2;
	public final static int SCALED_TILE_IMAGE_SIZE_S = 2;
	public final static int SCALED_TILE_IMAGE_SIZE_M = 20;
	public final static int SCALED_TILE_IMAGE_SIZE_L = 64;
	public final static int SCALED_TILE_IMAGE_SIZE_XL = 128;
	public final static int SCALED_UI_IMAGE_SIZE = 300;
	public final static int TIME_BETWEEN_FRAMES = 5; // 5/30 of a second

	// New UI
	private static final int TOTAL_SCREENS = 11;
	private static final int FIRST_SCREEN = 0;
	
	private static final int INTRO1 = 0;
	private static final int INTRO2 = 1;
	private static final int INTRO3 = 2;
	private static final int MAIN1 = 3;
	private static final int MAIN2 = 4;
	private static final int MAIN3 = 5;
	private static final int MAIN4 = 6;
	private static final int MAIN5 = 7;
	private static final int MAIN6 = 8;
	private static final int MAIN7 = 9;
	private static final int MAIN8 = 10;
	
	//grid sizes for user to choose from
	private static final int GRID_S = 2;	
	private static final int GRID_M = 20;	
	private static final int GRID_L = 64;	
	private static final int GRID_XL = 128;	
	
	private static final String NEXT_BUTTON_NAME = "next";	
	private static final String GRID_S_BUTTON_NAME = "small";	
	private static final String GRID_M_BUTTON_NAME = "medium";	
	private static final String GRID_L_BUTTON_NAME = "large";	
	private static final String GRID_XL_BUTTON_NAME = "extra-large";

	public int page;
	public BufferedImage solidColor, mickeyMinnie, arcDeTriomphe, parrot, stream;
	ArrayList<BufferedImage> srcImgs = new ArrayList<BufferedImage>();
	ArrayList<TileImage> tileImgs = new ArrayList<TileImage>(); // tile images used for mosaic

	ArrayList<Screen> screens = new ArrayList<Screen>();
	int currentScreen;
	
	private Timer timer;
	int animationTimer; //every 1 second = 30 frames
	Animation animation;
	
	BufferedImage sourceImage;
	TileImage tileImage;
	MosaicOp operations;
	int userChosenTileSize;
	BufferedImage finalMosaic_S, finalMosaic_M, finalMosaic_L, finalMosaic_XL, finalMosaic;
		
	public MosaicPanel() {
		setPreferredSize(new Dimension(PAN_W, PAN_H));

		// SOURCE for mouse events taken from IAT 265 cafe project by Ivy
		MyMouseListener ml = new MyMouseListener(); //mouse clicked
		addMouseListener(ml);
		MyMouseMotionListener mml = new MyMouseMotionListener(); //mouse dragged
		addMouseMotionListener(mml);
		
		if (loadScreenImages()) { //new UI
			currentScreen = INTRO1;
			setButtons();
		}//if
		
		userChosenTileSize = GRID_M; //will change based on what the user chooses, this is the default
		if (loadSrcImages()) sourceImage = mickeyMinnie;
//		loadTileImages(userChosenTileSize);
		operations = new MosaicOp(sourceImage, tileImgs, userChosenTileSize);
		loadTileImages(GRID_S);
		finalMosaic_S = new MosaicOp(sourceImage, tileImgs, 2).getMosaicImage();
		loadTileImages(GRID_M);
		finalMosaic_M = new MosaicOp(sourceImage, tileImgs, 20).getMosaicImage();
		loadTileImages(GRID_L);
		finalMosaic_L = new MosaicOp(sourceImage, tileImgs, 64).getMosaicImage();
		loadTileImages(GRID_XL);
		finalMosaic_XL = new MosaicOp(sourceImage, tileImgs, 128).getMosaicImage();
		finalMosaic = finalMosaic_M;
		//animation of pixelation (reducing to one color)
		timer = new Timer(30, this);
		timer.start();
		animationTimer = TIME_BETWEEN_FRAMES;
	}
	
	//methods to load images from file. returns false if error occurs
	public boolean loadScreenImages() {
		// screens are split into intro and main so if we need to add more screens,
		// we don't have to rename all of them.
		String format = ".jpg";
		String folder = "UI_Screens/";
		String intro = "intro";
		String main = "main";
		
		int numIntros = 3;
		int numMains = 8;
		
		for (int i = 0; i < numIntros; i++) {
			String inputPath = folder.concat(intro).concat(Integer.toString(i+1)).concat(format);
			Screen screen = new Screen(inputPath, PAN_W, PAN_H);
			if (screen.isSuccessful()) screens.add(screen);
		}
		
		for (int i = 0; i < numMains; i++) {
			String inputPath = folder.concat(main).concat(Integer.toString(i+1)).concat(format);
			Screen screen = new Screen(inputPath, PAN_W, PAN_H);
			if (screen.isSuccessful()) screens.add(screen);
		}

		if (screens.size() == (numIntros + numMains)) return true;
		System.out.println("screen images cannot be loaded");
		return false;
		
	}
	
	public boolean loadSrcImages() {
		//fail indicators (if images cannot be loaded)
		boolean imagesLoaded = false;
		solidColor = null;
		mickeyMinnie = null;
		arcDeTriomphe = null;
		parrot = null;
		stream = null;
		
		try {
			Util.resize("mickey-minnie.jpg", "mickey-minnie-scaled.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			mickeyMinnie = ImageIO.read(new File("mickey-minnie-scaled.jpg")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: mickey-minnie.jpg");
		}
			
		try {
			Util.resize("arc-de-triomphe.jpg", "arc-de-triomphe-scaled.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			arcDeTriomphe = ImageIO.read(new File("arc-de-triomphe-scaled.jpg")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: arc-de-triomphe.jpg");
		}
			
		try {
			Util.resize("parrot.jpg", "parrot-scaled.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			parrot = ImageIO.read(new File("parrot-scaled.jpg")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: parrot.jpg");
		}
		
		try {	
			Util.resize("stream.jpg", "stream-scaled.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			stream = ImageIO.read(new File("stream-scaled.jpg")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: stream.jpg");
		}
		
		if (mickeyMinnie != null && 
				arcDeTriomphe != null && 
				parrot != null && 
				stream != null) {
			imagesLoaded = true;
			srcImgs.add(mickeyMinnie);
			srcImgs.add(arcDeTriomphe);
			srcImgs.add(parrot);
			srcImgs.add(stream);
		}
		if (!imagesLoaded) System.out.println("Source images not loaded properly.");

		return imagesLoaded;
	}
	
	public boolean loadTileImages(int size) {	
		tileImgs = new ArrayList<TileImage>();
		String format = ".jpg";
		String folder = "tiles/";
		String name = "tile-";
		
		int numImages = 47; // 0 to 46
		
		for (int i = 0; i < numImages; i++) {
			String inputPath = folder.concat(name).concat(Integer.toString(i)).concat(format);
			String outputPath = folder.concat(name).concat(Integer.toString(i)).concat("-scaled").concat(format);
//			System.out.println(inputPath);
//			System.out.println(outputPath);
			TileImage tile = new TileImage(inputPath, outputPath, size, size);
			if (tile.getAverageColor() != null) tileImgs.add(tile);
		}
		
		//load larger tile image to be displayed to user
		String pathIn = folder.concat(name).concat("41").concat(format);
		String pathOut = folder.concat(name).concat("41").concat("-scaled2").concat(format);
		tileImage = new TileImage(pathIn, pathOut, SCALED_UI_IMAGE_SIZE, SCALED_UI_IMAGE_SIZE);


		if (tileImgs.size() == numImages) return true;
		System.out.println("Tile images not loaded properly.");
		return false;
			
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw screen image
		if (screens != null && !screens.isEmpty()) {
			screens.get(currentScreen).draw(g2);
		}
		
		/*
		 * main1 = tile image							--ADDED
		 * main2 = tile image with removed background	--ADDED
		 * main3 = animation							--ADDED
		 * main4 = source image							--ADDED
		 * main5 = source image with grid				--ADDED
		 * main6 = source avg colors + tile avg colors	--ADDED
		 * main7 = source image + reordered tiles		--ADDED
		 * main8 = final mosaic image					--ADDED
		 */
		// add elements onto screen to show process
		switch (currentScreen) {
		case INTRO1:
			break;
			
		case INTRO2:
			break;
			
		case INTRO3: //draw first 16 tiles images in 4x4 grid
			drawFromTileArray(g2, tileImgs, 16, 4, 65, 65, 750, 280);
			
			break;
			
		case MAIN1: //tile image
			if (tileImage != null) tileImage.draw(g2, 725, 233, 1);
			break;
		
		case MAIN2: //tile image with removed background
			if (tileImage != null) tileImage.drawBackgroundRemoved(g2, 725, 233, 1);
			break;
			
		case MAIN3: //animation
			animation.draw(g2);
			break;
			
		case MAIN4: //source image
			if (sourceImage != null) g2.drawImage(sourceImage, 725, 233, SCALED_UI_IMAGE_SIZE, SCALED_UI_IMAGE_SIZE, null);
			BufferedImage grid;
			Color c = new Color(166, 66, 66); //button color red
			grid = getGridImage(sourceImage, c);
			g2.drawImage(grid, 725, 233, SCALED_UI_IMAGE_SIZE, SCALED_UI_IMAGE_SIZE, null);
			break;
		
		case MAIN5: //source image with grid
			grid = getGridImage(sourceImage, Color.black);
			g2.drawImage(grid, 725, 233, SCALED_UI_IMAGE_SIZE, SCALED_UI_IMAGE_SIZE, null);
			break;
			
		case MAIN6: //source avg colors + tile avg colors
			if (sourceImage != null && tileImage != null) {
				//draw source image
				g2.drawImage(sourceImage, 700, 160, SCALED_UI_IMAGE_SIZE/2, SCALED_UI_IMAGE_SIZE/2, null);
				drawFromTileArray(g2, tileImgs, 16, 4, 35, 35, 700, 350);

				// draw avg color image of source image
				BufferedImage srcImg_avgColors = operations.computeAvgColorInImage(sourceImage, userChosenTileSize, true);
				grid = getGridImage(srcImg_avgColors, Color.black);
				g2.drawImage(grid, 875, 160, SCALED_UI_IMAGE_SIZE/2, SCALED_UI_IMAGE_SIZE/2, null);

				
				// draw avg color image of 4x4 tile images
				BufferedImage bkgRm = tileImage.getBackgroundRemovedImage();
				BufferedImage tileImg_avgColors = operations.computeAvgColorInImage(bkgRm, bkgRm.getHeight(), false);
				drawAvgColFromTileArray(g2, tileImgs, 16, 4, 35, 35, 875, 350);
				
			}
			break;
			
		case MAIN7: //source image + reordered tiles
			//draw source image
			g2.drawImage(sourceImage, 650, 250, SCALED_UI_IMAGE_SIZE/4*3, SCALED_UI_IMAGE_SIZE/4*3, null);
			
			//draw reordered tiles
			ArrayList<Color> srcColorAvgs = operations.calculateSrcColorAvgs(sourceImage, userChosenTileSize);
			ArrayList<TileImage> orderedTiles = operations.computeBestMatches(tileImgs, srcColorAvgs);
			drawFromTileArray(g2, orderedTiles, 20, 20, 30, 30, 450, 500);
			break;
		
		case MAIN8: //final mosaic image
//			operations = new MosaicOp(sourceImage, tileImgs, userChosenTileSize);
//			BufferedImage mosaicImage = operations.getMosaicImage();
			BufferedImage mosaicImage = getFinalMosaic();
			if (mosaicImage != null) g2.drawImage(mosaicImage, 725, 233, SCALED_UI_IMAGE_SIZE, SCALED_UI_IMAGE_SIZE, null);
			break;
			
		default:
			break;
		}
	}
	
	private BufferedImage getFinalMosaic() {
		switch (userChosenTileSize) {
		case GRID_S:
			return finalMosaic_S;
		case GRID_M:
			return finalMosaic_M;
		case GRID_L:
			return finalMosaic_L;
		case GRID_XL:
			return finalMosaic_XL;
		default:
			return finalMosaic_M;
		}
	}
	
	private BufferedImage getGridImage(BufferedImage original, Color c) {
		switch (userChosenTileSize) {
		case GRID_S:
			return operations.smallGrid(original, c); //300px total, small grid
		case GRID_M:
			return operations.addGrid(original, 50, 7, c); //M grid
		case GRID_L:
			return operations.addGrid(original, 100, 10, c); //L grid
		case GRID_XL:
			return operations.addGrid(original, 150, 15, c); //XL grid
		default:
			return operations.addGrid(original, 50, 7, c); //M grid
		}
	}
	
	private void drawFromTileArray(Graphics2D g2, ArrayList<TileImage> orderedTiles, int numTiles, int numCols, int tileW,
		int tileH, int xPos, int yPos) {
		for (int i = 0; i < numTiles; i++) {
			int col = i%numCols;
			int row = i/numCols;
			BufferedImage ti = orderedTiles.get(i).getFullSizeImage();
			g2.drawImage(ti, xPos+col*tileW, yPos+row*tileH, tileW, tileH, null);
	
		}
	}
	
	private void drawAvgColFromTileArray(Graphics2D g2, ArrayList<TileImage> orderedTiles, int numTiles, int numCols, int tileW,
			int tileH, int xPos, int yPos) {
			for (int i = 0; i < numTiles; i++) {
				int col = i%numCols;
				int row = i/numCols;
				BufferedImage ti = orderedTiles.get(i).getAverageColorImage();
				g2.drawImage(ti, xPos+col*tileW, yPos+row*tileH, tileW, tileH, null);
			}
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (currentScreen == MAIN3) {
			if (animationTimer > 0) {
				animationTimer--;
			}
			else {
				animationTimer = TIME_BETWEEN_FRAMES;
				animation.changeFrame();
			}
		}
		repaint();
	}
	
	// functions for navigation through program
	private void addAnimation() {
		if (tileImage == null) System.out.println("No source image selected");
		else {
		ArrayList<Integer> factors = new ArrayList<Integer>();
		
		// compute the factors based on the size of the image
		int imageSize = tileImage.getImage().getWidth();
		for (int i = 1; i < imageSize; i++) {
			if (imageSize%i == 0) factors.add(i);
		}
			animation = new Animation(tileImage.getBackgroundRemovedImage(), factors, 725, 233, 1);
		}
	}
	private void nextScreen() {
		if (currentScreen >= TOTAL_SCREENS - 1) {
			currentScreen = FIRST_SCREEN;
		} else {
			currentScreen++;
		}
	}
	private void setButtons() {
//		addButtonArea(xpos, ypos, width, height)
		int xPos = 141;
		screens.get(INTRO1).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(INTRO2).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(INTRO3).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN1).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN2).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN3).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN4).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN5).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN6).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN7).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		screens.get(MAIN8).addButtonArea(xPos, 452, 123, 43, NEXT_BUTTON_NAME);
		
		//buttons for MAIN4 -- user choses tile size + display grid
		int yPos = 200;
		int height = 30;
		screens.get(MAIN4).addButtonArea(735, yPos, 52, height, GRID_S_BUTTON_NAME);
		screens.get(MAIN4).addButtonArea(790, yPos, 76, height, GRID_M_BUTTON_NAME);
		screens.get(MAIN4).addButtonArea(870, yPos, 60, height, GRID_L_BUTTON_NAME);
		screens.get(MAIN4).addButtonArea(933, yPos, 90, height, GRID_XL_BUTTON_NAME);
		
		//reset buttons
		

	}
	
	// SOURCE for mouse events taken from IAT 265 cafe project by Ivy
	public class MyMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();

			Screen screen = screens.get(currentScreen);
			// go to next screen if button on current screen is clicked
			if (screen.isButtonClicked(eX, eY, "next")) nextScreen();
			if (currentScreen == MAIN3) addAnimation();
			if (currentScreen == MAIN4) {
				// check for clicks on tile size
				if (screen.isButtonClicked(eX, eY, GRID_S_BUTTON_NAME)) userChosenTileSize = GRID_S;
				if (screen.isButtonClicked(eX, eY, GRID_M_BUTTON_NAME)) userChosenTileSize = GRID_M;
				if (screen.isButtonClicked(eX, eY, GRID_L_BUTTON_NAME)) userChosenTileSize = GRID_L;
				if (screen.isButtonClicked(eX, eY, GRID_XL_BUTTON_NAME)) userChosenTileSize = GRID_XL;
			}
			
			//handles double click events
			if (e.getClickCount() == 2) { 
			//double clicked
			}
			repaint();
		}
		
		public void mousePressed(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();
			repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();
			repaint();
		}
	}
	
	public class MyMouseMotionListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			//to implement dragging on an item, modify mousePressed() and mouseDragged()
			int eX = e.getX();
			int eY = e.getY();
//			System.out.println("mouse dragged");
			repaint();
		}
	}

}
