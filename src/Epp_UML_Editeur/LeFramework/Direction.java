

package Epp_UML_Editeur.LeFramework;

import java.awt.geom.Point2D;


public class Direction
{
	public static final Direction NORTH = new Direction(0, -1);
	public static final Direction SOUTH = new Direction(0, 1);
	public static final Direction EAST = new Direction(1, 0);
	public static final Direction WEST = new Direction(-1, 0);
	
	private double x;
	private double y;
	

	public Direction(double x, double y)
	{
		this.x = x;
		this.y = y;
		double length = Math.sqrt(this.x * this.x + this.y * this.y);
		if(length == 0) 
		{
			return;
		}
		this.x = this.x / length;
		this.y = this.y / length;
	}


	public Direction(Point2D appesPoint1, Point2D appesPoint2)
	{
		this(appesPoint2.getX() - appesPoint1.getX(),
				appesPoint2.getY() - appesPoint1.getY());
	}


	public Direction turn(double appesAngle)
	{
		double a = Math.toRadians(appesAngle);
		return new Direction(x * Math.cos(a) - y * Math.sin(a), x * Math.sin(a) + y * Math.cos(a));
	}


	public double getX()
	{
		return x;
	}


	public double getY()
	{
		return y;
	}
}
