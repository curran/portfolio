package variableEditorComponents;

import java.awt.event.KeyEvent;

import parser.RecursiveDescentParser;
import variables.Variable;
import expressionConsole.ExpressionConsoleModel;

/**
 * A JTextField which is bound to a String Variable which is interpreted as a
 * function. When the user enters something in the text field and presses the
 * enter key, the content of the Variable is updated with the content of the
 * text field as a StringValue. When the Variable is changed from another
 * source, the content of the text field is updated with the new contents of the
 * Variable. <br>
 * Additional features are provided, considering that the String represents a
 * function. As the user is typing, if the text generates an error when
 * parsed and evaluated, the text turns red.
 * 
 * @author Curran Kelleher
 * 
 */
public class FunctionStringVariableBoundTextField extends
		StringVariableBoundTextField {
	private static final long serialVersionUID = 2384411990111675612L;

	/**
	 * The parser used to check for errors, to turn the text red if there is an
	 * error.
	 */
	RecursiveDescentParser parserForErrorChecking = ExpressionConsoleModel
			.getInstance().getParser();

	public FunctionStringVariableBoundTextField(Variable variable,
			String explainationForObserving) {
		super(variable, explainationForObserving);
	}

	/**
	 * Check for errors whenever the text is changed, turn the text red if there
	 * is an error. (To do this, the function is parsed and evaluated.)
	 */
	public void keyReleased(KeyEvent e) {
		//TODO this sucks, make it better
		//setForeground(parserForErrorChecking.parse("executeFunction({"+getText()+"})").evaluate() instanceof ErrorValue ? Color.red
			//	: Color.black);
	}
}
