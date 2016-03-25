
package Epp_UML_Editeur.LeFramework;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.ChildNode;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.ElementGraph;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.NodeParent;


public final class Clipboard 
{
	private List<Node> appesNodes = new ArrayList<Node>();
	private List<Edge> appesEdges = new ArrayList<Edge>();


	public Clipboard() 
	{}
	

	Collection<Node> getNodes()
	{
		return Collections.unmodifiableCollection(appesNodes);
	}
	

	Collection<Edge> getEdges()
	{
		return Collections.unmodifiableCollection(appesEdges);
	}


	public void copy(ListSelection ppsSelection)
	{
		assert ppsSelection != null;
		appesNodes.clear();
		appesEdges.clear();
		


		for( ElementGraph element : ppsSelection )
		{
			if( element instanceof Edge && ppsSelection.capturesEdge((Edge)element ))
			{	
				appesEdges.add((Edge) ((Edge) element).clone());
			}
		}
		

		for( ElementGraph element : ppsSelection )
		{
			if( element instanceof Node )
			{
				Node cloned = ((Node) element).clone();
				appesNodes.add(cloned);
				edgesReassign(appesEdges, (Node) element, cloned);
			}
		}
	}
	
	private void edgesReassign(List<Edge> appesEdges, Node appesOld, Node appesNew)
	{
		for( Edge edge : appesEdges )
		{
			if( edge.getNodeStart() == appesOld )
			{
				edge.connect(appesNew, edge.getNodeEnd());
			}
			if( edge.getNodeEnd() == appesOld)
			{
				edge.connect(edge.getNodeStart(), appesNew);
			}
		}
		if( appesOld instanceof NodeParent)
		{
			List<ChildNode> oldChildnode = ((NodeParent) appesOld).getChildren();
			List<ChildNode> newChilnode = ((NodeParent) appesNew).getChildren();
			for( int i = 0; i < oldChildnode.size(); i++)
			{
				edgesReassign(appesEdges, oldChildnode.get(i), newChilnode.get(i));
			}
		}
	}
	

	public ListSelection paste(Graph appesGraph)
	{
		if( !Pastevalid(appesGraph))
		{
			return new ListSelection();
		}
		
		appesGraph.startActionCompound();
		Rectangle2D bounds = null;
		List<Edge> clonedEdges = new ArrayList<>();
		for( Edge edge : appesEdges)
		{
			clonedEdges.add((Edge) edge.clone());
			bounds = updateBounds(bounds, edge);
		}
		
		List<Node> nodesRootcloned = new ArrayList<>();
		for( Node node : appesNodes)
		{
			Node cloned = node.clone();
			nodesRootcloned.add(cloned);
			edgesReassign(clonedEdges, node, cloned);
			bounds = updateBounds(bounds, node);

		}
		
		for( Node node : nodesRootcloned )
		{
			appesGraph.add(node, new Point2D.Double(node.getBounds().getX() - bounds.getX(),
					node.getBounds().getY() - bounds.getY()));
		}
		for( Edge edge : clonedEdges )
		{
			appesGraph.connect(edge, edge.getNodeStart(), edge.getNodeEnd());
		}
		
		appesGraph.endActionCompound();
		
		ListSelection selectionList  = new ListSelection();
		for( Edge edge : clonedEdges )
		{
			selectionList.add(edge);
		}
		for( Node node : nodesRootcloned )
		{
			selectionList.add(node);
		}
		return selectionList;
	}
	
	private static Rectangle2D updateBounds(Rectangle2D appesBounds, ElementGraph appesElement)
	{
		Rectangle2D bounds = appesBounds;
		if( bounds == null )
		{
			bounds = appesElement.getBounds();
		}
		else
		{
			bounds.add(appesElement.getBounds());
		}
		return bounds;
	}
	

	private boolean Pastevalid(Graph appesGraph)
	{
		for( Edge edge : appesEdges)
		{
			if( !validEdgeFor(edge, appesGraph ))
			{
				return false;
			}
		}
		for( Node node : appesNodes)
		{
			if( !validNodeFor(node, appesGraph ))
			{
				return false;
			}
		}
		return true;
	}
	
	private static boolean validNodeFor( Node appesNode, Graph appesGraph )
	{
		for( Node node : appesGraph.getPrototypNodes() )
		{
			if( appesNode.getClass() == node.getClass() )
			{
				return true;
			}
		}
		return false;
	}
	
	private static boolean validEdgeFor( Edge appesEdge, Graph appesGraph )
	{
		for( Edge edge : appesGraph.getPrototypEdges() )
		{
			if( appesEdge.getClass() == edge.getClass() )
			{
				return true;
			}
		}
		return false;
	}
}





