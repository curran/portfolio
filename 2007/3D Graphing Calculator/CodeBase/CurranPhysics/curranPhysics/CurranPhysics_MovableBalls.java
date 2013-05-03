package curranPhysics;

import java.awt.event.MouseEvent;

import primitives3D.Object3D;
import primitives3D.Vector3D;

/**
 * A class which adds the feature of being able to move particles when in 2D
 * mode.
 * 
 * @author Curran Kelleher
 * 
 */
public class CurranPhysics_MovableBalls extends CurranPhysics {
	private static final long serialVersionUID = 324142777886101244L;

	Object3D grabbedObject = null;

	Vector3D positionOfGrabbedObject = new Vector3D();

	protected void updateForEachFrame() {
		super.updateForEachFrame();
		if (grabbedObject != null)
			grabbedObject.setCenterPoint(positionOfGrabbedObject);
	}

	public void mouseDragged(MouseEvent e) {
		if (viewer.window.drawFor3D)
			super.mouseDragged(e);
		else if (grabbedObject != null) {
			positionOfGrabbedObject.x = viewer.window.getXvalue(e.getX());
			positionOfGrabbedObject.y = viewer.window.getYvalue(e.getY());
			grabbedObject.setCenterPoint(positionOfGrabbedObject);
			//drawObjectsOnBufferImage();
			//repaint();
		}
	}

	public void mousePressed(MouseEvent e) {

		if (viewer.window.drawFor3D)
			super.mousePressed(e);
		else
		{
			grabbedObject = viewer.getObjectWhichContainsPoint(e.getPoint());
			if (grabbedObject != null) {
				positionOfGrabbedObject.x = viewer.window.getXvalue(e.getX());
				positionOfGrabbedObject.y = viewer.window.getYvalue(e.getY());
			}
		}

	}

	public void mouseReleased(MouseEvent e) {
		if (viewer.window.drawFor3D)
			super.mouseReleased(e);
		else
			grabbedObject = null;
	}
}
