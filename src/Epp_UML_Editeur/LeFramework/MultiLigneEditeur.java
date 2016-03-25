
package Epp_UML_Editeur.LeFramework;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class MultiLigneEditeur extends PropertyEditorSupport
{
	private static final int ROWS = 5;
	private static final int COLUMNS = 30;

	private static Set<AWTKeyStroke> tab = new HashSet<>(1);
	private static Set<AWTKeyStroke> shiftTab = new HashSet<>(1);

	static 
	{
		tab.add(KeyStroke.getKeyStroke("TAB" ));
		shiftTab.add(KeyStroke.getKeyStroke( "shift TAB" ));
	} 

	@Override
	public boolean supportsCustomEditor()
	{
		return true;
	}

	@Override
	public Component getCustomEditor()
	{
		final MultiLigne value = (MultiLigne)getValue();
		final JTextArea textArea = new JTextArea(ROWS, COLUMNS);

		textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, tab);
		textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, shiftTab);

		textArea.setText(value.getText());
		textArea.getDocument().addDocumentListener(new DocumentListener()
		{
			public void insertUpdate(DocumentEvent pEvent) 
			{
				value.setText(textArea.getText());
				firePropertyChange();
			}
			public void removeUpdate(DocumentEvent pEvent) 
			{
				value.setText(textArea.getText());
				firePropertyChange();
			}
			public void changedUpdate(DocumentEvent pEvent) 
			{}
		});
		return new JScrollPane(textArea);
	}
}
