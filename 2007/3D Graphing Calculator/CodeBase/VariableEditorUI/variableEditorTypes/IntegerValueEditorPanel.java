package variableEditorTypes;

import variableEditorComponents.VariableBoundIntegerSlider;
import variableEditorComponents.VariableEditorComponent;
import variables.Variable;

/**
 * An editor panel for variables of type IntegerValue.
 * 
 * @author Curran Kelleher
 * 
 */
public class IntegerValueEditorPanel extends BasicVariableEditorPanel {
	private static final long serialVersionUID = 217627759337918568L;

	/**
	 * Constructs an editor panel for the specified variable. This panel has in
	 * it both a slider and a textField, both can be used to edit the contents
	 * of the variabale.
	 * 
	 * @param variable
	 *            the variable to edit
	 */
	public IntegerValueEditorPanel(Variable variable) {
		super(variable);
	}

	/**
	 * Gets the slider used for editing the variable.
	 */
	protected VariableEditorComponent getTypeSpecificComponent() {
		return new VariableBoundIntegerSlider(variable);
	}
	
	/**
	 * Returns true so the text field will be shown initially.
	 */
	protected boolean showTextFieldInitially() {
		return true;
	}
	
	/**
	 * Returns true so the numeric value of the Variable is shown in the border
	 * title.
	 */
	protected boolean displayValueInBorder() {
		return true;
	}
}
