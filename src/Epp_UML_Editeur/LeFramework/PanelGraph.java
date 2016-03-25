
package Epp_UML_Editeur.LeFramework;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.NodeImplicitParameter;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.ChildNode;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.ElementGraph;
import Epp_UML_Editeur.Graphe.NodeObject;
import Epp_UML_Editeur.Graphe.NodesPackage;
import Epp_UML_Editeur.Graphe.NodeParent;


@SuppressWarnings("serial")
public class PanelGraph extends JPanel
{
	private enum DragMode 
	{NONE_DRAG_PANEL, MOVE_DRAG_PANEL, RUBBERBAND_DRAG, LASSO_DRAG
	}
	
	private static final int THRESHOLD_CONNECT = 8;
	private static final Color COLOR_GRABBER = new Color(77, 115, 153);
	private static final Color COLOR_FILL_GRABBER = new Color(173, 193, 214);
	private static final Color COLOR_FILL_TRANSPARENT_GRABBER = new Color(173, 193, 214, 75);
	
	private Graph appesGraph;
	private BarTool appesSideBar;
	private double appesZoom;
	private boolean appesHideGrid;
	private boolean appesModified;
	private ListSelection appesElementsSelected = new ListSelection();
	private Point2D appesMousePointLast;
	private Point2D appesMousePointDown;
	private DragMode appesDragMode;
	private UndoManager appesUndo = new UndoManager();
	private ListenerModificationGraph appesListenerMod = new ListenerModificationGraph(appesUndo);
	

	public PanelGraph(BarTool appesSideBar)
	{
		appesZoom = 1;
		this.appesSideBar = appesSideBar;
		setBackground(Color.WHITE);
		addMouseListener(new MouseListenerPanelGraph());
		addMouseMotionListener(new GraphPanelMouseMotionListener());
	}


