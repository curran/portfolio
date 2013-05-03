package colorMap;

import java.awt.Color;

/**
 * A node defining an endpoint of a color gradient in the class ColorMap.
 * @author Curran Kelleher
 * @see ColorMap
 *
 */
public class ColorNode implements Comparable<ColorNode>{
	/**
	 * The color of this node.
	 */
	public Color color;
	
	/**
	 * The numerical value which this node corresponds to.
	 */
	public double value;

	/**
	 * Construct a color node with the specified color and value
	 * @param color The color of this node
	 * @param value The numerical value between 0 and 1 which this node corresponds to
	 */
	public ColorNode(Color color, double value) {
		if (value < 0 || value > 1)
			(new Exception("value must be between 0 and 1!, it is " + value))
					.printStackTrace();
		this.color = color;
		this.value = value;
	}

	/**
	 * Compares ColorNodes by their value field.
	 */
	public int compareTo(ColorNode o) {
		return value<o.value?-1:value>o.value?1:0;
	}
}