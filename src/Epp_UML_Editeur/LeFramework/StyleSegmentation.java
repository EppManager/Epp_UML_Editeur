
package Epp_UML_Editeur.LeFramework;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.Graphe.Node;




public interface StyleSegmentation
{

	Point2D[] getPath(Node appesStart, Node appesEnd);
}
