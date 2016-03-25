

package Epp_UML_Editeur.Graphe;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.Direction;


public class EdgeTransitionState extends AbstractEdge
{
	private static final int APP_DEGREES_5 = 5;
	private static final int APP_DEGREES_10 = 10;
	private static final int APP_DEGREES_30 = 30;
	private static final int APP_DEGREES_60 = 60;
	private static JLabel label = new JLabel();
	private double appesAngle;
	private String appesLabelText = "";
	   

	public void setLabel(String appesNouvValue)
	{
		appesLabelText = appesNouvValue;
	}


	public String getLabel()
	{
		return appesLabelText;
	}

	@Override
	public void draw(Graphics2D appesGraphics2D)
	{
		appesGraphics2D.draw(getShape());
		drawLabel(appesGraphics2D);
		ArHead.V.draw(appesGraphics2D, getControlPoint(), getPointsConnection().getP2());
	}


	private void drawLabel(Graphics2D appesGraphics2D)
	{
		Rectangle2D labelBounds = getLabelBounds();
		double x = labelBounds.getX();
		double y = labelBounds.getY();
		appesGraphics2D.translate(x, y);
		label.paint(appesGraphics2D);
		appesGraphics2D.translate(-x, -y);
	}


	private Rectangle2D getLabelBounds()
	{
		Line2D line = getPointsConnection();
		Point2D point2dcontrol = getControlPoint();
		double x = point2dcontrol.getX() / 2 + line.getX1() / 4 + line.getX2() / 4;
		double y = point2dcontrol.getY() / 2 + line.getY1() / 4 + line.getY2() / 4;

		label.setText("<html>" + appesLabelText + "</html>");
		Dimension dim = label.getPreferredSize();
		label.setBounds(0, 0, dim.width, dim.height);
   
		final int gp = 3;
		if (line.getY1() == line.getY2())
		{
			x -= dim.getWidth() / 2;
		}
		else if (line.getY1() <= line.getY2())
		{
			x += gp;
		}
		else
		{
			x -= dim.getWidth() + gp;
		}
		if (line.getX1() == line.getX2())
		{
			y += dim.getHeight() / 2;
		}
		else if (line.getX1() <= line.getX2())
		{
			y -= dim.getHeight() + gp;
		}
		else
		{
			y += gp;
		}
		return new Rectangle2D.Double(x, y, dim.width, dim.height);
   }   


	private Point2D getControlPoint()
	{
		Line2D line = getPointsConnection();
		double t = Math.tan(Math.toRadians(appesAngle));
		double dx = (line.getX2() - line.getX1()) / 2;
		double dy = (line.getY2() - line.getY1()) / 2;
		return new Point2D.Double((line.getX1() + line.getX2()) / 2 + t * dy, (line.getY1() + line.getY2()) / 2 - t * dx);         
	}
   
	@Override
	protected Shape getShape()
	{
		Line2D ligne = getPointsConnection();
		Point2D point2dcontrol = getControlPoint();
		GeneralPath path = new GeneralPath();
		path.moveTo((float) ligne.getX1(), (float) ligne.getY1());
		path.quadTo((float) point2dcontrol.getX(), (float) point2dcontrol.getY(), (float) ligne.getX2(), (float) ligne.getY2());
		return path;
	}

	@Override
	public Rectangle2D getBounds()
	{
		Rectangle2D h = super.getBounds();
		h.add(getLabelBounds());
		return h;
	}
   
	@Override
	public Line2D getPointsConnection()
	{
		Direction dirc1;
		Direction dirc2;

		if(getNodeStart() == getNodeEnd())
		{
			appesAngle = APP_DEGREES_60;
			dirc1 = Direction.EAST.turn(-APP_DEGREES_30);
			dirc2 = Direction.EAST.turn(APP_DEGREES_30);
		}
		else
		{
			appesAngle = APP_DEGREES_10;
			Rectangle2D startRect2d = getNodeStart().getBounds();
			Rectangle2D endRect2d = getNodeEnd().getBounds();
			Point2D startPoint2dCenter = new Point2D.Double(startRect2d.getCenterX(), startRect2d.getCenterY());
			Point2D endPoint2dCenter = new Point2D.Double(endRect2d.getCenterX(), endRect2d.getCenterY());
			dirc1 = new Direction(startPoint2dCenter, endPoint2dCenter).turn(-APP_DEGREES_5);
			dirc2 = new Direction(endPoint2dCenter, startPoint2dCenter).turn(APP_DEGREES_5);
		}
		return new Line2D.Double(getNodeStart().getConnectionPoint(dirc1), getNodeEnd().getConnectionPoint(dirc2));
	}
}
