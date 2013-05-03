package variables;

import parser.Value;
import valueTypes.ErrorValue;

/**
 * The Constant class represents "variables" whose values cannot be changed.
 * 
 * @author Curran Kelleher
 * @see Variable
 */
public class Constant extends Variable {

	/**
	 * A human-readable description of this constant.
	 */
	String description;

	/**
	 * Constructs a Constant with the specified name and permanent value.
	 * 
	 * @param name
	 *            the name of the constant (for example "pi")
	 * @param value
	 *            the permanent value of the constant
	 * @param description
	 *            A human-readable description of this constant.
	 */
	protected Constant(String name, Value value, String description) {
		super(name);
		this.value = value;
		this.description = description;
	}

	/**
	 * Returns an ErrorValue if this method is called, as constants cannot be
	 * assigned.
	 * 
	 * @param value
	 *            the value the caller is attempting to set this variable to.
	 * @return an error message stating that this constant cannot be assigned a
	 *         new value.
	 */
	public Value set(Value value) {
		return new ErrorValue("the constant " + name
				+ " cannot be assigned a new value.");
	}

	/**
	 * Creates a global Constant with the specified name and permanent value.
	 * This constant behaves just like a variable, except that it can never be
	 * assigned a new value.
	 * 
	 * @param name
	 *            the name of the constant (for example "pi")
	 * @param value
	 *            the permanent value of the constant
	 * @param description
	 *            A human-readable description of this constant.
	 * @return true if successful, false if unsuccessful (that is, if a Constant
	 *         with the same name has already been defined, and the new constant
	 *         was not created).
	 */
	public static boolean createConstant(String name, Value value,
			String description) {
		Object variable = variables.get(name);
		if (variable != null)
			if (variable instanceof Constant)
				return false;

		// if we are here then we are creating new variable
		Variable newVariable = new Constant(name, value, description);
		variables.put(name, newVariable);
		return true;
	}

	/**
	 * 
	 * @return a human-readable description of this constant.
	 */
	public String getDescription() {
		return description;
	}

}
