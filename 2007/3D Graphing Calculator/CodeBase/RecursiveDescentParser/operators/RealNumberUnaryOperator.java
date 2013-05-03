package operators;

import parser.ExpressionNode;
import parser.Value;
import valueTypes.DecimalValue;
import valueTypes.ErrorValue;

/**
 * An abstract UnaryOperator which will deal exclusively with real numbers
 * (DecimalValue objects) as operands, and only one argument (for example, the
 * operator d(x,y,z) could not be created using this class). It is indended to
 * be subclassed easily to generate custom operators. All the subclass needs to
 * do is define a method double evaluate(double x) which performs the operation
 * on a double value.
 * 
 * RealNumberUnaryOperator provides optimization for repeated evaluations by
 * having each instance hold a persistant Value which gets set to the result of
 * the operation and returned each time the operator is evaluated.
 * Alternatively, one could create new Value objects each time the operator is
 * evaluated, but this would put the garbage collector to work, resulting in
 * poor performance.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class RealNumberUnaryOperator extends UnaryOperator {
	/**
	 * The persistant result, a memory optimization for repeated evaluations.
	 */
	DecimalValue persistantValue = new DecimalValue(0);

	/**
	 * The persistant value of the child evaluation, a memory optimization for
	 * repeated evaluations.
	 */
	Value childValue;

	/**
	 * The name of the operator ("sin" for the sine function) This is necessary
	 * for generating informative error messages.
	 */
	String name;

	/**
	 * Creates a RealNumberUnaryOperator.
	 * 
	 * @param name
	 *            the name of the operator ("sin" for the sine function) This is
	 *            necessary for generating informative error messages.
	 * 
	 * @param child
	 *            The evaluation tree whose resulting valuee will be the
	 *            argument for the operator.
	 */
	public RealNumberUnaryOperator(String name, ExpressionNode child) {
		super(child);
		this.name = name;
	}

	/**
	 * The method which evaluates the operator.
	 * 
	 * @return the resulting Value
	 */
	public Value evaluate() {
		childValue = child.evaluate();
		try {
			persistantValue.value = evaluate(((DecimalValue) childValue).value);
			return persistantValue;
		} catch (Exception e) {
			return new ErrorValue("'" + name
					+ "' is not a valid operator for the type "
					+ childValue.getType() + ", so " + name + "( " + childValue
					+ " )could not be evaluated");
		}
	}

	/**
	 * @return a string representation of this RealNumberUnaryOperator.
	 */
	public String toString() {
		return name + "(" + child + ")";
	}

	/**
	 * The method which evaluates the operator on a double value.
	 * 
	 * @param x
	 *            the operand
	 * @return the value resulting from the operation
	 */
	abstract double evaluate(double x);
}