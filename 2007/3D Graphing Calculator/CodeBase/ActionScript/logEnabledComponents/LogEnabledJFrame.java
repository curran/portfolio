package logEnabledComponents;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import parser.ExpressionList;
import parser.ExpressionNode;
import parser.Value;
import valueTypes.DecimalValue;
import variables.Variable;
import actionScript.ActionScriptFlags;
import expressionConsole.ExpressionConsoleModel;

/**
 * A JFrame which has all of it's actions logged for later replay.
 * 
 * @author Curran Kelleher
 * 
 */
public class LogEnabledJFrame extends JFrame implements ComponentListener,
		WindowListener, Observer {
	private static final long serialVersionUID = 4826646701968523374L;

	/**
	 * The icon image that all newly created frames get.
	 */
	public static Image defaultIconImage = null;

	/**
	 * A list of all names which have been used in LogEnabledJFrames. It is
	 * checked against for every new name to ensure uniqueness.
	 */
	private static List<String> allUniqueNames = new LinkedList<String>();

	/**
	 * the Variable which will be used to keep track of this frame's parameters.
	 * (the "parameter variable")
	 */
	Variable thisFramesParameterVariable;

	/**
	 * A reference to the console model so getInstance() only needs to be called
	 * once.
	 */
	ExpressionConsoleModel console = ExpressionConsoleModel.getInstance();

	/**
	 * A flag which is set to false when the update is self-generated (to
	 * prevent an infinite loop)
	 */
	boolean respondToUpdates = true;

	/**
	 * If this is false, logging is disabled for this JFrame. It is here for
	 * constructors and methods to use, so events generated programmatically by
	 * code in the subclass don't get logged as user-generated events would. The
	 * code should set this to false, do the action, then set this to true
	 * again.
	 */
	private boolean logEventsForThisFrame = true;

	/**
	 * For keeping track of the bounds for checking if they have changed.
	 */
	Rectangle previousBounds = new Rectangle();

	/**
	 * Keeps track of the extended state which is stored in the variable, used
	 * for checking if it has changed.
	 */
	int previousExtendedState = -5;

	/**
	 * Constructs a LogEnabledJFrame whose parameters will be kept track of by
	 * the Variable with the specified name.
	 * 
	 * @param uniqueName
	 *            the nme of the Variable which will be used to keep track of
	 *            this frame's parameters. It must be unique, something long
	 *            like "grapher2DGUIFrame" is recommended.
	 */
	public LogEnabledJFrame(String uniqueName) {
		if (allUniqueNames.contains(uniqueName))
			console
					.enterErrorMessage("There already exists a frame with the name "
							+ uniqueName
							+ "! After this, logging and replay may not work correctly.");
		else
			allUniqueNames.add(uniqueName);

		Variable.getVariable(uniqueName).addObserver(this,
				"Used by a frame for logging it's parameters");
		// this is called in this manner because the act of adding the observer
		// replaces the existing Variable with an ObservableVariable in the
		// symbol table.
		thisFramesParameterVariable = Variable.getVariable(uniqueName);
		addComponentListener(this);
		addWindowListener(this);

		// Set the icon
		if (defaultIconImage != null)
			setIconImage(defaultIconImage);
	}

	/**
	 * Updates the value of the parameters variable with the curren bounds via a
	 * command on the console.
	 * 
	 */
	private void updateParameters() {
		if (ActionScriptFlags.LOGGING_ENABLED && logEventsForThisFrame) {
			Rectangle newBoundsRect = getBounds();
			if (!previousBounds.equals(newBoundsRect)
					|| previousExtendedState != getExtendedState()) {
				// don't respond to self-generated updates.
				respondToUpdates = false;

				console.enterExpression(thisFramesParameterVariable.toString()
						+ " = [" + newBoundsRect.x + ", " + newBoundsRect.y
						+ ", " + newBoundsRect.width + ", "
						+ newBoundsRect.height + ", " + getExtendedState()
						+ "]");

				// do respond to other updates.
				respondToUpdates = true;
			}
		}
	}

	public void componentResized(ComponentEvent e) {
		updateParameters();
	}

	public void componentMoved(ComponentEvent e) {
		updateParameters();
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * Gets updates whenever the parameter variable changes. If the parameter
	 * variable gets set to 0, this frame is closed and disposed of.
	 * 
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if (respondToUpdates)
			if (arg == thisFramesParameterVariable) {
				boolean invalidValue = false;
				Value currentValue = thisFramesParameterVariable.evaluate();

				// If the parameter variable gets set to 0, this frame is closed
				// and disposed of.
				if (currentValue instanceof DecimalValue)
					if (((DecimalValue) currentValue).value == 0) {
						allUniqueNames.remove(thisFramesParameterVariable
								.toString());
						o.deleteObserver(this);
						dispose();
					} else
						invalidValue = true;
				else if (currentValue instanceof ExpressionList) {
					ExpressionNode[] allParameters = ((ExpressionList) currentValue)
							.getNodes();
					if (allParameters.length == 5) {
						int[] parameterValues = new int[5];
						for (int i = 0; i < parameterValues.length
								&& !invalidValue; i++) {
							Value xValue = allParameters[i].evaluate();
							if (xValue instanceof DecimalValue)
								parameterValues[i] = (int) ((DecimalValue) xValue).value;
							else
								invalidValue = true;
						}
						if (!invalidValue) {
							// first make sure the values are not negative
							for (int i = 0; i < parameterValues.length - 1; i++)
								parameterValues[i] = parameterValues[i] < 1 ? 1
										: parameterValues[i];

							// *********************************
							// get the bounds out of the first 4
							Rectangle newBoundsRect = new Rectangle(
									parameterValues[0], parameterValues[1],
									parameterValues[2], parameterValues[3]);

							previousBounds = newBoundsRect;
							previousExtendedState = parameterValues[4];

							if (!newBoundsRect.equals(getBounds()))
								setBounds(newBoundsRect);

							// *********************************
							// get the extended state out of the 5th
							if (getExtendedState() != parameterValues[4])
								setExtendedState(parameterValues[4]);
						}
					} else
						invalidValue = true;
				} else
					invalidValue = true;

				if (invalidValue) {
					console
							.enterErrorMessage(thisFramesParameterVariable
									.toString()
									+ " is being used by a frame and has been set to an invalid value, "
									+ currentValue
									+ ". It will be reset to the frame's current parameters.");
					updateParameters();
				}

			}
	}

	/*
	 * public void dispose() {
	 * allUniqueNames.remove(thisFramesParameterVariable.toString());
	 * super.dispose(); }
	 */

	/**
	 * Ensure that code which calls setVisible() does not cause events to be
	 * logged.
	 */
	public void setVisible(boolean b) {
		// make sure the events generated by calling
		// setVisible() are not logged.
		logEventsForThisFrame = false;
		super.setVisible(b);

		// log real events hereafter
		logEventsForThisFrame = true;
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		// log the destruction of this frame so that it replays properly.
		if (getDefaultCloseOperation() == WindowConstants.DISPOSE_ON_CLOSE)
			console.enterExpression(thisFramesParameterVariable.toString()
					+ " = 0");
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
		updateParameters();
	}

	public void windowDeiconified(WindowEvent e) {
		updateParameters();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
