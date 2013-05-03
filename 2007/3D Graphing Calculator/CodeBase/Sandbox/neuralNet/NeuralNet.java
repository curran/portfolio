package neuralNet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class NeuralNet extends JPanel implements MouseListener {
	List<Neuron> neurons = new ArrayList<Neuron>();

	// the radius of the circles which represent neurons on the screen.
	int circleRadius = 10;

	public NeuralNet() {
		int cols = 5;
		int rows = 5;
		Neuron[][] neuronGrid = new Neuron[cols][rows];
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				neurons.add(neuronGrid[i][j] = new Neuron(new Point2D.Double(
						(double) (i + 1) / (cols + 1), (double) (j + 1)
								/ (rows + 1))));

		for (int i = 0; i < cols - 1; i++)
			for (int j = 0; j < rows; j++)
				for (int n = 0; n < rows; n++)
					neuronGrid[i][j].connectToNeuron(neuronGrid[i + 1][n]);

		addMouseListener(this);
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);

		int height = getHeight();
		int width = getWidth();
		g.fillRect(0, 0, width, height);

		for (Neuron n : neurons) {
			g.setColor(n.state == 1 ? Color.red : Color.white);
			g.fillOval((int) (n.pointOnScreen.x * width) - circleRadius / 2,
					(int) (n.pointOnScreen.y * height) - circleRadius / 2,
					circleRadius, circleRadius);
			for (Neuron l : n.connectedNeurons)
				g.drawLine((int) (n.pointOnScreen.x * width),
						(int) (n.pointOnScreen.y * height),
						(int) (l.pointOnScreen.x * width),
						(int) (l.pointOnScreen.y * height));
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setBounds(200, 200, 400, 400);
		f.add(new NeuralNet());
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
		double x = (double) e.getX() / getWidth();
		double y = (double) e.getY() / getHeight();
		double d = (double) circleRadius / 2 / getWidth();
		for (Neuron n : neurons)
			if (Math.abs(n.pointOnScreen.x - x) < d)
				if (Math.abs(n.pointOnScreen.y - y) < d) {
					n.fire();
					repaint();
					return;
				}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}
}
