

package Epp_UML_Editeur.Graphe;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodeNote extends NodeRectangular
{
	private static final int APP_DEFAULT_WIDTH = 60;
	private static final int APP_DEFAULT_HEIGHT = 40;
	private static final Color APP_DEFAULT_COLOR = new Color(0.9f, 0.9f, 0.6f);
	private static final int APP_FOLD_X = 8;
	private static final int APP_FOLD_Y = 8;
	
	private MultiLigne appesText;


	public NodeNote()
	{
		setBounds(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_HEIGHT));
		appesText = new MultiLigne();
		appesText.setJustification(MultiLigne.LEFT);
	}

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		Rectangle2D bRect2d = appesText.getBounds(appsGraphics2D);
		Rectangle2D boundsRect2d = getBounds();
		bRect2d = new Rectangle2D.Double(boundsRect2d.getX(), boundsRect2d.getY(), Math.max(bRect2d.getWidth(), APP_DEFAULT_WIDTH), Math.max(bRect2d.getHeight(), APP_DEFAULT_HEIGHT));
		appLagrille.snap(bRect2d);
		setBounds(bRect2d);
	}


	public MultiLigne getText()
	{
		return appesText;
	}


	public void setText(MultiLigne appesText)
	{
		this.appesText = appesText;
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		Color colorOld = appesGraphics2D.getColor();
		appesGraphics2D.setColor(APP_DEFAULT_COLOR);

		Shape path = getShape();
		appesGraphics2D.fill(path);
		appesGraphics2D.setColor(colorOld);
		appesGraphics2D.draw(path);

		Rectangle2D boundsRect2d = getBounds();
		GeneralPath foldPath = new GeneralPath();
		foldPath.moveTo((float) (boundsRect2d.getMaxX() - APP_FOLD_X), (float) boundsRect2d.getY());
		foldPath.lineTo((float) boundsRect2d.getMaxX() - APP_FOLD_X, (float) boundsRect2d.getY() + APP_FOLD_X);
		foldPath.lineTo((float) boundsRect2d.getMaxX(), (float) (boundsRect2d.getY() + APP_FOLD_Y));
		foldPath.closePath();
		colorOld = appesGraphics2D.getColor();
		appesGraphics2D.setColor(appesGraphics2D.getBackground());
		appesGraphics2D.fill(foldPath);
		appesGraphics2D.setColor(colorOld);
		appesGraphics2D.draw(foldPath);
      
		appesText.draw(appesGraphics2D, getBounds());
	}
   
	@Override
	public Shape getShape()
	{
		Rectangle2D boundsRect2d = getBounds();
		GeneralPath path = new GeneralPath();
		path.moveTo((float)boundsRect2d.getX(), (float)boundsRect2d.getY());
		path.lineTo((float)(boundsRect2d.getMaxX() - APP_FOLD_X), (float)boundsRect2d.getY());
		path.lineTo((float)boundsRect2d.getMaxX(), (float)(boundsRect2d.getY() + APP_FOLD_Y));
		path.lineTo((float)boundsRect2d.getMaxX(), (float)boundsRect2d.getMaxY());
		path.lineTo((float)boundsRect2d.getX(), (float)boundsRect2d.getMaxY());
		path.closePath();
		return path;
	}

	@Override
	public NodeNote clone()
	{
		NodeNote cloned = (NodeNote)super.clone();
		cloned.appesText = appesText.clone();
		return cloned;
	}
}
