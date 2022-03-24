/*
 * TitleUI draws the introduction screen to the application where users can choose to 
 * begin with their selection of the image, or have the program choose a random one.
 */
package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JTextArea;

import main.MosaicPanel;

/*
 * TitleUI.java
 * Interactive screen for the introduction of the program, 
 * lets users choose which image they want to perform mosaic on.
 * As well, aids in detecting mouse events on buttons and image cards
 *
 */

public class TitleUI {
	Rectangle2D.Double panel;
	private int width, height;
	ArrayList<BufferedImage> srcImgs;
	
	//interactive elements (the buttons)
	Rectangle2D.Double beginButton, chooseRandomButton, imgArea1, imgArea2, imgArea3, imgArea4;
	Rectangle2D.Double card1, card2, card3, card4;
	
	int margin2;
	int imageSize, x1, x2, y1, y2;
	int button1X, button2X, buttonY, buttonWidth, buttonHeight;
	
	String title = "Mosaic Images";
	String className = "IAT 455";
	String project = "Final Project";
	String author = "Hana & Ivy";
	
	String instruction = "Choose an image to be mosaic";
	String desc = "The mosaic image is an image that is divided into sections and each section is replaced with another image. "
			+ "A user can control different parameters for the final output, allowing them to fine tune the result before exporting.";
	//break text up to fit on the screen
	String desc1 = "The mosaic image is an image that is divided into sections ";
	String desc2 = "and each section is replaced with another image. A user can";
	String desc3 = "control different parameters for the final output.";
	
	//button text
	String begin = "Begin with selection";
	String random = "Help me choose";
	
	public TitleUI(int w, int h, ArrayList<BufferedImage> srcImgs) {
//		background = new Rectangle2D.Double(0,0,w,h);
		width = w;
		height = h;
		this.srcImgs = srcImgs;
		initialize();

	}
	
	private void initialize() {
		//left side panel
		panel = new Rectangle2D.Double(0,0,width/5,height);
		
		//source image borders
		margin2 = width/5+100;
		int cardSize = 180;
		int gutter = 18;
		card1 = new Rectangle2D.Double(margin2, 80*2+gutter, cardSize, cardSize);
		card2 = new Rectangle2D.Double(margin2+cardSize+gutter, 80*2+gutter, cardSize, cardSize);
		card3 = new Rectangle2D.Double(margin2+cardSize+gutter, 80*2+gutter+cardSize+gutter, cardSize, cardSize);
		card4 = new Rectangle2D.Double(margin2, 80*2+gutter+cardSize+gutter, cardSize, cardSize);

		//source images
		imageSize = 160;
		int gutter2 = gutter+20;
		x1 = margin2+10;
		x2 = margin2+10+gutter2+imageSize;
		y1 = 80*2+gutter+10;
		y2 = 80*2+gutter+10+imageSize+gutter2;
		
		//for mouse detection
		imgArea1 = new Rectangle2D.Double(x1,y1,imageSize,imageSize);
		imgArea2 = new Rectangle2D.Double(x2,y1,imageSize,imageSize);
		imgArea3 = new Rectangle2D.Double(x1,y2,imageSize,imageSize);
		imgArea4 = new Rectangle2D.Double(x2,y2,imageSize,imageSize);
		
		//button borders
		buttonWidth = 200;
		buttonHeight = 40;
		button1X = (MosaicPanel.PAN_W - margin2)/4 + margin2 - buttonWidth + 50;
		button2X = button1X + buttonWidth + cardSize;
		buttonY = y2 + cardSize + gutter*2;
		
		beginButton = new Rectangle2D.Double(button1X,buttonY,buttonWidth,buttonHeight);
		chooseRandomButton = new Rectangle2D.Double(button2X,buttonY,buttonWidth,buttonHeight);
	}
	
	public void draw(Graphics2D g2) {
		//left side panel
		g2.setColor(MyColors.red_500);
		g2.fill(panel);
		
		// title text in panel
//		SOURCE to define fonts: https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html
		int margin = width/10-title.length()*5;
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Helvetica", Font.BOLD, MosaicPanel.FONT_SIZE_HEADER));
		g2.drawString(title, margin, 80);
		
		//panel text
		g2.setFont(new Font("Helvetica", Font.PLAIN , MosaicPanel.FONT_SIZE_HEADER));
		g2.drawString(className, margin, 80*2);
		g2.drawString(project, margin, (int)(80*2.5));
		g2.drawString(author, margin, 80*3);
		
		//source image borders
		g2.setColor(MyColors.red_800);
		g2.fill(card1);
		g2.fill(card2);
		g2.fill(card3);
		g2.fill(card4);

		//source images		
		g2.drawImage(srcImgs.get(0), x1, y1, imageSize, imageSize, null);
		g2.drawImage(srcImgs.get(1), x2, y1, imageSize, imageSize, null);
		g2.drawImage(srcImgs.get(2), x1, y2, imageSize, imageSize, null);
		g2.drawImage(srcImgs.get(3), x2, y2, imageSize, imageSize, null);
		
		//body text - instructions
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Helvetica", Font.PLAIN, MosaicPanel.FONT_SIZE_BODY));
		g2.drawString(instruction, margin2, 80*2);
		
		//button borders
		g2.setColor(MyColors.button);
		g2.fill(beginButton);
		g2.fill(chooseRandomButton);

		//button text
		g2.setColor(MyColors.text_light);
		g2.setFont(new Font("Helvetica", Font.BOLD, MosaicPanel.FONT_SIZE_BODY));
		g2.drawString(begin, button1X + buttonWidth/2-begin.length()*4, buttonY + buttonHeight/2 + 6);
		g2.drawString(random, button2X + buttonWidth/2-random.length()*4-2, buttonY + buttonHeight/2 + 6);
		
		//body text - description
		int lineHeight = 22;
		g2.setColor(MyColors.button);
		g2.setFont(new Font("Helvetica", Font.PLAIN, MosaicPanel.FONT_SIZE_BODY));
		g2.drawString(desc1, button2X, (int)(80*2.5));
		g2.drawString(desc2, button2X, (int)(80*2.5) + lineHeight);
		g2.drawString(desc3, button2X, (int)(80*2.5) + lineHeight*2);
	}

	public Double getBeginButton() {
		return beginButton;
	}

	public Double getChooseRandomButton() {
		return chooseRandomButton;
	}

	public Double getImgArea1() {
		return imgArea1;
	}

	public Double getImgArea2() {
		return imgArea2;
	}

	public Double getImgArea3() {
		return imgArea3;
	}

	public Double getImgArea4() {
		return imgArea4;
	}
		
}
