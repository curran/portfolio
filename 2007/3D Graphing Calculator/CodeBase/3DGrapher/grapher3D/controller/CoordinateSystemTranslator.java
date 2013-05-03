package grapher3D.controller;

import grapher3D.Grapher3DConstants;

import java.util.Observable;
import java.util.Observer;

import valueTypes.StringValue;
import variables.Variable;

/**
 * The class which facilitates different coordinate systems, such as cartesian
 * or spherical. It Observes the
 * Grapher3DConstants.Grapher3DFunctionString_external Variable, and changes the
 * Grapher3DConstants.Grapher3DFunctionString_internal Variable appropriately
 * when it changes. <br>
 * <br>
 * Specifically, Grapher3DConstants.Grapher3DFunctionString_internal is set with
 * a StringValue containing a parametric surface function which describes the
 * function in externalFunctionString (which specifies dependent variables in a
 * particular coordinate system, not necessarily x, y, or z) which assigns x, y,
 * and z in terms of u and v.
 * 
 * @author Curran Kelleher
 * 
 */
public class CoordinateSystemTranslator implements Observer {
	Variable grapher3DFunctionString_external = Variable
			.getVariable(Grapher3DConstants.Grapher3DFunctionString_external);

	Variable grapher3DFunctionString_internal = Variable
			.getVariable(Grapher3DConstants.Grapher3DFunctionString_internal);

	CoordinateSystemSpecification currentTranslator = new CoordinateSystemSpecification(
			"executeFunction({x=u*20-10;y=v*20-10;#})");

	public CoordinateSystemTranslator() {
		grapher3DFunctionString_external
				.addObserver(
						this,
						"Controlled by the 3D grapher's function field; specifies the function to be graphed using the current coordinate space.");
	}

	/**
	 * Get the update from the external function string, then change the
	 * internal function string.
	 */
	public void update(Observable o, Object arg) {
		if (arg == grapher3DFunctionString_external) {
			grapher3DFunctionString_internal
					.set(new StringValue(
							currentTranslator
									.generateInternalFunction(grapher3DFunctionString_external
											.evaluate().toString())));
		}
	}

	/**
	 * Removes this as an Observer from everything it is observing, thus
	 * disabling it from doing anything.
	 * 
	 */
	public void shutDown() {
		grapher3DFunctionString_external.deleteObserver(this);
	}
}
