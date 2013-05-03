package logEnabledComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JRadioButtonMenuItem;

import parser.Value;
import valueTypes.DecimalValue;
import variables.Variable;
import actionScript.ActionScriptFlags;
import expressionConsole.ExpressionConsoleModel;

/**
 * A JButton which has all of it's actions logged for later replay.
 * 
 * @author Curran Kelleher
 * 
 */
public class LogEnabledJRadioButtonMenuItem extends JRadioButtonMenuItem implements Observer,
		ActionListener {
	private static final long serialVersionUID = -8162310483610735221L;

	/**
	 * A list of all names which have been used. It is checked against for every
	 * new name to ensure uniqueness.
	 */
	private static List<String> allUniqueNames = new LinkedList<String>();

	/**
	 * the Variable which will be used to keep track of this object's
	 * parameters.
	 */
	Variable thisObjectsParameterVariable;

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
	 * If this is false, logging is disabled for this object. It is here for
	 * constructors and methods to use, so events generated programmatically by
	 * code in the subclass don't get logged as user-generated events would. The
	 * code should set this to false, do the action, then set this to true
	 * again.
	 */
	private boolean logEventsForThisObject = true;

	/**
	 * The number of times this button has been clicked.
	 */
	private int clickCount = 0;

	/**
	 * Constructs a LogEnabledJButton whose click actions will be logged into
	 * the Variable with the specified name in the for of click count. If the
	 * number is set to 0, then no click occurs. Setting the variable to any
	 * number except 0 would cause a click event to occur.
	 * @param text the text of the <code>JRadioButtonMenuItem</code> 
	 * @param uniqueName
	 *            the name of the Variable which will be used to keep track of
	 *            the click count. It must be unique, something long is
	 *            recommended.
	 */
	public LogEnabledJRadioButtonMenuItem(String text, String uniqueName) {
		super(text);
		if (allUniqueNames.contains(uniqueName))
			console
					.enterErrorMessage("There already exists a button with the name "
							+ uniqueName
							+ "! After this, logging and replay may not work correctly.");
		else
			allUniqueNames.add(uniqueName);

		Variable.getVariable(uniqueName).addObserver(
				this,
				"Used by a radio button (" + uniqueName
						+ ") for logging it's clicks.");
		// this is called in this manner because the act of adding the observer
		// replaces the existing Variable with an ObservableVariable in the
		// symbol table.
		thisObjectsParameterVariable = Variable.getVariable(uniqueName);
		addActionListener(this);
	}

	/**
	 * Called when the button is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		if (ActionScriptFlags.LOGGING_ENABLED && logEventsForThisObject) {
			// don't respond to self-generated updates.
			respondToUpdates = false;

			// increment the click count
			clickCount++;

			// log the click
			console.enterExpression(thisObjectsParameterVariable.toString()
					+ " = " + clickCount);

			// do respond to other updates.
			respondToUpdates = true;
		}
	}

	/**
	 * Gets updates whenever the parameter variable changes, and clicks the
	 * button. If the variable is 0, then the button is not clicked, and it is unbound from it's variable.
	 * 
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if (respondToUpdates)
			if (arg == thisObjectsParameterVariable) {
				boolean invalidValue = false;
				Value currentValue = thisObjectsParameterVariable.evaluate();

				if (currentValue instanceof DecimalValue) {
					if (((DecimalValue) currentValue).value != 0)
						this.doClick(0);
					else
					{
						allUniqueNames.remove(thisObjectsParameterVariable.toString());
						o.deleteObserver(this);
					}
				} else
					invalidValue = true;

				if (invalidValue) {
					respondToUpdates = false;
					thisObjectsParameterVariable.set(new DecimalValue(
							clickCount));
					respondToUpdates = true;
					console
							.enterErrorMessage(thisObjectsParameterVariable
									.toString()
									+ " is being used by a button and has been set to an invalid value, "
									+ currentValue
									+ ". It has been reset to it's previous value.");
				}

			}
	}
}
