package drawing3D;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

/**
 * A panel which draws 3D objects and allows the user to rotate the view of them
 * with the mouse.
 * 
 * Use the field of this class called "viewer" to manipulate 3D objects. Upon
 * construction, an animation thread is started which redraws the 3D objects
 * frequently.
 * 
 * @author Curran Kelleher
 * 
 */
public class RotatableObject3DViewingPanel extends MinimalObject3DViewingPanel
		implements Runnable, MouseInputListener {
	private static final long serialVersionUID = 6264324613452387956L;

	/**
	 * A boolean flag signaling whether or not the view should be animating.
	 */
	protected boolean animate = true;

	/**
	 * When this is set to true, the animation thread is stopped permanently.
	 */
	public boolean stopThread = false;

	/**
	 * The per-frame increment of the rotation angle about the X axis.
	 */
	protected double rotationIncrementX = 0;

	/**
	 * The per-frame increment of the rotation angle about the Y axis.
	 */
	protected double rotationIncrementY = 0;

	/**
	 * Temporary variable used when the mouse is being dragged to calculate what
	 * the rotationIncrementX should be when the mouse is released.
	 */
	double tempRotationIncrementX = 0;

	/**
	 * Temporary variable used when the mouse is being dragged to calculate what
	 * the rotationIncrementY should be when the mouse is released.
	 */
	double tempRotationIncrementY = 0;

	/**
	 * The angle of rotation in the "X" direction on the screen.
	 */
	protected double rotationStateX = 2.7;

	/**
	 * The angle of rotation in the "Y" direction on the screen.
	 */
	protected double rotationStateY = 1;

	/**
	 * Temporary variable used when the mouse is being dragged to calculate the
	 * amount the mouse had moved since the last mouseMoved event.
	 */
	double oldMouseX = 0;

	/**
	 * Temporary variable used when the mouse is being dragged to calculate the
	 * amount the mouse had moved since the last mouseMoved event.
	 */
	double oldMouseY = 0;

	/**
	 * The factor which converts pixels of mouse movement to angles of rotation.
	 */
	protected double mouseRotationFactor = 0.006;

	/**
	 * Reset to System.currentTimeMillis() every time the mouse is dragged. If
	 * the user has held the mouse still for a while (more than 400 ms) before
	 * letting go, the the intention probably is to make it stay still after the
	 * mouse is released, not set it spinning. This variable is used for
	 * checking that.
	 */
	long lastTimeMouseWasDragged = 0;

	/**
	 * Construct an empty RotatableObject3DViewingPanel. Use the field of this
	 * class called "viewer" to manipulate 3D objects. Upon construction, an
	 * animation thread is started which redraws the 3D objects frequently.
	 * 
	 */
	public RotatableObject3DViewingPanel() {
		super();
		// add the mouse listener
		addMouseMotionListener(this);
		addMouseListener(this);
		(new Thread(this)).start();
	}

	/**
	 * Called to start the animation Thread.
	 */
	@SuppressWarnings("static-access")
	public void run() {
		while (!stopThread) {
			// only update the graph if the animate flag is true
			if (animate) {
				updateForEachFrame();
			}
			try {
				Thread.currentThread().sleep(!animate ? 200 : 20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Called every frame.
	 * 
	 */
	protected void updateForEachFrame() {
		// update the rotation
		rotationStateX += rotationIncrementX;
		if (rotationStateX < 0)
			rotationStateX += 2 * Math.PI;
		else if (rotationStateX > 2 * Math.PI)
			rotationStateX -= 2 * Math.PI;

		rotationStateY += rotationIncrementY;
		if (rotationStateY < 0)
			rotationStateY += 2 * Math.PI;
		else if (rotationStateY > 2 * Math.PI)
			rotationStateY -= 2 * Math.PI;

		// set the rotation
		viewer.window.setRotation(rotationStateY, 0, rotationStateX);

		// draw the objects
		drawObjectsOnBufferImage();

		// draw the buffered image to the screen
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
		rotationStateX += (tempRotationIncrementX = (oldMouseX - e.getX())
				* mouseRotationFactor * ((rotationStateY < Math.PI) ? -1 : 1));
		rotationStateY += (tempRotationIncrementY = (oldMouseY - e.getY())
				* mouseRotationFactor);

		oldMouseX = e.getX();
		oldMouseY = e.getY();
		lastTimeMouseWasDragged = System.currentTimeMillis();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		rotationIncrementX = rotationIncrementY = tempRotationIncrementX = tempRotationIncrementY = 0;
		oldMouseX = e.getX();
		oldMouseY = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		// if the user has held the mouse still for a while (more than 400 ms)
		// before letting go, the the intention probably is to make it stay
		// still after the mouse is released, not set it spinning
		if (System.currentTimeMillis() - lastTimeMouseWasDragged > 400)
			setRotationIncrementValues(0, 0);
		else
			setRotationIncrementValues(tempRotationIncrementX,
					tempRotationIncrementY);
	}

	/**
	 * Sets the rotation increment values. This method is called when the mouse
	 * is released. It sets the current rotation increment parameters to the
	 * specified values.
	 * 
	 * @param rotationIncrementX
	 *            the new rotation increment value for the "X" direction
	 * @param rotationIncrementY
	 *            the new rotation increment value for the "Y" direction
	 */
	protected void setRotationIncrementValues(double rotationIncrementX,
			double rotationIncrementY) {
		this.rotationIncrementX = rotationIncrementX;
		this.rotationIncrementY = rotationIncrementY;
	}

}
