package parser;

import valueTypes.CharacterSeparatedStatementPair;
import valueTypes.ErrorValue;

/**
 * This class represents a list of expressions. It is created when the parser
 * encounters an expression of the form "[1,2,3]". Anything could be put in
 * there, for example "[a,b,c]", or "[5^5,b-w,77,true && (true || false),"you're
 * ugly"]"
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionList extends Value {
	/**
	 * The list of expressions held in this ExpressionList
	 */
	ExpressionNode[] nodes;

	/**
	 * Constructs an ExpressionList from a tree of comma separated statements.
	 * 
	 * @param node
	 *            a tree of comma separated statements
	 */
	private ExpressionList(ExpressionNode[] nodes) {
		this.nodes = nodes;
	}

	/**
	 * This method creates an ExpressionList from either a single expression
	 * or a CharacterSeparatedStatementPair with a comma as it's separator.
	 * 
	 * @param node
	 *            the node to extract the list from
	 * @return an ExpressionList if successful, or an ErrorValue if not
	 */
	public static Value extractExpressionList(ExpressionNode node) {
		if (node instanceof CharacterSeparatedStatementPair) {
			CharacterSeparatedStatementPair listOfExpressions = (CharacterSeparatedStatementPair) node;
			if (listOfExpressions.getSymbol().equals(",")) {
				ExpressionNode[] allExpressions = ((CharacterSeparatedStatementPair) node)
						.extractAllStatements();
				// check for errors
				if (allExpressions.length == 1)
					if (allExpressions[0] instanceof ErrorValue)
						return (ErrorValue)allExpressions[0];

				// if we are here then there are no errors
				return new ExpressionList(allExpressions);
			} else
				return new ErrorValue(
						"The symbol \""
								+ listOfExpressions.getSymbol()
								+ "\" is an invalid element separator when creating a list");
		} else {
			// if we are here then there is only one expression in the list
			ExpressionNode[] allExpressions = { node };
			return new ExpressionList(allExpressions);
		}
	}
	
	/**
	 * 
	 * @return The list of expressions held in this ExpressionList
	 */
	public ExpressionNode[] getNodes()
	{
		return nodes;
	}

	/**
	 * Gets a human readable desctiption of the type of this Value.
	 */
	public String getType() {
		return "List of expressions";
	}

	/**
	 * Creates a String representation of this ExpressionList.
	 */
	public String toString()
	{
		return toParseableString();
	}
	
	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public String toParseableString() {
		StringBuffer buff = new StringBuffer();
		buff.append("[");
		for(int i=0;i<nodes.length;i++)
			buff.append(nodes[i].toString()+(i<nodes.length-1?", ":""));
		buff.append("]");
		return buff.toString();
	}
}
