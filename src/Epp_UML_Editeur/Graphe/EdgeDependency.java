

package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleLine;
import Epp_UML_Editeur.LeFramework.StyleSegmentationFacto;


public class EdgeDependency extends EdgeRelationshipClass
{

	public EdgeDependency()
	{}
	
	@Override
	protected ArHead endArrowHead()
	{
		return ArHead.V;
	}
	
	@Override
	protected StyleLine obtainStyleLine()
	{
		return StyleLine.DOTTEDSTYLE;
	}
	
	@Override
	protected Point2D[] getPoints()
	{
		return StyleSegmentationFacto.createStraightStrategy().getPath(getNodeStart(), getNodeEnd());
   }
}
