package expressionConsole;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

/**
 * The GUI of the expression console, unifying the model
 * (ExpressionConsoleModel), the view (ExpressionConsoleView), and controller
 * (ExpressionConsoleController) into a command-line-like user interface.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleGUI extends JFrame {
	private static final long serialVersionUID = -5227441236832102386L;

	/**
	 * Constructs an ExpressionConsoleGUI. Use setVisible(true) to show it.
	 */
	public ExpressionConsoleGUI() {this(new Dimension(500,500));}
	
	/**
	 * Upon construction, the bounds are set such that the frame will have the
	 * specified Dimension and will be centered on the screen.
	 * 
	 * @param frameSize
	 *            the size of the frame
	 */
	public ExpressionConsoleGUI(Dimension frameSize) {

		// set up the model, controller, and view
		ExpressionConsoleModel model = ExpressionConsoleModel.getInstance();
		ExpressionConsoleController controller = new ExpressionConsoleController();
		ExpressionConsoleView view = new ExpressionConsoleView();
		model.addObserver(view);

		// SET UP THE GUI
		// this panel will contain a split pane, which will contain the entire
		// GUI.
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				controller, view);
		splitPane.setDividerSize(1);
		splitPane.setEnabled(false);
		content.add(splitPane);
		// put the content panel into the frame.
		getContentPane().add(content);

		// set the bounds to center the frame with the given dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(new Rectangle(new Point(screenSize.width / 2
				- frameSize.width / 2, screenSize.height / 2 - frameSize.height
				/ 2), frameSize));

		// set the frame to dispose on close
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// set the title
		setTitle("Expression Evaluator");
	}
}
