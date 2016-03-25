

package Epp_UML_Editeur.Graphe;


public abstract class EdgeRelationshipClass extends EdgeSegmentedLabeled
{	
	private String appesStartLabel = "";
	private String appesMiddleLabel = "";
	private String appesEndLabel = "";
	
	@Override
	protected String obtainLabelatStart()
	{
		return appesStartLabel;
	}
	
	@Override
	protected String obtainLabelatMiddle()
	{
		return appesMiddleLabel;
	}
	
	@Override
	protected String obtainLabelatEnd()
	{
		return appesEndLabel;
	}
	

	public void setStartLabel(String appesLabel)
	{
		appesStartLabel = appesLabel;
	}
	

	public void setLabelatMiddle(String appesLabel)
	{
		appesMiddleLabel = appesLabel;
	}
	

	public void setEndLabel(String appesLabel)
	{
		appesEndLabel = appesLabel;
	}
	

	public String getLabelatStart()
	{
		return appesStartLabel;
	}
	

	public String getLabelatMiddle()
	{
		return appesMiddleLabel;
	}
	

	public String getEndLabel()
	{
		return appesEndLabel;
	}
}
