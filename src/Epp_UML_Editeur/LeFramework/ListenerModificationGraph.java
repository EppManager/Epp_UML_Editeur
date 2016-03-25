
package Epp_UML_Editeur.LeFramework;

import java.awt.geom.Rectangle2D;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import Epp_UML_Editeur.LesCommandes.CommandAdRemoveEdge;
import Epp_UML_Editeur.LesCommandes.DcmandCmpound;
import Epp_UML_Editeur.LesCommandes.MoveCommandApp;
import Epp_UML_Editeur.LesCommandes.ChangePropertiesComd;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.ElementGraph;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.LesCommandes.CommandAdRemoveNode;


public class ListenerModificationGraph
{
	private UndoManager appesManagerUndo;
	private Node[] appesNodesSelection;
	private Rectangle2D[] appesBoundsSelection;
	private Object[] appesValuesProperty;


	public ListenerModificationGraph(UndoManager appesUndo)
	{
		appesManagerUndo = appesUndo;
	}



	public ListenerModificationGraph()
	{
		appesManagerUndo = new UndoManager();
	}


	public void nodeAdded(Graph appesGraph, Node appesNode)
	{
		CommandAdRemoveNode nb = new CommandAdRemoveNode(appesGraph, appesNode, true);
		appesManagerUndo.add(nb);
	}


	public void nodeRemoved(Graph appesGraph, Node appesNode)
	{
		CommandAdRemoveNode ba = new CommandAdRemoveNode(appesGraph, appesNode, false);
		appesManagerUndo.add(ba);
	}


	public void Movednode(Graph appesGraph, Node appesNode, double dX, double dY)
	{
		MoveCommandApp nc = new MoveCommandApp(appesGraph, appesNode, dX, dY);
		appesManagerUndo.add(nc);
	}


	public void startTrackingMove(Graph appesGraph, ListSelection appesElementsSelected)
	{
		appesNodesSelection = new Node[appesElementsSelected.size()];
		appesBoundsSelection = new Rectangle2D[appesElementsSelected.size()];
		int i = 0;
		for(ElementGraph ele : appesElementsSelected)
		{
			if(ele instanceof Node)
			{
				appesNodesSelection[i] = (Node) ele;
				appesBoundsSelection[i] = appesNodesSelection[i].getBounds();
				i++;
			}
		}
	}


	public void MoveendTracking(Graph appesGraph, ListSelection appesElementsSelected)
	{
		DcmandCmpound nn = new DcmandCmpound();
		Rectangle2D[] selectionBounds2 = new Rectangle2D[appesElementsSelected.size()];
		int i = 0;
		for(ElementGraph ele : appesElementsSelected)
		{
			if(ele instanceof Node)
			{
				selectionBounds2[i] = ((Node) ele).getBounds();
				i++;
			}
		}
		for(i = 0; i< appesNodesSelection.length && appesNodesSelection[i] != null; i++)
		{
			double dY = selectionBounds2[i].getY() - appesBoundsSelection[i].getY();
			double dX = selectionBounds2[i].getX() - appesBoundsSelection[i].getX();
			if (dX != 0 || dY != 0)
			{
				nn.add(new MoveCommandApp(appesGraph, appesNodesSelection[i], dX, dY));
			}
		}
		if (nn.size() > 0)
		{
			appesManagerUndo.add(nn);
		}
	}


	public void ChangePropertytrack(Graph appesGraph, Object appesEdited)
	{
		BeanInfo info;
		try 
		{
			info = Introspector.getBeanInfo(appesEdited.getClass());
			PropertyDescriptor[] oldDescriptors = info.getPropertyDescriptors().clone();
			appesValuesProperty = new Object[oldDescriptors.length];
			for(int i = 0; i< appesValuesProperty.length; i++)
			{
				final Method getter = oldDescriptors[i].getReadMethod();
				if (getter != null)
				{
					appesValuesProperty[i] = getter.invoke(appesEdited, new Object[] {});
					appesValuesProperty[i] = Cloneproperty(appesValuesProperty[i]);
				}
			}
		} 
		catch (IntrospectionException e) 
		{
			e.printStackTrace();
			return;
		} 
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
		{
			e.printStackTrace();
			return;
		}

	}


	public void finishPropertyChange(Graph appesGraph, Object appesEdited)
	{
		BeanInfo info;
		DcmandCmpound bm = new DcmandCmpound();
		try 
		{
			info = Introspector.getBeanInfo(appesEdited.getClass());
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors().clone();  
			for(int i = 0; i<descriptors.length; i++)
			{
				final Method getter = descriptors[i].getReadMethod();
				if(getter != null)
				{
					Object propVal = getter.invoke(appesEdited, new Object[] {});
					if (!propertyEquals(propVal, appesValuesProperty[i]))
					{
						Object oldPropValue = appesValuesProperty[i];
						Object propValue;
						propValue = Cloneproperty(propVal);
						bm.add(new ChangePropertiesComd(appesGraph, appesEdited, oldPropValue, propValue, i));
					}
				}
			}
		}
		catch (IntrospectionException e) 
		{
			e.printStackTrace();
			return;
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
		{
			e.printStackTrace();
			return;
		}
		finally
		{
			if (bm.size() > 0)
			{
				appesManagerUndo.add(bm);
			}
		}
	}


	public void edgeAdded(Graph appesGraph, Edge appesEdge)
	{
		CommandAdRemoveEdge ac = new CommandAdRemoveEdge(appesGraph, appesEdge, true);
		appesManagerUndo.add(ac);
	}


	public void edgeRemoved(Graph appesGraph, Edge appesEdge)
	{
		CommandAdRemoveEdge dc = new CommandAdRemoveEdge(appesGraph, appesEdge, false);
		appesManagerUndo.add(dc);
	}


	private Object Cloneproperty(Object appesObject)
	{
		Object temp = null;
		if(appesObject instanceof MultiLigne)
		{
			temp = ((MultiLigne) appesObject).clone();
		}



		if(temp != null)
		{
			return temp;
		}
		else
		{
			return appesObject;
		}
	}


	private boolean propertyEquals(Object appesObject1, Object appesObject2)
	{
		if(appesObject1 == null || appesObject1 == null)
		{
			return false;
		}
		if (appesObject1 instanceof MultiLigne && appesObject2 instanceof MultiLigne)
		{
			return ((MultiLigne) appesObject1).equalProperties((MultiLigne) appesObject2);
		}
		if (appesObject1 instanceof String && appesObject2 instanceof String)
		{
			return appesObject1.equals(appesObject2);
		}
		else
		{
			return appesObject1.equals(appesObject2);
		}
	}
	

	public void ListeningstartCompound()
	{
		appesManagerUndo.startTracking();
	}
	

	public void ListeningendCompound()
	{
		appesManagerUndo.endTracking();
	}

}
