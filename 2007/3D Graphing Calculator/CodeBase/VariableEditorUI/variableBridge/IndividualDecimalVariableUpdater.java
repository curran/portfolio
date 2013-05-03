package variableBridge;

import javax.swing.JOptionPane;

import parser.Value;
import valueTypes.DecimalValue;
import variables.Variable;
import actionScript.ActionScriptFlags;

/**
 * A class which serves as the base class for the implementation of external to
 * internal Variable bridging for decimal numbers.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class IndividualDecimalVariableUpdater implements
		VariableBridgeImplementation {
	DecimalValue reusableValue = new DecimalValue(0);

	public void updateInternalVariable(Variable variableToRead) {
		Value value = variableToRead.evaluate();
		if (value instanceof DecimalValue) {
			double doubleValue = DecimalValue.extractDoubleValue(value);
			if (doubleValue != getInternalVariableValue())
				updateInternalVariable(doubleValue);
		} else {
			variableToRead.set(new DecimalValue(getInternalVariableValue()));
			if (!ActionScriptFlags.suppressWarningDialogBoxes)
				JOptionPane
						.showMessageDialog(
								null,
								variableToRead.toString()
										+ " was set to an invalid value. It must be a number, not a "
										+ value.getType()
										+ ". It has been reset to it's previous value.",
								"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void updateExternalVariable(Variable variableToUpdate) {
		reusableValue.value = getInternalVariableValue();
		variableToUpdate.set(reusableValue);
	}

	public abstract void updateInternalVariable(double newValue);

	public abstract double getInternalVariableValue();
}