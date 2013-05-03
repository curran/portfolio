package valueTypes;

import parser.Value;

/**
 * The Value representing null.
 * @author Curran Kelleher
 *
 */
public class NullValue extends Value {

	/**
	 * The only one static instance of NullValue which should be used in all cases.
	 */
	public static final Value NULL = new NullValue();

	/**
	 * Private constructor, because NullValue.NULL should always be used instead of creating a new NullValue.
	 *
	 */
	private NullValue(){}
	
	/**
	 * Gets a human readable desctiption of this type.
	 * 
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "null";
	}
	
	/**
	 * @return a string representation of this NullValue.
	 */
	public String toString()
	{
		return toParseableString();
	}
	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public String toParseableString() {
		return "null";
	}

}
