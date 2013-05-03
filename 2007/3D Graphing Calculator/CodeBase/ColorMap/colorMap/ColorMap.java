package colorMap;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class which facilitates mapping values to colors. This mapping includes
 * gradients between an arbitrary number of colors.
 * 
 * @author Curran Kelleher
 * 
 */
public class ColorMap {

	List<ColorNode> colorNodes;

	float[] rgba_current = new float[4];

	float[] rgba_next = new float[4];

	float[] rgba_result = new float[4];

	Color[] colors = new Color[512];

	/**
	 * Construct a ColorMap with the specified ColorNodes
	 * 
	 * @param colorNodes
	 *            the color nodes which will comprise this color map initially
	 */
	public ColorMap(ColorNode[] colorNodes) {
		this.colorNodes = new ArrayList<ColorNode>(colorNodes.length);
		for (int i = 0; i < colorNodes.length; i++)
			this.colorNodes.add(colorNodes[i]);
		calculateColors();
	}

	/**
	 * Construct a ColorMap with the specified ColorNodes
	 * 
	 * @param colorNodes
	 *            the color nodes which will comprise this color map initially
	 */
	public ColorMap(List<ColorNode> colorNodes) {
		this.colorNodes = new ArrayList<ColorNode>(colorNodes.size());
		this.colorNodes.addAll(colorNodes);
		calculateColors();
	}

	/**
	 * Populates the color array based on the current color nodes
	 * 
	 */
	public void calculateColors() {
		Collections.sort(this.colorNodes);
		for (int i = 0; i < colors.length; i++)
			colors[i] = createColorForValue((double) i / colors.length);
	}

	/**
	 * Creates a new Color object based on the current color nodes
	 * 
	 * @param value
	 *            the value between 0 and 1 for which to generate the
	 *            corresponding Color.
	 * @return a new Color object corresponding to the specified value on this
	 *         color map.
	 */
	private Color createColorForValue(double value) {
		if (colorNodes.size() == 1)
			return colorNodes.get(0).color;

		for (int i = 0; i < colorNodes.size() - 1; i++) {
			ColorNode current = colorNodes.get(i);
			double min = current.value;
			rgba_current = current.color.getComponents(rgba_current);

			ColorNode next = colorNodes.get(i + 1);
			double max = next.value;
			rgba_next = next.color.getComponents(rgba_next);

			// if the value is in the current range between color node
			// values...
			if (value >= min && value <= max) {
				// assign the color properly
				for (int c = 0; c < rgba_current.length; c++) {
					double percentBtwMinAndMax = (value - min) / (max - min);
					rgba_result[c] = (float) ((1.0 - percentBtwMinAndMax)
							* rgba_current[c] + percentBtwMinAndMax
							* rgba_next[c]);
				}
			}// if it is lower than the lowest node value
			else if (i == 0 && value < min)
				return current.color;
			else
			// if it is greater than the greatest node
			if (i == colorNodes.size() - 2 && value > max)
				return next.color;

		}
		return new Color(rgba_result[0], rgba_result[1], rgba_result[2],
				rgba_result[3]);
	}

	/**
	 * Gets the Color corresponging to the specified value in this color map.
	 * 
	 * @param value
	 *            the value between 0 and 1 for which this method will return
	 *            the corresponding Color. If this value is less than 0 or
	 *            greater than 1, 0 or 1 (respectively) will be used instead.
	 * @return the Color corresponding to the specified value. (No new Color
	 *         object is allocated, a Color object from a pre-calculated array
	 *         is returned. see getColorArray())
	 */
	public Color getColorAtValue(double value) {
		return colors[(int) ((value<0?0:value>1?1:value) * (colors.length - 1))];
	}

	/**
	 * Returns the array of Color objects which represent this color map.
	 * 
	 * @return
	 */
	public Color[] getColorArray() {
		return colors;
	}

	/**
	 * Draws this color map horizontally (leftmost = 0, rightmost = 1) in a
	 * rectangle with the specified x, y, width, and height.
	 * 
	 * @param g
	 *            the Graphics on which to draw the color map rectangle
	 * @param x
	 *            the x pixel coordinate of the rectangle
	 * @param y
	 *            the y pixel coordinate of the rectangle
	 * @param width
	 *            the width of the rectangle
	 * @param height
	 *            the height of the rectangle
	 */
	public void paintOnThis(Graphics g, int x, int y, int width, int height) {
		for (int col = 0; col < width; col++) {
			g.setColor(getColorAtValue((double) col / (width - 1)));
			g.drawLine(col, y, col, y + height);
		}
	}

	/**
	 * Gets the color nodes that comprise this color map.
	 * 
	 * @return
	 */
	public List<ColorNode> getColorNodes() {
		return colorNodes;
	}

	/**
	 * Generates the default color map.
	 * 
	 * @return a newly allocated ColorMap object with default settings
	 */
	public static ColorMap generateDefaultColorMap() {

		ColorNode[] colorNodes = { new ColorNode(Color.blue, 0),
				new ColorNode(Color.green, 0.5), new ColorNode(Color.red, 1) };
		return new ColorMap(colorNodes);

	}
}
