package primitives3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import math3D.Window3D;

/**
 * Draws a String in 3D space
 * 
 * @author Curran Kelleher
 * 
 */
public class textObject3D extends Object3D {
	String text;

	Vector3D pointInSpace;

	Point placeToDrawString = new Point();

	Color color;

	/**
	 * Creates a textObject3D with the specified text at the specified place.
	 * 
	 * @param text
	 *            The String to display
	 * @param centerPoint
	 *            the place in 3D space to put it
	 * @param color
	 *            the color of the text
	 */
	public textObject3D(String text, Vector3D centerPoint, Color color) {
		this.text = text;
		this.pointInSpace = centerPoint;
		this.color = color;
	}

	public void drawOnThis(Graphics2D g) {
		g.setColor(color);
		g.drawString(text, placeToDrawString.x-2, placeToDrawString.y+3);
	}

	public void calculateRotation(Window3D window) {
		window.getRotatedVector3D(pointInSpace, centerPoint);
		window.getPixelFromRotatedPoint3D(centerPoint, placeToDrawString);
	}
}
