package math3D;

import generalMathClasses.Window2D;

import java.awt.Point;
import java.awt.geom.Point2D;

import primitives3D.Vector3D;

/**
 * Translates 3D coordinates from values to pixels and handles rotation
 * calculations.
 * 
 * @author Curran Kelleher
 * 
 */
public class Window3D extends Window2D {
	/**
	 * The distance of the viewer from the origin.
	 */
	private double viewerZdist = 50;

	/**
	 * The zoom factor.
	 */
	private double zoom = 30;

	/**
	 * When true, 3D perspective is implemented. When false, this window behaves
	 * like a 2D window.
	 */
	public boolean drawFor3D = true;

	/**
	 * Vectors used in rotation calculation.
	 */
	private Vector3D rotatedXAxis = new Vector3D(1, 0, 0),
			rotatedYAxis = new Vector3D(0, 1, 0), rotatedZAxis = new Vector3D(
					0, 0, 1);

	/**
	 * Temporary variable used by getPixelFromRotatedVector3D()
	 */
	double denom = 0;

	/**
	 * Temporary variable used by getPixelFromVector3D()
	 */
	Vector3D tempPoint = new Vector3D(0, 0, 0);

	/**
	 * Construct a Window3D with the specified dimensions (in pixels) of the
	 * viewing space. The range is set to -10, 10, -10, 10.
	 * 
	 * @param w
	 *            the width (in pixels) of the drawing space.
	 * @param h
	 *            the height (in pixels) of the drawing space.
	 */
	public Window3D(int w, int h) {
		super(w, h);
	}

	public void setRotation(double xrot, double yrot, double zrot) {
		// reset the axes for fresh rotation
		rotatedXAxis.x = 1;
		rotatedXAxis.y = 0;
		rotatedXAxis.z = 0;

		rotatedYAxis.x = 0;
		rotatedYAxis.y = 1;
		rotatedYAxis.z = 0;

		rotatedZAxis.x = 0;
		rotatedZAxis.y = 0;
		rotatedZAxis.z = 1;

		// perform the rotation

		rotatedXAxis.rotateVectorAboutZAxis(zrot, rotatedXAxis);
		rotatedYAxis.rotateVectorAboutZAxis(zrot, rotatedYAxis);
		rotatedZAxis.rotateVectorAboutZAxis(zrot, rotatedZAxis);

		rotatedXAxis.rotateVectorAboutXAxis(xrot, rotatedXAxis);
		rotatedYAxis.rotateVectorAboutXAxis(xrot, rotatedYAxis);
		rotatedZAxis.rotateVectorAboutXAxis(xrot, rotatedZAxis);

		rotatedXAxis.rotateVectorAboutYAxis(yrot, rotatedXAxis);
		rotatedYAxis.rotateVectorAboutYAxis(yrot, rotatedYAxis);
		rotatedZAxis.rotateVectorAboutYAxis(yrot, rotatedZAxis);

	}

	/**
	 * Rotates the specified point according to the rotation parameters which
	 * were set by calling setRotation().
	 * 
	 * @param pointToRotate
	 *            the template point to rotate (this point is not changed, the
	 *            coordinates of the rotated point are put into
	 *            pointToPutResultIn.)
	 * @param pointToPutResultIn
	 *            the point which will get set to the coordinates of
	 *            pointToRotate after the rotation has been calculated.
	 */
	public void getRotatedVector3D(Vector3D pointToRotate,
			Vector3D pointToPutResultIn) {
		if (drawFor3D)
			rotatePoint(pointToRotate, rotatedXAxis, rotatedYAxis,
					rotatedZAxis, pointToPutResultIn);
		else
			pointToPutResultIn.set(pointToRotate);
	}

	/**
	 * Rotates the specified point given the rotated X,Y,and Z axes.
	 * 
	 * @param pointToRotate
	 * @param rotatedXAxis
	 * @param rotatedYAxis
	 * @param rotatedZAxis
	 * @param pointToPutResultIn
	 */
	private void rotatePoint(Vector3D pointToRotate, Vector3D rotatedXAxis,
			Vector3D rotatedYAxis, Vector3D rotatedZAxis,
			Vector3D pointToPutResultIn) {
		pointToPutResultIn.x = pointToRotate.x * rotatedXAxis.x
				+ pointToRotate.y * rotatedYAxis.x + pointToRotate.z
				* rotatedZAxis.x;
		pointToPutResultIn.y = pointToRotate.x * rotatedXAxis.y
				+ pointToRotate.y * rotatedYAxis.y + pointToRotate.z
				* rotatedZAxis.y;
		pointToPutResultIn.z = pointToRotate.x * rotatedXAxis.z
				+ pointToRotate.y * rotatedYAxis.z + pointToRotate.z
				* rotatedZAxis.z;
	}

