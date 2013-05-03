package operators;

import parser.ExpressionNode;
import parser.Value;
import valueTypes.DecimalValue;
import valueTypes.ErrorValue;
/**
 * The factorial operator. For example, 4! yields 24 (4*3*2*1) 
 * @author Curran Kelleher
 *
 */
public class Factorial extends UnaryOperator {
	/**
	 * The persistant result, a memory optimization for repeated evaluations.
	 */
	DecimalValue persistantResult = new DecimalValue(0);
	
	/**
	 * Construct a Factorial which will operate on the result from the specified child.
	 * @param child the evaluation tree whose result will be operated on by the this Factorial.
	 */
	public Factorial(ExpressionNode child) {
		super(child);
	}

	/**
	 * The method which evaluates the operator as a node in an evaluation tree.
	 * @return the value resulting from the evaluation of the operation.
	 */
	public Value evaluate() {
		Value childValue = child.evaluate();
		if(childValue instanceof DecimalValue)
		{
			double val = ((DecimalValue)childValue).value;
			if(val - (int)val == 0)
			{
				int result = 1;

				for (int i = 1; i <= val; i++)
					result*= i;
				
				persistantResult.value = result;
				
				return persistantResult;
			}
		}
		return new ErrorValue("the factorial operator is only valid for integers, not "+childValue.getType()+", so ("+childValue+")! could not be evaluated");
	}

	/**
	 * Returns a String representation of this Object.
	 */
	public String toString()
	{
		return "("+child+")!";
	}
}
