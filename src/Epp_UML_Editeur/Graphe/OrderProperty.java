
package Epp_UML_Editeur.Graphe;

import java.util.HashMap;


public final class OrderProperty
{
	private static final OrderProperty INSTANCE_ORDER_PROPERTY = new OrderProperty();
	
	static
	{
		INSTANCE_ORDER_PROPERTY.addIndex(EdgeRelationshipClass.class, "startLabel", 1);
		INSTANCE_ORDER_PROPERTY.addIndex(EdgeRelationshipClass.class, "middleLabel", 2);
		INSTANCE_ORDER_PROPERTY.addIndex(EdgeRelationshipClass.class, "endLabel", 3);
		INSTANCE_ORDER_PROPERTY.addIndex(EdgeCall.class, "middleLabel", 1);
		INSTANCE_ORDER_PROPERTY.addIndex(ClassNode.class, "name", 1);
		INSTANCE_ORDER_PROPERTY.addIndex(ClassNode.class, "attributes", 2);
		INSTANCE_ORDER_PROPERTY.addIndex(ClassNode.class, "methods", 3);
		INSTANCE_ORDER_PROPERTY.addIndex(InterfaceNode.class, "name", 1);
		INSTANCE_ORDER_PROPERTY.addIndex(InterfaceNode.class, "methods", 2);
		INSTANCE_ORDER_PROPERTY.addIndex(NodesPackage.class, "name", 1);
		INSTANCE_ORDER_PROPERTY.addIndex(NodesPackage.class, "contents", 2);
	}
	
	private HashMap<Class<?>, HashMap<String, Integer>> appesProperties = new HashMap<>();
	
	private OrderProperty()
	{}
	

	public static OrderProperty getInstanceOrderProperty()
	{
		return INSTANCE_ORDER_PROPERTY;
	}
	

	public int getIndex( Class<?> appesClass, String appesProperty )
	{
		if( appesClass == null )
		{
			return 0;
		}
		HashMap<String, Integer> properties = appesProperties.get(appesClass);
		if( properties != null )
		{
			if( properties.containsKey(appesProperty))
			{
				return properties.get(appesProperty);
			}
			else
			{
				return getIndex(appesClass.getSuperclass(), appesProperty);
			}
		}
		else
		{
			return getIndex(appesClass.getSuperclass(), appesProperty);
		}
	}
	
	private void addIndex( Class<?> appesClass, String appesProperty, int appesIndex )
	{
		HashMap<String, Integer> properties = appesProperties.get(appesClass);
		if( properties == null )
		{
			properties = new HashMap<String, Integer>();
			appesProperties.put(appesClass, properties);
		}
		properties.put(appesProperty, appesIndex);
	}
}
