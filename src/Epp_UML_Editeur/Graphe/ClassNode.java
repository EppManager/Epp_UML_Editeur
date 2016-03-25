
package Epp_UML_Editeur.Graphe;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import Epp_UML_Editeur.LeFramework.MultiLigne;


public class ClassNode extends InterfaceNode
{
	private MultiLigne appesAttributes;


	public ClassNode()
	{
		appesAttributes = new MultiLigne();
		appesAttributes.setJustification(MultiLigne.LEFT);
		appesName.setText("");
	}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		super.graphics2dDraw(appesGraphics2D);
		double midHeight = middleCompute(appesGraphics2D).getHeight();
		double bottomHeight = computeBottom(appesGraphics2D).getHeight();
		Rectangle2D topRect2d = new Rectangle2D.Double(getBounds().getX(), getBounds().getY(),
				getBounds().getWidth(), getBounds().getHeight() - midHeight - bottomHeight);
		Rectangle2D mid = new Rectangle2D.Double(topRect2d.getX(), topRect2d.getMaxY(), topRect2d.getWidth(), midHeight);
		appesAttributes.draw(appesGraphics2D, mid);
   }
	

	public void setAttributes(MultiLigne ppesNouvValue)
	{
		appesAttributes = ppesNouvValue;
	}


	public MultiLigne getAttributes()
	{
		return appesAttributes;
	}
	

	@Override
	protected boolean needsCompartmentMiddle()
	{
		return !appesAttributes.getText().isEmpty();
	}
	

	@Override
	protected Rectangle2D middleCompute(Graphics2D ppesGraphics2D)
	{
		if( !needsCompartmentMiddle() )
		{
			return new Rectangle2D.Double(0, 0, 0, 0);
		}
			
		Rectangle2D attributesRect2d = appesAttributes.getBounds(ppesGraphics2D);
		attributesRect2d.add(new Rectangle2D.Double(0, 0, APP_DEFAULT_WIDTH, APP_DEFAULT_COMPARTMENT_HEIGHT));
		return attributesRect2d;
	}

	@Override
	public ClassNode clone()
	{
		ClassNode cloned = (ClassNode)super.clone();
		cloned.appesAttributes = appesAttributes.clone();
		return cloned;
	}
}
