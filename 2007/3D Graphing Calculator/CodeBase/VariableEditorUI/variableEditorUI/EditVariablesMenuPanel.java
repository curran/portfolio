package variableEditorUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import parser.ExpressionNode;
import valueTypes.CharacterSeparatedStatementPair;
import valueTypes.ErrorValue;
import variables.Variable;
import expressionConsole.ExpressionConsoleModel;

/**
 * The panel used in the menu which lets the user enter a comma separated list
 * of variable names to edit when the enter key is pressed.
 * 
 * @author Curran Kelleher
 * 
 */
public class EditVariablesMenuPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = -9092708506771144827L;

	JTextField textField = new JTextField();

	/**
	 * Constructs a new EditVariablesMenuPanel, ready to go.
	 * 
	 */
	public EditVariablesMenuPanel() {
		textField.addKeyListener(this);
		add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JLabel(
				"Variable(s) to edit...(ex. \"a, b, c\")"), textField));
	}

	public void keyTyped(KeyEvent e) {
	}

	/**
	 * When the enter key is pressed, the command "edit(" + textField.getText() +
	 * ")" is executed in the console.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			if (textIsValid()) {
				ExpressionConsoleModel.getInstance().enterExpression(
						"edit(" + textField.getText() + ")");
			} else
				JOptionPane.showMessageDialog(this, "Invalid text! "
						+ textField.getText()
						+ " is not a comma separated list of variable names.",
						"Invalid text!", JOptionPane.ERROR_MESSAGE);

			// clear the text
			textField.setText("");
		}

	}

	/**
	 * Tests to see if the text in the text box is valid.
	 * 
	 * @return
	 */
	private boolean textIsValid() {
		ExpressionNode evalTree = ExpressionConsoleModel.getInstance()
				.getParser().parse(textField.getText());
		if (evalTree instanceof CharacterSeparatedStatementPair) {
			CharacterSeparatedStatementPair statements = (CharacterSeparatedStatementPair) evalTree;
			if (statements.getSymbol().equals(",")) {
				ExpressionNode[] allVars = statements.extractAllStatements();
				if (allVars.length == 1)
					if (allVars[0] instanceof ErrorValue)
						// this happens when the separator characters are
						// inconsistent
						return false;
				return true;
			} else
				return false;
		} else if (evalTree instanceof Variable)
			return true;
		else
			return false;
	}

	public void keyReleased(KeyEvent e) {
	}
}
