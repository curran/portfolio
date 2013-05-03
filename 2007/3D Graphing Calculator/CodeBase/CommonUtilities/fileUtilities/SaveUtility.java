package fileUtilities;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SaveUtility {

	/**
	 * Prompts the user to save a file with the specified file extention. If any
	 * errors occur, an error dialog box is displayed to the user.
	 * 
	 * @param fileExtention
	 *            the file extention that the saved file will have.
	 * @return the selected file, or null if no file was selected or an error
	 *         occurred.
	 */
	public static File promptUserToSave(String fileExtention) {
		fileExtention = fileExtention.toLowerCase();
		JFileChooser chooser = new JFileChooser();
		//chooser.set
		chooser.setFileFilter(new ExtentionFileFilter(fileExtention));
		//if the user clicks "save"...
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			String name = f.getName();
			
			
			
			if (name.contains(".")) {
				int i = name.lastIndexOf(".") + 1;
				if (i < name.length()) {
					String ext = name.substring(i).toLowerCase();
					if (!ext.equals(fileExtention))
						JOptionPane
								.showMessageDialog(
										null,
										ext
												+ " is not a valid file extension. The file will not be saved.",
										"Human Error!",
										JOptionPane.ERROR_MESSAGE);
				
				} else
					JOptionPane
							.showMessageDialog(
									null,
									"\".\" is not a valid file extension. The file will not be saved.",
									"Human Error!", JOptionPane.ERROR_MESSAGE);

			} else
				f = new File(f.getPath() + "."
						+ fileExtention);

			if (f.exists())
				if (JOptionPane.showConfirmDialog(null, "Overwrite the file "
						+ f.getPath() + "?", "Overwrite file?",
						JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
					return null;
			
			return f;
		}
		return null;
	}

}
