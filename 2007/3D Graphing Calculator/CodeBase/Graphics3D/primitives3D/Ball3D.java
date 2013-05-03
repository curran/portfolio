package primitives3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import math3D.Window3D;
import drawing3D.Object3DViewer;

/**
 * A simple 3D ball.
 * 
 * @author Curran Kelleher
 * 
 */
public class Ball3D extends Object3D {
	/**
	 * The color of the ball.
	 */
	protected Color color;

	/**
	 * The center point of the ball.
	 */
	public Vector3D p;

	/**
	 * A flag which determines whether or not the color of this ball will be
	 * shaded according to depth (true) or not (false).
	 */
	public boolean shade = true;

	/**
	 * The raduis of the ball
	 */
	double radius;

	/**
	 * A temporary variable used in calculating the ball's size on screen.
	 */
	Vector3D temp = new Vector3D(0, 0, 0);

	/**
	 * The points which determine where to draw the circle onscreen.
	 */
	Point A = new Point(), B = new Point();

	/**
	 * Constructs a simple ball.
	 * 
	 * @param p
	 *            the center point of the ball.
	 * @param radius
	 *            the radius of the ball.
	 * @param color
	 *            the color of the ball.
	 */
	public Ball3D(Vector3D p, double radius, Color color) {
		this.p = p;
		this.radius = radius;
		this.color = color;
	}

	/**
	 * Draws this ball to the specified graphics.
	 */
	public void drawOnThis(Graphics2D g) {
		// If the rotated Vector3D is not drawable on the screen, the x
		// coordinate of pointToPutResultIn gets set to Integer.MIN_VALUE by the
		// method Window.getPixelFromTranslatedVector3D

		if (A.x != Integer.MIN_VALUE && B.x != Integer.MIN_VALUE) {
			if (shade)
				g.setColor(Object3DViewer.shadeColor(color, centerPoint.z));
			else
				g.setColor(color);
			g.fillOval(A.x, A.y, B.x, B.y);
		}
	}

	/**
	 * Sets the color of this ball.
	 * 
	 * @param c
	 *            the new color of the ball.
	 */
	public void setColor(Color c) {
		color = c;
	}

	public void calculateRotation(Window3D w) {
		w.getRotatedVector3D(p, centerPoint);

		w.getPixelFromRotatedPoint3D(centerPoint, A);
		temp.x = centerPoint.x - radius;
		temp.y = centerPoint.y;
		temp.z = centerPoint.z;

		w.getPixelFromRotatedPoint3D(temp, B);

		int d = Math.abs(B.x - A.x);

		// offset A, so the center of the oval will represent the point, instead
		// of the upper left corner
		A.x -= d / 2;
		A.y -= d / 2;

		// B describes the width and height of the "oval"
		// set it to the calculated on-screen radius
		B.x = d;
		B.y = d;
	}
	
	/**
	 * If, when drawn on the screen, the specified point is "inside" this object
	 * ("over" it in 2D pixel space), then this method returns true, otherwise
	 * returns false.
	 * 
	 */
	public boolean contains(Point point) {
		return (new Rectangle(A.x, A.y, B.x, B.y)).contains(point);
	}
	
	/**
	 * Sets the center of this object to the specified 3D point, used to enable
	 * the user to move objects. By default this method is not implemented.
	 * 
	 */
	public void setCenterPoint(Vector3D point) {
		p.set(point);
	}
}
