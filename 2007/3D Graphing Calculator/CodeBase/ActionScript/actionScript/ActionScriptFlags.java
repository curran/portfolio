package actionScript;

/**
 * Contains flags telling the system for to behave with regard to recording
 * events.
 * 
 * @author Curran Kelleher
 * 
 */
public class ActionScriptFlags {
	/**
	 * If this is set to false, things necessary only for history playback are
	 * not logged (logged meaning executed on the console as a script which can
	 * later be written to a log-like file).
	 */
	public static final boolean LOGGING_ENABLED = true;

	/**
	 * When true, the displaying of error messages is disabled. Normally this
	 * should be false. It should only be set to true when replaying an action
	 * script.
	 */
	public static boolean suppressWarningDialogBoxes = false;

	/**
	 * The file extention to use for history files.
	 */
	public final static String FILE_EXTENTION = "hist";
}
