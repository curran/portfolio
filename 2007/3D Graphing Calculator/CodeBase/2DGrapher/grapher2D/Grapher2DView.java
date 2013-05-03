package grapher2D;

import expressionConsole.ExpressionConsoleModel;
import generalMathClasses.CoordinateSpace2D;
import generalMathClasses.Window2D;
import graphicsUtilities.GeneralGraphicsUtilities;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import parser.ExpressionNode;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.BooleanValue;
import valueTypes.DecimalValue;
import variables.Variable;

/**
 * The view of the Model View Controller paradigm for the Grapher. In this
 * paradigm, the model stores information necessary to produce the view (in this
 * case this information includes the function string, coordinate space, and
 * some other things.) The controller is the UI which modifies the model (in
 * this case it is the function field). The view is the graphical representation
 * of the model. Typically there is some overlap between the roles of the view
 * and the model (such as zooming by drawing a rectangle on the view, or
 * displayed parameters in the controller which must be updated to reflect
 * changes in the model. The model, view, and controller communicate using the
 * Observer design pattern. The model is the Observable, and the controller and
 * view are Observers of the model. When the model changes, it sends a
 * notification event to all Observers, thus signaling them to update so that
 * they reflect the current state of the model.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher2DView extends JPanel implements Observer, Runnable,
		ComponentListener {
	private static final long serialVersionUID = 6185291782620102768L;

	/**
	 * The window that will translate between pixel space and coordinate space.
	 * It is constructed with a reference to the coordinate space stored in the
	 * model. It will always use this reference, so will automatically reflect
	 * any changes made to the coordinate space in the model.
	 */
	Window2D window = new Window2D(Grapher2DConstants.coordinateSpace);

	/**
	 * A reference to the parser. (so we don't need call
	 * ExpressionConsoleModel.getInstance().getParser() more than once)
	 */
	RecursiveDescentParser parser = ExpressionConsoleModel.getInstance()
			.getParser();

	/**
	 * A reference to the function string variable. (so we don't need call
	 * Variable.getVariable(Grapher2DConstants.Grapher2DFunctionString) more
	 * than once)
	 */
	ExpressionNode functionStringVar = Variable
			.getVariable(Grapher2DConstants.Grapher2DFunctionString);

	/**
	 * A reference to the model's evaluation tree of the function to graph.
	 */
	ExpressionNode functionEvaluationTree = null;

	/**
	 * A boolean flag signaling whether or not the view should be animating. It
	 * reflects the state of the Variable whose name is stored in
	 * Grapher2DConstants.Grapher2DAnimateFlag.
	 */
	boolean animate = true;

	/**
	 * When this is set to true, the animation thread is stopped permanently.
	 */
	public boolean stopThread = false;

	/**
	 * The evaluation tree to increment t.
	 */
	ExpressionNode tIncrementExpression = ExpressionConsoleModel.getInstance()
			.getParser().parse("t = t + tIncrement");

	/**
	 * Get references to the mapped x, y, and t variables used in the evaluation
	 * tree for use later
	 */
	Variable xVar = Variable.getVariable("x"),
			tVar = Variable.getVariable("t"), yVar = Variable.getVariable("y");

	/**
	 * A flag which keeps track of whether or not an error has occured.
	 */
	boolean error = false;

	/**
	 * A flag which is set to true when the points are initialized for the first
	 * time. It is used to prevent premature attempts to draw the points.
	 */
	boolean pointsHaveBeenInitialized = false;

	/**
	 * If error is true, the contents of this string will be displayed.
	 */
	String errorMessage = "";

	// temporary variables declared here for optimization of re-use
	CoordinateSpace2D coordinateSpace;

	double maxMinusMinX, xVal, yVal;

	DecimalValue xDecimalValue = new DecimalValue(0);

	int numPoints = 0, i;

	Point[] points = new Point[0];

	Value result;

	Dimension size = new Dimension();

	/**
	 * Upon construction, the animation thread is started.
	 * 
	 */
	public Grapher2DView() {

		// Initialize and set up the Observer pattern with the appropriate
		// Variables.
		initializeVariables();

		// initialize the size of the screen and resolution of the graph
		updateSizes();

		// start the animation thread
		(new Thread(this)).start();

		// set up the resize listener
		addComponentListener(this);
	}

	/**
	 * Initializes and sets up the Observer pattern with the appropriate
	 * Variables.
	 * 
	 */
	private void initializeVariables() {
		// Observe the function string for changes
		Variable
				.getVariable(Grapher2DConstants.Grapher2DFunctionString)
				.addObserver(this,
						"Used by the Grapher2D drawing space as the descriprion of the graph to draw.");

		// initialize the animation flag to true
		Variable animationFlagVar = Variable
				.getVariable(Grapher2DConstants.Grapher2DAnimateFlag);
		animationFlagVar.set(new BooleanValue(true));
		// Observe the animation flag for changes
		animationFlagVar
				.addObserver(
						this,
						"Used by the Grapher2D drawing space to signal when to turn animation on (value = true) and off (value = false)");

		Variable resolutionVar = Variable
				.getVariable(Grapher2DConstants.Grapher2DResolution);
		// initialize the resolution to 500
		resolutionVar.set(new DecimalValue(500));
		// Observe the resolution variable for changes
		resolutionVar
				.addObserver(
						this,
						"Used by the Grapher2D drawing space to define the number of points used to construct the graph.");

		// initialize tIncrement
		Variable.getVariable(Grapher2DConstants.TimeIncrement).set(
				new DecimalValue(0.1));

	}

	/**
	 * Called from Swing to repaint the panel.
	 */
	public void paint(Graphics g) {
		// clear away the old crap
		g.clearRect(0, 0, size.width, size.height);
		// if there was an error, show it on the screen
		if (error)
			// g.drawString(errorMessage, 20, 20);
			GeneralGraphicsUtilities.drawErrorMessage((Graphics2D) g, errorMessage,
					size.width);

		// otherwise connect the precomputed dots to draw the graph
		else if (pointsHaveBeenInitialized)
			for (int i = 0; i < numPoints - 1; i++)
				g.drawLine(points[i].x, points[i].y, points[i + 1].x,
						points[i + 1].y);
	}

	/**
	 * Responds to notifications from the function string Variable when it has
	 * changed.
	 */
	public void update(Observable o, Object arg) {
		if (arg instanceof Variable) {
			String variableName = ((Variable) arg).toString();

			// if the function string has changed
			if (variableName.equals(Grapher2DConstants.Grapher2DFunctionString)) {
				// parse the function into a usable evaluation tree
				functionEvaluationTree = parser.parse("executeFunction({"
						+ functionStringVar.evaluate().toString() + "})");
				
				//reset t to 0
				tVar.set(new DecimalValue(0));

				// the animation thread is constantly updating the graph and
				// recalculating, so we the change is updated for, needn't do
				// anything more here.
			}
			// if the animation flag has changed
			else if (variableName
					.equals(Grapher2DConstants.Grapher2DAnimateFlag))
				// update our internal representation of it, whose state will be
				// acted upon by the animation thread.
				animate = Variable.getVariable(
						Grapher2DConstants.Grapher2DAnimateFlag).evaluate()
						.toString().equals("true") ? true : false;
			// if the resolution has changed
			else if (variableName
					.equals(Grapher2DConstants.Grapher2DResolution))
				// update the size of the screen and resolution of the graph
				updateSizes();

		}
	}

	/**
	 * Updates the size of the screen and resolution of the graph
	 * 
	 */
	private void updateSizes() {
		// store the size of the window for use later
		size = getSize();

		// make sure the window has the correct panel (pixel space) dimensions.
		window.set(size);

		// get the graph drawing resolution from the model
		Value resolutionValue = Variable.getVariable(
				Grapher2DConstants.Grapher2DResolution).evaluate();
		int newNumPoints = 0;
		if (resolutionValue instanceof DecimalValue)
			newNumPoints = (int) ((DecimalValue) resolutionValue).value;
		else {
			ExpressionConsoleModel
					.getInstance()
					.enterErrorMessage(
							"The variable "
									+ Grapher2DConstants.Grapher2DResolution
									+ ", used by the grapher to specify the resolution of the graph, has an invalid value, "
									+ resolutionValue
									+ ", so it will be reset to 500.");
			ExpressionConsoleModel.getInstance().enterExpression(
					Grapher2DConstants.Grapher2DResolution + " = 500");
		}
		// if the resolution has changed, re-allocate the points array
		if (newNumPoints != numPoints)
			points = new Point[numPoints = newNumPoints];
	}

	/**
	 * Calculates the points used to draw the graph.
	 * 
	 */

	private void calculatePoints() {

		// if the function is still not there,
		if (functionEvaluationTree == null) {
			// report the error that there is no function.
			error = true;
			errorMessage = "There is no function to display";
			return;
		} else
			// otherwise there is no error
			error = false;

		// extract the coordinate space from the window so we can work with it
		coordinateSpace = window.getCoordinateSpace();

		// compute xMax - xMin, because we will use it many times
		maxMinusMinX = coordinateSpace.xMax - coordinateSpace.xMin;

		// calculate through the points
		for (i = 0; i < numPoints; i++) {
			// calculate the x value based on i
			xVal = (double) i / numPoints * maxMinusMinX + coordinateSpace.xMin;

			// set the x variable used in the evaluation tree
			xDecimalValue.value = xVal;
			xVar.set(xDecimalValue);

			// evaluate the function
			functionEvaluationTree.evaluate();

			// get the value out of the y variable
			result = yVar.evaluate();

			// create a point at this index
			points[i] = new Point();

			// if there were no parsing or evaluation problems
			if (result instanceof DecimalValue) {
				error = false;
				yVal = ((DecimalValue) result).value;
				points[i].x = window.getXpixel(xVal);
				points[i].y = window.getYpixel(yVal);
			} else {
				// if there was an error, report it.
				error = true;
				errorMessage = result.toString();
				return;
			}
		}

		if (!pointsHaveBeenInitialized)
			// if we have gotten here, then the points have been properly
			// initialized for the first time
			pointsHaveBeenInitialized = true;
	}

	/**
	 * Called to start the animation Thread.
	 */
	public void run() {

		// constantly update t and recalculate/redraw the graph
		while (!stopThread) {
			// only update the graph if the animate flag in the model is true
			if (animate) {
				// calculate the points used to draw the graph
				calculatePoints();

				// draw the graph
				repaint();

				// increment t
				tIncrementExpression.evaluate();
			}

			// sleep for 20 milliseconds if the graph is good
			// sleep for 200 milliseconds if there is an error or animation is
			// turned off

			try {
				Thread.sleep(error || !animate ? 200 : 20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//for testing
			//System.out.print(".");
		}
	}

	/**
	 * Called when this panel is shown.
	 */
	public void componentShown(ComponentEvent e) {
	}

	/**
	 * Called when this panel is moved.
	 */
	public void componentHidden(ComponentEvent e) {
	}

	/**
	 * Called when this panel is moved.
	 */
	public void componentMoved(ComponentEvent e) {
	}

	/**
	 * Called when this panel is resized.
	 */
	public void componentResized(ComponentEvent e) {
		updateSizes();
	}

}
