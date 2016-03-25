

package Epp_UML_Editeur.LeFramework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;


public final class ArHead
{
	public static final ArHead ARROWHEAD_NONE = new ArHead();
	public static final ArHead ARROWHEAD_TRIANGLE = new ArHead();
	public static final ArHead ARROWHEAD_BLACK_TRIANGLE = new ArHead();
	public static final ArHead V = new ArHead();
	public static final ArHead ARROWHEAD_HALF_V = new ArHead();
	public static final ArHead ARROWHEAD_DIAMOND = new ArHead();
	public static final ArHead ARROWHEAD_BLACK_DIAMOND = new ArHead();
	   

	private static final double ANGLE_ARROW = Math.PI / 6;
	private static final double LENGTH_ARROW = 10;

	
   private ArHead() {}

   public void draw(Graphics2D appesGraphics2D, Point2D appesPoint1, Point2D appesEnd)
   {
	   GeneralPath path = getPath(appesPoint1, appesEnd);
	   Color colorOld = appesGraphics2D.getColor();
	   if(this == ARROWHEAD_BLACK_DIAMOND || this == ARROWHEAD_BLACK_TRIANGLE)
	   {
		   appesGraphics2D.setColor(Color.BLACK);
	   }
	   else 
	   {
		   appesGraphics2D.setColor(Color.WHITE);
	   }
	   appesGraphics2D.fill(path);
	   appesGraphics2D.setColor(colorOld);
	   appesGraphics2D.draw(path);
   	}
   
   @Override
   public String toString()
   {
	   String lReturn = "Unknown";
	   if( this == ARROWHEAD_NONE)
	   {
		   lReturn = "ARROWHEAD_NONE";
	   }
	   else if( this == ARROWHEAD_TRIANGLE)
	   {
		   lReturn = "ARROWHEAD_TRIANGLE";
	   }
	   else if( this == ARROWHEAD_BLACK_TRIANGLE)
	   {
		   lReturn = "ARROWHEAD_BLACK_TRIANGLE";
	   }
	   else if( this == V )
	   {
		   lReturn = "V";
	   }
	   else if( this == ARROWHEAD_HALF_V)
	   {
		   lReturn = "ARROWHEAD_HALF_V";
	   }
	   else if( this == ARROWHEAD_DIAMOND)
	   {
		   lReturn = "ARROWHEAD_DIAMOND";
	   }
	   else if( this == ARROWHEAD_BLACK_DIAMOND)
	   {
		   lReturn = "ARROWHEAD_BLACK_DIAMOND";
	   }
	   return lReturn;
   }


   	public GeneralPath getPath(Point2D appesPoint1, Point2D appesEnd)
   	{
   		GeneralPath path = new GeneralPath();
   		if(this == ARROWHEAD_NONE)
   		{
   			return path;
   		}
   		
   		double dx = appesEnd.getX() - appesPoint1.getX();
   		double dy = appesEnd.getY() - appesPoint1.getY();
   		double angle = Math.atan2(dy, dx);
   		double x1 = appesEnd.getX() - LENGTH_ARROW * Math.cos(angle + ANGLE_ARROW);
   		double y1 = appesEnd.getY() - LENGTH_ARROW * Math.sin(angle + ANGLE_ARROW);
   		double x2 = appesEnd.getX() - LENGTH_ARROW * Math.cos(angle - ANGLE_ARROW);
   		double y2 = appesEnd.getY() - LENGTH_ARROW * Math.sin(angle - ANGLE_ARROW);

   		path.moveTo((float)appesEnd.getX(), (float)appesEnd.getY());
   		path.lineTo((float)x1, (float)y1);
   		if(this == V)
   		{
   			path.moveTo((float)x2, (float)y2);
   			path.lineTo((float)appesEnd.getX(), (float)appesEnd.getY());
   		}
   		else if(this == ARROWHEAD_TRIANGLE || this == ARROWHEAD_BLACK_TRIANGLE)
   		{
   			path.lineTo((float)x2, (float)y2);
   			path.closePath();                  
   		}
   		else if(this == ARROWHEAD_DIAMOND || this == ARROWHEAD_BLACK_DIAMOND)
   		{
   			double x3 = x2 - LENGTH_ARROW * Math.cos(angle + ANGLE_ARROW);
   			double y3 = y2 - LENGTH_ARROW * Math.sin(angle + ANGLE_ARROW);
   			path.lineTo((float)x3, (float)y3);
   			path.lineTo((float)x2, (float)y2);
   			path.closePath();         
   		}      
   		return path;
   	}
}
