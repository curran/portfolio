package variableEditorUI;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import parser.Value;
import valueTypes.BooleanValue;
import valueTypes.ColorValue;
import valueTypes.DecimalValue;
import valueTypes.IntegerValue;
import variableEditorTypes.BasicVariableEditorPanel;
import variableEditorTypes.BooleanValueEditorPanel;
import variableEditorTypes.ColorMapValueEditorPanel;
import variableEditorTypes.ColorValueEditorPanel;
import variableEditorTypes.DecimalValueEditorPanel;
import variableEditorTypes.IndividualVariableEditorPanel;
import variableEditorTypes.IntegerValueEditorPanel;
import variables.Variable;
import colorMapValue.ColorMapValue;

/**
 * A panel which houses a variable editor panel for a particular. The actual
 * editor panel depends on the type of the Variable, and is swapped out for a
 * new one if the type of variable changes.
 * 
 * @author Curran Kelleher
 * 
 */
public class VariableEditorPanel extends JPanel implements Observer {
	private static final long serialVersionUID = -4730265945673395759L;

	/**
	 * The Variable being edited.
	 */
	Variable variable;

	/**
	 * The real editor panel, which can be swapped out if the type of the
	 * variable changes.
	 */
	IndividualVariableEditorPanel editorPanel;

	/**
	 * Construct a VariableEditorPanel for editing the specified Variable
	 * 
	 * @param variable
	 *            the Variable to edit
	 */
	public VariableEditorPanel(Variable variable) {
		this.variable = variable;
		setLayout(new GridLayout(1, 0));
		assignTypeSpecificEditorPanel();
		VariableEditorUIUpdateThread.getInstance().addObserver(this);
	}

	/**
	 * Makes an editor panel specific to the type currently contained in the
	 * Variable, assigns it to the field editorPanel, and makes it the contents
	 * of this panel.
	 * 
	 */
	private void assignTypeSpecificEditorPanel() {
		Value value = variable.evaluate();

		// handle DecimalValue type
		if (value instanceof DecimalValue) {
			if (value instanceof IntegerValue)
				editorPanel = new IntegerValueEditorPanel(variable);
			else
				editorPanel = new DecimalValueEditorPanel(variable);
		} else if (value instanceof BooleanValue)
			editorPanel = new BooleanValueEditorPanel(variable);
		else if (value instanceof ColorValue)
			editorPanel = new ColorValueEditorPanel(variable);
		else if (value instanceof ColorMapValue)
			editorPanel = new ColorMapValueEditorPanel(variable);
		else
			editorPanel = new BasicVariableEditorPanel(variable);

		this.removeAll();
		add(editorPanel);
		validate();
	}

	/**
	 * Get the update from the VariableEditorUIUpdateThread
	 */
	public void update(Observable o, Object arg) {
		if (o == VariableEditorUIUpdateThread.getInstance())
			// if this component is no longer usable, remove it as an Observer.
			if (!isDisplayable())
				VariableEditorUIUpdateThread.getInstance().deleteObserver(this);
			else // respond to type conflicts
			if (editorPanel.hasTypeConflict())
				assignTypeSpecificEditorPanel();
	}
}
