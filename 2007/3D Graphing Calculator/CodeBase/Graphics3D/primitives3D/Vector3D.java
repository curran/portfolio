package primitives3D;

/**
 * A 3 dimensional Vector.
 * 
 * @author Curran Kelleher
 * 
 */
public class Vector3D {
	/**
	 * The x component of this vector.
	 */
	public double x;

	/**
	 * The y component of this vector.
	 */
	public double y;

	/**
	 * The z component of this vector.
	 */
	public double z;

	/**
	 * Temporary variables used in calculating rotation.
	 */
	private static double sint, cost, newX, newY, newZ;

	/**
	 * Construct a 3D point with the specified components
	 * 
	 * @param x
	 *            the x component
	 * @param y
	 *            the y component
	 * @param z
	 *            the z component
	 */
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Construct a 3D vector with the components of the specified vector.
	 * 
	 * @param vector
	 *            the vector to copy the components from.
	 */
	public Vector3D(Vector3D vector) {
		this(vector.x, vector.y, vector.z);
	}

	/**
	 * Constructs a Vector3D with the values of 0,0,0.
	 * 
	 */
	public Vector3D() {
		this(0, 0, 0);
	}

	/**
	 * Calculates the rotation of this vector about the X axis, then puts the
	 * resulting coordinates into the vectorToPutResultsIn. Calling this method
	 * never changes the vector it is called in.
	 * 
	 * @param theta
	 *            the angle specifying the how much to rotate
	 * @param vectorToPutResultsIn
	 *            the vector which gets the coordinates resulting from the
	 *            rotation calculation.
	 */
	public void rotateVectorAboutXAxis(double theta,
			Vector3D vectorToPutResultsIn) {
		sint = Math.sin(theta);
		cost = Math.cos(theta);

		newX = x;
		newY = y * cost - z * sint;
		newZ = y * sint + z * cost;
		// do calculations first, then apply. otherwise, if vectorToPutResultsIn
		// is also the invoking instance, it
		// causes problems
		vectorToPutResultsIn.x = newX;
		vectorToPutResultsIn.y = newY;
		vectorToPutResultsIn.z = newZ;
	}

	/**
	 * Calculates the rotation of this vector about the Y axis, then puts the
	 * resulting coordinates into the vectorToPutResultsIn. Calling this method
	 * never changes the vector it is called in.
	 * 
	 * @param theta
	 *            the angle specifying the how much to rotate
	 * @param vectorToPutResultsIn
	 *            the vector which gets the coordinates resulting from the
	 *            rotation calculation.
	 */
	public void rotateVectorAboutYAxis(double theta,
			Vector3D vectorToPutResultsIn) {
		sint = Math.sin(theta);
		cost = Math.cos(theta);

		newX = z * sint + x * cost;
		newY = y;
		newZ = z * cost - x * sint;
		// do calculations first, then apply. otherwise, if vectorToPutResultsIn
		// is also the invoking instance, it
		// causes problems
		vectorToPutResultsIn.x = newX;
		vectorToPutResultsIn.y = newY;
		vectorToPutResultsIn.z = newZ;
	}

	/**
	 * Calculates the rotation of this vector about the Z axis, then puts the
	 * resulting coordinates into the vectorToPutResultsIn. Calling this method
	 * never changes the vector it is called in.
	 * 
	 * @param theta
	 *            the angle specifying the how much to rotate
	 * @param vectorToPutResultsIn
	 *            the vector which gets the coordinates resulting from the
	 *            rotation calculation.
	 */
	public void rotateVectorAboutZAxis(double theta,
			Vector3D vectorToPutResultsIn) {
		sint = Math.sin(theta);
		cost = Math.cos(theta);

		newX = x * cost - y * sint;
		newY = x * sint + y * cost;
		newZ = z;
		// do calculations first, then apply. otherwise, if vectorToPutResultsIn
		// is also the invoking instance, it
		// causes problems

		vectorToPutResultsIn.x = newX;
		vectorToPutResultsIn.y = newY;
		vectorToPutResultsIn.z = newZ;
	}

	/**
	 * Adds the specified vector to this one and returns the result in a new
	 * Vector.
	 * 
	 * @param b
	 *            the Vector to subtract from this one
	 */
	public Vector3D plus(Vector3D b) {
		Vector3D results = new Vector3D(0, 0, 0);
		this.plus(b, results);
		return results;
	}

	/**
	 * Adds the specified vector to this one and returns the result in the
	 * specified vectorToPutResultsIn
	 * 
	 * @param b
	 *            the Vector to add to this one
	 * @param vectorToPutResultsIn
	 *            the Vector to put the results in
	 */
	public void plus(Vector3D b, Vector3D vectorToPutResultsIn) {
		vectorToPutResultsIn.x = x + b.x;
		vectorToPutResultsIn.y = y + b.y;
		vectorToPutResultsIn.z = z + b.z;
	}

