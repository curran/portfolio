package parser;

import java.util.HashMap;
import java.util.Map;

import operators.BinaryOperatorCreator;
import operators.Factorial;
import operators.Minus;
import operators.Operators;
import operators.UnaryOperator;
import operators.UnaryOperatorCreator;
import valueTypes.ErrorValue;
import valueTypes.FunctionValue;
import variables.Constants;
import variables.Variable;

/**
 * The parser class which can parse strings into evaluation trees.
 * 
 * @author Curran Kelleher
 * 
 */
public class RecursiveDescentParser {
	// all present binary operators
	// the type is <String symbol,BinaryOperatorCreator operator>
	Map<String,BinaryOperatorCreator> binaryOperators = new HashMap<String,BinaryOperatorCreator>();

	// all present unary operators
	// the type is <String name,UnaryOperatorCreator operator>
	Map<String,UnaryOperatorCreator> unaryOperators = new HashMap<String,UnaryOperatorCreator>();

	// the current token being processed
	Object token;

	/**
	 * The last parsed unary operator.necessary for linking statements with
	 * their associated functions.
	 * 
	 * For example, when parsing "if(a<0){a = 0}", the "if" statement of "if(a<0)"
	 * would go in this variable, then the function {a = 0} will be linked to
	 * it.
	 */
	UnaryOperator lastParsedUnaryOperator;

	/**
	 * the scanner used to scan for tokens in the expression string
	 */
	TokenScanner tokenScanner;

	/**
	 * Constructs a RecursiveDescentParser. It automatically includes all
	 * operators defined in Operators.getBinaryOperators() and
	 * Operators.getUnaryOperators().
	 * 
	 * @see Operators
	 */
	public RecursiveDescentParser() {
		// initialize all constants (such as pi, e, etc.)
		Constants.initializeConstants();

		// populate ourselves with all binary operators (such as +, -, etc.)
		Operators.getBinaryOperators(this);

		// populate ourselves with all unary operators (such as sin, cos, etc.)
		Operators.getUnaryOperators(this);
	}

	/**
	 * Parses the specified expression string.
	 * 
	 * @param expression
	 *            the expression string
	 * @return the resulting evaluation tree.
	 */
	public ExpressionNode parse(String expression) {

		// reset the scanner
		tokenScanner = new TokenScanner(binaryOperators, unaryOperators,
				expression);

		// reset the lastParsedUnaryOperator
		lastParsedUnaryOperator = null;

		// scan the first token
		token = tokenScanner.scan();

		return expression();
	}

	/**
	 * Recursively descends through all binary operator precedence levels, then
	 * calls factor()
	 * 
	 * @param precedence
	 *            the current precedence
	 * @return the resulting evaluation tree.
	 */
	private ExpressionNode expression() {
		return expression(-6);
	}

	/**
	 * Recursively descends through all binary operator precedence levels, then
	 * calls factor()
	 * 
	 * @param precedence
	 *            the current precedence
	 * @return the resulting evaluation tree.
	 */
	private ExpressionNode expression(int precedence) {
		precedence++;
		ExpressionNode root = precedence == 4 ? factor()
				: expression(precedence);
		while (true) {
			if (token instanceof BinaryOperatorCreator ? ((BinaryOperatorCreator) token).precedence == precedence
					: false) {
				BinaryOperatorCreator binaryOperatorCreator = (BinaryOperatorCreator) token;
				token = tokenScanner.scan();
				root = binaryOperatorCreator.create(root,
						precedence == 4 ? factor() : expression(precedence));
			} else if (token instanceof ErrorValue)
				return (ErrorValue) token;
			// else if(token represents Factorial)
			else if (token instanceof Character ? ((Character) token)
					.charValue() == '!' : false) {
				token = tokenScanner.scan();
				return new Factorial(root);
			} else if (token instanceof Character ? ((Character) token)
					.charValue() == '{' : false) {
				UnaryOperator previouslyParsedUnaryOperator = lastParsedUnaryOperator;
				FunctionValue associatedFunction = new FunctionValue(
						parseGrouping('}'));
				if (previouslyParsedUnaryOperator == null)
					return new ErrorValue("no operator present to associate the function "
							+ associatedFunction + " with");
				else {
					// if success
					if (previouslyParsedUnaryOperator
							.linkAssociatedFunction(associatedFunction))
						return root;
					else // if failure
						return new ErrorValue("the function "+associatedFunction+" cannot be associated with an operator which does not accept associated functions.");
				}
			} else
				return root;
		}
	}

	/**
	 * Deals with atoms, groupings, and unary operators
	 * 
	 * @return the resulting evaluation tree
	 */
	private ExpressionNode factor() {
		ExpressionNode root = null;

		if (token instanceof Value) {
			root = (Value) token;
			token = tokenScanner.scan();
			return root;
		}

		if (token instanceof UnaryOperatorCreator) {
			UnaryOperatorCreator unopCreator = (UnaryOperatorCreator) token;
			token = tokenScanner.scan();
			ExpressionNode child = factor();
			return lastParsedUnaryOperator = unopCreator.create(child);
		}

		if (token instanceof Minus) {
			token = tokenScanner.scan();
			return Minus.createUnaryMinus(factor());
		}

		if (token instanceof Character) {
			char c = ((Character) token).charValue();
			switch (c) {
			case '(':
				return parseGrouping(')');
			case '[':
				return ExpressionList.extractExpressionList(parseGrouping(']'));
			case '{':
				return new FunctionValue(parseGrouping('}'));
			default:
				return new ErrorValue("invalid character: " + c);
			}
		}

		if (token instanceof Variable) {
			root = (Variable) token;
			token = tokenScanner.scan();
			return root;
		}

		return new ErrorValue("Syntax error: '" + token + "'");
	}

	/**
	 * Parses expressions within groupers. (such as (a+b))
	 * 
	 * @param endGroupingChar
	 *            the character at the end of the group. For example ')' for
	 *            (a+b).
	 * @return the resulting evaluation tree.
	 */
	private ExpressionNode parseGrouping(char endGroupingChar) {
		token = tokenScanner.scan();
		ExpressionNode root = expression();
		if (token instanceof Character ? ((Character) token).charValue() != endGroupingChar
				: true)
			return new ErrorValue("missing '" + endGroupingChar + "' at "
					+ token);

		token = tokenScanner.scan();
		return root;
	}

	/**
	 * Maps a symbol to a BinaryOperatorCreator. When a string is parsed into an
	 * evaluation tree, this mapping will be used to create all BinaryOperators.
	 * 
	 * @param symbol
	 *            the symbol of the binary operator (for example '+')
	 * @param creator
	 *            the BinaryOperatorCreator which, when called upon, will create
	 *            a BinaryOperators which will perform the operation
	 *            corresponding to the symbol of the operator.
	 */
	public void addBinaryOperator(String symbol, BinaryOperatorCreator creator) {
		binaryOperators.put(symbol, creator);
	}

	/**
	 * Maps an operator name to a UnaryOperatorCreator. When a string is parsed
	 * into an evaluation tree, this mapping will be used to create all
	 * UnaryOperators.
	 * 
	 * @param name
	 *            the name of the operator (for example "sin")
	 * @param creator
	 *            the UnaryOperatorCreator which, when called upon, will create
	 *            a UnaryOperators which will perform the operation
	 *            corresponding to the name of the operator.
	 */
	public void addUnaryOperator(String name, UnaryOperatorCreator creator) {
		unaryOperators.put(name, creator);
	}
}
