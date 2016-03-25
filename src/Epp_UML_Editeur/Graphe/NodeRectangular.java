

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.Direction;
import Epp_UML_Editeur.LeFramework.Grid;


public abstract class NodeRectangular extends AbstractNode
{
	private Rectangle2D appesBoundsRect2d;

	@Override
	public NodeRectangular clone()
	{
		NodeRectangular cloned = (NodeRectangular) super.clone();
		cloned.appesBoundsRect2d = (Rectangle2D) appesBoundsRect2d.clone();
		return cloned;
	}

	@Override
	public void AppCommandtranslate(double pDX, double pDY)
	{
      appesBoundsRect2d.setFrame(appesBoundsRect2d.getX() + pDX, appesBoundsRect2d.getY() + pDY, appesBoundsRect2d.getWidth(), appesBoundsRect2d.getHeight());
      super.AppCommandtranslate(pDX, pDY);
	}

	@Override
	public boolean contains(Point2D appPoint)
	{
		return appesBoundsRect2d.contains(appPoint);
	}

	@Override
	public Rectangle2D getBounds()
	{
		return (Rectangle2D) appesBoundsRect2d.clone();
	}


	public void setBounds(Rectangle2D appesNouvBounds)
	{
		appesBoundsRect2d = appesNouvBounds;
	}

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		appLagrille.snap(appesBoundsRect2d);
	}

	@Override
	public Point2D getConnectionPoint(Direction appDirections)
	{
		double slope = appesBoundsRect2d.getHeight() / appesBoundsRect2d.getWidth();
		double ex = appDirections.getX();
		double ey = appDirections.getY();
		double x = appesBoundsRect2d.getCenterX();
		double y = appesBoundsRect2d.getCenterY();
      
		if(ex != 0 && -slope <= ey / ex && ey / ex <= slope)
		{  

			if(ex > 0) 
			{
				x = appesBoundsRect2d.getMaxX();
				y += (appesBoundsRect2d.getWidth() / 2) * ey / ex;
			}
			else
			{
				x = appesBoundsRect2d.getX();
				y -= (appesBoundsRect2d.getWidth() / 2) * ey / ex;
			}
		}
		else if(ey != 0)
		{  

			if(ey > 0) 
			{
				x += (appesBoundsRect2d.getHeight() / 2) * ex / ey;
				y = appesBoundsRect2d.getMaxY();
			}
			else
			{
				x -= (appesBoundsRect2d.getHeight() / 2) * ex / ey;
				y = appesBoundsRect2d.getY();
			}
		}
		return new Point2D.Double(x, y);
	}

	@Override
	public Shape getShape()
	{
		return appesBoundsRect2d;
	}
}
