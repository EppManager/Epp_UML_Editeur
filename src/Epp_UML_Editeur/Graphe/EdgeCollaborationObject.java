
package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.StyleSegmentationFacto;


public class EdgeCollaborationObject extends EdgeRelationshipClass
{

	public EdgeCollaborationObject()
	{}
	
	@Override
	protected Point2D[] getPoints()
	{
		return StyleSegmentationFacto.createStraightStrategy().getPath(getNodeStart(), getNodeEnd());
   }
}
