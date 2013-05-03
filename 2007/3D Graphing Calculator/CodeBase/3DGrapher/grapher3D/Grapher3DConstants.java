package grapher3D;

/**
 * A class of constants which define the names to be used for
 * programmatically-accessed Variables.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher3DConstants {
	/**
	 * The name of the variable which stores the string of the user-definable
	 * function used by the 3D Grapher. This variable should contain a string
	 * which defines the function to graph, given the selected coordinate space.
	 * This string is what the user types and sees in the function text field.
	 */
	public static final String Grapher3DFunctionString_external = "grapher3DFunctionString";

	/**
	 * The name of the variable which stores the string of the user-definable
	 * function used by the 3D Grapher. This variable should contain a string
	 * which fully defines the function to graph, that is, the conversion of u
	 * and v ranging from 0 to 1 to x, y, and z. This string is not what the
	 * user types and sees, but rather the function string used internally.
	 */
	public static final String Grapher3DFunctionString_internal = "grapher3DFunctionString_Internal";

	/**
	 * The name of the variable which stores a boolean flag signaling whether or
	 * not the view should be animating.
	 */
	public static final String Grapher3DAnimateFlag = "grapher_Animation";

	/**
	 * The name of the variable which stores the amount by which the t Variable
	 * will be incremented by each frame. So every frame, t = t + [the variable
	 * whose name is this String]
	 */
	public static final String TimeIncrement = "tIncrement";

	/**
	 * The angle of rotation in the "X" direction on the screen.
	 */
	public static final String rotationStateX = "X_rotation";

	/**
	 * The angle of rotation in the "Y" direction on the screen.
	 */
	public static final String rotationStateY = "Y_rotation";

	/**
	 * Controls the per-frame increment of rotation in the X direction on the
	 * screen.
	 */
	public static final String rotationIncrementX = "X_rotation_Increment";

	/**
	 * Controls the per-frame increment of rotation in the Y direction on the
	 * screen.
	 */
	public static final String rotationIncrementY = "Y_rotation_Increment";

	/**
	 * When true, the surface is represented using lines, when false, the
	 * surface is represented as solid 3D polygons.
	 */
	public static final String Grapher3DWireframeFlag = "Wireframe";

	/**
	 * The default color of all graphs.
	 */
	public static final String DefaultGraphColor = "Graph_Color";

	/**
	 * The background color of graphs.
	 */
	public static final String BackgroundColor = "Background_Color";

	/**
	 * When true, the axes of the graph are shown.
	 */
	public static final String Grapher3DShowAxesFlag = "Show_Axes";

	/**
	 * The color of the axes.
	 */
	public static final String AxesColor = "Axes_Color";

	/**
	 * The color map for graphs.
	 */
	public static final String ColorMap = "Color_Map";

	/**
	 * The variable which keeps track of the state of the main GUI Frame
	 */
	public static final String GUIFrameVariable = "grapher3DGUIFrame";

	/**
	 * A boolean variable which flags whether to draw the graphs in 3D (true) or
	 * 2D (false)
	 */
	public static final String DrawGraphsIn3D = "drawGraphsIn3D";

	/**
	 * Controls the resolution of the 3D graph; the number of rigid segments per
	 * side that the surface is divided into.
	 */
	public static final String GraphResolution = "Graph_Resolution";

	/**
	 * "Controls the resolution of the 3D graph in the u direction."
	 */
	public static final String GraphResolution_U = "Graph_Resolution_U";

	/**
	 * "Controls the resolution of the 3D graph in the v direction."
	 */
	public static final String GraphResolution_V = "Graph_Resolution_V";
}
