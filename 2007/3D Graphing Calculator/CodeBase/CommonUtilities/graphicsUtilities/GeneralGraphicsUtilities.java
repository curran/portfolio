package graphicsUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * A class containing static methods which do useful high level graphics and UI
 * things.
 * 
 * @author Curran Kelleher
 * 
 */
public class GeneralGraphicsUtilities {
	/**
	 * Draws text in red with appropriate word wrap.
	 * 
	 * @param g
	 *            the Graphics on which to draw
	 * @param message
	 *            the error message
	 * @param widthOfDrawingSpace
	 *            the width (in pixels) of the "paragraph"
	 */
	public static void drawErrorMessage(Graphics2D g, String message,
			int widthOfDrawingSpace) {
		LineBreakMeasurer linebreaker = new LineBreakMeasurer(
				new AttributedString(message).getIterator(), g
						.getFontRenderContext());
		g.setColor(Color.red);
		float y = 0.0f;
		while (linebreaker.getPosition() < message.length()) {
			TextLayout tl = linebreaker.nextLayout(widthOfDrawingSpace);
			y += tl.getAscent();
			tl.draw(g, 0, y);
			y += tl.getDescent() + tl.getLeading();
		}
	}

	/**
	 * Centers a JFrame on the screen and gives it the specified width and
	 * height
	 * 
	 * @param frame
	 *            the frame to center
	 * @param width
	 *            the width of the frame
	 * @param height
	 *            the height of the frame
	 */
	public static void centerFrame(JFrame frame, int width, int height) {
		frame.setSize(new Dimension(width, height));
		centerFrame(frame);
	}

	/**
	 * Centers a JFrame on the screen
	 * 
	 * @param frame
	 *            the frame to center
	 */
	public static void centerFrame(JFrame frame) {
		Dimension frameSize = frame.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(screenSize.width / 2 - frameSize.width / 2,
				screenSize.height / 2 - frameSize.height / 2, frameSize.width,
				frameSize.height);
	}

	/**
	 * Sets the look and feel of the calling Java program to that of the system which it is running on.
	 *
	 */
	public static void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