	/**
	 * Maps a Vector3D to a pixel, applying the window's transformation
	 * (rotation and and scale).
	 * 
	 * Puts the result directly into the x and y coordinates of the given
	 * <code>Point</code> (pointToPutResultIn) This is done to optimize memory
	 * usage and enhance performance.
	 * 
	 * If the specified <code>Vector3D</code> is not drawable on the screen,
	 * the x coordinate of pointToPutResultIn gets set to
	 * <code>Integer.MIN_VALUE</code>
	 * 
	 */
	public void getPixelFromVector3D(Vector3D p, Point pointToPutResultIn) {
		if (drawFor3D)
			getRotatedVector3D(p, tempPoint);
		getPixelFromRotatedPoint3D(tempPoint, pointToPutResultIn);
	}
	
	/**
	 * Maps a Vector3D to a pixel, applying the window's transformation
	 * (rotation and and scale).
	 * 
	 * Puts the result directly into the x and y coordinates of the given
	 * <code>Point</code> (pointToPutResultIn) This is done to optimize memory
	 * usage and enhance performance.
	 * 
	 * If the specified <code>Vector3D</code> is not drawable on the screen,
	 * the x coordinate of pointToPutResultIn gets set to
	 * <code>Integer.MIN_VALUE</code>
	 * 
	 */
	public void getPixelFromVector3D(Vector3D p, Point2D.Float pointToPutResultIn) {
			getRotatedVector3D(p, tempPoint);
		getPixelFromRotatedPoint3D(tempPoint, pointToPutResultIn);
	}

	/**
	 * Maps a Vector3D to a pixel, but without applying the window's rotation.
	 * 
	 * Puts the result directly into the x and y coordinates of the given
	 * <code>Point</code> (pointToPutResultIn) This is done to optimize memory
	 * usage and enhance performance.
	 * 
	 * If the specified <code>Vector3D</code> is not drawable on the screen,
	 * the x coordinate of pointToPutResultIn gets set to
	 * <code>Integer.MIN_VALUE</code>
	 */
	public void getPixelFromRotatedPoint3D(Vector3D p, Point pointToPutResultIn) {
		if (drawFor3D) {
			denom = p.z - viewerZdist;
			if (denom > 0 || p.z == Double.NaN || p.x == Double.NaN
					|| p.y == Double.NaN || Double.isInfinite(p.z)
					|| Double.isInfinite(p.x) || Double.isInfinite(p.y)) {
				pointToPutResultIn.x = Integer.MIN_VALUE;
			} else {
				pointToPutResultIn.x = getXpixel(zoom * p.x / denom);
				pointToPutResultIn.y = getYpixel(zoom * p.y / denom);
			}
		} else {
			pointToPutResultIn.x = getXpixel(p.x);
			pointToPutResultIn.y = getYpixel(p.y);
		}
	}
	
	/**
	 * Maps a Vector3D to a pixel, but without applying the window's rotation.
	 * 
	 * Puts the result directly into the x and y coordinates of the given
	 * <code>Point</code> (pointToPutResultIn) This is done to optimize memory
	 * usage and enhance performance.
	 * 
	 * If the specified <code>Vector3D</code> is not drawable on the screen,
	 * the x coordinate of pointToPutResultIn gets set to
	 * <code>Integer.MIN_VALUE</code>
	 */
	public void getPixelFromRotatedPoint3D(Vector3D p, Point2D.Float pointToPutResultIn) {
		if (drawFor3D) {
			denom = p.z - viewerZdist;
			if (denom > 0 || p.z == Double.NaN || p.x == Double.NaN
					|| p.y == Double.NaN || Double.isInfinite(p.z)
					|| Double.isInfinite(p.x) || Double.isInfinite(p.y)) {
				pointToPutResultIn.x = Integer.MIN_VALUE;
			} else {
				pointToPutResultIn.x = (float)getXpixelAsDouble(zoom * p.x / denom);
				pointToPutResultIn.y = (float)getYpixelAsDouble(zoom * p.y / denom);
			}
		} else {
			pointToPutResultIn.x = (float)getXpixelAsDouble(p.x);
			pointToPutResultIn.y = (float)getYpixelAsDouble(p.y);
		}
	}
}