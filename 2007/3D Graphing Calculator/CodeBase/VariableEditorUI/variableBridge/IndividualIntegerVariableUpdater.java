package variableBridge;

import javax.swing.JOptionPane;

import parser.Value;
import valueTypes.DecimalValue;
import valueTypes.IntegerValue;
import variables.Variable;
import actionScript.ActionScriptFlags;

/**
 * A class which serves as the base class for the implementation of external to
 * internal Variable bridging for integers.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class IndividualIntegerVariableUpdater implements
		VariableBridgeImplementation {
	IntegerValue reusableValue = new IntegerValue(0);

	public void updateInternalVariable(Variable variableToRead) {
		Value value = variableToRead.evaluate();
		if (value instanceof DecimalValue) {
			int integerValue = (int) DecimalValue.extractDoubleValue(value);
			if (integerValue != getInternalVariableValue())
				updateInternalVariable(integerValue);
		} else {
			updateExternalVariable(variableToRead);
			if (!ActionScriptFlags.suppressWarningDialogBoxes)
				JOptionPane
						.showMessageDialog(
								null,
								variableToRead.toString()
										+ " was set to an invalid value. It must be an integer, not a "
										+ value.getType()
										+ ". It has been reset to it's previous value.",
								"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void updateExternalVariable(Variable variableToUpdate) {
		reusableValue.value = getInternalVariableValue();
		variableToUpdate.set(reusableValue);
	}

	public abstract void updateInternalVariable(int newValue);

	public abstract int getInternalVariableValue();
}