
package Epp_UML_Editeur.LeFramework;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.ElementGraph;
import Epp_UML_Editeur.Graphe.Node;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.NodePoint;


public class BarTool extends JPanel
{
	private static final int SIZE_BUTTON = 25;
	private static final int SET_OFF = 3;
	private static final int PADDING = 5;
	private static final int COMMAND_VERTICAL_SEPARATOR = 15;
	private static final int SIZE_FONT = 14;

	private static final String EXPAND = "<<";
	private static final String COLLAPSE = ">>";
	
	private ArrayList<JToggleButton> appesButtons = new ArrayList<>();
	private ArrayList<JToggleButton> appesButtonsEx = new ArrayList<>();
	private JPanel appesToolPanel = new JPanel(new GridLayout(0, 1));
	private JPanel appesToolPanelEx = new JPanel(new GridLayout(0, 1));
	private ArrayList<ElementGraph> appesTools = new ArrayList<>();
	private JPopupMenu appesPopupMenu = new JPopupMenu();


	public BarTool(Graph appesGraph)
	{
		ButtonGroup groupBu = new ButtonGroup();
		ButtonGroup groupEx = new ButtonGroup();
		setLayout(new BorderLayout());
		createToolSelection(groupBu, groupEx);
		createToolsNodesAndEdges(appesGraph, groupBu, groupEx);
		addandCopyToClipboard();
		createExpandButton();
		freeCtrlTab();
		add(appesToolPanel, BorderLayout.NORTH);
	}
	
	private static Icon createSSelectionIcon()
	{
		return new Icon()
		{
			public int getIconHeight() 
			{ return SIZE_BUTTON; }
            
			public int getIconWidth() 
			{ return SIZE_BUTTON; }
            
			public void paintIcon(Component appesComponent, Graphics appesGraphics, int x, int y)
            {
				int offset = SET_OFF +3;
				Graphics2D abel = (Graphics2D)appesGraphics;
				PanelGraph.drawGrabber(abel, x + offset, y + offset);
				PanelGraph.drawGrabber(abel, x + offset, y + SIZE_BUTTON - offset);
				PanelGraph.drawGrabber(abel, x + SIZE_BUTTON - offset, y + offset);
				PanelGraph.drawGrabber(abel, x + SIZE_BUTTON - offset, y + SIZE_BUTTON - offset);
            }
		};
	}
	
	private static Icon createNodeIcon( final Node appesNode )
	{
		return new Icon()
		{
            public int getIconHeight() 
            { return SIZE_BUTTON; }
            
            public int getIconWidth() 
            { return SIZE_BUTTON; }
            
            public void paintIcon(Component appesComponent, Graphics appesGraphics, int x, int y)
            {
            	double width = appesNode.getBounds().getWidth();
            	double height = appesNode.getBounds().getHeight();
               	Graphics2D abel = (Graphics2D)appesGraphics;
               	abel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               	double scalX = (SIZE_BUTTON - SET_OFF)/ width;
               	double scalY = (SIZE_BUTTON - SET_OFF)/ height;
               	double scal = Math.min(scalX, scalY);

               	AffineTransform transformOld = abel.getTransform();
               	abel.translate(x , y);
               	abel.scale(scal, scal);
               	
               	abel.translate(Math.max((height - width) / 2, 0), Math.max((width - height) / 2, 0));
               	abel.setColor(Color.black);
               	appesNode.graphics2dDraw(abel);
               	abel.setTransform(transformOld);
            }
		};
	}

