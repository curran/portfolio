package expressionConsole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import variables.Variable;

/**
 * A class which encapsulates all the history information of the
 * ExpressionHistoryConsole.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleHistory {
	/**
	 * The list containing all executed expressions and their results.
	 */
	private List<ExpressionConsoleHistoryEntry> expressionList = new ArrayList<ExpressionConsoleHistoryEntry>();

	/**
	 * The initial state of the symbol table in the Variable class. This is
	 * necessary for the history to initialize properly when loaded. See the
	 * method recordInitialVariableValues
	 */
	private Map<String, String> initialSymbolTableSnapshot = null;

	/**
	 * Do not use this method. It is here to make this class a Java Bean so it
	 * can be written to a file using the Java XMLEncoder.
	 * 
	 */
	public void setExpressionList(
			List<ExpressionConsoleHistoryEntry> expressionList) {
		this.expressionList = expressionList;
	}

	/**
	 * Gets the list containing all executed expressions and their results.
	 * 
	 * @return
	 */
	public List<ExpressionConsoleHistoryEntry> getExpressionList() {
		return expressionList;
	}

	/**
	 * Records into the history the current state of the symbol table, which
	 * maps Variable names to Values. This is necessary for the history to
	 * initialize properly when loaded.
	 * 
	 */
	public void recordInitialVariableValues() {
		Map<String, Variable> symbolTable = Variable.getSymbolTable();
		Map<String, String> symbolTableSnapshot = new HashMap<String, String>();
		for (Iterator<Entry<String, Variable>> it = symbolTable.entrySet()
				.iterator(); it.hasNext();) {
			Entry<String, Variable> currentEntry = it.next();
			symbolTableSnapshot.put(currentEntry.getKey(), currentEntry
					.getValue().evaluate().toParseableString());
		}
		this.initialSymbolTableSnapshot = symbolTableSnapshot;
	}

	/**
	 * Do not use this method. It is here to make this class a Java Bean so it
	 * can be written to a file using the Java XMLEncoder.
	 * 
	 */
	public void setInitialSymbolTableSnapshot(
			Map<String, String> initialSymbolTableSnapshot) {
		this.initialSymbolTableSnapshot = initialSymbolTableSnapshot;
	}

	/**
	 * Gets the state of the symbol table (from the Variable class) at the time
	 * after the program finished initializing and before the user did anything.
	 * The keys are the Variable names, and the values are strings which, when
	 * parsed and evaluated, give rise to the Value that was in that Variable.
	 * 
	 * @return The initial state of the symbol table for this particular run of the program.
	 */
	public Map<String, String> getInitialSymbolTableSnapshot() {
		return initialSymbolTableSnapshot;
	}
}
