

package Epp_UML_Editeur.Graphe;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import Epp_UML_Editeur.LeFramework.Direction;
import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodesPackage extends NodeRectangular implements NodeParent, ChildNode
{
	private static final int TOP_WIDTH_DEFAULT = 60;
	private static final int TOP_HEIGHT_DEFAULT = 20;
	private static final int WIDTH_DEFAULT_AP = 100;
	private static final int HEIGHT_DEFAULT = 80;
	private static final int GAP_NAME_APP = 3;
	private static final int X = 5;
	private static final int Y = 5;
	   
	private static JLabel label = new JLabel();

	private String appsName;
	private MultiLigne appsContents;
	private Rectangle2D appsTop;
	private Rectangle2D appsBottom;
	private ArrayList<ChildNode> appsNodesContained;
	private NodeParent appsContainer;
	   

	public NodesPackage()
	{
		appsName = "";
		appsContents = new MultiLigne();
		setBounds(new Rectangle2D.Double(0, 0, WIDTH_DEFAULT_AP, HEIGHT_DEFAULT));
		appsTop = new Rectangle2D.Double(0, 0, TOP_WIDTH_DEFAULT, TOP_HEIGHT_DEFAULT);
		appsBottom = new Rectangle2D.Double(0, TOP_HEIGHT_DEFAULT, WIDTH_DEFAULT_AP, HEIGHT_DEFAULT - TOP_HEIGHT_DEFAULT);
		appsNodesContained = new ArrayList<>();
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		Rectangle2D bounds = getBounds();

		label.setText("<html>" + appsName + "</html>");
		label.setFont(appesGraphics2D.getFont());
		Dimension d = label.getPreferredSize();
		label.setBounds(0, 0, d.width, d.height);

		appesGraphics2D.draw(appsTop);

		double textX = bounds.getX() + GAP_NAME_APP;
		double textY = bounds.getY() + (appsTop.getHeight() - d.getHeight()) / 2;
      
		appesGraphics2D.translate(textX, textY);
		label.paint(appesGraphics2D);
		appesGraphics2D.translate(-textX, -textY);
     
		appesGraphics2D.draw(appsBottom);
		appsContents.draw(appesGraphics2D, appsBottom);
	}
   
	@Override
	public Shape getShape()
	{
		GeneralPath path = new GeneralPath();
		path.append(appsTop, false);
		path.append(appsBottom, false);
		return path;
	}
	

	public Point2D getTopRightCorner()
	{
		return new Point2D.Double(appsBottom.getMaxX(), appsBottom.getMinY());
	}
	
	@Override
	public Point2D getConnectionPoint(Direction appDirections)
	{
		Point2D connectionPoint = super.getConnectionPoint(appDirections);
		if( connectionPoint.getY() < appsBottom.getMinY() && appsTop.getMaxX() < connectionPoint.getX() )
		{

			double delta = appsTop.getHeight() * (connectionPoint.getX() - getBounds().getCenterX()) * 2 /
					getBounds().getHeight();
			double newX = connectionPoint.getX() - delta;
			if( newX < appsTop.getMaxX() )
			{
				newX = appsTop.getMaxX() + 1;
			}
			return new Point2D.Double(newX, appsBottom.getMinY());
		}
		else
		{
			return connectionPoint;
		}
	}

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		label.setText("<html>" + appsName + "</html>");
		label.setFont(appsGraphics2D.getFont());
		Dimension d = label.getPreferredSize();
		double topWidth = Math.max(d.getWidth() + 2 * GAP_NAME_APP, TOP_WIDTH_DEFAULT);
		double topHeight = Math.max(d.getHeight(), TOP_HEIGHT_DEFAULT);
		
		Rectangle2D childBounds = null;
		for( ChildNode child : getChildren() )
		{
			child.layout(appsGraphs, appsGraphics2D, appLagrille);
			if( childBounds == null )
			{
				childBounds = child.getBounds();
			}
			else
			{
				childBounds.add(child.getBounds());
			}
		}
		
		Rectangle2D contentsBounds = appsContents.getBounds(appsGraphics2D);
		
		if( childBounds == null )
		{
			setBounds( new Rectangle2D.Double(getBounds().getX(), getBounds().getY(), 
					computeWidth(topWidth, contentsBounds.getWidth(), 0.0),
					computeHeight(topHeight, contentsBounds.getHeight(), 0.0)));
		}
		else
		{
			setBounds( new Rectangle2D.Double(childBounds.getX() - X, childBounds.getY() - topHeight - Y,
					computeWidth(topWidth, contentsBounds.getWidth(), childBounds.getWidth() + 2 * X),
					computeHeight(topHeight, contentsBounds.getHeight(), childBounds.getHeight() + 2 * Y)));
		}
		
		Rectangle2D b = getBounds();
		appsTop = new Rectangle2D.Double(b.getX(), b.getY(), topWidth, topHeight);
		appsBottom = new Rectangle2D.Double(b.getX(), b.getY() + topHeight, b.getWidth(), b.getHeight() - topHeight);
	}
	
	private double computeWidth(double pTopWidth, double pContentWidth, double pChildrenWidth)
	{
		return max(WIDTH_DEFAULT_AP, pTopWidth + WIDTH_DEFAULT_AP - TOP_WIDTH_DEFAULT, pContentWidth, pChildrenWidth);
	}
	
	private double computeHeight(double pTopHeight, double pContentHeight, double pChildrenHeight)
	{
		return pTopHeight + max( HEIGHT_DEFAULT - TOP_HEIGHT_DEFAULT, pContentHeight, pChildrenHeight);
	}
	

	private static double max(double ... pNumbers)
	{
		double maximum = Double.MIN_VALUE;
		for(double number : pNumbers)
		{
			if(number > maximum)
			{
				maximum = number;
			}
		}
		return maximum;
	}
	

	public void setName(String appsName)
	{
		this.appsName = appsName;
	}


	public String getName()
	{
		return appsName;
	}


	public void setContents(MultiLigne appsContents)
	{
		this.appsContents = appsContents;
	}
	
	@Override
	public void AppCommandtranslate(double pDX, double pDY)
	{
		super.AppCommandtranslate(pDX, pDY);
		
		appsTop = (Rectangle2D) appsTop.clone();
		appsBottom = (Rectangle2D) appsBottom.clone();
		appsTop.setFrame(appsTop.getX() + pDX, appsTop.getY() + pDY, appsTop.getWidth(), appsTop.getHeight());
		appsBottom.setFrame(appsBottom.getX() + pDX, appsBottom.getY() + pDY, appsBottom.getWidth(), appsBottom.getHeight());
		for(Node childNode : getChildren())
        {
        	childNode.AppCommandtranslate(pDX, pDY);
        }   
	}


	public MultiLigne getContents()
	{
		return appsContents;
	}

	@Override
	public NodesPackage clone()
	{
		NodesPackage appscloned = (NodesPackage)super.clone();
		appscloned.appsContents = appsContents.clone();
		appscloned.appsNodesContained = new ArrayList<>();
		for( ChildNode child : appsNodesContained)
		{

			ChildNode clonedChild = (ChildNode) child.clone();
			clonedChild.setNodeParent(appscloned);
			appscloned.appsNodesContained.add(clonedChild);
		}
		return appscloned;
	}
	
	@Override
	public NodeParent getNodeParent()
	{
		return appsContainer;
	}

	@Override
	public void setNodeParent(NodeParent pNode)
	{
		assert pNode instanceof NodesPackage || pNode == null;
		appsContainer = pNode;
	}

	@Override
	public List<ChildNode> getChildren()
	{
		return appsNodesContained;
	}

	@Override
	public void addChild(int appesIndex, ChildNode appesNode)
	{
		assert appesNode != null;
		assert appesIndex >=0 && appesIndex <= appsNodesContained.size();
		NodeParent oldParent = appesNode.getNodeParent();
		if (oldParent != null)
		{
			oldParent.ChildNodeDeleted(appesNode);
		}
		appsNodesContained.add(appesIndex, appesNode);
		appesNode.setNodeParent(this);
	}

	@Override
	public void addChild(ChildNode appesNode)
	{
		assert appesNode != null;
		addChild(appsNodesContained.size(), appesNode);
	}

	@Override
	public void ChildNodeDeleted(ChildNode appesNode)
	{
		appsNodesContained.remove(appesNode);
		appesNode.setNodeParent(null);
	}
	

	public static void setPersistenceDelegate(Encoder appsEncoder)
	{
		appsEncoder.setPersistenceDelegate(NodesPackage.class, new DefaultPersistenceDelegate() {
			protected void initialize(Class<?> pType, Object pOldInstance, Object pNewInstance, Encoder pOut) {
				super.initialize(pType, pOldInstance, pNewInstance, pOut);
				for (ChildNode node : ((NodeParent) pOldInstance).getChildren()) {
					pOut.writeStatement(new Statement(pOldInstance, "addChild", new Object[]{node}));
				}
			}
		});
	}
}
