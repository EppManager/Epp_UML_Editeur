

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.List;

import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodeObject extends NodeRectangular implements NodeParent
{
	private static final int APP_DEFAULT_WIDTH = 80;
	private static final int APP_DEFAULT_HEIGHT = 60;
	private static final int XGP = 5;
	private static final int YGP = 5;

	private double appesTopHeight;
	private MultiLigne appesName;
	private ArrayList<ChildNode> appesFields;


	public NodeObject()
	{
		appesName = new MultiLigne(true);
		appesName.setUnderlined(true);
		setBounds(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_HEIGHT));
		appesFields = new ArrayList<>();
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		Rectangle2D topRect2d = getTopRectangle();
		appesGraphics2D.draw(topRect2d);
		appesGraphics2D.draw(getBounds());
		appesName.draw(appesGraphics2D, topRect2d);
	}


	@Override
	public void AppCommandtranslate(double pDX, double pDY)
	{
		super.AppCommandtranslate(pDX, pDY);
		for (Node childNode : getChildren())
		{
			childNode.AppCommandtranslate(pDX, pDY);
		}   
	}    


	public Rectangle2D getTopRectangle()
	{
		return new Rectangle2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), appesTopHeight);
	}

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		Rectangle2D bel = appesName.getBounds(appsGraphics2D);
		bel.add(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_HEIGHT - YGP));
		double leftWidth = 0;
		double rightWidth = 0;
		List<ChildNode> pcfields = getChildren();
		double height = 0;
		if( pcfields.size() != 0 )
		{
			height = YGP;
		}
		for(int i = 0; i < pcfields.size(); i++)
		{
			NodeField nodef = (NodeField)pcfields.get(i);
			nodef.layout(appsGraphs, appsGraphics2D, appLagrille);
			Rectangle2D b2Rect2d = nodef.getBounds();
			height += b2Rect2d.getBounds().getHeight() + YGP;
			double axis = nodef.getAxisX();
			leftWidth = Math.max(leftWidth, axis);
			rightWidth = Math.max(rightWidth, b2Rect2d.getWidth() - axis);
		}
		double width = 2 * Math.max(leftWidth, rightWidth) + 2 * XGP;
		width = Math.max(width, bel.getWidth());
		width = Math.max(width, APP_DEFAULT_WIDTH);
		bel = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(), width, bel.getHeight() + height);
		appLagrille.snap(bel);
		setBounds(bel);
		appesTopHeight = bel.getHeight() - height;
		double ytop = bel.getY() + appesTopHeight + YGP;
		double xmid = bel.getCenterX();
		for(int i = 0; i < pcfields.size(); i++)
		{
			NodeField nodef = (NodeField)pcfields.get(i);
			Rectangle2D b2 = nodef.getBounds();
			nodef.setBounds(new Rectangle2D.Double(xmid - nodef.getAxisX(), ytop, nodef.getAxisX() + rightWidth, b2.getHeight()));
			nodef.setBoxWidth(rightWidth);
			ytop += nodef.getBounds().getHeight() + YGP;
		}
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
	public NodeObject clone()
	{
		NodeObject cloned = (NodeObject)super.clone();
		cloned.appesName = appesName.clone();
		cloned.appesFields = new ArrayList<>();
		
		for( ChildNode child : appesFields)
		{

			ChildNode clonedChild = (ChildNode) child.clone();
			clonedChild.setNodeParent(cloned);
			cloned.appesFields.add(clonedChild);
		}
		
		return cloned;
	}

	@Override
	public void addChild(ChildNode appesNode)
	{
		addChild(appesFields.size(), appesNode);
	}
	
	@Override
	public void addChild(int appesIndex, ChildNode appesNode)
	{
		NodeParent oldParentnode = appesNode.getNodeParent();
		if (oldParentnode != null)
		{
			oldParentnode.ChildNodeDeleted(appesNode);
		}
		appesFields.add(appesIndex, appesNode);
		appesNode.setNodeParent(this);

	}

	@Override
	public List<ChildNode> getChildren()
	{
		return appesFields;
	}

	@Override
	public void ChildNodeDeleted(ChildNode appesNode)
	{
		if (appesNode.getNodeParent() != this)
		{
			return;
		}
		appesFields.remove(appesNode);
		appesNode.setNodeParent(null);
	}
	

	public static void setPersistenceDelegate(Encoder appesEncoder)
	{
		appesEncoder.setPersistenceDelegate(NodeObject.class, new DefaultPersistenceDelegate() {
			protected void initialize(Class<?> appesType, Object appesOldInstance, Object appesNouvInstance, Encoder appesOut) {
				super.initialize(appesType, appesOldInstance, appesNouvInstance, appesOut);
				for (ChildNode node : ((NodeParent) appesOldInstance).getChildren()) {
					appesOut.writeStatement(new Statement(appesOldInstance, "addChild", new Object[]{node}));
				}
			}
		});
	}
}
