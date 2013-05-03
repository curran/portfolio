package expressionConsole;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;


public /**
 * A menu item which enters the specified command in the expression console
 * when clicked
 * 
 * @author Curran Kelleher
 * 
 */
class CommandExecutingMenuItem extends JMenuItem {
	private static final long serialVersionUID = -4273995539338480663L;

	/**
	 * Create a menu item which enters the specified command in the
	 * expression console when clicked.
	 * 
	 * @param text
	 *            the title of the menu item
	 * @param mnemonic
	 *            the mnemonic
	 * @param command
	 *            the command that will be executed in the console when the
	 *            menu item is clicked.
	 */
	public CommandExecutingMenuItem(String text, char mnemonic,
			String command) {
		super(text);
		setMnemonic(mnemonic);

		class CommandMenuItemActionListener implements ActionListener {
			String command;

			public CommandMenuItemActionListener(String command) {
				this.command = command;
			}

			public void actionPerformed(ActionEvent e) {
				ExpressionConsoleModel.getInstance().enterExpression(
						command);
			}
		}
		addActionListener(new CommandMenuItemActionListener(command));
	}
}