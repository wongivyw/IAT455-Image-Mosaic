/*
 * TileImage holds the operations necessary to perform on a tile image for mosaic image creation.
 */
package src;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

public class TileImage {
	private BufferedImage fullSizeImage, image, bkgRemoved;
	private Color avgCol, avgColofBkgRemoved;
	private String name;
	private int w, h;
	
	public TileImage(String name, String outputName, int w, int h) {
		this.name = name; //or outputName
		this.w = w;
		this.h = h;
		try {
			// scales the image into a square
			fullSizeImage = ImageIO.read(new File(name)); // full size image
			Util.resize(name, outputName, w, h);
			image = ImageIO.read(new File(outputName)); // original image
			avgCol = computeAverageColor(); //do this on original image
			bkgRemoved = getBackgroundRemoved(); //remove background on image
			avgColofBkgRemoved = computeAverageColorIgnoreBlack(); // do this to background removed img
//			avgCol = computeAverageColorIgnoreBlack(); // do this on background removed image
			
		} catch (Exception e) {
			System.out.println("Cannot load the provided image: " + outputName);
		}
	}
	
//	public TileImage(BufferedImage img) {
//		image = img;
//		w = img.getWidth();
//		h = img.getHeight();
//		avgCol = computeAverageColor();
//		bkgRemoved = getBackgroundRemoved();
//		avgColofBkgRemoved = computeAverageColorIgnoreBlack();
//	}
	
	public void draw(Graphics2D g2, int xPos, int yPos, double scale) {
		g2.drawImage(image, xPos, yPos, (int)(w*scale), (int)(h*scale), null);
	}
	
	public void drawBackgroundRemoved(Graphics2D g2, int xPos, int yPos, double scale) {
		g2.drawImage(bkgRemoved, xPos, yPos, (int)(w*scale), (int)(h*scale), null);
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public BufferedImage getFullSizeImage() {
		return fullSizeImage;
	}
	
	public BufferedImage getBackgroundRemovedImage() {
		return bkgRemoved;
	}
	
	public Color getAverageColor() {
		return avgCol;
	}
	
	public BufferedImage getAverageColorImage() {
		BufferedImage avgColImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				avgColImg.setRGB(x, y, avgColofBkgRemoved.getRGB());
			}
		}
		return avgColImg;
	}

	
	public Color getAverageColorOfBkgRemoved() {
		return avgColofBkgRemoved;
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

	private Color computeAverageColorIgnoreBlack() {
		if (bkgRemoved == null) return null;
		int red = 0, green = 0, blue = 0;
		int count = 0;
		
		for (int x = 0; x < bkgRemoved.getWidth(); x++) {
			for (int y = 0; y < bkgRemoved.getHeight(); y++) {
				// for each pixel in the image...
				int rgb = bkgRemoved.getRGB(x, y);
				
				if (rgb != Color.black.getRGB()) {
					red += Util.getRed(rgb);
					green += Util.getGreen(rgb);
					blue += Util.getBlue(rgb);
				
					count++;
				}
			}
		}
			
		//take the average of the sum of rgb channels
		red = Util.clip(red/count);
		green = Util.clip(green/count);
		blue = Util.clip(blue/count);
		return new Color(red, green, blue);
	}
	
	private BufferedImage getBackgroundRemoved() {
		BufferedImage matte = matteImage(image);
		BufferedImage rm = removeBackground(image, matte);		
		return rm;
	}
	
	//SOURCE: IAT 455 Assignment 1 by Ivy Wong
    private BufferedImage matteImage (BufferedImage src1) {
		BufferedImage matteImage = new BufferedImage(src1.getWidth(), src1.getHeight(), src1.getType());

		// keying based on chrominance
		int width = src1.getWidth();
        int height = src1.getHeight();
        
        //get color from top corner
        int cornerPixel = src1.getRGB(width - width/10, height/8);
        //set tolerance for color in top color
        int tolerance = 50;

    
    	for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int currPixel = src1.getRGB(x, y);
				
				// check if the corner pixel and the current pixel are within tolerance
				// to see if the current pixel is a background pixel or foreground pixel
				if (withinTolerance(cornerPixel, currPixel, tolerance)) { //if the pixel is within the tolerance -> black
					matteImage.setRGB(x, y, Color.black.getRGB());
				}
				else {//otherwise -> white
					matteImage.setRGB(x, y, Color.white.getRGB());
				}
			}
        }
		return matteImage;
    }
    
	//SOURCE: IAT 455 Assignment 1 by Ivy Wong
    // function to check if two pixels are within a tolerance value
    private boolean withinTolerance (int pixel1, int pixel2, int tolerance) {
    	
    	// get RGB channel values for the first pixel
    	int red1 = Util.getRed(pixel1);
    	int green1 = Util.getGreen(pixel1);
    	int blue1 = Util.getBlue(pixel1);
    	
    	// get RGB channel values for the first pixel
    	int red2 = Util.getRed(pixel2);
    	int green2 = Util.getGreen(pixel2);
    	int blue2 = Util.getBlue(pixel2);
    	
    	// if the difference of all three channels is within tolerance, return true
    	if ((Math.abs(red1-red2) <= tolerance) && 
    			(Math.abs(green1-green2) <= tolerance) && 
    			(Math.abs(blue1-blue2) <= tolerance)) {
    		return true;
    	}
    	return false;
    	
    }
    
//    SOURCE: IAT455 Assignment 1 by Ivy Wong
    // used for the composite image 
    private BufferedImage removeBackground(BufferedImage src1, BufferedImage matteImage) {
		BufferedImage compositeImage = new BufferedImage(src1.getWidth(), src1.getHeight(), src1.getType());
//		O = (A x M) + [(1 � M) x B]
//		image 1 is the foreground, image 2 is the background using matteImage
		
        int width = src1.getWidth();
        int height = src1.getHeight();
        int blackPixel = new Color(0,0,0).getRGB();
        
    	for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel1 = src1.getRGB(x, y);
				int pixelM = matteImage.getRGB(x, y);
				
				int redValue = Util.clip(compositeImageFormula(Util.getRed(pixel1), Util.getRed(blackPixel), Util.getRed(pixelM)));
				int greenValue = Util.clip(compositeImageFormula(Util.getGreen(pixel1), Util.getGreen(blackPixel), Util.getGreen(pixelM)));
				int blueValue = Util.clip(compositeImageFormula(Util.getBlue(pixel1), Util.getBlue(blackPixel), Util.getBlue(pixelM)));
				int new_rgb = new Color(redValue, greenValue, blueValue).getRGB();
				compositeImage.setRGB(x, y, new_rgb);
			}
        }
    	return compositeImage;
    }
    
    // image used to calculate which image (foreground or background) to display
    private int compositeImageFormula (int a, int b, int m) {
    	return (a*m/255) + ((1-m/255)*b);
    }
}
