

package Epp_UML_Editeur.LesDiagrammes;

import java.awt.geom.Point2D;
import java.util.ResourceBundle;

import Epp_UML_Editeur.Graphe.NodeStateCircular;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.EdgeNote;
import Epp_UML_Editeur.Graphe.NodeNote;
import Epp_UML_Editeur.Graphe.NodeState;
import Epp_UML_Editeur.Graphe.EdgeTransitionState;


public class GraphDiagramState extends Graph
{
	private static final Node[] PROTOTYP_ES_NODE = new Node[]{new NodeState(), new NodeStateCircular(), new NodeStateCircular(), new NodeNote()};
	private static final Edge[] PROTOTYP_ES_EDGE = new Edge[]{new EdgeTransitionState(), new EdgeNote()};
	
	static
	{
		((NodeStateCircular) PROTOTYP_ES_NODE[2]).setFinal(true);
	}
	
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
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("state.extension");
	}

	@Override
	public String getDescription() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("state.name");
	}
	
	@Override
	public boolean connect(Edge appesEdge, Point2D appesPoint1, Point2D appesPoint2)
	{	
		Node node1 = findNode(appesPoint1);
		Node node2 = findNode(appesPoint2);
		if(node1 != null)
		{
			if(node1 instanceof NodeStateCircular)
			{
				NodeStateCircular end = (NodeStateCircular) node1;
				if(end.isFinal() && !(appesEdge instanceof EdgeNote))
				{
					return false;
				}
			}

			if (node2 instanceof NodeStateCircular)
			{
				NodeStateCircular begin = (NodeStateCircular) node2;
				if(!begin.isFinal() && !(appesEdge instanceof EdgeNote))
				{
					return false;
				}
			}
		}
		return super.connect(appesEdge, appesPoint1, appesPoint2);
	}	
}





