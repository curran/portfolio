package operators;

import parser.ExpressionNode;
import parser.Value;
import valueTypes.DecimalValue;
import valueTypes.ErrorValue;

/**
 * An abstract BinaryOperator which will deal exclusively with real numbers
 * (DecimalValue objects) as operands. It is indended to be subclassed easily to
 * generate custom operators. All the subclass needs to do is define a method
 * double evaluate(double l,double r) which performs the operation on two double
 * values.
 * 
 * RealNumberBinaryOperator provides optimization for repeated evaluations by
 * having each instance hold a persistant Value which gets set to the result of
 * the operation and returned each time the operator is evaluated.
 * Alternatively, one could create new Value objects each time the operator is
 * evaluated, but this would put the garbage collector to work, resulting in
 * poor performance.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class RealNumberBinaryOperator extends BinaryOperator {
	/**
	 * The persistant result, a memory optimization for repeated evaluations.
	 */
	DecimalValue persistantValue = new DecimalValue(0);

	/**
	 * The persistant left and right values, a memory optimization for repeated evaluations.
	 */
	Value leftValue, rightValue;


	/**
	 * Constructs a real number binary operator which operates on the specified left-child and right-child evaluation trees.
	 * 
	 * @param symbol the symbol of the operator ('+' for the plus operator) This is necessary for generating informative error messages.
	 * @param leftChild the left-child evaluation tree
	 * @param rightChild the right-child evaluation tree
	 */
	protected RealNumberBinaryOperator(char symbol, ExpressionNode leftChild,
			ExpressionNode rightChild) {
		super(""+symbol,leftChild, rightChild);
	}

	/**
	 * The method which evaluates the operator.
	 * @return the resulting Value
	 */
	public Value evaluate() {
		leftValue = leftChild.evaluate();
		rightValue = rightChild.evaluate();
		try {
			persistantValue.value = evaluate(((DecimalValue) leftValue).value,
					((DecimalValue) rightValue).value);
			return persistantValue;
		} catch (Exception e) {
			return new ErrorValue("'" + symbol
					+ "' is not a valid operator for the types "
					+ leftValue.getType() + " and " + rightValue.getType()
					+ ", so " + leftValue + " " + symbol + " " + rightValue
					+ " could not be evaluated");
		}
	}
	
	
	
	/**
	 * The method which performs the operation on two double values.
	 * 
	 * @param l the value on the left of the operator (a in a+b)
	 * @param r the value on the right of the operator (b in a+b)
	 * @return the value resulting from the operation.
	 */
	abstract double evaluate(double l, double r);
}
