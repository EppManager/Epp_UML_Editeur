
package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.Collection;

import Epp_UML_Editeur.LeFramework.ListenerModificationGraph;
import Epp_UML_Editeur.LeFramework.Grid;


public abstract class Graph
{
	protected ListenerModificationGraph appesListenerMode;
	protected ArrayList<Node> appesNodesRoot;
	protected ArrayList<Edge> appesEdges;
	protected transient ArrayList<Node> appesNodesToBeRemoved;
	protected transient ArrayList<Edge> appesEdgesToBeRemoved;
	private transient boolean appesNeedsLayout;
	private transient Rectangle2D appesMinBounds;


	public Graph()
	{
		appesNodesRoot = new ArrayList<>();
		appesEdges = new ArrayList<>();
		appesNodesToBeRemoved = new ArrayList<>();
		appesEdgesToBeRemoved = new ArrayList<>();
		appesListenerMode = new ListenerModificationGraph();
		appesNeedsLayout = true;
	}
	

	public void startActionCompound()
	{
		appesListenerMode.ListeningstartCompound();
	}
	

	public void endActionCompound()
	{
		appesListenerMode.ListeningendCompound();
	}


	public void addModificationListener(ListenerModificationGraph appesModListener)
	{
		appesListenerMode = appesModListener;
	}


	public abstract String getFileExtension();


	public abstract String getDescription();
	

	protected void addEdge(Node appesOrigin, Edge appesEdge, Point2D appesPoint1, Point2D appesPoint2)
	{}

	private NodePoint createNodePointIfAllowed(Node appesNode1, Edge apoesEdge, Point2D appesPoint2)
	{
		if(appesNode1 instanceof NodeNote && apoesEdge instanceof EdgeNote)
		{
			NodePoint lReturn = new NodePoint();
			lReturn.AppCommandtranslate(appesPoint2.getX(), appesPoint2.getY());
			return lReturn;
		}
		else
		{
			return null;
		}
	}
	

	public boolean connect(Edge appesEdge, Point2D appesPoint1, Point2D appesPoint2)
	{
		Node node1 = findNode(appesPoint1);
		if( node1 == null )
		{
			return false;
		}
		
		Node node2 = findNode(appesPoint2);
		if( node1 instanceof NodeNote)
		{
			node2 = createNodePointIfAllowed(node1, appesEdge, appesPoint2);
		}
		
		if(!appsConnectElements(appesEdge, node1, node2, appesPoint2))
		{
			return false;
		}

		appesEdge.connect(node1, node2);
			


		appesListenerMode.ListeningstartCompound();
		addEdge(node1, appesEdge, appesPoint1, appesPoint2);
		appesEdges.add(appesEdge);
		appesListenerMode.edgeAdded(this, appesEdge);
		
		if(!appesNodesRoot.contains(appesEdge.getNodeEnd()) && appesEdge.getNodeEnd() instanceof NodePoint)
		{
			appesNodesRoot.add(appesEdge.getNodeEnd());
		}
		appesNeedsLayout = true;
		appesListenerMode.ListeningendCompound();
		return true;
	}


	public boolean add(Node appesNode, Point2D appesPoint)
	{
		Rectangle2D bounds = appesNode.getBounds();
		appesNode.AppCommandtranslate(appesPoint.getX() - bounds.getX(), appesPoint.getY() - bounds.getY());
		
		appesListenerMode.nodeAdded(this, appesNode);
		if( !(appesNode instanceof ChildNode && ((ChildNode)appesNode).getNodeParent() != null) )
		{

			appesNodesRoot.add(appesNode);
		}
		appesNeedsLayout = true;
		return true;
	}


	public Node findNode(Point2D appesPoint)
	{
		Node result = null;
		for( Node node : appesNodesRoot)
		{
			Node temp = findNodedeep(node, appesPoint);
			if( temp != null )
			{
				result = temp;
			}
		}
		return result;
	}
	
	private Node findNodedeep(Node appesNode, Point2D appesPoint)
	{
		Node node = null;
		if( appesNode instanceof NodeParent)
		{
			for( Node child : ((NodeParent) appesNode).getChildren())
			{
				node = findNodedeep(child, appesPoint);
				if( node != null )
				{
					return node;
				}
			}
		}
		if( appesNode.contains(appesPoint))
		{
			return appesNode;
		}
		else
		{
			return null;
		}
	}


	public Edge findEdge(Point2D appesPoint)
	{
		for (int i = appesEdges.size() - 1; i >= 0; i--)
		{
			Edge ed = appesEdges.get(i);
			if(ed.contains(appesPoint))
			{
				return ed;
			}
		}
		return null;
	}


