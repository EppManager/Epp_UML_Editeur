

package Epp_UML_Editeur.LesDiagrammes;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Epp_UML_Editeur.Graphe.NodeCall;
import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.Graphe.EdgeCall;
import Epp_UML_Editeur.Graphe.ChildNode;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.NodeImplicitParameter;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.EdgeNote;
import Epp_UML_Editeur.Graphe.NodeNote;
import Epp_UML_Editeur.Graphe.EdgeReturn;


public class GraphDiagramSequence extends Graph
{
	private static final Node[] PROTOTYP_ES_NODE = new Node[]{new NodeImplicitParameter(), new NodeCall(), new NodeNote()};
	private static final Edge[] PROTOTYP_ES_EDGE = new Edge[]{new EdgeCall(), new EdgeReturn(), new EdgeNote()};
	
	private static final int YGP_NODE_CALL = 5;
	

	@Override
	public boolean add(Node appesNode, Point2D appesPoint)
	{
		if( appesNode instanceof NodeImplicitParameter && getRootNodes().isEmpty())
		{

			super.add(appesNode, appesPoint);
			NodeCall nodeTocall = new NodeCall();
			NodeImplicitParameter node = (NodeImplicitParameter)appesNode;
			node.addChild(nodeTocall);
			super.add(nodeTocall, appesPoint);

			nodeTocall.setBounds(new Rectangle2D.Double(nodeTocall.getBounds().getX(), node.getTopRectangle().getHeight(),
					nodeTocall.getBounds().getWidth(), nodeTocall.getBounds().getHeight()));
			return true;
		}
		else if(appesNode instanceof NodeCall)
		{
			NodeImplicitParameter target = insideTargetArea(appesPoint);
			if( target != null )
			{
				target.addChild((ChildNode)appesNode);
				super.add(appesNode, appesPoint);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			super.add( appesNode, appesPoint);
			return true;
		}
	}
	

	private NodeImplicitParameter insideTargetArea(Point2D pesPoint2d)
	{
		for( Node appesnode : getRootNodes() )
		{
			if(appesnode instanceof NodeImplicitParameter && appesnode.contains(pesPoint2d))
			{
				if( !(pesPoint2d.getY() < ((NodeImplicitParameter)appesnode).getTopRectangle().getMaxY() + YGP_NODE_CALL))
				{
					return (NodeImplicitParameter) appesnode;
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean appsConnectElements(Edge appesEdge, Node appesNode1, Node appesNode2, Point2D appesPoint2)
	{
		boolean lReturn = true;
		if( !super.appsConnectElements(appesEdge, appesNode1, appesNode2, appesPoint2) )
		{
			lReturn = false;
		}
		else if(appesNode1 instanceof NodeCall && appesEdge instanceof EdgeReturn && appesNode2 instanceof NodeCall)
		{

			lReturn = appesNode2 == getCaller(appesNode1) &&
					!(((NodeCall)appesNode1).getNodeParent() == ((NodeCall)appesNode2).getNodeParent());
		}
		else if(appesNode1 instanceof NodeCall && !(appesEdge instanceof EdgeCall))
		{
			lReturn = false;
		}
		else if(appesNode1 instanceof NodeCall && !(appesNode2 instanceof NodeCall) && !(appesNode2 instanceof NodeImplicitParameter))
		{
			lReturn = false;
		}
		else if(appesNode1 instanceof NodeImplicitParameter)
		{
			lReturn = false;
		}
		else if( appesNode1 instanceof NodeCall && appesEdge instanceof EdgeCall && appesNode2 instanceof NodeImplicitParameter && getCaller(appesNode2) != null)
		{
			lReturn = !((NodeImplicitParameter)appesNode2).getTopRectangle().contains(appesPoint2);
		}
		return lReturn;
	}
	

	private boolean dominatorisCall(NodeCall apespCallerPotential, NodeCall appesCallee)
	{
		for( NodeCall caller = getCaller(appesCallee); caller != null; caller = getCaller(caller))
		{
			if( caller == apespCallerPotential )
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void addEdge(Node appesOrigin, Edge appesEdge, Point2D appesPoint1, Point2D appesPoint2)
	{
		if( !(appesOrigin instanceof NodeCall) )
		{
			return;
		}
		NodeCall originCallnode = (NodeCall) appesOrigin;
		if( appesEdge instanceof EdgeReturn)
		{
			return;
		}
		Node end = appesEdge.getNodeEnd();
		

		if( end instanceof NodeCall)
		{
			NodeCall nodeCallAsEnd = (NodeCall) end;
			if( nodeCallAsEnd.getNodeParent() == originCallnode.getNodeParent() )
			{
				NodeCall newNodeCall = new NodeCall();
				((NodeImplicitParameter)originCallnode.getNodeParent()).addChild(newNodeCall, appesPoint1);
				appesEdge.connect(originCallnode, newNodeCall);
			}
			else
			{
				if (dominatorisCall(nodeCallAsEnd, originCallnode))
				{
					NodeCall newNodeCall = new NodeCall();
					((NodeImplicitParameter)nodeCallAsEnd.getNodeParent()).addChild(newNodeCall, appesPoint1);
					appesEdge.connect(originCallnode, newNodeCall);
				}

			}
		}
		else if( end instanceof NodeImplicitParameter)
		{
			NodeImplicitParameter nodeParameterImplicitAsend = (NodeImplicitParameter) end;
			if(nodeParameterImplicitAsend.getTopRectangle().contains(appesPoint2))
			{
				((EdgeCall)appesEdge).setLabelatMiddle("\u00ABcreate\u00BB");
			}
			else
			{
				NodeCall newNodeCall = new NodeCall();
				nodeParameterImplicitAsend.addChild(newNodeCall, appesPoint1);
				appesEdge.connect(appesOrigin, newNodeCall);
			}
		}
	}

	@Override
	public void edgeDeleted(Edge appesEdge)
	{
		super.edgeDeleted(appesEdge);
		if(appesEdge instanceof EdgeCall && CalleeshasNo(appesEdge.getNodeEnd()))
		{
			deleteNode(appesEdge.getNodeEnd());
		}
		
 		if( appesEdge instanceof EdgeCall)
		{
			Edge returnEdge = null;
			for( Edge edge : appesEdges)
			{
				if( edge instanceof EdgeReturn && edge.getNodeStart() == appesEdge.getNodeEnd() && edge.getNodeEnd() == appesEdge.getNodeStart())
				{
					returnEdge = edge;
					break;
				}
			}
			if( returnEdge != null )
			{
				edgeDeleted(returnEdge);
			}
		}
	}
	

	private boolean CalleeshasNo(Node appesNode)
	{
		if( !(appesNode instanceof NodeCall))
		{
			return false;
		}
		assert appesNode instanceof NodeCall;
		for( Edge edge : appesEdges)
		{
			if( edge.getNodeStart() == appesNode )
			{
				return false;
			}
		}
		return true;
	}
	

	public NodeCall getCaller(Node appesNode)
	{
		for( Edge edge : appesEdges)
		{
			if( edge.getNodeEnd() == appesNode  && edge instanceof EdgeCall)
			{
				return (NodeCall) edge.getNodeStart();
			}
		}
		return null;
	}
	

	public Edge edgeFind(Node appesStart, Node appesEnd)
	{
		for( Edge edge : appesEdges)
		{
			if(edge.getNodeStart() == appesStart && edge.getNodeEnd() == appesEnd)
			{
				return edge;
			}
		}
		return null;
	}
 
	@Override
	public void layout(Graphics2D appesGraphics2D, Grid appesGrid)
	{
		super.layout(appesGraphics2D, appesGrid);

		ArrayList<Node> callsLeveltop = new ArrayList<>();
		ArrayList<Node> nodeObjects = new ArrayList<>();
		
		for( Node rootNode : appesNodesRoot)
		{
			if( rootNode instanceof NodeImplicitParameter)
			{
				nodeObjects.add(rootNode);
				for( Node callNode : ((NodeImplicitParameter) rootNode).getChildren())
				{
					if( getCaller(callNode) == null )
					{
						callsLeveltop.add(callNode);
					}
				}
			}
		}
		
		for( Edge edge : appesEdges)
		{
			if(edge instanceof EdgeCall)
			{
				Node end = edge.getNodeEnd();
				if(end instanceof NodeCall)
				{
					((NodeCall)end).setSignaled(((EdgeCall)edge).signal());
				}
			}
		}
		layoutObjectheight(callsLeveltop, nodeObjects, appesGraphics2D, appesGrid);
	}
	

	private void layoutObjectheight(ArrayList<Node> pCallsTopLevel, ArrayList<Node> appesObjects, Graphics2D appesGraphics2D, Grid appesGrid)
	{
		double top = 0;
		for(Node node : appesObjects)
		{
			node.AppCommandtranslate(0, -node.getBounds().getY());
			top = Math.max(top, ((NodeImplicitParameter)node).getTopRectangle().getHeight());
		}

		for(Node node : pCallsTopLevel )
		{
			node.layout(this, appesGraphics2D, appesGrid);
		}

		for(Node node : appesNodesRoot)
		{
			if( node instanceof NodeImplicitParameter)
			{
				for( Node callNode : ((NodeImplicitParameter) node).getChildren())
				{
					top = Math.max(top, callNode.getBounds().getY() + callNode.getBounds().getHeight());
				}
			}
		}

		top += NodeCall.YGAP_CALL;

		for( Node node : appesObjects )
		{
			Rectangle2D bounds = node.getBounds();
			((NodeImplicitParameter)node).setBounds(new Rectangle2D.Double(bounds.getX(),
					bounds.getY(), bounds.getWidth(), top - bounds.getY()));         
		}
	}

	@Override
	public void draw(Graphics2D appesGraphics2D, Grid appesGrid)
	{
		layout(appesGraphics2D, appesGrid);
		super.draw(appesGraphics2D, appesGrid);
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
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("sequence.extension");
	}

	@Override
	public String getDescription() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("sequence.name");
	}
}





