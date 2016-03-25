
package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleLine;


public class EdgeReturn extends EdgeSegmentedLabeled
{
	private String appesMiddleLabel;
	

	public EdgeReturn()
	{
		appesMiddleLabel = "";
	}
	

	public void setLabelatMiddle(String appesLabel)
	{
		appesMiddleLabel = appesLabel;
	}
	

	public String getLabelatMiddleLabel()
	{
		return appesMiddleLabel;
	}
	
	@Override
	protected String obtainLabelatMiddle()
	{
		return getLabelatMiddleLabel();
	}
	
	@Override
	protected StyleLine obtainStyleLine()
	{
		return StyleLine.DOTTEDSTYLE;
	}
	
	@Override
	protected ArHead endArrowHead()
	{
		return ArHead.V;
	}

	@Override
	protected Point2D[] getPoints()
	{
		ArrayList<Point2D> lReturnedge = new ArrayList<>();
		Node endNode = getNodeEnd();
		Rectangle2D startRect2d = getNodeStart().getBounds();
		Rectangle2D endRect2d = getNodeEnd().getBounds();
		if(endNode instanceof NodePoint)
		{
			lReturnedge.add(new Point2D.Double(endRect2d.getX(), endRect2d.getY()));
			lReturnedge.add(new Point2D.Double(startRect2d.getMaxX(), endRect2d.getY()));
		}      
		else if(startRect2d.getCenterX() < endRect2d.getCenterX())
		{
			lReturnedge.add(new Point2D.Double(startRect2d.getMaxX(), startRect2d.getMaxY()));
			lReturnedge.add(new Point2D.Double(endRect2d.getX(), startRect2d.getMaxY()));
		}
		else
		{
			lReturnedge.add(new Point2D.Double(startRect2d.getX(), startRect2d.getMaxY()));
			lReturnedge.add(new Point2D.Double(endRect2d.getMaxX(), startRect2d.getMaxY()));
		}
		return lReturnedge.toArray(new Point2D[lReturnedge.size()]);
	}
}
