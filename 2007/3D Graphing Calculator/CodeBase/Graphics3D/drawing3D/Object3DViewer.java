package drawing3D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ProgressMonitor;

import math3D.Window3D;
import primitives3D.Object3D;

/**
 * Manages a collection of 3D objects for drawing. This class is responsible for
 * Z-Sorting.
 * 
 * @author Curran Kelleher
 * 
 */
public class Object3DViewer {

	/**
	 * The 3D objects to Z-Sort and draw.
	 */
	private Vector<Object3D> objects = new Vector<Object3D>();

	/**
	 * The Window3D to use for calculating rotations.
	 */
	public Window3D window;

	/**
	 * The comparator to use for Z-Sorting.
	 */
	private Comparator<Object3D> depthComparator;

	/**
	 * The color of the background used in depth shading
	 */
	public static Color backgroundColor = Color.black;

	/**
	 * Temporary variables used in calculation of depth shading.
	 */
	static float[] colorComponents = { 0, 0, 0 }, bkgColorComponents = { 0, 0,
			0 };

	/**
	 * Construct an Object3DViewer which will use the default Window3D to
	 * calculate rotations.
	 */
	public Object3DViewer() {
		this(new Window3D(0, 0));
	}

	/**
	 * Construct an Object3DViewer which will use the specified Window3D to
	 * calculate rotations.
	 * 
	 * @param window
	 *            the window to use when calculating rotations.
	 */
	public Object3DViewer(Window3D window) {
		depthComparator = new Comparator<Object3D>() {
			public int compare(Object3D a, Object3D b) {
				return a.centerPoint.z > b.centerPoint.z ? 1 : -1;
			}
		};
		this.window = window;
	}

	/**
	 * Adds an Object3D to the collection of objects to draw.
	 * 
	 * @param o
	 *            the Object3D to add to the collection of objects to draw.
	 */
	public void add3DObject(Object3D o) {
		objects.add(o);
	}

	/**
	 * Adds an array of 3D objects to the collection of objects to draw.
	 * 
	 * @param objects
	 *            the array of 3D objects to add to the collection of objects to
	 *            draw.
	 */
	public void add3DObjects(Object3D[] objects) {
		for (int i = 0; i < objects.length; i++)
			add3DObject(objects[i]);
	}

	/**
	 * Adds a collection of 3D objects to the collection of objects to draw.
	 * 
	 * @param objects
	 *            the collection of 3D objects to add to the collection of
	 *            objects to draw.
	 */
	public void add3DObjects(Collection<Object3D> objects) {
		this.objects.addAll(objects);
	}

	/**
	 * Removes all Object3Ds from the collection of objects to draw.
	 * 
	 */
	public void clear3DObjects() {
		objects.clear();
	}

	/**
	 * Z-Sorts and draws all 3D objects which have been added to the collection
	 * of objects to draw via the add3DObject() method.
	 * 
	 * @param g
	 *            the Graphics on which to draw the 3D objects.
	 */
	public void drawObjectsOnThis(Graphics g) {
		synchronized (objects) {
			// calculate all rotations
			for (Iterator<Object3D> it = objects.iterator(); it.hasNext();) {
				Object3D currentObject = it.next();
				// very rarely, when the "objects" list is being modified by
				// another thread, "currentObject" is null here
				if (currentObject != null)
					currentObject.calculateRotation(window);
				else
					// if we are here, then that strange thread error has
					// occurred, and it causes an exception to be thrown
					// when
					// Collections.sort() is called later, so just
					// return now to prevent further errors
					return;

			}

			// Z-Sort
			Collections.sort(objects, depthComparator);

			// Draw all objects
			Graphics2D g2D = (Graphics2D) g;
			for (Iterator<Object3D> it = objects.iterator(); it.hasNext();)
				it.next().drawOnThis(g2D);
		}
	}

	/**
	 * Z-Sorts and draws all 3D objects which have been added to the collection
	 * of objects to draw via the add3DObject() method. The progress of the
	 * drawing is instrumented with the specified progress monitor.
	 * 
	 * @param g
	 *            the Graphics on which to draw the 3D objects.
	 * @param progressMonitor
	 *            the progress monitor which will be updated with the progress
	 *            of drawing the objects. If the progress dialog is cancelled,
	 *            then the drawing stops.
	 */
	public void drawObjectsOnThis(Graphics g, ProgressMonitor progressMonitor) {
		synchronized (objects) {
			progressMonitor.setProgress(1);
			progressMonitor.setNote("Calculating rotations...");
			for (Iterator<Object3D> it = objects.iterator(); it.hasNext();) {
				Object3D currentObject = it.next();
				// check for cancellation
				if (progressMonitor.isCanceled())
					return;

				// very rarely, when the "objects" list is being modified by
				// another thread, "currentObject" here is null
				if (currentObject != null)
					currentObject.calculateRotation(window);
				else
					// if we are here, then that strange thread error has
					// occurred, and it causes an exception to be thrown when
					// Collections.sort() is called later, so just
					// return now to prevent further errors
					return;
			}

			progressMonitor.setNote("Z-Sorting...");
			// Z-Sort
			Collections.sort(objects, depthComparator);

			// Draw all objects
			Graphics2D g2D = (Graphics2D) g;
			double numberOfObjects = (double) objects.size();
			double currentIndex = 1;

			progressMonitor.setNote("Drawing objects...");
			int maxProgress = progressMonitor.getMaximum();
			for (Iterator<Object3D> it = objects.iterator(); it.hasNext();) {
				// check for cancellation
				if (progressMonitor.isCanceled())
					return;
				else {
					progressMonitor.setProgress((int) (currentIndex++
							/ numberOfObjects * maxProgress));
					it.next().drawOnThis(g2D);
				}
			}
		}
	}

	/**
	 * Shades a color; darker with distance.
	 * 
	 * @param originalColor
	 *            the original color
	 * @param z
	 *            the z position of the color
	 * @return the shaded color
	 */
	public static Color shadeColor(Color originalColor, double z) {
		originalColor.getColorComponents(colorComponents);
		backgroundColor.getColorComponents(bkgColorComponents);

		double percentOriginal = (z / 20 + 0.8);
		double percentBackground = 1 - percentOriginal;
		double x;
		for (int i = 0; i < 3; i++) {
			x = colorComponents[i] * percentOriginal + bkgColorComponents[i]
					* percentBackground;
			colorComponents[i] = (float) (x > 1 ? 1 : x < 0 ? 0 : x);
		}
		return new Color(colorComponents[0], colorComponents[1],
				colorComponents[2]);
	}

	/**
	 * Figures out the object which, when drawn on the screen, the specified
	 * point is "inside"
	 * 
	 * @param point
	 *            the point to look for objects under
	 * @return the object which, when drawn on the screen, the specified point
	 *         is "inside", or null if the point is not over an object.
	 */
	public Object3D getObjectWhichContainsPoint(Point point) {
		
		for (Iterator<Object3D> it = objects.iterator(); it.hasNext();) {
			Object3D currentObject = it.next();
			if(currentObject.contains(point))
				return currentObject;
		}
		
		return null;
	}
}
