package variableBridge;

/**
 * A class which contains all the information necessary to, when used in a
 * 
 * @link variableBridge.VariableBridge, fully bridge, or link, a single Variable
 *       object to an internal Java variable.
 * @author Curran Kelleher
 * 
 */
public class IndividualVariableBridge {
	/**
	 * the name of the Variable object to bridge
	 */
	public final String variableName;

	/**
	 * the explaination of what this Variable will do
	 */
	public final String explaination;

	/**
	 * the implementation of VariableBridgeImplementation which actually carries
	 * out the transfer of information between the Variable object and the
	 * corresponding internal Java variable.
	 */
	public final VariableBridgeImplementation bridgeImplementation;

	/**
	 * Construct a IndividualVariableBridge which will bridge the Variable
	 * object which the specified name (variableName) to an internal Java
	 * variable via the specified VariableBridgeImplementation.
	 * 
	 * @param variableName
	 *            the name of the Variable object to bridge
	 * @param explaination
	 *            the explaination of what this Variable will do
	 * @param bridgeImplementation
	 *            the implementation of VariableBridgeImplementation which
	 *            actually carries out the transfer of information between the
	 *            Variable object and the corresponding internal Java variable.
	 */
	public IndividualVariableBridge(String variableName, String explaination,
			VariableBridgeImplementation bridgeImplementation) {
		this.variableName = variableName;
		this.explaination = explaination;
		this.bridgeImplementation = bridgeImplementation;

	}
}
