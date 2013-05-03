package operators;

import parser.ExpressionNode;

/**
 * The '-' operator creator. It is a special case because it could be either a binary or a unary operator, and needs to be dealt with directly by the parser.
 * 
 * @author Curran Kelleher
 * 
 */
public class Minus extends BinaryOperatorCreator {

	/**
	 * Constructs a Minus, ready to be used as a BinaryOperatorCreator. It's precedence is 1.
	 *
	 */
	public Minus() {
		super(1);
	}

	/**
	 * Creates a new instance of the '-' binary operator which will operate on the result from the specified left-child and right-child evaluation trees.
	 * 
	 * @param left the left-child evaluation tree
	 * @param right the right-child evaluation tree
	 * @return a new instance of the '-' BinaryOperator
	 */
	public BinaryOperator create(ExpressionNode left, ExpressionNode right) {
		return new RealNumberBinaryOperator('-',left,right){
			double evaluate(double l, double r) {
				return l-r;
			}};
	}
	
	/**
	 * Creates a new instance of the '-' unary operator which will operate on the result from the specified child evaluation tree.
	 * 
	 * @param child the child evaluation tree
	 * @return a new instance of the '-' UnaryOperator
	 */
	public static UnaryOperator createUnaryMinus(ExpressionNode child)
	{
		return new RealNumberUnaryOperator("-",child){
			double evaluate(double x) {
				return -x;
			}};
	}
	
	public String getDescription() {
		return "the negation or subtraction operator. For example -5 yields -5, 5-2 yields 3";
	}
}
