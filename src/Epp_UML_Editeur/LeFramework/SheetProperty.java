
package Epp_UML_Editeur.LeFramework;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Epp_UML_Editeur.Graphe.OrderProperty;


@SuppressWarnings("serial")
public class SheetProperty extends JPanel
{
	private static final String PROPERTY_INVISIBLE_MARKER = "**INVISIBLE**";
	private static Map<Class<?>, Class<?>> editeurs;
	private static ResourceBundle appesNamesProperty = ResourceBundle.getBundle("Epp_UML_Editeur.graph.GraphElementProperties");

	private ArrayList<ChangeListener> appesListenersChange = new ArrayList<>();
	
	static
	{  
	      editeurs = new HashMap<>();
	      editeurs.put(String.class, PropertyEditorSupport.class);
	}
	

	public SheetProperty(final Object appesBean)
	{
		setLayout(new FormLayout());
		try
		{
			PropertyDescriptor[] descripteurs = Introspector.getBeanInfo(appesBean.getClass()).getPropertyDescriptors().clone();
			Arrays.sort(descripteurs, new Comparator<PropertyDescriptor>()
			{
				public int compare(PropertyDescriptor appesDescriptor1, PropertyDescriptor appesDescriptor2)
				{
					int index1 = OrderProperty.getInstanceOrderProperty().getIndex(appesBean.getClass(), appesDescriptor1.getName());
					int index2 = OrderProperty.getInstanceOrderProperty().getIndex(appesBean.getClass(), appesDescriptor2.getName());
					if( index1 == index2 )
					{
						return appesDescriptor1.getName().compareTo(appesDescriptor2.getName());
					}
					else
					{
						return index1 - index2;
					}
				}
			});
			
			for(PropertyDescriptor descripteur : descripteurs)
			{
				PropertyEditor editeur = getEditeur(appesBean, descripteur);
				String propertyName = getNameProperty(appesBean.getClass(), descripteur.getName());
				if(editeur != null && !propertyName.equals(PROPERTY_INVISIBLE_MARKER))
				{
					add(new JLabel(propertyName));
					add(getEditorComponent(editeur));
				}
			}		
		}
		catch (IntrospectionException exception)
		{

		}
	}


	public PropertyEditor getEditeur(final Object appesBean, PropertyDescriptor appesDescripteur)
	{
		try
		{
			final Method getter = appesDescripteur.getReadMethod();
			final Method setter = appesDescripteur.getWriteMethod();
			if(getter == null || setter == null )
			{
				return null;
			}
			
			Class<?> type = appesDescripteur.getPropertyType();
			final PropertyEditor editeur;
			Class<?> editeurClass = appesDescripteur.getPropertyEditorClass();
			if(editeurClass == null && editeurs.containsKey(type))
			{
				editeurClass = editeurs.get(type);
			}
			if(editeurClass != null)
			{
				editeur = (PropertyEditor) editeurClass.newInstance();
			}
			else
			{
				editeur = PropertyEditorManager.findEditor(type);
			}
			if(editeur == null)
			{
				return null;
			}

			Object value = getter.invoke(appesBean, new Object[] {});
			editeur.setValue(value);
			editeur.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent pEvent) {
					try {
						setter.invoke(appesBean, new Object[]{editeur.getValue()});
						fireStateChanged(null);
					} catch (IllegalAccessException | InvocationTargetException exception) {
						exception.printStackTrace();
					}
				}
			});
			return editeur;
		}
		catch(InstantiationException | IllegalAccessException | InvocationTargetException exception)
		{
			return null;
		}
	}


	public Component getEditorComponent(final PropertyEditor appesEditeur)
	{      
		String[] tags = appesEditeur.getTags();
		String text = appesEditeur.getAsText();
		if(appesEditeur.supportsCustomEditor())
		{
			return appesEditeur.getCustomEditor();
         
		}
		else if(tags != null)
		{

			final JComboBox<String> comboBox = new JComboBox<>(tags);
			comboBox.setSelectedItem(text);
			comboBox.addItemListener(new ItemListener()
            	{
					public void itemStateChanged(ItemEvent pEvent)
					{
						if(pEvent.getStateChange() == ItemEvent.SELECTED)
						{
							appesEditeur.setAsText((String)comboBox.getSelectedItem());
						}
					}
            	});
			return comboBox;
		}
		else 
		{
			final JTextField textField = new JTextField(text, 10);
			textField.getDocument().addDocumentListener(new DocumentListener()
            	{
					public void insertUpdate(DocumentEvent pEvent) 
					{
						appesEditeur.setAsText(textField.getText());
					}
					public void removeUpdate(DocumentEvent pEvent) 
					{
						appesEditeur.setAsText(textField.getText());
					}
					public void changedUpdate(DocumentEvent pEvent) 
					{}
            	});
			return textField;
		}
	}


	public void ChangeListeneradded(ChangeListener appesListener)
	{
		appesListenersChange.add(appesListener);
	}


	private void fireStateChanged(ChangeEvent appesEvent)
	{
		for(ChangeListener listener : appesListenersChange)
		{
			listener.stateChanged(appesEvent);
		}
	}


	public boolean empty()
	{
		return getComponentCount() == 0;
	}
	

	private static String getNameProperty(Class<?> appesClass, String appesProperty)
	{
		assert appesProperty != null;
		if( appesClass == null )
		{
			return appesProperty;
		}
		String key = appesClass.getSimpleName() + "." + appesProperty;
		if( !appesNamesProperty.containsKey(key) )
		{
			return getNameProperty(appesClass.getSuperclass(), appesProperty);
		}
		else
		{
			return appesNamesProperty.getString(key);
		}
	}
}

