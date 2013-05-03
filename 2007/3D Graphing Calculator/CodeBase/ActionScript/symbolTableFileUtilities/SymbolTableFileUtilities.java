package symbolTableFileUtilities;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fileUtilities.ExtentionFileFilter;
import fileUtilities.SaveUtility;

/**
 * A class containing utility methods for loading, storing, and applying files
 * containing the state of the symbol table
 * 
 * @author Curran Kelleher
 * 
 */
public class SymbolTableFileUtilities {
	/**
	 * The file extention to use for symbol table files.
	 */
	public final static String FILE_EXTENTION = "graph";

	/**
	 * Shows the user a file chooser. If the user clicks "Save", then
	 * saveSymbolTable() is called with the chosen file as the argument.
	 */
	public static void promptUserToSaveSymbolTable() {
		File f = SaveUtility.promptUserToSave(FILE_EXTENTION);
		if (f != null)
			saveSymbolTable(f);
	}

	/**
	 * Saves the current symbol table state in the specified File.
	 * 
	 * @param outputFile
	 */
	public static void saveSymbolTable(File outputFile) {
		List<String> symbolTableSnapshot = SymbolTableStateUtilities.takeSymbolTableSnapshot();
		try {
			XMLEncoder e = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(outputFile)));
			e.writeObject(symbolTableSnapshot);
			e.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error writing file.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Shows the user a file chooser, then calls loadSymbolTable() with the
	 * selected file.
	 * 
	 * @return the list of variable assignment expressions which can be used to
	 *         recreate the state of the symbol table
	 */
	public static List<String> promptUserToLoadSymbolTable() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new ExtentionFileFilter(
				FILE_EXTENTION));
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			return loadSymbolTable(chooser.getSelectedFile());
		return null;
	}

	/**
	 * Loads a symbol table map from the specified file.
	 * 
	 * @param inputFile
	 *            the file to read from
	 * @return the list of variable assignment expressions which can be used to
	 *         recreate the state of the symbol table
	 */
	@SuppressWarnings("unchecked")
	public static List<String> loadSymbolTable(File inputFile) {
		List<String> symbolTable = null;
		try {
			XMLDecoder d = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(inputFile)));
			symbolTable = (List<String>) d.readObject();
			d.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error loading file.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return symbolTable;
	}

	/**
	 * Prompts the user to open a symbol table file, then applies the symbol
	 * table that the user chose.
	 */
	public static void promptUserToloadAndApplySymbolTable() {
		List<String> newSymbolTable = promptUserToLoadSymbolTable();
		if (newSymbolTable != null)
			SymbolTableStateUtilities.applySymbolTableSnapshot(newSymbolTable);
	}

}
