package grapher3D.controller;

/**
 * Specifies an interface for translating functions in a given coordinate system
 * (such as cartesian or spherical) into parametric surface functions, which
 * assign x, y, and z in terms of u and v alone.
 * 
 * @author Curran Kelleher
 * 
 */
public class CoordinateSystemSpecification {
	/**
	 * Specifies the parametric surface function where '#' will be replaced by
	 * the function in the coordinate space.
	 */
	String translationString;

	/**
	 * Constructs a CoordinateSystemSpecification which will use the specified
	 * String, which specifies the parametric surface function where '#' will be
	 * replaced by the function in the coordinate space.
	 * 
	 * @param translationString Specifies the parametric surface function where '#' will be replaced by
	 * the function in the coordinate space.
	 */
	public CoordinateSystemSpecification(String translationString) {
		this.translationString = translationString;
	}

	/**
	 * Generates a parametric surface function which describes the function in
	 * coordinateSystemFunctionString in terms of u and v, based on the external
	 * function string, which specified dependent variables in a particular
	 * coordinate system.
	 * 
	 * @param coordinateSystemFunctionString
	 *            the external function string, which specified dependent
	 *            variables in a particular coordinate system.
	 * @return a parametric surface function which describes the function in
	 *         coordinateSystemFunctionString in terms of u and v.
	 */
	String generateInternalFunction(String coordinateSystemFunctionString) {
		return translationString
				.replaceAll("#", coordinateSystemFunctionString);
	}
}
