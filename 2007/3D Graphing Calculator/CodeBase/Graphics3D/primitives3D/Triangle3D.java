package primitives3D;

import java.awt.Color;

/**
 * A 3D Triangle.
 * 
 * @author Curran Kelleher
 * 
 */
public class Triangle3D extends Polygon3D {

	/**
	 * Construct a triangle with the specified vertices and color.
	 * 
	 * @param a
	 *            the first vertex of the triangle
	 * @param b
	 *            the second vertex of the triangle
	 * @param c
	 *            the third vertex of the triangle
	 * @param color
	 */
	public Triangle3D(Vector3D a, Vector3D b, Vector3D c, Color color) {
		super(makeVector3DArray(a,b,c),color);
	}

	private static Vector3D[] makeVector3DArray(Vector3D a, Vector3D b, Vector3D c) {
		Vector3D[] array = {a,b,c};
		return array;
	}
}
