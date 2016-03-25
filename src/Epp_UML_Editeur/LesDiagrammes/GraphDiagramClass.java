

package Epp_UML_Editeur.LesDiagrammes;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Epp_UML_Editeur.Graphe.EdgeAggregation;
import Epp_UML_Editeur.Graphe.EdgeAssociation;
import Epp_UML_Editeur.Graphe.ChildNode;
import Epp_UML_Editeur.Graphe.ClassNode;
import Epp_UML_Editeur.Graphe.EdgeDependency;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.EdgeGeneralization;
import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.InterfaceNode;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.EdgeNote;
import Epp_UML_Editeur.Graphe.NodeNote;
import Epp_UML_Editeur.Graphe.NodesPackage;


public class GraphDiagramClass extends Graph
{
	private static final Node[] PROTOTYP_NODE = new Node[] {new ClassNode(),
															  new InterfaceNode(),
															  new NodesPackage(),
															  new NodeNote()};
	
	private static final Edge[] PROTOTYP_EDGE = new Edge[] {new EdgeDependency(),
															  new EdgeGeneralization(),
															  new EdgeGeneralization(EdgeGeneralization.Type.Implementation),
															  new EdgeAssociation(),
															  new EdgeAggregation(),
															  new EdgeAggregation(EdgeAggregation.Type.Composition),
															  new EdgeNote()};

	@Override
	public Node[] getPrototypNodes()
	{
		return PROTOTYP_NODE;
	}

	@Override
	public Edge[] getPrototypEdges()
	{
		return PROTOTYP_EDGE;
	}

	@Override
	public String getFileExtension() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("class.extension");
	}

	@Override
	public String getDescription() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("class.name");
	}

	private static boolean aChildNodeAdded(Node aChildPotential)
	{
		return aChildPotential instanceof ClassNode || aChildPotential instanceof InterfaceNode ||
					aChildPotential instanceof NodesPackage;
	}
	
	@Override
	public boolean appsConnectElements(Edge appesEdge, Node appesNode1, Node appesNode2, Point2D appesPoint2)
	{
		if( !super.appsConnectElements(appesEdge, appesNode1, appesNode2, appesPoint2) )
		{
			return false;
		}
		if( appesEdge instanceof EdgeGeneralization && appesNode1 == appesNode2)
		{
			return false;
		}
		
		return true;
	}

	private NodesPackage findContainer( List<Node> pNodes, Point2D pPoint)
	{
		NodesPackage container = null;
		for( Node node : pNodes )
		{
			if( node instanceof NodesPackage && node.contains(pPoint) )
			{
				container = (NodesPackage) node;
			}
		}
		if( container == null )
		{
			return null;
		}

		List<Node> children = new ArrayList(container.getChildren());
		if( children.size() == 0 )
		{
			return container;
		}
		else
		{
			NodesPackage deeperContainer = findContainer( children, pPoint );
			if( deeperContainer == null )
			{
				return container;
			}
			else
			{
				return deeperContainer;
			}
		}
	}
	
	@Override
	public boolean add(Node appesNode, Point2D appesPoint)
	{
		if( aChildNodeAdded(appesNode))
		{
			NodesPackage container = null;
			if( appesNode instanceof ChildNode && ((ChildNode) appesNode).getNodeParent() != null )
			{
				container = (NodesPackage)((ChildNode) appesNode).getNodeParent();
			}
			else
			{
				container = findContainer(appesNodesRoot, appesPoint);
			}
			if( container != null )
			{
				container.addChild((ChildNode) appesNode);
			}
		}
		super.add(appesNode, appesPoint);
		return true;
	}
}





