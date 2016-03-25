

package Epp_UML_Editeur.Graphe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import Epp_UML_Editeur.LesDiagrammes.GraphDiagramSequence;
import Epp_UML_Editeur.LeFramework.Direction;
import Epp_UML_Editeur.LeFramework.Grid;


public class NodeCall extends NodeRectangular implements ChildNode
{
	public static final int YGAP_CALL = 20;

	private static final int WIDTH_DEFAULT = 16;
	private static final int HEIGHT_DEFAULT = 30;
	private static final int YGAP_MIN = 10;

	private NodeImplicitParameter appesImplicitParameter;
	private boolean appesSignaled;
	private boolean appesOpenBottom;


	public NodeCall()
	{
		setBounds(new Rectangle2D.Double(0, 0, WIDTH_DEFAULT, HEIGHT_DEFAULT));
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		Color oldColor = appesGraphics2D.getColor();
		appesGraphics2D.setColor(Color.WHITE);
		appesGraphics2D.fill(getBounds());
		appesGraphics2D.setColor(oldColor);
		if(appesOpenBottom)
		{
			Rectangle2D nab = getBounds();
			double x1 = nab.getX();
			double x2 = x1 + nab.getWidth();
			double y1 = nab.getY();
			double y3 = y1 + nab.getHeight();
			double y2 = y3 - YGAP_CALL;
			appesGraphics2D.draw(new Line2D.Double(x1, y1, x2, y1));
			appesGraphics2D.draw(new Line2D.Double(x1, y1, x1, y2));
			appesGraphics2D.draw(new Line2D.Double(x2, y1, x2, y2));
			Stroke oldStroke = appesGraphics2D.getStroke();

			appesGraphics2D.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 5.0f, 5.0f }, 0.0f));

			appesGraphics2D.draw(new Line2D.Double(x1, y2, x1, y3));
			appesGraphics2D.draw(new Line2D.Double(x2, y2, x2, y3));
			appesGraphics2D.setStroke(oldStroke);
		}
		else
		{
			appesGraphics2D.draw(getBounds());
		}
	}

	@Override
	public Point2D getConnectionPoint(Direction appDirections)
	{
		if(appDirections.getX() > 0)
		{
			return new Point2D.Double(getBounds().getMaxX(), getBounds().getMinY());
		}
		else
		{
			return new Point2D.Double(getBounds().getX(), getBounds().getMinY());
		}
	}


	@Override
	public void AppCommandtranslate(double pDX, double pDY)
	{
		super.AppCommandtranslate(pDX, pDY);

		if( getBounds().getY() < appesImplicitParameter.getTopRectangle().getMaxY() + YGAP_MIN)
		{
			setBounds(new Rectangle2D.Double(getBounds().getX(), appesImplicitParameter.getTopRectangle().getMaxY() + YGAP_MIN,
					getBounds().getWidth(), getBounds().getHeight()));
		}
	}

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		assert appesImplicitParameter != null;
		assert appsGraphs instanceof GraphDiagramSequence;
		GraphDiagramSequence graph = (GraphDiagramSequence) appsGraphs;


		AppCommandtranslate(midXCompute(appsGraphs) - getBounds().getCenterX(), 0);


		double bottomY = bottomYCompute(graph, appsGraphics2D, appLagrille);

		Rectangle2D boundsRect2d = getBounds();

		double heightMin = HEIGHT_DEFAULT;
		Edge returnEdge = graph.edgeFind(this, graph.getCaller(this));
		if(returnEdge != null)
		{
			Rectangle2D edgeBounds = returnEdge.getBounds();
			heightMin = Math.max(heightMin, edgeBounds.getHeight());
		}
		setBounds(new Rectangle2D.Double(boundsRect2d.getX(), boundsRect2d.getY(), boundsRect2d.getWidth(), Math.max(heightMin, bottomY - boundsRect2d.getY())));
	}
	

	private List<Node> getNodeCallees(Graph appesGraph)
	{
		List<Node> callees = new ArrayList<>();
		for( Edge edge : appesGraph.getEdges())
		{
			if( edge.getNodeStart() == this && edge instanceof EdgeCall)
			{
				if( edge.getNodeEnd() instanceof NodeImplicitParameter)
				{
					callees.add(0, edge.getNodeEnd());
				}
				else
				{
					callees.add(edge.getNodeEnd());
				}
			}
		}
		return callees;
	}


	private double midXCompute(Graph appesGraph)
	{
		double xnab = appesImplicitParameter.getBounds().getCenterX();


		for(NodeCall node = ((GraphDiagramSequence)appesGraph).getCaller(this); node != null && node != this;
				node = ((GraphDiagramSequence)appesGraph).getCaller(node))
		{
			if(node.appesImplicitParameter == appesImplicitParameter)
			{
				xnab += getBounds().getWidth() / 2;
			}
		}
		return xnab;
	}


	private double bottomYCompute(GraphDiagramSequence appesGraph, Graphics2D appesGraphics2D, Grid appesGrid)
	{

		double bottomY = getBounds().getY() + YGAP_CALL;

		for(Node node : getNodeCallees(appesGraph))
		{
			if(node instanceof NodeImplicitParameter)
			{
				node.AppCommandtranslate(0, bottomY - ((NodeImplicitParameter) node).getTopRectangle().getCenterY());
				bottomY += ((NodeImplicitParameter)node).getTopRectangle().getHeight() / 2 + YGAP_CALL;
			}
			else if(node instanceof NodeCall)
			{  
				Edge callEdge = appesGraph.edgeFind(this, node);

				if(callEdge != null)
				{
					bottomY += callEdge.getBounds().getHeight() - YGAP_CALL;
				}

				node.AppCommandtranslate(0, bottomY - node.getBounds().getY());
				node.layout(appesGraph, appesGraphics2D, appesGrid);
				if(((NodeCall) node).appesSignaled)
				{
					bottomY += YGAP_CALL;
				}
				else
				{
					bottomY += node.getBounds().getHeight() + YGAP_CALL;
				}
			}
		}
		if(appesOpenBottom)
		{
			bottomY += 2 * YGAP_CALL;
		}
		return bottomY;
	}


	public void setSignaled(boolean appesNewValue)
	{ 
		appesSignaled = appesNewValue;
	}


	public boolean isOpenBottom() 
	{ 
		return appesOpenBottom;
	}


	public void setOpenBottom(boolean appesNewValue)
	{ 
		appesOpenBottom = appesNewValue;
	}

	@Override
	public NodeCall clone()
	{
		NodeCall cloned = (NodeCall) super.clone();
		return cloned;
	}
	

	public NodeParent getNodeParent()
   	{ 
		return appesImplicitParameter;
	}


	public void setNodeParent(NodeParent appesNode)
	{
		assert appesNode instanceof NodeImplicitParameter || appesNode == null;
		appesImplicitParameter = (NodeImplicitParameter) appesNode;
	}
}
