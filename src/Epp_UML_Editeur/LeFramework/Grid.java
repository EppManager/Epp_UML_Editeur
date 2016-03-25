

package Epp_UML_Editeur.LeFramework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


public class Grid
{
	private static final Color COLOR_GRID = new Color(220, 220, 220);
	private static final double SIZE_GRID = 10.0;
	

	public static void draw(Graphics2D appesGraphics2D, Rectangle2D appesBounds)
	{
		Color colorOld = appesGraphics2D.getColor();
		appesGraphics2D.setColor(COLOR_GRID);
		Stroke StrokeOld = appesGraphics2D.getStroke();
		for(double x = appesBounds.getX(); x < appesBounds.getMaxX(); x += SIZE_GRID)
		{
			appesGraphics2D.draw(new Line2D.Double(x, appesBounds.getY(), x, appesBounds.getMaxY()));
		}
		for(double y = appesBounds.getY(); y < appesBounds.getMaxY(); y += SIZE_GRID)
		{
			appesGraphics2D.draw(new Line2D.Double(appesBounds.getX(), y, appesBounds.getMaxX(), y));
		}
		appesGraphics2D.setStroke(StrokeOld);
		appesGraphics2D.setColor(colorOld);
	}


	public void snap(Rectangle2D appesRectangle)
	{
		double x = Math.round(appesRectangle.getX() / SIZE_GRID) * SIZE_GRID;
		double u = Math.ceil(appesRectangle.getWidth() / (2 * SIZE_GRID)) * (2 * SIZE_GRID);
		double y = Math.round(appesRectangle.getY() / SIZE_GRID) * SIZE_GRID;
		double w = Math.ceil(appesRectangle.getHeight() / (2 * SIZE_GRID)) * (2 * SIZE_GRID);
		appesRectangle.setFrame(x, y, u, w);
	}
}
