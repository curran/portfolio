package actionScript;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parser.RecursiveDescentParser;
import variables.Variable;
import expressionConsole.ExpressionConsoleHistory;
import expressionConsole.ExpressionConsoleHistoryEntry;
import expressionConsole.ExpressionConsoleModel;

/**
 * The class which has a utility method which executes action scripts
 * 
 * @author Curran Kelleher
 * 
 */
public class ActionScriptPlayer {
	/**
	 * Executes the specified history as an action script.
	 * 
	 * @param history
	 *            the history of events to execute
	 */
	public static void executeScript(ExpressionConsoleHistory history) {
		if (history != null) {
			Map<String, String> initialSymbolTable = history
					.getInitialSymbolTableSnapshot();

			Map<String, Variable> currentSymbolTable = Variable
					.getSymbolTable();

			ExpressionConsoleModel console = ExpressionConsoleModel
					.getInstance();

			RecursiveDescentParser parser = console.getParser();

			// suppress dialog boxes
			ActionScriptFlags.suppressWarningDialogBoxes = true;

			// initialize the symbol table
			for (Iterator<Entry<String, Variable>> it = currentSymbolTable
					.entrySet().iterator(); it.hasNext();) {
				Entry<String, Variable> currentEntry = it.next();
				String newValue = initialSymbolTable.get(currentEntry.getKey());
				String expressionToEvaluate = currentEntry.getKey() + " = "
						+ (newValue == null ? "0" : newValue);
				parser.parse(expressionToEvaluate).evaluate();
			}

			// step through all events
			List<ExpressionConsoleHistoryEntry> historyList = history
					.getExpressionList();

			for (Iterator<ExpressionConsoleHistoryEntry> it = historyList
					.iterator(); it.hasNext();) {
				ExpressionConsoleHistoryEntry currentEntry = it.next();
				if (currentEntry.getType() == ExpressionConsoleHistoryEntry.INPUT)
					console.enterExpression(currentEntry.getString());
			}

			// turn dialog boxes back on
			ActionScriptFlags.suppressWarningDialogBoxes = false;
		}
	}

}
