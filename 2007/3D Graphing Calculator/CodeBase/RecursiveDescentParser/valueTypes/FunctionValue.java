package valueTypes;

import parser.ExpressionNode;
import parser.Value;

/**
 * A Value type which contains a function (an evaluation tree).
 * 
 * @author Curran Kelleher
 * 
 */
public class FunctionValue extends Value {
	ExpressionNode[] statements;

	/**
	 * Construct a FunctionValue with the specified evaluation tree as it's
	 * function.
	 * 
	 * @param function
	 */
	public FunctionValue(ExpressionNode function) {
		if (function instanceof CharacterSeparatedStatementPair) {
			CharacterSeparatedStatementPair pair = (CharacterSeparatedStatementPair) function;
			if (pair.getSymbol().equals(";")) {
				statements = pair.extractAllStatements();
				
				
			} else {
				statements = new ExpressionNode[1];
				statements[0] = new ErrorValue(
						"only statements which are separated by semicolons ';' are allowed as statements within functions. '"
								+ pair.getSymbol()
								+ "' is an invalid statement separator.");
			}
		} else {
			statements = new ExpressionNode[1];
			statements[0] = function;
		}
	}

	

	/**
	 * Gets a human readable desctiption of this type.
	 * 
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "function";
	}

	/**
	 * @return a string representation of this FunctionValue.
	 */
	public String toString() {
		return toParseableString();
	}
	
	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 * evaluated, generates this value.
	 */
	public String toParseableString() {
		StringBuffer b = new StringBuffer();
		b.append("{");
		for (int i = 0; i < statements.length; i++)
			b.append(statements[i].toString()
					+ ((i < statements.length - 1) ? "; " : ""));
		b.append("}");
		return b.toString();
	}

	public boolean equals(Object o) {
		return o instanceof FunctionValue ? toString().equals(
				((FunctionValue) o).toString()) : false;
	}

	/**
	 * Executes the function within this FunctionValue
	 * 
	 * @return
	 */
	public Value executeFunction() {
		Value v;
		for (int i = 0; i < statements.length; i++)
			if((v=statements[i].evaluate()) instanceof ErrorValue)
				return v;
		return NullValue.NULL;
	}
}
