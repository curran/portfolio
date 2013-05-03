package primitives3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import math3D.Window3D;
import drawing3D.Object3DViewer;

/**
 * A 3 dimensional line segment.
 * @author Curran Kelleher
 *
 */
public class LineSegment3D extends Object3D {
	/**
	 * The first endpoint of the 3D line segment.
	 */
	Vector3D p1;
	
	/**
	 * The second endpoint of the 3D line segment.
	 */
	Vector3D p2;
	
	/**
	 * The 2D pixel location from which to start drawing the line segment. (From point A to point B)
	 */
	Point2D.Float A = new Point2D.Float();
	
	/**
	 * The 2D pixel location to draw the line segment to. (From point A to point B)
	 */
	Point2D.Float B = new Point2D.Float();

	/**
	 * Construct a 3D line segment with the specified endpoints and color.
	 * @param p1
	 * @param p2
	 * @param color
	 */
	public LineSegment3D(Vector3D p1, Vector3D p2, Color color) {
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
	}
	
	/**
	 * Draws this Object3D onto the specified Graphics.
	 * @param g the Graphics on which to draw this 3D object
	 */
	public void drawOnThis(Graphics2D g) {
		// If the rotated Vector3D is not drawable on the screen, the x
		// coordinate of pointToPutResultIn gets set to Integer.MIN_VALUE by the
		// method Window.getPixelFromTranslatedVector3D

		if (A.x != Integer.MIN_VALUE && B.x != Integer.MIN_VALUE) {
			g.setColor(Object3DViewer.shadeColor(color,centerPoint.z));
			g.drawLine((int)A.x,(int)A.y,(int)B.x,(int)B.y);
		}
	}

	/**
	 * Called on each object before sorting and drawing them, allows them to
	 * calculate the rotated points and whatever else would help.
	 * 
	 * @param window
	 *            the window used for the rotation (use
	 *            window.getRotatedVector3D(Vector3D p))
	 */
	public void calculateRotation(Window3D w) {
		w.getRotatedVector3D(p1, centerPoint);

		w.getPixelFromRotatedPoint3D(centerPoint, A);
		
		w.getPixelFromVector3D(p2, B);
	}
}
