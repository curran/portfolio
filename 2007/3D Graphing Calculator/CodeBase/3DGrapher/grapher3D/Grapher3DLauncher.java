package grapher3D;

import java.awt.Toolkit;

import javax.swing.UIManager;

import logEnabledComponents.LogEnabledJFrame;
import parser.RecursiveDescentParser;
import variableEditorUI.VariableEditorUICommand;
import colorMapValue.CreateColorMapCommand;
import expressionConsole.ExpressionConsoleModel;
import expressionConsole.ExpressionConsoleUserManual;

/**
 * Launches the 3D Grapher.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher3DLauncher {

	/**
	 * Launches the 3D Grapher.
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

		//set up the icon
		LogEnabledJFrame.defaultIconImage = Toolkit.getDefaultToolkit().getImage("greenGraph.gif");
		
		//get the parser to work with
		RecursiveDescentParser parser = ExpressionConsoleModel.getInstance()
				.getParser();

		// setup the launchGraph3D() command
		Grapher3DLaunchCommand.setUpLaunchGraph3DCommand(parser);

		// setup the edit() command
		VariableEditorUICommand.setUpEditVariablesCommand(parser);

		// setup the userManual() command
		ExpressionConsoleUserManual.setUpUserManualCommand(parser);

		// setup the createColorMap() command
		CreateColorMapCommand.setUpCreateColorMapCommand(parser);

		// display the initial message
		ExpressionConsoleModel
				.getInstance()
				.enterMessage(
						"Welcome!\nType mathematical expressions or commands to have them evaluated. Enter \"userManual()\" to see the user manual.");

		// this line must be here, it enables the proper recording of history by
		// storing the initial state of the symbol table
		ExpressionConsoleModel.getInstance().recordInitialVariableValues();

		// launch the program via entering the command to do so
		ExpressionConsoleModel.getInstance().enterExpression("launchGraph3D()");
	}

}
