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

import src.MosaicUI;
import src.MosaicUI_Edit;
import src.MyColors;
import src.TileImage;
import src.TitleUI;
import src.Util;


public class MosaicPanel extends JPanel implements ActionListener {

//	private BufferedImage image;
	private final int textHeight = 30;

	public final static int PAN_W = 1400;
	public final static int PAN_H = 750;
	public final static int NUM_SRCIMG = 4;
	public final static int FONT_SIZE_HEADER = 18;
	public final static int FONT_SIZE_BODY = 16;

	public final static int TITLE_UI = 0;
	public final static int MOSAIC_UI = 1;
	public final static int MOSAIC_UI_EDIT = 2;
	
	public final static int SCALED_IMAGE_SIZE = 1280;
	public final static int SCALED_TILE_IMAGE_SIZE = 160;
//	 * 1280 is divisible by are 1, 2, 4, 5, 8, 10, 16, 20, 32, 40, 64, 80, 128, 160, 256, 320, 640, and 1280.

	
	
	private BufferedImage srcImage;
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

	
	//status of images loaded
	boolean imagesLoaded;
		
	public MosaicPanel() {
		//UNCOMMENT TO DRAW APPLICATION (COMMENTED OUT DURING TESTING)
		setPreferredSize(new Dimension(PAN_W, PAN_H));
		setBackground(MyColors.red_700);

		// SOURCE for mouse events taken from IAT 265 cafe project by Ivy
		MyMouseListener ml = new MyMouseListener(); //mouse clicked
		addMouseListener(ml);
		MyMouseMotionListener mml = new MyMouseMotionListener(); //mouse dragged
		addMouseMotionListener(mml);

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
			Util.resize("mickey-minnie.jpg", "mickey-minnie.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			mickeyMinnie = ImageIO.read(new File("mickey-minnie.jpg")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: mickey-minnie.jpg");
		}
			
		try {
			Util.resize("arc-de-triomphe.jpg", "arc-de-triomphe.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			arcDeTriomphe = ImageIO.read(new File("arc-de-triomphe.jpg")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: arc-de-triomphe.jpg");
		}
			
		try {
			Util.resize("parrot.jpg", "parrot.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			parrot = ImageIO.read(new File("parrot.jpg")); 
		} catch (Exception e) {
			imagesLoaded = false;
			System.out.println("Cannot load the provided image: parrot.jpg");
		}
		
		try {	
			Util.resize("stream.jpg", "stream.jpg", SCALED_IMAGE_SIZE, SCALED_IMAGE_SIZE);
			stream = ImageIO.read(new File("stream.jpg")); 
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
		return imagesLoaded;
	}
	
	public boolean loadTileImages() {
		String imgPath1 = "tiles/1024x1024-aero-blue-solid-color-background.jpg";
		String imgPath2 = "tiles/1024x1024-aero-solid-color-background.jpg";
		String imgPath3 = "tiles/1024x1024-african-violet-solid-color-background.jpg";
		String imgPath4 = "tiles/1024x1024-air-force-blue-solid-color-background.jpg";
		String imgPath5 = "tiles/1024x1024-air-force-dark-blue-solid-color-background.jpg";
		String imgPath6 = "tiles/1024x1024-air-superiority-blue-solid-color-background.jpg";
		String imgPath7 = "tiles/1024x1024-alabama-crimson-solid-color-background.jpg";
		String imgPath8 = "tiles/1024x1024-alice-blue-solid-color-background.jpg";
		
		TileImage tile1 = new TileImage(imgPath1, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile2 = new TileImage(imgPath2, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile3 = new TileImage(imgPath3, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile4 = new TileImage(imgPath4, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile5 = new TileImage(imgPath5, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile6 = new TileImage(imgPath6, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile7 = new TileImage(imgPath7, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		TileImage tile8 = new TileImage(imgPath8, SCALED_TILE_IMAGE_SIZE, SCALED_TILE_IMAGE_SIZE);
		
		if (tile1.getAverageColor() != null) {
			tileImgs.add(tile1);
		}
		if (tile2.getAverageColor() != null) {
			tileImgs.add(tile2);
		}
		if (tile3.getAverageColor() != null) {
			tileImgs.add(tile3);
		}
		if (tile4.getAverageColor() != null) {
			tileImgs.add(tile4);
		}
		if (tile5.getAverageColor() != null) {
			tileImgs.add(tile5);
		}
		if (tile6.getAverageColor() != null) {
			tileImgs.add(tile6);
		}
		if (tile7.getAverageColor() != null) {
			tileImgs.add(tile7);
		}
		if (tile8.getAverageColor() != null) {
			tileImgs.add(tile8);
		}
		
		if (tileImgs.size() == 8) return true;
		return false;
		
//		return true;
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
		}
		else if (page == MOSAIC_UI_EDIT) {
			mosaicEditPage = new MosaicUI_Edit(PAN_W, PAN_H, mosaicPage.getMosaicImage());
			mosaicEditPage.draw(g2);
		}
	}
	
	public int getWidth(){
		if (imagesLoaded) return srcImgs.get(0).getWidth();
		return -1;
	}

	public int getHeight(){
		if(imagesLoaded) return srcImgs.get(0).getHeight() + textHeight;
		return -1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
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
