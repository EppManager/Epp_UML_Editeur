

package Epp_UML_Editeur.LeFramework;

import java.awt.BasicStroke;
import java.awt.Stroke;


public final class StyleLine
{
	public static final StyleLine SOLIDSTYLE = new StyleLine();
	   public static final StyleLine DOTTEDSTYLE = new StyleLine();
	
	   private static final Stroke SOLID_STROKE = new BasicStroke();
	   private static final Stroke DOTTED_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, 
			   BasicStroke.JOIN_MITER, 10.0f, new float[] { 3.0f, 3.0f }, 0.0f);
	
   private StyleLine() {}


   public Stroke getStroke()
   {
      if (this == DOTTEDSTYLE)
	{
		return DOTTED_STROKE;
	}
      return SOLID_STROKE;
   }

   @Override
   public String toString()
   {
	   if( this == SOLIDSTYLE)
	   {
		   return "SOLIDSTYLE";
	   }
	   else if( this == DOTTEDSTYLE)
	   {
		   return "DOTTEDSTYLE";
	   }
	   else
	   {
		   return "Unknown";
	   }
   }
}
