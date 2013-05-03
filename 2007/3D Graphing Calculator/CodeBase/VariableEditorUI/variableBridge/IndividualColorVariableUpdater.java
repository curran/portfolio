package variableBridge;

import java.awt.Color;

import javax.swing.JOptionPane;

import parser.Value;
import valueTypes.ColorValue;
import variables.Variable;
import actionScript.ActionScriptFlags;

/**
 * A class which serves as the base class for the implementation of external to
 * internal Variable bridging for Colors.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class IndividualColorVariableUpdater implements
		VariableBridgeImplementation {
	ColorValue reusableValue = new ColorValue(Color.GREEN);

	public void updateInternalVariable(Variable variableToRead) {
		Value value = variableToRead.evaluate();
		if (value instanceof ColorValue) {
			Color color = ((ColorValue) value).value;
			if (!color.equals(getInternalVariableValue()))
				updateInternalVariable(color);
		} else {
			variableToRead.set(new ColorValue(getInternalVariableValue()));
			if (!ActionScriptFlags.suppressWarningDialogBoxes)
				JOptionPane
						.showMessageDialog(
								null,
								variableToRead.toString()
										+ " was set to an invalid value. It must be a color, not a "
										+ value.getType()
										+ ". It has been reset to it's previous value.",
								"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void updateExternalVariable(Variable variableToUpdate) {
		reusableValue.value = getInternalVariableValue();
		variableToUpdate.set(reusableValue);
	}

	public abstract void updateInternalVariable(Color newValue);

	public abstract Color getInternalVariableValue();
}