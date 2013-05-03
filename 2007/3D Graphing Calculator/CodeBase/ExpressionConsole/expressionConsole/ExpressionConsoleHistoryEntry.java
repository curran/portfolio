package expressionConsole;

/**
 * The class which represents entries to the console history. An
 * ExpressionConsoleHistoryEntry is essentially a String, and the type of entry.
 * 
 * @see expressionConsole.ExpressionConsoleHistory
 * 
 * @author Curran Kelleher
 * 
 */
public class ExpressionConsoleHistoryEntry {
	/**
	 * The constant used to represent the fact that the type of
	 * ExpressionConsoleHistoryEntry is a user input.
	 */
	public final static int INPUT = 0;

	/**
	 * The constant used to represent the fact that the type of
	 * ExpressionConsoleHistoryEntry is a console response indicating successful
	 * execution of the command (no errors).
	 */
	public final static int CONSOLE_RESPONSE_SUCCESS = 1;

	/**
	 * The constant used to represent the fact that the type of
	 * ExpressionConsoleHistoryEntry is a console response indicating
	 * unsuccessful execution of the command (with errors).
	 */
	public final static int CONSOLE_RESPONSE_ERROR = 2;

	/**
	 * The constant used to represent the fact that the type of
	 * ExpressionConsoleHistoryEntry is a message.
	 */
	public final static int MESSAGE = 3;

	/**
	 * The string which comprises the entry.
	 */
	private String string;

	/**
	 * A flag stating the type of entry this is. It must be one of the following
	 * constants:<br>
	 * ExpressionConsoleHistoryEntry.INPUT for input to the console to be
	 * evaluated.<br>
	 * ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_SUCCESS for successful
	 * execution of the command (no errors)<br>
	 * ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_ERROR for unsuccessful
	 * execution of the command (with errors)<br>
	 * ExpressionConsoleHistoryEntry.MESSAGE for messages, which will be
	 * displayed but not evaluated
	 * 
	 */
	private int type;

	/**
	 * Constructs an ExpressionConsoleHistoryEntry with the specified string and
	 * type.
	 * 
	 * @param string
	 *            the String which comprises the entry
	 * @param type
	 *            A flag stating the type of entry this is. It must be one of
	 *            the following constants:<br>
	 *            ExpressionConsoleHistoryEntry.INPUT for input to the console
	 *            to be evaluated.<br>
	 *            ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_SUCCESS for
	 *            successful execution of the command (no errors)<br>
	 *            ExpressionConsoleHistoryEntry.CONSOLE_RESPONSE_ERROR for
	 *            unsuccessful execution of the command (with errors)<br>
	 *            ExpressionConsoleHistoryEntry.MESSAGE for messages, which will
	 *            be displayed but not evaluated
	 */
	public ExpressionConsoleHistoryEntry(String string, int type) {
		this.string = string;
		this.type = type;
		// check the type, print an exception if it is not valid
		if (!(type == INPUT || type == CONSOLE_RESPONSE_SUCCESS
				|| type == CONSOLE_RESPONSE_ERROR || type == MESSAGE))
			new Exception("Invalid type:" + type).printStackTrace();
	}

	/**
	 * Do not use this constructor. It is here to make this class a Java Bean so
	 * it can be written to a file using the Java XMLEncoder.
	 * 
	 */
	public ExpressionConsoleHistoryEntry() {
	}

	/**
	 * Do not use this method. It is here to make this class a Java Bean so it
	 * can be written to a file using the Java XMLEncoder.
	 * 
	 */
	public void setString(String string) {
		this.string = string;
	}

	/**
	 * Gets the string which comprises the entry.
	 */
	public String getString() {
		return string;
	}
//go go gadget Gospel!
	/**
	 * Do not use this method. It is here to make this class a Java Bean so it
	 * can be written to a file using the Java XMLEncoder.
	 * 
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the type of this entry.
	 * 
	 * @return INPUT, CONSOLE_RESPONSE_SUCCESS, CONSOLE_RESPONSE_ERROR, or
	 *         MESSAGE
	 */
	public int getType() {
		return type;
	}

}
