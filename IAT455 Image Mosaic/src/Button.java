package src;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Button {
	Rectangle2D.Double box;
	String name;
	
	public Button(Rectangle2D.Double box, String name) {
		this.box = box;
		this.name = name;
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(Color.cyan);
		g2.draw(box);
	}
	
	public boolean isClicked(int eX, int eY) {
		return box.contains(eX, eY);
	}
	
	public String getName() {
		return name;
	}
}
