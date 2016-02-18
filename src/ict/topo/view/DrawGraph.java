/*     */ package ict.topo.view;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.Toolkit;

/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.TreeNode;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ public class DrawGraph extends JFrame
/*     */ {
/*     */   private static final long serialVersionUID = -1895146153906356102L;
/*     */   private Dimension screenSize;
/*     */   private MyPoint[][] position;
/*     */   private JScrollPane left;
/*     */   private JScrollPane right;
/*     */   private JScrollPane menuScroll;
/*     */   private JPanel leftPanel;
/*     */   private JTree tree;
/*     */   private DefaultMutableTreeNode root;
/*     */   private DefaultTreeModel treeModel;
/*     */   private PopupMenu popMenu;
/*     */   private JMenuBar menuBar;
/*     */   private MyMouseAction mouseListener;
/*     */   private JLayeredPane currentPane;
/*     */   private String frameName;
/*     */   private int tabCount;
/*     */   private int currentTab;

            private Dimension area;

            private JMenu menu1;            
			private JMenu menu2;
            private JMenu menu3;
            private JMenu menu4;
            
/*     */   public DrawGraph(String name)
/*     */   {
/*  67 */     this.frameName = name;
/*  68 */     this.tabCount = 0;
/*  69 */     this.currentTab = 0;
/*  70 */     init();
/*     */   }
/*     */ 
/*     */   public DrawGraph()
/*     */   {
/*  75 */     this.tabCount = 0;
/*  76 */     this.currentTab = 0;
/*  77 */     init();
/*     */   }
/*     */ 
/*     */   public DrawGraph(MyMouseAction action)
/*     */   {
/*  82 */     this.tabCount = 0;
/*  83 */     this.currentTab = 0;
/*  84 */     setMyAction(action);
/*  85 */     init();
/*     */   }
/*     */ 
/*     */   public DrawGraph(String name, MyMouseAction action)
/*     */   {
/*  90 */     this.frameName = name;
/*  91 */     this.tabCount = 0;
/*  92 */     this.currentTab = 0;
/*  93 */     setMyAction(action);
/*  94 */     init();
/*     */   }
/*     */ 
/*     */   public void setMyAction(MyMouseAction action)
/*     */   {
/* 111 */     this.mouseListener = action;
/*     */   }
/*     */ 
/*     */   public void init()
/*     */   {
/* 117 */     this.popMenu = new PopupMenu();
/* 118 */     if (this.mouseListener == null)
/*     */     {
/* 120 */       this.mouseListener = new MyMouseAction();
/*     */     }
/* 122 */     this.mouseListener.setPopMenu(this.popMenu);
/*     */ 
/* 124 */     this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
/*     */ 
/* 126 */     this.position = new MyPoint[100][100];
/* 127 */     //int tempx = (this.screenSize.width - 200) / 16; int tempy = this.screenSize.height / 16;
/* 128 */     //int x = tempx / 2 - 40; int y = tempy / 2 - 45;
              int tempx = 100; int tempy = 180;
              int x = 10; int y = 10;
/* 129 */     for (int i = 0; i < 100; i++)
/*     */     {
/* 131 */       for (int j = 0; j < 100; j++)
/*     */       {
/* 133 */         this.position[i][j] = new MyPoint(x, y);
/* 134 */         x += tempx;
/*     */       }
/* 136 */       y += tempy;
/* 137 */       x = tempx / 2 - 40;
/*     */     }
/*     */ 
/* 140 */     this.left = new JScrollPane();
/* 141 */     this.leftPanel = new JPanel();
/* 142 */     this.right = new JScrollPane();
/* 143 */     this.menuScroll = new JScrollPane();
/* 144 */     this.mouseListener.setRightPane(this.right);
/* 145 */     this.root = new DefaultMutableTreeNode("设备列表 ");
/* 146 */     this.tree = new JTree(this.root);
/* 147 */     this.treeModel = ((DefaultTreeModel)this.tree.getModel());
/*     */ 
/* 150 */     this.leftPanel.setBackground(Color.WHITE);
/* 151 */     this.leftPanel.add(this.tree);
/* 152 */     this.left.setBounds(0, 0, 200, this.screenSize.height - 90);
/* 153 */     this.left.setBorder(BorderFactory.createEtchedBorder());
/* 154 */     this.left.setViewportView(this.leftPanel);
/* 155 */     this.left.setHorizontalScrollBarPolicy(30);
/* 156 */     this.left.setVerticalScrollBarPolicy(20);
/*     */ 
/* 158 */     //this.right.setLocation(210, 38);
              this.right.setLocation(210, 45);
/* 159 */     //this.right.setSize(this.screenSize.width - 225, this.screenSize.height - 118);
              area = new Dimension(this.screenSize.width - 225, this.screenSize.height - 118);
              this.right.setSize(area);
/* 160 */     this.right.setBorder(BorderFactory.createEtchedBorder());
/* 161 */     this.right.setHorizontalScrollBarPolicy(30);
/* 162 */     this.right.setVerticalScrollBarPolicy(20);
/*     */ 
/* 164 */     this.menuScroll.setLocation(210, 0);
/* 165 */     this.menuScroll.setSize(this.screenSize.width - 210, 50);
              //this.menuScroll.setSize(this.screenSize.width - 210, 0);
/* 166 */     this.menuScroll.setHorizontalScrollBarPolicy(30);
/* 167 */     this.menuScroll.setVerticalScrollBarPolicy(20);
/*     */ 
/* 169 */     DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
/*     */ 
/* 171 */     cellRenderer.setLeafIcon(new ImageIcon(ClassLoader.getSystemResource("images/smallTag.png")));
/* 172 */     cellRenderer.setOpenIcon(new ImageIcon(ClassLoader.getSystemResource("images/opened.png")));
/* 173 */     cellRenderer.setClosedIcon(new ImageIcon(ClassLoader.getSystemResource("images/closed.png")));
/* 174 */     this.tree.addMouseListener(this.mouseListener);
/*     */ 
/* 176 */     this.menuBar = new JMenuBar();
/* 177 */     this.menuBar.setLayout(null);
/* 178 */     this.menuBar.setPreferredSize(new Dimension(this.screenSize.width - 220, 30));
/* 179 */     this.menuScroll.setViewportView(this.menuBar);
/* 180 */     getContentPane().setLayout(null);
/* 181 */     setBounds(0, 0, this.screenSize.width, this.screenSize.height - 50);
/* 182 */     if (this.frameName != null)
/*     */     {
/* 184 */       setTitle(this.frameName);
/*     */     }
/* 186 */     setDefaultCloseOperation(3);
/* 187 */     setVisible(true);
/*     */ 
/* 189 */     getContentPane().add(this.left);
/* 190 */     getContentPane().add(this.right);
/* 191 */     getContentPane().add(this.menuScroll);

/*     */   }
/*     */ 
/*     */   public JMenuBar getRightMenuBar()
/*     */   {
/* 197 */     return this.menuBar;
/*     */   }
/*     */ 
/*     */   public void addTreeNode(String name)
/*     */   {
/* 203 */     DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name);
/* 204 */     this.treeModel.insertNodeInto(newNode, this.root, this.root.getChildCount());
/* 205 */     TreeNode[] nodes = this.treeModel.getPathToRoot(newNode);
/* 206 */     TreePath path = new TreePath(nodes);
/* 207 */     this.tree.scrollPathToVisible(path);
/*     */   }
/*     */ 
/*     */   public void addTopoData(TopoNode node, JLayeredPane pane)
/*     */   {
/* 213 */     JLabel temp = node;
/* 214 */     temp.addMouseListener(this.mouseListener);
/* 215 */     temp.addMouseMotionListener(this.mouseListener);
/* 216 */     addTreeNode(temp.getText());
/* 217 */     this.mouseListener.addNodes(Integer.parseInt(pane.getName()), node);
/* 218 */     pane.add(node);
/*     */   }
/*     */ 
/*     */   public void addTopoData(TopoNode node, int x, int y, JLayeredPane pane)
/*     */   {
/* 223 */     if ((x <= 0) || (x > 100) || (y <= 0) || (y > 100))
/*     */     {
/* 225 */       System.out.print("索引超出范围 ，输入1-100之间的数！");
/* 226 */       return;
/*     */     }
/*     */ 
/* 229 */     MyPoint point = this.position[(x - 1)][(y - 1)];
/* 230 */     node.setXY(point.x, point.y);
/* 231 */     node.addMouseListener(this.mouseListener);
/* 232 */     node.addMouseMotionListener(this.mouseListener);
/* 233 */     addTreeNode(node.getText());
/* 234 */     this.mouseListener.addNodes(Integer.parseInt(pane.getName()), node);
/* 235 */     pane.add(node);
/*     */   }
/*     */ 
/*     */   public void addTopoData(TopoLink link, JLayeredPane pane)
/*     */   {
/* 241 */     link.addMouseListener(this.mouseListener);
/* 242 */     link.addMouseMotionListener(this.mouseListener);
/* 243 */     this.mouseListener.addlinks(Integer.parseInt(pane.getName()), link);
/* 244 */     pane.add(link);
/*     */   }
/*     */ 
/*     */   public void addTopoData(TopoNode node)
/*     */   {
/* 250 */     JLabel temp = node;
/* 251 */     temp.addMouseListener(this.mouseListener);
/* 252 */     temp.addMouseMotionListener(this.mouseListener);
/* 253 */     addTreeNode(temp.getText());
/* 254 */     this.mouseListener.addNodes(this.currentTab, node);
/* 255 */     this.currentPane.add(node);
/*     */   }
/*     */ 
/*     */   public void addTopoData(TopoNode node, int x, int y)
/*     */   {
/* 260 */     if ((x <= 0) || (x > 100) || (y <= 0) || (y > 100))
/*     */     {
/* 262 */       System.out.print("索引超出范围 ，输入1-100之间的数！");
/* 263 */       return;
/*     */     }
/*     */ 
/* 266 */     MyPoint point = this.position[(x - 1)][(y - 1)];
/* 267 */     node.setXY(point.x, point.y);
              
              if(point.x>=area.width){
            	  area.width = point.x + 100;
              }
              
              if(point.y>=area.height){
            	  area.height = point.y + 100;
              }

/* 268 */     node.addMouseListener(this.mouseListener);
/* 269 */     node.addMouseMotionListener(this.mouseListener);
/* 270 */     addTreeNode(node.getText());
/* 271 */     this.mouseListener.addNodes(this.currentTab, node);
/* 272 */     this.currentPane.add(node);
              this.currentPane.setPreferredSize(area);
              this.currentPane.repaint();
/*     */   }
/*     */ 
/*     */   public void addTopoData(TopoLink link)
/*     */   {
/* 278 */     link.addMouseListener(this.mouseListener);
/* 279 */     link.addMouseMotionListener(this.mouseListener);
/* 280 */     this.mouseListener.addlinks(this.currentTab, link);
/* 281 */     this.currentPane.add(link);
/*     */   }
/*     */ 
/*     */   public void setCurrentPane(JLayeredPane current)
/*     */   {
/* 287 */     if (current == null)
/*     */     {
/* 289 */       System.out.print("无法设置当前界面");
/*     */     }
/* 291 */     this.currentPane = current;
/* 292 */     this.mouseListener.setCurrentPane(current);
/* 293 */     this.currentTab = Integer.parseInt(this.currentPane.getName());
/* 294 */     if (this.currentTab == 0)
/*     */     {
/* 296 */       this.currentPane.add(this.popMenu);
/* 297 */       this.currentPane.setVisible(true);
/* 298 */       this.right.setViewportView(this.currentPane);
/*     */     }
/* 300 */     this.right.repaint();
/*     */   }
/*     */ 
/*     */   public JLayeredPane createPane()
/*     */   {
/* 306 */     this.tabCount += 1;
/* 307 */     menu1 = new JMenu("   导出规则");
              menu1.setName("exportRule");
              menu2 = new JMenu("   导入数据");
              menu2.setName("importData");
              menu3 = new JMenu("   生成ROC");
              menu3.setName("generateRoc");
              menu4 = new JMenu("   保存图片");
              menu4.setName("savePic");
/* 308 */     //menu1.setName(Integer.toString(this.tabCount - 1));
/* 309 */     //menu1.setBounds((this.tabCount - 1) * 80, 0, 85, 28);
              menu1.setBounds(10, 10, 85, 28);
/* 310 */     menu1.setBorder(BorderFactory.createEtchedBorder());
/* 311 */     menu1.addMouseListener(this.mouseListener);

              menu2.setBounds(105, 10, 85, 28);
/* 310 */     menu2.setBorder(BorderFactory.createEtchedBorder());
/* 311 */     menu2.addMouseListener(this.mouseListener);

              menu3.setBounds(200, 10, 85, 28);
              menu3.setBorder(BorderFactory.createEtchedBorder());
/* 311 */     menu3.addMouseListener(this.mouseListener);

              menu4.setBounds(295, 10, 85, 28);
              menu4.setBorder(BorderFactory.createEtchedBorder());
/* 311 */     menu4.addMouseListener(this.mouseListener);

/* 312 */     this.menuBar.add(menu1);
              this.menuBar.add(menu2);
              this.menuBar.add(menu3);
              this.menuBar.add(menu4);
/*     */ 
/* 314 */     if (this.tabCount * 80 >= this.screenSize.width - 210)
/*     */     {
/* 316 */       this.menuBar.setPreferredSize(new Dimension(this.menuBar.getPreferredSize().width + 80, 
/* 317 */         30));
/*     */     }
/* 319 */    // this.menuBar.repaint();
/* 320 */     JLayeredPane tempPanel = new JLayeredPane();
/* 321 */     tempPanel.addMouseListener(this.mouseListener);
/* 322 */     tempPanel.addMouseMotionListener(this.mouseListener);
/* 323 */     tempPanel.setName(Integer.toString(this.tabCount - 1));
/* 324 */     tempPanel.setOpaque(true);
/* 325 */     tempPanel.setBackground(Color.white);
/* 326 */     //tempPanel.setPreferredSize(new Dimension(this.screenSize.width - 210, 
/* 327 */     //          this.screenSize.height - 60));
              tempPanel.setPreferredSize(new Dimension(this.getContentPane().getWidth(), this.getContentPane().getHeight()));
/* 328 */     tempPanel.setVisible(false);
/* 329 */     this.right.add(tempPanel);
/* 330 */     this.mouseListener.addPanes(tempPanel);
/* 331 */     return tempPanel;
/*     */   }
/*     */ 
/*     */   public MyPoint[][] getPosition()
/*     */   {
/* 336 */     return this.position;
/*     */   }
/*     */ 
/*     */   public class MyPoint
/*     */   {
/*     */     int x;
/*     */     int y;
/*     */ 
/*     */     public MyPoint(int x, int y)
/*     */     {
/* 104 */       this.x = x;
/* 105 */       this.y = y;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\javaplugins\tuopu\newtuopu.jar
 * Qualified Name:     ict.topo.view.DrawGraph
 * JD-Core Version:    0.6.2
 */