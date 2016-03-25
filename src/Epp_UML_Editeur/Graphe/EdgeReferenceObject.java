
package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.Direction;


public class EdgeReferenceObject extends AbstractEdge
{
	private static final int ENDSIZE = 10;

	@Override
	public void draw(Graphics2D appesGraphics2D)
	{
		appesGraphics2D.draw(getShape());
		Line2D line = getPointsConnection();
		double x1;
		double x2 = line.getX2();
		double y = line.getY2();
		if (isSShaped())
		{
			x1 = x2 - ENDSIZE;
		}
		else
		{
			x1 = x2 + ENDSIZE;
		}
		ArHead.ARROWHEAD_BLACK_TRIANGLE.draw(appesGraphics2D, new Point2D.Double(x1, y), new Point2D.Double(x2, y));
	}

	@Override
	protected Shape getShape()
	{
		Line2D line = getPointsConnection();

		double y1 = line.getY1();
		double y2 = line.getY2();
		double xmid = (line.getX1() + line.getX2()) / 2;
		double ymid = (line.getY1() + line.getY2()) / 2;
		GeneralPath path = new GeneralPath();
		if (isSShaped())
		{
			double x1 = line.getX1() + ENDSIZE;
			double x2 = line.getX2() - ENDSIZE;
         
			path.moveTo((float)line.getX1(), (float)y1);
			path.lineTo((float)x1, (float)y1);
			path.quadTo((float)((x1 + xmid) / 2), (float)y1, (float)xmid, (float)ymid);
			path.quadTo((float)((x2 + xmid) / 2), (float)y2, (float)x2, (float)y2);
			path.lineTo((float)line.getX2(), (float)y2);
		}
		else
		{
			double x1 = Math.max(line.getX1(), line.getX2()) + ENDSIZE;
			double x2 = x1 + ENDSIZE;
			path.moveTo((float)line.getX1(), (float)y1);
			path.lineTo((float)x1, (float)y1);
			path.quadTo((float)x2, (float)y1, (float)x2, (float)ymid);
			path.quadTo((float)x2, (float)y2, (float)x1, (float)y2);
			path.lineTo((float)line.getX2(), (float)y2);
		}
		return path;
	}

	@Override
	public Line2D getPointsConnection()
	{
		Point2D p = getNodeStart().getConnectionPoint(Direction.EAST);
		if (isSShaped())
		{
			return new Line2D.Double(p, getNodeEnd().getConnectionPoint(Direction.WEST));
		}
		else
		{
			return new Line2D.Double(p, getNodeEnd().getConnectionPoint(Direction.EAST));
		}
   }


	private boolean isSShaped()
	{
		Rectangle2D b = getNodeEnd().getBounds();
		Point2D p = getNodeStart().getConnectionPoint(Direction.EAST);
		return b.getX() >= p.getX() + 2 * ENDSIZE;
	}
}
