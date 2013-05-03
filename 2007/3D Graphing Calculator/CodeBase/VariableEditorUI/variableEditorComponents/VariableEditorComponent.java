package variableEditorComponents;

/**
 * A common interface for all components which are bound to Variables.
 * 
 * @author Curran Kelleher
 * 
 */
public interface VariableEditorComponent {
	/**
	 * Updates the component so that it displays the current value of the
	 * variable it is bound to.
	 * 
	 */
	void updateWithCurrentVariableValue();
	
	/**
	 * Sets up a VariableEditorComponent such that
	 * updateWithCurrentVariableValue() will be called in it whenever this
	 * VariableEditorComponent changes the value of the Variable.
	 * 
	 * @param componentToUpdate
	 *            the VariableEditorComponent to update when this
	 *            VariableEditorComponent changes the value of the Variable
	 */
	void bindToVariableEditorComponent(
			VariableEditorComponent componentToUpdate);
}
