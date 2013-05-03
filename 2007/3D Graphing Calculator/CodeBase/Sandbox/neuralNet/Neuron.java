package neuralNet;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Neuron {
	/**
	 * x and y range from 0 to 1
	 */
	public Point2D.Double pointOnScreen;

	/**
	 * The list of Neurons to which this one is connected
	 */
	public List<Neuron> connectedNeurons = new ArrayList<Neuron>();
	
	/**
	 * The state of this neuron.
	 */
	public int state = 0;

	public Neuron(Point2D.Double pointOnScreen) {
		this.pointOnScreen = pointOnScreen;
	}

	/**
	 * Adds the specified neuron to this neuron's list of neurons which it is
	 * connected to.
	 * 
	 * @param neuron
	 */
	public void connectToNeuron(Neuron neuron) {
		connectedNeurons.add(neuron);
	}

	/**
	 * Causes this neuron to fire.
	 *
	 */
	public void fire() {
		state = 1;
	}

}
