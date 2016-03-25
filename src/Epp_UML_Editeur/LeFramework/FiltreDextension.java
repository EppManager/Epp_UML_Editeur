

package Epp_UML_Editeur.LeFramework;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class FiltreDextension extends FileFilter
{
	private String appesDescript;
	private String appesExtension;
	

   	public FiltreDextension(String appesDescript, String appesExtension)
   	{
   		assert appesDescript != null;
		assert appesExtension != null;
		assert appesExtension.length() > 0;
	   	this.appesDescript = appesDescript;
	   	this.appesExtension = appesExtension;
   	}
   
   	@Override
   	public boolean accept(File appesFile)
   	{  
   		if( appesFile.isDirectory() )
   		{
   			return true;
   		}
      
   		String fileName = appesFile.getName().toLowerCase();
   		if(fileName.endsWith(appesExtension.toLowerCase()))
		{
			return true;
		}
   		return false;
   }
   
   	@Override
   	public String getDescription()
   	{ 
   		return appesDescript;
   	}
   

   	public String getExtension()
   	{
   		return appesExtension;
   	}
}
