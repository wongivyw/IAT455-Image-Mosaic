package src;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Screen {
	String filePath;
	BufferedImage screen;
	int w, h;
	boolean successful;
	
	//button for mouse clicks
	ArrayList<Button> buttons = new ArrayList<Button>();
	
	public Screen (String filePath, int w, int h) {
		this.filePath = filePath;
		try {
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
		
//		if (!buttons.isEmpty()) {
//			for (Button b : buttons) {
//				b.draw(g2);
//			}
//		}
	}
	
	public boolean isSuccessful() {
		return successful;
	}
	
	public void addButtonArea(int xPos, int yPos, int w, int h, String buttonName) {
		Rectangle2D.Double newButton = new Rectangle2D.Double(xPos, yPos, w, h);
		buttons.add(new Button(newButton, buttonName));
	}

	public boolean isButtonClicked(int xPos, int yPos, String buttonName) {
		for (Button b : buttons) {
			if (b.getName().equals(buttonName)) {
				return b.isClicked(xPos, yPos);
			}
		}
		return false;
	}

}