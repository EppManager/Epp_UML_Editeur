

package Epp_UML_Editeur.LeFramework;


public class ArHeadEditeur extends SelectProperty
{
	private static final String[] NAMES = { "None", "Triangle", "V", "Diamond", "Black Diamond" };
	private static final Object[] VALUES = { ArHead.ARROWHEAD_NONE, ArHead.ARROWHEAD_TRIANGLE, ArHead.V, ArHead.ARROWHEAD_DIAMOND, ArHead.ARROWHEAD_BLACK_DIAMOND};
	

	public ArHeadEditeur()
	{
		super(NAMES, VALUES);
	}
}
