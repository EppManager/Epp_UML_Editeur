
package Epp_UML_Editeur.LeFramework;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;


public class RecentesQueueFiles implements Iterable<File>
{
	private static final int CAPACITY_RECENTES_FILES = 5;
	
	private final LinkedList<File> appesFiles = new LinkedList<>();







	public void add(String appesNameFile)
	{
		assert appesNameFile != null;
		if( appesNameFile.length() == 0 )
		{
			return;
		}
		File newFile = new File(appesNameFile).getAbsoluteFile();
		if( appesFiles.contains(newFile))
		{
			appesFiles.remove(newFile);
		}
		if( newFile.exists() && newFile.isFile() )
		{
			appesFiles.add(0, newFile);
			if( appesFiles.size() > CAPACITY_RECENTES_FILES)
			{
				appesFiles.removeLast();
			}
		}
	}
	

	public File getMostRecentDirectory()
	{
		if(appesFiles.size() > 0)
		{
			return appesFiles.get(0).getParentFile();
		}
		else
		{
			return new File(".");
		}
	}

	@Override
	public Iterator<File> iterator() 
	{
		return appesFiles.iterator();
	}


	public String serialize()
	{
		StringBuilder blder = new StringBuilder();
		if( appesFiles.size() > 0 )
		{
			blder.append(appesFiles.get(0));
		}
		for( int i = 1; i < appesFiles.size(); i++)
		{
			blder.append("|");
			blder.append(appesFiles.get(i));
		}
		return blder.toString();
	}
	

	public int size()
	{
		return appesFiles.size();
	}
	

	public void deserialize(String appesString)
	{
		assert appesString != null;
		appesFiles.clear();
		String[] files = appesString.split("\\|");
		for( int i = files.length-1; i >=0; i--)
		{
			if( files[i].trim().length() > 0 )
			{
				add(files[i]);
			}
		}
	}
}
