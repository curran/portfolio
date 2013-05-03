package operators;

import parser.ExpressionNode;
import valueTypes.FunctionValue;
/**
 * The base class for all unary operators.
 * @author Curran Kelleher
 *
 */
public abstract class UnaryOperator extends ExpressionNode {
	protected ExpressionNode child;
	/**
	 * Creates a UnaryOperator.
	 * @param child The evaluation tree whose resulting valuee will be the argument for the operator.
	 */
	protected UnaryOperator(ExpressionNode child)
	{
		this.child = child;
	}
	
	/**
	 * Subclasses which work with associated functions should implement this method.
	 * For example, in the statement "if(a<0){a = 0}", {a = 0} is the associated function to the operator "if".
	 * @param associatedFunction the associated function
	 * @return true if successful, false if unsuccessful or if associated functions are not supported by this operator. Returns false by default, only returns true if implemented by a subclass.
	 */
	public boolean linkAssociatedFunction(FunctionValue associatedFunction) {
		return false;
	}
}
