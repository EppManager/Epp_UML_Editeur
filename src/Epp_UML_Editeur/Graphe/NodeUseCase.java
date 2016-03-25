
package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;

import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodeUseCase extends NodeRectangular
{
	private static final int APP_DEFAULT_WIDTH = 110;
	private static final int APP_DEFAULT_HEIGHT = 40;
	
	private MultiLigne appesName;


	public NodeUseCase()
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
		return new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight());
	}
   

	public void setName(MultiLigne appespName)
	{
		appesName = appespName;
	}


	public MultiLigne getName()
	{
		return appesName;
	}

	@Override
	public NodeUseCase clone()
	{
		NodeUseCase cloned = (NodeUseCase) super.clone();
		cloned.appesName = appesName.clone();
		return cloned;
	}
}
