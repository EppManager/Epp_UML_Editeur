
package Epp_UML_Editeur.LesCommandes;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import Epp_UML_Editeur.Graphe.Graph;


public class ChangePropertiesComd implements Command
{
	private Graph graphapps;
	private Object objectsapps;
	private Object valueaPropPrev;
	private Object ValueaPropNew;
	private int anIdex;


	public ChangePropertiesComd(Graph graphapps, Object objectsapps, Object valueaPropPrev, Object ValueaPropNew, int anIdex)
	{
		this.graphapps = graphapps;
		this.objectsapps = objectsapps;
		this.valueaPropPrev = valueaPropPrev;
		this.ValueaPropNew = ValueaPropNew;
		this.anIdex = anIdex;
	}



	public void undo() 
	{
		BeanInfo info;
		try 
		{
			info = Introspector.getBeanInfo(objectsapps.getClass());
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors().clone();  
			final Method setter = descriptors[anIdex].getWriteMethod();
			if (setter != null)
			{
				setter.invoke(objectsapps, new Object[] {valueaPropPrev});
			}
		}
		catch (IntrospectionException e) 
		{
			e.printStackTrace();
			return;
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
		{
			e.printStackTrace();
			return;
		}
		graphapps.layout();
	}


	public void execute() 
	{
		BeanInfo info;
		try 
		{
			info = Introspector.getBeanInfo(objectsapps.getClass());
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors().clone();  
			final Method setter = descriptors[anIdex].getWriteMethod();
			if(setter!= null)
			{
				setter.invoke(objectsapps, new Object[] {ValueaPropNew});
			}
		}
		catch (IntrospectionException e) 
		{
			e.printStackTrace();
			return;
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
		{
			e.printStackTrace();
			return;
		}
		graphapps.layout();
	}

}
