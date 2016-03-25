
package Epp_UML_Editeur.LeFramework;

import java.util.Stack;

import Epp_UML_Editeur.LesCommandes.Command;
import Epp_UML_Editeur.LesCommandes.DcmandCmpound;


public class UndoManager
{
	private Stack<Command> appesPastCommands;
	private Stack<Command> appesUndoneCommands;
	private Stack<DcmandCmpound> appesTrackingCommands;
	private boolean appesHoldChanges = false;
	

	public UndoManager()
	{
		appesPastCommands = new Stack<Command>();
		appesUndoneCommands = new Stack<Command>();
		appesTrackingCommands = new Stack<DcmandCmpound>();
	}


	public void add(Command appesCommand)
	{
		if(!appesHoldChanges)
		{
			if(!appesUndoneCommands.empty())
			{
				appesUndoneCommands.clear();
			}
			if(!appesTrackingCommands.empty())
			{
				appesTrackingCommands.peek().add(appesCommand);
			}
			else
			{
				appesPastCommands.push(appesCommand);
			}
		}
	}


	public void commandUndo()
	{
		if(appesPastCommands.empty())
		{
			return;
		}
		appesHoldChanges = true;
		Command undoComd = appesPastCommands.pop();
		undoComd.undo();
		appesUndoneCommands.push(undoComd);
		appesHoldChanges = false;
	}


	void commandredo()
	{
		appesHoldChanges = true;
		if (appesUndoneCommands.empty())
		{
			return;
		}
		Command redoComd = appesUndoneCommands.pop();
		redoComd.execute();
		appesPastCommands.push(redoComd);
		appesHoldChanges = false;
	}


	public void startTracking()
	{
		appesTrackingCommands.push(new DcmandCmpound());
	}


	public void endTracking()
	{
		if(!appesTrackingCommands.empty())
		{
			DcmandCmpound dcmandCmpound = appesTrackingCommands.pop();
			if(dcmandCmpound.size() > 0)
			{
				add(dcmandCmpound);
			}
		}
	}

}