	public ArrayList<Edge> getNodeEdges(Node appesNode)
	{
		ArrayList<Edge> toRet = new ArrayList<Edge>();
		for(int i = 0; i < appesEdges.size(); i++)
		{
			Edge ed = appesEdges.get(i);
			if((ed.getNodeStart() == appesNode || ed.getNodeEnd() == appesNode) && !appesEdgesToBeRemoved.contains(ed))
			{
				toRet.add(ed);
			}
		}
		return toRet;
	}
	

	public boolean existsEdge(Class<?> appesType, Node appesStart, Node appesEnd)
	{
		assert appesType !=null && appesStart != null && appesEnd != null;
		for( Edge edge : getEdges() )
		{
			if( edge.getClass() == appesType && edge.getNodeStart() == appesStart && edge.getNodeEnd() == appesEnd )
			{
				return true;
			}
		}
		return false;
	}


	public void draw(Graphics2D appesGraphics2D, Grid appesGrid)
	{
		layout(appesGraphics2D, appesGrid);

		for(int i = 0; i < appesNodesRoot.size(); i++)
		{
			Node n = appesNodesRoot.get(i);
			drawNode(n, appesGraphics2D);
		}

		for(int i = 0; i < appesEdges.size(); i++)
		{
			Edge e = appesEdges.get(i);
			e.draw(appesGraphics2D);
		}
	}
	
	private void drawNode(Node appesNode, Graphics2D appesGraphics2D)
	{
		appesNode.graphics2dDraw(appesGraphics2D);
		if( appesNode instanceof NodeParent)
		{
			for( Node node : ((NodeParent) appesNode).getChildren())
			{
				drawNode(node, appesGraphics2D);
			}
		}
	}


	public void deleteNode(Node appesNode)
	{
		if(appesNodesToBeRemoved.contains(appesNode))
		{
			return;
		}
		appesListenerMode.ListeningstartCompound();
		appesNodesToBeRemoved.add(appesNode);
		
		if(appesNode instanceof NodeParent)
		{
			ArrayList<ChildNode> children = new ArrayList<ChildNode>(((NodeParent) appesNode).getChildren());

			for(Node childNode: children)
			{
				deleteNode(childNode);
			}
		}

 		for(Node node : appesNodesRoot)
		{
			deleteFromParent(node, appesNode);
		}
		

		for(Edge edge : appesEdges)
		{
			if(edge.getNodeStart() == appesNode || edge.getNodeEnd() == appesNode)
			{
				edgeDeleted(edge);
			}
		}
		appesListenerMode.nodeRemoved(this, appesNode);
		appesListenerMode.ListeningendCompound();
		appesNeedsLayout = true;
	}
	
	private static void deleteFromParent(Node appesParent, Node appesToRemove)
	{
		if( appesParent instanceof NodeParent)
		{
			if( appesToRemove instanceof ChildNode && ((ChildNode) appesToRemove).getNodeParent() == appesParent )
			{
				((NodeParent) appesParent).getChildren().remove(appesToRemove);


			}
			for( Node child : ((NodeParent) appesParent).getChildren() )
			{
				deleteFromParent(child, appesToRemove);
			}
		}
	}


	public void removeElement(ElementGraph appesElement)
	{
		if(appesElement instanceof Node)
		{
			deleteNode((Node) appesElement);
		}
		else if(appesElement instanceof Edge)
		{

			edgeDeleted((Edge) appesElement);
		}
	}


