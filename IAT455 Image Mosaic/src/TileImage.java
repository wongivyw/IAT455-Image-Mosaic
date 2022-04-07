/*
 * TileImage holds the operations necessary to perform on a tile image for mosaic image creation.
 */
package src;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class TileImage {
	private BufferedImage image;
	private Color avgCol;
	private String name;
	
	public TileImage(String name, int w, int h) {
		this.name = name;
		try {
			// scales the image into a square
			Util.resize(name, name, w, h);
			image = ImageIO.read(new File(name)); 
			
			avgCol = computeAverageColor();
			
		} catch (Exception e) {
			System.out.println("Cannot load the provided image: " + name);
		}
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public Color getAverageColor() {
		return avgCol;
	}
	
	private Color computeAverageColor() {
		if (image == null) return null;
		int red = 0, green = 0, blue = 0;
		int numPixelsInImage = 0;
		
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				// for each pixel in the image...
				int rgb = image.getRGB(x, y);
				red += Util.getRed(rgb);
				green += Util.getGreen(rgb);
				blue += Util.getBlue(rgb);
			}
			numPixelsInImage = image.getWidth() * image.getHeight();
		}
			
		//take the average of the sum of rgb channels
		red = Util.clip(red/numPixelsInImage);
		green = Util.clip(green/numPixelsInImage);
		blue = Util.clip(blue/numPixelsInImage);
		return new Color(red, green, blue);
	}
}
