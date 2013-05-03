package grapher3D.view;

import expressionConsole.ExpressionConsoleModel;
import grapher3D.Grapher3DConstants;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import parser.ExpressionNode;
import primitives3D.SolidRod3D;
import variables.Variable;

/**
 * The view part of the model, view, controller paradigm used in the 3D Grapher
 * GUI.
 * 
 * @see grapher3D.Grapher3DGUI
 * @see grapher3D.controller.Grapher3DController
 * @author Curran Kelleher
 * 
 */
public class Grapher3DView extends VariableBridgedGraph3DViewingPanel implements
		Observer {
	private static final long serialVersionUID = -6138280144691595668L;

	/**
	 * A reference to the variable which will hold in it a string, which is the
	 * function to be graphed.
	 */
	Variable functionStringVariable;

	/**
	 * The evaluation tree for the expression which is used to reset t when the
	 * function is changed.
	 */
	ExpressionNode tResetEvaluationTree = ExpressionConsoleModel.getInstance()
			.getParser().parse("t=0");

	/**
	 * Construct a Grapher3DView
	 */
	public Grapher3DView() {
		// observe the function string variable for updates
		Variable
				.getVariable(
						Grapher3DConstants.Grapher3DFunctionString_internal)
				.addObserver(
						this,
						"A parametric surface function which describes the function to be graphed. Observed by the 3D graph view so the drawing gets updated when it changes.");

		// set our reference to the observed variable. This will be used later
		// to check updates against.
		functionStringVariable = Variable
				.getVariable(Grapher3DConstants.Grapher3DFunctionString_internal);
		
		// set this flag so the lines will draw smoothly on screen
	SolidRod3D.drawAsSimpleLine= true;
	}

	/**
	 * Recieve the update notification from the function variable (<code>Grapher3DConstants.Grapher3DFunctionString</code>)
	 */
	public void update(Observable o, Object arg) {
		if (arg == functionStringVariable) {
			ExpressionNode newFunction = ExpressionConsoleModel.getInstance()
					.getParser().parse(
							functionStringVariable.evaluate().toString());
			String errorMessage = graph.checkForErrorsInFunction(newFunction);
			if (errorMessage == null) {
				// if no errors, proceed
				graph.function = newFunction;
				tResetEvaluationTree.evaluate();
				graph.calculateGrid();
			} else
				JOptionPane.showMessageDialog(null, errorMessage, "Error",
						JOptionPane.ERROR_MESSAGE);
		}
	}
}
