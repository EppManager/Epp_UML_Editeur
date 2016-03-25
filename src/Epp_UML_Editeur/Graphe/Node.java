

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.Direction;
import Epp_UML_Editeur.LeFramework.Grid;


public interface Node extends ElementGraph
{

	void graphics2dDraw(Graphics2D pGraphics2D);


	void AppCommandtranslate(double pDX, double pDY);


	boolean contains(Point2D appPoint);


	Point2D getConnectionPoint(Direction appDirections);


	void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille);


	Node clone();
}
