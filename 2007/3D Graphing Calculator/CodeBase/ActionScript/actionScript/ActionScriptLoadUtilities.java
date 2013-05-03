package actionScript;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import expressionConsole.ExpressionConsoleHistory;
import fileUtilities.ExtentionFileFilter;

/**
 * A utilities class for loading scripts.
 * 
 * @author Curran Kelleher
 * 
 */
public class ActionScriptLoadUtilities {

	/**
	 * Shows the user a file chooser, then calls loadScript(), passing the
	 * chosen file and the specified model as parameters.
	 * 
	 * @return the ExpressionConsoleHistory object loaded from the file, or null
	 *         if unsuccessful.
	 */
	public static ExpressionConsoleHistory promptUserToLoadScript() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new ExtentionFileFilter(ActionScriptFlags.FILE_EXTENTION));
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			return loadScript(chooser.getSelectedFile());
		return null;
	}

	/**
	 * Saves the current model of the history
	 * 
	 * @param outputFile
	 * @return the ExpressionConsoleHistory object loaded from the file, or null
	 *         if unsuccessful.
	 */
	public static ExpressionConsoleHistory loadScript(File inputFile) {
		ExpressionConsoleHistory expressionHistory = null;
		try {
			XMLDecoder d = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(inputFile)));
			expressionHistory = (ExpressionConsoleHistory) d.readObject();
			d.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return expressionHistory;
	}
}