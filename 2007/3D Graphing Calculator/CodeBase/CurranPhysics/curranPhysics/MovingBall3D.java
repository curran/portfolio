package curranPhysics;

import java.awt.Color;

import primitives3D.Ball3D;
import primitives3D.Vector3D;
import colorMap.ColorMap;
import colorMap.ColorNode;

/**
 * A simple 3D ball which has a vector of motion and a charge.
 * 
 * @author Curran Kelleher
 * 
 */
public class MovingBall3D extends Ball3D {
	/**
	 * The vector of motion (the per-frame rates of change; (dx,dy,dz))
	 * corresponding to this ball.
	 */
	public Vector3D motionVector = new Vector3D();

	/**
	 * The charge of this ball
	 */
	private double charge = 0;

	public double chargePolarity = Math.random() > .5 ? 1 : -1;

	private Color positiveChargeColor = Color.red;

	private Color neutralChargeColor = Color.white;

	private Color negativeChargeColor = Color.blue;
	
	private static ColorMap map=null;

	/**
	 * Constructs a simple ball, with a motion vector of zero and a charge of 1.
	 * 
	 * @param p
	 *            the center point of the ball.
	 * @param radius
	 *            the radius of the ball.
	 */
	public MovingBall3D(Vector3D p, double radius) {
		super(p, radius, Color.white);
		
		setCharge(chargePolarity);
	}

	/**
	 * Sets the charge of this ball, and updates it's color according to the new
	 * charge.
	 * 
	 * @param charge
	 */
	public void setCharge(double charge) {
		if (this.charge != charge) {
			this.charge = charge;
			if(map == null)
			{
				ColorNode[] colorNodes = { new ColorNode(negativeChargeColor, 0),
						new ColorNode(neutralChargeColor, 0.5),
						new ColorNode(positiveChargeColor, 1) };
				map = new ColorMap(colorNodes);
			}
			color = map.getColorAtValue(charge/2+0.5);
		}
	}

	/**
	 * Gets the charge of this ball.
	 * 
	 * @return
	 */
	public double getCharge() {
		return charge;
	}

	/**
	 * Increments the position of the ball with the motion vector.
	 * 
	 */
	public void applyMotionVector() {
		p.plus(motionVector, p);
	}

}