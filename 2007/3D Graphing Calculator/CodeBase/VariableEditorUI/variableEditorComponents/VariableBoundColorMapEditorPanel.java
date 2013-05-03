package variableEditorComponents;

import parser.Value;
import variables.Variable;
import colorMap.ColorMapEditorPanel;
import colorMapValue.ColorMapValue;

/**
 * A ColorMapEditorPanel which is bound to a Variable.
 * 
 * @author Curran Kelleher
 * 
 */
public class VariableBoundColorMapEditorPanel extends ColorMapEditorPanel
		implements VariableEditorComponent {
	
	/**
	 * The associated Variable
	 */
	Variable variable;

	public VariableBoundColorMapEditorPanel(Variable variable) {
		super(ColorMapValue.extractColorMap(variable.evaluate()));
		this.variable = variable;
	}

	private static final long serialVersionUID = 0L;

	/**
	 * This method doesn't do anything.
	 */
	public void bindToVariableEditorComponent(
			VariableEditorComponent componentToUpdate) {
	}

	
	public void updateWithCurrentVariableValue() {
		Value value = variable.evaluate();
		if(value instanceof ColorMapValue)
			this.setColorMap(((ColorMapValue)value).value);
	}

	
}
