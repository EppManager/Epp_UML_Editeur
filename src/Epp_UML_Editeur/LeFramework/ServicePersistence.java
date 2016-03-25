
package Epp_UML_Editeur.LeFramework;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.NodeImplicitParameter;
import Epp_UML_Editeur.Graphe.AbstractNode;
import Epp_UML_Editeur.Graphe.NodeObject;
import Epp_UML_Editeur.Graphe.NodesPackage;


public final class ServicePersistence
{
	private static PersistenceDelegate FieldDelegatestatic = new DefaultPersistenceDelegate()
	{
		@Override
		protected Expression instantiate(Object appesOldInstance, Encoder appesOut)
		{
			try
			{
				Class<?> cl = appesOldInstance.getClass();
				Field[] fields = cl.getFields();
				for(int i = 0; i < fields.length; i++)
				{
					if(Modifier.isStatic(fields[i].getModifiers()) && fields[i].get(null) == appesOldInstance)
					{
						return new Expression(fields[i], "get", new Object[] { null });
					}
				}
			}
			catch(IllegalAccessException ex) 
			{
				ex.printStackTrace();
			}
			return null;
		}
            
		@Override
		protected boolean mutatesTo(Object appesOldInstance, Object appesNewInstance)
		{
			return appesOldInstance == appesNewInstance;
		}
	};
         
	private ServicePersistence() {}
	

	public static Graph read(InputStream appesIn) throws IOException
	{
		assert appesIn != null;
		try( XMLDecoder reader = new XMLDecoder(appesIn) )
		{
			Graph graph = (Graph) reader.readObject();
			return graph;
		}
		finally
		{
			appesIn.close();
		}
	}
	

	public static void saveFile(Graph appesGraph, OutputStream appesOut)
	{
		XMLEncoder encoder = new XMLEncoder(appesOut);

		encoder.setPersistenceDelegate(StyleLine.class, FieldDelegatestatic);
		encoder.setPersistenceDelegate(ArHead.class, FieldDelegatestatic);
      
		Graph.setPersistenceDelegate(encoder);
		AbstractNode.setPersistenceDelegate(encoder);
		NodesPackage.setPersistenceDelegate(encoder);
		NodeObject.setPersistenceDelegate(encoder);
		NodeImplicitParameter.setPersistenceDelegate(encoder);
		
		encoder.writeObject(appesGraph);
		encoder.close();
	}
}
