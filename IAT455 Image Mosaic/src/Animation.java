package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;

import main.MosaicPanel;

public class Animation {
	BufferedImage img, currentFrame;
	int w, h, xPos, yPos, currentPos;
	double scale;
	ArrayList<BufferedImage> frames;
	ArrayList<Integer> factors;

	public Animation(BufferedImage img, ArrayList<Integer> factors, int xPos, int yPos, double scale) {
		this.img = img;
		this.factors = factors;
		this.w = img.getWidth();
		this.h = img.getHeight();
		this.xPos = xPos;
		this.yPos = yPos;
		this.scale = scale;
		frames = new ArrayList<BufferedImage>();
		frames = getFrames(factors);
		currentPos = 0;
		currentFrame = frames.get(0);
		
	}
	
	public void draw(Graphics2D g2) {
		g2.drawImage(currentFrame, xPos, yPos, (int)(w*scale), (int)(h*scale), null);
	}
	
//	public BufferedImage getNextFrame(int currFrameIndex) {
//		BufferedImage nextFrame;
//		if (currFrameIndex < frames.size()-1) {
//			nextFrame = frames.get(currFrameIndex+1);
//			currentPos++;
//		} else {
//			nextFrame = frames.get(0);
//			currentPos = 0;
//		}
//		return nextFrame;
//	}
	
	public void changeFrame() {
		if (currentPos < frames.size()-1) {
			currentPos++;
		} else {
			currentPos = 0;
		}
		currentFrame = frames.get(currentPos);
	}
	
	private ArrayList<BufferedImage> getFrames(ArrayList<Integer> factors) {
		ArrayList<BufferedImage> listOfFrames = new ArrayList<BufferedImage>();
		for (int i = 0; i < factors.size(); i++) {
			listOfFrames.add(averageImage(img, factors.get(i)));
		}		
		return listOfFrames;
	}
	
	private BufferedImage averageImage(BufferedImage src, int tileSize) {
		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

		int numTiles = src.getWidth()/tileSize;
		
		int posX, posY;
		for (int i = 0; i < numTiles; i++) { //y
			posY = tileSize*i; //top left corner of tile in y-axis
			for (int j = 0; j < numTiles; j++) { //x
				posX = tileSize*j; //top left corner of the tile in x-axis

				//find average color in tile and set the tile to that
				// for loop to iterate through each pixel in tile
				int red = 0, green = 0, blue = 0;
				for (int k = 0; k < tileSize; k++) { //y
					red=0;
					green=0;
					blue=0;
					for (int l = 0; l < tileSize; l++) {//x
						int rgba = src.getRGB(posX+l, posY+k);
						red += Util.getRed(rgba);
						green += Util.getGreen(rgba);
						blue += Util.getBlue(rgba);
					}
					
				}
				red = Util.clip(red/tileSize);
				green = Util.clip(green/tileSize);
				blue = Util.clip(blue/tileSize);
				int rgba = new Color(red, green, blue).getRGB();	

				// for loop to iterate through each pixel in tile
				for (int k = 0; k < tileSize; k++) { //y
					for (int l = 0; l < tileSize; l++) {//x
						result.setRGB(posX+l, posY+k, rgba); // use average color in tile
					}
				}
			}
		}
		return result;
	}
}
;