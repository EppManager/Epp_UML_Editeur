

package Epp_UML_Editeur.LeFramework;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import Epp_UML_Editeur.LesDiagrammes.GraphDiagramObject;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramState;
import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.FinalClassUML;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramClass;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramUseCase;
import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.ElementGraph;
import Epp_UML_Editeur.Graphe.Node;


@SuppressWarnings("serial")
public class FrameEditeur extends JFrame
{
	private static final int FRAM_GP = 20;
	private static final int FRAMES_ESTIMATE = 5;
	private static final int FILES_MAX_RECENT = 8;
	private static final double FACTOR_GROW_SCALE = Math.sqrt(2);
	private static final int SCREEN_MARGIN = 8;
	private static final int IMAGE_MARGIN = 2;
	private static final int HELP_MENU_TEXT_WIDTH = 10;
	private static final int HELP_MENU_TEXT_HEIGHT = 40;
	
	
	private FactMenu FactoApp;
	private ResourceBundle appesResourcesApp;
	private ResourceBundle appesResourcesVersion;
	private ResourceBundle appesResourcesEditeur;
	private JTabbedPane appesTabbedPane;
	private ArrayList<JInternalFrame> aTabs = new ArrayList<>();
	private JMenu appesMenuNouv;
	private Clipboard appesClipboard = new Clipboard();
	
	private RecentesQueueFiles aFailesRecentes = new RecentesQueueFiles();
	private JMenu aFailesRecentesMenu;
	
	private BienvenueTab appesBienvenueTab;


	public FrameEditeur(Class<?> pAppClass)
	{  
		String aNameClass = pAppClass.getName();
		appesResourcesApp = ResourceBundle.getBundle(aNameClass + "Strings");
		FactoApp = new FactMenu(appesResourcesApp);
		appesResourcesVersion = ResourceBundle.getBundle(aNameClass + "Version");
		appesResourcesEditeur = ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings");
		FactMenu factory = new FactMenu(appesResourcesEditeur);
		
		aFailesRecentes.deserialize(Preferences.userNodeForPackage(FinalClassUML.class).get("recent", "").trim());
      
		setTitle(appesResourcesApp.getString("app.name"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();

		setBounds(screenWidth / (SCREEN_MARGIN *2), screenHeight / (SCREEN_MARGIN *2), (screenWidth * (SCREEN_MARGIN -1)) / SCREEN_MARGIN,
				(screenHeight * (SCREEN_MARGIN -1))/ SCREEN_MARGIN);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
            public void windowClosing(WindowEvent pEvent)
            {
               exit();
            }
		});

		appesTabbedPane = new JTabbedPane();
		setContentPane(appesTabbedPane);

     	setJMenuBar(new JMenuBar());
     	
		createFileMenu(factory);
		createEditMenu(factory);
		createViewMenu(factory);
     	createHelpMenu(factory);
	}
	
	private void createFileMenu(FactMenu pFactory)
	{
		JMenuBar menuBar = getJMenuBar();
     	JMenu fileMenu = pFactory.createMenu("file");
     	menuBar.add(fileMenu);

     	appesMenuNouv = pFactory.createMenu("file.new");
     	fileMenu.add(appesMenuNouv);

     	JMenuItem fileOpenItem = pFactory.createMenuItem("file.open", this, "openFile"); 
     	fileMenu.add(fileOpenItem);      

     	aFailesRecentesMenu = pFactory.createMenu("file.recent");
     	buildRecentFilesMenu();
     	fileMenu.add(aFailesRecentesMenu);
     	
     	JMenuItem closeFileItem = pFactory.createMenuItem("file.close", this, "close");
     	fileMenu.add(closeFileItem);
      
     	JMenuItem fileSaveItem = pFactory.createMenuItem("file.save", this, "save"); 
     	fileMenu.add(fileSaveItem);
     	JMenuItem fileSaveAsItem = pFactory.createMenuItem("file.save_as", this, "saveAs");
     	fileMenu.add(fileSaveAsItem);

     	JMenuItem fileExportItem = pFactory.createMenuItem("file.export_image", this, "exportImage"); 
     	fileMenu.add(fileExportItem);
     	
     	JMenuItem fileCopyToClipboard = pFactory.createMenuItem("file.copy_to_clipboard", this, "copyToClipboard"); 
     	fileMenu.add(fileCopyToClipboard);
     	
     	fileMenu.addSeparator();

     	JMenuItem fileExitItem = pFactory.createMenuItem("file.exit", this, "exit");
     	fileMenu.add(fileExitItem);
	}
	
