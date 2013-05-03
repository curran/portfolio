package variableEditorTypes;

import variableEditorComponents.VariableBoundColorMapEditorPanel;
import variableEditorComponents.VariableEditorComponent;
import variables.Variable;

/**
 * An editor panel for variables of type ColorMapValue.
 * 
 * @author Curran Kelleher
 * 
 */
public class ColorMapValueEditorPanel extends BasicVariableEditorPanel {
	private static final long serialVersionUID = 398791084487255548L;

	/**
	 * Constructs an editor panel for the specified variable. This panel has in
	 * it both a Color Map box and a textField, both can be used to edit the
	 * contents of the variable.
	 * 
	 * @param variable
	 *            the variable to edit
	 */
	public ColorMapValueEditorPanel(Variable variable) {
		super(variable);
	}

	/**
	 * 
	 * @return an editor for the color map
	 */
	protected VariableEditorComponent getTypeSpecificComponent() {
		return new VariableBoundColorMapEditorPanel(variable);
	}
}
