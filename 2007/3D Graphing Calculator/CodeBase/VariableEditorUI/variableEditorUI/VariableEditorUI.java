package variableEditorUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import variables.Variable;

/**
 * A panel which contains panels for editing Variables.
 * 
 * @author Curran Kelleher
 * 
 */
public class VariableEditorUI extends JPanel {
	private static final long serialVersionUID = 9099313244057866317L;

	/**
	 * Construct a VariableEditorUI which will contain a variable editor panel
	 * for each of the specified Variables.
	 * 
	 * @param variables
	 * @param parser
	 */
	public VariableEditorUI(List<Variable> variables) {
		setLayout(new GridLayout(variables.size(), 1));
		for(Iterator<Variable> it = variables.iterator();it.hasNext();)
			add(new VariableEditorPanel(it.next()));
		setPreferredSize(new Dimension(200,variables.size()*55));
	}
}
