package symbolTableFileUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import expressionConsole.ExpressionConsoleModel;

import actionScript.ActionScriptFlags;

import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.DecimalValue;
import variables.Variable;

/**
 * A utility class for taking snapshots of the symbol table as well as applying
 * existing symbol table snapshots
 * 
 * @author Curran Kelleher
 * 
 */
public class SymbolTableStateUtilities {
	private SymbolTableStateUtilities() {
	}

	/**
	 * Generates a SymbolTableState object which reflects the state of the
	 * current symbol table in the Variable class.
	 * 
	 * @return the list of variable assignment expressions which can be used to
	 *         recreate the state of the symbol table
	 */
	public static List<String> takeSymbolTableSnapshot() {
		// get the unsorted list of variables from the symbol table
		List<Variable> variables = new ArrayList<Variable>(Variable
				.getSymbolTable().values());

		// sort the variables by time of last update
		Collections.sort(variables, new Comparator<Variable>() {
			public int compare(Variable a, Variable b) {
				return a.getTimeOfLastChange() > a.getTimeOfLastChange() ? 1
						: -1;
			}
		});

		// create the list of expressions
		List<String> variableAssignmentExpressions = new LinkedList<String>();
		for (Iterator<Variable> it = variables.iterator(); it.hasNext();) {
			Variable current = it.next();
			variableAssignmentExpressions.add(current.toString() + "="
					+ current.evaluate().toParseableString());
		}
		return variableAssignmentExpressions;
	}

	/**
	 * Clears the current symbol table and executes the specified list of
	 * commands.
	 * 
	 * @param newSymbolTable
	 *            the list of variable assignment expressions which can be used
	 *            to recreate the state of the symbol table
	 */
	public static void applySymbolTableSnapshot(List<String> newSymbolTable) {
		// suppress dialog boxes
		ActionScriptFlags.suppressWarningDialogBoxes = true;

		// clear the current symbol table
		Value zeroValue = new DecimalValue(0);
		for (Iterator<Entry<String, Variable>> it = Variable.getSymbolTable()
				.entrySet().iterator(); it.hasNext();)
			it.next().getValue().set(zeroValue);

		// execute the expressions to restore the symbol table
		RecursiveDescentParser parser = ExpressionConsoleModel.getInstance()
				.getParser();
		for (Iterator<String> it = newSymbolTable.iterator(); it.hasNext();)
			parser.parse(it.next()).evaluate();

		// turn dialog boxes back on
		ActionScriptFlags.suppressWarningDialogBoxes = true;
	}
}