	private void createEditMenu(FactMenu pFactory)
	{
		JMenuBar menuBar = getJMenuBar();
		JMenu editMenu = pFactory.createMenu("edit");
     	menuBar.add(editMenu);
     	
     	editMenu.add(pFactory.createMenuItem("edit.undo", new ActionListener()
     	{
     		public void actionPerformed(ActionEvent pEvent)
            {
               final aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
               if(frame == null)
               {
            	   return;
               }
               PanelGraph panel = frame.getGraphPanel();
               panel.undo();
            }
         }));
     	
     	editMenu.add(pFactory.createMenuItem("edit.redo", new ActionListener()
     	{
     		public void actionPerformed(ActionEvent pEvent)
            {
               final aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
               if(frame == null)
               {
            	   return;
               }
               PanelGraph panel = frame.getGraphPanel();
               panel.redo();
            }
         }));
     	
     	editMenu.add(pFactory.createMenuItem("edit.properties", new ActionListener()
     	{
     		public void actionPerformed(ActionEvent pEvent)
            {
               final aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
               if(frame == null)
               {
            	   return;
               }
               PanelGraph panel = frame.getGraphPanel();
               panel.selectedEdit();
            }
         }));
     	
     	editMenu.add(pFactory.createMenuItem("edit.cut", this, "cut"));
     	editMenu.add(pFactory.createMenuItem("edit.paste", this, "paste"));
     	editMenu.add(pFactory.createMenuItem("edit.copy", this, "copy"));
     	

     	editMenu.add(pFactory.createMenuItem("edit.delete", new ActionListener()
     	{
            public void actionPerformed(ActionEvent pEvent)
            {
               aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
               if(frame == null)
               {
            	   return;
               }
               PanelGraph panel = frame.getGraphPanel();
               panel.removeSelected();
            }
     	}));
	}
	
	private void createViewMenu(FactMenu pFactory)
	{
		JMenuBar menuBar = getJMenuBar();
		
		JMenu viewMenu = pFactory.createMenu("view");
     	menuBar.add(viewMenu);

     	viewMenu.add(pFactory.createMenuItem("view.zoom_out", new ActionListener()
     	{
            public void actionPerformed(ActionEvent pEvent)
            {
               aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
               if(frame == null)
               {
            	   return;
               }
               PanelGraph panel = frame.getGraphPanel();
               panel.changeZoom(-1);
            }
         }));

     	viewMenu.add(pFactory.createMenuItem("view.zoom_in", new ActionListener()
     	{
            public void actionPerformed(ActionEvent pEvent)
            {
               aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
               if(frame == null)
               {
            	   return;
               }
               PanelGraph panel = frame.getGraphPanel();
               panel.changeZoom(1);
            }
         }));
      
     	viewMenu.add(pFactory.createMenuItem("view.grow_drawing_area", new ActionListener()
     	{
     		public void actionPerformed(ActionEvent pEvent)
     		{
     			aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
     			if(frame == null)
				{
					return;
				}
     			Graph g = frame.getGraph();
     			Rectangle2D bounds = g.getBounds();
     			bounds.add(frame.getGraphPanel().getBounds());
     			g.setMinBounds(new Rectangle2D.Double(0, 0, FACTOR_GROW_SCALE * bounds.getWidth(), FACTOR_GROW_SCALE * bounds.getHeight()));
                frame.getGraphPanel().revalidate();
                frame.repaint();
     		}
     	}));
      
     	viewMenu.add(pFactory.createMenuItem("view.clip_drawing_area", new ActionListener()
     	{
     		public void actionPerformed(ActionEvent pEvent)
     		{
     			aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
     			if(frame == null)
				{
					return;
				}
                Graph g = frame.getGraph();
                g.setMinBounds(null); 
                frame.getGraphPanel().revalidate();
                frame.repaint();
     		}
     	}));

     	final JCheckBoxMenuItem hideGridItem  = (JCheckBoxMenuItem) pFactory.createCheckBoxMenuItem("view.hide_grid", new ActionListener()
     	{
            public void actionPerformed(ActionEvent pEvent)
            {
               aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
               if(frame == null)
               {
            	   return;
               }
               PanelGraph panel = frame.getGraphPanel();
               JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) pEvent.getSource();               
               panel.setHideGrid(menuItem.isSelected());
            }
        });
     	viewMenu.add(hideGridItem);

