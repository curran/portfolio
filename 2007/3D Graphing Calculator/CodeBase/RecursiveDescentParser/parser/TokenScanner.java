package parser;

import java.util.Map;

import valueTypes.DecimalValue;
import valueTypes.ErrorValue;
import valueTypes.StringValue;
import variables.Variable;

/**
 * The class whose purpose is to scan for tokens in the expression string. This
 * is only meant to be used by the RecursiveDescentParser class.
 * 
 * @author Curran Kelleher
 * 
 */
public class TokenScanner {

	// the current index in the expression string being parsed
	public int currentCharIndex = 0;

	// the current expression String being parsed
	public String expressionStr;

	// all present binary operators
	// the type is <String symbol,BinaryOperatorCreator operator>
	Map binaryOperators;

	// all present unary operators
	// the type is <String name,BinaryOperatorCreator operator>
	Map unaryOperators;

	/**
	 * Constructs a TokenScanner for the specified expression string, and with
	 * references to the available operators.
	 * 
	 * @param binaryOperators
	 *            the available binary operators
	 * @param unaryOperators
	 *            the available unary operators
	 * @param expression
	 *            the expression string
	 */
	public TokenScanner(Map binaryOperators, Map unaryOperators,
			String expression) {
		this.binaryOperators = binaryOperators;
		this.unaryOperators = unaryOperators;
		expressionStr = expression;
	}

	/**
	 * Scans for the next token, and returns it's interpreted value.
	 * 
	 * @return null if at the end of the string. a Character object if it is a
	 *         grouper('(',')','[',']'), absolute value (|x|), function { a =
	 *         5^x }, or factorial (!) a Value for all parsed Values. a Variable
	 *         for all parsed Variable names. a BinaryOperatorCreator for all
	 *         binary operators a UnaryOperatorCreator for all unary operators
	 *         an ErrorValue if there was a syntax error.
	 * 
	 */
	public Object scan() {
		if (currentCharIndex >= expressionStr.length())
			return null;
		char c = expressionStr.charAt(currentCharIndex++);
		while (true) {

			// check for two-character binary operator
			if (currentCharIndex < expressionStr.length()) {
				char d = expressionStr.charAt(currentCharIndex);
				Object binaryOperator = binaryOperators.get("" + c + d);
				if (binaryOperator != null) {
					currentCharIndex++;
					return binaryOperator;
				}
			}
			// check for single character binary operator
			Object binaryOperator = binaryOperators.get("" + c);
			if (binaryOperator != null)
				return binaryOperator;

			if (Character.isDigit(c) || c == '.') {
				boolean decimalFound = false;
				if (c == '.')
					decimalFound = true;
				StringBuffer s = new StringBuffer().append(c);

				while ((currentCharIndex) < expressionStr.length())
					if (Character.isDigit(c = expressionStr
							.charAt(currentCharIndex++)))
						s.append(c);
					else if (c == '.') {
						if (!decimalFound)
							decimalFound = true;
						else
							return new ErrorValue("malformed decimal: "
									+ s.toString());
						;
						s.append(c);
					} else {
						currentCharIndex--;
						break;
					}
				if (s.toString().equals("."))
					return new ErrorValue("there is a dangling decimal point!");
				else
					return new DecimalValue(Double.parseDouble(s.toString()));

			} else if (Character.isLetter(c)) {
				StringBuffer s = new StringBuffer().append(c);
				while (currentCharIndex < expressionStr.length()) {
					c = expressionStr.charAt(currentCharIndex++);

					if (Character.isLetterOrDigit(c)||c=='_') {
						s.append(c);
						if (currentCharIndex < expressionStr.length())
							if (expressionStr.charAt(currentCharIndex) == '('
									|| expressionStr.charAt(currentCharIndex) == '[')// found
								// a
								// unary
								// operator
								return getUnaryOperator(s.toString());
					} else {
						if (currentCharIndex < expressionStr.length())
							if (c == '(' || c == '[')// found
							// a one-letter
							// unary
							// operator
							{
								currentCharIndex--;
								return getUnaryOperator(s.toString());
							}
						currentCharIndex--;
						break;
					}
				}
				// if we are here it is a variable name
				return Variable.getVariable(s.toString());
			}

			switch (c) {
			case '(':
			case ')':
			case '[':
			case ']':
			case '{':
			case '}':
			case '!':
				return new Character(c);
			case ' ':
			case '\t':
				if (currentCharIndex < expressionStr.length())
					c = expressionStr.charAt(currentCharIndex++);
				else
					return new ErrorValue("unfinished statement: "
							+ expressionStr);

				continue;
			case '"':
				StringBuffer b = new StringBuffer();
				if (currentCharIndex < expressionStr.length()) {
					c = expressionStr.charAt(currentCharIndex++);
				} else {
					return new ErrorValue("unfinished string literal: "
							+ b.toString());
				}
				while (c != '"') {
					if (currentCharIndex < expressionStr.length()) {
						b.append(c);
						c = expressionStr.charAt(currentCharIndex++);
					} else
						return new ErrorValue("unfinished string: "
								+ b.toString());

				}
				return new StringValue(b.toString());

			default:
				return new ErrorValue("invalid symbol " + c);
			}
		}
	}

	/**
	 * Gets the UnaryOperatorCreator mapped to the specified operator name.
	 * 
	 * @param string
	 *            the name of the operator (for example "sin")
	 * @return the UnaryOperatorCreator mapped to the name, or an ErrorValue
	 *         with a message if no mapping exists.
	 */
	private Object getUnaryOperator(String string) {
		Object unaryOperator = unaryOperators.get(string);
		if (unaryOperator != null)
			return unaryOperator;
		return new ErrorValue(string + " is an invalid unary operator.");

	}
}
