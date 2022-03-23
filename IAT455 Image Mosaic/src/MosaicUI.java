package src;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
 * MosaicUI.java
 * Interactive screen for the bulk of the program, displays the mosaic image result
 * As well, performs the mosaic operations
 *
 */
public class MosaicUI {
	Rectangle2D.Double panel;
	private int width, height;
	ArrayList<BufferedImage> srcImgs;
	
	public MosaicUI(int w, int h, ArrayList<BufferedImage> srcImgs) {
		width = w;
		height = h;
		this.srcImgs = srcImgs;
	}
}
