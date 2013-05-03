package curranPhysics;

import java.awt.Color;
import java.awt.Dimension;

import primitives3D.Cube3DEdges;
import primitives3D.Vector3D;
import drawing3D.RotatableObject3DViewingPanel;

public class CurranPhysics extends RotatableObject3DViewingPanel {
	private static final long serialVersionUID = -5009278391630542659L;

	/**
	 * The number of balls
	 */
	int numBalls = 60;

	/**
	 * The viscosity, a value from 0 to 1.
	 */
	double viscosity = 0.01;

	/**
	 * The gravity.
	 */
	double gravity = 0;

	/**
	 * The strength of the attractive forces
	 */
	double attractiveForceStrength = 0;

	/**
	 * The strength of the repulsive forces
	 */
	double repulsiveForceStrength = 0;

	/**
	 * The strength of the coulombic forces
	 */
	double coulombicForceStrength = 0;

	/**
	 * The array containing the balls.
	 */
	MovingBall3D[] balls;

	/**
	 * The dimension which will store the size of the panel.
	 */
	Dimension panelSize = new Dimension();

	/**
	 * A temporary variable used in calculations
	 */
	Vector3D tempVector = new Vector3D();
	
	boolean in3D = false;

	/**
	 * A flag which is set to true after the constructor finishes, so that
	 * updateForEachFrame() doesn't try to access the array of balls before it
	 * has been created.
	 */
	boolean allset = false;

	public CurranPhysics() {
		// turn off 3D rotation and perspective
		viewer.window.drawFor3D = false;
		
		//initialize the balls
		createBalls();
		
		// set the flag that indicates that the constructor is finished
		// used by updateForEachFrame()
		allset = true;
	}
	
	protected void createBalls()
	{
		
		//clear previous balls
		viewer.clear3DObjects();
		
		//add the cube if in 3D
		if(viewer.window.drawFor3D)
		viewer.add3DObjects((new Cube3DEdges(new Vector3D(0,0,0),20,Color.green)).getLineSegments());
		
//		 add the balls
		balls = new MovingBall3D[numBalls];
		for (int i = 0; i < balls.length; i++)
		{
			viewer.add3DObject(balls[i] = new MovingBall3D(new Vector3D(Math
					.random() * 20-10, Math.random() * 20-10, viewer.window.drawFor3D?Math.random() * 20-10:0), .6));
			balls[i].shade = viewer.window.drawFor3D;
		}
		setCharges();
	}

	protected void setCharges() {
		double newValue = coulombicForceStrength;
//		 the value should be between 0 and 1
		newValue = newValue < 0 ? -newValue : newValue;
		newValue = newValue > 1 ? 1 : newValue;
		// set the "charge" inside the balls so they
		// have the right color (that is the only
		// reason)
		for (int i = 0; i < balls.length; i++)
			balls[i].setCharge(newValue
					* balls[i].chargePolarity);
	}
	
	/**
	 * This gets called every frame. It updates the balls.
	 */
	protected void updateForEachFrame() {
		if (allset) {
			if(numBalls != balls.length)
				createBalls();
			if(in3D != viewer.window.drawFor3D)
			{
				viewer.window.drawFor3D = in3D;
				createBalls();
			}
			
			
			// calculate the forces for each pair of balls once
			for (int j = 0; j < balls.length; j++)
				for (int i = 1 + j; i < balls.length; i++)
					calculateForce(balls[i], balls[j]);

			// apply the forces to the balls
			getSize(panelSize);
			for (int i = 0; i < balls.length; i++) {
				bounceIfNecessary(balls[i]);
				balls[i].applyMotionVector();

				// apply viscosity
				balls[i].motionVector.times(1 - viscosity,
						balls[i].motionVector);

				// apply gravity
				if(viewer.window.drawFor3D)
					balls[i].motionVector.z -= gravity/1000;
				else
					balls[i].motionVector.y -= gravity/1000;
				

			}

			// draw the balls and do whatever else is necessary
			super.updateForEachFrame();
		}
	}

	/**
	 * Modifies the motion of the specified ball such that it will stay on the
	 * screen.
	 * 
	 * @param ball
	 */
	private void bounceIfNecessary(MovingBall3D ball) {
		if (ball.p.x > 10)
			ball.motionVector.x = -Math.abs(ball.motionVector.x);
		else if (ball.p.x < -10)
			ball.motionVector.x = Math.abs(ball.motionVector.x);
		if (ball.p.y > 10)
			ball.motionVector.y = -Math.abs(ball.motionVector.y);
		else if (ball.p.y < -10)
			ball.motionVector.y = Math.abs(ball.motionVector.y);
		if (ball.p.z > 10)
			ball.motionVector.z = -Math.abs(ball.motionVector.z);
		else if (ball.p.z < -10)
			ball.motionVector.z = Math.abs(ball.motionVector.z);
	}

	/**
	 * Calculates force between the two specified balls, then adds that force to
	 * the motion vector of each ball.
	 * 
	 * @param ballA
	 *            the first of the pair of balls to perform the calculations on.
	 * @param ballB
	 *            the second of the pair of balls to perform the calculations
	 *            on.
	 */
	private void calculateForce(MovingBall3D ballA, MovingBall3D ballB) {

		// calculate the distance between the balls
		double distance = Vector3D.calculateDistance(ballA.p, ballB.p);

		// calculate the unit vector for the force
		ballB.p.minus(ballA.p, tempVector);
		tempVector.times(1 / distance, tempVector);


		double dSquared = Math.pow(Math.max(distance, 0.6),2);
		
		//the attractive forces
		double force=attractiveForceStrength/dSquared;
//		the repulsive forces
		force-=repulsiveForceStrength/(Math.pow(Math.max(distance, 0.4),3));
//		the coulombic forces
		force-=ballA.chargePolarity*ballB.chargePolarity*coulombicForceStrength/dSquared;
		
		force/=1000;
		
		tempVector.times(force, tempVector);

		// apply the force to ballA
		ballA.motionVector.plus(tempVector, ballA.motionVector);

		// apply the force to ballB
		ballB.motionVector.minus(tempVector, ballB.motionVector);
	}
}
/*
 * class DecimalVariable { DecimalValue reUsableValue = new DecimalValue(0);
 * 
 * Variable variable;
 * 
 * public DecimalVariable(String variableName) { variable =
 * Variable.getVariable(variableName); }
 * 
 * public double getValue() { return
 * DecimalValue.extractDoubleValue(variable.evaluate()); }
 * 
 * public void setValue(double value) { reUsableValue.value = value;
 * variable.set(reUsableValue); } }
 */