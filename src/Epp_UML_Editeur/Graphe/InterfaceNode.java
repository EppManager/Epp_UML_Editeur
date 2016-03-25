
package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class InterfaceNode extends NodeRectangular implements ChildNode
{
	protected static final int APP_DEFAULT_COMPARTMENT_HEIGHT = 20;
	protected static final int APP_DEFAULT_WIDTH = 100;
	protected static final int APP_DEFAULT_HEIGHT = 60;
	
	protected MultiLigne appesName;
	protected MultiLigne appesMethods;
	
	private NodeParent appesContainer;
	


	public InterfaceNode()
	{
		appesName = new MultiLigne(true);
		appesName.setText("\u00ABinterface\u00BB\n");
		appesName.setJustification(MultiLigne.CENTER);
		appesMethods = new MultiLigne();
		appesMethods.setJustification(MultiLigne.LEFT);
		setBounds(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_HEIGHT));
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		double midHeight = middleCompute(appesGraphics2D).getHeight();
		double bottomHeight = computeBottom(appesGraphics2D).getHeight();
		Rectangle2D topRecto2d = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(),
				getBounds().getWidth(), getBounds().getHeight() - midHeight - bottomHeight);
		appesGraphics2D.draw(topRecto2d);
		appesName.draw(appesGraphics2D, topRecto2d);
		Rectangle2D midRect2d = new Rectangle2D.Double(topRecto2d.getX(), topRecto2d.getMaxY(), topRecto2d.getWidth(), midHeight);
		appesGraphics2D.draw(midRect2d);
		Rectangle2D botRect2d = new Rectangle2D.Double(topRecto2d.getX(), midRect2d.getMaxY(), topRecto2d.getWidth(), bottomHeight);
		appesGraphics2D.draw(botRect2d);
		appesMethods.draw(appesGraphics2D, botRect2d);
	}
	

	protected Rectangle2D computeTop(Graphics2D appesGraphics2D)
	{
		Rectangle2D topRecto = appesName.getBounds(appesGraphics2D);
		
		double minHeight = APP_DEFAULT_COMPARTMENT_HEIGHT;
		if(!needsCompartmentMiddle() && !needsCompartmentBottom() )
		{
			minHeight = APP_DEFAULT_HEIGHT;
		}
		else if( needsCompartmentMiddle() ^ needsCompartmentBottom() )
		{
			minHeight = 2 * APP_DEFAULT_COMPARTMENT_HEIGHT;
		}
		topRecto.add(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, minHeight));

		return topRecto;
	}
	

	protected Rectangle2D middleCompute(Graphics2D appesGraphics2D)
	{
		return new Rectangle2D.Double(0, 0, 0, 0);
	}
	

	protected Rectangle2D computeBottom(Graphics2D appesGraphics2D)
	{
		if( !needsCompartmentBottom() )
		{
			return new Rectangle2D.Double(0, 0, 0, 0);
		}
			
		Rectangle2D bottom = appesMethods.getBounds(appesGraphics2D);
		bottom.add(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_COMPARTMENT_HEIGHT));
		return bottom;
	}
	


	protected boolean needsCompartmentMiddle()
	{
		return false;
	}
	

	protected boolean needsCompartmentBottom()
	{
		return !appesMethods.getText().isEmpty();
	}
	

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		Rectangle2D topRect2d = computeTop(appsGraphics2D);
		Rectangle2D middleRect2d = middleCompute(appsGraphics2D);
		Rectangle2D bottomRect2d = computeBottom(appsGraphics2D);

		Rectangle2D boundsRect2d = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(),
				Math.max(Math.max(topRect2d.getWidth(), middleRect2d.getWidth()), bottomRect2d.getWidth()), topRect2d.getHeight() + middleRect2d.getHeight() + bottomRect2d.getHeight());
		appLagrille.snap(boundsRect2d);
		setBounds(boundsRect2d);
	}


	public void setName(MultiLigne appesName)
	{
		this.appesName = appesName;
	}


	public MultiLigne getName()
	{
		return appesName;
	}
	

	public void setMethods(MultiLigne appesMethods)
	{
		this.appesMethods = appesMethods;
	}
	

	public MultiLigne getMethods()
	{
		return appesMethods;
	}

	@Override
	public InterfaceNode clone()
	{
		InterfaceNode cloned = (InterfaceNode)super.clone();
		cloned.appesName = appesName.clone();
		cloned.appesMethods = appesMethods.clone();
		return cloned;
	}
	
	@Override
	public NodeParent getNodeParent()
	{
		return appesContainer;
	}

	@Override
	public void setNodeParent(NodeParent appesNode)
	{
		assert appesNode instanceof NodesPackage || appesNode == null;
		appesContainer = appesNode;
	}
}
