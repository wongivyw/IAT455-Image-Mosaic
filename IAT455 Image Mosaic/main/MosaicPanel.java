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
import src.MosaicUI;
import src.MosaicUI_Edit;
import src.MyColors;
import src.TileImage;
import src.TitleUI;
import src.Util;


public class MosaicPanel extends JPanel implements ActionListener {

//	private BufferedImage image;
	private final int textHeight = 30;

	public final static int PAN_W = 1133;//1400;
	public final static int PAN_H = 744;//750;
	public final static int NUM_SRCIMG = 4;
	public final static int FONT_SIZE_HEADER = 18;
	public final static int FONT_SIZE_BODY = 16;

	public final static int TITLE_UI = 0;
	public final static int MOSAIC_UI = 1;
	public final static int MOSAIC_UI_EDIT = 2;
	
	public final static int SCALED_IMAGE_SIZE = 1280;
	public final static int SCALED_TILE_IMAGE_SIZE = 40;
//	 * 1280 is divisible by are 1, 2, 4, 5, 8, 10, 16, 20, 32, 40, 64, 80, 128, 160, 256, 320, 640, and 1280.

	public final static int TIME_BETWEEN_FRAMES = 5; // 5/30 of a second
	
	private BufferedImage srcImage, intro;
	public int page;
	public BufferedImage solidColor, mickeyMinnie, arcDeTriomphe, parrot, stream;

	//interactive elements (the buttons)
	Rectangle2D.Double beginButton, chooseRandomButton, imgArea1, imgArea2, imgArea3, imgArea4;
	Rectangle2D.Double editButton, createAnotherButton, saveButton, editCreateAnotherButton;
	ArrayList<Rectangle2D.Double> filterOptions = new ArrayList<Rectangle2D.Double>();
	Rectangle2D.Double filterOption1, filterOption2, filterOption3, filterOption4, filterOption5, filterOption6;
	
	TitleUI introPage;
	MosaicUI mosaicPage;
	MosaicUI_Edit mosaicEditPage;
	ArrayList<BufferedImage> srcImgs = new ArrayList<BufferedImage>();
	ArrayList<TileImage> tileImgs = new ArrayList<TileImage>(); // tile images used for mosaic

	private Timer timer;
	int animationTimer; //every 1 second = 30 frames
	Animation animation;
	
	//status of images loaded
	boolean imagesLoaded;
		
	public MosaicPanel() {
		//UNCOMMENT TO DRAW APPLICATION (COMMENTED OUT DURING TESTING)
		setPreferredSize(new Dimension(PAN_W, PAN_H));
//		setBackground(MyColors.red_700);

		// SOURCE for mouse events taken from IAT 265 cafe project by Ivy
		MyMouseListener ml = new MyMouseListener(); //mouse clicked
		addMouseListener(ml);
		MyMouseMotionListener mml = new MyMouseMotionListener(); //mouse dragged
		addMouseMotionListener(mml);
		

//		loadScreenImages(); //new UI

		if (loadSrcImages() && loadTileImages()) {
			
			//titleUI
			page = TITLE_UI;
			introPage = new TitleUI(PAN_W, PAN_H, srcImgs);
			beginButton = introPage.getBeginButton();
			chooseRandomButton = introPage.getChooseRandomButton();
			imgArea1 = introPage.getImgArea1();
			imgArea2 = introPage.getImgArea2();
			imgArea3 = introPage.getImgArea3();
			imgArea4 = introPage.getImgArea4();
			
			//mosaicUI
			srcImage = introPage.getSelectedImage();
			mosaicPage = new MosaicUI(PAN_W, PAN_H, srcImage, tileImgs, SCALED_TILE_IMAGE_SIZE);
			editButton = mosaicPage.getEditButton();
			createAnotherButton = mosaicPage.getCreateAnotherButton();
			
			//mosaicUI_Edit
			mosaicEditPage = new MosaicUI_Edit(PAN_W, PAN_H, mosaicPage.getMosaicImage());
			saveButton = mosaicEditPage.getSaveButton();
			editCreateAnotherButton = mosaicEditPage.getCreateAnotherButton();
			filterOptions = mosaicEditPage.getFilterOptions();	
			
		}//if
		
		//animation of pixelation (reducing to one color)
		timer = new Timer(30, this);
		timer.start();
		animationTimer = TIME_BETWEEN_FRAMES;
	}
	
