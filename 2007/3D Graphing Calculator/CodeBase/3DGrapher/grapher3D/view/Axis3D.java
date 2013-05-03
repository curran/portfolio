package grapher3D.view;

import primitives3D.Object3D;
import primitives3D.SolidRod3D;
import primitives3D.Vector3D;
import primitives3D.textObject3D;
import valueTypes.ColorValue;

/**
 * Specifies a 3D axis
 * 
 * @author Curran Kelleher
 * 
 */
public class Axis3D {
	public int axisResolution = 30;

	Vector3D pointA, pointB;

	String axisName;

	ColorValue color;

	public Axis3D(Vector3D pointA, Vector3D pointB, String axisName,
			ColorValue color) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.axisName = axisName;
		this.color = color;
	}

	public Object3D[] generateAxisObjects() {
		// initialize the Z axis
		Vector3D[] axisPoints = new Vector3D[axisResolution + 2];
		double percent;
		for (int i = 0; i < axisResolution + 2; i++) {
			// be not mystified, the axisResolution + 1 case is used as the
			// location of the text label
			percent = i == axisResolution + 1 ? 1.05 : (double) i
					/ axisResolution;
			axisPoints[i] = new Vector3D(percent * (pointB.x - pointA.x)
					+ pointA.x, percent * (pointB.y - pointA.y) + pointA.y,
					percent * (pointB.z - pointA.z) + pointA.z);
		}
		Object3D[] axisObjects = new Object3D[axisResolution + 1];
		for (int i = 0; i < axisResolution; i++)
			// axisObjects[i]= new
			// LineSegment3D(axisPoints[i],axisPoints[i+1],color.value);
			axisObjects[i] = new SolidRod3D(axisPoints[i], axisPoints[i + 1],
					0.04, color.value);

		axisObjects[axisResolution] = new textObject3D(axisName,
				axisPoints[axisResolution + 1], color.value);
		return axisObjects;
	}

}
