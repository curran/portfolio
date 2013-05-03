package actionScript;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import expressionConsole.ExpressionConsoleHistory;
import expressionConsole.ExpressionConsoleModel;
import fileUtilities.SaveUtility;

/**
 * A utilities class for saving scripts.
 * 
 * @author Curran Kelleher
 * 
 */
public class ActionScriptSaveUtilities {

	/**
	 * Shows the user a file chooser. If the user clicks "Save", then
	 * saveScript() is called with the chosen file as the argument.
	 */
	public static void promptUserToSaveScript() {
		File f = SaveUtility.promptUserToSave(ActionScriptFlags.FILE_EXTENTION);
		if (f != null)
			saveScript(f);
	}

	

	/**
	 * Saves the current model of the history in the specified File.
	 * 
	 * @param outputFile
	 */
	public static void saveScript(File outputFile) {
		ExpressionConsoleHistory expressionHistory = ExpressionConsoleModel
				.getInstance().getExpressionHistory();

		try {
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(outputFile)));
			e.writeObject(expressionHistory);
			e.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
