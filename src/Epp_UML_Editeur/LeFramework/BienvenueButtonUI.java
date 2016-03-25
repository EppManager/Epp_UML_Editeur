
package Epp_UML_Editeur.LeFramework;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;




public class BienvenueButtonUI extends BasicButtonUI
{
	private static final Color COLOR_HOVER = new Color(255, 255, 255);
	private static final int SIZE_FONT = 25;
	

	    public BienvenueButtonUI()
	    {
	        super();
	    }


	    @Override
	    protected void installDefaults(AbstractButton appesButton)
	    {
	        super.installDefaults(appesButton);
	        appesButton.setOpaque(false);
	        appesButton.setBorderPainted(false);
	        appesButton.setRolloverEnabled(true);
	        appesButton.setFont(new Font("Arial", Font.PLAIN, SIZE_FONT));
	        appesButton.setBorder(new EmptyBorder(4, 0, 0, 4));
	    }

	    @Override
	    protected void paintText(Graphics appesGraphic, AbstractButton appesButton, Rectangle appesTextRect, String appesText)
	    {

	        ButtonModel buttmodel = appesButton.getModel();
	        if (buttmodel.isRollover())
	        {
	        	appesButton.setForeground(COLOR_HOVER);
	            appesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        }
	        else
	        {
	        	appesButton.setForeground(Color.BLACK);
	        }
	        super.paintText(appesGraphic, appesButton, appesTextRect, appesText);
	    }
}
