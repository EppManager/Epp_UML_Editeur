

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodeState extends NodeRectangular
{
	private static final int APP_SIZE_ARC = 20;
	private static final int APP_DEFAULT_WIDTH = 80;
	private static final int APP_DEFAULT_HEIGHT = 60;
	
	private MultiLigne appesName;


	public NodeState()
	{
		appesName = new MultiLigne();
		setBounds(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_HEIGHT));
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		appesGraphics2D.draw(getShape());
		appesName.draw(appesGraphics2D, getBounds());
	}
   
	@Override
	public Shape getShape()
	{       
		return new RoundRectangle2D.Double(getBounds().getX(), getBounds().getY(), 
				getBounds().getWidth(), getBounds().getHeight(), APP_SIZE_ARC, APP_SIZE_ARC);
   }

	@Override	
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		Rectangle2D bRect2d = appesName.getBounds(appsGraphics2D);
		bRect2d = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(),
				Math.max(bRect2d.getWidth(), APP_DEFAULT_WIDTH), Math.max(bRect2d.getHeight(), APP_DEFAULT_HEIGHT));
		appLagrille.snap(bRect2d);
		setBounds(bRect2d);
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
	public NodeState clone()
	{
		NodeState cloned = (NodeState)super.clone();
		cloned.appesName = appesName.clone();
		return cloned;
	}
}
