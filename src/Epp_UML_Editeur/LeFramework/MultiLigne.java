

package Epp_UML_Editeur.LeFramework;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class MultiLigne implements Cloneable
{
	private enum Appslign
	{ LEFT, CENTER, RIGHT }
	

	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;
	
	private String appesText = "";
	private Appslign appesJustification = Appslign.CENTER;
	private boolean appesBold = false;
	private boolean appesUnderlined = false;
	

	public MultiLigne()
	{}
	

	public MultiLigne(boolean appesBold)
	{ 
		this.appesBold = appesBold;
	}
	

	public void setText(String appesText)
	{ 
		this.appesText = appesText;
		getLabel(); 
	}
   

	public String getText() 
	{ 
		return appesText;
	}
   

	public void setJustification(int appesJustification)
	{ 
		assert appesJustification >= 0 && appesJustification < Appslign.values().length;
		this.appesJustification = Appslign.values()[appesJustification];
	}
   

	public int getJustification() 
	{ 
		return appesJustification.ordinal();
	}
   

	public boolean underlined()
	{ 
		return appesUnderlined;
	}
	

	public boolean bold()
	{
		return appesBold;
	}
  

	public void setUnderlined(boolean appesUnderlined)
	{ 
		this.appesUnderlined = appesUnderlined;
	}
   
	@Override
	public String toString()
	{
		return appesText.replace('\n', '|');
	}

	private JLabel getLabel()
	{
		JLabel label = new JLabel(convertToHtml().toString());
		
		if(appesJustification == Appslign.LEFT)
		{
			label.setHorizontalAlignment(SwingConstants.LEFT);
		}
		else if(appesJustification == Appslign.CENTER)
		{
			label.setHorizontalAlignment(SwingConstants.CENTER);
		}
		else if(appesJustification == Appslign.RIGHT)
		{
			label.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label;
	}


	StringBuffer convertToHtml()
	{
		StringBuffer appesprefix = new StringBuffer();
		StringBuffer appessuffix = new StringBuffer();
		StringBuffer htmlText = new StringBuffer();
		

		appesprefix.append("&nbsp;");
		appessuffix.insert(0, "&nbsp;");
		if(appesUnderlined)
		{
			appesprefix.append("<u>");
			appessuffix.insert(0, "</u>");
		}
		if(appesBold)
		{
			appesprefix.append("<b>");
			appessuffix.insert(0, "</b>");
		}

		htmlText.append("<html><p align=\"" + appesJustification.toString().toLowerCase() + "\">");
		StringTokenizer tokenizer = new StringTokenizer(appesText, "\n");
		boolean first = true;
		while(tokenizer.hasMoreTokens())
		{
			if(first) 
			{
				first = false;
			}
			else 
			{
				htmlText.append("<br>");
			}
			htmlText.append(appesprefix);
			String nxt = tokenizer.nextToken();
			String nxt1 = nxt.replaceAll("<", "&lt;");
			String nxt2 = nxt1.replaceAll(">", "&gt;");
			htmlText.append(nxt2);
			htmlText.append(appessuffix);
		}      
		htmlText.append("</p></html>");
		return htmlText;
	}
   

	public Rectangle2D getBounds(Graphics2D appesGraphics2D)
	{
		if(appesText.length() == 0)
		{
			return new Rectangle2D.Double();
		}
		Dimension dim = getLabel().getPreferredSize();       
		return new Rectangle2D.Double(0, 0, dim.getWidth(), dim.getHeight());
	}


	public void draw(Graphics2D appesGraphics2D, Rectangle2D appesRectangle)
	{
		JLabel label = getLabel();
		label.setFont(appesGraphics2D.getFont());
		label.setBounds(0, 0, (int) appesRectangle.getWidth(), (int) appesRectangle.getHeight());
		appesGraphics2D.translate(appesRectangle.getX(), appesRectangle.getY());
		label.paint(appesGraphics2D);
		appesGraphics2D.translate(-appesRectangle.getX(), -appesRectangle.getY());
	}
	

	public boolean equalProperties(MultiLigne appesString)
	{
		if(!appesText.equals(appesString.getText()))
		{
			return false;
		}
		if(appesJustification.ordinal() != appesString.getJustification())
		{
			return false;
		}
		if(appesBold != appesString.appesBold)
		{
			return false;
		}
		if(appesUnderlined != appesString.underlined())
		{
			return false;
		}
		return true;
	}
	
	@Override
	public MultiLigne clone()
	{
		try
		{
			return (MultiLigne) super.clone();
		}
		catch (CloneNotSupportedException exception)
		{
			return null;
		}
	}
}
