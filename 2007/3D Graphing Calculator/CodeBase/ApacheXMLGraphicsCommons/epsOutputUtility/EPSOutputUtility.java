package epsOutputUtility;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JComponent;

import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;

/**
 * A utility class for generating EPS files, powered by Apache XML Graphics
 * Commons.
 * 
 * @author Curran Kelleher
 * 
 */
public class EPSOutputUtility {
	/**
	 * Creates an EPS file based on what is painted when
	 * componentToPaint.paint() is called
	 * 
	 * @param componentToPaint
	 *            The component whose paint(), getWidth(), and getHeight()
	 *            methods will be called to generate the contents of the EPS
	 *            file
	 * @param outputFile
	 *            the output EPS file
	 * @throws IOException
	 */
	public static void paintJComponentToEPSFile(JComponent componentToPaint,
			File outputFile) throws IOException {
		OutputStream out = new java.io.FileOutputStream(outputFile);
		out = new java.io.BufferedOutputStream(out);
		try {
			// Instantiate the EPSDocumentGraphics2D instance
			EPSDocumentGraphics2D g2d = new EPSDocumentGraphics2D(false);
			g2d
					.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());

			// Set up the document size
			g2d.setupDocument(out, componentToPaint.getWidth(),
					componentToPaint.getHeight());

			// paint the component
			componentToPaint.paint(g2d);

			// Cleanup
			g2d.finish();
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
}
