package grapher3D.controller;

import grapher3D.Grapher3DConstants;

import java.awt.GridLayout;

import javax.swing.JPanel;

import variableEditorComponents.FunctionStringVariableBoundTextField;
import variables.Variable;

/**
 * The controller part of the model, view, controller paradigm used in the 3D
 * Grapher GUI.
 * 
 * @see grapher3D.Grapher3DGUI
 * @see grapher3D.view.Grapher3DView
 * @author Curran Kelleher
 * 
 */
public class Grapher3DController extends JPanel {
	private static final long serialVersionUID = -4053890244503081186L;

	/**
	 * The current coordinate system. This class recieves updates from the
	 * external function variable, and in turn updates the internal function
	 * variable with the fully specified parametric surface function.
	 */
	public CoordinateSystemTranslator coordinateSystem = new CoordinateSystemTranslator();

	/**
	 * Upon construction, the function field is set up.
	 * 
	 */
	public Grapher3DController() {
		// a GridLayout is used to make the functionField fill the panel space.
		setLayout(new GridLayout());

		// put the function field in the panel
		add(new FunctionStringVariableBoundTextField(
				Variable
						.getVariable(Grapher3DConstants.Grapher3DFunctionString_external),
				"Controlled by the 3D grapher's function field."));
	}
}
