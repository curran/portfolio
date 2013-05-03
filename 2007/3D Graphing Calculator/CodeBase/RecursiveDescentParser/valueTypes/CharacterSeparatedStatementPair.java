package valueTypes;

import java.util.ArrayList;

import operators.BinaryOperator;
import parser.ExpressionNode;
import parser.Value;
/**
 * Represents a pair of two statements (expression trees which have not been evaluated) separated by a colon.
 * @author Curran Kelleher
 *
 */
public class CharacterSeparatedStatementPair extends BinaryOperator {

	/**
	 * Constrict a ColonSeparatedStatementPair with the specified two expression trees as it's pair of statements.
	 * @param character separating the statements
	 * @param leftChild the left-side statement
	 * @param rightChild the right-side statement
	 */
	public CharacterSeparatedStatementPair(String symbol,ExpressionNode leftChild, ExpressionNode rightChild) {
		super(symbol,leftChild,rightChild);
	}

	/**
	 * Gets the left-side statement
	 * @return the left-side statement of the statement pair
	 */
	public ExpressionNode getLeftStatement() {
		return leftChild;
	}

	/**
	 * Gets the right statement
	 * @return the right statement of the statement pair
	 */
	public ExpressionNode getRightStatement() {
		return rightChild;
	}

	/**
	 * If many statements are strung together, separated by characters, this
	 * method returns an array of evaluation trees, one for each character separated statement.
	 * 
	 * If the separators are inconsistent, an array with a single ErrorValue is returned.
	 * 
	 * @return array of evaluation trees, or an array with a single ErrorValue if the separator characters are inconsistent.
	 */
	public ExpressionNode[] extractAllStatements() {
		ArrayList<ExpressionNode> extractedStatements = new ArrayList<ExpressionNode>();
		ExpressionNode[] statements;
		if (extractAllStatements(extractedStatements, this)) {
			statements = new ExpressionNode[extractedStatements.size()];
			for (int i = 0; i < statements.length; i++)
				statements[i] = extractedStatements
						.get(i);
		} else {
			statements = new ExpressionNode[1];
			statements[0] = new ErrorValue(
					"inconsistant characters used as statement sepatarors.");
		}
		return statements;
	}
	
	private boolean extractAllStatements(ArrayList<ExpressionNode> statements,
			CharacterSeparatedStatementPair pair) {

		// (((a;b);c);d)

		boolean success = true;
		ExpressionNode leftStatement = pair.getLeftStatement();
		if (leftStatement instanceof CharacterSeparatedStatementPair) {
			CharacterSeparatedStatementPair leftPair = (CharacterSeparatedStatementPair) leftStatement;
			if (leftPair.getSymbol().equals(pair.getSymbol()))
				success = extractAllStatements(statements, leftPair);
			else {
				// failure,  inconsistant characters used as statement separators
				success = false;
				statements.add(leftStatement);
			}
		} else
			statements.add(leftStatement);

		statements.add(pair.getRightStatement());
		return success;
	}

	/**
	 * Returns an ErrorValue, as CharacterSeparatedStatementPair objects are not intended to be evaluated, they are used only as part of other constructs, and should be extracted.
	 */
	public Value evaluate() {
		return new ErrorValue("statements separated by the '"+symbol+"' character should not be evaluated. They should only be used as a part of some construct (such as a function definition or when defining a \"for\" loop");
	}
}
