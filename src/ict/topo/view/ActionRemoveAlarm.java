/*    */ package ict.topo.view;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.AbstractAction;
/*    */ 
/*    */ public class ActionRemoveAlarm extends AbstractAction
/*    */ {
/*    */   private static final long serialVersionUID = 4898754024134946342L;
/*    */ 
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 16 */     Component obj = (Component)getValue("selectTopoObject");
/* 17 */     if ((obj instanceof TopoLink))
/*    */     {
/* 19 */       ((TopoLink)obj).setColor(Color.BLACK);
/* 20 */       ((TopoLink)obj).repaint();
/* 21 */     } else if ((obj instanceof TopoNode))
/*    */     {
/* 23 */       ((TopoNode)obj).setColor(Color.WHITE);
/*    */     }
/*    */   }
/*    */ 
/*    */   public ActionRemoveAlarm()
/*    */   {
/* 29 */     super("Çå³ý¸æ¾¯");
/*    */   }
/*    */ }

/* Location:           C:\javaplugins\tuopu\newtuopu.jar
 * Qualified Name:     ict.topo.view.ActionRemoveAlarm
 * JD-Core Version:    0.6.2
 */