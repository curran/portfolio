package grapher3D;

import grapher3D.controller.Grapher3DController;
import grapher3D.controller.Grapher3DMenuBar;
import grapher3D.view.Grapher3DView;
import graphicsUtilities.GeneralGraphicsUtilities;

import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import logEnabledComponents.LogEnabledJFrame;
import valueTypes.StringValue;
import variables.Variable;

/**
 * The GUI for the 3D Grapher. This GUI uses the model, view, controller
 * paradigm. In this paradigm, the role of the model is to store all data. The
 * role of the view is to expose this data (the model) to the user. The role of
 * the controller is to let the user manipulate the model (the data). (There is
 * usually some overlap between the view and controller) <br>
 * In this GUI, the classes Grapher3DView and Grapher3DController are used as
 * the view and the controller. The class ExpressionConsoleModel, it's
 * associated RecursiveDescentParser, and the Variables that it uses are all
 * utilized as the model for the GUI.
 * 
 * @see grapher3D.controller.Grapher3DController
 * @see grapher3D.view.Grapher3DView
 * @see parser.RecursiveDescentParser
 * @see variables.Variable
 * @see expressionConsole.ExpressionConsoleModel
 * @see grapher3D.Grapher3DLaunchCommand
 * @see grapher3D.Grapher3DLauncher
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher3DGUI extends LogEnabledJFrame {
	private static final long serialVersionUID = 6379124215543930140L;

	/**
	 * The controller part of the GUI.
	 */
	Grapher3DController controller = new Grapher3DController();

	/**
	 * The view part of the GUI.
	 */
	Grapher3DView view = new Grapher3DView();;

	/**
	 * Constructs the 3D grapher GUI.
	 * 
	 */
	public Grapher3DGUI() {
		super(Grapher3DConstants.GUIFrameVariable);

		setUpSplitPaneGUI();

		setFrameSettings();

		setJMenuBar(Grapher3DMenuBar.createMenuBar(controller,view));

		Variable.getVariable(
				Grapher3DConstants.Grapher3DFunctionString_external).set(
				new StringValue("z = sin((x^2+y^2)*((sin(t)+1)/20)+t/2)"));
	}

	/**
	 * Sets up the split pane GUI with the controller and view.
	 * 
	 */
	private void setUpSplitPaneGUI() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				controller, view);
		splitPane.setDividerSize(3);
		splitPane.setEnabled(false);
		getContentPane().add(splitPane);
	}

	/**
	 * Sets various JFrame settings
	 * 
	 */
	private void setFrameSettings() {
		// set the bounds to center the frame
		GeneralGraphicsUtilities.centerFrame(this, 500,500);
		// set the title
		setTitle("Mathematorium");
		// set the frame to exit upon closing
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/**
	 * This frame gets disposed of whenever an action script is played, so all
	 * of it's children must be cleaned up
	 */
	public void dispose() {
		view.stopThread = true;
		controller.coordinateSystem.shutDown();
		super.dispose();
	}
}
