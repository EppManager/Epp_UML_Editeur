
package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public interface Edge extends ElementGraph
{

   void draw(Graphics2D appesGraphics2D);


   	boolean contains(Point2D appesPoint2d);


   void connect(Node appesStart, Node appesEnd);


   	Node getNodeStart();


   	Node getNodeEnd();


   	Line2D getPointsConnection();


   	Object clone();
}

