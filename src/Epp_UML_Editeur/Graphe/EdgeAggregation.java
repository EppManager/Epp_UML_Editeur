
package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleSegmentationFacto;


public class EdgeAggregation extends EdgeRelationshipClass
{

	public enum Type 
	{Aggregation, Composition}

	private Type appesType = Type.Aggregation;


	public EdgeAggregation(Type appesType)
	{
		this.appesType = appesType;
	}


	public EdgeAggregation()
	{}


	public Type getType()
	{
		return appesType;
	}


	public void setType(Type apespType)
	{
		appesType = apespType;
	}

	@Override
	protected ArHead startArrowHead()
	{
		if( appesType == Type.Composition )
		{
			return ArHead.ARROWHEAD_BLACK_DIAMOND;
		}
		else
		{
			return ArHead.ARROWHEAD_DIAMOND;
		}
	}

	@Override
	public Point2D[] getPoints()
	{
		return StyleSegmentationFacto.createHVHStrategy().getPath(getNodeStart(), getNodeEnd());
	}
}
