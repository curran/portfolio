package expressionConsole;

import java.util.Observable;

import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.ErrorValue;

/**
 * The model of the expression console in the Model View Controller paradigm.
 * This model is a singleton class.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleModel extends Observable {

	/**
	 * The object containing all history information.
	 */
	private ExpressionConsoleHistory expressionHistory = new ExpressionConsoleHistory();

	/**
	 * The parser we will use to parse the function string.
	 */
	private RecursiveDescentParser parser = new RecursiveDescentParser();

	/**
	 * The singleton instance of this class
	 */
	static ExpressionConsoleModel INSTANCE = null;

	/**
	 * The constructor is private because the class is a singleton, so can only
	 * be instantiated from within.
	 * 
	 */
	private ExpressionConsoleModel() {

	}

	/**
	 * Returns a reference to the singleton instance of this class.
	 * 
	 * @return a reference to the singleton instance of this class.
	 */
	public static ExpressionConsoleModel getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ExpressionConsoleModel();
		return INSTANCE;
	}

	/**
	 * Enters the specified expression in the console and evaluates it.
	 * 
	 * @param expressionString
	 *            the expression String
	 */
	public void enterExpression(String expressionString) {

		// add the input to the history
		expressionHistory.getExpressionList().add(
				new ExpressionConsoleHistoryEntry(expressionString,
						ExpressionConsoleHistoryEntry.INPUT));

		// evaluate the expression
		Value parseResult = parser.parse(expressionString).evaluate();

		// add the result to the history
		expressionHistory
				.getExpressionList()
				.add(
						new ExpressionConsoleHistoryEntry(
								parseResult.toString(),
								parseResult instanceof ErrorValue ? ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_ERROR
										: ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_SUCCESS));

		// send the notification (presumably the view and controller are
		// listening)
		setChanged();
		notifyObservers();
	}

	/**
	 * Enters the specified String in the console without evaluating it. It is
	 * displayed in the style of an error.
	 * 
	 * @param messageString
	 *            the string to display
	 */
	public void enterErrorMessage(String messageString) {
		// add the message to the history
		expressionHistory.getExpressionList().add(
				new ExpressionConsoleHistoryEntry(messageString,
						ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_ERROR));

		// send the notification (presumably the view and controller are
		// listening)
		setChanged();
		notifyObservers();
	}

	/**
	 * Enters the specified String in the console without evaluating it.
	 * 
	 * @param messageString
	 *            the string to display
	 */
	public void enterMessage(String messageString) {

		// add the message to the history
		expressionHistory.getExpressionList().add(
				new ExpressionConsoleHistoryEntry(messageString,
						ExpressionConsoleHistoryEntry.MESSAGE));

		// send the notification (presumably the view and controller are
		// listening)
		setChanged();
		notifyObservers();
	}

	/**
	 * The list containing the full history of console entries.
	 * 
	 * @return The list containing the full history of console entries.
	 */
	public ExpressionConsoleHistory getExpressionHistory() {
		return expressionHistory;
	}

	/**
	 * Gets the parser used by the console.
	 * 
	 * @return The parser used by the console.
	 */
	public RecursiveDescentParser getParser() {
		return parser;
	}

	/**
	 * Records into the history the current state of the symbol table, which
	 * maps Variable names to Values. This is necessary for the history to
	 * initialize properly when loaded.
	 * 
	 */
	public void recordInitialVariableValues() {
		expressionHistory.recordInitialVariableValues();
	}

	/**
	 * Executes (does not display anything) the specified function (It can include many expressions separated by ';' characters). Calling this
	 * is equivalent to calling
	 * <code>ExpressionConsoleModel.getInstance().getParser().parse(
	 "executeFunction({" + function
	 + "})").evaluate();</code>
	 * 
	 * @param function the function string to evaluate
	 */
	public void executeFunction(String function) {

		parser.parse("executeFunction({" + function + "})").evaluate();
	}

}
