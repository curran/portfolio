package grapher2D;

import generalMathClasses.CoordinateSpace2D;

/**
 * A class of constants which define the names to be used for
 * programmatically-accessed and used Variables used by RecursiveDescentParser.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher2DConstants {

	/**
	 * The name of the variable which stores the string of the funciton used by
	 * the 2D Grapher
	 */
	public static final String Grapher2DFunctionString = "grapher2DFunctionString";

	/**
	 * The name of the variable which stores a boolean flag signaling whether or
	 * not the view should be animating.
	 */
	public static final String Grapher2DAnimateFlag = "grapher2DAnimation";

	/**
	 * The name of the Variable which stores the number of points which will be
	 * used to generate the graph on the screen.
	 */
	public static final String Grapher2DResolution = "grapher2DResolution";

	/**
	 * The coordinate space, or "window", that the graph will be viewed in.
	 */
	public static final CoordinateSpace2D coordinateSpace = new CoordinateSpace2D(
			-10, 10, -10, 10);

	/**
	 * The name of the variable which stores the amount by which the t Variable
	 * will be incremented by each frame. So every frame, t = t + [the variable
	 * whose name is this String]
	 */
	public static final String TimeIncrement = "tIncrement";

}
