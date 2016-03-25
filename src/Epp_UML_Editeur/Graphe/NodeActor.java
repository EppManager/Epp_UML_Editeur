

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;

import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodeActor extends NodeRectangular
{

	private static final int WIDTH_DEFAULT_APP = 48;
	private static final int HEIGHT_DEFAULT_APP = 64;
	

	private static final int GAP_ABOVE = 4;
	private static final int SIZE_HEAD = WIDTH_DEFAULT_APP *4/12;
	private static final int SIZE_BODY = WIDTH_DEFAULT_APP *5/12;
	private static final int SIZE_LEG = WIDTH_DEFAULT_APP *5/12;
	private static final int SIZE_ARMS = WIDTH_DEFAULT_APP *6/12;

	
	private MultiLigne appesName;


	public NodeActor()
	{
		appesName = new MultiLigne();
		appesName.setText("Actor");
		setBounds(new Rectangle2D.Double(0, 0, WIDTH_DEFAULT_APP, HEIGHT_DEFAULT_APP));
	}
   
	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		Rectangle2D top2d = new Rectangle2D.Double(0, 0, WIDTH_DEFAULT_APP, HEIGHT_DEFAULT_APP);
		Rectangle2D bot2d = appesName.getBounds(appsGraphics2D);
		Rectangle2D nb = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(),
            Math.max(top2d.getWidth(), bot2d.getWidth()), top2d.getHeight() + bot2d.getHeight());
		appLagrille.snap(nb);
		setBounds(nb);
	}
    
	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{	
		Rectangle2D ppesbounds = getBounds();


		GeneralPath path = new GeneralPath();
		float nckX = (float) (ppesbounds.getX() + ppesbounds.getWidth() / 2);
		float nckY = (float) (ppesbounds.getY() + SIZE_HEAD + GAP_ABOVE);

		path.moveTo(nckX, nckY);
		path.quadTo(nckX + SIZE_HEAD / 2, nckY, nckX + SIZE_HEAD / 2, nckY - SIZE_HEAD / 2);
		path.quadTo(nckX + SIZE_HEAD / 2, nckY - SIZE_HEAD, nckX, nckY - SIZE_HEAD);
		path.quadTo(nckX - SIZE_HEAD / 2, nckY - SIZE_HEAD, nckX - SIZE_HEAD / 2, nckY - SIZE_HEAD / 2);
		path.quadTo(nckX - SIZE_HEAD / 2, nckY, nckX, nckY);

		float hpX = nckX;
		float hpY = nckY + SIZE_BODY;
		path.lineTo(hpX, hpY);

		path.moveTo(nckX - SIZE_ARMS / 2, nckY + SIZE_BODY / 3);
		path.lineTo(nckX + SIZE_ARMS / 2, nckY + SIZE_BODY / 3);

		float dx = (float) (SIZE_LEG / Math.sqrt(2));
		float feetX1 = hpX - dx;
		float feetX2 = hpX + dx + 1;
		float feetY  = hpY + dx + 1;
		path.moveTo(feetX1, feetY);
		path.lineTo(hpX, hpY);
		path.lineTo(feetX2, feetY);

		appesGraphics2D.draw(path);


		Rectangle2D bot2d = appesName.getBounds(appesGraphics2D);

		Rectangle2D namebox2d = new Rectangle2D.Double(ppesbounds.getX() + (ppesbounds.getWidth() - bot2d.getWidth()) / 2,
				ppesbounds.getY() + HEIGHT_DEFAULT_APP, bot2d.getWidth(), bot2d.getHeight());
		appesName.draw(appesGraphics2D, namebox2d);
	}


	public void setName(MultiLigne appespNewValue)
	{
		appesName = appespNewValue;
	}


	public MultiLigne getName()
	{
		return appesName;
	}
	
	@Override
	public NodeActor clone()
	{
		NodeActor cloned = (NodeActor) super.clone();
		cloned.appesName = appesName.clone();
		return cloned;
	} 
}
