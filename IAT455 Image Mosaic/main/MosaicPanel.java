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

import src.MyColors;
import src.TitleUI;

public class MosaicPanel extends JPanel implements ActionListener {

	private BufferedImage image;
	private final int textHeight = 30;

	public final static int PAN_W = 1400;
	public final static int PAN_H = 750;
	public final static int NUM_SRCIMG = 4;
	public final static int FONT_SIZE_HEADER = 18;
	public final static int FONT_SIZE_BODY = 16;

	//interactive elements (the buttons)
//	Rectangle2D.Double beginButton = new Rectangle2D.Double();
//	Rectangle2D.Double chooseRandomButton = new Rectangle2D.Double();
//	Rectangle2D.Double imgArea1 = new Rectangle2D.Double();
//	Rectangle2D.Double imgArea2 = new Rectangle2D.Double();
//	Rectangle2D.Double imgArea3 = new Rectangle2D.Double();
//	Rectangle2D.Double imgArea4 = new Rectangle2D.Double();
	Rectangle2D.Double beginButton, chooseRandomButton, imgArea1, imgArea2, imgArea3, imgArea4;

	
	TitleUI introPage;
	ArrayList<BufferedImage> srcImgs = new ArrayList<BufferedImage>();
		
	public MosaicPanel() {
		setPreferredSize(new Dimension(PAN_W, PAN_H));
		setBackground(MyColors.red_700);

		// SOURCE for mouse events taken from IAT 265 cafe project by Ivy
		MyMouseListener ml = new MyMouseListener(); //mouse clicked
		addMouseListener(ml);
		MyMouseMotionListener mml = new MyMouseMotionListener(); //mouse dragged
		addMouseMotionListener(mml);
		
		try {
			image = ImageIO.read(new File("smileyFruit.jpeg")); 
		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
		
		//add the image 4 times (needs to be modified)
		for (int i = 0; i < NUM_SRCIMG; i++) {
			srcImgs.add(image);
		}
		
		//titleUI
		introPage = new TitleUI(PAN_W, PAN_H, srcImgs);
		beginButton = introPage.getBeginButton();
		chooseRandomButton = introPage.getChooseRandomButton();
		imgArea1 = introPage.getImgArea1();
		imgArea2 = introPage.getImgArea2();
		imgArea3 = introPage.getImgArea3();
		imgArea4 = introPage.getImgArea4();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		introPage.draw(g2);
	}
	
	public int getWidth(){
		return image.getWidth();
	}

	public int getHeight(){
		return image.getHeight() + textHeight;
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
			if (beginButton.contains(eX, eY)) {
				System.out.println("begin button clicked");
			} else if (chooseRandomButton.contains(eX, eY)) {
				System.out.println("choose random button clicked");
			} else if (imgArea1.contains(eX, eY)) {
				System.out.println("image area 1 clicked");
			} else if (imgArea2.contains(eX, eY)) {
				System.out.println("image area 2 clicked");
			} else if (imgArea3.contains(eX, eY)) {
				System.out.println("image area 3 clicked");
			} else if (imgArea4.contains(eX, eY)) {
				System.out.println("image area 4 clicked");
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
