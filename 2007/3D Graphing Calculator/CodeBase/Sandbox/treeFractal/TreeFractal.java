package treeFractal;

import evaluationTree.EvaluationTreeGUI;
import graphicsUtilities.GeneralGraphicsUtilities;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class TreeFractal extends JPanel{
	public void paint(Graphics g)
	{
		g.setColor(Color.green);
		g.drawLine(0, 0, 200, 200);
	}
	
	public TreeFractal() {
		GeneralGraphicsUtilities.setNativeLookAndFeel();
		JFrame frame = new JFrame();
		GeneralGraphicsUtilities.centerFrame(frame, 500, 500);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
	}
	
	public static void main(String[] args)
	{
		(new TreeFractal()).setVisible(true);
	}
}
