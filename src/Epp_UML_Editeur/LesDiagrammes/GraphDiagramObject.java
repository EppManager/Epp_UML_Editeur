

package Epp_UML_Editeur.LesDiagrammes;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Epp_UML_Editeur.Graphe.NodeField;
import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.LeFramework.MultiLigne;
import Epp_UML_Editeur.Graphe.ChildNode;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.EdgeNote;
import Epp_UML_Editeur.Graphe.NodeNote;
import Epp_UML_Editeur.Graphe.EdgeCollaborationObject;
import Epp_UML_Editeur.Graphe.NodeObject;
import Epp_UML_Editeur.Graphe.EdgeReferenceObject;


public class GraphDiagramObject extends Graph
{
	private static final Node[] PROTOTYP_ES_NODE = new Node[3];
	private static final Edge[] PROTOTYP_ES_EDGE_ = new Edge[3];
	
	static
	{
		PROTOTYP_ES_NODE[0] = new NodeObject();
	      
		NodeField f = new NodeField();
	    MultiLigne fn = new MultiLigne();
	    fn.setText("name");
	    f.setName(fn);
	    MultiLigne fv = new MultiLigne();
	    fv.setText("value");
	    f.setValue(fv);
	    PROTOTYP_ES_NODE[1] = f;
	      
	    PROTOTYP_ES_NODE[2] = new NodeNote();
	    
	    PROTOTYP_ES_EDGE_[0] = new EdgeReferenceObject();
	    
	    PROTOTYP_ES_EDGE_[1] = new EdgeCollaborationObject();
	    PROTOTYP_ES_EDGE_[2] = new EdgeNote();
	}
	
	@Override
	public boolean appsConnectElements(Edge appesEdge, Node appesNode1, Node appesNode2, Point2D appesPoint2)
	{
		if( !super.appsConnectElements(appesEdge, appesNode1, appesNode2, appesPoint2) )
		{
			return false;
		}
		if( appesNode1 instanceof NodeObject)
		{
			return (appesEdge instanceof EdgeCollaborationObject && appesNode2 instanceof NodeObject) ||
					(appesEdge instanceof EdgeNote && appesNode2 instanceof NodeNote);
		}
		if( appesNode1 instanceof NodeField)
		{
			return appesEdge instanceof EdgeReferenceObject && appesNode2 instanceof NodeObject;
		}
		return true;
	}
	

	@Override
	public boolean add(Node appesNode, Point2D appesPoint)
	{
		if( appesNode instanceof NodeField)
		{
			NodeObject object = (NodeObject) ((NodeField) appesNode).getNodeParent();
			
			if( object == null )
			{
				object = findObject((NodeField)appesNode, appesPoint);
			}
			if( object != null )
			{
				object.addChild((ChildNode)appesNode);
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
			super.add(appesNode, appesPoint);
			return true;
		}
	}
	

	private NodeObject findObject(NodeField appesNode, Point2D appesPoint)
	{
		ArrayList<NodeObject> objectacndidates = new ArrayList<>();
		for( Node appesnode : appesNodesRoot)
		{
			if( appesnode == appesNode )
			{
				continue;
			}
			else if( appesNode.getNodeParent() == appesnode )
			{
				return (NodeObject)appesnode;
			}
			else if( appesnode.contains(appesPoint) && childNodecanAdded(appesnode, appesNode))
			{
				objectacndidates.add((NodeObject) appesnode);
			}
		}


		if( objectacndidates.size() > 0 )
		{
			return objectacndidates.get(objectacndidates.size()-1);
		}
		else
		{
			return null;
		}
	}
	
	private static boolean childNodecanAdded(Node appesNparent, Node pPotentialChild)
	{
		if( appesNparent instanceof NodeObject)
		{
			return pPotentialChild instanceof NodeField;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void addEdge(Node appesOrigin, Edge appesEdge, Point2D appesPoint1, Point2D appesPoint2)
	{
		if( appesOrigin instanceof NodeField)
		{
			appesListenerMode.ChangePropertytrack(this, appesOrigin);
			((NodeField)appesOrigin).getValue().setText("");
			appesListenerMode.finishPropertyChange(this, appesOrigin);
		}
	}
	
	@Override
	public Node[] getPrototypNodes()
	{
		return PROTOTYP_ES_NODE;
	}

	@Override
	public Edge[] getPrototypEdges()
	{
		return PROTOTYP_ES_EDGE_;
	}   
	
	@Override
	public String getFileExtension() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("object.extension");
	}

	@Override
	public String getDescription() 
	{
		return ResourceBundle.getBundle("Epp_UML_Editeur.UMLEditorStrings").getString("object.name");
	}
}





