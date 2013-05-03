package variableEditorComponents;

import parser.Value;
import variables.Variable;
import expressionConsole.ExpressionConsoleModel;

/**
 * A JTextField which is bound to a String Variable. When the user enters
 * something in the text field and presses the enter key, the content of the
 * Variable is updated with the content of the text field as a StringValue. When
 * the Variable is changed from another source, the content of the text field is
 * updated with the new contents of the Variable.
 * 
 * @author Curran Kelleher
 * 
 */
public class StringVariableBoundTextField extends VariableBoundTextField {
	private static final long serialVersionUID = 2439761192389225962L;

	/**
	 * Construct a StringVariableBoundTextField which is bound to the specified
	 * Variable, which should contain a StringValue.
	 * 
	 * @param variable
	 *            the variable to edit
	 */
	public StringVariableBoundTextField(Variable variable,String explainationForObserving) {
		super(variable);
		setUpAsDirectObserver(explainationForObserving);
	}
	
	/**
	 * Called when the user enters text and hits the enter key. This method
	 * updates the contents of the variable to displayedValue, which is set to
	 * the contents of the text field after being parsed and evaluated.
	 * 
	 * @param text
	 *            the text in the text field when the user hit the enter key.
	 */
	protected void processInputText(String text) {
		// log the change for correct replay
		ExpressionConsoleModel.getInstance().enterExpression(
				variable.toString() + " = \"" + getText()+"\"");
	}
	
	/**
	 * Updates the content of the text field to reflect the current value of the
	 * variable.
	 * 
	 */
	public void updateWithCurrentVariableValue() {
		Value currentValue = variable.evaluate();
		
		displayedValue = currentValue.toString();
		setText(displayedValue);	
	}
}
