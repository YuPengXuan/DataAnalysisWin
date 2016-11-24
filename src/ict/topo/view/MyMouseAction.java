/*     */ package ict.topo.view;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import java.awt.PopupMenu;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;

/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;

import com.xn.alex.data.action.ExportRuleAction;
import com.xn.alex.data.action.ImportDataAction;
import com.xn.alex.data.action.RocAction;
import com.xn.alex.data.action.SavePicAction;
import com.xn.alex.data.action.WindowAction;
/*     */ 
/*     */ public class MyMouseAction
/*     */   implements MouseListener, MouseMotionListener
/*     */ {
/*     */   private Point point;
/*     */   private static Component lastCom;
/*     */   private int selectedIndex;
/*     */   private int lastSelectedIndex;
/*     */   private PopupMenu popupMenu;
/*     */   //private JScrollPane rightPane;
/*     */   private JLayeredPane currentPane;
/*     */   private Map<Integer, ArrayList<TopoNode>> nodeList;
/*     */   private Map<Integer, ArrayList<TopoLink>> linkList;
/*     */   private static ArrayList<JLayeredPane> allPanes;
/*     */   private static int lastTab;
/*     */   public boolean diy;
            private boolean isDataImport = false;
/*     */   //private boolean mode;
/*     */ 
/*     */   public MyMouseAction()
/*     */   {
/*  56 */     //this.mode = false;
/*  57 */     JLabel label = new JLabel();
/*  58 */     this.point = new Point(0, 0);
/*  59 */     lastCom = label;
/*  60 */     this.selectedIndex = 0;
/*  61 */     lastTab = 0;
/*  62 */     this.lastSelectedIndex = -1;
/*  63 */     this.nodeList = new HashMap<Integer, ArrayList<TopoNode>>();
/*  64 */     this.linkList = new HashMap<Integer, ArrayList<TopoLink>>();
/*  65 */     allPanes = new ArrayList<JLayeredPane>();
/*  66 */     this.popupMenu = new PopupMenu();
/*  67 */     //this.rightPane = new JScrollPane();
/*  68 */     this.currentPane = new JLayeredPane();
/*     */   }
/*     */ 
/*     */   public void setMode(boolean mode)
/*     */   {
/*  76 */     //this.mode = mode;
/*     */   }
/*     */ 
/*     */   public Component getLastCom()
/*     */   {
/*  84 */     return lastCom;
/*     */   }
/*     */ 
/*     */   public JLayeredPane getCurrentPane()
/*     */   {
/*  92 */     return this.currentPane;
/*     */   }
/*     */ 
/*     */   public void setCurrentPane(JLayeredPane currentPane)
/*     */   {
/* 100 */     this.currentPane = currentPane;
/*     */   }
/*     */ 
/*     */   public void addNodes(int index, TopoNode node)
/*     */   {
/* 105 */     if (this.nodeList.containsKey(Integer.valueOf(index)))
/*     */     {
/* 107 */       (this.nodeList.get(Integer.valueOf(index))).add(node);
/*     */     }
/*     */     else
/*     */     {
/* 111 */       ArrayList<TopoNode> temp = new ArrayList<TopoNode>();
/* 112 */       temp.add(node);
/* 113 */       this.nodeList.put(Integer.valueOf(index), temp);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Map<Integer, ArrayList<TopoNode>> getNodes()
/*     */   {
/* 119 */     return this.nodeList;
/*     */   }
/*     */ 
/*     */   public void setCurrentTab(int tab)
/*     */   {
/* 124 */     lastTab = tab;
/*     */   }
/*     */ 
/*     */   public void addlinks(int index, TopoLink link)
/*     */   {
/* 129 */     if (this.linkList.containsKey(Integer.valueOf(index)))
/*     */     {
/* 131 */       (this.linkList.get(Integer.valueOf(index))).add(link);
/*     */     }
/*     */     else
/*     */     {
/* 135 */       ArrayList<TopoLink> temp = new ArrayList<TopoLink>();
/* 136 */       temp.add(link);
/* 137 */       this.linkList.put(Integer.valueOf(index), temp);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Map<Integer, ArrayList<TopoLink>> getLinks()
/*     */   {
/* 143 */     return this.linkList;
/*     */   }
/*     */ 
/*     */   public void addPanes(JLayeredPane pane)
/*     */   {
/* 148 */     allPanes.add(pane);
/*     */   }
/*     */ 
/*     */   public void setPopMenu(PopupMenu popMenu)
/*     */   {
/* 153 */     this.popupMenu = popMenu;
/*     */   }
/*     */ 
/*     */   public void setRightPane(JScrollPane pane)
/*     */   {
/* 158 */     //this.rightPane = pane;
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent e)
/*     */   {
/* 167 */     Component com = (Component)e.getSource();
/*     */ 
/* 169 */     if ((e.getSource() instanceof TopoNode))
/*     */     {
/* 172 */       Point newPoint = SwingUtilities.convertPoint(com, 
/* 173 */         e.getPoint(), 
/* 174 */         com.getParent());
/*     */ 
/* 176 */       com.setLocation(com.getX() + (newPoint.x - this.point.x), 
/* 177 */         com.getY() + (newPoint.y - this.point.y));
/*     */ 
/* 179 */       this.point = newPoint;
/* 180 */       com.repaint();
/* 181 */       com.getParent().repaint();
/*     */     }
/* 184 */     else if ((e.getSource() instanceof JPanel))
/*     */     {
/* 186 */       ((JPanel)e.getSource()).repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent e)
/*     */   {
/* 196 */     if ((e.getSource() instanceof TopoNode))
/*     */     {
/* 199 */       Component com = (Component)e.getSource();
/*     */ 
/* 202 */       this.point = SwingUtilities.convertPoint(com, 
/* 203 */         e.getPoint(), 
/* 204 */         com.getParent());
/* 205 */       JLabel temp = (JLabel)com;
/* 206 */       MyBorder border = new MyBorder();
/* 207 */       temp.setBorder(border);
/* 208 */       if ((temp != lastCom) && ((lastCom instanceof JLabel)))
/*     */       {
/* 210 */         ((JLabel)lastCom).setBorder(null);
/* 211 */         lastCom = temp;
/*     */       }
/*     */ 
/*     */     }
/* 215 */     else if ((e.getSource() instanceof TopoLink))
/*     */     {
/* 217 */       ((JLabel)lastCom).setBorder(null);
/*     */ 
/* 219 */       ArrayList<?> arr = (ArrayList<?>)this.nodeList.get(Integer.valueOf(lastTab));
/* 220 */       if (arr != null)
/*     */       {
/* 222 */         for (int i = 0; i < arr.size(); i++)
/*     */         {
/* 224 */           TopoNode tempNode = (TopoNode)arr.get(i);
/* 225 */           if (tempNode.getBounds().contains(e.getPoint()))
/*     */           {
/* 228 */             ((JLayeredPane)tempNode.getParent()).moveToFront(tempNode);
/* 229 */             MyBorder border = new MyBorder();
/* 230 */             tempNode.setBorder(border);
/* 231 */             if ((tempNode != lastCom) && ((lastCom instanceof JLabel)))
/*     */             {
/* 233 */               ((JLabel)lastCom).setBorder(null);
/* 234 */               lastCom = tempNode;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 240 */     else if ((e.getSource() instanceof JTree))
/*     */     {
/* 242 */       ((JLabel)lastCom).setBorder(null);
/* 243 */       JTree tree = (JTree)e.getSource();
/* 244 */       DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
/*     */ 
/* 246 */       if (selectedNode == null) {
/* 247 */         return;
/*     */       }
/* 249 */       DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParent();
/*     */ 
/* 251 */       if (parent == null) {
/* 252 */         return;
/*     */       }
/* 254 */       this.selectedIndex = parent.getIndex(selectedNode);
/* 255 */       if (this.lastSelectedIndex != this.selectedIndex)
/*     */       {
/* 257 */         MyBorder border = new MyBorder();
/* 258 */         if (((ArrayList<?>)this.nodeList.get(Integer.valueOf(lastTab))).size() <= this.selectedIndex)
/*     */         {
/* 260 */           return;
/*     */         }
/* 262 */         ((TopoNode)((ArrayList<?>)this.nodeList.get(Integer.valueOf(lastTab))).get(this.selectedIndex)).setBorder(border);
/* 263 */         if (this.lastSelectedIndex != -1)
/*     */         {
/* 265 */           ((TopoNode)((ArrayList<?>)this.nodeList.get(Integer.valueOf(lastTab))).get(this.lastSelectedIndex)).setBorder(null);
/*     */         }
/* 267 */         this.lastSelectedIndex = this.selectedIndex;
/* 268 */         lastCom = (Component)((ArrayList<?>)this.nodeList.get(Integer.valueOf(lastTab))).get(this.lastSelectedIndex);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseClicked(MouseEvent e)
/*     */   {
/* 277 */     Component com = (Component)e.getSource();
/* 278 */     String tempStr = null;
/* 279 */     TopoNode tempNode = null;
/*     */ 
/* 281 */     int mods = e.getModifiers();
/* 282 */     if ((mods & 0x4) != 0)
/*     */     {
/* 284 */       ((JLayeredPane)allPanes.get(lastTab)).add(this.popupMenu);
/* 285 */       if ((com instanceof TopoNode))
/*     */       {
/* 288 */         this.popupMenu.removeAll();
/* 289 */         showMenu(e, com);
/*     */       }
/* 291 */       else if ((com instanceof TopoLink))
/*     */       {
/* 293 */         ArrayList<?> arr = (ArrayList<?>)this.nodeList.get(Integer.valueOf(lastTab));
/* 294 */         for (int i = 0; i < arr.size(); i++)
/*     */         {
/* 296 */           tempNode = (TopoNode)arr.get(i);
/* 297 */           if (tempNode.getBounds().contains(e.getPoint()))
/*     */           {
/* 299 */             this.popupMenu.removeAll();
/* 300 */             showMenu(e, tempNode);
/* 301 */             tempNode = null;
/* 302 */             return;
/*     */           }
/*     */         }
/*     */ 
/* 306 */         int mx = e.getX(); int my = e.getY();
/* 307 */         double min = 200.0D; double t = 0.0D;
/* 308 */         TopoLink temp = null; TopoLink showLine = null;
/* 309 */         ArrayList<?> ar = (ArrayList<?>)this.linkList.get(Integer.valueOf(lastTab));
/* 310 */         for (int i = 0; i < ar.size(); i++)
/*     */         {
/* 312 */           temp = (TopoLink)ar.get(i);
/* 313 */           t = cal(temp.x0 + temp.lw / 2, 
/* 314 */             temp.y0 + temp.lh / 2, 
/* 315 */             temp.x1 + temp.rw / 2, 
/* 316 */             temp.y1 + temp.rh / 2, 
/* 317 */             mx, 
/* 318 */             my);
/* 319 */           if (t < min)
/*     */           {
/* 321 */             min = t;
/* 322 */             showLine = temp;
/*     */           }
/*     */         }
/* 325 */         if (showLine != null)
/*     */         {
/* 327 */           this.popupMenu.removeAll();
/* 328 */           showMenu(e, showLine);
/* 329 */           tempStr = null;
/*     */         }
/*     */       }
/*     */     }
/* 333 */     else if ((e.getClickCount() >= 2) && ((com instanceof JLabel)))
/*     */     {
/* 335 */       if ((com instanceof TopoLink))
/*     */       {
/* 337 */         int mx = e.getX(); int my = e.getY();
/* 338 */         double min = 200.0D; double t = 0.0D;
/* 339 */         TopoLink temp = null; TopoLink showLine = null;
/* 340 */         ArrayList<?> ar = (ArrayList<?>)this.linkList.get(Integer.valueOf(lastTab));
/* 341 */         for (int i = 0; i < ar.size(); i++)
/*     */         {
/* 343 */           temp = (TopoLink)ar.get(i);
/* 344 */           t = cal(temp.x0 + temp.lw / 2, 
/* 345 */             temp.y0 + temp.lh / 2, 
/* 346 */             temp.x1 + temp.rw / 2, 
/* 347 */             temp.y1 + temp.rh / 2, 
/* 348 */             mx, 
/* 349 */             my);
/* 350 */           if (t < min)
/*     */           {
/* 352 */             min = t;
/* 353 */             showLine = temp;
/*     */           }
/*     */         }
/*     */ 
/* 357 */         if (showLine != null)
/*     */         {
/* 359 */           tempStr = "链路:" + showLine.getLineName() + "\n";
/* 360 */           if (showLine.getCost() != 0)
/*     */           {
/* 362 */             tempStr = tempStr + "Cost: " + showLine.getCost();
/*     */           }
/* 364 */           JOptionPane.showMessageDialog(showLine.getParent(), tempStr);
/* 365 */           tempStr = null;
/*     */         }
/*     */       }
/* 368 */       else if ((com instanceof TopoNode))
/*     */       {
/* 370 */         showDialog(e, com);
/*     */       }
/*     */     }
/* 373 */     else if ((com instanceof JMenu))
/*     */     {
	            WindowAction action = null;
	            if("exportRule".equals(com.getName())){
	            	action = ExportRuleAction.Instance();	            	
	            }
	            else if("importData".equals(com.getName())){
	            	action = ImportDataAction.Instance();
	            	isDataImport = true;
	            }
	            else if("generateRoc".equals(com.getName())){
	            	action = RocAction.Instance();
	            	RocAction.Instance().setDataImport(isDataImport);
	            }
	            else if("savePic".equals(com.getName())){
	            	action = SavePicAction.Instance();
	            	SavePicAction.Instance().setCurrentPane(currentPane);
	            }
	            
	            if(null!=action){
	            	action.takeAction();
	            }
/* 375 */       //int index = Integer.parseInt(((JMenu)com).getName());
/* 376 */       //if (!this.mode)
/*     */       //{
/* 378 */       //  if ((lastTab != index) && (index < allPanes.size()))
/*     */       //  {
/* 380 */       //    ((JLayeredPane)allPanes.get(lastTab)).setVisible(false);
/* 381 */       //     JLayeredPane pane = (JLayeredPane)allPanes.get(index);
/* 382 */       //    this.rightPane.setViewportView(pane);
/* 383 */       //   pane.setVisible(true);
/* 384 */       //    lastTab = index;
/* 385 */       //   this.currentPane = pane;
/*     */       //  }
/*     */       // }
/*     */      // else
/*     */      // {
/* 390 */      //   clearColorLines(lastTab);
/* 391 */      //   drawColorLines(index);
/* 392 */     //    lastTab = index;
/*     */      // }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void showDialog(MouseEvent e, Component com)
/*     */   {
/* 399 */     //String tempStr = "名称：" + ((TopoNode)com).getText() + "\n";
	          //String tempStr = "名称：" + ((TopoNode)com).getName() + "\n";
	          String tempStr = "";
/* 400 */     if (((TopoNode)com).getInfo() != null)
/*     */     {
/* 402 */       //tempStr = tempStr + "信息：" + ((TopoNode)com).getInfo();
	            tempStr += ((TopoNode)com).getInfo();
/*     */     }
              JOptionPane.showMessageDialog(null, tempStr);
/* 249 */     tempStr = null;
/*     */   }
/*     */ 
/*     */   public double cal(int x1, int y1, int x2, int y2, int mx, int my)
/*     */   {
/* 409 */     double d = Math.abs((y2 - y1) * 
/* 410 */       mx + 
/* 411 */       (x1 - x2) * 
/* 412 */       my + (
/* 413 */       x2 * y1 - x1 * y2)) / 
/* 414 */       Math.sqrt(Math.pow(y2 - y1, 2.0D) + Math.pow(x1 - x2, 2.0D));
/* 415 */     return d;
/*     */   }
/*     */ 
/*     */   public void showMenu(MouseEvent e, Component com)
/*     */   {
/* 420 */     if ((com instanceof TopoLink))
/*     */     {
/* 422 */       TopoLink tl = (TopoLink)com;
/* 423 */       this.popupMenu.add("右键对象" + 
/* 424 */         tl.getLnode().getText() + 
/* 425 */         "——" + 
/* 426 */         tl.getRnode().getText());
/*     */     }
/* 428 */     else if ((com instanceof TopoNode))
/*     */     {
/* 430 */       this.popupMenu.add("右键对象" + ((TopoNode)com).getText());
/*     */     }
/* 432 */     //this.popupMenu.addSeparator();
/* 433 */     //MenuItem addAlarm = new MenuItem("\u6DFB\u52A0\u544A\u8B66");
/* 434 */     //ActionAddAlarm actionAddAlarm = new ActionAddAlarm();
/* 435 */     //actionAddAlarm.putValue("topoGraphView", com.getParent());
/* 436 */     //actionAddAlarm.putValue("selectTopoObject", com);
/* 437 */     //addAlarm.addActionListener(actionAddAlarm);
/* 438 */     //this.popupMenu.add(addAlarm);
/* 439 */     //this.popupMenu.addSeparator();
/* 440 */     //MenuItem removeAlarm = new MenuItem("删除告警");
/* 441 */     //ActionRemoveAlarm actionRemoveAlarm = new ActionRemoveAlarm();
/* 442 */     //actionRemoveAlarm.putValue("selectTopoObject", com);
/* 443 */     //removeAlarm.addActionListener(actionRemoveAlarm);
/* 444 */     //this.popupMenu.add(removeAlarm);
/* 445 */     this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
/*     */   }
/*     */ 
/*     */   public void drawColorLines(int tabIndex)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void clearColorLines(int tabIndex)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent e)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseReleased(MouseEvent e)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\javaplugins\tuopu\newtuopu.jar
 * Qualified Name:     ict.topo.view.MyMouseAction
 * JD-Core Version:    0.6.2
 */