

package Epp_UML_Editeur.LeFramework;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import Epp_UML_Editeur.Graphe.Graph;



public class aFramGraph extends JInternalFrame
{
	private JTabbedPane aTabbedPane;
	private Graph aGraph;
	private PanelGraph aPanel;
	private File aFile;
	

	public aFramGraph(Graph pGraph, JTabbedPane pTabbedPane)
	{
		aGraph = pGraph;
		aTabbedPane = pTabbedPane;
		BarTool sideBar = new BarTool(pGraph);
		aPanel = new PanelGraph(sideBar);
		Container contentPane = getContentPane();
		contentPane.add(sideBar, BorderLayout.EAST);
		contentPane.add(new JScrollPane(aPanel), BorderLayout.CENTER);
		aPanel.setGraph(aGraph);
		setComponentPopupMenu( null );
	}


	public Graph getGraph()
	{
		return aGraph;
	}


	public PanelGraph getGraphPanel()
   	{
		return aPanel;
   	}
	

	public JTabbedPane getJTabbedPane()
	{
		return aTabbedPane;
	}
	

	public void setTitle(boolean pModified)
	{
		if(aFile != null)
		{
			String title = aFile.getName();
			if(pModified)
			{
				if(!getTitle().endsWith("*"))
				{
					setTitle(title + "*");
				}
			}
			else
			{
				setTitle(title);
			}
		}
	}


	public File getFileName()
	{
		return aFile;
	}


	public void setFile(File pFile)
	{
		aFile = pFile;
		setTitle(aFile.getName());
	}
}	        
