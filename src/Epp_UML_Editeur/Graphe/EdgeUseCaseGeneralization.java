

package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleSegmentationFacto;


public class EdgeUseCaseGeneralization extends EdgeSegmentedLabeled
{

	public EdgeUseCaseGeneralization()
	{}
	
	@Override
	protected ArHead endArrowHead()
	{
		return ArHead.ARROWHEAD_TRIANGLE;
	}
	
	@Override
	protected Point2D[] getPoints()
	{
		return StyleSegmentationFacto.createStraightStrategy().getPath(getNodeStart(), getNodeEnd());
	}
}
