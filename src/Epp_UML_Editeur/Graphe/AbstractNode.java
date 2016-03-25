
package Epp_UML_Editeur.Graphe;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;

import Epp_UML_Editeur.LeFramework.Grid;



public abstract class AbstractNode implements Node
{
	public static final int GAP_SHADOW_APP = 4;
	
	private static final Color COLOR_SHADOW_APP = Color.LIGHT_GRAY;



	public AbstractNode()
	{
	}

	@Override
	public AbstractNode clone()
	{
		try
		{
			AbstractNode cloned = (AbstractNode) super.clone();
			return cloned;
		}
		catch(CloneNotSupportedException exception)
		{
			return null;
		}
	}

	@Override
	public void AppCommandtranslate(double x, double y)
	{
	}
	
	@Override
	public void layout(Graph appsGraphs, Graphics2D appsGraphics2D, Grid appLagrille)
	{}

	@Override
	public void graphics2dDraw(Graphics2D appesGraphics2D)
	{
		Shape shape = getShape();
		if(shape == null)
		{
			return;
		}

		Color oldColor = appesGraphics2D.getColor();
		appesGraphics2D.translate(GAP_SHADOW_APP, GAP_SHADOW_APP);
		appesGraphics2D.setColor(COLOR_SHADOW_APP);
		appesGraphics2D.fill(shape);
		appesGraphics2D.translate(-GAP_SHADOW_APP, -GAP_SHADOW_APP);
		appesGraphics2D.setColor(appesGraphics2D.getBackground());
		appesGraphics2D.fill(shape);
		appesGraphics2D.setColor(oldColor);
	}
   

	public Shape getShape() 
	{ return null; }   
   

	public static void setPersistenceDelegate(Encoder appesEncoder)
	{
      appesEncoder.setPersistenceDelegate(AbstractNode.class, new DefaultPersistenceDelegate() {
		  protected void initialize(Class<?> appesType, Object appesOldInstance, Object appesNewInstance, Encoder appesOut) {
			  super.initialize(appesType, appesOldInstance, appesNewInstance, appesOut);
		  }
	  });
   }
}

