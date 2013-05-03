package colorMap;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * A panel which displays a ColorMap
 * 
 * @author Curran Kelleher
 * 
 */
public class ColorMapEditorPanel extends JPanel implements MouseListener,
		MouseMotionListener {
	private static final long serialVersionUID = 5598720624549427817L;

	/**
	 * The ColorMap which this panel will display.
	 */
	ColorMap associatedColorMap;

	/**
	 * The editors for the individual color nodes.
	 */
	List<ColorNodeEditor> nodeEditors = new ArrayList<ColorNodeEditor>();

	/**
	 * The editor which is currently "grabbed" by the user.
	 */
	ColorNodeEditor grabbedNodeEditor = null;

	/**
	 * The popup menu which appears when right clicked
	 */
	final ColorNodeEditorPopupMenu popupMenu = new ColorNodeEditorPopupMenu();

	/**
	 * Construct a ColorMapEditorPanel which will display and edit the specified
	 * ColorMap
	 * 
	 * @param associatedColorMap
	 */
	public ColorMapEditorPanel(ColorMap associatedColorMap) {
		setColorMap(associatedColorMap);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * Sets the color map associated with this color map editor
	 * @param associatedColorMap
	 */
	public void setColorMap(ColorMap associatedColorMap) {
		this.associatedColorMap = associatedColorMap;
		rebuildEditorPanels();
	}

	/**
	 * Clears all existing editor panels, and creates new ones which mirror the
	 * nodes of the associatedColorMap.
	 * 
	 */
	private void rebuildEditorPanels() {
		nodeEditors.clear();
		for (Iterator<ColorNode> it = associatedColorMap.colorNodes.iterator(); it
				.hasNext();)
			nodeEditors.add(new ColorNodeEditor(it.next()));
		repaint();
	}

	public void paint(Graphics g) {
		associatedColorMap.paintOnThis(g, 0, 0, getWidth(), getHeight());

		// paint the editors
		for (Iterator<ColorNodeEditor> it = nodeEditors.iterator(); it
				.hasNext();)
			it.next().paintOnThis(g, getWidth(), getHeight());
	}

	@SuppressWarnings("serial")
	private class ColorNodeEditor extends Polygon {
		ColorNode node;

		int xLowerBound = 0, xUpperBound = 0;
		
		int halfEditorWidth = 4;
		int editorHeight = 3;
		int pointHeight = 4;
		int offsetFromTop = 0;

		public ColorNodeEditor(ColorNode node) {
			super(new int[5], new int[5], 5);
			this.node = node;
		}

		public void paintOnThis(Graphics g, int width, int height) {
			//if reset is not called, then contains() will not work
			this.reset();

			int thisNodesXCenter = (int) (node.value * width);
			xLowerBound = thisNodesXCenter - halfEditorWidth;
			xUpperBound = thisNodesXCenter + halfEditorWidth;
			xpoints[0] = xLowerBound;
			xpoints[1] = xUpperBound;
			xpoints[2] = xUpperBound;
			xpoints[3] = thisNodesXCenter;
			xpoints[4] = xLowerBound;

			ypoints[0] = offsetFromTop;
			ypoints[1] = offsetFromTop;
			ypoints[2] = offsetFromTop + editorHeight;
			ypoints[3] = offsetFromTop + editorHeight + pointHeight;
			ypoints[4] = offsetFromTop + editorHeight;

			npoints = 5;

			g.setColor(node.color);
			g.fillPolygon(this);
			g.setColor((double)(node.color.getRed()+node.color.getGreen()+node.color.getBlue())/(255.0*3)>.3?Color.black:Color.white);
			g.drawPolygon(this);
		}
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	/**
	 * Displays a color chooser for a node.
	 * 
	 * @param current
	 */
	private void displayColorChooserForNode(ColorNodeEditor current) {
		if (current == null)
			return;
		Color newColor = JColorChooser.showDialog(this, "Choose Color",
				current.node.color);
		if (newColor != null) {
			current.node.color = newColor;
			associatedColorMap.calculateColors();
			repaint();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		grabbedNodeEditor = getEditorUnderPoint(e.getPoint());
	}

	/**
	 * Figures out if the specified point is over a color node editor.
	 * 
	 * @param p
	 *            the point to check
	 * @return the ColorNodeEditor that the point p is inside, or null if p is
	 *         not inside a ColorNodeEditor
	 */
	private ColorNodeEditor getEditorUnderPoint(Point p) {
		for (Iterator<ColorNodeEditor> it = nodeEditors.iterator(); it
				.hasNext();) {
			ColorNodeEditor current = it.next();
			if (current.contains(p))
				return current;
		}
		return null;
	}

	public void mouseReleased(MouseEvent e) {
		grabbedNodeEditor = null;
		if (e.isPopupTrigger()) 
			popupMenu.showForNodeEditor(getEditorUnderPoint(e.getPoint()), getWidth(), e.getComponent(), e.getX(), e.getY());
		else
			displayColorChooserForNode(getEditorUnderPoint(e.getPoint()));
	}

	public void mouseDragged(MouseEvent e) {
		// if the user is dragging a node editor
		if (grabbedNodeEditor != null) {
			double v = (double) e.getX() / getWidth();
			// move the node editor
			grabbedNodeEditor.node.value = v<0?0:v>1?1:v;
			// recalculate the color map
			associatedColorMap.calculateColors();
			// repaint
			repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
	}

	@SuppressWarnings("serial")
	private class ColorNodeEditorPopupMenu extends JPopupMenu {
		ColorNodeEditor associatedEditor = null;

		JMenuItem createNodeMenuItem, deleteNodeMenuItem, changeColorMenuItem;

		int currentX = 0, currentWidth = 1;

		public ColorNodeEditorPopupMenu() {

			deleteNodeMenuItem = new JMenuItem("Delete Node");
			deleteNodeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (associatedEditor != null) {
						associatedColorMap.colorNodes
								.remove(associatedEditor.node);
						associatedColorMap.calculateColors();
						rebuildEditorPanels();
					}
				}
			});
			
			changeColorMenuItem = new JMenuItem("Change Color");
			changeColorMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (associatedEditor != null)
						displayColorChooserForNode(associatedEditor);
				}
			});

			createNodeMenuItem = new JMenuItem("Create Node");
			createNodeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					double value = (double) currentX / currentWidth;
					associatedColorMap.colorNodes.add(new ColorNode(associatedColorMap.getColorAtValue(value),
							value));
					associatedColorMap.calculateColors();
					rebuildEditorPanels();
				}
			});
		}

		/**
		 * Shows the popup menu
		 * 
		 * @param editorUnderPoint
		 *            the ColorNodeEditor that the menu should interact with, or
		 *            null if the menu should not interact with any node editor
		 */
		public void showForNodeEditor(ColorNodeEditor editorUnderPoint,
				int width, Component invoker, int x, int y) {
			currentX = x;
			currentWidth = width;

			associatedEditor = editorUnderPoint;
			removeAll();
			if (associatedEditor != null)
			{
				add(changeColorMenuItem);
				add(deleteNodeMenuItem);
				
			}
			else
				add(createNodeMenuItem);

			super.show(invoker, x, y);
		}

	}
}
