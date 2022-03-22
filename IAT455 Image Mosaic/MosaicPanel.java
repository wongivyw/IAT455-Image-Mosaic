
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MosaicPanel extends JPanel implements ActionListener {

	private BufferedImage image;
	private final int textHeight = 30;
	private Timer timer;


	public final static int PAN_W = 1400;
	public final static int PAN_H = 750;
		
	public MosaicPanel() {
		setPreferredSize(new Dimension(PAN_W, PAN_H));
		
		MyMouseListener ml = new MyMouseListener(); //mouse clicked
		addMouseListener(ml);
		MyMouseMotionListener mml = new MyMouseMotionListener(); //mouse dragged
		addMouseMotionListener(mml);
		
		timer = new Timer(30, this);
		timer.start();
		
		try {
			image = ImageIO.read(new File("cow.jpg")); 
		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, textHeight, null);
	}
	
	public int getWidth(){
		return image.getWidth();
	}

	public int getHeight(){
		return image.getHeight() + textHeight;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
public class MyMouseListener extends MouseAdapter {
		
		public void mouseClicked(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();
	
			//handles double click events
			if (e.getClickCount() == 2) { 
				//double clicked
			}

		}
		
		public void mousePressed(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();
			
			repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			int eX = e.getX();
			int eY = e.getY();

			repaint();
		}
	}
	
	public class MyMouseMotionListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			//to implement dragging on an item, modify mousePressed() and mouseDragged()
			int eX = e.getX();
			int eY = e.getY();
		
			repaint();
		}
	}

}
