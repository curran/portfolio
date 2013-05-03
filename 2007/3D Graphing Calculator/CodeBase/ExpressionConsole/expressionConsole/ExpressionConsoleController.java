package expressionConsole;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The controller of the Model View Controller paradigm for the Expression Console.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleController extends JPanel implements KeyListener {
	private static final long serialVersionUID = -4053890244503081186L;

	/**
	 * The text field in which the user enters the function.
	 */
	JTextField functionField;

	/**
	 * A reference to the singleton model. (so we don't need call getInstance()
	 * many times)
	 */
	ExpressionConsoleModel model = ExpressionConsoleModel.getInstance();

	/**
	 * Keeps track of where the user is in the history when using the up and
	 * down arrows to navigate it.
	 */
	int currentInputEntryIndex = 0;

	/**
	 * A flag reflecting whether or not the control key is pressed down
	 */
	boolean controlKeyIsDown = false;

	/**
	 * Upon construction, the function field is set up.
	 * 
	 */
	public ExpressionConsoleController() {
		// create the funcion field and set up listening to it
		functionField = new JTextField();
		functionField.addKeyListener(this);
		// set the background and foreground (default for text) colors
		functionField.setBackground(Color.black);
		functionField.setForeground(Color.orange);

		// set the font
		functionField.setFont(Font.decode("Monospaced-PLAIN-13"));
		functionField.setCaretColor(Color.white);

		// a GridLayout is used to make the functionField fill the panel space.
		setLayout(new GridLayout());

		// put the function field in the panel
		add(functionField);
	}

	/**
	 * Called when a key is pressed when the function field has focus
	 */
	public void keyPressed(KeyEvent e) {
		// if the enter key was pressed
		if (e.getKeyChar() == '\n') {
			// send the command string to the model
			model.enterExpression(functionField.getText());

			// clear the function field
			functionField.setText("");

			// set the current place for navigation using the up/down arrows
			currentInputEntryIndex = model.getExpressionHistory()
					.getExpressionList().size() - 1;
		} else if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			controlKeyIsDown = true;
		// the user can navigate the history of inputs using the up/down arrows
		else if (e.getKeyCode() == KeyEvent.VK_UP) {
			List<ExpressionConsoleHistoryEntry> history = model
					.getExpressionHistory().getExpressionList();
			for (int i = currentInputEntryIndex - 1; i > 0; i--) {
				ExpressionConsoleHistoryEntry currentEntry = history.get(i);
				if (currentEntry.getType() == ExpressionConsoleHistoryEntry.INPUT
						|| (controlKeyIsDown ? currentEntry.getType() == ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_SUCCESS
								: false)) {
					functionField.setText(currentEntry.getString());
					currentInputEntryIndex = i;
					break;
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			List<ExpressionConsoleHistoryEntry> history = model
					.getExpressionHistory().getExpressionList();

			for (int i = currentInputEntryIndex + 1; i < history.size(); i++) {
				ExpressionConsoleHistoryEntry currentEntry = (ExpressionConsoleHistoryEntry) history
						.get(i);
				if (currentEntry.getType() == ExpressionConsoleHistoryEntry.INPUT
						|| (controlKeyIsDown ? currentEntry.getType() == ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_SUCCESS
								: false)) {
					functionField.setText(currentEntry.getString());
					currentInputEntryIndex = i;
					break;
				}
			}
		}
	}

	/**
	 * Called when a key is released when the function field has focus
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			controlKeyIsDown = false;
	}

	/**
	 * Called when a key is typed when the function field has focus
	 */
	public void keyTyped(KeyEvent e) {

	}
}
