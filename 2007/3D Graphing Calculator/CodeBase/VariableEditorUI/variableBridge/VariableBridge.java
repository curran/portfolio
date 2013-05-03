package variableBridge;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import variableEditorUI.VariableEditorUIUpdateThread;
import variables.Variable;
import expressionConsole.ExpressionConsoleModel;

/**
 * A class which facilitates the binding of Variables (Objects of type
 * 
 * @link variables.Variable, considered "External") to actual variables within
 *       Java classes (considered "internal").
 * @author Curran Kelleher
 * @see variables.Variable
 */
public class VariableBridge implements Observer {
	/**
	 * The list of Variables which are bound to actual Java variables in another
	 * class.
	 */
	Variable[] variables;

	/**
	 * The map which maps Variable names to their corresponding
	 * VariableBridgeImplementations.
	 */
	Map<String, VariableBridgeImplementation> individualVariableUpdaters = new HashMap<String, VariableBridgeImplementation>();;

	/**
	 * Set up a functioning VariableBridge which uses the specified
	 * IndividualVariableBridges.
	 * 
	 * @param bridges
	 *            the IndividualVariableBridges which will be used to carry out
	 *            the bridging.
	 */
	public VariableBridge(IndividualVariableBridge[] bridges) {
		// establish references to the Variables
		variables = new Variable[bridges.length];
		for (int i = 0; i < bridges.length; i++) {
			variables[i] = Variable.getVariable(bridges[i].variableName);
			individualVariableUpdaters.put(bridges[i].variableName,
					bridges[i].bridgeImplementation);
			variables[i].addObserver(this, bridges[i].explaination);
		}

		// initialize the external Variables to the current values of the
		// internal variables
		updateExternalVariables();

		// recieve periodic updates to update the externals from the internals
		VariableEditorUIUpdateThread.getInstance().addObserver(this);
	}

	/**
	 * Updates all of the Variable objects based on the current values of their
	 * associated Java variables.
	 * 
	 */
	public void updateExternalVariables() {
		for (int i = 0; i < variables.length; i++)
			if (!updateExternalVariable(variables[i]))
				(new Exception(
						"The variable bridged class does not handle the Variable "
								+ variables[i] + ", but it should!"))
						.printStackTrace();
	}

	/**
	 * Updates all of the Java variables based on the current values of their
	 * associated Variable objects.
	 * 
	 */
	public void updateInternalVariables() {
		for (int i = 0; i < variables.length; i++)
			if (!updateInternalVariable(variables[i]))
				(new Exception(
						"The variable bridged class does not handle the Variable "
								+ variables[i] + ", but it should!"))
						.printStackTrace();
	}

	/**
	 * Updates the specified external Variable based on the contents of it's
	 * corresponding internal Java variable.
	 * 
	 * @param variable
	 *            the Variable which should be updated
	 * @return true if success, false if the variable was not recognized
	 */
	public boolean updateExternalVariable(Variable variable) {
		VariableBridgeImplementation u = individualVariableUpdaters
				.get(variable.toString());
		if (u != null) {
			u.updateExternalVariable(variable);
			return true;
		} else
			return false;
	}

	/**
	 * Updates the contents of the internal Java variable corresponding to the
	 * specified external Variable.
	 * 
	 * @param variable
	 *            the Variable which should be used for updating it's
	 *            corresponding internal Java variable.
	 * @return true if success, false if the variable was not recognized
	 */
	public boolean updateInternalVariable(Variable variable) {
		VariableBridgeImplementation u = individualVariableUpdaters
				.get(variable.toString());
		if (u != null) {
			u.updateInternalVariable(variable);
			return true;
		} else
			return false;
	}

	/**
	 * Get updates from the Variables when they change, or the
	 * VariableEditorUIUpdateThread to periodically update external Variables.
	 */
	public void update(Observable o, Object arg) {
		// if the update is from one of the variables we are dealing with
		if (arg instanceof Variable) {
			if (individualVariableUpdaters.containsKey(arg.toString()))
				// update it's corresponding internal value
				updateInternalVariable((Variable) arg);
		} else if (arg == VariableEditorUIUpdateThread.UPDATE_ARGUMENT)
			updateExternalVariables();
	}

	/**
	 * Displays a variable editor frame for all of the variables contained in
	 * this variable bridge.
	 * 
	 */
	public void showEditorForAllVariables() {
		StringBuffer editCommand = new StringBuffer();
		editCommand.append("edit(");
		for(int i=0;i<variables.length;i++)
			editCommand.append(variables[i].toString()+(i<variables.length-1?",":""));
		editCommand.append(")");
		ExpressionConsoleModel.getInstance().enterExpression(editCommand.toString());
	}
}
