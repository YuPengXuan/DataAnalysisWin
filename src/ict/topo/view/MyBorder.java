/*    */ package ict.topo.view;
/*    */ 
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.border.Border;
/*    */ 
/*    */ public class MyBorder
/*    */   implements Border
/*    */ {
/*    */   private int thinkness;
/*    */   private Color color;
/*    */ 
/*    */   public MyBorder(Color c)
/*    */   {
/* 11 */     this.thinkness = 1;
/* 12 */     this.color = c;
/*    */   }
/*    */ 
/*    */   public MyBorder()
/*    */   {
/* 17 */     this.color = Color.BLUE;
/* 18 */     this.thinkness = 1;
/*    */   }
/*    */ 
/*    */   public Insets getBorderInsets(Component c) {
/* 22 */     return new Insets(this.thinkness, this.thinkness, this.thinkness, this.thinkness);
/*    */   }
/*    */ 
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*    */   {
/* 27 */     Graphics2D g2d = (Graphics2D)g;
/* 28 */     g.setColor(this.color);
/* 29 */     g2d.setStroke(new BasicStroke(this.thinkness, 0, 
/* 30 */       2, 1.0F, new float[] { this.thinkness * 2, 
/* 31 */       this.thinkness }, this.thinkness));
/* 32 */     g2d.drawRect(x + this.thinkness / 2, y + this.thinkness / 2, width - this.thinkness, 
/* 33 */       height - this.thinkness);
/*    */   }
/*    */ 
/*    */   public boolean isBorderOpaque() {
/* 37 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\javaplugins\tuopu\newtuopu.jar
 * Qualified Name:     ict.topo.view.MyBorder
 * JD-Core Version:    0.6.2
 */