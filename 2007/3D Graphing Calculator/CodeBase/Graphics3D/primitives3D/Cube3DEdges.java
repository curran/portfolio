package primitives3D;

import java.awt.Color;

/**
 * A class which genetates line segments arranges as the edges of a cube.
 * @author Curran Kelleher
 *
 */
public class Cube3DEdges {
	LineSegment3D[] edges = new LineSegment3D[12];
	public Cube3DEdges(Vector3D center, double edgeLength, Color color)
	{
		double e = edgeLength/2;
		Vector3D[] corners = new Vector3D[8];
		corners[0] = new Vector3D(center.plus(new Vector3D(-e, e, e)));
		corners[1] = new Vector3D(center.plus(new Vector3D( e, e, e)));
		corners[2] = new Vector3D(center.plus(new Vector3D(-e,-e, e)));
		corners[3] = new Vector3D(center.plus(new Vector3D( e, e,-e)));
		corners[4] = new Vector3D(center.plus(new Vector3D(-e, e,-e)));
		corners[5] = new Vector3D(center.plus(new Vector3D(-e,-e,-e)));
		corners[6] = new Vector3D(center.plus(new Vector3D( e,-e,-e)));
		corners[7] = new Vector3D(center.plus(new Vector3D( e,-e, e)));
		
		edges[0] = new LineSegment3D(corners[0],corners[4],color);
		edges[1] = new LineSegment3D(corners[0],corners[2],color);
		edges[2] = new LineSegment3D(corners[0],corners[1],color);
		edges[3] = new LineSegment3D(corners[1],corners[3],color);
		edges[4] = new LineSegment3D(corners[5],corners[4],color);
		edges[5] = new LineSegment3D(corners[3],corners[4],color);
		edges[6] = new LineSegment3D(corners[2],corners[5],color);
		edges[7] = new LineSegment3D(corners[5],corners[6],color);
		edges[8] = new LineSegment3D(corners[2],corners[7],color);
		edges[9] = new LineSegment3D(corners[3],corners[6],color);
		edges[10]= new LineSegment3D(corners[1],corners[7],color);
		edges[11]= new LineSegment3D(corners[7],corners[6],color);
	}

	/**
	 * @return the line segments which make up the edges of this cube.
	 */
	public Object3D[] getLineSegments() {
		return edges;
	}
}
