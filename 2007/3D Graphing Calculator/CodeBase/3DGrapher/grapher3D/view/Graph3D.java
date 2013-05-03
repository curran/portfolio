package grapher3D.view;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;

import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;
import primitives3D.LineSegment3D;
import primitives3D.Object3D;
import primitives3D.Polygon3D;
import primitives3D.SolidRod3D;
import primitives3D.Vector3D;
import valueTypes.ColorValue;
import valueTypes.DecimalValue;
import valueTypes.ErrorValue;
import variables.Variable;
import colorMap.ColorMap;

public class Graph3D {
	/**
	 * The lines which connect the points in the U direction.
	 */
	LineSegment3D[][] linesU;

	/**
	 * The lines which connect the points in the V direction.
	 */
	LineSegment3D[][] linesV;

	/**
	 * The 3D polygons which represent the surface.
	 */
	Polygon3D[][] polygons;

	/**
	 * The colors of each part of the surface.
	 */
	ColorValue[][] colors;

	/**
	 * The two dimensional array of 3D points ([u][v]) which will be connected
	 * together to form the surface.
	 */
	Vector3D[][] points = new Vector3D[0][0];

	/**
	 * The indexed values of U from 0 to 1.
	 */
	double[] valuesU = new double[0];

	/**
	 * The indexed values of V from 0 to 1.
	 */
	double[] valuesV;

	/**
	 * The number of 3D points representing the surface in the U direction.
	 */
	protected int graphResolutionU = 30;

	/**
	 * The number of 3D points representing the surface in the V direction.
	 */
	protected int graphResolutionV = 30;

	/**
	 * The ColorValue which holds the default color of the surface.
	 */
	protected Color defaultColor = Color.green;

	/**
	 * The function evaluation tree which will be graphed. This function should
	 * assign x,y,and z based on u and v varying from 0 to 1.
	 * 
	 */
	ExpressionNode function = (new RecursiveDescentParser())
			.parse("executeFunction({x=u*20-10;y=v*20-10;z=sin(x*y/10+t)})");

	/**
	 * The u variable
	 */
	Variable uVar = Variable.getVariable("u");

	/**
	 * The v variable
	 */
	Variable vVar = Variable.getVariable("v");

	/**
	 * The x variable
	 */
	Variable xVar = Variable.getVariable("x");

	/**
	 * The y variable
	 */
	Variable yVar = Variable.getVariable("y");

	/**
	 * The v variable
	 */
	Variable zVar = Variable.getVariable("z");

	/**
	 * The color variable
	 */
	Variable colorVar = Variable.getVariable("color");

	/**
	 * The color map used when the color variable is assigned values
	 */
	ColorMap colorMap = ColorMap.generateDefaultColorMap();

	/**
	 * The red variable
	 */
	Variable redVar = Variable.getVariable("red");

	/**
	 * The green variable
	 */
	Variable greenVar = Variable.getVariable("green");

	/**
	 * The blue variable
	 */
	Variable blueVar = Variable.getVariable("blue");

	/**
	 * The value which is used to set the value of the u variable.
	 */
	DecimalValue reusableUValue = new DecimalValue(0);

	/**
	 * The value which is used to set the value of the v variable.
	 */
	DecimalValue reusableVValue = new DecimalValue(0);

	/**
	 * The value which is used to set the value of the color variable.
	 */
	DecimalValue reusableColorValue = new DecimalValue(-1);

	/**
	 * When true, the surface is represented using lines, when false, the
	 * surface is represented as solid 3D polygons.
	 */
	protected boolean wireframe = false;

	/**
	 * Sets up the grid with the current values of graphResolutionU and
	 * graphResolutionV.
	 */
	protected void recreateGrid() {
		synchronized (points) {
			synchronized (valuesU) {
				// initialize the points and colors
				colors = new ColorValue[graphResolutionU + 1][graphResolutionV + 1];
				points = new Vector3D[graphResolutionU + 1][graphResolutionV + 1];
				// initialize and add the lines
				linesU = new SolidRod3D[graphResolutionU][graphResolutionV + 1];
				linesV = new SolidRod3D[graphResolutionU + 1][graphResolutionV];
				// initialize and add the polygons
				polygons = new Polygon3D[graphResolutionU + 1][graphResolutionV + 1];

				for (int u = 0; u < graphResolutionU + 1; u++)
					for (int v = 0; v < graphResolutionV + 1; v++) {
						points[u][v] = new Vector3D(0, 0, 0);
						colors[u][v] = new ColorValue(defaultColor);
					}

				for (int u = 0; u < graphResolutionU + 1; u++)
					for (int v = 0; v < graphResolutionV + 1; v++) {
						// the lines
						if (u < graphResolutionU) {
							linesU[u][v] = new SolidRod3D(points[u][v],
									points[u + 1][v], 0.04, Color.BLUE);
						}
						if (v < graphResolutionV) {
							linesV[u][v] = new SolidRod3D(points[u][v],
									points[u][v + 1], 0.04, Color.BLUE);
						}

						// the polygons
						if (u < graphResolutionU && v < graphResolutionV) {
							Vector3D[] polygonPoints = { points[u][v],
									points[u + 1][v], points[u + 1][v + 1],
									points[u][v + 1] };
							polygons[u][v] = new Polygon3D(polygonPoints,
									Color.green);
						}
					}

				// initialize the values
				valuesU = new double[graphResolutionU + 1];
				for (int i = 0; i < graphResolutionU + 1; i++)
					valuesU[i] = (double) i / graphResolutionU;
				valuesV = new double[graphResolutionV + 1];
				for (int i = 0; i < graphResolutionV + 1; i++)
					valuesV[i] = (double) i / graphResolutionV;
			}
		}
	}

