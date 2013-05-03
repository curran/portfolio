package variables;

import valueTypes.BooleanValue;
import valueTypes.DecimalValue;
import valueTypes.NullValue;

/**
 * A class which provides the utility method initializeConstants() for setting
 * up all constants.
 * 
 * @author Curran Kelleher
 * 
 */
public class Constants {
	private static boolean constantsHaveBeenInitialized = false;

	/**
	 * Initializes the standard constants. These include: pi, e, Infinity, true,
	 * false
	 * 
	 * @see Constant
	 */
	public static void initializeConstants() {
		if (!constantsHaveBeenInitialized) {
			Constant.createConstant("pi", new DecimalValue(Math.PI),"the ratio of circumference to diameter of a circle");
			Constant.createConstant("e", new DecimalValue(Math.E),"the base of natural logarithms, often called Euler's number or Napier's constant");
			Constant.createConstant("Infinity", new DecimalValue(
					Double.POSITIVE_INFINITY),"the value representing positive infinity. It will behave like infinity in calculations");
			Constant.createConstant("true", new BooleanValue(true),"the boolean value true");
			Constant.createConstant("false", new BooleanValue(false),"the boolean value false");
			Constant.createConstant("null", NullValue.NULL,"the value representing null, or nothing");

			constantsHaveBeenInitialized = true;
		}
	}

}
