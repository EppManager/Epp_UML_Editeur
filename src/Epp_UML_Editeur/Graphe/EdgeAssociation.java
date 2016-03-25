

package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleSegmentationFacto;


public class EdgeAssociation extends EdgeRelationshipClass
{

	public enum Directionality 
	{None, Start, End, Both}
	
	private Directionality appesDirectionality = Directionality.None;
	

	public EdgeAssociation()
	{}
	

	public void setDirectionality( Directionality appesDirectionality )
	{
		this.appesDirectionality = appesDirectionality;
	}
	

	public Directionality getDirectionality()
	{
		return appesDirectionality;
	}
	
	@Override
	protected ArHead startArrowHead()
	{
		if( appesDirectionality == Directionality.Both || appesDirectionality == Directionality.Start )
		{
			return ArHead.V;
		}
		else
		{
			return ArHead.ARROWHEAD_NONE;
		}
	}
	
	@Override
	protected ArHead endArrowHead()
	{
		if( appesDirectionality == Directionality.Both || appesDirectionality == Directionality.End )
		{
			return ArHead.V;
		}
		else
		{
			return ArHead.ARROWHEAD_NONE;
		}
	}
	
	@Override
	public Point2D[] getPoints()
	{
		return StyleSegmentationFacto.createHVHStrategy().getPath(getNodeStart(), getNodeEnd());
   }
}
