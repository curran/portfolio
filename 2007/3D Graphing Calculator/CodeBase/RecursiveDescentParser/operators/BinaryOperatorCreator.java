package operators;

import parser.ExpressionNode;

/**
 * A class designed for subclasses to define a method which creates a custom
 * binary operator.
 * 
 * The reason for having this class is so that each time a binary operator is
 * used in an expression, a new instance of the BinaryOperator which evaluates
 * the operation will be created for that particular node in the evaluation
 * tree.
 * 
 * This allows the potential of optimization for repeated evaluations by having
 * each BinaryOperator hold a persistant Value which gets set to the result of
 * the operation and returned each time the operator is evaluated.
 * Alternatively, one could create new Value objects each time the operator is
 * evaluated, but this would put the garbage collector to work, resulting in
 * poor performance.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class BinaryOperatorCreator {
	
	/**
	 * The precedence of the operator that this BinaryOperatorCreator will be creating.
	 */
	public final int precedence;

	/**
	 * Constructs a BinaryOperatorCreator for a binary operator which has the
	 * specified precedence.
	 * 
	 * For example, + and - have precedence 1; *,/, and % have precedence 2; ^
	 * has precedence 3; etc.
	 * 
	 * @param precedence
	 *            the precedence of the binary operator which this
	 *            BinaryOperatorCreator will be creating. Precedence defines the
	 *            order of operations.
	 */
	protected BinaryOperatorCreator(int precedence) {
		this.precedence = precedence;
	}

	/**
	 * Creates a new instance of a binary operator (defined by a subclass) which
	 * will operate on the result from the specified left-child and right-child
	 * evaluation trees.
	 * 
	 * @param leftChild
	 *            the left-child evaluation tree
	 * @param rightChild
	 *            the right-child evaluation tree
	 * @return a new instance of the BinaryOperator (defined by a subclass)
	 */
	public abstract BinaryOperator create(ExpressionNode leftChild,
			ExpressionNode rightChild);

	/**
	 * 
	 * @return a human-readable description of the binary operator which this
	 *         BinaryOperatorCreator creates.
	 */
	public abstract String getDescription();
}
