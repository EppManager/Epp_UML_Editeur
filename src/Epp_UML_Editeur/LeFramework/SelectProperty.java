

package Epp_UML_Editeur.LeFramework;

import java.beans.PropertyEditorSupport;




public class SelectProperty extends PropertyEditorSupport
{
	private String[] appesNames;
	private Object[] appesValues;
	

	public SelectProperty(String[] appesTags, Object[] appesValues)
	{
		appesNames = appesTags;
		this.appesValues = appesValues;
	}

	@Override
	public String[] getTags()
	{
		return appesNames;
	}

	@Override
	public String getAsText()
	{
		for(int i = 0; i < appesValues.length; i++)
		{
			if(getValue().equals(appesValues[i]))
			{
				return appesNames[i];
			}
		}
		return null;
	}

	@Override
	public void setAsText(String appesString)
	{
		for(int i = 0; i < appesNames.length; i++)
		{
			if(appesString.equals(appesNames[i]))
			{
				setValue(appesValues[i]);
			}
		}
	}

 
}
