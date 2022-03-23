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

public class TitleUI {
	Rectangle2D.Double panel;
	private int width, height;
	ArrayList<BufferedImage> srcImgs;
	
	String title = "Mosaic Images";
	String className = "IAT 455";
	String project = "Final Project";
	String author = "Hana & Ivy";
	
	String instruction = "Choose an image to be mosaic";
	String desc = "The mosaic image is an image that is divided into sections and each section is replaced with another image. "
			+ "A user can control different parameters for the final output, allowing them to fine tune the result before exporting.";
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

	}
	
	public void draw(Graphics2D g2) {
		//left side panel
		g2.setColor(MyColors.red_500);
		panel = new Rectangle2D.Double(0,0,width/5,height);
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
		int margin2 = width/5+100;
		int cardSize = 180;
		int gutter = 18;
		Rectangle2D.Double card = new Rectangle2D.Double(margin2,80*2+gutter,cardSize,cardSize);
		
		AffineTransform tr = g2.getTransform();
		g2.setColor(MyColors.red_800);
		g2.fill(card);
		g2.translate(cardSize+gutter,0);
		g2.fill(card);
		g2.translate(0,cardSize+gutter);
		g2.fill(card);
		g2.translate(-cardSize-gutter,0);
		g2.fill(card);
		g2.setTransform(tr);

		//source images
		int imageSize = 160;
		int gutter2 = gutter+20;
		int x1 = margin2+10;
		int x2 = margin2+10+gutter2+imageSize;
		int y1 = 80*2+gutter+10;
		int y2 = 80*2+gutter+10+imageSize+gutter2;
		
		g2.drawImage(srcImgs.get(0), x1, y1, imageSize, imageSize, null);
		g2.drawImage(srcImgs.get(1), x2, y1, imageSize, imageSize, null);
		g2.drawImage(srcImgs.get(2), x1, y2, imageSize, imageSize, null);
		g2.drawImage(srcImgs.get(3), x2, y2, imageSize, imageSize, null);
		
		//body text - instructions
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("Helvetica", Font.PLAIN, MosaicPanel.FONT_SIZE_BODY));
		g2.drawString(instruction, margin2, 80*2);
		
		//button borders
		int buttonWidth = 200;
		int buttonHeight = 40;
		int button1X = (MosaicPanel.PAN_W - margin2)/4 + margin2 - buttonWidth + 50;
		int button2X = button1X + buttonWidth + cardSize;
		int buttonY = y2 + cardSize + gutter*2;
		
		Rectangle2D.Double button1 = new Rectangle2D.Double(button1X,buttonY,buttonWidth,buttonHeight);
		Rectangle2D.Double button2 = new Rectangle2D.Double(button2X,buttonY,buttonWidth,buttonHeight);
		g2.setColor(MyColors.button);
		g2.fill(button1);
		g2.fill(button2);

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
		
}
