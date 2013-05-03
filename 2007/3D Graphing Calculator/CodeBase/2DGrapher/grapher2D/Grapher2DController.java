package grapher2D;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextField;

import variables.Variable;

import expressionConsole.ExpressionConsoleModel;

/**
 * The controller of the Model View Controller paradigm for the Grapher. In this
 * paradigm, the model stores information necessary to produce the view (in this
 * case this information includes the function string, coordinate space, and
 * some other things.) The controller is the UI which modifies the model (in
 * this case it is the function field). The view is the graphical representation
 * of the model. Typically there is some overlap between the roles of the view
 * and the model (such as zooming by drawing a rectangle on the view, or
 * displayed parameters in the controller which must be updated to reflect
 * changes in the model. The model, view, and controller communicate using the
 * Observer design pattern. The model is the Observable, and the controller and
 * view are Observers of the model. When the model changes, it sends a
 * notification event to all Observers, thus signaling them to update so that
 * they reflect the current state of the model.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher2DController extends JPanel implements Observer,
		KeyListener {
	private static final long serialVersionUID = -4053890244503081186L;

	/**
	 * The text field in which the user enters the function.
	 */
	JTextField functionField;

	/**
	 * A flag to signify that updates from the model should be ignored.
	 */
	boolean ignoreUpdates = false;

	/**
	 * Upon construction, the function field is set up.
	 * 
	 */
	public Grapher2DController() {
		// create the funcion field and set up listening to it
		functionField = new JTextField();
		functionField.addKeyListener(this);

		// Observe the function string for changes
		Variable.getVariable(Grapher2DConstants.Grapher2DFunctionString)
				.addObserver(this,
						"The text displayed in the function text box.");

		// a GridLayout is used to make the functionField fill the panel space.
		setLayout(new GridLayout());

		// put the function field in the panel
		add(functionField);
	}

	/**
	 * Responds to notifications from the function string when it has changed.
	 */
	public void update(Observable o, Object arg) {
		// update the text to reflect the function string in the model.
		if (!ignoreUpdates)
			functionField.setText(Variable.getVariable(
					Grapher2DConstants.Grapher2DFunctionString).evaluate()
					.toString());
	}

	/**
	 * Responds to the enter key when it is pressed in the function field by
	 * updating the model.
	 * 
	 * @param e
	 *            the key event
	 */
	public void keyPressed(KeyEvent e) {
		// only act if the enter key has been pressed
		if (e.getKeyChar() == '\n') {
			// ignore the self-originated model update notification
			ignoreUpdates = true;

			// update the string function variable with the new function String.
			ExpressionConsoleModel.getInstance().enterExpression(
					Grapher2DConstants.Grapher2DFunctionString + " = \""
							+ functionField.getText() + "\"");
			// the function string variable is being observed by the view, so as
			// a result of this code execution, it will be appropriately
			// notified and will update itself.

			// but do not ignore subsequent updates
			ignoreUpdates = false;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}
}
