package expressionConsole;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * The view of the Model View Controller paradigm for the expression console.
 * This view is a graphical text representation of the ExpressionConsoleModel.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleView extends JScrollPane implements Observer,
		AdjustmentListener {
	private static final long serialVersionUID = 6185291782620102768L;

	/**
	 * A reference to the singleton model. (so we don't need call getInstance()
	 * many times)
	 */
	ExpressionConsoleModel model = ExpressionConsoleModel.getInstance();

	/**
	 * The internal model for the text. It is used to specify the style of
	 * specific text segments.
	 */
	StyledDocument document;

	/**
	 * The JTextPanel which displays the entry history as text.
	 */
	JTextPane textPane;

	/**
	 * A counter used to keep track of the length of the history which is
	 * currently displayed.
	 */
	int displayedHistorySize = 0;

	/**
	 * A flag used to determine whether or not the scroll bar listener should
	 * scroll to the bottom in response to scroll bar property changes. It
	 * should only do this when the change comes from the program, not the user,
	 * so that's when this flag is set to true.
	 */
	boolean scrollBarAdjustmentWasFromTheUser = true;

	/**
	 * Upon construction, the colors and styles of the textPane are set up.
	 * 
	 */
	public ExpressionConsoleView() {
		// create the text pane
		textPane = new JTextPane();
		textPane.setEditable(false);

		// add it to this JScrollPane
		setViewportView(textPane);

		// set the background and foreground (default for text) colors
		textPane.setBackground(Color.black);
		textPane.setForeground(Color.orange);

		// set the font
		textPane.setFont(Font.decode("Monospaced-PLAIN-13"));

		// add the INPUT style
		Style style = textPane.addStyle(ExpressionConsoleHistoryEntry.INPUT
				+ "", null);
		StyleConstants.setForeground(style, Color.orange);

		// add the CONSOLE_RESPONSE_SUCCESS style
		style = textPane.addStyle(
				ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_SUCCESS + "",
				null);
		StyleConstants.setForeground(style, Color.green);

		// add the CONSOLE_RESPONSE_ERROR style
		style = textPane
				.addStyle(ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_ERROR
						+ "", null);
		StyleConstants.setForeground(style, Color.red);
		StyleConstants.setBold(style, true);

		// add the MESSAGE style
		style = textPane.addStyle(ExpressionConsoleHistoryEntry.MESSAGE + "",
				null);
		StyleConstants.setForeground(style, Color.CYAN);

		// get a reference to the internal document for later re-use
		document = textPane.getStyledDocument();

		// add a listener to the scrollbar which will ensure the desired
		// behavior that the view is always scrolled to the very bottom every
		// time an update is made.
		getVerticalScrollBar().addAdjustmentListener(this);

		// initialize the display to show the current contents of the model by
		// sending an artificial update noficaiton
		update(null, null);
	}

	/**
	 * Responds to scroll bar changes
	 */
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (!scrollBarAdjustmentWasFromTheUser)
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());
		scrollBarAdjustmentWasFromTheUser = true;
	}

	/**
	 * Responds to notifications from the model when it has changed.
	 */
	public void update(Observable o, Object arg) {
		// extract the text from the model and display it
		List<ExpressionConsoleHistoryEntry> historyList = model
				.getExpressionHistory().getExpressionList();
		int actualHistorySize = historyList.size();

		// set the flag so that the scroll bar listener scrolls down to the
		// bottom
		scrollBarAdjustmentWasFromTheUser = false;
		try {
			for (int i = displayedHistorySize; i < actualHistorySize; i++) {
				ExpressionConsoleHistoryEntry currentEntry = historyList.get(i);

				// stylize the text (remember that upon construction, the styles
				// map was coordinated to String representations of the type
				// codes.)
				document.insertString(document.getLength(), currentEntry
						.getString()
						+ "\n", textPane.getStyle(currentEntry.getType() + ""));

			}
			// catch the exeption which could be thrown from
			// document.insertString()
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		// update displayedHistorySize for next time
		displayedHistorySize = actualHistorySize;
	}
}
