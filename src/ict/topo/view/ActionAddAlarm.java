/*    */ package ict.topo.view;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ import javax.swing.JOptionPane;
/*    */ 
/*    */ public class ActionAddAlarm extends AbstractAction
/*    */ {
/*    */   private static final long serialVersionUID = -1260348956423289402L;
/* 16 */   private static Color[] colors = { Color.GREEN, Color.CYAN, new Color(205, 155, 255), Color.BLUE, Color.YELLOW, new Color(255, 150, 0), Color.RED };
/*    */ 
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 22 */     Component topoView = (Component)getValue("topoGraphView");
/* 23 */     String rs = JOptionPane.showInputDialog(topoView, 
/* 24 */       "请输入要添加的告警对象的级别(0,1,2,3,4,5,6),0-最低，6-最高");
/* 25 */     if ((rs == null) || (rs.equals("")))
/*    */     {
/* 27 */       rs = "2";
/*    */     }
/* 29 */     int level = Integer.parseInt(rs);
/* 30 */     Component obj = (Component)getValue("selectTopoObject");
/* 31 */     if ((obj instanceof TopoLink))
/*    */     {
/* 33 */       ((TopoLink)obj).setColor(colors[level]);
/* 34 */       ((TopoLink)obj).repaint();
/* 35 */     } else if ((obj instanceof TopoNode))
/*    */     {
/* 37 */       ((TopoNode)obj).setColor(colors[level]);
/*    */     }
/*    */   }
/*    */ 
/*    */   public ActionAddAlarm()
/*    */   {
/* 43 */     super("添加告警");
/*    */   }
/*    */ }

/* Location:           C:\javaplugins\tuopu\newtuopu.jar
 * Qualified Name:     ict.topo.view.ActionAddAlarm
 * JD-Core Version:    0.6.2
 */