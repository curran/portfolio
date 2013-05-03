package primitives3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import math3D.Window3D;

/**
 * The base class for all 3D objects.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class Object3D {

	/**
	 * The color of this Object3D. Subclasses should use this color when drawing
	 * themselves in the drawOnThis() method.
	 */
	public Color color;

	/**
	 * The center point of this Object3D after rotation, used for Z-sorting.
	 * This must get updated properly in the calculateRotation() method for
	 * correct Z-sorting.
	 */
	public Vector3D centerPoint = new Vector3D(0, 0, 0);

	/**
	 * Draws this Object3D onto the specified Graphics.
	 * 
	 * @param g
	 *            the Graphics on which to draw this 3D object
	 */
	public abstract void drawOnThis(Graphics2D g);

	/**
	 * Called on each object before sorting and drawing them, allows them to
	 * calculate the rotated points and whatever else would help.
	 * 
	 * @param window
	 *            the window used for the rotation (use
	 *            window.getRotatedPoint3D(Point3D p))
	 */
	public abstract void calculateRotation(Window3D window);

	/**
	 * If, when drawn on the screen, the specified point is "inside" this object
	 * ("over" it in 2D pixel space), then this method returns true, otherwise
	 * returns false. By default this method is not implemented.
	 * 
	 * @param point
	 * @return
	 */
	public boolean contains(Point point) {
		return false;
	}

	/**
	 * Sets the center of this object to the specified 3D point, used to enable
	 * the user to move objects. By default this method is not implemented.
	 * 
	 */
	public void setCenterPoint(Vector3D point) {

	}

}
