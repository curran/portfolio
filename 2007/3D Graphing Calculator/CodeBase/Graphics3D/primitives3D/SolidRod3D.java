package primitives3D;

import graphicsUtilities.Polygon2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import math3D.Window3D;
import drawing3D.Object3DViewer;

/**
 * A class which fills the outline created by the sillhouette of an elongated
 * square rod.
 * 
 * @author Curran Kelleher
 * 
 */
public class SolidRod3D extends LineSegment3D {

	/**
	 * A global flag which, when true, causes all SolidRod3D objects to draw
	 * themselves as simple 2D lines. They will appear exactly as LineSegment3D
	 * objects.
	 * 
	 * When false, high precision polygons will represent the lines instead.
	 */
	public static boolean drawAsSimpleLine = false;

	/**
	 * The thickness of the rod; the length of one edge of the square which is
	 * elogated between p1 and p2 to create the rod.
	 */
	double thickness;

	/**
	 * A temporary variable used in calculating the widths at the endpoints.
	 */
	Point2D.Float tempPoint = new Point2D.Float();

	/**
	 * A temporary variable used in calculating the widths for the endpoints.
	 */
	Vector3D temp = new Vector3D(0, 0, 0);

	/**
	 * The 2D polygon which will be drawn on the screen to represent this 3D
	 * "rod"
	 */
	Polygon2D polygon = new Polygon2D(new float[4], new float[4], 4);

	/**
	 * Creates a rod with the specified endpoints, thickness, and color
	 * 
	 * @param p1
	 *            The first endpoint of the 3D rod.
	 * @param p2
	 *            The second endpoint of the 3D rod.
	 * @param thickness
	 *            The thickness of the rod; the length of one edge of the square
	 *            which is elogated between p1 and p2 to create the rod.
	 * @param color
	 *            The color of the rod.
	 */
	public SolidRod3D(Vector3D p1, Vector3D p2, double thickness, Color color) {
		super(p1, p2, color);
		this.thickness = thickness;
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

		if (drawAsSimpleLine)
			super.calculateRotation(w);
		else {
			// rotate the endpoints, using centerPoint as an intremediate value
			// so
			// it can be used for proper z-sorting.
			w.getRotatedVector3D(p1, centerPoint);
			// get the first endpoint in pixel space
			w.getPixelFromRotatedPoint3D(centerPoint, A);
			// figure out the thickness at the first endpoint
			temp.x = centerPoint.x - thickness;
			temp.y = centerPoint.y;
			temp.z = centerPoint.z;
			w.getPixelFromRotatedPoint3D(temp, tempPoint);
			// TODO the 3D graphics are flipped! make 3D and 2D consistent.
			double p1Width = Math.abs(A.x - tempPoint.x);

			// get the second endpoint in pixel space, again using centerPoint
			// as an
			// intermediate
			w.getRotatedVector3D(p2, centerPoint);
			w.getPixelFromRotatedPoint3D(centerPoint, B);
			// figure out the thickness at the second endpoint
			temp.x = centerPoint.x - thickness;
			temp.y = centerPoint.y;
			temp.z = centerPoint.z;
			w.getPixelFromRotatedPoint3D(temp, tempPoint);
			// TODO the 3D graphics are flipped! make 3D and 2D consistent.
			double p2Width = Math.abs(B.x - tempPoint.x);

			double distance = Math.sqrt(Math.pow(B.x - A.x, 2)
					+ Math.pow(B.y - A.y, 2));

			double unitX = -(double) (B.x - A.x) / distance;
			double unitY = (double) (B.y - A.y) / distance;

			polygon.xpoints[0] = (float) (A.x + unitY * p1Width);
			polygon.xpoints[1] = (float) (B.x + unitY * p2Width);
			polygon.xpoints[2] = (float) (B.x - unitY * p2Width);
			polygon.xpoints[3] = (float) (A.x - unitY * p1Width);

			polygon.ypoints[0] = (float) (A.y + unitX * p1Width);
			polygon.ypoints[1] = (float) (B.y + unitX * p2Width);
			polygon.ypoints[2] = (float) (B.y - unitX * p2Width);
			polygon.ypoints[3] = (float) (A.y - unitX * p1Width);
		}
	}

	/**
	 * Draws this Object3D onto the specified Graphics.
	 * 
	 * @param g
	 *            the Graphics on which to draw this 3D object
	 */
	public void drawOnThis(Graphics2D g) {
		if (drawAsSimpleLine)
			super.drawOnThis(g);
		// If the rotated Vector3D is not drawable on the screen, the x
		// coordinate of pointToPutResultIn gets set to Integer.MIN_VALUE by the
		// method Window.getPixelFromTranslatedVector3D
		else if (A.x != Integer.MIN_VALUE && B.x != Integer.MIN_VALUE) {
			g.setColor(Object3DViewer.shadeColor(color, centerPoint.z));
			g.fill(polygon);
		}
	}
}