	public void selectedEdit()
	{
		Object edited = appesElementsSelected.getLastSelected();
		if( edited == null )
		{
			return;
		}
		appesListenerMod.ChangePropertytrack(appesGraph, edited);
		SheetProperty sheet = new SheetProperty(edited);
		if(sheet.empty())
		{
			return;
		}
		sheet.ChangeListeneradded(new ChangeListener() {
			public void stateChanged(ChangeEvent pEvent) {
				appesGraph.layout();
				repaint();
			}
		});
		 String[] options = {"OK"};
		 JOptionPane.showOptionDialog(this, sheet, 
		            ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings").getString("dialog.properties"),
		            		JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		appesListenerMod.finishPropertyChange(appesGraph, edited);
		setModified(true);
	}


	public void removeSelected()
	{
		appesUndo.startTracking();
		Stack<Node> nodes = new Stack<Node>();
		for( ElementGraph element : appesElementsSelected)
		{
			if (element instanceof Node)
			{
				for(Edge ele : appesGraph.getNodeEdges((Node) element))
				{
					appesGraph.edgeDeleted(ele);
				}
				nodes.add((Node) element);
			}
			else if(element instanceof Edge)
			{
				appesGraph.edgeDeleted((Edge) element);
			}
		}
		while(!nodes.empty())
		{
			appesGraph.deleteNode(nodes.pop());
		}
		appesUndo.endTracking();
		if(appesElementsSelected.size() > 0)
		{
			setModified(true);
		}
		repaint();
	}



	public void layoutGraph()
	{
		appesGraph.layout();
	}
	

	public Graph getGraph()
	{
		return appesGraph;
	}
	


	public void ListeningstartCompound()
	{
		appesUndo.startTracking();
	}
	

	public void ListeningendCompound()
	{
		appesUndo.endTracking();
	}
	

	public void undo()
	{
		appesUndo.commandUndo();
		repaint();
	}
	

	public void redo()
	{
		appesUndo.commandredo();
		repaint();
	}


	public void setGraph(Graph appesGraph)
	{
		this.appesGraph = appesGraph;
		this.appesGraph.addModificationListener(appesListenerMod);
		setModified(false);
		revalidate();
		repaint();
	}


	@Override
	public void paintComponent(Graphics pGraphics)
	{
		super.paintComponent(pGraphics);
		Graphics2D na2 = (Graphics2D) pGraphics;
		na2.scale(appesZoom, appesZoom);
		Rectangle2D abounds = getBounds();
		Rectangle2D graphBounds = appesGraph.getBounds();
		if(!appesHideGrid)
		{
			Grid.draw(na2, new Rectangle2D.Double(0, 0, Math.max(abounds.getMaxX() / appesZoom, graphBounds.getMaxX()),
				   Math.max(abounds.getMaxY() / appesZoom, graphBounds.getMaxY())));
		}
		appesGraph.draw(na2, new Grid());

		Set<ElementGraph> toBeRemoved = new HashSet<>();
		for(ElementGraph selected : appesElementsSelected)
		{
			if(!appesGraph.contains(selected))
			{
				toBeRemoved.add(selected);
			}
			else if(selected instanceof Node)
			{
				Rectangle2D grabberBounds = ((Node) selected).getBounds();
				drawGrabber(na2, grabberBounds.getMinX(), grabberBounds.getMinY());
				drawGrabber(na2, grabberBounds.getMinX(), grabberBounds.getMaxY());
				drawGrabber(na2, grabberBounds.getMaxX(), grabberBounds.getMinY());
				drawGrabber(na2, grabberBounds.getMaxX(), grabberBounds.getMaxY());
			}
			else if(selected instanceof Edge)
			{
				Line2D line = ((Edge) selected).getPointsConnection();
				drawGrabber(na2, line.getX1(), line.getY1());
				drawGrabber(na2, line.getX2(), line.getY2());
			}
		}

		for( ElementGraph element : toBeRemoved )
		{
			appesElementsSelected.remove(element);
		}                 
      
		if(appesDragMode == DragMode.RUBBERBAND_DRAG)
		{
			Color oldColor = na2.getColor();
			na2.setColor(COLOR_GRABBER);
			na2.draw(new Line2D.Double(appesMousePointDown, appesMousePointLast));
			na2.setColor(oldColor);
		}      
		else if(appesDragMode == DragMode.LASSO_DRAG)
		{
			Color oldColor = na2.getColor();
			na2.setColor(COLOR_GRABBER);
			double x1 = appesMousePointDown.getX();
			double y1 = appesMousePointDown.getY();
			double x2 = appesMousePointLast.getX();
			double y2 = appesMousePointLast.getY();
			Rectangle2D.Double lasso = new Rectangle2D.Double(Math.min(x1, x2), 
					Math.min(y1, y2), Math.abs(x1 - x2) , Math.abs(y1 - y2));
			na2.draw(lasso);
			na2.setColor(COLOR_FILL_TRANSPARENT_GRABBER);
			na2.fill(lasso);
			na2.setColor(oldColor);
		}      
	}




	public static void drawGrabber(Graphics2D appesGraphics2D, double px, double py)
	{
		final int size = 6;
		Color oldColor = appesGraphics2D.getColor();
		appesGraphics2D.setColor(COLOR_GRABBER);
		appesGraphics2D.drawRect((int) (px - size / 2), (int) (py - size / 2), size, size);
		appesGraphics2D.setColor(COLOR_FILL_GRABBER);
		appesGraphics2D.fillRect((int) (px - size / 2) + 1, (int) (py - size / 2) + 1, size - 1, size - 1);
		appesGraphics2D.setColor(oldColor);
	}

	@Override
	public Dimension getPreferredSize()
	{
		Rectangle2D bounds = appesGraph.getBounds();
		return new Dimension((int) (appesZoom * bounds.getMaxX()), (int) (appesZoom * bounds.getMaxY()));
	}



	public void changeZoom(int appesSteps)
	{
      final double factor = Math.sqrt(2);
      for(int i = 1; i <= appesSteps; i++)
      {
    	  appesZoom *= factor;
      }
      for(int i = 1; i <= -appesSteps; i++)
      {
    	  appesZoom /= factor;
      }
      revalidate();
      repaint();
	}


	public boolean isModified()
	{	
		return appesModified;
	}


	public void setModified(boolean appesModified)
	{
		this.appesModified = appesModified;

		aFramGraph graphFrame = getFrame();
		if(graphFrame != null)
		{
			graphFrame.setTitle(this.appesModified);
		}
	}
	

	private aFramGraph getFrame()
	{
		Component aprent = this;
		do
		{
			aprent = aprent.getParent();
		}
		while(aprent != null && !(aprent instanceof aFramGraph));
		return (aFramGraph) aprent;
	}
   

	public void setHideGrid(boolean appesHideGrid)
	{
		this.appesHideGrid = appesHideGrid;
		repaint();
	}


	public boolean getHideGrid()
	{
		return appesHideGrid;
	}
	

	public ListSelection getListSelection()
	{
		return appesElementsSelected;
	}
	

	public void setSelectionList(ListSelection appesListSelection)
	{
		appesElementsSelected = appesListSelection;
	}



	public boolean switchToSelectException(Node appesNode)
	{
		if(appesNode instanceof NodesPackage || appesNode instanceof NodeImplicitParameter || appesNode instanceof NodeObject)
		{
			return true;
		}
		return false;
	}
	
	private class MouseListenerPanelGraph extends MouseAdapter
	{	

		private void setSelection(ElementGraph appesElement)
		{
			appesElementsSelected.set(appesElement);
			for( Edge edge : appesGraph.getEdges() )
			{
				if( hasSelectedParent(edge.getNodeStart()) && hasSelectedParent(edge.getNodeEnd()))
				{
					appesElementsSelected.add(edge);
				}
			}
			appesElementsSelected.add(appesElement);
		}
		

		private void addToSelection(ElementGraph appesElement)
		{
			appesElementsSelected.add(appesElement);
			for( Edge edge : appesGraph.getEdges() )
			{
				if( hasSelectedParent(edge.getNodeStart()) && hasSelectedParent(edge.getNodeEnd()))
				{
					appesElementsSelected.add(edge);
				}
			}
			appesElementsSelected.add(appesElement);
		}
		

		private boolean hasSelectedParent(Node appesNode)
		{
			if( appesNode == null )
			{
				return false;
			}
			else if( appesElementsSelected.contains(appesNode) )
			{
				return true;
			}
			else if( appesNode instanceof ChildNode )
			{
				return hasSelectedParent( ((ChildNode)appesNode).getNodeParent() );
			}
			else
			{
				return false;
			}
		}
		
		private boolean isCtrl(MouseEvent pEvent)
		{
			return (pEvent.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
		}
		
		private Point2D getMousePoint(MouseEvent pEvent)
		{
			return new Point2D.Double(pEvent.getX() / appesZoom, pEvent.getY() / appesZoom);
		}
		

		private ElementGraph getSelectedElement(MouseEvent pEvent)
		{
			Point2D mousePoint = getMousePoint(pEvent);
			ElementGraph element = appesGraph.findEdge(mousePoint);
			if( element == null )
			{
				element = appesGraph.findNode(mousePoint);
			}
			return element;
		}
		
		private void handleSelection(MouseEvent pEvent)
		{
			ElementGraph element = getSelectedElement(pEvent);
			if(element != null)
			{
				if( isCtrl(pEvent) )
				{
					if(!appesElementsSelected.contains(element))
					{
						addToSelection(element);
					}
					else
					{
						appesElementsSelected.remove(element);
					}
				}
				else if( !appesElementsSelected.contains(element))
				{

					setSelection(element);
				}
				appesDragMode = DragMode.MOVE_DRAG_PANEL;
				appesListenerMod.startTrackingMove(appesGraph, appesElementsSelected);
			}
			else
			{
				if(!isCtrl(pEvent)) 
				{
					appesElementsSelected.clearSelection();
				}
				appesDragMode = DragMode.LASSO_DRAG;
			}
		}
		
		private void handleDoubleClick(MouseEvent pEvent)
		{
			ElementGraph element = getSelectedElement(pEvent);
			if( element != null )
			{
				setSelection(element);
				selectedEdit();
			}
			else
			{
				final Point2D mousePoint = getMousePoint(pEvent);
				appesSideBar.showPopup(PanelGraph.this, mousePoint);
			}
		}
		
		private void handleNodeCreation(MouseEvent appesEvent)
		{
			Node nouvNode = ((Node) appesSideBar.getSelectedTool()).clone();
			boolean added = appesGraph.add(nouvNode, getMousePoint(appesEvent));
			if(added)
			{
				setModified(true);
				setSelection(nouvNode);
			}
			else
			{
				handleSelection(appesEvent);
			}
		}
		
		private void handleEdgeStart(MouseEvent apespEvent)
		{
			ElementGraph element = getSelectedElement(apespEvent);
			if(element != null && element instanceof Node ) 
			{
				appesDragMode = DragMode.RUBBERBAND_DRAG;
			}
		}
		

		private ElementGraph getTool(MouseEvent appesEvent)
		{
			ElementGraph tool = appesSideBar.getSelectedTool();
			ElementGraph selected = getSelectedElement(appesEvent);
			
			if(tool !=null && tool instanceof Node)
			{
				if( selected != null && selected instanceof Node )
				{
					if(!(tool instanceof ChildNode && selected instanceof NodeParent))
					{
						appesSideBar.setToolToBeSelect();
						tool = null;
					}
				}
			}	
			return tool;
		}
		
		@Override
		public void mousePressed(MouseEvent appesEvent)
		{
			ElementGraph tool = getTool(appesEvent);

			if(appesEvent.getClickCount() > 1 || (appesEvent.getModifiers() & InputEvent.BUTTON1_MASK) == 0)
			{  
				handleDoubleClick(appesEvent);
			}
			else if(tool == null)
			{
				handleSelection(appesEvent);
			}
			else if(tool instanceof Node)
			{
				handleNodeCreation(appesEvent);
			}
			else if(tool instanceof Edge)
			{
				handleEdgeStart(appesEvent);
			}
			appesMousePointLast = getMousePoint(appesEvent);
			appesMousePointDown = appesMousePointLast;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent pEvent)
		{
			Point2D mousePoint = new Point2D.Double(pEvent.getX() / appesZoom, pEvent.getY() / appesZoom);
			Object tool = appesSideBar.getSelectedTool();
			if(appesDragMode == DragMode.RUBBERBAND_DRAG)
			{
				Edge prototype = (Edge) tool;
				Edge nouvEdge = (Edge) prototype.clone();
				if(mousePoint.distance(appesMousePointDown) > THRESHOLD_CONNECT && appesGraph.connect(nouvEdge, appesMousePointDown, mousePoint))
				{
					setModified(true);
					setSelection(nouvEdge);
				}
			}
			else if(appesDragMode == DragMode.MOVE_DRAG_PANEL)
			{
				appesGraph.layout();
				setModified(true);
				appesListenerMod.MoveendTracking(appesGraph, appesElementsSelected);
			}
			appesDragMode = DragMode.NONE_DRAG_PANEL;
			revalidate();
			repaint();
		}
	}
	
	private class GraphPanelMouseMotionListener extends MouseMotionAdapter
	{
		@Override
		public void mouseDragged(MouseEvent pEvent)
		{
			Point2D mousePoint = new Point2D.Double(pEvent.getX() / appesZoom, pEvent.getY() / appesZoom);
			boolean isCtrl = (pEvent.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0; 

			if(appesDragMode == DragMode.MOVE_DRAG_PANEL && appesElementsSelected.getLastNode()!=null)
			{               
				Node lastNode = appesElementsSelected.getLastNode();
				Rectangle2D bounds = lastNode.getBounds();
				double dx = mousePoint.getX() - appesMousePointLast.getX();
				double dy = mousePoint.getY() - appesMousePointLast.getY();
                   

				for( ElementGraph selected : appesElementsSelected)
				{
					if(selected instanceof Node)
					{
						Node n = (Node) selected;
						bounds.add(n.getBounds());
					}
				}
				dx = Math.max(dx, -bounds.getX());
				dy = Math.max(dy, -bounds.getY());
            
				for( ElementGraph selected : appesElementsSelected)
				{
					if(selected instanceof ChildNode)
					{
						ChildNode n = (ChildNode) selected;
						if (!appesElementsSelected.parentContained(n))
						{
							n.AppCommandtranslate(dx, dy);
						}	
					}
					else if(selected instanceof Node)
					{
						Node n = (Node) selected;
						n.AppCommandtranslate(dx, dy);
					}
				}
			}
			else if(appesDragMode == DragMode.LASSO_DRAG)
			{
				double x1 = appesMousePointDown.getX();
				double y1 = appesMousePointDown.getY();
				double x2 = mousePoint.getX();
				double y2 = mousePoint.getY();
				Rectangle2D.Double lasso = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2) , Math.abs(y1 - y2));
				for( Node node : appesGraph.getRootNodes() )
				{
					selectNode(isCtrl, node, lasso);
				}

				for (Edge edge: appesGraph.getEdges())
				{
					if(!isCtrl && !lasso.contains(edge.getBounds()))
					{
						appesElementsSelected.remove(edge);
					}
					else if(lasso.contains(edge.getBounds()))
					{
						if(appesElementsSelected.transitivelyContains(edge.getNodeStart()) && appesElementsSelected.transitivelyContains(edge.getNodeEnd()))
						{
							appesElementsSelected.add(edge);
						}
					}
				}
			}
			appesMousePointLast = mousePoint;
			repaint();
		}
		
		private void selectNode( boolean appesCtrl, Node appesNode, Rectangle2D.Double appesLasso )
		{
			if(!appesCtrl && !appesLasso.contains(appesNode.getBounds()))
			{
				appesElementsSelected.remove(appesNode);
			}
			else if(appesLasso.contains(appesNode.getBounds()))
			{
				appesElementsSelected.add(appesNode);
			}
			if( appesNode instanceof NodeParent)
			{
				for( ChildNode child : ((NodeParent) appesNode).getChildren() )
				{
					selectNode(appesCtrl, child, appesLasso);
				}
			}
		}
	}
}
