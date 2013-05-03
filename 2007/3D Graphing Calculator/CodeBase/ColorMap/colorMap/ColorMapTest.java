package colorMap;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ColorMapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setBounds(200, 200, 400, 70);
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
		ColorNode[] colorNodes = { new ColorNode(Color.blue, 0),
				new ColorNode(Color.green, 0.3),new ColorNode(Color.yellow, 1) };
		f.add(new ColorMapEditorPanel(new ColorMap(colorNodes)));
		
		f.setVisible(true);
		
	}

}
