/*
 * MosaicUI_Edit is the class that draws a screen to the application, namely the edit mosaic result screen.
 */
package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

import main.MosaicPanel;

public class MosaicUI_Edit {
	Rectangle2D.Double panel;
	private int width, height;
	
	BufferedImage mosaicImage, mosaicImageEdited;
	private int margin, oImageSize,oImageX,oImageY,mImageSize,mImageX,mImageY,
				buttonWidth, buttonHeight,button1X, button1Y, button2X, button2Y,
				optionSize, option1X, option2X, option3X, option1Y, option2Y;
	
	Rectangle2D.Double oCard, mCard, button1, button2, option1, option2, option3, option4, option5, option6;

	String title = "Edit the mosaic result";
	String save = "Save image"; //button1
	String createAnother = "Create another mosaic"; //button2
	String reset = "Reset changes";
	
	String addHue = "Filter options:";

	
	public MosaicUI_Edit(int w, int h, BufferedImage mosaicImage) {
		width = w;
		height = h;
		this.mosaicImage = mosaicImage;
//		mosaicImageEdited = mosaicImage; //new EditOp(mosaicImage).getImage(mosaicImage);
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
		button2X = mImageX + mImageSize+10 - buttonWidth;
		button2Y = mImageY + mImageSize + 20 + 40;
		button1X = button2X - buttonWidth - 60;
		button1Y = button2Y;
		button1 = new Rectangle2D.Double(button1X,button1Y,buttonWidth,buttonHeight);
		button2 = new Rectangle2D.Double(button2X,button2Y,buttonWidth,buttonHeight);
		
		//filter buttons - hue
		int gutter = 10;
		optionSize = 40;
		option1X = oImageX - 10;
		option2X = option1X + optionSize + gutter;
		option3X = option2X + optionSize + gutter;
		option1Y = oImageY + oImageSize + 10 + 50 + gutter;
		option2Y = option1Y + optionSize + gutter;
		option1 = new Rectangle2D.Double(option1X, option1Y, optionSize, optionSize);
		option2 = new Rectangle2D.Double(option2X, option1Y, optionSize, optionSize);
		option3 = new Rectangle2D.Double(option3X, option1Y, optionSize, optionSize);
		
		option4 = new Rectangle2D.Double(option1X, option2Y, optionSize, optionSize);
		option5 = new Rectangle2D.Double(option2X, option2Y, optionSize, optionSize);
		option6 = new Rectangle2D.Double(option3X, option2Y, optionSize, optionSize);
		
		
				
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
		g2.drawImage(mosaicImage,oImageX,oImageY,oImageSize,oImageSize, null);
		//mosaic image card
		g2.fill(mCard);
		//mosaic image
//		g2.drawImage(mosaicImage,mImageX,mImageY,mImageSize,mImageSize, null);
		
		//buttons 
		g2.setColor(MyColors.button);
		g2.fill(button1);
		g2.fill(button2);
		
		//button text- edit and create another mosaic
		g2.setColor(MyColors.text_light);
		g2.setFont(new Font("Helvetica", Font.BOLD, MosaicPanel.FONT_SIZE_BODY));
		g2.drawString(save, button1X + buttonWidth/2-save.length()*4, button1Y + buttonHeight/2 + 6);
		g2.drawString(createAnother, button2X + buttonWidth/2-createAnother.length()*4-2, button2Y + buttonHeight/2 + 6);
		
		//filter text - hue
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Helvetica", Font.PLAIN, MosaicPanel.FONT_SIZE_BODY));
		g2.drawString(addHue, oImageX-10, oImageY + oImageSize + 50);
		
		//filter buttons - hue
		g2.setColor(Color.RED);
		g2.fill(option1);
		g2.setColor(Color.GREEN);
		g2.fill(option2);
		g2.setColor(Color.BLUE);
		g2.fill(option3);
		
		g2.setColor(Color.CYAN);
		g2.fill(option4);
		g2.setColor(Color.PINK);
		g2.fill(option5);
		g2.setColor(Color.ORANGE);
		g2.fill(option6);

	}
	
	public Double getSaveButton() {
		return button1;
	}

	public Double getCreateAnotherButton() {
		return button2;
	}
}
