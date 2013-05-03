package valueTypes;

import parser.Value;

/**
 * The Value representing decimal numbers.
 * 
 * @author Curran Kelleher
 * 
 */
public class DecimalValue extends Value implements Comparable<DecimalValue> {
	public double value;

	/**
	 * Constructs a DecimalValue initialized with the specified double value.
	 * 
	 * @param value
	 *            the value this DecimalValue will have.
	 */
	public DecimalValue(double value) {
		this.value = value;
	}

	/**
	 * @return a string representation of this DecimalValue.
	 */
	public String toString() {
		return toParseableString();
	}
	
	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public String toParseableString() {
		return ((value%1==0)?"" + (int)value:"" + value);
	}

	/**
	 * Gets a human readable desctiption of this type.
	 * 
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "decimal";
	}

	/**
	 * Implementation of compareTo for DecimalValue objects.
	 * @see Comparable
	 * @param o
	 * @return
	 */
	public int compareTo(DecimalValue o) {
		return value == o.value ? 0 : (value < o.value ? -1 : 1);
	}

	/**
	 * Implementation of equals for DecimalValue objects.
	 * @param o
	 * @return
	 */
	public boolean equals(Object o) {
		return o instanceof DecimalValue?value == ((DecimalValue) o).value:false;
	}

	/**
	 * Extracts the value out of the specified Value if it is a DecimalValue. 
	 * @param value the value to extract the value from
	 * @return the double value of the specified Value, or 0 if the specified Value is not a DecimalValue.
	 */
	public static double extractDoubleValue(Value value) {
		return value instanceof DecimalValue?((DecimalValue)value).value:0;
	}
}