	public Collection<Object3D> get3DObjects() {
		Collection<Object3D> objects = new LinkedList<Object3D>();
		for (int u = 0; u < graphResolutionU + 1; u++)
			for (int v = 0; v < graphResolutionV + 1; v++) {
				// the lines
				if (wireframe) {
					if (u < graphResolutionU)
						objects.add(linesU[u][v]);
					if (v < graphResolutionV)
						objects.add(linesV[u][v]);
				} else
				// the polygons
				if (u < graphResolutionU && v < graphResolutionV)
					objects.add(polygons[u][v]);
			}
		return objects;
	}

	/**
	 * Calculates and sets the values of the points based on the current
	 * function evaluation tree.
	 * 
	 */
	public void calculateGrid() {
		// set colors to -1 to indicate to use the default color
		reusableColorValue.value = Double.MIN_VALUE;
		colorVar.set(reusableColorValue);
		redVar.set(reusableColorValue);
		greenVar.set(reusableColorValue);
		blueVar.set(reusableColorValue);
		// temporary variables for color, red, green, and blue
		double colorVarValue = 0, r = 0, g = 0, b = 0;

		synchronized (points) {
			synchronized (valuesU) {
				synchronized (valuesU) {
					for (int u = 0; u < valuesU.length; u++)
						for (int v = 0; v < valuesV.length; v++) {
							reusableUValue.value = valuesU[u];
							reusableVValue.value = valuesV[v];

							uVar.set(reusableUValue);
							vVar.set(reusableVValue);

							function.evaluate();
							points[u][v].x = DecimalValue
									.extractDoubleValue(xVar.evaluate());
							points[u][v].y = DecimalValue
									.extractDoubleValue(yVar.evaluate());
							points[u][v].z = DecimalValue
									.extractDoubleValue(zVar.evaluate());

							colorVarValue = DecimalValue
									.extractDoubleValue(colorVar.evaluate());
							r = DecimalValue.extractDoubleValue(redVar
									.evaluate());
							g = DecimalValue.extractDoubleValue(greenVar
									.evaluate());
							b = DecimalValue.extractDoubleValue(blueVar
									.evaluate());

							if (colorVarValue != Double.MIN_VALUE)
								colors[u][v].value = colorMap
										.getColorAtValue(colorVarValue);
							else if (r != Double.MIN_VALUE
									|| g != Double.MIN_VALUE
									|| b != Double.MIN_VALUE)
								colors[u][v].value = new Color(
										(float) (r < 0 ? 0 : r > 1 ? 1 : r),
										(float) (g < 0 ? 0 : g > 1 ? 1 : g),
										(float) (b < 0 ? 0 : b > 1 ? 1 : b));
							else
								colors[u][v].value = defaultColor;

							if (wireframe) {
								if (u < valuesU.length - 1)
									linesU[u][v].color = colors[u][v].value;
								if (v < valuesV.length - 1)
									linesV[u][v].color = colors[u][v].value;
							} else {
								if (u < valuesU.length - 1
										&& v < valuesV.length - 1)
									polygons[u][v].color = colors[u][v].value;
							}
						}
				}
			}
		}
	}

	/**
	 * Checks the specified function for errors.
	 * 
	 * @param function
	 *            the function to test
	 * @return null if no errors, or the error message string if there is an
	 *         error.
	 */
	public String checkForErrorsInFunction(ExpressionNode function) {
		// check for errors
		uVar.set(reusableUValue);
		vVar.set(reusableVValue);

		Value error = null;

		Value result = function.evaluate();
		if (result instanceof ErrorValue)
			error = result;
		if (xVar.evaluate() instanceof ErrorValue)
			error = xVar.evaluate();
		else if (yVar.evaluate() instanceof ErrorValue)
			error = yVar.evaluate();
		else if (zVar.evaluate() instanceof ErrorValue)
			error = zVar.evaluate();
		return error == null ? null : error.toString();
	}

	/**
	 * 
	 * @return resolution of the graph in the U direction. That is, the number
	 * of "rectangles" along the U direction used to represent the surface.
	 */
	public int getUResolution() {
		return graphResolutionU;
	}

	/**
	 * 
	 * @return resolution of the graph in the V direction. That is, the number
	 * of "rectangles" along the V direction used to represent the surface.
	 */
	public int getVResolution() {
		return graphResolutionV;
	}
	
	/**
	 * 
	 * Sets the resolution of the graph in the U direction. That is, the number
	 * of "rectangles" along the U direction used to represent the surface.
	 * 
	 * @param i
	 *            the new resolution.
	 */
	public void setUResolution(int i) {
		graphResolutionU = i;
	}

	/**
	 * 
	 * Sets the resolution of the graph in the V direction. That is, the number
	 * of "rectangles" along the V direction used to represent the surface.
	 * 
	 * @param i
	 *            the new resolution.
	 */
	public void setVResolution(int i) {
		graphResolutionV = i;
	}
}
