package variableEditorComponents;

import valueTypes.IntegerValue;
import variables.Variable;

/**
 * A JSlider which is bound to a Variable which contains an IntegerValue. When
 * the user moves the slider, the content of the Variable is updated. The scale
 * of the slider is also updated such that the value of the Variable is in the
 * middle of the slider range. When the Variable is changed from another source,
 * the scale of the slider is updated with the new contents of the Variable.
 * 
 * @author Curran Kelleher
 * 
 */
public class VariableBoundIntegerSlider extends VariableBoundDecimalSlider{
	private static final long serialVersionUID = -759288567035135828L;

	/**
	 * Construct a VariableBoundIntegerSlider which is bound to the specified Variable.
	 * 
	 * @param variable
	 *            the variable to edit
	 */
	public VariableBoundIntegerSlider(Variable variable) {
		super(variable);

		maxValueForZeroValue = 10;
		minValueForZeroValue = 0;
		reusableValue = new IntegerValue(0);
	}

	/**
	 * 
	 * @return the current value (which the Variable will be set to) derived
	 *         from the slider position.
	 */
	protected double getCurrentSliderValue() {
		return Math.round(minValue + ((double) getValue() / SLIDERSCALE)
				* (maxValue - minValue));
	}
}
