
package Epp_UML_Editeur.Graphe;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.Direction;


public class EdgeNote extends AbstractEdge
{
	private static final int ANGLE_180_DEGREES = 180;
	private static final Stroke DOTTED_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, 
													  BasicStroke.JOIN_ROUND, 0.0f, new float[] { 3.0f, 3.0f }, 0.0f);
	
	@Override
	public void draw(Graphics2D appesGraphics2D)
	{
		Stroke oldStroke = appesGraphics2D.getStroke();
		appesGraphics2D.setStroke(DOTTED_STROKE);
		appesGraphics2D.draw(getShape());
		appesGraphics2D.setStroke(oldStroke);
	}

	@Override
	public Line2D getPointsConnection()
	{
		Rectangle2D startRect2d = getNodeStart().getBounds();
		Rectangle2D endRect2d = getNodeEnd().getBounds();
		Direction direction = new Direction(endRect2d.getCenterX() - startRect2d.getCenterX(), endRect2d.getCenterY() - startRect2d.getCenterY());
		return new Line2D.Double(getNodeStart().getConnectionPoint(direction), getNodeEnd().getConnectionPoint(direction.turn(ANGLE_180_DEGREES)));
  	}

	@Override
	protected Shape getShape()
	{
		GeneralPath path = new GeneralPath();
		Line2D conn = getPointsConnection();
		path.moveTo((float)conn.getX1(), (float)conn.getY1());
		path.lineTo((float)conn.getX2(), (float)conn.getY2());
		return path;
	}
}
