
package Epp_UML_Editeur.LesCommandes;

import Epp_UML_Editeur.Graphe.Graph;
import Epp_UML_Editeur.Graphe.Node;


public class MoveCommandApp implements Command
{
	private Node appNdes;
	private Graph appGrphs;
	private double x;
	private double y;
	

	public MoveCommandApp(Graph appGrphs, Node appNdes, double x, double y)
	{
		this.appGrphs = appGrphs;
		this.appNdes = appNdes;
		this.x = x;
		this.y = y;
	}
	

	public void undo() 
	{
		appNdes.AppCommandtranslate(-x, -y);
		appGrphs.layout();
	}


	public void execute() 
	{
		appNdes.AppCommandtranslate(x, y);
		appGrphs.layout();
	}

}
