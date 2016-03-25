

package Epp_UML_Editeur.LesDiagrammes;

import java.awt.geom.Point2D;
import java.util.ResourceBundle;

import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.EdgeUseCaseGeneralization;
import Epp_UML_Editeur.Graphe.NodeActor;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.EdgeNote;
import Epp_UML_Editeur.Graphe.NodeNote;
import Epp_UML_Editeur.Graphe.EdgeUseCaseAssociation;
import Epp_UML_Editeur.Graphe.EdgeUseCaseDependency;
import Epp_UML_Editeur.Graphe.NodeUseCase;


public class GraphDiagramUseCase extends Graph
{
	private static final Node[] PROTOTYP_ES_NODE = new Node[]{new NodeActor(), new NodeUseCase(), new NodeNote()};
	private static final Edge[] PROTOTYP_ES_EDGE = new Edge[]{new EdgeUseCaseAssociation(),
															 new EdgeUseCaseDependency(EdgeUseCaseDependency.Type.Extend),
															 new EdgeUseCaseDependency(EdgeUseCaseDependency.Type.Include),
															 new EdgeUseCaseGeneralization(),
															 new EdgeNote()};

	@Override
	public Node[] getPrototypNodes()
	{
		return PROTOTYP_ES_NODE;
	}

	@Override
	public Edge[] getPrototypEdges()
	{
		return PROTOTYP_ES_EDGE;
	}   
	
	@Override
	public String getFileExtension() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("usecase.extension");
	}

	@Override
	public String getDescription() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("usecase.name");
	}
	
	@Override
	public boolean appsConnectElements(Edge appesEdge, Node appesNode1, Node appesNode2, Point2D appesPoint2)
	{
		if( !super.appsConnectElements(appesEdge, appesNode1, appesNode2, appesPoint2) )
		{
			return false;
		}
		if( appesNode1 == appesNode2 )
		{
			return false;
		}
		
		return true;
	}
}





