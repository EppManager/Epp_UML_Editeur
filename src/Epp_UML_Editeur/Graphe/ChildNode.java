package Epp_UML_Editeur.Graphe;


public interface ChildNode extends Node
{

	NodeParent getNodeParent();
	

	void setNodeParent(NodeParent ppesParentNode);
}
