
package Epp_UML_Editeur.LesCommandes;

import java.awt.Point;

import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.Node;


public class CommandAdRemoveNode implements Command
{
	private Node appNode;
	private Graph appGraph;
	private double x;
	private double y;
	private boolean aAdding;
	

	public CommandAdRemoveNode(Graph appGraph, Node appNode, boolean pAdding)
	{
		this.appGraph = appGraph;
		this.appNode = appNode;
		x = this.appNode.getBounds().getMinX();
		y = this.appNode.getBounds().getMinY();
		aAdding = pAdding;
	}
	

	public void undo() 
	{
		if(aAdding)
		{
			delete();
		}
		else
		{
			add();
		}
	}


	public void execute() 
	{
		if(aAdding)
		{
			add();
		}
		else
		{
			delete();
		}
	}


	private void delete() 
	{
		appGraph.deleteNode(appNode);
		appGraph.layout();
	}
	

	private void add() 
	{
		Point.Double point = new Point.Double(x, y);
		appGraph.add(appNode, point);
		appGraph.layout();
	}
	
}
