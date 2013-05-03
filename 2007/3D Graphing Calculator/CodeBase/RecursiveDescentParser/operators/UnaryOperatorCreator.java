package operators;

import parser.ExpressionNode;

/**
 * A class designed for subclasses to define a method which creates a custom
 * unary operator.
 * 
 * The reason for having this class is so that each time a unary operator is
 * used in an expression, a new instance of the UnaryOperator which evaluates
 * the operation will be created for that particular node in the evaluation
 * tree.
 * 
 * This allows the potential of optimization for repeated evaluations by having
 * each UnaryOperator hold a persistant Value which gets set to the result of
 * the operation and returned each time the operator is evaluated.
 * Alternatively, one could create new Value objects each time the operator is
 * evaluated, but this would put the garbage collector to work, resulting in
 * poor performance.
 * 
 * @author Curran Kelleher
 * 
 */
public interface UnaryOperatorCreator {

	/**
	 * The type constant which represents the fact that the unary operator which
	 * this UnaryOperatorCreator creates can be generally classified as a
	 * mathematical operator.
	 */
	int MATHEMATICAL = 1;

	/**
	 * The type constant which represents the fact that the unary operator which
	 * this UnaryOperatorCreator creates can be generally classified as a
	 * construct of the intrepreted language.
	 */
	int LANGUAGE_CONSTRUCT = 2;

	/**
	 * The type constant which represents the fact that the unary operator which
	 * this UnaryOperatorCreator creates is a command which performs some action
	 * when evaluated.
	 */
	int COMMAND = 3;

	/**
	 * The type constant which represents the fact that the unary operator which
	 * this UnaryOperatorCreator creates cannot be generally classified as
	 * anything in perticular.
	 */
	int MISCELLANEOUS = 4;

	/**
	 * Creates a new instance of a Unary operator (defined by a subclass) which
	 * will operate on the result from the specified arguments (which are
	 * evaluation trees).
	 * 
	 * @param child
	 *            The argument passed to the operator.
	 * @return a new instance of the UnaryOperator (defined by a subclass)
	 */
	public UnaryOperator create(ExpressionNode child);

	/**
	 * Gets the type flag which denotes which type of operator the unary
	 * operator which this UnaryOperatorCreator creates can be generally
	 * classified as.
	 * 
	 * @return UnaryOperatorCreator.MATHEMATICAL, UnaryOperatorCreator.LANGUAGE_CONSTRUCT, UnaryOperatorCreator.COMMAND or UnaryOperatorCreator.MISCELLANEOUS
	 */
	public int getType();

	/**
	 * 
	 * @return a human-readable description of the unary operator which this
	 *         UnaryOperatorCreator creates.
	 */
	public String getDescription();
}
