
package Epp_UML_Editeur.Graphe;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleLine;


public abstract class EdgeSegmentedLabeled extends AbstractEdge
{
	private static JLabel label = new JLabel();


	public EdgeSegmentedLabeled() {}
	

	protected StyleLine obtainStyleLine()
	{
		return StyleLine.SOLIDSTYLE;
	}
	

	protected ArHead startArrowHead()
	{
		return ArHead.ARROWHEAD_NONE;
	}
	

	protected ArHead endArrowHead()
	{
		return ArHead.ARROWHEAD_NONE;
	}
	

	protected String obtainLabelatStart()
	{
		return "";
	}
	

	protected String obtainLabelatMiddle()
	{
		return "";
	}
	

	protected String obtainLabelatEnd()
	{
		return "";
	}

	@Override
	public void draw(Graphics2D appesGraphics2D)
	{
		Point2D[] points = getPoints();

		Stroke oldStroke = appesGraphics2D.getStroke();
		appesGraphics2D.setStroke(obtainStyleLine().getStroke());
		appesGraphics2D.draw(getSegmentPath());
		appesGraphics2D.setStroke(oldStroke);
		startArrowHead().draw(appesGraphics2D, points[1], points[0]);
		endArrowHead().draw(appesGraphics2D, points[points.length - 2], points[points.length - 1]);

		drawString(appesGraphics2D, points[1], points[0], startArrowHead(), obtainLabelatStart(), false);
		drawString(appesGraphics2D, points[points.length / 2 - 1], points[points.length / 2], null, obtainLabelatMiddle(), true);
		drawString(appesGraphics2D, points[points.length - 2], points[points.length - 1], endArrowHead(), obtainLabelatEnd(), false);
	}


	private static void drawString(Graphics2D appesGraphics2D, Point2D appesEndPoint1, Point2D appesEndPoint2,
			ArHead pArrowHead, String appesString, boolean appesCenter)
	{
		if (appesString == null || appesString.length() == 0)
		{
			return;
		}
		label.setText("<html>" + appesString + "</html>");
		label.setFont(appesGraphics2D.getFont());
		Dimension dimensions = label.getPreferredSize();      
		label.setBounds(0, 0, dimensions.width, dimensions.height);

		Rectangle2D boundsRecto2d = getStringBounds(appesEndPoint1, appesEndPoint2, pArrowHead, appesString, appesCenter);

		appesGraphics2D.translate(boundsRecto2d.getX(), boundsRecto2d.getY());
		label.paint(appesGraphics2D);
		appesGraphics2D.translate(-boundsRecto2d.getX(), -boundsRecto2d.getY());
	}


	private static Point2D getAttachmentPoint(Point2D appesEndPoint1, Point2D appesEndPoint2,
			ArHead appesArrow, Dimension appesDimension, boolean appesCenter)
	{    
		final int gp = 3;
		double xoff = gp;
		double yoff = -gp - appesDimension.getHeight();
		Point2D attach = appesEndPoint2;
		if (appesCenter)
		{
			if (appesEndPoint1.getX() > appesEndPoint2.getX())
			{ 
				return getAttachmentPoint(appesEndPoint2, appesEndPoint1, appesArrow, appesDimension, appesCenter);
			}
			attach = new Point2D.Double((appesEndPoint1.getX() + appesEndPoint2.getX()) / 2,
					(appesEndPoint1.getY() + appesEndPoint2.getY()) / 2);
			if (appesEndPoint1.getY() < appesEndPoint2.getY())
			{
				yoff =  -gp-appesDimension.getHeight();
			}
			else if (appesEndPoint1.getY() == appesEndPoint2.getY())
			{
				xoff = -appesDimension.getWidth() / 2;
			}
			else
			{
				yoff = gp;
			}	
		}
		else 
		{
			if(appesEndPoint1.getX() < appesEndPoint2.getX())
			{
				xoff = -gp - appesDimension.getWidth();
			}
			if(appesEndPoint1.getY() > appesEndPoint2.getY())
			{
				yoff = gp;
			}
			if(appesArrow != null)
			{
				Rectangle2D arrowBounds = appesArrow.getPath(appesEndPoint1, appesEndPoint2).getBounds2D();
				if(appesEndPoint1.getX() < appesEndPoint2.getX())
				{
					xoff -= arrowBounds.getWidth();
				}
				else
				{
					xoff += arrowBounds.getWidth();
				}
			}
		}
		return new Point2D.Double(attach.getX() + xoff, attach.getY() + yoff);
	}


	private static Rectangle2D getStringBounds(Point2D pEndPoint1, Point2D pEndPoint2, 
			ArHead pArrow, String pString, boolean pCenter)
	{
		if (pString == null || pString.equals(""))
		{
			return new Rectangle2D.Double(pEndPoint2.getX(), pEndPoint2.getY(), 0, 0);
		}
		label.setText("<html>" + pString + "</html>");
		Dimension d = label.getPreferredSize();
		Point2D a = getAttachmentPoint(pEndPoint1, pEndPoint2, pArrow, d, pCenter);
		return new Rectangle2D.Double(a.getX(), a.getY(), d.getWidth(), d.getHeight());
	}

	@Override
	public Rectangle2D getBounds()
	{
		Point2D[] points = getPoints();
		Rectangle2D bounds = super.getBounds();
		bounds.add(getStringBounds(points[1], points[0], startArrowHead(), obtainLabelatStart(), false));
		bounds.add(getStringBounds(points[points.length / 2 - 1], points[points.length / 2], null, obtainLabelatMiddle(), true));
		bounds.add(getStringBounds(points[points.length - 2], points[points.length - 1], endArrowHead(), obtainLabelatEnd(), false));
		return bounds;
	}

	@Override
	protected Shape getShape()
	{
		GeneralPath path = getSegmentPath();
		Point2D[] points = getPoints();
		path.append(startArrowHead().getPath(points[1], points[0]), false);
		path.append(endArrowHead().getPath(points[points.length - 2], points[points.length - 1]), false);
		return path;
	}

	private GeneralPath getSegmentPath()
	{
		Point2D[] points = getPoints();
		GeneralPath path = new GeneralPath();
		Point2D p = points[points.length - 1];
		path.moveTo((float) p.getX(), (float) p.getY());
		for(int i = points.length - 2; i >= 0; i--)
		{
			p = points[i];
			path.lineTo((float) p.getX(), (float) p.getY());
		}
		return path;
	}

	@Override
	public Line2D getPointsConnection()
	{
		Point2D[] points = getPoints();
		return new Line2D.Double(points[0], points[points.length - 1]);
	}


	protected abstract Point2D[] getPoints();
}
