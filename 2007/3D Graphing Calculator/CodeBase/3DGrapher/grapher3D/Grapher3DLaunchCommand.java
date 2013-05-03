package grapher3D;

import javax.swing.WindowConstants;

import operators.UnaryOperator;
import operators.UnaryOperatorCreator;
import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.StringValue;

/**
 * This class provides a utility method for setting up the launchGraph3D()
 * command, which launches the 3D grapher user interface.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher3DLaunchCommand {

	/**
	 * The name of the launch command
	 */
	public static final String LAUNCH_COMMAND = "launchGraph3D";
	/**
	 * Sets up the launchGraph3D() command in the specified parser. This command
	 * launches the 3D grapher user interface.
	 * 
	 * @param parser
	 *            The parser to add the launchGraph3D() command to as a unary
	 *            operator
	 */
	public static void setUpLaunchGraph3DCommand(RecursiveDescentParser parser) {
		parser.addUnaryOperator(LAUNCH_COMMAND, new UnaryOperatorCreator() {
			public UnaryOperator create(ExpressionNode child) {
				return new UnaryOperator(child) {
					public Value evaluate() {

						// create the GUI
						Grapher3DGUI gui = new Grapher3DGUI();

						// set the frame to exit on close
						gui
								.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

						// show it
						gui.setVisible(true);

						return new StringValue("Successfully launched Graph3D");
					}
				};
			}

			public int getType() {
				return UnaryOperatorCreator.COMMAND;
			}

			public String getDescription() {
				return "launches the 3D grapher";
			}
		});
	}
}
