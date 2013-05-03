package generalMathClasses;

/**
 * Class used to store a coordinate-space, which is simply xMin, xMax, yMin,
 * yMax.
 * 
 * @author Curran Kelleher
 * 
 */
public class CoordinateSpace2D {

	/**
	 * The minimum x value in the coordinate space.
	 */
	public double xMin;

	/**
	 * The maximum x value in the coordinate space.
	 */
	public double xMax;

	/**
	 * The minimum y value in the coordinate space.
	 */
	public double yMin;

	/**
	 * The maximum y value in the coordinate space.
	 */
	public double yMax;

	/**
	 * Constructs a new CoordinateSpace2D with the specified paramaters.
	 * 
	 * @param Xm
	 *            The minimum x value in the coordinate space.
	 * @param xM
	 *            The maximum x value in the coordinate space.
	 * @param Ym
	 *            The minimum y value in the coordinate space.
	 * @param yM
	 *            The maximum y value in the coordinate space.
	 */
	public CoordinateSpace2D(double Xm, double xM, double Ym, double yM) {

		xMin = Xm;
		xMax = xM;
		yMin = Ym;
		yMax = yM;
	}

	/**
	 * Sets the coordinate space
	 * 
	 * @param Xm
	 *            The minimum x value in the coordinate space.
	 * @param xM
	 *            The maximum x value in the coordinate space.
	 * @param Ym
	 *            The minimum y value in the coordinate space.
	 * @param yM
	 *            The maximum y value in the coordinate space.
	 */
	public void set(double Xm, double xM, double Ym, double yM) {
		xMin = Xm;
		xMax = xM;
		yMin = Ym;
		yMax = yM;
	}

	/**
	 * Returns a string representation of this Coordinate space, in the form
	 * "[xMin,xMax,yMin,yMax]"
	 */
	public String toString() {
		return "[" + xMin + "," + xMax + "," + yMin + "," + yMax + "]";
	}
}