	private static Icon createIconEdge(final Edge appesEdge)
	{
		return new Icon()
        {
           public int getIconHeight() 
           { return SIZE_BUTTON; }
           
           public int getIconWidth() 
           { return SIZE_BUTTON; }
           
           public void paintIcon(Component appesComponent, Graphics appesGraphics, int x, int y)
           {
           	Graphics2D abel = (Graphics2D)appesGraphics;
           	abel.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
           	
           	NodePoint ppsNode = new NodePoint();
           	ppsNode.AppCommandtranslate(SET_OFF, SET_OFF);
           	NodePoint qqsNode = new NodePoint();
           	qqsNode.AppCommandtranslate(SIZE_BUTTON - SET_OFF, SIZE_BUTTON - SET_OFF);
           	appesEdge.connect(ppsNode, qqsNode);
              
           	Rectangle2D appesbounds = new Rectangle2D.Double();
           	appesbounds.add(ppsNode.getBounds());
           	appesbounds.add(qqsNode.getBounds());
           	appesbounds.add(appesEdge.getBounds());
              
           	double width = appesbounds.getWidth();
           	double height = appesbounds.getHeight();
           	double scalX = (SIZE_BUTTON - SET_OFF)/ width;
           	double scalY = (SIZE_BUTTON - SET_OFF)/ height;
           	double scal = Math.min(scalX, scalY);

           	AffineTransform oldTransform = abel.getTransform();
           	abel.translate(x, y);
           	abel.scale(scal, scal);
           	abel.translate(Math.max((height - width) / 2, 0), Math.max((width - height) / 2, 0));
                             
           	abel.setColor(Color.black);
           	appesEdge.draw(abel);
           	abel.setTransform(oldTransform);
           }
        };
	}
	
	private void createToolSelection(ButtonGroup appesGroup, ButtonGroup appesGroupEx)
	{
		installTool(createSSelectionIcon(),
				ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings").getString("grabber.tooltip"),
				null, true, appesGroup, appesGroupEx);
	}
	

