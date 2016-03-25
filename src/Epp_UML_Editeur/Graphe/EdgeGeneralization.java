

package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleLine;
import Epp_UML_Editeur.LeFramework.StyleSegmentationFacto;


public class EdgeGeneralization extends EdgeRelationshipClass
{

	public enum Type 
	{Inheritance, Implementation}
	
	private Type appesType = Type.Inheritance;
	

	public EdgeGeneralization(Type appesType)
	{
		this.appesType = appesType;
	}
	

	public EdgeGeneralization()
	{}



	public Type getType()
	{
		return appesType;
	}
	

	public void setType(Type appesType)
	{
		this.appesType = appesType;
	}
	
	@Override
	protected ArHead endArrowHead()
	{
		return ArHead.ARROWHEAD_TRIANGLE;
	}
	
	@Override
	protected StyleLine obtainStyleLine()
	{
		if( appesType == Type.Implementation )
		{
			return StyleLine.DOTTEDSTYLE;
		}
		else
		{
			return StyleLine.SOLIDSTYLE;
		}
	}
	
	@Override
	public Point2D[] getPoints()
	{
		return StyleSegmentationFacto.createVHVStrategy().getPath(getNodeStart(), getNodeEnd());
   }
}
