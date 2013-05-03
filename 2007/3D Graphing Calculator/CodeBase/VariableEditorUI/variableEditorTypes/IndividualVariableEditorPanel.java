package variableEditorTypes;

import javax.swing.JPanel;


/**
 * An interface for all types of Variable editor panels.
 * 
 * @author Curran Kelleher
 * 
 */
public abstract class IndividualVariableEditorPanel extends JPanel{

	/**
	 * @return true if the type of this editor panel is in conflict with the type of Value stored in the Variable.
	 */
	public abstract boolean hasTypeConflict();

}
