
package Epp_UML_Editeur.LeFramework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;



public class BienvenueTab extends JInternalFrame
{
	private static final int MARGIN_BORDER_TAB = 45;
	private static final int MARGIN_ALTERNATIVE_BORDER_TAB = 30;
	private static final int MARGIN_FOOT_BORDER_TAB = 10;
	private static final int SIZE_FONT = 25;
	private ResourceBundle appesBienvenueResources;
    private JPanel appesFootTextPanel;
    private JPanel appesRightTitlePanel;
    private JPanel appesLeftTitlePanel;
    private JPanel appesLeftPanel;
    private JPanel appesRightPanel;
    private JMenu appesNewFileMenu;
    private JMenu appesRecentFileMenu;
    private ImageIcon appesLeftPanelIcon;
    private ImageIcon appesRightPanelIcon;
    private String appesFootText;
    

	public BienvenueTab(JMenu appesNewFileMenu, JMenu appesRecentFileMenu)
	{
		appesBienvenueResources = ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings");
		appesLeftPanelIcon = new ImageIcon(getClass().getClassLoader().getResource(appesBienvenueResources.getString("welcome.create.icon")));
		appesRightPanelIcon = new ImageIcon(getClass().getClassLoader().getResource(appesBienvenueResources.getString("welcome.open.icon")));
	    setOpaque(false);
	    setLayout(new BorderLayout());
	    
	    BasicInternalFrameUI nu = (BasicInternalFrameUI)getUI();
	    Container north = nu.getNorthPane();
	    north.remove(0);
	    north.validate();
	    north.repaint();
	
	    this.appesNewFileMenu = appesNewFileMenu;
	    this.appesRecentFileMenu = appesRecentFileMenu;
	    JPanel panel = new JPanel();
	    panel.setLayout(new GridBagLayout());
	    panel.setOpaque(false);
	
	    JPanel Panelshortcut = new JPanel();
	    Panelshortcut.setOpaque(false);
	    Panelshortcut.setLayout(new GridLayout(2, 2));
	    Panelshortcut.add(getLeftTitlePanel());
	    Panelshortcut.add(getRightTitlePanel());
	    Panelshortcut.add(getLeftPanel());
	    Panelshortcut.add(getRightPanel());
	    GridBagConstraints bel = new GridBagConstraints();
	    bel.anchor = GridBagConstraints.NORTH;
	    bel.weightx = 1;
	    bel.gridx = 0;
	    bel.gridy = 1;
	    panel.add(Panelshortcut, bel);
	
	    add(panel, BorderLayout.NORTH);
	    add(getFootTextPanel(), BorderLayout.SOUTH);
	    setComponentPopupMenu( null );
	}
	
