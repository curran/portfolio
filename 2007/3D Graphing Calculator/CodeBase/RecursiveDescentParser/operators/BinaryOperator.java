package operators;

import parser.ExpressionNode;
/**
 * The base class for all binary operators.
 * @author Curran Kelleher
 *
 */
public abstract class BinaryOperator extends ExpressionNode {
	protected ExpressionNode leftChild,rightChild;
	protected String symbol;
	/**
	 * Constructs a binary operator which operates on the specified left-child and right-child evaluation trees.
	 * @param leftChild the left-child evaluation tree
	 * @param rightChild the right-child evaluation tree
	 */
	protected BinaryOperator(String symbol,ExpressionNode leftChild,ExpressionNode rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.symbol = symbol;
	}
	/**
	 * @return a string representation of this BinaryOperator.
	 */
	public String toString()
	{
		return "("+leftChild+" "+symbol+" "+rightChild+")";
	}
	
	/**
	 * Gets the symbol of this BinaryOperator.
	 * @return the symbol of this BinaryOperator.
	 */
	public String getSymbol()
	{
		return symbol;
	}
}
