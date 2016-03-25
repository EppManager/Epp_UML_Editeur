

package Epp_UML_Editeur.Graphe;

import java.awt.geom.Point2D;

import Epp_UML_Editeur.LeFramework.ArHead;
import Epp_UML_Editeur.LeFramework.StyleLine;
import Epp_UML_Editeur.LeFramework.StyleSegmentationFacto;


public class EdgeUseCaseDependency extends EdgeSegmentedLabeled
{
	private static final String APP_LABEL_INCLUDE = "\u00ABinclude\u00BB";
	private static final String APP_LABEL_EXTEND = "\u00ABextend\u00BB";
	

	public enum Type 
	{None, Include, Extend}
	
	private Type appesType = Type.None;
	

	public EdgeUseCaseDependency()
	{}
	

	public EdgeUseCaseDependency(Type appesType)
	{
		this.appesType = appesType;
	}
	

	public Type getType()
	{
		return appesType;
	}
	

	public void setType(Type appesType)
	{
		this.appesType = appesType;
	}
	
	@Override
	protected ArHead endArrowHead()
	{
		return ArHead.V;
	}
	
	@Override
	protected StyleLine obtainStyleLine()
	{
		return StyleLine.DOTTEDSTYLE;
	}
	
	@Override
	protected String obtainLabelatMiddle()
	{
		if( appesType == Type.Include )
		{
			return APP_LABEL_INCLUDE;
		}
		else if( appesType == Type.Extend )
		{
			return APP_LABEL_EXTEND;
		}
		else
		{
			return "";
		}
	}
	
	@Override
	protected Point2D[] getPoints()
	{
		return StyleSegmentationFacto.createStraightStrategy().getPath(getNodeStart(), getNodeEnd());
	}
}
