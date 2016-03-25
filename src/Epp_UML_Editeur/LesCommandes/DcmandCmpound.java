
package Epp_UML_Editeur.LesCommandes;

import java.util.Stack;


public class DcmandCmpound implements Command
{
	private Stack<Command> appsCommands;


	public DcmandCmpound()
	{
		appsCommands = new Stack<Command>();
	}


	public void add(Command ppsCommand)
	{
		appsCommands.push(ppsCommand);
	}


	public int size()
	{
		return appsCommands.size();
	}


	public void undo()
	{
		Stack<Command> temp = new Stack<Command>();
		while(!appsCommands.empty())
		{
			Command coms = appsCommands.pop();
			coms.undo();
			temp.push(coms);
		}
		appsCommands = temp;
	}


	public void execute()
	{
		Stack<Command> temp = new Stack<Command>();
		while(!appsCommands.empty())
		{
			Command com = appsCommands.pop();
			com.execute();
			temp.push(com);
		}
		appsCommands = temp;
	}
}
