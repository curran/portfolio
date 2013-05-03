package grapher2D;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import logEnabledComponents.LogEnabledJFrame;
import parser.RecursiveDescentParser;
import parser.Value;
import valueTypes.BooleanValue;
import variableEditorUI.EditVariablesMenuPanel;
import variables.Variable;
import actionScript.ActionScriptLoadUtilities;
import actionScript.ActionScriptSaveUtilities;
import expressionConsole.ExpressionConsoleHistory;
import expressionConsole.ExpressionConsoleHistoryEntry;
import expressionConsole.ExpressionConsoleModel;

/**
 * The GUI for the 2D Grapher.
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher2DGUI extends LogEnabledJFrame implements ActionListener {
	private static final long serialVersionUID = 7205295946430619788L;

	/**
	 * A reference to the singleton console. (so we don't need call
	 * ExpressionConsoleModel.getInstance() more than once)
	 */
	ExpressionConsoleModel console = ExpressionConsoleModel.getInstance();

	/**
	 * The panel which is displayed in the menu for editing veriables
	 */
	private EditVariablesMenuPanel editVariablesMenuPanel = new EditVariablesMenuPanel();

	/**
	 * The controller part of the GUI.
	 */
	Grapher2DController controller;

	/**
	 * The view part of the GUI.
	 */
	Grapher2DView view;

	/**
	 * Upon construction, the bounds are set such that the frame will have the
	 * specified Dimension and will be centered on the screen.
	 * 
	 * @param frameSize
	 *            the size of the frame
	 */
	public Grapher2DGUI(Dimension frameSize) {
		// initialize the GUI frame as a LogEnabledJFrame whose parameters will
		// be kept in the Variable named grapher2DGUIFrame
		super("grapher2DGUIFrame");

		// initialize the animation variable to be true
		Variable.getVariable(Grapher2DConstants.Grapher2DAnimateFlag).set(
				new BooleanValue(true));

		// set up the controller and view
		// the Model part of the MVC paradigm exists as a collection of
		// variables within the parser inside of the singleton
		// ExpressionConsoleModel
		controller = new Grapher2DController();
		view = new Grapher2DView();

		// set the initial function string
		// the function string variable is being observed by the view and
		// controller, and as a result of this code execution will be
		// appropriately notified.
		ExpressionConsoleModel
				.getInstance()
				.getParser()
				.parse(
						Grapher2DConstants.Grapher2DFunctionString
								+ "= \"a = sin(t/5); y = x>0?(sin(x*10-t)+3*sin(x/4+t)+2*sin(x-t))*a:sin(x+t)*x*a\"")
				.evaluate();

		// SET UP THE GUI
		// this panel will contain a split pane, which will contain the entire
		// GUI.
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				controller, view);
		splitPane.setDividerSize(3);
		splitPane.setEnabled(false);
		content.add(splitPane);
		// put the content panel into the frame.
		getContentPane().add(content);

		// set up the menu bar
		setJMenuBar(createMenuBar());

		// Set the icon
		setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));

		// set the title
		setTitle("2D Grapher");

		// set the frame to dispose itself upon closing
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// set the bounds to center the frame with the given dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(new Rectangle(new Point(screenSize.width / 2
				- frameSize.width / 2, screenSize.height / 2 - frameSize.height
				/ 2), frameSize));

		// set up the listener so the animation pauses when the frame is
		// minimized
		addWindowListener(new WindowAdapter() {
			/**
			 * Called when the window is minimized
			 */
			public void windowIconified(WindowEvent e) {
				// pause the animation by setting the flag in the model.
				Variable.getVariable(Grapher2DConstants.Grapher2DAnimateFlag)
						.set(new BooleanValue(false));

			}

			/**
			 * Called when the window is restored
			 */
			public void windowDeiconified(WindowEvent e) {
				// start the animation again by setting the flag in the model.
				Variable.getVariable(Grapher2DConstants.Grapher2DAnimateFlag)
						.set(new BooleanValue(true));

			}
		});

	}

	/**
	 * Upon disposal, stop the view's Thread
	 */
	public void dispose() {
		if (view != null)
			view.stopThread = true;
		view = null;
		controller = null;
		super.dispose();
	}

	/**
	 * Constructs the menu bar.
	 * 
	 * @return the menu bar.
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createExamplesMenu());
		menuBar.add(createEditVariableMenu());
		menuBar.add(createAnimationMenu());
		return (menuBar);
	}

	/**
	 * Creates the File menu
	 * 
	 * @return
	 */
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		fileMenu.add(createSaveHistoryMenuItem());
		fileMenu.add(createLoadHistoryMenuItem());

		return fileMenu;
	}

	/**
	 * Creates the "Load History" menu item.
	 * 
	 * @return
	 */
	private JMenuItem createLoadHistoryMenuItem() {
		JMenuItem loadHistory = new JMenuItem("Load History");
		loadHistory.setMnemonic('l');
		loadHistory.setVisible(true);
		loadHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// stop animation temporarily if it was on
				Variable animateVar = Variable
						.getVariable(Grapher2DConstants.Grapher2DAnimateFlag);
				animateVar.set(new BooleanValue(false));

				// load the script
				ExpressionConsoleHistory history = ActionScriptLoadUtilities
						.promptUserToLoadScript();

				Map<String, String> initialSymbolTable = history
						.getInitialSymbolTableSnapshot();

				Map<String, Variable> currentSymbolTable = Variable
						.getSymbolTable();

				ExpressionConsoleModel console = ExpressionConsoleModel
						.getInstance();

				RecursiveDescentParser parser = console.getParser();

				// initialize the symbol table
				for (Iterator<Entry<String, Variable>> it = currentSymbolTable
						.entrySet().iterator(); it.hasNext();) {
					Entry<String, Variable> currentEntry = it.next();
					String newValue = initialSymbolTable.get(currentEntry
							.getKey());
					String expressionToEvaluate = currentEntry.getKey() + " = "
							+ (newValue == null ? "0" : newValue);
					parser.parse(expressionToEvaluate).evaluate();
				}

				// step through all events
				List<ExpressionConsoleHistoryEntry> historyList = history
						.getExpressionList();

				for (Iterator<ExpressionConsoleHistoryEntry> it = historyList
						.iterator(); it.hasNext();) {
					ExpressionConsoleHistoryEntry currentEntry = it.next();
					if (currentEntry.getType() == ExpressionConsoleHistoryEntry.INPUT)
						console.enterExpression(currentEntry.getString());
				}

				// restart animation if it was on before
				// animateVar.set(oldAnimateValue);
			}
		});

		return loadHistory;
	}

	/**
	 * Creates the "Save History" menu item.
	 * 
	 * @return
	 */
	private JMenuItem createSaveHistoryMenuItem() {
		JMenuItem saveHistory = new JMenuItem("Save History");
		saveHistory.setMnemonic('s');
		saveHistory.setVisible(true);
		saveHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// stop animation temporarily if it was on
				Variable animateVar = Variable
						.getVariable(Grapher2DConstants.Grapher2DAnimateFlag);
				Value oldAnimateValue = animateVar.evaluate();
				animateVar.set(new BooleanValue(false));

				// save the script
				ActionScriptSaveUtilities.promptUserToSaveScript();

				// restart animation if it was on before
				animateVar.set(oldAnimateValue);
			}
		});

		return saveHistory;
	}

	/**
	 * Creates the "Animation" menu.
	 * 
	 * @return
	 */
	private JMenu createAnimationMenu() {
		JMenu animateMenu = new JMenu("Animation");
		animateMenu.setMnemonic('a');
		JMenuItem animationSettings = new JMenuItem("Animation Settings");
		animationSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExpressionConsoleModel.getInstance().enterExpression(
						"edit(" + Grapher2DConstants.Grapher2DAnimateFlag + ","
								+ Grapher2DConstants.TimeIncrement + ")");
			}
		});
		// animationSettings.setVisible(true);
		animateMenu.add(animationSettings);
		return animateMenu;
	}

	/**
	 * Creates the "Edit Variables" menu.
	 * 
	 * @return
	 */
	private JMenu createEditVariableMenu() {
		JMenu editVariablesMenu = new JMenu("Edit Variables");
		editVariablesMenu.setMnemonic('v');
		editVariablesMenu.add(editVariablesMenuPanel);
		return editVariablesMenu;
	}

	/**
	 * Creates the Examples menu.
	 * 
	 * @return
	 */
	private JMenu createExamplesMenu() {
		JMenu examplesMenu = new JMenu("Examples");
		examplesMenu.setMnemonic('e');
		examplesMenu.add(constructExampleMenuItem("Simple sine wave",
				"y = sin(x)", 's'));
		examplesMenu.add(constructExampleMenuItem("Time", "y = sin(x+t)", 't'));
		examplesMenu.add(constructExampleMenuItem("Variables",
				"a = sin(4*x); b = 2*sin(x/2);y = a+b", 'v'));

		examplesMenu.add(constructExampleMenuItem("Binary Operators",
				"y = 1+2-3*4/5^6E-7%8!", 'b'));
		examplesMenu
				.add(constructExampleMenuItem(
						"Trigonometry",
						"y = sin(3)+cos(5)+tan(5)+cot(.5)+sec(.5)+csc(.5)+asin(0.5)+acos(.5)+atan(.5)",
						't'));
		examplesMenu
				.add(constructExampleMenuItem(
						"Other Operators",
						"y = ln(5)+log(1000)+abs(-5)+sqrt(25)+round(6.5)+floor(6.6)+ceil(6.4)-30",
						'o'));
		examplesMenu.add(constructExampleMenuItem("Constants",
				"y = pi+e+1/Infinity", 'o'));
		examplesMenu.add(constructExampleMenuItem("Conditional",
				"y = x<1?5^x:5", 'n'));
		examplesMenu.add(constructExampleMenuItem("Boolean Algebra",
				"y = (x<1&&x>-1)||x>8?5:-5", 'b'));

		examplesMenu
				.add(constructExampleMenuItem(
						"Magical Mathematics",
						"a = .7;y = sin(x*5*(1+sin(t/8))-t);b = sin(a+x/2+t/4);y = y<b?y*10:b*10",
						'm'));
		return examplesMenu;
	}

	/**
	 * Constructs a menu item intended for use in the Examples menu.
	 * 
	 * @param menuText
	 * @param functionString
	 * @param mnemonicChar
	 * @return
	 */
	private JMenuItem constructExampleMenuItem(String menuText,
			String functionString, char mnemonicChar) {
		JMenuItem menuItem = new JMenuItem(menuText);
		// store the function in the action command for use later
		menuItem.setActionCommand(functionString);
		menuItem.addActionListener(this);
		menuItem.setVisible(true);
		menuItem.setMnemonic(mnemonicChar);
		return menuItem;
	}

	/**
	 * Recieves action events when file menu items are clicked
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		// set the function string to the one which is in the action
		// command of the menu item by entering it as an expression in the
		// expression console
		console.enterExpression(Grapher2DConstants.Grapher2DFunctionString
				+ " = \"" + e.getActionCommand() + "\"");
		// the function string variable is being observed by the view and
		// controller, and as a result of this code execution will be
		// appropriately notified.

	}
}
