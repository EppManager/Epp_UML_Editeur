
package Epp_UML_Editeur.LesCommandes;

import Epp_UML_Editeur.Graphe.Edge;
import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.Node;


public class CommandAdRemoveEdge implements Command
{
	private Edge appBords;
	private Graph appGrph;
	private Node aPnode1;
	private Node aPnode2;
	private boolean aAdding;

	public CommandAdRemoveEdge(Graph appGrph, Edge appBords, boolean pAdding)
	{
		this.appGrph = appGrph;
		this.appBords = appBords;
		aPnode1 = this.appBords.getNodeStart();
		aPnode2 = this.appBords.getNodeEnd();
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
		appGrph.edgeDeleted(appBords);
		appGrph.layout();
	}
	

	private void add() 
	{

		appGrph.connect(appBords, aPnode1, aPnode2);
		appGrph.layout();
	}
	
}