	/**
	 * Subtracts the specified vector from this one and returns the result in a
	 * new Vector.
	 * 
	 * @param b
	 *            the Vector to subtract from this one
	 * @return a new Vector with the result of the addition inside it.
	 */
	public Vector3D minus(Vector3D b) {
		Vector3D results = new Vector3D(0, 0, 0);
		this.minus(b, results);
		return results;
	}

	/**
	 * Subtracts the specified vector from this one and returns the result in
	 * the specified vectorToPutResultsIn
	 * 
	 * @param b
	 *            the Vector to subtract from this one
	 * @param vectorToPutResultsIn
	 *            the Vector to put the results in
	 */
	public void minus(Vector3D b, Vector3D vectorToPutResultsIn) {
		vectorToPutResultsIn.x = x - b.x;
		vectorToPutResultsIn.y = y - b.y;
		vectorToPutResultsIn.z = z - b.z;
	}

	/**
	 * Returns the cross product of the current vector (a) and the specified
	 * vector (b). it returns aXb. The vector resulting from the cross product
	 * has a direction which is the normal of a and b. The magnitude of the
	 * cross product is the area of the parallelogram traced by the two vectors
	 * a and b.
	 */

	/**
	 * Calculates the cross-product of this vector with the specified one.
	 * 
	 * @param b
	 *            the Vector to cross with this one
	 * @return a new Vector with the result of the cross product inside it.
	 */
	public Vector3D cross(Vector3D b) {
		return cross(new Vector3D());
	}

	/**
	 * Calculates the cross-product of this vector with the specified one. The
	 * result is put into the specified vectorToPutResultsIn.
	 * 
	 * @param b
	 *            the Vector to cross with this one *
	 * @param vectorToPutResultsIn
	 *            the Vector to put the results in
	 */
	public void cross(Vector3D b, Vector3D vectorToPutResultsIn) {
		vectorToPutResultsIn.x = y * b.z - z * b.y;
		vectorToPutResultsIn.y = z * b.x - x * b.z;
		vectorToPutResultsIn.z = x * b.y - y * b.x;
	}

	/**
	 * Performs a scalar multiplication on this vector and returns the result in
	 * a new Vector.
	 * 
	 * @param scalarFactor
	 *            the scalar factor to multiply each of the vector's components
	 *            by
	 * @return a new Vector with the result of the scalar multiplication inside
	 *         it.
	 */
	public Vector3D times(double scalarFactor) {
		Vector3D results = new Vector3D();
		this.times(scalarFactor, results);
		return results;
	}

	/**
	 * Performs a scalar multiplication on this vector and puts the result in a
	 * the specified vectorToPutResultsIn.
	 * 
	 * @param scalarFactor
	 *            the scalar factor to multiply each of the vector's components
	 *            by
	 */
	public void times(double scalarFactor, Vector3D vectorToPutResultsIn) {
		vectorToPutResultsIn.x = x * scalarFactor;
		vectorToPutResultsIn.y = y * scalarFactor;
		vectorToPutResultsIn.z = z * scalarFactor;
	}

	/**
	 * Calculates the normal from the specified 3 points. The cross product of
	 * (a-b) and (c-b) is put into the vectorToPutNormalInto vector.
	 * 
	 * @param a
	 *            Vector a
	 * @param b
	 *            Vector b, the vertex of the normal
	 * @param c
	 *            Vector c
	 * @param vectorToPutNormalInto
	 *            The vector which the normal calculation will be put into. This
	 *            vector will be changed.
	 */
	public static void calculateNormal(Vector3D a, Vector3D b, Vector3D c,
			Vector3D vectorToPutNormalInto) {
		// B-b-C
		// |
		// a
		// |
		// A
		// a = A-B
		// b = C-B
		// normal = aXb
		double Ax = a.x - b.x;
		double Ay = a.y - b.y;
		double Az = a.z - b.z;

		double Bx = c.x - b.x;
		double By = c.y - b.y;
		double Bz = c.z - b.z;

		vectorToPutNormalInto.x = Ay * Bz - Az * By;
		vectorToPutNormalInto.y = Az * Bx - Ax * Bz;
		vectorToPutNormalInto.z = Ax * By - Ay * Bx;
	}

	/**
	 * Calculates the distance between the two specified vectors.
	 * 
	 * @param a
	 * @param b
	 * @return the distance between vectors a and b
	 */
	public static double calculateDistance(Vector3D a, Vector3D b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2)
				+ Math.pow(a.z - b.z, 2));
	}

	/**
	 * Sets the values of this vector to the values from the specified vector.
	 * 
	 * @param vectorToCopy
	 *            the vector whose values will be copied into this one.
	 */
	public void set(Vector3D vectorToCopy) {
		x = vectorToCopy.x;
		y = vectorToCopy.y;
		z = vectorToCopy.z;
	}
}
