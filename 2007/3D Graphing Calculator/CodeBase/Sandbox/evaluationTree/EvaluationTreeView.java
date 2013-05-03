package evaluationTree;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class EvaluationTreeView extends JPanel implements Observer {
	public EvaluationTreeView() {
		EvaluationTreeGUI.EXPRESSION_STRING_VARIABLE
				.addObserver(this, "");
	}

	public void update(Observable o, Object arg) {
		removeAll();
		String expressionString = EvaluationTreeGUI.EXPRESSION_STRING_VARIABLE.evaluate()
						.toString();
		add(new JLabel(expressionString));
		doLayout();
	}
}
