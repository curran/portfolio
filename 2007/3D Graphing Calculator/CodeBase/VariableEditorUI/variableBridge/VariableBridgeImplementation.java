package variableBridge;

import variables.Variable;

/**
 * The interface for the implementation of external to internal Variable
 * bridging.
 * 
 * @author Curran Kelleher
 * @see variableBridge.VariableBridge
 */
public interface VariableBridgeImplementation {
	/**
	 * Updates the contents of the internal Java variable corresponding to the
	 * specified external Variable.
	 * 
	 * @param variableToRead
	 *            the Variable which should be used for updating it's
	 *            corresponding internal Java variable.
	 */
	void updateInternalVariable(Variable variableToRead);

	/**
	 * Updates the specified external Variable based on the contents of it's
	 * corresponding internal Java variable.
	 * 
	 * @param variableToUpdate
	 *            the Variable which should be updated
	 */
	void updateExternalVariable(Variable variableToUpdate);
}
