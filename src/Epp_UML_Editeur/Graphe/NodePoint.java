

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.Direction;


public class NodePoint extends AbstractNode
{
	private Point2D appesPoint;


	public NodePoint()
	{
		appesPoint = new Point2D.Double();
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{}

	@Override
	public void AppCommandtranslate(double x, double y)
	{
      appesPoint.setLocation(appesPoint.getX() + x, appesPoint.getY() + y);
	}

	@Override
	public boolean contains(Point2D appPoint)
	{
		final double threshold = 5;
		return appesPoint.distance(appPoint) < threshold;
	}

	@Override
	public Rectangle2D getBounds()
	{
		return new Rectangle2D.Double(appesPoint.getX(), appesPoint.getY(), 0, 0);
	}

	@Override
	public Point2D getConnectionPoint(Direction appDirections)
	{
		return appesPoint;
	}
}