	private JPanel getLeftPanel()
	{
		if(appesLeftPanel == null)
		{
			appesLeftPanel = new JPanel();
			appesLeftPanel.setOpaque(false);
			appesLeftPanel.setLayout(new BoxLayout(appesLeftPanel, BoxLayout.Y_AXIS));
			appesLeftPanel.setBorder(new EmptyBorder(0, 0, 0, MARGIN_BORDER_TAB));

			for(int i = 0; i < appesNewFileMenu.getItemCount(); i++)
			{
				final JMenuItem item = appesNewFileMenu.getItem(i);
				String label = item.getText();
				JButton newDiagramShortcut = new JButton(label.toLowerCase());
				newDiagramShortcut.setUI(new BienvenueButtonUI());
				newDiagramShortcut.setAlignmentX(Component.RIGHT_ALIGNMENT);
				newDiagramShortcut.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent pEvent)
					{
						item.doClick();
					}
				});
				appesLeftPanel.add(newDiagramShortcut);
			}
		}
		return appesLeftPanel;
	}

	private JPanel getRightPanel()
	{
		if(appesRightPanel == null)
		{
			appesRightPanel = new JPanel();
			appesRightPanel.setOpaque(false);
			appesRightPanel.setLayout(new BoxLayout(appesRightPanel, BoxLayout.Y_AXIS));
			appesRightPanel.setBorder(new EmptyBorder(0, MARGIN_BORDER_TAB, 0, MARGIN_BORDER_TAB));

			for(int i = 0; i < appesRecentFileMenu.getItemCount(); i++)
			{
				final JMenuItem item = appesRecentFileMenu.getItem(i);
				String label = item.getText().substring(2);
				JButton fileShortcut = new JButton(label.toLowerCase());
				fileShortcut.setUI(new BienvenueButtonUI());
				fileShortcut.setAlignmentX(Component.LEFT_ALIGNMENT);
				fileShortcut.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent appesEvent)
					{
						item.doClick();
					}
				});
				appesRightPanel.add(fileShortcut);
			}

		}
		return this.appesRightPanel;
	}

	private JPanel getLeftTitlePanel()
	{
		if(appesLeftTitlePanel == null)
		{
			JLabel icon = new JLabel();
			icon.setIcon(this.appesLeftPanelIcon);

			JLabel title = new JLabel(appesNewFileMenu.getText().toLowerCase());
			title.setFont(new Font("Arial", Font.PLAIN, SIZE_FONT));
			title.setForeground(Color.DARK_GRAY);
			title.setBorder(new EmptyBorder(0, MARGIN_ALTERNATIVE_BORDER_TAB, 0, 0));

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.add(icon);
			panel.add(title);
			panel.setOpaque(false);

			appesLeftTitlePanel = new JPanel();
			appesLeftTitlePanel.setOpaque(false);
			appesLeftTitlePanel.setLayout(new BorderLayout());
			appesLeftTitlePanel.add(panel, BorderLayout.EAST);
			appesLeftTitlePanel.setBorder(new EmptyBorder(0, 0, MARGIN_ALTERNATIVE_BORDER_TAB, MARGIN_BORDER_TAB));
		}
		return appesLeftTitlePanel;
	}

	private JPanel getRightTitlePanel()
	{
		if(appesRightTitlePanel == null)
		{
			JLabel icon = new JLabel();
			icon.setIcon(this.appesRightPanelIcon);
			icon.setAlignmentX(Component.LEFT_ALIGNMENT);

			JLabel title = new JLabel(appesRecentFileMenu.getText().toLowerCase());
			title.setFont(new Font("Arial", Font.PLAIN, SIZE_FONT));
			title.setForeground(Color.DARK_GRAY);
			title.setBorder(new EmptyBorder(0, 0, 0, MARGIN_ALTERNATIVE_BORDER_TAB));

			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.add(title);
			panel.add(icon);
			panel.setOpaque(false);

			appesRightTitlePanel = new JPanel();
			appesRightTitlePanel.setOpaque(false);
			appesRightTitlePanel.setLayout(new BorderLayout());
			appesRightTitlePanel.add(panel, BorderLayout.WEST);
			appesRightTitlePanel.setBorder(new EmptyBorder(0, MARGIN_BORDER_TAB, MARGIN_ALTERNATIVE_BORDER_TAB, 0));
		}
		return appesRightTitlePanel;
	}

	private JPanel getFootTextPanel()
	{
		if(appesFootTextPanel == null)
		{
			appesFootText = appesBienvenueResources.getString("welcome.copyright");
			appesFootTextPanel = new JPanel();
			appesFootTextPanel.setOpaque(false);
			appesFootTextPanel.setBorder(new EmptyBorder(0, 0, MARGIN_FOOT_BORDER_TAB, 0));
			appesFootTextPanel.setLayout(new BoxLayout(this.appesFootTextPanel, BoxLayout.Y_AXIS));
			appesFootTextPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel text = new JLabel(this.appesFootText);
			text.setAlignmentX(Component.CENTER_ALIGNMENT);

			appesFootTextPanel.add(text);
		}

		return appesFootTextPanel;
	}

}	
