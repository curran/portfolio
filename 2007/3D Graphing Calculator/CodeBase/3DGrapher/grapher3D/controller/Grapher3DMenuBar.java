package grapher3D.controller;

import epsOutputUtility.EPSOutputUtility;
import expressionConsole.CommandExecutingMenuItem;
import expressionConsole.ExpressionConsoleGUI;
import expressionConsole.ExpressionConsoleModel;
import fileUtilities.SaveUtility;
import grapher3D.Grapher3DConstants;
import grapher3D.view.Grapher3DView;
import graphicsUtilities.GeneralGraphicsUtilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ProgressMonitor;

import logEnabledComponents.LogEnabledJFrame;
import primitives3D.SolidRod3D;
import symbolTableFileUtilities.SymbolTableFileUtilities;
import variableEditorUI.EditVariablesMenuPanel;
import variables.Variable;
import actionScript.ActionScriptLoadUtilities;
import actionScript.ActionScriptPlayer;
import actionScript.ActionScriptSaveUtilities;
import drawing3D.Object3DViewer;

/**
 * The class whose method createMenuBar() is responsible for creating the menu
 * bar of the 3D Grapher GUI
 * 
 * @author Curran Kelleher
 * 
 */
public class Grapher3DMenuBar {
	/**
	 * Creates the menu bar for the 3D Grapher GUI.
	 * 
	 * @param controller
	 *            the controller which some menu items will interact with.
	 * @param view
	 *            the view which some menu items will interact with.
	 * 
	 * @return
	 */
	public static JMenuBar createMenuBar(Grapher3DController controller,
			Grapher3DView view) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu(view));
		menuBar.add(new CoordinateSystemSelectorMenu(
				controller.coordinateSystem));
		menuBar.add(createEditVariableMenu());
		menuBar.add(createSettingsMenu());
		menuBar.add(createHelpMenu());

		return (menuBar);
	}

	/**
	 * Creates the "File" menu.
	 * 
	 * @param view
	 *            the view used for exporting as EPS
	 * 
	 * @return
	 */
	private static JMenu createFileMenu(Grapher3DView view) {
		JMenu menu = new JMenu("File");
		menu.setMnemonic('f');
		// create the Open menu item
		menu.add(createSaveOrLoadMenuItem(false, false));
		// create the Save menu item
		menu.add(createSaveOrLoadMenuItem(true, false));

		// create the Open History menu item
		// menu.add(createSaveOrLoadMenuItem(false, true));
		// create the Save History menu item
		// menu.add(createSaveOrLoadMenuItem(true, true));

		menu.addSeparator();
		// create the export as EPS menu item
		menu.add(createExportAsEPSMenuItem(view));

		return menu;
	}

	/**
	 * Creates an "Export as EPS" menu item.
	 * 
	 * @param view
	 *            The view whose contents will be exported.
	 * @return the menu item which, when clicked, will prompt the user to choose
	 *         a location to save the eps file, then generate and save the eps
	 *         file.
	 */
	@SuppressWarnings("serial")
	private static JMenuItem createExportAsEPSMenuItem(Grapher3DView view) {
		final Object3DViewer viewer = view.viewer;
		JMenuItem menuItem = new JMenuItem("Export as EPS");
		menuItem.setMnemonic('e');
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final File f = SaveUtility.promptUserToSave("eps");
				if (f != null) {
					// do it in a separate thread so the progress bar works
					(new Thread(new Runnable() {

						
						public void run() {

							// draw the lines nicely for the EPS
							SolidRod3D.drawAsSimpleLine = false;

							try {
								EPSOutputUtility.paintJComponentToEPSFile(
										new JPanel() {
											public void paint(Graphics g) {
												viewer
														.drawObjectsOnThis(
																g,
																new ProgressMonitor(
																		null,
																		"Generating EPS file...",
																		null,
																		0, 100));
											}

											public int getWidth() {
												return viewer.window.width;
											}

											public int getHeight() {
												return viewer.window.height;
											}
										}, f);
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane
										.showMessageDialog(
												null,
												"An error occurred while writing the file",
												"Error",
												JOptionPane.ERROR_MESSAGE);
							}
							// draw the lines quickly for the screen again
							SolidRod3D.drawAsSimpleLine = true;
						}
					})).start();
				}
			}
		});
		return menuItem;
	}

	/**
	 * Creates the "Save History" menu item.
	 * 
	 * @param save
	 *            true to create the a menu item for saving, false to create a
	 *            menu item for loading.
	 * @param history
	 *            true to create menu items to load or save the history, false
	 *            to create menu items to load or save a symbol table snapshot.
	 */
	private static JMenuItem createSaveOrLoadMenuItem(boolean save,
			boolean history) {
		final boolean promptForSave = save;
		final boolean promptForHistory = history;
		JMenuItem saveHistory = new JMenuItem((save ? "Save" : "Open")
				+ (history ? " History" : ""));
		saveHistory.setMnemonic(save ? 's' : 'o');
		saveHistory.setVisible(true);
		saveHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (promptForSave) {
					if (promptForHistory)
						// save the history script
						ActionScriptSaveUtilities.promptUserToSaveScript();
					else
						// save the symbol table snapshot
						SymbolTableFileUtilities.promptUserToSaveSymbolTable();
				} else {// if loading...
					if (promptForHistory)
						// load the script and execute it
						ActionScriptPlayer
								.executeScript(ActionScriptLoadUtilities
										.promptUserToLoadScript());
					else {// if loading the symbol table
						// make the gui frame variable temporarily unmodifiable
						// so we don't lose it!
						Variable guiFrameVariable = Variable
								.getVariable(Grapher3DConstants.GUIFrameVariable);
						guiFrameVariable.modifiable = false;

						// load the symbol table and apply it
						SymbolTableFileUtilities
								.promptUserToloadAndApplySymbolTable();

						// back to normal
						guiFrameVariable.modifiable = true;
					}
				}

			}
		});

		return saveHistory;
	}

	/**
	 * Creates the "Edit Variables" menu.
	 * 
	 * @return
	 */
	private static JMenu createEditVariableMenu() {
		JMenu menu = new JMenu("Edit");
		menu.setMnemonic('e');
		menu.add(new EditVariablesMenuPanel());
		JMenuItem consoleMenuItem = new JMenuItem("Show Console");
		consoleMenuItem.setMnemonic('s');
		consoleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				(new ExpressionConsoleGUI()).setVisible(true);
			}
		});
		menu.add(consoleMenuItem);
		return menu;
	}

	/**
	 * Creates the "Settings" menu.
	 * 
	 * @return
	 */
	private static JMenu createSettingsMenu() {
		JMenu animateMenu = new JMenu("Settings");
		animateMenu.setMnemonic('s');

		animateMenu.add(new CommandExecutingMenuItem("Animation Settings", 'a',
				"edit(" + Grapher3DConstants.Grapher3DAnimateFlag + ","
						+ Grapher3DConstants.TimeIncrement + ")"));

		animateMenu.add(new CommandExecutingMenuItem("Rotation", 'r', "edit("
				+ Grapher3DConstants.rotationStateX + ", "
				+ Grapher3DConstants.rotationStateY + ", "
				+ Grapher3DConstants.rotationIncrementX + ", "
				+ Grapher3DConstants.rotationIncrementY + ")"));

		animateMenu.add(new CommandExecutingMenuItem("Graph Drawing", 'g',
				"edit(" + Grapher3DConstants.GraphResolution + ", "
						+ Grapher3DConstants.Grapher3DWireframeFlag + ", "
						+ Grapher3DConstants.Grapher3DShowAxesFlag + ")"));

		animateMenu.add(new CommandExecutingMenuItem("Colors", 'c', "edit("
				+ Grapher3DConstants.AxesColor + ", "
				+ Grapher3DConstants.DefaultGraphColor + ", "
				+ Grapher3DConstants.BackgroundColor + ", "
				+ Grapher3DConstants.ColorMap + ")"));

		return animateMenu;
	}

	/**
	 * Creates the "Help" menu
	 * 
	 * @return
	 */
	private static JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('h');
		
		JMenuItem userManualMenuItem = new JMenuItem("User Manual");
		userManualMenuItem.setMnemonic('u');

		userManualMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				(new ExpressionConsoleGUI()).setVisible(true);
				ExpressionConsoleModel.getInstance().enterExpression("userManual()");
			}
		});
		helpMenu.add(userManualMenuItem);
		
		JMenuItem aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.setMnemonic('a');

		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame aboutFrame = new JFrame("About Mathematorium");
				GeneralGraphicsUtilities.centerFrame(aboutFrame, 500, 170);

				JTextArea textArea = new JTextArea();
				textArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea
						.setText("Mathematorium"
								+ "\n\nVersion 1.0"
								+ "\n© Copyright 2007 Curran Kelleher"
								+ "\n\nThis product includes software developed by the Apache Software Foundation http://www.apache.org/");
				textArea.setEditable(false);
				JLabel imageLabel = new JLabel(new ImageIcon(Toolkit
						.getDefaultToolkit().getImage("mathematorium.jpg")));
				JPanel panel = new JPanel();
				panel.add(imageLabel);
				panel.setBackground(Color.white);
				JSplitPane splitPane = new JSplitPane(
						JSplitPane.HORIZONTAL_SPLIT, panel, textArea);
				splitPane.setDividerSize(2);
				splitPane.setEnabled(false);
				// imagePanel.add(label);
				aboutFrame.add(splitPane);

				aboutFrame.setIconImage(LogEnabledJFrame.defaultIconImage);
				aboutFrame.setVisible(true);
			}
		});
		helpMenu.add(aboutMenuItem);
		return helpMenu;
	}
}
