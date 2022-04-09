package src;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Screen {
	String filePath;
	BufferedImage screen;
	int w, h;
	boolean successful;
	
	//button for mouse clicks
	Rectangle2D.Double button;
	int buttonX, buttonY;
	
	public Screen (String filePath, int w, int h) {
		this.filePath = filePath;
		try {
			// scales the image into a square
			screen = ImageIO.read(new File(filePath)); // original image
			successful = true;			
		} catch (Exception e) {
			System.out.println("Cannot load the provided image: " + filePath);
			successful = false;
		}
		this.w = w;
		this.h = h;
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(screen, 0, 0, w, h, null);
		
		if (button != null) {
			g2.setColor(Color.cyan);
			g2.draw(button);
		}
	}
	
	public boolean isSuccessful() {
		return successful;
	}
	
	public void addButtonArea(int xPos, int yPos, int w, int h) {
		button = new Rectangle2D.Double(xPos, yPos, w, h);
	}

	public boolean isButtonClicked(int xPos, int yPos) {
		if (button.contains(xPos, yPos)) {
			return true;
		}
		return false;
	}

}
