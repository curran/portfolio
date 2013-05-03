package grapher2D;

import java.awt.Dimension;

import javax.swing.UIManager;

import operators.UnaryOperator;
import operators.UnaryOperatorCreator;
import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.StringValue;
import variableEditorUI.VariableEditorUICommand;
import expressionConsole.ExpressionConsoleGUI;
import expressionConsole.ExpressionConsoleModel;
import expressionConsole.ExpressionConsoleUserManual;

/**
 * Launches the 2D Grapher.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher2DLauncher {

	/**
	 * Launches the 2D Grapher.
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

		RecursiveDescentParser parser = ExpressionConsoleModel.getInstance()
				.getParser();

		// setup the launchGraph2D() command
		parser.addUnaryOperator("launchGraph2D", new UnaryOperatorCreator() {
			public UnaryOperator create(ExpressionNode child) {
				return new UnaryOperator(child) {
					public Value evaluate() {

						// create the GUI Dimension
						Dimension guiDimension = new Dimension(500, 500);
						Grapher2DGUI gui = new Grapher2DGUI(guiDimension);

						// set the frame to exit on close
						// gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

						// show it
						gui.setVisible(true);

						return new StringValue("Successfully launched Graph2D");
					}
				};
			}

			public int getType() {
				return UnaryOperatorCreator.COMMAND;
			}

			public String getDescription() {
				return "launches the 2D grapher";
			}
		});

		// setup the edit() command
		VariableEditorUICommand.setUpEditVariablesCommand(parser);

		// setup the userManual() command
		ExpressionConsoleUserManual.setUpUserManualCommand(parser);

		// create the console GUI
		Dimension guiDimension = new Dimension(500, 500);
		ExpressionConsoleGUI gui = new ExpressionConsoleGUI(guiDimension);

		// show it
		gui.setVisible(true);

		// display the initial message
		ExpressionConsoleModel
				.getInstance()
				.enterMessage(
						"Welcome!\nType mathematical expressions or commands to have them evaluated. Enter \"userManual()\" to see the user manual.");
		
//		this line must be here, it enables the proper recording of history
		ExpressionConsoleModel.getInstance().recordInitialVariableValues();
		
		ExpressionConsoleModel.getInstance().enterExpression("launchGraph2D()");
		
		
	}

}
