package variableEditorTypes;

import variableEditorComponents.VariableBoundCheckBox;
import variableEditorComponents.VariableEditorComponent;
import variables.Variable;

/**
 * An editor panel for variables of type BooleanValue.
 * 
 * @author Curran Kelleher
 * 
 */
public class BooleanValueEditorPanel extends BasicVariableEditorPanel {
	private static final long serialVersionUID = -6942378885789408919L;

	/**
	 * Constructs an editor panel for the specified variable. This panel has in
	 * it both a check box and a textField, both can be used to edit the contents
	 * of the variabale.
	 * 
	 * @param variable
	 *            the variable to edit
	 */
	public BooleanValueEditorPanel(Variable variable) {
		super(variable);
	}
	
	/**
	 * Gets the check box used to edit the Variable.
	 */
	protected VariableEditorComponent getTypeSpecificComponent() {
		return new VariableBoundCheckBox(variable);
	}
}
