package drawing3D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

/**
 * A panel which draws 3D objects.
 * 
 * @author Curran Kelleher
 * 
 */
public class MinimalObject3DViewingPanel extends JPanel implements
		ComponentListener {
	private static final long serialVersionUID = -8534357451176139503L;

	/**
	 * The viewer which manages the 3D objects which will be drawn by this
	 * MinimalObject3DViewingPanel.
	 */
	public Object3DViewer viewer = new Object3DViewer();

	/**
	 * The size of this panel
	 */
	Dimension panelDimension = new Dimension();

	/**
	 * The background color behind the 3D objects.
	 */
	protected Color backgroundColor = Color.black;

	/**
	 * The buffer image.
	 */
	Image bufferImage = null;

	/**
	 * The Graphics of the buffer image.
	 */
	Graphics bufferGraphics = null;

	/**
	 * Construct an empty MinimalObject3DViewingPanel. Use the field of this
	 * class called "viewer" to manipulate 3D objects and rotation.
	 * 
	 */
	public MinimalObject3DViewingPanel() {
		// Add the resize listener, which gives the Object3DViewer viewer the
		// correct pixel dimensions and properly sizes the graphics buffer
		addComponentListener(this);
	}

	/**
	 * displays the buffered image on screen. To draw 3d objects,
	 * drawObjectsOnBufferImage() must be called before painting.
	 */
	public void paint(Graphics g) {
		if (bufferImage != null) {
			g.drawImage(bufferImage, 0, 0, this);
		}
	}

	/**
	 * Draws the 3D objects onto the buffer image.
	 */
	public void drawObjectsOnBufferImage() {
		if (bufferGraphics != null)
			drawObjectsOnThis(bufferGraphics);
	}

	/**
	 * Draws the contents of this panel (the background rectangle and the 3D
	 * objects) into the specified Graphics.
	 * 
	 * @param g
	 */
	public void drawObjectsOnThis(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, panelDimension.width, panelDimension.height);
		viewer.drawObjectsOnThis(g);
	}

	/**
	 * Inherited from ComponentListener. Sets the viewer's window to have the
	 * proper pixel space whenever this panel is resized.
	 */
	public void componentResized(ComponentEvent e) {

		getSize(panelDimension);

		viewer.window.set(panelDimension);
		viewer.window.makeWindowSquare();

		bufferImage = createImage(panelDimension.width, panelDimension.height);
		bufferGraphics = bufferImage.getGraphics();
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

}
