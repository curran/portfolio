package grapher3D.controller;

import grapher3D.Grapher3DConstants;
import variables.Variable;

/**
 * Defines all the information necessary for a functioning coordinate system.
 * 
 * @author Curran Kelleher
 * 
 */
public class CoordinateSystem {
	/**
	 * for translating functions in this coordinate system into parametric
	 * surface functions.
	 */
	final CoordinateSystemSpecification translationSpecification;

	/**
	 * The function which is associated with this coordinate system.
	 */
	String functionString;

	/**
	 * the title of the coordinate system, which could be displayed to the user
	 * to describe it. For example, "z = f(x,y)"
	 */
	public final String title;

	/**
	 * An alphanumeric name for this coordinate system.
	 */
	public final String alphaNumericName;

	/**
	 * the mnemonic for this CoordinateSystem
	 */
	public final char mnemonic;

	/**
	 * The command used to edit the range of this coordinate space.
	 */
	public final String editRangeCommand;

	/**
	 * The expression which is evaluated when this coordinate system is put into
	 * effect.
	 */
	public final String initializingCommand;

	/**
	 * The expression which is evaluated when this coordinate system is taken
	 * out of effect.
	 */
	public final String exitingCommand;

	/**
	 * Construct a CoordinateSystem with the specified parameters.
	 * 
	 * @param title
	 *            the title of the coordinate system, which could be displayed
	 *            to the user to describe it. For example, "Cartesian z =
	 *            f(x,y)"
	 * @param mnemonic
	 *            the mnemonic which goes along with the title for this
	 *            CoordinateSystem
	 * @param translationString
	 *            Specifies the parametric surface function where '#' will be
	 *            replaced by the function in the coordinate space.
	 * @param initialFunctionString
	 *            the initial function in this coordinate system.
	 * @param alphaNumericName
	 *            the name which represents this coordinate system in Variable
	 *            space.
	 * @param editRangeCommand
	 *            the command to be entered in the console for editing the range
	 *            of this coordinate system.
	 */
	public CoordinateSystem(String title, char mnemonic,
			String translationString, String initialFunctionString,
			String alphaNumericName, String editRangeCommand) {
		this(title, mnemonic, translationString, initialFunctionString,
				alphaNumericName, editRangeCommand, null, null);
	}

	/**
	 * Construct a CoordinateSystem with the specified parameters.
	 * 
	 * @param title
	 *            the title of the coordinate system, which could be displayed
	 *            to the user to describe it. For example, "Cartesian z =
	 *            f(x,y)"
	 * @param mnemonic
	 *            the mnemonic which goes along with the title for this
	 *            CoordinateSystem
	 * @param translationString
	 *            Specifies the parametric surface function where '#' will be
	 *            replaced by the function in the coordinate space.
	 * @param initialFunctionString
	 *            the initial function in this coordinate system.
	 * @param alphaNumericName
	 *            the name which represents this coordinate system in Variable
	 *            space.
	 * @param editRangeCommand
	 *            the command to be entered in the console for editing the range
	 *            of this coordinate system.
	 * @param initializingCommand
	 *            The expression string which is evaluated when this coordinate
	 *            system is put into effect. (null is ok too)
	 * @param exitingCommand
	 *            The expression string which is evaluated when this coordinate
	 *            system is taken out of effect. (null is ok too)
	 */
	public CoordinateSystem(String title, char mnemonic,
			String translationString, String initialFunctionString,
			String alphaNumericName, String editRangeCommand,
			String initializingCommand, String exitingCommand) {
		this.editRangeCommand = editRangeCommand;
		this.mnemonic = mnemonic;
		translationSpecification = new CoordinateSystemSpecification(
				translationString);
		functionString = initialFunctionString;
		this.title = title;
		this.alphaNumericName = alphaNumericName;
		this.initializingCommand = initializingCommand;
		this.exitingCommand = exitingCommand;
	}

	/**
	 * Saves the current function into this CoordinateSystem for next time.
	 * 
	 */
	public void saveState() {
		functionString = Variable.getVariable(
				Grapher3DConstants.Grapher3DFunctionString_external).evaluate()
				.toString();
	}
}
