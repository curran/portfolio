package example;

import java.awt.Dimension;

import javax.swing.UIManager;

import expressionConsole.ExpressionConsoleGUI;
import expressionConsole.ExpressionConsoleModel;

/**
 * A simple expression console demonstration program.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleLauncher {

	/**
	 * Launches the expression console demonstration program.
	 * 
	 * @param args
	 *            command line arguments, not used.
	 */
	public static void main(String[] args) {
		// set the native system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// create the GUI
		Dimension guiDimension = new Dimension(500, 500);
		ExpressionConsoleGUI gui = new ExpressionConsoleGUI(guiDimension);

		// display the initial message
		ExpressionConsoleModel
				.getInstance()
				.enterMessage(
						"Welcome!\nType mathematical expressions to have them evaluated, for example...");
		ExpressionConsoleModel.getInstance().enterExpression(
				"(1+(2-3)*4)/5^6E-7%8!");

		// show it
		gui.setVisible(true);
	}

}
