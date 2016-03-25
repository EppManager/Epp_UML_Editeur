
package Epp_UML_Editeur.LeFramework;


public class StyleLineEditeur extends SelectProperty
{
	private static final String[] NAMES = { "Solid", "Dotted" };
	private static final Object[] VALUES = { StyleLine.SOLIDSTYLE, StyleLine.DOTTEDSTYLE};
	

	public StyleLineEditeur()
	{
		super(NAMES, VALUES);
	}
}

