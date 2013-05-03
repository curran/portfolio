package variableEditorTypes;

import variableEditorComponents.VariableBoundColorBox;
import variableEditorComponents.VariableEditorComponent;
import variables.Variable;

/**
 * An editor panel for variables of type ColorValue.
 * 
 * @author Curran Kelleher
 * 
 */
public class ColorValueEditorPanel extends BasicVariableEditorPanel {
	private static final long serialVersionUID = 398791084487255548L;

	/**
	 * Constructs an editor panel for the specified variable. This panel has in
	 * it both a Color box and a textField, both can be used to edit the
	 * contents of the variable.
	 * 
	 * @param variable
	 *            the variable to edit
	 */
	public ColorValueEditorPanel(Variable variable) {
		super(variable);
	}

	/**
	 * 
	 * @return a button which, when clicked, displays a color chooser which changes
	 * the value of the Variable with the new color when the user picks a color.
	 */
	protected VariableEditorComponent getTypeSpecificComponent() {
		return new VariableBoundColorBox(variable);
	}
}
