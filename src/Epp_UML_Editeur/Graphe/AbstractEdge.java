
package Epp_UML_Editeur.Graphe;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.Direction;


abstract class AbstractEdge implements Edge
{  
	private static final int ANGLE_180_DEGREES = 180;
	private static final double DISTANCE_MAX = 3.0;
	private Node appesStart;
	private Node appesEnd;
	

	protected abstract Shape getShape();
	
	@Override
	public Rectangle2D getBounds()
	{
		return getShape().getBounds();
	}

	@Override
	public boolean contains(Point2D appesPoint2d)
	{

		Line2D conn2d = getPointsConnection();
		if(appesPoint2d.distance(conn2d.getP1()) <= DISTANCE_MAX || appesPoint2d.distance(conn2d.getP2()) <= DISTANCE_MAX)
		{
			return false;
		}

		Shape pathfat = new BasicStroke((float)(2 * DISTANCE_MAX)).createStrokedShape(getShape());
		return pathfat.contains(appesPoint2d);
	}
	
	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException exception)
		{
			return null;
		}
	}

	@Override
	public void connect(Node appesStart, Node appesEnd)
	{  
		assert appesStart != null && appesEnd != null;
		this.appesStart = appesStart;
		this.appesEnd = appesEnd;
	}

	@Override
	public Node getNodeStart()
	{
		return appesStart;
	}

	@Override
	public Node getNodeEnd()
	{
		return appesEnd;
	}

	@Override
	public Line2D getPointsConnection()
	{
		Rectangle2D boundsStart = appesStart.getBounds();
		Rectangle2D boundsEnd = appesEnd.getBounds();
		Point2D centerStart = new Point2D.Double(boundsStart.getCenterX(), boundsStart.getCenterY());
		Point2D centerEnd = new Point2D.Double(boundsEnd.getCenterX(), boundsEnd.getCenterY());
		Direction directiontoEnd = new Direction(centerStart, centerEnd);
		return new Line2D.Double(appesStart.getConnectionPoint(directiontoEnd), appesEnd.getConnectionPoint(directiontoEnd.turn(ANGLE_180_DEGREES)));
   }
}
