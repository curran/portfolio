package valueTypes;

import parser.Value;

/**
 * The Value representing decimal numbers.
 * 
 * @author Curran Kelleher
 * 
 */
public class BooleanValue extends Value {
	public boolean value;

	/**
	 * Constructs a BooleanValue initialized with the specified double value.
	 * @param value the value this DecimalValue will have.
	 */
	public BooleanValue(boolean value) {
		this.value = value;
	}

	/**
	 * @return a string representation of this BooleanValue.
	 */
	public String toString() {
		return toParseableString();
	}
	
	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public String toParseableString() {
		return "" + value;
	}

	/**
	 * Gets a human readable desctiption of this type.
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "boolean";
	}
	
	/**
	 * Tests for equality to another BooleanValue.
	 */
	public boolean equals(Object o) {
		return o instanceof BooleanValue?value == ((BooleanValue) o).value:false;
	}

	/**
	 * Extracts the value out of the specified Value if it is a BooleanValue. 
	 * @param value the value to extract the value from
	 * @return the boolean value of the specified Value, or false if the specified Value is not a BooleanValue.
	 */
	public static boolean extractBooleanValue(Value value) {
		return value instanceof BooleanValue?((BooleanValue)value).value:false;
	}
}
