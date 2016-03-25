

package Epp_UML_Editeur;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import Epp_UML_Editeur.LeFramework.FrameEditeur;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramClass;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramObject;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramSequence;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramState;
import Epp_UML_Editeur.LesDiagrammes.GraphDiagramUseCase;


public final class FinalClassUML
{

	private FinalClassUML() {}


	public static void main(String[] pArgs)
	{

		try
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
		catch (SecurityException ex)
		{

		}
		final String[] arguments = pArgs;

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() {
				setLookFeel();
				FrameEditeur frame = new FrameEditeur(FinalClassUML.class);
				frame.addTypeGraph("class_diagram", GraphDiagramClass.class);
				frame.addTypeGraph("sequence_diagram", GraphDiagramSequence.class);
				frame.addTypeGraph("state_diagram", GraphDiagramState.class);
				frame.addTypeGraph("object_diagram", GraphDiagramObject.class);
				frame.addTypeGraph("usecase_diagram", GraphDiagramUseCase.class);
				frame.setVisible(true);
				frame.LireArgts(arguments);
				frame.addWelcomeTab();
				frame.setIcones();
			}
		});
	}

	private static void setLookFeel() {

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Metal".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {

		}
	}






}