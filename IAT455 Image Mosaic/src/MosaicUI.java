package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.MosaicPanel;

/*
 * MosaicUI.java
 * Interactive screen for the bulk of the program, displays the mosaic image result
 * As well, performs the mosaic operations
 *
 */
public class MosaicUI {
	Rectangle2D.Double panel;
	private int width, height;
	
	BufferedImage srcImg;
	private int margin, oImageSize,oImageX,oImageY,mImageSize,mImageX,mImageY,
				buttonWidth, buttonHeight,button1X, button1Y, button2X, button2Y;
	Rectangle2D.Double oCard, mCard, button1, button2;

	String title = "Result of your selection";
	String edit = "Edit mosaic image";
	String createAnother = "Create another mosaic";

	
	public MosaicUI(int w, int h, BufferedImage srcImg) {
		width = w;
		height = h;
		this.srcImg = srcImg;
		initialize();
	}
	
	private void initialize() {
		margin = width/10;
		panel = new Rectangle2D.Double(0,0, margin, height);
		
		// original image
		oImageSize = 150;
		oImageX = width/4;
		oImageY = 180;
		//original image card
		oCard = new Rectangle2D.Double(oImageX-10, oImageY-10, oImageSize+20, oImageSize+20);

		//mosaic image
		mImageSize = 300;
		mImageX = width/2;
		mImageY = 180;
		//mosaic image card
		mCard = new Rectangle2D.Double(mImageX-10, mImageY-10, mImageSize+20, mImageSize+20);
		
		//buttons - edit and create another mosaic
		buttonWidth = 200;
		buttonHeight = 40;
		button1X = oImageX - 10;
		button1Y = oImageY + oImageSize + 20 + 40;
		button2X = button1X;
		button2Y = button1Y + buttonHeight + 20;
		button1 = new Rectangle2D.Double(button1X,button1Y,buttonWidth,buttonHeight);
		button2 = new Rectangle2D.Double(button2X,button2Y,buttonWidth,buttonHeight);
				
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(MyColors.red_500);
		g2.fill(panel);
		
		//title
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Helvetica", Font.BOLD, MosaicPanel.FONT_SIZE_HEADER));
		g2.drawString(title, margin*2, 80);
		
		//original image card
		g2.setColor(MyColors.red_800);
		g2.fill(oCard);		
		// original image
		g2.drawImage(srcImg,oImageX,oImageY,oImageSize,oImageSize, null);
		//mosaic image card
		g2.fill(mCard);
		//mosaic image
		g2.drawImage(srcImg,mImageX,mImageY,mImageSize,mImageSize, null);
		
		//buttons 
		g2.setColor(MyColors.button);
		g2.fill(button1);
		g2.fill(button2);
		
		//button text- edit and create another mosaic
		g2.setColor(MyColors.text_light);
		g2.setFont(new Font("Helvetica", Font.BOLD, MosaicPanel.FONT_SIZE_BODY));
		g2.drawString(edit, button1X + buttonWidth/2-edit.length()*4, button1Y + buttonHeight/2 + 6);
		g2.drawString(createAnother, button2X + buttonWidth/2-createAnother.length()*4-2, button2Y + buttonHeight/2 + 6);
		
	}
	
	public Double getEditButton() {
		return button1;
	}

	public Double getCreateAnotherButton() {
		return button2;
	}
}