	private void installTool( Icon appesIcon, String appesToolTip, ElementGraph appesTool, boolean appesIsSelected, ButtonGroup appesCollapsed, ButtonGroup appesExpanded )
	{
		final JToggleButton button = new JToggleButton(appesIcon);
		button.setToolTipText(appesToolTip);
		appesCollapsed.add(button);
		appesButtons.add(button);
		appesToolPanel.add(button);
		button.setSelected(appesIsSelected);
		appesTools.add(appesTool);
		
		final JToggleButton buttonEx = new JToggleButton(appesIcon);
		buttonEx.setToolTipText(appesToolTip);
		appesExpanded.add(buttonEx);
		appesButtonsEx.add(buttonEx);
		
		appesToolPanelEx.add(createExpandedRowElement(buttonEx, appesToolTip));
		buttonEx.setSelected(appesIsSelected);
      
		JMenuItem item = new JMenuItem(appesToolTip, appesIcon);
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent appesEvent)
			{
				button.setSelected(true);
				buttonEx.setSelected(true);
			}
		});
		appesPopupMenu.add(item);
	}
	

	private JPanel createExpandedRowElement(JComponent pButton, String pToolTip)
	{
		JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		linePanel.add(pButton);
		JLabel label = new JLabel(pToolTip);
		Font font = new Font(label.getFont().getFontName(), Font.PLAIN, SIZE_FONT);
		label.setFont(font);
		label.setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, PADDING));
		linePanel.add(label);
		return linePanel;
	}
	
	private void createToolsNodesAndEdges(Graph pGraph, ButtonGroup pGroup, ButtonGroup pGroupEx)
	{
		ResourceBundle resources = ResourceBundle.getBundle(pGraph.getClass().getName() + "Strings");

		Node[] nodeTypes = pGraph.getPrototypNodes();
		for(int i = 0; i < nodeTypes.length; i++)
		{
			installTool(createNodeIcon(nodeTypes[i]), resources.getString("node" + (i + 1) + ".tooltip"), 
					nodeTypes[i], false, pGroup, pGroupEx);
		}
		
		Edge[] edgeTypes = pGraph.getPrototypEdges();
		for(int i = 0; i < edgeTypes.length; i++)
		{
			installTool(createIconEdge(edgeTypes[i]), resources.getString("edge" + (i + 1) + ".tooltip"),
					edgeTypes[i], false, pGroup, pGroupEx);
		}
	}
	

	private void freeCtrlTab()
	{
		Set<AWTKeyStroke> oldKeys = getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		HashSet<AWTKeyStroke> newKeys = new HashSet<>();
		newKeys.addAll(oldKeys);
		newKeys.remove(KeyStroke.getKeyStroke("ctrl TAB"));
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, newKeys);
		oldKeys = getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
		newKeys = new HashSet<>();
		newKeys.addAll(oldKeys);
		newKeys.remove(KeyStroke.getKeyStroke("ctrl shift TAB"));
		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, newKeys); 
	}
	

	public ElementGraph getSelectedTool()
	{
		return appesTools.get(getSelectedButtonIndex());
	}
	

	public void setToolToBeSelect()
	{
		for( JToggleButton button : appesButtons)
		{
			button.setSelected(false);
		}
		for( JToggleButton button : appesButtonsEx)
		{
			button.setSelected(false);
		}
		appesButtons.get(0).setSelected(true);
		appesButtonsEx.get(0).setSelected(true);
	}

	private void addandCopyToClipboard()
	{

		appesToolPanel.add(Box.createRigidArea(new Dimension(0, COMMAND_VERTICAL_SEPARATOR)));
		appesToolPanelEx.add(Box.createRigidArea(new Dimension(0, COMMAND_VERTICAL_SEPARATOR)));

		URL imageLocation = getClass().getClassLoader().
				getResource(ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings").
						getString("toolbar.copyToClipBoard"));
		String toolTip = ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings").getString("file.copy_to_clipboard.text");
		
		final JButton button = new JButton(new ImageIcon(imageLocation));
		button.setToolTipText(toolTip);
		appesToolPanel.add(button);

		
		final JButton buttonEx = new JButton(new ImageIcon(imageLocation));
		buttonEx.setToolTipText(toolTip);
		appesToolPanelEx.add(createExpandedRowElement(buttonEx, toolTip));

		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				copyToClipboard();
			}
		});

		buttonEx.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				copyToClipboard();
			}
		});
	}
	
	private void copyToClipboard()
	{

		Container parent = getParent();
		while( parent.getClass() != FrameEditeur.class )
		{
			parent = parent.getParent();
		}
		((FrameEditeur)parent).copyToClipboard();
	}
		
	private void createExpandButton()
	{
		final JButton expandButton = new JButton(EXPAND);
		expandButton.setAlignmentX(CENTER_ALIGNMENT);
		final String expandString = ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings").getString("toolbar.expand");
		final String collapseString = ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings").getString("toolbar.collapse");
		expandButton.setToolTipText(expandString);
		expandButton.setPreferredSize(new Dimension(SIZE_BUTTON, SIZE_BUTTON));
		expandButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				if(expandButton.getText().equals(EXPAND))
				{
					synchronizeToolSelection();
					expandButton.setText(COLLAPSE);
					expandButton.setToolTipText(collapseString);
					remove(appesToolPanel);
					add(appesToolPanelEx, BorderLayout.NORTH);
				}
				else
				{
					synchronizeToolSelection();
					expandButton.setText(EXPAND);
					expandButton.setToolTipText(expandString);
					remove(appesToolPanelEx);
					add(appesToolPanel, BorderLayout.NORTH);
				}
			}
		});
		add(expandButton, BorderLayout.SOUTH);
	}
	
	private void synchronizeToolSelection()
	{
		int index = getSelectedButtonIndex();
		assert index >= 0;
		appesButtons.get(index).setSelected(true);
		appesButtonsEx.get(index).setSelected(true);
	}
	
	private int getSelectedButtonIndex()
	{
		ArrayList<JToggleButton> activeButtons = appesButtons;
		if( isExpanded() )
		{
			activeButtons = appesButtonsEx;
		}
		
		for(int i = 0; i < activeButtons.size(); i++)
		{
			JToggleButton button = activeButtons.get(i);
			if(button.isSelected())
			{
				return i;
			}
		}
		return -1;
	}
	

	private boolean isExpanded()
	{
		for( Component component : getComponents() )
		{
			if( component == appesToolPanelEx)
			{
				return true;
			}
		}
		return false;
	}
	

	public void showPopup(PanelGraph appesPanel, Point2D appesPoint)
	{
		appesPopupMenu.show(appesPanel, (int) appesPoint.getX(), (int) appesPoint.getY());
	}
}