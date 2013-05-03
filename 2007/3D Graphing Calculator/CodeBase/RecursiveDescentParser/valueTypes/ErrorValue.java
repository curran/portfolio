package valueTypes;

import parser.Value;
/**
 * A Value whose purpose is to report error messages.
 * @author Curran Kelleher
 *
 */
public class ErrorValue extends Value {

	String message;
	/**
	 * Constructs an ErrorValue with the specified message.
	 * @param message the error message
	 */
	public ErrorValue(String message) {
		this.message = message;
	}

	/**
	 * @return the message contained in this ErrorValue.
	 */
	public String toString()
	{
		return message;
	}
	
	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public String toParseableString() {
		return "createErrorValue(\""+message+"\")";
	}

	/**
	 * Gets a human readable desctiption of this type.
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "error message";
	}
	
	public boolean equals(Object o) {
		return o instanceof ErrorValue?toString().equals(((ErrorValue) o).toString()):false;
	}
}
