package valueTypes;

import parser.Value;

/**
 * The Value representing string literals.
 * 
 * @author Curran Kelleher
 */
public class StringValue extends Value {
	String value;

	/**
	 * Constructs a StringValue initialized with the specified value.
	 * 
	 * @param value
	 *            the value this Object will have.
	 */
	public StringValue(String value) {
		this.value = value;
	}

	/**
	 * Gets a human readable desctiption of this type.
	 * 
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "string";
	}

	/**
	 * @return the string of this StringValue.
	 */
	public String toString() {
		return value;
	}
	
	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public String toParseableString() {
		return "\""+value+"\"";
	}
	
	public boolean equals(Object o) {
		return o instanceof StringValue?toString().equals(((StringValue) o).toString()):false;
	}

}
