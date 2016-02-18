/*    */ package ict.topo.view;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
import java.awt.Rectangle;

/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ 
/*    */ public class TopoNode extends JLabel
/*    */ {
/*    */   private static final long serialVersionUID = 8443089188719938763L;
/*    */   private String infomation;
/*    */   private Color color;
/*    */ 
/*    */   public TopoNode(int syn, String name)
/*    */   {
/* 21 */     //setSize(55, 45);
	         setBounds(new Rectangle(70, 90));
/* 22 */     this.infomation = null;
/* 23 */     this.color = Color.WHITE;
/* 24 */     setOpaque(true);
/* 25 */     if (syn == 0)
/*    */     {
/* 27 */       //ImageIcon routerIcon = new ImageIcon(ClassLoader.getSystemResource("images/smallRouter.png"));
	           ImageIcon routerIcon = new ImageIcon(ClassLoader.getSystemResource("images/Router.png"));
/* 28 */       setIcon(routerIcon);
/*    */     }
/*    */     else
/*    */     {
/* 32 */       //ImageIcon switchIcon = new ImageIcon(ClassLoader.getSystemResource("images/smallSwitch.png"));
	           ImageIcon switchIcon = new ImageIcon(ClassLoader.getSystemResource("images/Switch.png"));
/* 33 */       setIcon(switchIcon);
/*    */     }
/*    */ 
/* 36 */     setHorizontalAlignment(0);
/* 37 */     setHorizontalTextPosition(0);
/* 38 */     setVerticalTextPosition(3);
/* 39 */     setText(name);
/*    */   }
/*    */ 
/*    */   public void setXY(int x, int y)
/*    */   {
/* 45 */     //setBounds(x, y, 55, 45);
	         setBounds(x, y, 70, 90);
/*    */   }
/*    */ 
/*    */   public void setInfo(String info)
/*    */   {
/* 50 */     this.infomation = info;
/*    */   }
/*    */ 
/*    */   public String getInfo()
/*    */   {
/* 55 */     return this.infomation;
/*    */   }
/*    */ 
/*    */   public void setColor(Color c)
/*    */   {
/* 60 */     this.color = c;
/* 61 */     repaint();
/*    */   }
/*    */ 
/*    */   public void paint(Graphics g)
/*    */   {
/* 67 */     super.paint(g);
/* 68 */     setBackground(this.color);
/*    */   }
/*    */ }

/* Location:           C:\javaplugins\tuopu\newtuopu.jar
 * Qualified Name:     ict.topo.view.TopoNode
 * JD-Core Version:    0.6.2
 */