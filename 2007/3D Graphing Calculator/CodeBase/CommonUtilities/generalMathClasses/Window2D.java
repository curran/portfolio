package generalMathClasses;

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * Class used to translate between pixel-space and coordinate-space.
 * 
 * @author Curran Kelleher
 * 
 */
public class Window2D {
	/**
	 * The height in pixels of the pixel space.
	 */
	public int height;

	/**
	 * The width in pixels of the pixel space.
	 */
	public int width;

	/**
	 * The coordinate space (which specifies a 2D range of values) used by this
	 * Window2D.
	 */
	public CoordinateSpace2D coordinateSpace;

	/**
	 * Constructs a new Window2D with the specified paramaters.
	 * 
	 * @param w
	 *            The width in pixels of the pixel space.
	 * @param h
	 *            The height in pixels of the pixel space.
	 * @param Xm
	 *            The minimum x value in the coordinate space.
	 * @param xM
	 *            The maximum x value in the coordinate space.
	 * @param Ym
	 *            The minimum y value in the coordinate space.
	 * @param yM
	 *            The maximum y value in the coordinate space.
	 */
	public Window2D(int w, int h, double Xm, double xM, double Ym, double yM) {
		height = h;
		width = w;
		coordinateSpace = new CoordinateSpace2D(Xm, xM, Ym, yM);
	}

	/**
	 * Constructs a Window2D with the specified pixel space dimensions. The
	 * coordinate space is set to -10,10,-10,10.
	 * 
	 * @param w
	 *            The width in pixels of the pixel space.
	 * @param h
	 *            The height in pixels of the pixel space.
	 */
	public Window2D(int w, int h) {
		height = h;
		width = w;
		coordinateSpace = new CoordinateSpace2D(-10, 10, -10, 10);
	}

	/**
	 * Constructs a Window2D with all parameter values set to zero.
	 */
	public Window2D() {
		this(0, 0, 0, 0, 0, 0);
	}

	/**
	 * Constructs a Window2D which will always use the reference to the
	 * specified coordinate space. It will always use this reference, so will
	 * automatically reflect any changes made to the coordinate space.
	 * 
	 * The pixel space is set to 0,0.
	 * 
	 * @param coordinateSpace
	 *            the coodrinate space reference which this Window2D will use.
	 */
	public Window2D(CoordinateSpace2D coordinateSpace) {
		this.coordinateSpace = coordinateSpace;
		height = 0;
		width = 0;
	}

	/**
	 * Returns the coordinate space used by this Window2D.
	 * 
	 * @return the coordinate space used by this Window2D.
	 */
	public CoordinateSpace2D getCoordinateSpace() {
		return coordinateSpace;
	}

	/**
	 * Sets the coordinate space
	 * 
	 * @param Xm
	 *            The minimum x value in the coordinate space.
	 * @param xM
	 *            The maximum x value in the coordinate space.
	 * @param Ym
	 *            The minimum y value in the coordinate space.
	 * @param yM
	 *            The maximum y value in the coordinate space.
	 */
	public void set(double Xm, double xM, double Ym, double yM) {
		coordinateSpace.xMin = Xm;
		coordinateSpace.xMax = xM;
		coordinateSpace.yMin = Ym;
		coordinateSpace.yMax = yM;
	}

	/**
	 * Sets the pixel space
	 * 
	 * @param w
	 *            The width in pixels of the pixel space.
	 * @param h
	 *            The height in pixels of the pixel space.
	 */
	public void set(int w, int h) {
		height = h;
		width = w;
	}

	/**
	 * Sets the pixel space
	 * 
	 * @param newPixelSpace
	 *            the new dimensions of the pixel space.
	 */
	public void set(Dimension newPixelSpace) {
		height = newPixelSpace.height;
		width = newPixelSpace.width;
	}

	/**
	 * Sets the coordinate space such that it reflects the subspace of the pixel
	 * space defined by the zoomBox rectancle. It "zooms" into the specified
	 * rectangle.
	 * 
	 * @param zoomBox
	 *            the subspace of the pixel space to use as the new coordinate
	 *            space. The rectangle to "zoom" to.
	 */
	public void zoom(Rectangle zoomBox) {
		double x1 = zoomBox.getX();
		double y1 = zoomBox.getY();
		set(getXvalue(x1), getXvalue(x1 + zoomBox.getWidth()), getYvalue(y1
				+ zoomBox.getHeight()), getYvalue(y1));
	}

	/**
	 * Translates an x value from coordinate space into pixel space.
	 * 
	 * @param xVal
	 *            the value in coordinate space
	 * @return the corresponding value in pixel space
	 */
	public int getXpixel(double xVal) {
		return (int) getXpixelAsDouble(xVal);
	}
	/**
	 * Translates an x value from coordinate space into pixel space.
	 * 
	 * @param xVal
	 *            the value in coordinate space
	 * @return the corresponding value in pixel space, with double precision
	 */
	public double getXpixelAsDouble(double xVal) {
		return  ((xVal - coordinateSpace.xMin) * ((double) (width) / (coordinateSpace.xMax - coordinateSpace.xMin)));
	}

	/**
	 * Translates a y value from coordinate space into pixel space.
	 * 
	 * @param yVal
	 *            the value in coordinate space
	 * @return the corresponding value in pixel space
	 */
	public int getYpixel(double yVal) {
		return (int) getYpixelAsDouble(yVal);
	}

	/**
	 * Translates a y value from coordinate space into pixel space.
	 * 
	 * @param yVal
	 *            the value in coordinate space
	 * @return the corresponding value in pixel space
	 */
	public double getYpixelAsDouble(double yVal) {
		return height
				-  (((yVal - coordinateSpace.yMin) * ((double) (height) / (coordinateSpace.yMax - coordinateSpace.yMin))));

	}

	/**
	 * Translates an x value from pixel space into coordinate space.
	 * 
	 * @param xPix
	 *            the value in pixel space
	 * @return the corresponding value in coordinate space
	 */
	public double getXvalue(double xPix) {
		return (((double) xPix * ((double) (coordinateSpace.xMax - coordinateSpace.xMin) / width)) + coordinateSpace.xMin);
	}

	/**
	 * Translates a y value from pixel space into coordinate space.
	 * 
	 * @param yPix
	 *            the value in pixel space
	 * @return the corresponding value in coordinate space
	 */
	public double getYvalue(double yPix) {
		return (((height - yPix) / (double) height)
				* (coordinateSpace.yMax - coordinateSpace.yMin) + coordinateSpace.yMin);
	}

	/**
	 * Makes the window such that there is no distortion of the coordinate
	 * space.
	 * 
	 */
	public void makeWindowSquare() {
		double oldYrange = coordinateSpace.yMax - coordinateSpace.yMin;
		double oldXrange = coordinateSpace.xMax - coordinateSpace.xMin;
		double newXRange = oldYrange * width / height;
		double halfXDifference = (newXRange - oldXrange) / 2;
		coordinateSpace.xMax += halfXDifference;
		coordinateSpace.xMin -= halfXDifference;
		/*
		 * if (height < width){ double newXRange = oldYrange * width / height;
		 * double halfXDifference = (newXRange - oldXrange) / 2;
		 * coordinateSpace.xMax += halfXDifference; coordinateSpace.xMin -=
		 * halfXDifference; } else { double newYRange = oldXrange / width
		 * *height; double halfYDifference = (newYRange - oldYrange) / 2;
		 * coordinateSpace.yMax += halfYDifference; coordinateSpace.yMin -=
		 * halfYDifference; }
		 */

	}
}