	public void loadScreenImages() {
		try {
			intro = ImageIO.read(new File("intro.png")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: intro.png");
		}
	}
	
	public boolean loadSrcImages() {
		//fail indicators (if images cannot be loaded)
		imagesLoaded = false;
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
	
	public boolean loadTileImages() {
//		String imgPath1 = "tiles/1024x1024-aero-blue-solid-color-background.jpg", outputPath1 = "tiles/1024x1024-aero-blue-solid-color-background-scaled.jpg";
//		String imgPath2 = "tiles/1024x1024-aero-solid-color-background.jpg", outputPath2 = "tiles/1024x1024-aero-solid-color-background-scaled.jpg";
//		String imgPath3 = "tiles/1024x1024-african-violet-solid-color-background.jpg", outputPath3 = "tiles/1024x1024-african-violet-solid-color-background-scaled.jpg";
//		String imgPath4 = "tiles/1024x1024-air-force-blue-solid-color-background.jpg", outputPath4 = "tiles/1024x1024-air-force-blue-solid-color-background-scaled.jpg";
//		String imgPath5 = "tiles/1024x1024-air-force-dark-blue-solid-color-background.jpg", outputPath5 = "tiles/1024x1024-air-force-dark-blue-solid-color-background-scaled.jpg";
//		String imgPath6 = "tiles/1024x1024-air-superiority-blue-solid-color-background.jpg", outputPath6 = "tiles/1024x1024-air-superiority-blue-solid-color-background-scaled.jpg";
//		String imgPath7 = "tiles/1024x1024-alabama-crimson-solid-color-background.jpg", outputPath7 = "tiles/1024x1024-alabama-crimson-solid-color-background-scaled.jpg";
//		String imgPath8 = "tiles/1024x1024-alice-blue-solid-color-background.jpg", outputPath8 = "tiles/1024x1024-alice-blue-solid-color-background-scaled.jpg";
		
		String imgPath1 = "originalTiles/t1.jpg", outputPath1 = "originalTiles/t1-scaled.jpg";
		String imgPath2 = "originalTiles/t2.jpg", outputPath2 = "originalTiles/t2-scaled.jpg";
		String imgPath3 = "originalTiles/t3.jpg", outputPath3 = "originalTiles/t3-scaled.jpg";
		String imgPath4 = "originalTiles/t4.jpg", outputPath4 = "originalTiles/t4-scaled.jpg";
		String imgPath5 = "originalTiles/t5.jpg", outputPath5 = "originalTiles/t5-scaled.jpg";
		String imgPath6 = "originalTiles/t6.jpg", outputPath6 = "originalTiles/t6-scaled.jpg";
		String imgPath7 = "originalTiles/t7.jpg", outputPath7 = "originalTiles/t7-scaled.jpg";
		String imgPath8 = "originalTiles/t8.jpg", outputPath8 = "originalTiles/t8-scaled.jpg";
		String imgPath9 = "originalTiles/t9.jpg", outputPath9 = "originalTiles/t9-scaled.jpg";
		String imgPath10 = "originalTiles/t10.jpg", outputPath10 = "originalTiles/t10-scaled.jpg";

		String imgPath11 = "originalTiles/t11.jpg", outputPath11 = "originalTiles/t11-scaled.jpg";
		String imgPath12 = "originalTiles/t12.jpg", outputPath12 = "originalTiles/t12-scaled.jpg";
		String imgPath13 = "originalTiles/t13.jpg", outputPath13 = "originalTiles/t13-scaled.jpg";
		String imgPath14 = "originalTiles/t14.jpg", outputPath14 = "originalTiles/t14-scaled.jpg";
		String imgPath15 = "originalTiles/t15.jpg", outputPath15 = "originalTiles/t15-scaled.jpg";
		String imgPath16 = "originalTiles/t16.jpg", outputPath16 = "originalTiles/t16-scaled.jpg";
		String imgPath17 = "originalTiles/t17.jpg", outputPath17 = "originalTiles/t17-scaled.jpg";
		String imgPath18 = "originalTiles/t18.jpg", outputPath18 = "originalTiles/t18-scaled.jpg";
		String imgPath19 = "originalTiles/t19.jpg", outputPath19 = "originalTiles/t19-scaled.jpg";
		String imgPath20 = "originalTiles/t20.jpg", outputPath20 = "originalTiles/t20-scaled.jpg";

		String imgPath21 = "originalTiles/t21.jpg", outputPath21 = "originalTiles/t21-scaled.jpg";
		String imgPath22 = "originalTiles/t22.jpg", outputPath22 = "originalTiles/t22-scaled.jpg";
		String imgPath23 = "originalTiles/t23.jpg", outputPath23 = "originalTiles/t23-scaled.jpg";
		String imgPath24 = "originalTiles/t24.jpg", outputPath24 = "originalTiles/t24-scaled.jpg";
		String imgPath25 = "originalTiles/t25.jpg", outputPath25 = "originalTiles/t25-scaled.jpg";
		String imgPath26 = "originalTiles/t26.jpg", outputPath26 = "originalTiles/t26-scaled.jpg";
		
		TileImage tile1 = new TileImage(imgPath1, outputPath1, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile2 = new TileImage(imgPath2, outputPath2, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile3 = new TileImage(imgPath3, outputPath3, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile4 = new TileImage(imgPath4, outputPath4, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile5 = new TileImage(imgPath5, outputPath5, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile6 = new TileImage(imgPath6, outputPath6, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile7 = new TileImage(imgPath7, outputPath7, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile8 = new TileImage(imgPath8, outputPath8, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile9 = new TileImage(imgPath9, outputPath9, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile10 = new TileImage(imgPath10, outputPath10, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		
		TileImage tile11 = new TileImage(imgPath11, outputPath11, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile12 = new TileImage(imgPath12, outputPath12, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile13 = new TileImage(imgPath13, outputPath13, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile14 = new TileImage(imgPath14, outputPath14, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile15 = new TileImage(imgPath15, outputPath15, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile16 = new TileImage(imgPath16, outputPath16, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile17 = new TileImage(imgPath17, outputPath17, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile18 = new TileImage(imgPath18, outputPath18, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile19 = new TileImage(imgPath19, outputPath19, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile20 = new TileImage(imgPath20, outputPath20, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		
		TileImage tile21 = new TileImage(imgPath21, outputPath21, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile22 = new TileImage(imgPath22, outputPath22, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile23 = new TileImage(imgPath23, outputPath23, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile24 = new TileImage(imgPath24, outputPath24, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile25 = new TileImage(imgPath25, outputPath25, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile26 = new TileImage(imgPath26, outputPath26, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);

		if (tile1.getAverageColor() != null) tileImgs.add(tile1);
		if (tile2.getAverageColor() != null) tileImgs.add(tile2);
		if (tile3.getAverageColor() != null) tileImgs.add(tile3);
		if (tile4.getAverageColor() != null) tileImgs.add(tile4);
		if (tile5.getAverageColor() != null) tileImgs.add(tile5);
		if (tile6.getAverageColor() != null) tileImgs.add(tile6);
		if (tile7.getAverageColor() != null) tileImgs.add(tile7);
		if (tile8.getAverageColor() != null) tileImgs.add(tile8);
		if (tile9.getAverageColor() != null) tileImgs.add(tile9);
		if (tile10.getAverageColor() != null) tileImgs.add(tile10);

		if (tile11.getAverageColor() != null) tileImgs.add(tile11);
		if (tile12.getAverageColor() != null) tileImgs.add(tile12);
		if (tile13.getAverageColor() != null) tileImgs.add(tile13);
		if (tile14.getAverageColor() != null) tileImgs.add(tile14);
		if (tile15.getAverageColor() != null) tileImgs.add(tile15);
		if (tile16.getAverageColor() != null) tileImgs.add(tile16);
		if (tile17.getAverageColor() != null) tileImgs.add(tile17);
		if (tile18.getAverageColor() != null) tileImgs.add(tile18);
		if (tile19.getAverageColor() != null) tileImgs.add(tile19);
		if (tile20.getAverageColor() != null) tileImgs.add(tile20);
		
		if (tile21.getAverageColor() != null) tileImgs.add(tile21);
		if (tile22.getAverageColor() != null) tileImgs.add(tile22);
		if (tile23.getAverageColor() != null) tileImgs.add(tile23);
		if (tile24.getAverageColor() != null) tileImgs.add(tile24);
		if (tile25.getAverageColor() != null) tileImgs.add(tile25);
		if (tile26.getAverageColor() != null) tileImgs.add(tile26);

		int numImages = 26;

		if (tileImgs.size() == numImages) return true;
		System.out.println("Tile images not loaded properly.");
		return false;
		
			
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (page == TITLE_UI) introPage.draw(g2);
		else if (page == MOSAIC_UI) {
			srcImage = introPage.getSelectedImage();
			mosaicPage = new MosaicUI(PAN_W, PAN_H, srcImage, tileImgs, SCALED_TILE_IMAGE_SIZE);
			mosaicPage.draw(g2);
			
			animation.draw(g2);
		}
		else if (page == MOSAIC_UI_EDIT) {
			mosaicEditPage = new MosaicUI_Edit(PAN_W, PAN_H, mosaicPage.getMosaicImage());
			mosaicEditPage.draw(g2);
		}
		
		// New UI Test1
//		g2.drawImage(intro, 0, 0, 1133, 744, null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (page == MOSAIC_UI) {
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
		
	public int getWidth(){
		if (imagesLoaded) return srcImgs.get(0).getWidth();
		return -1;
	}

	public int getHeight(){
		if(imagesLoaded) return srcImgs.get(0).getHeight() + textHeight;
		return -1;
	}
	
	private void addAnimation() {
		ArrayList<Integer> factors = new ArrayList<Integer>();
//		1, 2, 4, 5, 8, 10, 16, 20, 32, 40, 64, 80, 128, 160, 256, 320, 640, and 1280 are factors of 1280
		factors.add(1);
		factors.add(5);
		factors.add(16);
		factors.add(32);
		factors.add(64);
		factors.add(128);
		factors.add(256);
		factors.add(320);
		factors.add(640);
		factors.add(1280);
		animation = new Animation(introPage.getSelectedImage(), factors, 0, 0, 0.25);
	}
	
// SOURCE for mouse events taken from IAT 265 cafe project by Ivy
public class MyMouseListener extends MouseAdapter {
		
		public void mouseClicked(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();
//			System.out.println("mouse clicked");
			if (page == TITLE_UI) {
				if (beginButton.contains(eX, eY)) {
//					System.out.println("begin button clicked");
					page = MOSAIC_UI;
					addAnimation();
				} else if (chooseRandomButton.contains(eX, eY)) {
//					System.out.println("choose random button clicked");
				} else if (imgArea1.contains(eX, eY)) {
//					System.out.println("image area 1 clicked");
					introPage.setSelectedImage(1);
				} else if (imgArea2.contains(eX, eY)) {
//					System.out.println("image area 2 clicked");
					introPage.setSelectedImage(2);
				} else if (imgArea3.contains(eX, eY)) {
//					System.out.println("image area 3 clicked");
					introPage.setSelectedImage(3);
				} else if (imgArea4.contains(eX, eY)) {
//					System.out.println("image area 4 clicked");
					introPage.setSelectedImage(4);
				} 
			} else if ((page == MOSAIC_UI)) {
				if (editButton.contains(eX, eY)) {
//					System.out.println("edit button clicked");
					page = MOSAIC_UI_EDIT;
				} else if (createAnotherButton.contains(eX, eY)) {
//					System.out.println("create another button clicked");
					page = TITLE_UI;
				}
			} else if (page == MOSAIC_UI_EDIT) {
				if (saveButton.contains(eX, eY)) {
			
				} else if (editCreateAnotherButton.contains(eX, eY)) {
//					System.out.println("create another button clicked");
					page = TITLE_UI;
				} else if (editCreateAnotherButton.contains(eX, eY)) {
//					System.out.println("create another button clicked");
					
				} else {
					for (int i = 0; i < filterOptions.size(); i++) {
						if (filterOptions.get(i).contains(eX, eY)) {
							mosaicEditPage.setFilterOption(i);
//							System.out.println("Filter option: " + i + " clicked");
						}
					}
				}
			}
			
			//handles double click events
			if (e.getClickCount() == 2) { 
			//double clicked
//			System.out.println("mouse double clicked");

			}
			repaint();
		}
		
		public void mousePressed(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();
//			System.out.println("mouse pressed");

			repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();
//			System.out.println("mouse released");

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
