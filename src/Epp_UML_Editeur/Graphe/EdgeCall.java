

package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.Direction;


public class EdgeCall extends EdgeSegmentedLabeled
{
	private boolean appesSignal;
	private String appesMiddleLabel;
	

	public EdgeCall()
	{
		setSignal(false);
		appesMiddleLabel = "";
	}
	

	public void setLabelatMiddle(String appesLabel)
	{
		appesMiddleLabel = appesLabel;
	}
	

	public String getLabelatMiddle()
	{
		return appesMiddleLabel;
	}
	
	@Override
	protected String obtainLabelatMiddle()
	{
		return getLabelatMiddle();
	}
	

	protected ArHead endArrowHead()
	{
		if(appesSignal)
		{
			return ArHead.ARROWHEAD_HALF_V;
		}
		else
		{
			return ArHead.V;
		}
	}


	public boolean signal()
	{ return appesSignal; }


	public void setSignal(boolean appesNewValue)
	{ 
		appesSignal = appesNewValue;
	}

	@Override
	protected Point2D[] getPoints()
	{
		ArrayList<Point2D> b = new ArrayList<>();
		Node nodend = getNodeEnd();
		Rectangle2D startrctgle2d = getNodeStart().getBounds();
		Rectangle2D endrctgle2d = nodend.getBounds();
      
		if(nodend instanceof NodeCall && ((NodeCall)nodend).getNodeParent() == ((NodeCall) getNodeStart()).getNodeParent())
		{
			Point2D pt1 = new Point2D.Double(startrctgle2d.getMaxX(), endrctgle2d.getY() - NodeCall.YGAP_CALL / 2);
			Point2D pt2 = new Point2D.Double(endrctgle2d.getMaxX(), endrctgle2d.getY());
			Point2D pt3 = new Point2D.Double(pt2.getX() + endrctgle2d.getWidth(), pt2.getY());
			Point2D pt4 = new Point2D.Double(pt3.getX(), pt1.getY());
			b.add(pt1);
			b.add(pt4);
			b.add(pt3);
			b.add(pt2);
		}
		else if(nodend instanceof NodePoint)
		{
			b.add(new Point2D.Double(startrctgle2d.getMaxX(), startrctgle2d.getY()));
			b.add(new Point2D.Double(endrctgle2d.getX(), startrctgle2d.getY()));
		}
		else     
		{
			Direction dirc = new Direction(startrctgle2d.getX() - endrctgle2d.getX(), 0);
			Point2D endPoint = getNodeEnd().getConnectionPoint(dirc);
         
			if(startrctgle2d.getCenterX() < endPoint.getX())
			{
				b.add(new Point2D.Double(startrctgle2d.getMaxX(), endPoint.getY()));
			}
			else
			{
				b.add(new Point2D.Double(startrctgle2d.getX(), endPoint.getY()));
			}
			b.add(endPoint);
		}
		return b.toArray(new Point2D[b.size()]);
	}
}


