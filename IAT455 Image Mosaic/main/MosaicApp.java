package main;

// from CafeDemo.java by Ivy Wong in IAT 265 Assignment 4
//**********************************************************/
import javax.imageio.ImageIO;
import javax.media.jai.Histogram;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MosaicApp extends JFrame {

	public MosaicApp(String title) {
		super(title);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//		this.setLocation(350, 100);
		MosaicPanel cp = new MosaicPanel();
		this.add(cp);
		this.pack();
		this.setVisible(true);	
	}
		
	public static void main(String[] args) {
		new MosaicApp("MosaicApp Demo");
	}

	
}//=======================================================//