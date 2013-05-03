package curranPhysics;

import javax.swing.JApplet;

@SuppressWarnings("serial")
/**
 * An applet which displays a button to start CurranPhysics
 */
public class CurranPhysicsApplet extends JApplet {
	public void init() {
		// launch it
		VariableBridgedCurranPhysics.main(null);
	}
}
