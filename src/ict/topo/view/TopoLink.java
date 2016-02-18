/*     */ package ict.topo.view;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ public class TopoLink extends JLabel
/*     */ {
/*     */   private static final long serialVersionUID = 649231849786877232L;
/*     */   private TopoNode lnode;
/*     */   private TopoNode rnode;
/*     */   private JLabel left;
/*     */   private JLabel right;
/*     */   private String lineName;
/*     */   private int lineCost;
/*     */   private Color lineColor;
/*     */   private int thickness;
/*     */   //private Dimension screenSize;
/*     */   public int x0;
/*     */   public int y0;
/*     */   public int x1;
/*     */   public int y1;
/*     */   public int lw;
/*     */   public int lh;
/*     */   public int rw;
/*     */   public int rh;
/*     */   public boolean hasArrow;
/*     */ 
/*     */   public TopoLink(String name, TopoNode node1, TopoNode node2)
/*     */   {
/*  38 */     this.lnode = node1;
/*  39 */     this.rnode = node2;
/*  40 */     this.lineName = name;
/*  41 */     this.lineColor = Color.BLACK;
/*  42 */     this.lineCost = 0;
/*  43 */     this.left = this.lnode;
/*  44 */     this.right = this.rnode;
/*  45 */     this.thickness = 1;
/*  46 */     this.hasArrow = false;
/*  47 */     setSize();
/*     */   }
/*     */ 
/*     */   public TopoLink(String name, int cost, TopoNode node1, TopoNode node2)
/*     */   {
/*  52 */     this.lnode = node1;
/*  53 */     this.rnode = node2;
/*  54 */     this.lineName = name;
/*  55 */     this.lineCost = cost;
/*  56 */     this.lineColor = Color.BLACK;
/*  57 */     this.thickness = 1;
/*  58 */     setSize();
/*     */   }
/*     */ 
/*     */   public TopoLink(String name, TopoNode node1, TopoNode node2, Color c)
/*     */   {
/*  63 */     this.lineName = name;
/*  64 */     this.lnode = node1;
/*  65 */     this.rnode = node2;
/*  66 */     this.left = this.lnode;
/*  67 */     this.right = this.rnode;
/*  68 */     this.thickness = 1;
/*  69 */     this.lineColor = c;
/*  70 */     this.hasArrow = true;
/*  71 */     setSize();
/*     */   }
/*     */ 
/*     */   public void setSize()
/*     */   {
/*  77 */     //this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
/*  78 */     //setBounds(0, 0, this.screenSize.width, this.screenSize.height);
              //for show 100 node
              setBounds(0, 0, 10000, 180000);
/*     */   }
/*     */ 
/*     */   public void setCost(int cost)
/*     */   {
/*  83 */     this.lineCost = cost;
/*     */   }
/*     */ 
/*     */   public int getCost()
/*     */   {
/*  88 */     return this.lineCost;
/*     */   }
/*     */ 
/*     */   public String getLineName()
/*     */   {
/*  93 */     return this.lineName;
/*     */   }
/*     */ 
/*     */   public TopoNode getLnode()
/*     */   {
/*  98 */     return this.lnode;
/*     */   }
/*     */ 
/*     */   public TopoNode getRnode()
/*     */   {
/* 103 */     return this.rnode;
/*     */   }
/*     */ 
/*     */   public void setColor(Color color)
/*     */   {
/* 108 */     this.lineColor = color;
/*     */   }
/*     */ 
/*     */   public void setThickness(int thick)
/*     */   {
/* 113 */     this.thickness = thick;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 119 */     super.paint(g);
/* 120 */     ((Graphics2D)g).setStroke(new BasicStroke(this.thickness, 
/* 121 */       0, 
/* 122 */       2));
/* 123 */     ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
/* 124 */       RenderingHints.VALUE_ANTIALIAS_ON);
/* 125 */     g.setColor(this.lineColor);
/* 126 */     if (this.hasArrow)
/*     */     {
/* 128 */       drawLine(g);
/*     */     }
/*     */     else
/*     */     {
/* 132 */       this.left = this.lnode;
/* 133 */       this.right = this.rnode;
/* 134 */       this.x0 = this.left.getX();
/* 135 */       this.y0 = this.left.getY();
/* 136 */       this.lw = this.left.getWidth();
/* 137 */       this.lh = this.left.getHeight();
/* 138 */       this.x1 = this.right.getX();
/* 139 */       this.y1 = this.right.getY();
/* 140 */       this.rw = this.right.getWidth();
/* 141 */       this.rh = this.right.getHeight();
/* 142 */       g.drawLine(this.x0 + this.lw / 2, this.y0 + this.lh / 2, this.x1 + this.rw / 2, this.y1 + this.rh / 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawLine(Graphics g)
/*     */   {
/* 149 */     double H = 10.0D;
/* 150 */     double L = 6.0D;
/* 151 */     int x3 = 0;
/* 152 */     int y3 = 0;
/* 153 */     int x4 = 0;
/* 154 */     int y4 = 0;
/* 155 */     this.left = this.lnode;
/* 156 */     this.right = this.rnode;
/* 157 */     this.x0 = this.left.getX();
/* 158 */     this.y0 = this.left.getY();
/* 159 */     this.lw = this.left.getWidth();
/* 160 */     this.lh = this.left.getHeight();
/* 161 */     this.x1 = this.right.getX();
/* 162 */     this.y1 = this.right.getY();
/* 163 */     this.rw = this.right.getWidth();
/* 164 */     this.rh = this.right.getHeight();
/* 165 */     double awrad = Math.atan(L / H);
/* 166 */     double arraow_len = Math.sqrt(L * L + H * H);
/* 167 */     int sx = this.x0 + this.lw / 2; int sy = this.y0 + this.lh / 2;
/* 168 */     int ex = this.x1 + this.rw / 2; int ey = this.y1 + this.rh / 2;
/* 169 */     double d = Math.sqrt(Math.abs(ex - sx) * 
/* 170 */       Math.abs(ex - sx) + 
/* 171 */       Math.abs(ey - sy) * 
/* 172 */       Math.abs(ey - sy));
/*     */ 
/* 174 */     double xishu = (d - 39.0D) / d;
/* 175 */     double arrx = xishu * (ex - sx) + sx + 1.5D;
/* 176 */     double arry = xishu * (ey - sy) + sy + 1.5D;
/* 177 */     double[] arrXY_1 = rotateVec(arrx - this.x0, 
/* 178 */       arry - this.y0, 
/* 179 */       awrad, 
/* 180 */       true, 
/* 181 */       arraow_len);
/* 182 */     double[] arrXY_2 = rotateVec(arrx - this.x0, 
/* 183 */       arry - this.y0, 
/* 184 */       -awrad, 
/* 185 */       true, 
/* 186 */       arraow_len);
/* 187 */     double x_3 = arrx - arrXY_1[0];
/* 188 */     double y_3 = arry - arrXY_1[1];
/* 189 */     double x_4 = arrx - arrXY_2[0];
/* 190 */     double y_4 = arry - arrXY_2[1];
/*     */ 
/* 192 */     Double X3 = new Double(x_3);
/* 193 */     x3 = X3.intValue();
/* 194 */     Double Y3 = new Double(y_3);
/* 195 */     y3 = Y3.intValue();
/* 196 */     Double X4 = new Double(x_4);
/* 197 */     x4 = X4.intValue();
/* 198 */     Double Y4 = new Double(y_4);
/* 199 */     y4 = Y4.intValue();
/*     */ 
/* 202 */     g.drawLine(sx, sy, ex, ey);
/*     */ 
/* 204 */     GeneralPath triangle = new GeneralPath();
/* 205 */     triangle.moveTo(arrx, arry);
/* 206 */     triangle.lineTo(x3, y3);
/* 207 */     triangle.lineTo(x4, y4);
/* 208 */     triangle.closePath();
/*     */ 
/* 210 */     ((Graphics2D)g).fill(triangle);
/*     */   }
/*     */ 
/*     */   public static double[] rotateVec(double e, double f, double ang, boolean isChLen, double newLen)
/*     */   {
/* 224 */     double[] mathstr = new double[2];
/*     */ 
/* 226 */     double vx = e * Math.cos(ang) - f * Math.sin(ang);
/* 227 */     double vy = e * Math.sin(ang) + f * Math.cos(ang);
/* 228 */     if (isChLen)
/*     */     {
/* 230 */       double d = Math.sqrt(vx * vx + vy * vy);
/* 231 */       vx = vx / d * newLen;
/* 232 */       vy = vy / d * newLen;
/* 233 */       mathstr[0] = vx;
/* 234 */       mathstr[1] = vy;
/*     */     }
/* 236 */     return mathstr;
/*     */   }
/*     */ }

/* Location:           C:\javaplugins\tuopu\newtuopu.jar
 * Qualified Name:     ict.topo.view.TopoLink
 * JD-Core Version:    0.6.2
 */