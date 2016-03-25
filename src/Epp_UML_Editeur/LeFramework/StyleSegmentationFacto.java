

package Epp_UML_Editeur.LeFramework;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.NodesPackage;


public final class StyleSegmentationFacto
{
	private static final int MARGIN_STYLE = 20;
	private static final int MIN_SEGMENT_STYLE = 10;
	
	private StyleSegmentationFacto(){}
	

	private static StyleSegmentation createGenericStrategy(final StyleSegmentation appesMain, final StyleSegmentation appesAlternate)
	{
		return new StyleSegmentation()
		{
			@Override
			public Point2D[] getPath(Node appesStart, Node appesEnd)
			{
				if( appesStart == appesEnd)
				{
					return createSelfPath(appesStart);
				}
				Point2D[] path = appesMain.getPath(appesStart, appesEnd);
				if( path == null && appesAlternate != null )
				{
					path = appesAlternate.getPath(appesStart, appesEnd);
				}
				if( path != null )
				{
					return path;
				}
				else
				{
					path = new Straight().getPath(appesStart, appesEnd);
					assert path != null;
					return path;
				}
			}
		};
	}
	

	public static StyleSegmentation createStraightStrategy()
	{
		return createGenericStrategy(new Straight(), null);
	}
	

	public static StyleSegmentation createHVHStrategy()
	{
		return createGenericStrategy(new HVH(), new VHV());
	}
	

	public static StyleSegmentation createVHVStrategy()
	{
		return createGenericStrategy(new VHV(), new HVH());
	}
	

	private static Point2D[] createSelfPath(Node appesNode)
	{
		Point2D topRight = findTopRightCorner(appesNode);
		double x1 = topRight.getX() - MARGIN_STYLE;
		double y1 = topRight.getY();
		double x2 = x1;
		double y2 = y1 - MARGIN_STYLE;
		double x3 = x2 + MARGIN_STYLE * 2;
		double y3 = y2;
		double x4 = x3;
		double y4 = topRight.getY() + MARGIN_STYLE;
		double x5 = topRight.getX();
		double y5 = y4;
		
		return new Point2D[] {new Point2D.Double(x1, y1), new Point2D.Double(x2, y2),
							  new Point2D.Double(x3, y3), new Point2D.Double(x4, y4), new Point2D.Double(x5, y5)};
	}
	

	private static Point2D findTopRightCorner(Node appesNode)
	{
		if( appesNode instanceof NodesPackage)
		{
			return ((NodesPackage)appesNode).getTopRightCorner();
		}
		else
		{
			return new Point2D.Double(appesNode.getBounds().getMaxX(), appesNode.getBounds().getMinY());
		}
	}
	
	static Point2D[] connectionPoints(Node appesNode)
	{
		return new Point2D[] { appesNode.getConnectionPoint(Direction.WEST) ,
							   appesNode.getConnectionPoint(Direction.NORTH),
							   appesNode.getConnectionPoint(Direction.EAST),
							   appesNode.getConnectionPoint(Direction.SOUTH)};
	}
	
	private static class Straight implements StyleSegmentation
	{
		@Override
		public Point2D[] getPath(Node appesStart, Node appesEnd)
		{
			Point2D[] connectionPointsStart = connectionPoints(appesStart);
		    Point2D[] connectionPointsEnd = connectionPoints(appesEnd);
		    Point2D start = connectionPointsStart[0];
		    Point2D end = connectionPointsEnd[0];
		    double distance = start.distance(end);
		    
		    for( Point2D startPoint : connectionPointsStart)
		    {
		    	for( Point2D endPoint : connectionPointsEnd )
		    	{
		    		double newDistance = startPoint.distance(endPoint);
		    		if( newDistance < distance )
		    		{
		    			start = startPoint;
		    			end = endPoint;
		    			distance = newDistance;
		    		}
		    	}
		    }
		    return new Point2D[] {start, end};
		}		
	}
	
	private static class HVH implements StyleSegmentation
	{
		@Override
		public Point2D[] getPath(Node appesStart, Node appesEnd)
		{
			Point2D start = appesStart.getConnectionPoint(Direction.EAST);
			Point2D end = appesEnd.getConnectionPoint(Direction.WEST);
			
			if( start.getX() + 2* MIN_SEGMENT_STYLE <= end.getX() )
			{
			}
			else if( appesEnd.getConnectionPoint(Direction.EAST).getX() + 2* MIN_SEGMENT_STYLE <= appesStart.getConnectionPoint(Direction.WEST).getX() )
			{
				start = appesStart.getConnectionPoint(Direction.WEST);
				end = appesEnd.getConnectionPoint(Direction.EAST);
			}
			else
			{
				return null;
			}
			
	  		if(Math.abs(start.getY() - end.getY()) <= MIN_SEGMENT_STYLE)
	  		{
	  			return new Point2D[] {new Point2D.Double(start.getX(), end.getY()), new Point2D.Double(end.getX(), end.getY()) };
	  		}
	  		else
	  		{
	  			return new Point2D[] { new Point2D.Double(start.getX(), start.getY()), 
	  								   new Point2D.Double((start.getX() + end.getX()) / 2, start.getY()),
	  								   new Point2D.Double((start.getX() + end.getX()) / 2, end.getY()), 
	  								   new Point2D.Double(end.getX(), end.getY())};
	  		}
		}
	}
	
	private static class VHV implements StyleSegmentation
	{
		@Override
		public Point2D[] getPath(Node appesStart, Node appesEnd)
		{
			Point2D start = appesStart.getConnectionPoint(Direction.SOUTH);
			Point2D end = appesEnd.getConnectionPoint(Direction.NORTH);
			
			if( start.getY() + 2* MIN_SEGMENT_STYLE <= end.getY() )
			{
			}
			else if( appesEnd.getConnectionPoint(Direction.SOUTH).getY() + 2* MIN_SEGMENT_STYLE <= appesStart.getConnectionPoint(Direction.NORTH).getY() )
			{
				start = appesStart.getConnectionPoint(Direction.NORTH);
				end = appesEnd.getConnectionPoint(Direction.SOUTH);
			}
			else
			{
				return null;
			}
			
	  		if(Math.abs(start.getX() - end.getX()) <= MIN_SEGMENT_STYLE)
	  		{
	  			return new Point2D[] {new Point2D.Double(end.getX(), start.getY()), new Point2D.Double(end.getX(), end.getY())};
	  		}
	  		else
	  		{
	  			return new Point2D[] {new Point2D.Double(start.getX(), start.getY()), 
	  								  new Point2D.Double(start.getX(), (start.getY() + end.getY()) / 2), 
	  								  new Point2D.Double(end.getX(), (start.getY() + end.getY()) / 2), 
	  								  new Point2D.Double(end.getX(), end.getY())};
	  		}
		}
	}
}

