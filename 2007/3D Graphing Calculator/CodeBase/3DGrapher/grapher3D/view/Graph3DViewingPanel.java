package grapher3D.view;

import java.awt.Color;

import primitives3D.Vector3D;
import valueTypes.ColorValue;
import drawing3D.RotatableObject3DViewingPanel;

/**
 * A panel which draws explicit 3D graphs and allows the user to rotate the view
 * of them with the mouse.
 * 
 * @author Curran Kelleher
 * 
 */
public class Graph3DViewingPanel extends RotatableObject3DViewingPanel {
	private static final long serialVersionUID = 3219153708090474921L;

	/**
	 * The number of 3D points representing each axis.
	 */
	protected int axesResolution = 30;

	/**
	 * The object containing the surface.
	 */
	Graph3D graph = new Graph3D();

	/**
	 * The color of the axes.
	 */
	ColorValue axesColor = new ColorValue(Color.WHITE);

	/**
	 * The X axis.
	 */
	Axis3D xAxis = new Axis3D(new Vector3D(-10, 0, 0), new Vector3D(10, 0, 0),
			"x", axesColor);

	/**
	 * The Y axis.
	 */
	Axis3D yAxis = new Axis3D(new Vector3D(0, -10, 0), new Vector3D(0, 10, 0),
			"y", axesColor);

	/**
	 * The Z axis.
	 */
	Axis3D zAxis = new Axis3D(new Vector3D(0, 0, -10), new Vector3D(0, 0, 10),
			"z", axesColor);

	/**
	 * When true, the X axis of the graph is drawn.
	 */
	protected boolean drawXAxis = true;
	
	/**
	 * When true, the Y axis of the graph is drawn.
	 */
	protected boolean drawYAxis = true;
	
	/**
	 * When true, the Z axis of the graph is drawn.
	 */
	protected boolean drawZAxis = true;

	/**
	 * Construct a Graph3DViewingPanel
	 * 
	 */
	public Graph3DViewingPanel() {
		recreateGrid();
		graph.calculateGrid();
	}

	/**
	 * Sets up the grid with the current values of graphResolutionU and
	 * graphResolutionV.
	 */
	protected void recreateGrid() {
		graph.recreateGrid();

		xAxis.axisResolution = axesResolution;
		yAxis.axisResolution = axesResolution;
		zAxis.axisResolution = axesResolution;

		resetObjectsInViewer();
	}

	/**
	 * Clears the 3D objects currently in the viewer, then adds the appropriate
	 * objects
	 * 
	 */
	protected void resetObjectsInViewer() {
		// clear the objects in the viewer
		viewer.clear3DObjects();
		viewer.add3DObjects(graph.get3DObjects());

		if (drawXAxis)
			viewer.add3DObjects(xAxis.generateAxisObjects());
		if (drawYAxis)
			viewer.add3DObjects(yAxis.generateAxisObjects());
		if (drawZAxis && viewer.window.drawFor3D)
			viewer.add3DObjects(zAxis.generateAxisObjects());

	}

}
