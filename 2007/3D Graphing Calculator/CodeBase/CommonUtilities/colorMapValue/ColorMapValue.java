package colorMapValue;

import java.util.Iterator;

import parser.Value;
import valueTypes.ColorValue;
import colorMap.ColorMap;
import colorMap.ColorNode;

/**
 * The Value representing color maps.
 * 
 * @author Curran Kelleher
 */
public class ColorMapValue extends Value {
	/**
	 * The color map that this value contains.
	 */
	public ColorMap value;

	/**
	 * Constructs a ColorMapValue initialized with the specified color map.
	 * 
	 * @param value
	 *            the value this DecimalValue will have.
	 */
	public ColorMapValue(ColorMap value) {
		this.value = value;
	}

	/**
	 * Gets a human readable desctiption of this type.
	 * 
	 * @return a human readable desctiption of this type.
	 */
	public String getType() {
		return "color map";
	}

	/**
	 * @return a String representation of this object.
	 */
	public String toString() {
		return toParseableString();
	}

	/**
	 * @return a String which, when parsed by RecursiveDescentParser and
	 *         evaluated, generates this value.
	 */
	public String toParseableString() {
		StringBuffer command = new StringBuffer();
		command.append("createColorMap(");
		for(Iterator<ColorNode> it = value.getColorNodes().iterator();it.hasNext();) {
			ColorNode currentNode = it.next();
			command.append(ColorValue.generateParseableString(currentNode.color)+","+currentNode.value);
			command.append(it.hasNext()?",":"");
		}
		command.append(")");
		return command.toString();
	}
	
	/**
	 * Returns the color map which is in the specified value
	 * @return
	 */
	public static ColorMap extractColorMap(Value value)
	{
		if(value instanceof ColorMapValue)
			return((ColorMapValue)value).value;	
		else
			return ColorMap.generateDefaultColorMap();
		
	}
}
