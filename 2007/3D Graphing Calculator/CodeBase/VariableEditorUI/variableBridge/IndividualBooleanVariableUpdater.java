package variableBridge;

import javax.swing.JOptionPane;

import parser.Value;
import valueTypes.BooleanValue;
import variables.Variable;
import actionScript.ActionScriptFlags;

/**
 * A class which serves as the base class for the implementation of external to
 * internal Variable bridging for Boolean values.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class IndividualBooleanVariableUpdater implements
		VariableBridgeImplementation {
	BooleanValue reusableValue = new BooleanValue(true);

	public void updateInternalVariable(Variable variableToRead) {
		Value value = variableToRead.evaluate();
		if (value instanceof BooleanValue) {
			boolean booleanValue = BooleanValue.extractBooleanValue(value);
			if (getInternalVariableValue() != booleanValue)
				updateInternalVariable(booleanValue);
		} else {
			variableToRead.set(new BooleanValue(getInternalVariableValue()));
			if (!ActionScriptFlags.suppressWarningDialogBoxes)
				JOptionPane
						.showMessageDialog(
								null,
								variableToRead.toString()
										+ " was set to an invalid value. It must be a boolean value, not a "
										+ value.getType()
										+ ". It has been reset to it's previous value.",
								"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void updateExternalVariable(Variable variableToUpdate) {
		reusableValue.value = getInternalVariableValue();
		variableToUpdate.set(reusableValue);
	}

	public abstract void updateInternalVariable(boolean newValue);

	public abstract boolean getInternalVariableValue();
}