	public boolean contains( ElementGraph appesElement )
	{	
		if( appesEdges.contains( appesElement ))
		{
			return true;
		}
		for( Node node : appesNodesRoot)
		{
			if( containsNode( node, appesElement))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean containsNode(Node appesTest, ElementGraph appesTarget)
	{
		if( appesTest == appesTarget )
		{
			return true;
		}
		else if( appesTest instanceof NodeParent)
		{
			for( Node node : ((NodeParent) appesTest).getChildren())
			{
				if( containsNode(node, appesTarget))
				{
					return true;
				}
			}
		}
		return false;
	}


	public void edgeDeleted(Edge appesEdge)
	{
		if (appesEdgesToBeRemoved.contains(appesEdge))
		{
			return;
		}
		appesEdgesToBeRemoved.add(appesEdge);
		appesListenerMode.edgeRemoved(this, appesEdge);
		for(int i = appesNodesRoot.size() - 1; i >= 0; i--)
		{
			Node n = appesNodesRoot.get(i);
			if( n instanceof EdgeNote)
			{
				if(appesEdge.getNodeStart() == n)
				{
					deleteNode(appesEdge.getNodeEnd());
				}
			}
		}
		appesNeedsLayout = true;
	}


	public void layout()
	{
		appesNeedsLayout = true;
	}


	protected void layout(Graphics2D appesGraphics2D, Grid appesGrid)
	{
		if(!appesNeedsLayout)
		{
			return;
		}
		appesNodesRoot.removeAll(appesNodesToBeRemoved);
		appesEdges.removeAll(appesEdgesToBeRemoved);
		appesNodesToBeRemoved.clear();
		appesEdgesToBeRemoved.clear();

		for(int i = 0; i < appesNodesRoot.size(); i++)
		{
			Node n = appesNodesRoot.get(i);
			n.layout(this, appesGraphics2D, appesGrid);
		}
		appesNeedsLayout = false;
	}


	public Rectangle2D getBounds()
	{
		Rectangle2D rect2d = appesMinBounds;
		for(int i = 0; i < appesNodesRoot.size(); i++)
		{
			Node n = appesNodesRoot.get(i);
			Rectangle2D b = n.getBounds();
			if(rect2d == null)
			{
				rect2d = b;
			}
			else
			{
				rect2d.add(b);
			}
		}
		for(int i = 0; i < appesEdges.size(); i++)
		{
			Edge ed = appesEdges.get(i);
			rect2d.add(ed.getBounds());
		}
		if(rect2d == null )
		{
			return new Rectangle2D.Double();
		}
		else
		{
			return new Rectangle2D.Double(rect2d.getX(), rect2d.getY(), rect2d.getWidth() + AbstractNode.GAP_SHADOW_APP, rect2d.getHeight() + AbstractNode.GAP_SHADOW_APP);
		}
	}


	public Rectangle2D getMinBounds() 
	{ return appesMinBounds; }


	public void setMinBounds(Rectangle2D appesMinBounds)
	{ this.appesMinBounds = appesMinBounds; }


	public abstract Node[] getPrototypNodes();


	public abstract Edge[] getPrototypEdges();


	public static void setPersistenceDelegate(Encoder appesEncoder)
	{
		appesEncoder.setPersistenceDelegate(Graph.class, new DefaultPersistenceDelegate() {
			protected void initialize(Class<?> pType, Object appesOldInstance, Object pNewInstance, Encoder appesOut) {
				super.initialize(pType, appesOldInstance, pNewInstance, appesOut);
				Graph gra = (Graph) appesOldInstance;

				for (int i = 0; i < gra.appesNodesRoot.size(); i++) {
					Node n = gra.appesNodesRoot.get(i);
					Rectangle2D bounds = n.getBounds();
					Point2D p = new Point2D.Double(bounds.getX(), bounds.getY());
					appesOut.writeStatement(new Statement(appesOldInstance, "addNode", new Object[]{n, p}));
				}
				for (int i = 0; i < gra.appesEdges.size(); i++) {
					Edge ed = gra.appesEdges.get(i);
					appesOut.writeStatement(new Statement(appesOldInstance, "connect", new Object[]{ed, ed.getNodeStart(), ed.getNodeEnd()}));
				}
			}
		});
	}


	public Collection<Node> getRootNodes()
	{ return appesNodesRoot; }


	public Collection<Edge> getEdges() 
	{ return appesEdges; }


	public void addNode(Node appesNode, Point2D appesPoint)
	{
		Rectangle2D bounds = appesNode.getBounds();
		appesNode.AppCommandtranslate(appesPoint.getX() - bounds.getX(), appesPoint.getY() - bounds.getY());
		appesNodesRoot.add(appesNode);
	}


	public void connect(Edge appesEdge, Node appesStart, Node appesEnd)
	{
		appesEdge.connect(appesStart, appesEnd);
		appesEdges.add(appesEdge);
	}


	public boolean appsConnectElements(Edge appesEdge, Node appesNode1, Node appesNode2, Point2D appesPoint2)
	{
		if( appesNode2 == null )
		{
			return false;
		}
		if( existsEdge(appesEdge.getClass(), appesNode1, appesNode2))
		{
			return false;
		}
		if((appesNode2 instanceof NodeNote || appesNode1 instanceof NodeNote) && !(appesEdge instanceof EdgeNote))
		{
			return false;
		}
		if( appesEdge instanceof EdgeNote && !(appesNode1 instanceof NodeNote || appesNode2 instanceof NodeNote))
		{
			return false;
		}
		return true;
	}
}


