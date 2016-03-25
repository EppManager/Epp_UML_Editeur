
package Epp_UML_Editeur.LeFramework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.ChildNode;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.ElementGraph;
import Epp_UML_Editeur.Graphe.NodeParent;


public class ListSelection implements Iterable<ElementGraph>
{
	private Stack<ElementGraph> appesSelected = new Stack<>();
	

	public void add(ElementGraph ppesElement)
	{
		assert ppesElement != null;
		if( !parentContained( ppesElement ))
		{
			appesSelected.remove(ppesElement);
			appesSelected.push(ppesElement);
			

			ArrayList<ElementGraph> toRemove = new ArrayList<>();
			for( ElementGraph element : appesSelected)
			{
				if( parentContained(element) )
				{
					toRemove.add(element);
				}
			}
			for( ElementGraph element : toRemove )
			{
				remove(element);
			}
		}
	}
	

	public boolean parentContained(ElementGraph ppesElement)
	{
		if( ppesElement instanceof ChildNode )
		{
			NodeParent nodparent = ((ChildNode) ppesElement).getNodeParent();
			if( nodparent == null )
			{
				return false;
			}
			else if( appesSelected.contains(nodparent))
			{
				return true;
			}
			else
			{
				return parentContained(nodparent);
			}
		}
		else
		{
			return false;
		}
	}
	

	public boolean transitivelyContains(ElementGraph ppesElement)
	{
		return contains(ppesElement) || parentContained(ppesElement);
	}
	

	public void clearSelection()
	{
		appesSelected.clear();
	}
	

	public ElementGraph getLastSelected()
	{
		if( appesSelected.size() > 0 )
		{
			return appesSelected.peek();
		}
		else
		{
			return null;
		}
	}
	

	public Node getLastNode()
	{
		for( int i = appesSelected.size()-1; i >=0; i--)
		{
			if( appesSelected.get(i) instanceof Node )
			{
				return (Node) appesSelected.get(i);
			}
		}
		return null;
	}
	

	public boolean contains(ElementGraph ppesElement)
	{
		return appesSelected.contains(ppesElement);
	}
	

	public boolean capturesEdge(Edge ppesEdge)
	{
		return (contains(ppesEdge.getNodeStart()) || parentContained(ppesEdge.getNodeStart())) &&
				(contains(ppesEdge.getNodeEnd()) || parentContained(ppesEdge.getNodeEnd()));
	}
	

	public void remove(ElementGraph ppesElement)
	{
		assert ppesElement != null;
		appesSelected.remove(ppesElement);
	}
	

	public void set(ElementGraph ppesElement)
	{
		assert ppesElement != null;
		appesSelected.clear();
		appesSelected.add(ppesElement);
	}

	@Override
	public Iterator<ElementGraph> iterator()
	{
		return appesSelected.iterator();
	}
	

	public int size()
	{
		return appesSelected.size();
	}
}
