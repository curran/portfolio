package valueTypes;

import java.awt.Color;

import parser.Value;

/**
 * The Value representing Colors.
 * 
 * @author Curran Kelleher
 */
public class ColorValue extends Value {
	/**
	 * The actual Color contained in this ColorValue.
	 */
	public Color value;

	/**
	 * Constructs a ColorValue initialized with the specified Color.
	 * 
	 * @param value
	 *            the value this DecimalValue will have.
	 */
	public ColorValue(Color value) {
		set(value);
	}

	/**
	 * Constructs a ColorValue initialized with the Color specified by the r, g,
	 * and b values.
	 * 
	 * @param r
	 *            the red value of the color, between 0 and 1
	 * @param g
	 *            the green value of the color, between 0 and 1
	 * @param b
	 *            the blue value of the color, between 0 and 1
	 */
	public ColorValue(double r, double g, double b) {
		set(r, g, b);
	}

	/**
	 * Gets a human readable desctiption of this type.
	 * 
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "Color";
	}

	/**
	 * @return the Color of this ColorValue.
	 */
	public Color toColor() {
		return value;
	}

	public String toString() {
		return toParseableString();
	}

	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 *         evaluated, generates this value.
	 */
	public String toParseableString() {
		return generateParseableString(value);
	}

	public boolean equals(Object o) {
		return o instanceof ColorValue ? value.equals(((ColorValue) o).value)
				: false;
	}

	/**
	 * Extracts the Color out of the specified Value if it is a ColorValue.
	 * Otherwise, gray is returned.
	 * 
	 * @param value
	 *            the Value to extract the Color out of.
	 * @return the Color in the value, or Color.GRAY if the value is not a
	 *         ColorValue.
	 */
	public static Color extractColor(Value value) {
		return value instanceof ColorValue ? ((ColorValue) value).value
				: Color.GRAY;
	}

	/**
	 * Sets the Color in thie value to the Color defined by the specified r, g,
	 * and b values (which range from 0 to 1, not 0 to 255).
	 * 
	 * @param r
	 *            the red value of the color, between 0 and 1
	 * @param g
	 *            the green value of the color, between 0 and 1
	 * @param b
	 *            the blue value of the color, between 0 and 1
	 */
	public void set(double r, double g, double b) {
		set(new Color((int) (255 * (r > 1 ? 1 : r < 0 ? 0 : r)),
				(int) (255 * (g > 1 ? 1 : g < 0 ? 0 : g)),
				(int) (255 * (b > 1 ? 1 : b < 0 ? 0 : b))));
	}

	/**
	 * Sets the Color in thie value to the Color defined by the specified r, g,
	 * and b values (which range from 0 to 1, not 0 to 255).
	 * 
	 * @param r
	 *            the red value of the color, between 0 and 1
	 * @param g
	 *            the green value of the color, between 0 and 1
	 * @param b
	 *            the blue value of the color, between 0 and 1
	 * @param a
	 *            the alpha, or opacity, value of the color, between 0 and 1
	 */
	public void set(double r, double g, double b, double a) {
		set(new Color((int) (255 * (r > 1 ? 1 : r < 0 ? 0 : r)),
				(int) (255 * (g > 1 ? 1 : g < 0 ? 0 : g)),
				(int) (255 * (b > 1 ? 1 : b < 0 ? 0 : b)),
				(int) (255 * (a > 1 ? 1 : a < 0 ? 0 : a))));
	}

	/**
	 * Sets the Color in this ColorValue
	 * 
	 * @param value
	 *            the new Color for this value.
	 */
	public void set(Color value) {
		this.value = value;
	}

	/**
	 * Generates a string which, when parsed, will generate a ColorValue
	 * containing the specified color
	 * 
	 * @param color
	 * @return
	 */
	public static String generateParseableString(Color color) {
		return "createColor("
				+ (double) color.getRed()
				/ 255
				+ ","
				+ (double) color.getGreen()
				/ 255
				+ ","
				+ (double) color.getBlue()
				/ 255
				+ (color.getAlpha() != 255 ? ("," + (double) color.getAlpha() / 255)
						: "") + ")";
	}
}
