
package Epp_UML_Editeur.LeFramework;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;
 

public class ComponentButtonTab extends JPanel
{
	private static final int SIZE_TAB_COMPONENT = 17;
    private static final int BORDER_LABEL_COMPONENT = 5;
    private static final int DLTA_COMPONENT = 6;
    private static final MouseListener MOUSE_LISTENER_BUTTON = new MouseAdapter() {
  	   public void mouseEntered(MouseEvent pEvent) 
         {
             Component component = pEvent.getComponent();
             if (component instanceof AbstractButton) 
             {
                 AbstractButton button = (AbstractButton) component;
                 button.setBorderPainted(true);
             }
         }

         public void mouseExited(MouseEvent pEvent) 
         {
             Component component = pEvent.getComponent();
             if (component instanceof AbstractButton) 
             {
                 AbstractButton button = (AbstractButton) component;
                 button.setBorderPainted(false);
             }
         }
      };
	private final FrameEditeur aFrameEditeur;
	private final JInternalFrame aJInternalFrame;
    private final JTabbedPane aPane;
   
 

    public ComponentButtonTab(FrameEditeur pFrameEditeur, JInternalFrame pJInternalFrame, final JTabbedPane pJTabbedPane)
    {

        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pFrameEditeur == null)
        {
            throw new NullPointerException("FrameEditeur is null");
        }
        aFrameEditeur = pFrameEditeur;
        if (pJInternalFrame == null) 
        {
            throw new NullPointerException("aFramGraph is null");
        }
        aJInternalFrame = pJInternalFrame;
        if (pJTabbedPane == null) 
        {
            throw new NullPointerException("TabbedPane is null");
        }
        aPane = pJTabbedPane;
        setOpaque(false);
         
         JLabel label = new JLabel()
        {
            public String getText() 
            {
                int i = aPane.indexOfTabComponent(ComponentButtonTab.this);
                if (i != -1) 
                {
                    return aPane.getTitleAt(i);
                }
                return null;
            }
        };
         
        add(label);
         label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, BORDER_LABEL_COMPONENT));

        JButton button = new TabButton();
        add(button);
         setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }
 
    private class TabButton extends JButton implements ActionListener 
    {
        TabButton() 
        {
            int size = SIZE_TAB_COMPONENT;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");

            setUI(new BasicButtonUI());

            setContentAreaFilled(false);

            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
             addMouseListener(MOUSE_LISTENER_BUTTON);
            setRolloverEnabled(true);

            addActionListener(this);
        }
 
        public void actionPerformed(ActionEvent pEvent) 
        {
            int i = aPane.indexOfTabComponent(ComponentButtonTab.this);
            if (i != -1) 
            {
                aFrameEditeur.close(aJInternalFrame);
            }
        }
 

        public void updateUI() 
        {
        }
 

        protected void paintComponent(Graphics pGraphics) 
        {
            super.paintComponent(pGraphics);
            Graphics2D g2 = (Graphics2D) pGraphics.create();
             if (getModel().isPressed())
            {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) 
            {
                g2.setColor(Color.MAGENTA);
            }
            int dlta = DLTA_COMPONENT;
            g2.drawLine(dlta, dlta, getWidth() - dlta - 1, getHeight() - dlta - 1);
            g2.drawLine(getWidth() - dlta - 1, dlta, dlta, getHeight() - dlta - 1);
            g2.dispose();
        }
    }
}