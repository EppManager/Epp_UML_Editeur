

package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.Direction;
import Epp_UML_Editeur.LeFramework.Grid;
import Epp_UML_Editeur.LeFramework.MultiLigne;


public class NodeField extends NodeRectangular implements ChildNode
{
	public static final int DEFAULT_WIDTH = 60;
	public static final int DEFAULT_HEIGHT = 20;
	
	private double appesAxisX;
	private MultiLigne appesName;
	private MultiLigne appesValue;
	private Rectangle2D appesNameBounds;
	private Rectangle2D appesValueBounds;
	private boolean appesBoxedValue;
	private double appesBoxWidth;
	private NodeObject appesObject;


	public NodeField()
	{
		appesName = new MultiLigne();
		appesName.setJustification(MultiLigne.RIGHT);
		appesValue = new MultiLigne();
		setBounds(new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT));
   }

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		Rectangle2D b = getBounds();
		double leftWidth = appesName.getBounds(appesGraphics2D).getWidth();
		MultiLigne equal = new MultiLigne();
		equal.setText(" = ");
		double midWidth = equal.getBounds(appesGraphics2D).getWidth();
      
		double Widthright = appesValue.getBounds(appesGraphics2D).getWidth();
		if(Widthright == 0)
		{
			Widthright = DEFAULT_WIDTH / 2;
		}
		Widthright = Math.max(Widthright, appesBoxWidth - midWidth / 2);

		appesNameBounds = new Rectangle2D.Double(b.getX(), b.getY(), leftWidth, b.getHeight());
		appesName.draw(appesGraphics2D, appesNameBounds);
		Rectangle2D mid = new Rectangle2D.Double(b.getX() + leftWidth, b.getY(), midWidth, b.getHeight());
		equal.draw(appesGraphics2D, mid);
		appesValueBounds = new Rectangle2D.Double(b.getMaxX() - Widthright, b.getY(), Widthright, b.getHeight());
		if(appesBoxedValue)
		{
			appesValue.setJustification(MultiLigne.CENTER);
		}
		else
		{
			appesName.setJustification(MultiLigne.LEFT);
		}
		appesValue.draw(appesGraphics2D, appesValueBounds);
		if(appesBoxedValue)
		{
			appesGraphics2D.draw(appesValueBounds);
		}
	}
	
	@Override
	public Point2D getConnectionPoint(Direction appDirections)
	{
		Rectangle2D bel = getBounds();
		return new Point2D.Double((bel.getMaxX() + bel.getX() + appesAxisX) / 2, bel.getCenterY());
	}

	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{
		appesNameBounds = appesName.getBounds(appsGraphics2D);
		appesValueBounds = appesValue.getBounds(appsGraphics2D);
		MultiLigne equal = new MultiLigne();
		equal.setText(" = ");
		Rectangle2D e = equal.getBounds(appsGraphics2D);
		double leftWidth = appesNameBounds.getWidth();
		double midWidth = e.getWidth();
		double Widthright = appesValueBounds.getWidth();
		if(Widthright == 0)
		{
			Widthright = DEFAULT_WIDTH / 2;
		}
		Widthright = Math.max(Widthright, appesBoxWidth - midWidth / 2);
		double width = leftWidth + midWidth + Widthright;
		double height = Math.max(appesNameBounds.getHeight(), Math.max(appesValueBounds.getHeight(), e.getHeight()));

		Rectangle2D b = getBounds();
		setBounds(new Rectangle2D.Double(b.getX(), b.getY(), width, height));
		appesAxisX = leftWidth + midWidth / 2;
      
		appesValueBounds.setFrame(b.getMaxX() - Widthright, b.getY(), appesValueBounds.getWidth(), appesValueBounds.getHeight());
	}


	public void setName(MultiLigne appesName)
	{
		this.appesName = appesName;
	}


	public MultiLigne getName()
	{
		return appesName;
	}


	public void setValue(MultiLigne appesNouvValue)
	{
		appesValue = appesNouvValue;
	}


	public MultiLigne getValue()
	{
		return appesValue;
	}


	public void setBoxWidth(double appesBoxWidth)
	{
		this.appesBoxWidth = appesBoxWidth;
	}
   

	public void setBoxedValue(boolean appesNouvValue)
	{
		appesBoxedValue = appesNouvValue;
	}


	public boolean isBoxedValue()
	{
		return appesBoxedValue;
	}

	@Override
	public NodeField clone()
	{
		NodeField cloned = (NodeField)super.clone();
		cloned.appesName = appesName.clone();
		cloned.appesValue = appesValue.clone();
		return cloned;
	}


	public double getAxisX()
	{
		return appesAxisX;
	}
   
	@Override
	public Shape getShape()
	{
		if(appesBoxedValue)
		{
			return appesValueBounds;
		}
		else
		{
			return null;
		}
	}

	@Override
	public NodeParent getNodeParent()
	{
		return appesObject;
	}

	@Override
	public void setNodeParent(NodeParent appesNode)
	{
		assert appesNode == null || appesNode instanceof NodeObject;
		appesObject = (NodeObject) appesNode;
	}
}

