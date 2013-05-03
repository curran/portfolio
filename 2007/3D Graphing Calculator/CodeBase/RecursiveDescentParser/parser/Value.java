package parser;

/**
 * The base class for all value types.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class Value extends ExpressionNode {

	/**
	 * Gets a human readable desctiption of the type of this Value.
	 * 
	 * @return a human readable desctiption of the type of this Value.
	 */
	public abstract String getType();

	/**
	 * Returns this. This method is required as Value inherits from
	 * ExpressionNode.
	 */
	public Value evaluate() {
		return this;
	}

	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public abstract String toParseableString();
}
