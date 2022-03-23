package src;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class TileImage {
	BufferedImage image;
	
	public TileImage(String name, int w, int h) {
		try {
			// scales the image into a square
			Util.resize(name, name, w, h);
			image = ImageIO.read(new File(name)); 
			
		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
}
