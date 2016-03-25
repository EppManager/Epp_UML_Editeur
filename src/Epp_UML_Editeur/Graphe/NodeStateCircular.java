

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;

import Epp_UML_Editeur.LeFramework.Direction;


public class NodeStateCircular extends NodeRectangular
{
	private static final int DIAMETER_DEFAULT_APP = 14;
	private static final int GAP_DEFAULT_APP = 3;
	
	private boolean appesFinalState;
	   

	public NodeStateCircular()
	{     
		setBounds(new Rectangle2D.Double(0, 0, DIAMETER_DEFAULT_APP, DIAMETER_DEFAULT_APP));
	}
   

	public boolean isFinal()
	{
		return appesFinalState;
	}
   

	public void setFinal(boolean appesFinalState)
	{
		this.appesFinalState = appesFinalState;
		if(this.appesFinalState)
		{
			setBounds(new Rectangle2D.Double(getBounds().getX(), getBounds().getY(),
		               DIAMETER_DEFAULT_APP + 2 * GAP_DEFAULT_APP, DIAMETER_DEFAULT_APP + 2 * GAP_DEFAULT_APP));
		}
		else
		{
			setBounds(new Rectangle2D.Double(getBounds().getX(), getBounds().getY(),
					DIAMETER_DEFAULT_APP, DIAMETER_DEFAULT_APP));
		}
	}
   
	@Override
	public Point2D getConnectionPoint(Direction appDirections)
	{
		Rectangle2D boundsRect2d = getBounds();
		double a = boundsRect2d.getWidth() / 2;
		double b = boundsRect2d.getHeight() / 2;
		double x = appDirections.getX();
		double y = appDirections.getY();
		double cx = boundsRect2d.getCenterX();
		double cy = boundsRect2d.getCenterY();
      
		if(a != 0 && b != 0 && !(x == 0 && y == 0))
		{
			double t = Math.sqrt((x * x) / (a * a) + (y * y) / (b * b));
			return new Point2D.Double(cx + x / t, cy + y / t);
		}
		else
		{
			return new Point2D.Double(cx, cy);
		}
	}   	 

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		Ellipse2D circle = new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight());
      
      	if(appesFinalState)
      	{
      		Rectangle2D boundsRect2d = getBounds();
      		Ellipse2D inside = new Ellipse2D.Double( boundsRect2d.getX() + GAP_DEFAULT_APP,
      				boundsRect2d.getY() + GAP_DEFAULT_APP, boundsRect2d.getWidth() - 2 * GAP_DEFAULT_APP, boundsRect2d.getHeight() - 2 * GAP_DEFAULT_APP);
      		appesGraphics2D.fill(inside);
      		appesGraphics2D.draw(circle);
      	}
		else
		{
			appesGraphics2D.fill(circle);
		}      
	}
   
	@Override
	public Shape getShape()
	{
		return new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth() - 1, getBounds().getHeight() - 1);
	}
}

