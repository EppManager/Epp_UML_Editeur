
package Epp_UML_Editeur.Graphe;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;

import Epp_UML_Editeur.LeFramework.Direction;
import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodeImplicitParameter extends NodeRectangular implements NodeParent
{
	private static final int APP_DEFAULT_TOP_HEIGHT = 60;
	private static final int APP_DEFAULT_WIDTH = 80;
	private static final int APP_DEFAULT_HEIGHT = 120;
	
	private double appesTopHeight;
	private MultiLigne appesName;
	private List<ChildNode> appesCallNodes = new ArrayList<>();


	public NodeImplicitParameter()
	{
		appesName = new MultiLigne();
		appesName.setUnderlined(true);
		setBounds(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_HEIGHT));
		appesTopHeight = APP_DEFAULT_TOP_HEIGHT;
	}

	@Override
	public boolean contains(Point2D appesPoint)
	{
		Rectangle2D boundsRect2d = getBounds();
		return boundsRect2d.getX() <= appesPoint.getX() && appesPoint.getX() <= boundsRect2d.getX() + boundsRect2d.getWidth();
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		Rectangle2D topRect2d = getTopRectangle();
		appesGraphics2D.draw(topRect2d);
		appesName.draw(appesGraphics2D, topRect2d);
		double xmid = getBounds().getCenterX();
		Line2D line = new Line2D.Double(xmid, topRect2d.getMaxY(), xmid, getBounds().getMaxY());
		Stroke oldStroke = appesGraphics2D.getStroke();

		appesGraphics2D.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[] { 5.0f, 5.0f }, 0.0f));

		appesGraphics2D.draw(line);
		appesGraphics2D.setStroke(oldStroke);
	}


	public Rectangle2D getTopRectangle()
	{
		return new Rectangle2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), appesTopHeight);
	}

	@Override
	public Shape getShape()
	{ return getTopRectangle(); }
   
	@Override
	public Point2D getConnectionPoint(Direction appDirections)
	{
		if(appDirections.getX() > 0)
		{
			return new Point2D.Double(getBounds().getMaxX(), getBounds().getMinY() + appesTopHeight / 2);
		}
		else
		{
			return new Point2D.Double(getBounds().getX(), getBounds().getMinY() + appesTopHeight / 2);
		}
	}

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		Rectangle2D bel = appesName.getBounds(appsGraphics2D);
		bel.add(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_TOP_HEIGHT));
		Rectangle2D topRect2d = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(), bel.getWidth(), bel.getHeight());
		appLagrille.snap(topRect2d);
		setBounds(new Rectangle2D.Double(topRect2d.getX(), topRect2d.getY(), topRect2d.getWidth(), getBounds().getHeight()));
		appesTopHeight = topRect2d.getHeight();
	}


	public void setName(MultiLigne appesName)
	{
		this.appesName = appesName;
	}


	public MultiLigne getName()
	{
		return appesName;
	}

	@Override
	public NodeImplicitParameter clone()
	{
		NodeImplicitParameter cloned = (NodeImplicitParameter) super.clone();
		cloned.appesName = appesName.clone();
		cloned.appesCallNodes = new ArrayList<>();
		for( ChildNode child : appesCallNodes)
		{

			ChildNode clonedChildrenNode = (ChildNode) child.clone();
			clonedChildrenNode.setNodeParent(cloned);
			cloned.appesCallNodes.add(clonedChildrenNode);
		}
		return cloned;
	}
	
	@Override
	public List<ChildNode> getChildren()
	{
		return appesCallNodes;
	}

	@Override
	public void addChild(int appesIndex, ChildNode appesNode)
	{
		NodeParent oldParent = appesNode.getNodeParent();
		if (oldParent != null)
		{
			oldParent.ChildNodeDeleted(appesNode);
		}
		appesCallNodes.add(appesIndex, appesNode);
		appesNode.setNodeParent(this);
	}
	

	public void addChild(ChildNode appesChild, Point2D appesPoint2d)
	{
		int i = 0;
		while(i < appesCallNodes.size() && appesCallNodes.get(i).getBounds().getY() <= appesPoint2d.getY())
		{
			i++;
		}
		addChild(i, appesChild);
	}

	@Override
	public void addChild(ChildNode appesNode)
	{
		addChild(appesCallNodes.size(), appesNode);
	}

	@Override
	public void ChildNodeDeleted(ChildNode appesNode)
	{
		if (appesNode.getNodeParent() != this)
		{
			return;
		}
		appesCallNodes.remove(appesNode);
		appesNode.setNodeParent(null);
	}
	

	public static void setPersistenceDelegate(Encoder appesEncoder)
	{
		appesEncoder.setPersistenceDelegate(NodeImplicitParameter.class, new DefaultPersistenceDelegate() {
			protected void initialize(Class<?> pType, Object pOldInstance, Object pNewInstance, Encoder pOut) {
				super.initialize(pType, pOldInstance, pNewInstance, pOut);
				for (ChildNode node : ((NodeParent) pOldInstance).getChildren()) {
					pOut.writeStatement(new Statement(pOldInstance, "addChild", new Object[]{node}));
				}
			}
		});
	}
}
