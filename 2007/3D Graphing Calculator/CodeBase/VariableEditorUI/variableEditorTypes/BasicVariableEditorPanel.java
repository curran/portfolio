package variableEditorTypes;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JSplitPane;

import parser.Value;
import variableEditorComponents.VariableBoundTextField;
import variableEditorComponents.VariableBoundTitledBorder;
import variableEditorComponents.VariableEditorComponent;
import variables.Variable;

/**
 * An editor panel for variables of any type. It includes a text field and a
 * border.
 * 
 * @author Curran Kelleher
 * 
 */
public class BasicVariableEditorPanel extends IndividualVariableEditorPanel {
	private static final long serialVersionUID = -4715753465310426790L;

	/**
	 * The Variable being edited.
	 */
	protected Variable variable;

	/**
	 * The initial Value of the variable.
	 */
	Value initialValue;

	/**
	 * Constructs an editor panel for the specified variable. This panel has in
	 * it a descriptive border and a textField. The text field can be used to
	 * edit the contents of the variabale.
	 * 
	 * @param variable
	 *            the variable to edit
	 */
	@SuppressWarnings("serial")
	public BasicVariableEditorPanel(Variable variable) {
		// set the reference for later use
		this.variable = variable;

		// store the initlai value to text against for type changes
		initialValue = variable.evaluate();

		// set the layout so it fills the space with the splitPane
		setLayout(new GridLayout(1, 0));

		// create the border
		VariableBoundTitledBorder border = new VariableBoundTitledBorder(
				variable, this,displayValueInBorder());

		// create the text field
		VariableBoundTextField textField = new VariableBoundTextField(variable);

		// bind the border to the text field
		textField.bindToVariableEditorComponent(border);

		setBorder(border);

		VariableEditorComponent typeSpecificComponent = getTypeSpecificComponent();
		if (typeSpecificComponent == null)
			add(textField);
		else if (typeSpecificComponent instanceof Component) {
			// so the component updates when the text field does.
			textField.bindToVariableEditorComponent(typeSpecificComponent);
			// so the border updates when the type specific component changes
			// the Variable
			typeSpecificComponent.bindToVariableEditorComponent(border);
			// so the text field updates when the type specific component
			// changes the
			// Variable
			typeSpecificComponent.bindToVariableEditorComponent(textField);

			// set up the split pane with the type-specific component the and
			// text field
			JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					(Component) typeSpecificComponent, textField);
			// split.setOneTouchExpandable(true);
			split.setResizeWeight(1);

			if (!showTextFieldInitially())
				split.setDividerLocation(1000);

			// make the split pane the contents of the panel
			add(split);
		} else
			(new Exception("typeSpecificComponent is not a Component"))
					.printStackTrace();

	}

	public boolean hasTypeConflict() {
		return initialValue.getClass() != variable.evaluate().getClass();
	}

	/**
	 * Gets the component which should be used to edit this variable of a
	 * particular type. Subclasses should implement this to provide
	 * type-specific variable editing capabilities. The result from this method
	 * is put inside the split pane if it is not null.
	 * 
	 * @return the component which allows the user to edit the variable in a
	 *         type-specific way.
	 */
	protected VariableEditorComponent getTypeSpecificComponent() {
		return null;
	}

	/**
	 * If this returns true, the text field is shown initially. Otherwise, the
	 * split pane is initialized so that the text field is hidden, but still
	 * there for the user to use by moving the divider. This returns false by
	 * default, it is intended for subclasses to override if the text field is
	 * desirable.
	 * 
	 * @return whether or not to initially show the text field
	 */
	protected boolean showTextFieldInitially() {
		return false;
	}
	
	/**
	 * Gets whether or not to display the value of the variable along with it's
	 * name in the title of the border.
	 * 
	 * @return false. Subclasses should override this and return true if showing
	 *         the value of the Variable in the border is desirable.
	 */
	protected boolean displayValueInBorder() {
		return false;
	}
}
