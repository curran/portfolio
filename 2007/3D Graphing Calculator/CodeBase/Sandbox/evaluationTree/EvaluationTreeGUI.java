package evaluationTree;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import variableEditorComponents.FunctionStringVariableBoundTextField;
import variables.Variable;
import graphicsUtilities.GeneralGraphicsUtilities;

@SuppressWarnings("serial")
public class EvaluationTreeGUI extends JFrame {
	public static final Variable EXPRESSION_STRING_VARIABLE = Variable.getVariable("expressionString");

	public EvaluationTreeGUI() {
		//set up the split pane
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				new FunctionStringVariableBoundTextField(EXPRESSION_STRING_VARIABLE,""), new EvaluationTreeView());
		splitPane.setDividerSize(0);
		splitPane.setEnabled(false);
		
//		set up the frame
		GeneralGraphicsUtilities.setNativeLookAndFeel();
		GeneralGraphicsUtilities.centerFrame(this, 500, 500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().add(splitPane);
	}
	
	public static void main(String[] args)
	{
		(new EvaluationTreeGUI()).setVisible(true);
	}
}
