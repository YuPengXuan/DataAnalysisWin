package com.xn.alex.data.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class Logo extends JWindow implements MouseListener {
	
    private JPanel panelMain = new JPanel();

    private FlowLayout layout = new FlowLayout();
    
    public Logo(String file) {
        super();
        
        layout.setHgap(3);
        layout.setVgap(3);
        getContentPane().setLayout(layout);
        getContentPane().setBackground(java.awt.Color.gray);
        getContentPane().add(panelMain);
        
     
        SplashImage JLabel1 = new SplashImage();
        ImageIcon image = new ImageIcon(file);
        JLabel1.setIcon(image);
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        
        panelMain.setSize(image.getIconWidth(), image.getIconHeight());      
        panelMain.setBackground(java.awt.Color.white);
        panelMain.add(JLabel1);

        addMouseListener(this);

        pack();
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xc = (int)(tk.getScreenSize().getWidth()/2 - getSize().getWidth()/2);
        int yc = (int)(tk.getScreenSize().getHeight()/2 - getSize().getHeight()/2);
        setLocation(new Point(xc, yc));
        setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

}

@SuppressWarnings("serial")
class SplashImage extends JLabel {
    static String srelease = "1.0";
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        Graphics2D g2 = (Graphics2D)g;
        Dimension dsize = getSize();
        FontMetrics fm = getFontMetrics(getFont());
        g2.drawString(srelease, (float)dsize.width/2-fm.stringWidth(srelease)/2, (float)dsize.height * 3/4 + 30 + (fm.getMaxDescent()*2+3)*2+3);
        g2.setColor(Color.white);
    }
    
}
