package primitives3D;

import graphicsUtilities.Polygon2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import math3D.Window3D;

/**
 * A 3D polygon.
 * 
 * @author Curran Kelleher
 * 
 */
public class Polygon3D extends Object3D {
	/**
	 * When true, all Polygon3D objects are shaded using their normals
	 */
	public static boolean shade = true;

	/**
	 * Vectors used for shading calculations; the normal and a re-usable vector
	 * for calculating rotations of the normal.
	 */
	Vector3D normal = new Vector3D(0, 0, 0), rotatedNormal = new Vector3D(0, 0,
			0);

	/**
	 * The array containing the points which bound the 3D polygon.
	 */
	Vector3D[] polygonPoints;

	/**
	 * The x points of the 2D triangle used to draw this 3D triangle.
	 */
	float[] xPoints;

	/**
	 * The y points of the 2D triangle used to draw this 3D triangle.
	 */
	float[] yPoints;

	/**
	 * The 2D polygon used to draw this 3D polygon.
	 */
	Polygon2D polygon = new Polygon2D();

	/**
	 * A Point object used as a temporary variable in rotation calculations.
	 */
	Point2D.Float tempPoint = new Point2D.Float();

	/**
	 * The color of this object when shaded.
	 */
	Color shadedColor;

	public Polygon3D(Vector3D[] polygonPoints, Color color) {
		if (polygonPoints.length < 3)
			(new Exception(
					"polygonPoints must have at least 3 points to make a polygon!"))
					.printStackTrace();
		this.polygonPoints = polygonPoints;
		xPoints = new float[polygonPoints.length];
		yPoints = new float[polygonPoints.length];
		for (int i = 0; i < polygonPoints.length; i++) {
			xPoints[i] = 0;
			yPoints[i] = 0;
		}
		polygon.xpoints = xPoints;
		polygon.ypoints = yPoints;
		polygon.npoints = polygonPoints.length;
		this.color = color;
		shadedColor = color;
	}

	/**
	 * Draws this Object3D onto the specified Graphics.
	 * 
	 * @param g
	 *            the Graphics on which to draw this 3D object
	 */
	public void drawOnThis(Graphics2D g) {
		// If the rotated Vector3D is not drawable on the screen, the x
		// coordinate of pointToPutResultIn gets set to Integer.MIN_VALUE by the
		// method Window.getPixelFromTranslatedVector3D

		g.setColor(shade ? shadedColor : color);
		g.fill(polygon);
	}

	/**
	 * )Called on each object before sorting and drawing them, allows them to
	 * calculate the rotated points and whatever else would help.
	 * 
	 * @param window
	 *            the window used for the rotation (use
	 *            window.getRotatedVector3D(Vector3D p))
	 */
	public void calculateRotation(Window3D w) {
		w.getRotatedVector3D(polygonPoints[0], centerPoint);

		w.getPixelFromRotatedPoint3D(centerPoint, tempPoint);
		xPoints[0] = tempPoint.x;
		yPoints[0] = tempPoint.y;

		for (int i = 1; i < polygonPoints.length; i++) {
			w.getPixelFromVector3D(polygonPoints[i], tempPoint);
			xPoints[i] = tempPoint.x;
			yPoints[i] = tempPoint.y;
		}
		if (w.drawFor3D) {
			if (shade)
				calculateShading(w);
		} else
			shadedColor = color;
	}

	int[] initColorComponents = new int[3];

	int[] colorComponents = new int[3];

	private void calculateShading(Window3D w) {
		/**
		 * if(polygonPoints.length == 4) { Vector3D tempA=new Vector3D();
		 * Vector3D tempB=new Vector3D();
		 * 
		 * polygonPoints[2].minus(polygonPoints[0],tempA);
		 * polygonPoints[3].minus(polygonPoints[1],tempB);
		 * 
		 * tempA.cross(tempB,normal); } else
		 */
		Vector3D.calculateNormal(polygonPoints[0], polygonPoints[1],
				polygonPoints[2], normal);

		w.getRotatedVector3D(normal, rotatedNormal);
		double thetaX = Math.atan2(rotatedNormal.x, rotatedNormal.z);
		double thetaY = Math.atan2(rotatedNormal.y, rotatedNormal.z);

		double sunX = 0;
		double sunY = 0;

		double angleAwayFromSunX = Math.abs(thetaX - sunX);
		double angleAwayFromSunY = Math.abs(thetaY - sunY);
		double AngleAwayFromSun = Math.sqrt(angleAwayFromSunX
				* angleAwayFromSunX + angleAwayFromSunY * angleAwayFromSunY);
		double maxAngleAwayFromSun = Math.sqrt(2) * Math.PI;

		// 1 is closest, 0 is farthest away
		double scaledAngleAwayFromSun = 1 - AngleAwayFromSun
				/ maxAngleAwayFromSun;
		// 1 is brightest, 0 is darkest
		double usableAngleAwayFromSun = 2 * Math
				.abs(scaledAngleAwayFromSun - .5);

		initColorComponents[0] = color.getRed();
		initColorComponents[1] = color.getGreen();
		initColorComponents[2] = color.getBlue();

		for (int i = 0; i < 3; i++) {
			colorComponents[i] = (int) ((double) initColorComponents[i] * (usableAngleAwayFromSun + .2));
			if (colorComponents[i] > 255)
				colorComponents[i] = 255;
			if (colorComponents[i] < 0)
				colorComponents[i] = 0;
		}
		shadedColor = new Color(colorComponents[0], colorComponents[1],
				colorComponents[2], color.getAlpha());

	}
}
