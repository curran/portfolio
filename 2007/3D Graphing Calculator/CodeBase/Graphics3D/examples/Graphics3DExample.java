package examples;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import primitives3D.Cube3DEdges;
import primitives3D.Vector3D;
import primitives3D.Triangle3D;
import drawing3D.RotatableObject3DViewingPanel;

/**
 * A progrm demonstrating 3D drawing.
 * @author Curran Kelleher
 *
 */
public class Graphics3DExample {

	/**
	 * A progrm demonstrating 3D drawing.
	 * @param args
	 *            command line arguments not used.
	 */
	public static void main(String[] args) {

		RotatableObject3DViewingPanel panel = new RotatableObject3DViewingPanel();
		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
				for (int z = -1; z <= 1; z++)
					if (z != 0)
						panel.viewer.add3DObjects(new Cube3DEdges(new Vector3D(
								x * 5, y * 5, z * 5), 4, Color.green)
								.getLineSegments());

		for (int z = -2; z <= 2; z++)
			panel.viewer.add3DObject(new Triangle3D(new Vector3D(0, 0, z),
					new Vector3D(0, 4, z), new Vector3D(4, 0, z), new Color(
							(int) (Math.random() * 255),
							(int) (Math.random() * 255),
							(int) (Math.random() * 255))));

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setBounds(200, 200, 400, 400);
		frame.setVisible(true);
	}

}
