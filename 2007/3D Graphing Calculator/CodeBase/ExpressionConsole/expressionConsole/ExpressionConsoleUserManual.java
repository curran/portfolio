package expressionConsole;

import operators.UnaryOperator;
import operators.UnaryOperatorCreator;
import parser.ExpressionNode;
import parser.ParserUtilities;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.StringValue;

/**
 * A class containing only a text description of how to use the console.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleUserManual {
	public static String USER_MANUAL_TEXT = "Enter expressions into the console text box and press \"Enter\" to have them evaluated. Use the up and down arrows to navigate the history of input (hold down the control key to navigate results as well).";

	/**
	 * Sets up the userManual() command, which displays the user manual, in the
	 * specified parser as a unary operator.
	 * 
	 * @param parserToAddCommandTo
	 *            the parser to add the userManual() unary operator to
	 */
	public static void setUpUserManualCommand(
			RecursiveDescentParser parserToAddCommandTo) {
		parserToAddCommandTo.addUnaryOperator("userManual",
				new UnaryOperatorCreator() {
					public UnaryOperator create(ExpressionNode child) {
						return new UnaryOperator(child) {
							public Value evaluate() {
								return new StringValue(
										"THE CONSOLE:\n"
												+ ExpressionConsoleUserManual.USER_MANUAL_TEXT
												+ "\n\nCOMMANDS:\n"
												+ ParserUtilities
														.getUnopsAndDescriptions(
																ExpressionConsoleModel
																		.getInstance()
																		.getParser(),
																UnaryOperatorCreator.COMMAND)
												+ "\n\nMATHEMATICAL BINARY OPERATORS:\n"
												+ ParserUtilities
														.getBinopsAndDescriptions(ExpressionConsoleModel
																.getInstance()
																.getParser())
												+ "\n\nMATHEMATICAL UNARY OPERATORS:\n"
												+ ParserUtilities
														.getUnopsAndDescriptions(
																ExpressionConsoleModel
																		.getInstance()
																		.getParser(),
																UnaryOperatorCreator.MATHEMATICAL)
												+ "\n\nCONSTANTS:\n"
												+ ParserUtilities
														.getConstantsAndDescriptions()
												+ "\n\nLANGUAGE CONSTRUCTS:\n"
												+ ParserUtilities
														.getUnopsAndDescriptions(
																ExpressionConsoleModel
																		.getInstance()
																		.getParser(),
																UnaryOperatorCreator.LANGUAGE_CONSTRUCT)
												+ "\n\nMISCELLANEOUS UNARY OPERATORS:\n"
												+ ParserUtilities
														.getUnopsAndDescriptions(
																ExpressionConsoleModel
																		.getInstance()
																		.getParser(),
																UnaryOperatorCreator.MISCELLANEOUS)
												+ "\n\nFUNCTIONAL VARIABLES:\n"
												+ ParserUtilities
														.getObservedVariablesAndDescriptions());
							}
						};
					}

					public int getType() {
						return UnaryOperatorCreator.COMMAND;
					}

					public String getDescription() {
						return "displays the user manual";
					}
				});
	}
}
