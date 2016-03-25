

package Epp_UML_Editeur.LeFramework;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;


class FormLayout implements LayoutManager
{  
	private static final int GP = 6;
	
	private int appesLeft;
	private int appesRight;
	private int appesHeight;
	
	@Override
	public Dimension preferredLayoutSize(Container appesParent)
	{  
		Component[] components = appesParent.getComponents();
		appesLeft = 0;
		appesRight = 0;
		appesHeight = 0;
		for(int i = 0; i < components.length; i += 2)
		{
			Component cleft = components[i];
			Component cright = components[i + 1];
			Dimension dleft = cleft.getPreferredSize();
			Dimension dright = cright.getPreferredSize();
			appesLeft = Math.max(appesLeft, dleft.width);
			appesRight = Math.max(appesRight, dright.width);
			appesHeight = appesHeight + Math.max(dleft.height, dright.height);
		}      
		return new Dimension(appesLeft + GP + appesRight, appesHeight);
	}
      
	@Override
	public Dimension minimumLayoutSize(Container appesParent)
	{  
		return preferredLayoutSize(appesParent);
	}

	@Override
	public void layoutContainer(Container appesParent)
	{  
		preferredLayoutSize(appesParent);
		Component[] components = appesParent.getComponents();
		Insets insets = appesParent.getInsets();
		int xcenter = insets.left + appesLeft;
		int y = insets.top;

		for(int i = 0; i < components.length; i += 2)
		{
			Component cleft = components[i];
			Component cright = components[i + 1];

			Dimension dleft = cleft.getPreferredSize();
			Dimension dright = cright.getPreferredSize();

			int height = Math.max(dleft.height, dright.height);

			cleft.setBounds(xcenter - dleft.width, y + (height - dleft.height) / 2, dleft.width, dleft.height);
			cright.setBounds(xcenter + GP, y + (height - dright.height) / 2, dright.width, dright.height);
			y += height;
		}
	}

	@Override
	public void addLayoutComponent(String appesName, Component appesComponent)
	{}

	@Override
	public void removeLayoutComponent(Component appesComponent)
	{}

   
}