     	viewMenu.addMenuListener(new MenuListener()
     	{
     		public void menuSelected(MenuEvent pEvent)
            {
	     		if(appesTabbedPane.getSelectedComponent() instanceof BienvenueTab)
	     		{
	     			return;
	     		}	
     			aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
                if(frame == null)
				{
					return;
				}
                PanelGraph panel = frame.getGraphPanel();
                hideGridItem.setSelected(panel.getHideGrid());  
            }
     		public void menuDeselected(MenuEvent pEvent)
            {}
            public void menuCanceled(MenuEvent pEvent)
            {}
     	});
	}


	private void createHelpMenu(FactMenu pFactory)
	{
		JMenuBar menuBar = getJMenuBar();
		JMenu helpMenu = pFactory.createMenu("help");
		menuBar.add(helpMenu);

		helpMenu.add(pFactory.createMenuItem("help.about", this, "showAboutDialog"));
		helpMenu.add(pFactory.createMenuItem("help.cours", new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				try {
					Desktop.getDesktop().open(new File("F:\\cours uml.pdf"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));

	}
	

	public void addTypeGraph(String pResourceName, final Class<?> pGraphClass)
	{
		appesMenuNouv.add(FactoApp.createMenuItem(pResourceName, new ActionListener() {
			public void actionPerformed(ActionEvent pEvent) {
				try {
					aFramGraph frame = new aFramGraph((Graph) pGraphClass.newInstance(), appesTabbedPane);
					addTab(frame);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}));
	}
	

	public void setIcones()
    {
    	try
		{
			java.net.URL url = getClass().getClassLoader().getResource(appesResourcesApp.getString("app.icon"));
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(url);
			setIconImage(img);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }


	public void LireArgts(String[] pArgs)
	{
	   if(pArgs.length != 0)
	   {	
		   for(String argument : pArgs)
		   {
			   open(argument);
		   }
	   } 

   	}
   

	private void open(String pName)
	{	
		for(int i = 0; i < aTabs.size(); i++)
		{
			if(appesTabbedPane.getComponentAt(i) instanceof aFramGraph)
			{
				aFramGraph frame = (aFramGraph) appesTabbedPane.getComponentAt(i);
				if(frame.getFileName() != null && frame.getFileName().getAbsoluteFile().equals(new File(pName).getAbsoluteFile())) 
				{
					try
					{
						frame.toFront();
						frame.setSelected(true); 
						addRecentFile(new File(pName).getPath());
					}
					catch(PropertyVetoException exception)
					{}
					return;
				}
			}
		}	      
		try
		{	              
			Graph graph = ServicePersistence.read(new FileInputStream(pName));
			aFramGraph frame = new aFramGraph(graph, appesTabbedPane);
			frame.setFile(new File(pName).getAbsoluteFile());    
			addRecentFile(new File(pName).getPath());
			addTab(frame);
		}
		catch(IOException exception)
		{
			JOptionPane.showMessageDialog(appesTabbedPane, exception.getMessage(),
    			  appesResourcesEditeur.getString("file.open.text"), JOptionPane.ERROR_MESSAGE);
		}      
	}   


   private void addTab(final JInternalFrame pInternalFrame)
   {  
	   int frameCount = appesTabbedPane.getComponentCount();
	   BasicInternalFrameUI ui = (BasicInternalFrameUI)pInternalFrame.getUI();
	   Container north = ui.getNorthPane();
	   north.remove(0);
	   north.validate();
	   north.repaint();
	   appesTabbedPane.add(setTitle(pInternalFrame), pInternalFrame);
	   int i = aTabs.size();
	   appesTabbedPane.setTabComponentAt(i,
               new ComponentButtonTab(this, pInternalFrame, appesTabbedPane));
	   aTabs.add(pInternalFrame);

	   int emptySpace = FRAM_GP * Math.max(FRAMES_ESTIMATE, frameCount);
	   int width = Math.max(appesTabbedPane.getWidth() / 2, appesTabbedPane.getWidth() - emptySpace);
	   int height = Math.max(appesTabbedPane.getHeight() / 2, appesTabbedPane.getHeight() - emptySpace);

	   pInternalFrame.reshape(frameCount * FRAM_GP, frameCount * FRAM_GP, width, height);
	   pInternalFrame.show(); 
	   int last = aTabs.size();
	   appesTabbedPane.setSelectedIndex(last-1);
	   if(appesTabbedPane.getComponentAt(0) instanceof BienvenueTab)
	   {
		   BienvenueDeletTab();
	   }

   	}


   	private String setTitle(JInternalFrame appesInternalFrame)
   	{
   		String appName = appesResourcesApp.getString("app.name");
   		String diagramName;
   		
   		if(appesInternalFrame == null || !(appesInternalFrame instanceof aFramGraph))
   		{
   			return appName;
   		}
   		else
   		{
   			aFramGraph frame = (aFramGraph)appesInternalFrame;
   			File file = frame.getFileName();
   			if( file == null )
   			{
   				Graph graphType = frame.getGraph();
   				if(graphType instanceof GraphDiagramClass)
   				{
   					diagramName = "Class Diagram";
   				}
   				else if(graphType instanceof GraphDiagramObject)
   				{
   					diagramName = "Object Diagram";
   				}
   				else if(graphType instanceof GraphDiagramUseCase)
   				{
   					diagramName = "Use Case Diagram";
   				}
   				else if(graphType instanceof GraphDiagramState)
   				{
   					diagramName = "State Diagram";
   				}
   				else
   				{
   					diagramName =  "Sequence Diagram";
   				}
   				return diagramName;
   			}
   			else
   			{
   				return file.getName();
   			}
   		}
   }
   	

   	public void addWelcomeTab()
   	{
   		appesBienvenueTab = new BienvenueTab(appesMenuNouv, aFailesRecentesMenu);
     	appesTabbedPane.add("Bienvenue", appesBienvenueTab);
     	aTabs.add(appesBienvenueTab);
   	}
   
   	

   	public void BienvenueDeletTab()
   	{
   		if(appesBienvenueTab !=null)
   		{
   			appesTabbedPane.remove(0);
   			aTabs.remove(0);
   		}
   	}
   	

   	public void TabDelet(final JInternalFrame pInternalFrame)
    {
        if (!aTabs.contains(pInternalFrame))
        {
            return;
        }
        JTabbedPane tp = appesTabbedPane;
        int pos = aTabs.indexOf(pInternalFrame);
        tp.remove(pos);
        aTabs.remove(pInternalFrame);
        if(aTabs.size() == 0)
        {
        	appesBienvenueTab = new BienvenueTab(appesMenuNouv, aFailesRecentesMenu);
        	appesTabbedPane.add("Welcome", appesBienvenueTab);
        	aTabs.add(appesBienvenueTab);
        }
    }
   	

   	private void addRecentFile(String pNewFile)
   	{
   		aFailesRecentes.add(pNewFile);
   		buildRecentFilesMenu();
   	}
   

   	private void buildRecentFilesMenu()
   	{ 
   		assert aFailesRecentes.size() <= FILES_MAX_RECENT;
   		aFailesRecentesMenu.removeAll();
   		aFailesRecentesMenu.setEnabled(aFailesRecentes.size() > 0);
   		int i = 1;
   		for( File file : aFailesRecentes)
   		{
   			String name = i + " " + file.getName();
   			final String fileName = file.getAbsolutePath();
   			JMenuItem item = new JMenuItem(name);
   			item.setMnemonic('0'+i);
   			aFailesRecentesMenu.add(item);
            item.addActionListener(new ActionListener()
            {
           	 	public void actionPerformed(ActionEvent pEvent)
           	 	{
           	 		open(fileName);
           	 	}
            });
            i++;
   		}     
   }

   	public void openFile()
   	{  
		JFileChooser fileChooser = new JFileChooser(aFailesRecentes.getMostRecentDirectory());
		fileChooser.setFileFilter(new FiltreDextension(appesResourcesApp.getString("files.name"), appesResourcesApp.getString("files.extension")));

		FiltreDextension[] filters = new FiltreDextension[]{
			new FiltreDextension(appesResourcesApp.getString("state.name"),
					appesResourcesApp.getString("state.extension") + appesResourcesApp.getString("files.extension")),
   			new FiltreDextension(appesResourcesApp.getString("object.name"),
   						appesResourcesApp.getString("object.extension") + appesResourcesApp.getString("files.extension")),
   			new FiltreDextension(appesResourcesApp.getString("class.name"),
   						appesResourcesApp.getString("class.extension") + appesResourcesApp.getString("files.extension")),
   			new FiltreDextension(appesResourcesApp.getString("usecase.name"),
   						appesResourcesApp.getString("usecase.extension") + appesResourcesApp.getString("files.extension")),
   			new FiltreDextension(appesResourcesApp.getString("sequence.name"),
   						appesResourcesApp.getString("sequence.extension") + appesResourcesApp.getString("files.extension"))
   		};
   		for(FiltreDextension filter: filters)
		{
			fileChooser.addChoosableFileFilter(filter);
		}
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileChooser.getSelectedFile();
   			open(file.getAbsolutePath());
		}
   	}
   	

   	public void cut()
   	{
   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		if(frame == null)
   		{
   			return;
   		}
   		PanelGraph panel = frame.getGraphPanel();
   		Graph curGraph = frame.getGraph();
   		if(panel.getListSelection().size()>0)
   		{
   			ListSelection currentSelection = panel.getListSelection();
   			appesClipboard.copy(currentSelection);
   			Iterator<ElementGraph> iter = currentSelection.iterator();
   			panel.ListeningstartCompound();
   			while(iter.hasNext())
   			{
   				ElementGraph element = iter.next();
   				if(element instanceof Edge)
   				{
   					curGraph.edgeDeleted((Edge) element);
   				}
   				else
   				{
   					curGraph.deleteNode((Node) element);
   				}
   				iter.remove();
   			}
   			panel.ListeningendCompound();
   		}	
   		panel.repaint();
   	}
   	

   	public void copy()
   	{
   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		if(frame == null)
   		{
   			return;
   		}
   		PanelGraph panel = frame.getGraphPanel();
   		if(panel.getListSelection().size()>0)
   		{
   			ListSelection currentSelection = panel.getListSelection();
   			appesClipboard.copy(currentSelection);
   		}	
   	}
   	

   	public void paste()
   	{
   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		if(frame == null)
   		{
   			return;
   		}
   		
   		PanelGraph panel = frame.getGraphPanel();
   		try
   		{

   			ListSelection updatedSelectionList = appesClipboard.paste(panel.getGraph());
   			panel.setSelectionList(updatedSelectionList);
   			panel.repaint();
   		}
   		finally
   		{
   		}
   	}
   	

   	public void copyToClipboard()
   	{
   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		if( frame == null )
   		{
   			return;
   		}
   		final BufferedImage image = getImage(frame.getGraph());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new Transferable()
		{
			@Override
			public boolean isDataFlavorSupported(DataFlavor pFlavor)
			{
				return DataFlavor.imageFlavor.equals(pFlavor);
			}
			
			@Override
			public DataFlavor[] getTransferDataFlavors()
			{
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}
			
			@Override
			public Object getTransferData(DataFlavor pFlavor) throws UnsupportedFlavorException, IOException
			{
				if(DataFlavor.imageFlavor.equals(pFlavor))
		        {
		            return image;
		        }
		        else
		        {
		            throw new UnsupportedFlavorException(pFlavor);
		        }
			}
		}, null);
   		JOptionPane.showInternalMessageDialog(appesTabbedPane, appesResourcesEditeur.getString("dialog.to_clipboard.message"),
				appesResourcesEditeur.getString("dialog.to_clipboard.title"), JOptionPane.INFORMATION_MESSAGE);
   	}
   	

   	public void close()
   	{
   		if(appesTabbedPane.getSelectedComponent() instanceof BienvenueTab)
   		{
   			return;
   		}
        JInternalFrame curFrame = (JInternalFrame) appesTabbedPane.getSelectedComponent();
        if(curFrame != null)
        {
        	aFramGraph openFrame = (aFramGraph) curFrame;

			if(openFrame.getGraphPanel().isModified())
			{                   

				if(JOptionPane.showConfirmDialog(openFrame, 
						appesResourcesEditeur.getString("dialog.close.ok"), null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					TabDelet(curFrame);
				}
				return;
			}
			else
			{
				TabDelet(curFrame);
			}
        }
    }
   	

   	public void close(JInternalFrame pJInternalFrame)
   	{
        JInternalFrame FramCur = pJInternalFrame;
        if (FramCur != null)
        {
        	aFramGraph openFrame = (aFramGraph) FramCur;

			if(openFrame.getGraphPanel().isModified())
			{              
				if(JOptionPane.showConfirmDialog(openFrame, 
						appesResourcesEditeur.getString("dialog.close.ok"), null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					TabDelet(FramCur);
				}
				return;
			}
			TabDelet(FramCur);
        }
    }
   	

   	public void save()
   	{
   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		if(frame == null)
   		{
   			return;
   		}
   		File file = frame.getFileName(); 
   		if(file == null) 
   		{	
   			saveAs(); 
   			return; 
   		}
   		try
   		{
   			ServicePersistence.saveFile(frame.getGraph(), new FileOutputStream(file));
   			frame.getGraphPanel().setModified(false);
   		}        
   		catch(Exception exception)
   		{
   			JOptionPane.showInternalMessageDialog(appesTabbedPane, exception);
   		}        
   	}
   

   	public void saveAs()
   	{
   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		if(frame == null) 
   		{
   			return;
   		}
   		Graph graph = frame.getGraph();    
   		try
   		{
   			File result = null;
   			
   	   		JFileChooser fileChooser = new JFileChooser();
   			fileChooser.setFileFilter(new FiltreDextension(graph.getDescription(),
					graph.getFileExtension() + appesResourcesApp.getString("files.extension")));
   			fileChooser.setCurrentDirectory(new File("."));
   			
   			if(frame.getFileName() != null)
   			{           
   				fileChooser.setSelectedFile(frame.getFileName());
   			}
   			else 
   			{
   				fileChooser.setSelectedFile(new File(""));
   			}
   			int response = fileChooser.showSaveDialog(this);         
   			if(response == JFileChooser.APPROVE_OPTION)
   			{
   				File f = fileChooser.getSelectedFile();
   				if( !fileChooser.getFileFilter().accept(f))
   				{
   					f = new File(f.getPath() + graph.getFileExtension() + appesResourcesApp.getString("files.extension"));
   				}

   				if(!f.exists()) 
   				{
   					result = f;
   				}
   				else
   				{
   	        		ResourceBundle editorResources = ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings");
   	        		int theresult = JOptionPane.showConfirmDialog(this, editorResources.getString("dialog.overwrite"), 
   	        				null, JOptionPane.YES_NO_OPTION);
   	        		if(theresult == JOptionPane.YES_OPTION) 
   	        		{
   	        			result = f;
   	        		}
   				}
   			}
   			
   			if(result != null)
   			{
   				OutputStream out = new FileOutputStream(result);
   				try
   				{
   					ServicePersistence.saveFile(graph, out);
   				}
   				finally
   				{
   					out.close();
   				}
   				addRecentFile(result.getAbsolutePath());
   				frame.setFile(result);
   				appesTabbedPane.setTitleAt(appesTabbedPane.getSelectedIndex(), frame.getFileName().getName());
   				frame.getGraphPanel().setModified(false);
   			}
   		}
   		catch(IOException exception)
   		{
   			JOptionPane.showInternalMessageDialog(appesTabbedPane, exception);
   		}
   	}


	static String replaceExtension(String appesOrginalapp, String appesToBeRemoved, String appesDesired)
	{
		assert appesOrginalapp != null && appesToBeRemoved != null && appesDesired != null;
	
		if(appesOrginalapp.endsWith(appesToBeRemoved))
		{
			return appesOrginalapp.substring(0, appesOrginalapp.length() - appesToBeRemoved.length()) + appesDesired;
		}
		else
		{
			return appesOrginalapp;
		}
	}
   	

   	public void exportImage()
   	{

   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		if(frame == null) 
   		{
   			return;
   		}
   		

   		File file = chooseFileToExportTo();
   		if( file == null )
   		{
   			return;
   		}
   		

   		String fileName = file.getPath();
		String format  = fileName.substring(fileName.lastIndexOf(".") + 1);
		if(!ImageIO.getImageWritersByFormatName(format).hasNext())
		{
			JOptionPane.showInternalMessageDialog(appesTabbedPane, appesResourcesEditeur.getString("error.unsupported_image"),
					appesResourcesEditeur.getString("error.unsupported_image.title"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		confirmFileOverwrite(file);
   		
   		try( OutputStream out = new FileOutputStream(file))
   		{
   			ImageIO.write(getImage(frame.getGraph()), format, out);
   		}
   		catch(IOException exception)
   		{
   			JOptionPane.showInternalMessageDialog(appesTabbedPane, exception);
   		}      
   	}
   	
   	private static String[] getAllSupportedImageWriterFormats()
   	{
   		String[] names = ImageIO.getWriterFormatNames();
   		HashSet<String> formats = new HashSet<String>();
   		for( String name : names )
   		{
   			formats.add(name.toLowerCase());
   		}
   		String[] lReturn = formats.toArray(new String[formats.size()]);
   		Arrays.sort(lReturn);
   		return lReturn;
   	}
   	
    	private FileFilter createFileFilter(final String pFormat)
   	{
   		return new FileFilter()
		{
			@Override
			public String getDescription()
			{
				return pFormat.toUpperCase() + " " + appesResourcesEditeur.getString("files.image.name");
			}
			
			@Override
			public boolean accept(File pFile)
			{
				return !pFile.isDirectory() && (pFile.getName().endsWith("." +pFormat.toLowerCase()) || 
						pFile.getName().endsWith("." +pFormat.toUpperCase()));
			}
			

			@Override
			public String toString()
			{
				return pFormat;
			}
		};
   	}


   	private File chooseFileToExportTo()
   	{
   		aFramGraph frame = (aFramGraph) appesTabbedPane.getSelectedComponent();
   		assert frame != null;
 

	   	JFileChooser fileChooser = new JFileChooser();
	   	for( String format : getAllSupportedImageWriterFormats() )
	   	{
	   		fileChooser.addChoosableFileFilter(createFileFilter(format));
	   	}
		fileChooser.setCurrentDirectory(new File("."));
		

		if(frame.getFileName() != null)
		{
			File f = new File(replaceExtension(frame.getFileName().getAbsolutePath(),
					appesResourcesApp.getString("files.extension"), ""));
			fileChooser.setSelectedFile(f);
		}
		
		File file = null;
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			file = fileChooser.getSelectedFile();	
			FileFilter selectedFilter = fileChooser.getFileFilter();
			
			if( !selectedFilter.accept(file) && selectedFilter != fileChooser.getAcceptAllFileFilter())
			{
				file = new File(file.getPath() + "." + 
						selectedFilter.getDescription().substring(0, selectedFilter.toString().length()).toLowerCase());
			}
		}
		return file;
   	}
   	

   	private File confirmFileOverwrite(File pFile)
   	{
   		if(!pFile.exists()) 
		{
			return pFile;
		}
		
		ResourceBundle editorResources = ResourceBundle.getBundle("Epp_UML_Editeur.framework.EditorStrings");
		int result = JOptionPane.showConfirmDialog(this, editorResources.getString("dialog.overwrite"), null, JOptionPane.YES_NO_OPTION);
		if(result == JOptionPane.YES_OPTION) 
		{
			return pFile;	     
		}
		else
		{
			return null;
		}
   	}

   

    private static BufferedImage getImage(Graph pGraph)
    {
        Rectangle2D bounds = pGraph.getBounds();
        BufferedImage image = new BufferedImage((int) (bounds.getWidth() + IMAGE_MARGIN *2),
        		(int) (bounds.getHeight() + IMAGE_MARGIN *2), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.translate(-bounds.getX(), -bounds.getY());
        g2.setColor(Color.WHITE);
        g2.fill(new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth() + IMAGE_MARGIN *2, bounds.getHeight() + IMAGE_MARGIN *2));
        g2.translate(IMAGE_MARGIN, IMAGE_MARGIN);
        g2.setColor(Color.BLACK);
        g2.setBackground(Color.WHITE);
        pGraph.draw(g2, null);
        return image;
    }
    

   	public void showAboutDialog()
   	{
   		MessageFormat formatter = new MessageFormat(appesResourcesEditeur.getString("dialog.about.version"));
   		JOptionPane.showInternalMessageDialog(appesTabbedPane, formatter.format(new Object[]{
						appesResourcesApp.getString("app.name"),
						appesResourcesVersion.getString("version.number"),
						appesResourcesVersion.getString("version.date"),
						appesResourcesApp.getString("app.copyright"),
						appesResourcesEditeur.getString("dialog.about.license")}),
               new MessageFormat(appesResourcesEditeur.getString("dialog.about.title")).format(new Object[]{
					   appesResourcesApp.getString("app.name")}),
               JOptionPane.INFORMATION_MESSAGE,
               new ImageIcon(getClass().getClassLoader().getResource(appesResourcesApp.getString("app.icon"))));
   		
   	}


   	public void exit()
   	{
   		int modcount = 0;
   		for(int i = 0; i < aTabs.size(); i++)
   		{
   			if(aTabs.get(i) instanceof aFramGraph)
   			{	
				aFramGraph frame = (aFramGraph) aTabs.get(i);
				if(frame.getGraphPanel().isModified()) 
				{
					modcount++;
				}
   			}	
   		}
   		if(modcount > 0)
   		{

   			int result = JOptionPane.showInternalConfirmDialog(appesTabbedPane, MessageFormat.format(appesResourcesEditeur.getString("dialog.exit.ok"),
					new Object[]{new Integer(modcount)}), null, JOptionPane.YES_NO_OPTION);
         

   			if(result != JOptionPane.YES_OPTION) 
   			{
   				return;
   			}
   		}
   		Preferences.userNodeForPackage(FinalClassUML.class).put("recent", aFailesRecentes.serialize());
   		System.exit(0);
   	}
}
