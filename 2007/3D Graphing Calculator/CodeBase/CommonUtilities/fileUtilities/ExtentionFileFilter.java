package fileUtilities;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A file filter for use with JFileChooser which only passes files whose
 * extension is the string extentionToAccept, specified upon construction.
 * 
 * @author Curran Kelleher
 * 
 */
public class ExtentionFileFilter extends FileFilter {
	/**
	 * the extention of the files to accept
	 */
	final String extentionToAccept;

	/**
	 * Constructs a file filter which only passes files with the specified
	 * extension.
	 * 
	 * @param extentionToAccept
	 *            the extention of the files to accept.
	 */
	public ExtentionFileFilter(String extentionToAccept) {
		this.extentionToAccept = extentionToAccept;
	}

	/**
	 * Accepts only files whose extension is the String in
	 * ActionScriptFlags.FILE_EXTENTION
	 */
	public boolean accept(File f) {
		String name = f.getName();
		if (name.contains(".")) {
			int i = name.lastIndexOf(".") + 1;
			if (i < name.length())
				if (name.toLowerCase().substring(i).equals(extentionToAccept))
					return true;
		}
		return false;
	}

	public String getDescription() {
		return "." + extentionToAccept+ " files";
	}
}
