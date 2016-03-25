

package Epp_UML_Editeur.LeFramework;

import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


class FactMenu
{
	private ResourceBundle aBundle;
	private final String aSystem; 
	

	FactMenu(ResourceBundle pBundle)
	{
		aBundle = pBundle;
		aSystem = System.getProperty("os.name").toLowerCase();
	}


	public JMenuItem createMenuItem(String pPrefix, Object pTarget, String pMethodName)
	{
		return createMenuItem(pPrefix, EventHandler.create(ActionListener.class, pTarget, pMethodName));
	}


	public JMenuItem createMenuItem(String pPrefix, ActionListener pListener)
	{
		String text = aBundle.getString(pPrefix + ".text");
		JMenuItem menuItem = new JMenuItem(text);
		return configure(menuItem, pPrefix, pListener);
	}


	public JMenuItem createCheckBoxMenuItem(String pPrefix, ActionListener pListener)
	{
		String text = aBundle.getString(pPrefix + ".text");
		JMenuItem menuItem = new JCheckBoxMenuItem(text);
		return configure(menuItem, pPrefix, pListener);
	}	


	private JMenuItem configure(JMenuItem pMenuItem, String pPrefix, ActionListener pListener)
	{
		pMenuItem.addActionListener(pListener);
		if( aBundle.containsKey(pPrefix + ".mnemonic"))
		{
			pMenuItem.setMnemonic(aBundle.getString(pPrefix + ".mnemonic").charAt(0));
		}
		if( aBundle.containsKey(pPrefix + ".accelerator.mac"))
		{
			if(aSystem.indexOf("mac") >= 0)
			{
				pMenuItem.setAccelerator(KeyStroke.getKeyStroke(aBundle.getString(pPrefix + ".accelerator.mac")));	
			}
			else
			{
				pMenuItem.setAccelerator(KeyStroke.getKeyStroke(aBundle.getString(pPrefix + ".accelerator.win")));
			}
			
		}
		if( aBundle.containsKey(pPrefix + ".tooltip"))
		{
			pMenuItem.setToolTipText(aBundle.getString(pPrefix + ".tooltip"));         
		}
		if( aBundle.containsKey(pPrefix + ".icon"))
		{
			pMenuItem.setIcon(new ImageIcon(getClass().getClassLoader().getResource(aBundle.getString(pPrefix + ".icon"))));
		}
		return pMenuItem;
	}
	

	public JMenu createMenu(String pPrefix)
	{
		String text = aBundle.getString(pPrefix + ".text");
		JMenu menu = new JMenu(text);
		if( aBundle.containsKey(pPrefix + ".mnemonic"))
		{
			menu.setMnemonic(aBundle.getString(pPrefix + ".mnemonic").charAt(0));
		}
		if( aBundle.containsKey(pPrefix + ".tooltip"))
      	{
      		menu.setToolTipText(aBundle.getString(pPrefix + ".tooltip"));         
      	}
		if( aBundle.containsKey(pPrefix + ".accelerator.mac"))
		{
			if(aSystem.indexOf("mac") >= 0)
			{
				menu.setAccelerator(KeyStroke.getKeyStroke(aBundle.getString(pPrefix + ".accelerator.mac")));	
			}
			else
			{
				menu.setAccelerator(KeyStroke.getKeyStroke(aBundle.getString(pPrefix + ".accelerator.win")));
			}
		}
		if( aBundle.containsKey(pPrefix + ".icon"))
		{
			menu.setIcon(new ImageIcon(getClass().getClassLoader().getResource(aBundle.getString(pPrefix + ".icon"))));
		}

		return menu;
	}
}
