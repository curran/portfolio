package grapher3D.view;

import grapher3D.Grapher3DConstants;
import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import valueTypes.DecimalValue;
import variables.Variable;

/**
 * A panel which draws explicit 3D graphs and allows the user to rotate the view
 * of them with the mouse. The t Variable is incremented by tIncrement and the
 * graph is recalculated every frame.
 * 
 * @author Curran Kelleher
 * 
 */
public class AnimatedGraph3DViewingPanel extends Graph3DViewingPanel {
	private static final long serialVersionUID = -1838147037108342797L;

	/**
	 * References to the t and tIncrement Variables.
	 */
	Variable tIncrementVar = Variable
			.getVariable(Grapher3DConstants.TimeIncrement);

	/**
	 * The evaluation tree for the expression which is used to increment t each
	 * frame.
	 */
	ExpressionNode tIncrementEvaluationTree = new RecursiveDescentParser()
			.parse("t=t+tIncrement");

	/**
	 * A flag which is set to true after the constructor has finished executing.
	 * This prevents the method updateForEachFrame() (which is called by another
	 * thread before the constructor finished) from accessing anything before
	 * it's there, which would otherwise result in a nullpointer exception.
	 */
	boolean allSet = false;

	/**
	 * Construct an InstrumentedGraph3DViewingPanel
	 * 
	 */
	public AnimatedGraph3DViewingPanel() {
		tIncrementVar.set(new DecimalValue(0.1));
		allSet = true;
	}

	/**
	 * Called every frame; update t and recalculate the graph.
	 * 
	 */
	protected void updateForEachFrame() {
		if (allSet) {
			// increment t
			tIncrementEvaluationTree.evaluate();
			// re-calculate the grid
			graph.calculateGrid();
		}
		super.updateForEachFrame();
	}
}
