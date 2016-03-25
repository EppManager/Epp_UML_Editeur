
package Epp_UML_Editeur.Graphe;

import java.util.List;


public interface NodeParent extends Node
{

	List<ChildNode> getChildren(); 
	

	void addChild(int appesIndex, ChildNode appesNode);
	

	void addChild(ChildNode appesNode);
	

	void ChildNodeDeleted(ChildNode appesNode);
}
