package grapher3D.view;

import drawing3D.Object3DViewer;
import expressionConsole.ExpressionConsoleModel;
import grapher3D.Grapher3DConstants;

import java.awt.Color;

import parser.Value;
import variableBridge.IndividualBooleanVariableUpdater;
import variableBridge.IndividualColorVariableUpdater;
import variableBridge.IndividualDecimalVariableUpdater;
import variableBridge.IndividualIntegerVariableUpdater;
import variableBridge.IndividualVariableBridge;
import variableBridge.VariableBridge;
import variableBridge.VariableBridgeImplementation;
import variables.Variable;
import colorMapValue.ColorMapValue;

/**
 * A panel which draws explicit 3D graphs and allows the user to rotate the view
 * of them with the mouse. The t Variable is incremented by tIncrement every
 * frame. It's parameters are controlled by Variables.
 * 
 * @author Curran Kelleher
 * 
 */
public class VariableBridgedGraph3DViewingPanel extends
		AnimatedGraph3DViewingPanel {
	private static final long serialVersionUID = 4211575723928910614L;

	/**
	 * The VariableBridge which bridges this class's variables to external
	 * Variable objects.
	 */
	VariableBridge variableBridge;

	public VariableBridgedGraph3DViewingPanel() {
		IndividualVariableBridge[] bridges = {
				new IndividualVariableBridge(
						Grapher3DConstants.Grapher3DAnimateFlag,
						"Controls whether or not the view of the 3D Grapher is animated.",
						new IndividualBooleanVariableUpdater() {
							public void updateInternalVariable(boolean newValue) {
								animate = newValue;
							}

							public boolean getInternalVariableValue() {
								return animate;
							}
						}),

				new IndividualVariableBridge(
						Grapher3DConstants.rotationStateX,
						"Controls the angle of rotation in the X direction on the screen.",
						new IndividualDecimalVariableUpdater() {
							public void updateInternalVariable(double newValue) {
								rotationStateX = newValue;
							}

							public double getInternalVariableValue() {
								return rotationStateX;
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.rotationStateY,
						"Controls the angle of rotation in the Y direction on the screen.",
						new IndividualDecimalVariableUpdater() {
							public void updateInternalVariable(double newValue) {
								rotationStateY = newValue;
							}

							public double getInternalVariableValue() {
								return rotationStateY;
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.rotationIncrementX,
						"Controls the per-frame increment of rotation in the X direction on the screen.",
						new IndividualDecimalVariableUpdater() {
							public void updateInternalVariable(double newValue) {
								rotationIncrementX = newValue;

							}

							public double getInternalVariableValue() {
								return rotationIncrementX;
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.rotationIncrementY,
						"Controls the per-frame increment of rotation in the Y direction on the screen.",
						new IndividualDecimalVariableUpdater() {
							public void updateInternalVariable(double newValue) {
								rotationIncrementY = newValue;
							}

							public double getInternalVariableValue() {
								return rotationIncrementY;
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.GraphResolution,
						"Controls the resolution of the 3D graph; the number of rigid segments per side that the surface is divided into.",
						new IndividualIntegerVariableUpdater() {
							public void updateInternalVariable(int newValue) {

								newValue = (newValue < 0 ? -newValue : newValue);
								if (graph.getUResolution() != newValue
										|| graph.getVResolution() != newValue) {
									graph.setUResolution(newValue);
									graph.setVResolution(newValue);

									axesResolution = newValue;
									recreateGrid();

									if (!animate) {
										graph.calculateGrid();

										// draw the objects
										drawObjectsOnBufferImage();

										// draw the buffered image to the screen
										repaint();
									}
								}
							}

							public int getInternalVariableValue() {
								return graph.getUResolution();
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.GraphResolution_U,
						"Controls the resolution of the 3D graph in the u direction.",
						new IndividualIntegerVariableUpdater() {
							public void updateInternalVariable(int newValue) {
								newValue = (newValue < 0 ? -newValue : newValue);
								if (graph.getUResolution() != newValue) {
									graph.setUResolution(newValue);

									axesResolution = newValue;
									recreateGrid();

									if (!animate) {
										graph.calculateGrid();

										// draw the objects
										drawObjectsOnBufferImage();

										// draw the buffered image to the screen
										repaint();
									}
								}
							}

							public int getInternalVariableValue() {
								return graph.getUResolution();
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.GraphResolution_V,
						"Controls the resolution of the 3D graph in the v direction.",
						new IndividualIntegerVariableUpdater() {
							public void updateInternalVariable(int newValue) {
								newValue = (newValue < 0 ? -newValue : newValue);
								if (graph.getVResolution() != newValue) {
									graph.setVResolution(newValue);

									axesResolution = newValue;
									recreateGrid();

									if (!animate) {
										graph.calculateGrid();

										// draw the objects
										drawObjectsOnBufferImage();

										// draw the buffered image to the screen
										repaint();
									}
								}
							}

							public int getInternalVariableValue() {
								return graph.getVResolution();
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.Grapher3DWireframeFlag,
						"When true, the surface is represented using lines, when false, the surface is represented as solid 3D polygons.",
						new IndividualBooleanVariableUpdater() {
							public void updateInternalVariable(boolean newValue) {
								if (graph.wireframe != newValue) {
									graph.wireframe = newValue;
									resetObjectsInViewer();
									if (!animate) {
										graph.calculateGrid();

										// draw the objects
										drawObjectsOnBufferImage();

										// draw the buffered image to
										// the screen
										repaint();
									}
								}
							}

							public boolean getInternalVariableValue() {
								return graph.wireframe;
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.Grapher3DShowAxesFlag,
						"When true, the axes of the graph are shown.",
						new IndividualBooleanVariableUpdater() {
							public void updateInternalVariable(boolean newValue) {
								drawXAxis = drawYAxis = drawZAxis = newValue;
								resetObjectsInViewer();
								if (!animate) {
									graph.calculateGrid();

									// draw the objects
									drawObjectsOnBufferImage();

									// draw the buffered
									// image to the screen
									repaint();
								}

							}

							public boolean getInternalVariableValue() {
								return drawXAxis;
							}
						}),
				new IndividualVariableBridge(
						Grapher3DConstants.DefaultGraphColor,
						"The default color of all graphs.",
						new IndividualColorVariableUpdater() {
							public void updateInternalVariable(Color newValue) {
								graph.defaultColor = newValue;
							}

							public Color getInternalVariableValue() {
								return graph.defaultColor;
							}
						}),
				new IndividualVariableBridge(Grapher3DConstants.AxesColor,
						"The default color of the axes.",
						new IndividualColorVariableUpdater() {
							public void updateInternalVariable(Color newValue) {
								axesColor.value = newValue;
								resetObjectsInViewer();
							}

							public Color getInternalVariableValue() {
								return axesColor.value;
							}
						}),

				new IndividualVariableBridge(
						Grapher3DConstants.BackgroundColor,
						"The background color of graphs.",
						new IndividualColorVariableUpdater() {
							public void updateInternalVariable(Color newValue) {
								backgroundColor = newValue;

								Object3DViewer.backgroundColor = newValue;
							}

							public Color getInternalVariableValue() {
								return backgroundColor;
							}
						}),
				new IndividualVariableBridge(Grapher3DConstants.ColorMap,
						"The color map for graphs.",
						new VariableBridgeImplementation() {
							Value colorMapValue = new ColorMapValue(
									graph.colorMap);

							public void updateExternalVariable(
									Variable variableToUpdate) {
								if (variableToUpdate.evaluate() != colorMapValue)
									variableToUpdate.set(colorMapValue);
							}

							public void updateInternalVariable(
									Variable variableToRead) {
							}

						}),
				new IndividualVariableBridge(
						Grapher3DConstants.DrawGraphsIn3D,
						"When true, graphs are drawn in 3D, when false, they are drawn in 2D.",
						new IndividualBooleanVariableUpdater() {
							public void updateInternalVariable(boolean newValue) {
								viewer.window.drawFor3D = newValue;
								// reset the objects, so the z-axis will not be
								// drawn
								resetObjectsInViewer();
							}

							public boolean getInternalVariableValue() {
								return viewer.window.drawFor3D;
							}
						}),

		};
		// put the variable bridges into action
		variableBridge = new VariableBridge(bridges);
	}

	/**
	 * Override this method so changes in rotation are logged
	 */
	protected void setRotationIncrementValues(double rotationIncrementX,
			double rotationIncrementY) {
		ExpressionConsoleModel.getInstance().enterExpression(
				Grapher3DConstants.rotationIncrementX + " = "
						+ rotationIncrementX);
		ExpressionConsoleModel.getInstance().enterExpression(
				Grapher3DConstants.rotationIncrementY + " = "
						+ rotationIncrementY);
		ExpressionConsoleModel.getInstance().enterExpression(
				Grapher3DConstants.rotationStateX + " = " + rotationStateX);
		ExpressionConsoleModel.getInstance().enterExpression(
				Grapher3DConstants.rotationStateY + " = " + rotationStateY);
		super
				.setRotationIncrementValues(rotationIncrementX,
						rotationIncrementY);

	}
}
