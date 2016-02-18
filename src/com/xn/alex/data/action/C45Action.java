package com.xn.alex.data.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.xn.alex.data.c45.DecisionTree;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.graphics.TreeDataSheet;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.window.MainWindow;

public class C45Action extends WindowAction{
	
	private static C45Action c45ActionHandler = null;
	
	private JFrame frame;
	
	private JPanel contentPane;
	
	private JTextField treewidthTextField;
	
	private JTextField treeDeepthTextField;
	
	private JButton okBt;
	
	private JButton cancelBt;
	
	private int treeWidth = -1;
	
	private int treeDeepth = -1;
	
	private Vector<Integer> selectedCol=new Vector<Integer>();
	
	private JPanel finalPanel;
	
	private JTable table;
	
	private JCheckBox checkBox;
	
	private JComboBox<String> box;
	
	private JLabel l = new JLabel("因变量选择");
	
	private int depVar=0;
	
	private C45Action(){
		 
	 }
	 
	 public static C45Action Instance(){
		 
		 if(null == c45ActionHandler){
			 
			 c45ActionHandler = new C45Action();
		 }
		 
		 return c45ActionHandler;		 
	 }
	 
	 public void takeAction(){
		 try{
			 
		     createWindow();
		     
		     addListener();
		     
		     
		 }
		 catch(Exception e){
			 
			 System.out.print("生成C45树失败");
			 
			 e.printStackTrace();
		 }
		 
	 }
	 
	 private void createWindow(){
		 
		 frame = new JFrame("C4.5变量选择");
		 
		 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	     
	     contentPane = new JPanel();
	     
	     contentPane.setLayout(new BorderLayout());
	     
	     JPanel topPanel = new JPanel();
	     
	     GridLayout layout = new GridLayout(3,2);
	     
	     topPanel.setLayout(layout);
	     
	     layout.setVgap(50);
	     
	     layout.setHgap(8);
	     
	     //topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));
	     
	     JLabel treewidthLabel = new JLabel("树的宽度");
	     
	     topPanel.add(treewidthLabel);
	     
	     treewidthTextField = new JTextField(1);
	     
	     topPanel.add(treewidthTextField);
	     
	     JLabel treeDeepthLabel = new JLabel("树的深度");
	     
	     topPanel.add(treeDeepthLabel);
	     
	     treeDeepthTextField = new JTextField(1);
	     
	     topPanel.add(treeDeepthTextField);
	     
	     contentPane.add(topPanel, BorderLayout.NORTH);
	     
	     JPanel bottomPanel = new JPanel();
	     
	     okBt = new JButton("确定");
	     
	     bottomPanel.add(okBt);
	     
	     cancelBt = new JButton("取消");
	     
	     bottomPanel.add(cancelBt);
	     
	     contentPane.add(bottomPanel, BorderLayout.SOUTH);
	     ///////////
	     selectedCol.clear();
	     
	     finalPanel=new JPanel();
	     
	     finalPanel.setLayout(new BorderLayout());
	     
	     finalPanel.add(contentPane, BorderLayout.EAST);
	     
	     JPanel leftPanel=new JPanel();
	     
	     finalPanel.add(leftPanel, BorderLayout.WEST);
	     
	     Vector<String> name = new Vector<String>();
	     
	     name.add("自变量选择");
	     
	     name.add("选择");
	     
	     Vector <Vector <String>> d= new Vector <Vector <String>>();
	     
	     Vector <String> temp;
	     
	     for(int i=0;i<MainWindow.getJtableColumnVec().size();i++){
	    	 
	    	 temp= new Vector<String>();
	    	 
	    	 temp.add( MainWindow.getJtableColumnVec().get(i));
	    	 
	    	 d.add(temp);
	    	 
	     }
	     
	     table= new JTable(d,name);
	     
	     FitTableColumns(table);
	     
	     table.setPreferredScrollableViewportSize(new Dimension(550,30));
	     
	     JScrollPane scrollPane=new JScrollPane(table);
	     
	     scrollPane.setPreferredSize(new Dimension(200, 600));
	     
	     leftPanel.add(scrollPane);
	     
	     TableColumnModel tcm = table.getColumnModel();
	     
	     checkBox = new JCheckBox();
			
	     checkBox.setForeground(table.getTableHeader().getForeground());
	     
	     checkBox.setBackground(table.getTableHeader().getBackground());
	     
	     checkBox.setHorizontalAlignment(SwingConstants.CENTER);
	     
	     checkBox.setVisible(true);
			
	     tcm.getColumn(1).setCellEditor(new DefaultCellEditor(checkBox));
	     
//	     tcm.getColumn(1).setCellEditor(new CheckBoxCellEditor());
	     
//	     tcm.getColumn(1).setCellRenderer(new CWCheckBoxRenderer());
	     
	     topPanel.add(l);
	     
	     box=new JComboBox<String>();
	     
	     topPanel.add(box);
	     
	     for(Vector<String> s: d){
	    	 
	    	 box.addItem(s.get(0));
	    	 
	     }
	     
	     box.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent I) {
				// TODO Auto-generated method stub
				depVar=box.getSelectedIndex();
				
			}
	    	 
	     });
	     ////////////
	     
	     frame.setContentPane(finalPanel);

	     frame.pack();

	     frame.setLocationRelativeTo(null);
	     
	     frame.setSize(600,400);
	     
	     frame.setResizable(false);

	     frame.setVisible(true);
		 
	 }
	 
	 private void addListener(){
		 
		 cancelBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				frame.dispose();
			}
			 
		 });
		 
		 table.addMouseListener(new MouseAdapter(){
			 
			    public void mouseClicked(MouseEvent e){
			    	
			        if(e.getClickCount() == 1){
			        	
			            int columnIndex = table.columnAtPoint(e.getPoint()); //获取点击的列
			            
			            int rowIndex = table.rowAtPoint(e.getPoint()); //获取点击的行

			            if(columnIndex == 1) {//第0列时，执行代码
			            	
			            	
			                    if(table.getValueAt(rowIndex,columnIndex) == null){ //如果未初始化，则设置为false
			                          //table.setValueAt(false, rowIndex, columnIndex);
			                    	selectedCol.addElement(rowIndex);
			                    	
			                      }

			                    if(((Boolean)table.getValueAt(rowIndex,columnIndex)).booleanValue()){ //原来选中
			                          //table.setValueAt(false, rowIndex, 1); //点击后，取消选
			                    	selectedCol.addElement(rowIndex);
			                    	
			                      }
			                    else {//原来未选中
			                          //table.setValueAt(true, rowIndex, 1);
			                    	
			                    	selectedCol.remove(new Integer(rowIndex));
			                    	
			                      }
			                 
			                  //System.out.println(selectedCol);
			             }
			        }
			    
			    }
		 });
		 
		 okBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				if(false == isValueValid()){
					
			    	 return;
			     }
			     
			    //do something, call tree generate algorithm
			    executeAlogrithm(treeWidth,treeDeepth);
			    
			    //TreeDataSheet.Instance().test();
			    
			    frame.dispose();				
			}
			 
		 });
		 
	 }
	 
	 private boolean isValueValid(){
		 
		 String treeWidthText = treewidthTextField.getText();
		 
		 if("".equals(treeWidthText) || "-1".equals(treeWidthText)){
			 
			 JOptionPane.showMessageDialog(null,"树宽度","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
		 }
		 
		 treeWidth = Integer.parseInt(treeWidthText);
		 
		 String treeDeepthText = treeDeepthTextField.getText();
		 
		 if("".equals(treeDeepthText) || "-1".equals(treeDeepthText)||"1".equals(treeDeepthText)){
			 
			 JOptionPane.showMessageDialog(null,"树深度","错误信息",JOptionPane.ERROR_MESSAGE);
			 
			 return false;
			 
		 }
		 
		 treeDeepth = Integer.parseInt(treeDeepthText);
		 
		 return true;
	 }
	 
	 public void FitTableColumns(JTable myTable){  
		 
		    JTableHeader header = myTable.getTableHeader(); 
		    
		     int rowCount = myTable.getRowCount();  
		  
		     Enumeration<TableColumn> columns = myTable.getColumnModel().getColumns(); 
		     
		     while(columns.hasMoreElements()){  
		    	 
		         TableColumn column = (TableColumn)columns.nextElement();  
		         
		         int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
		         
		         int width = (int)myTable.getTableHeader().getDefaultRenderer()  
		        		 
		                 .getTableCellRendererComponent(myTable, column.getIdentifier()  
		                		 
		                         , false, false, -1, col).getPreferredSize().getWidth(); 
		         
		         for(int row = 0; row<rowCount; row++){  
		        	 
		             int preferedWidth = (int)myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,  
		            		 
		               myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
		             
		             width = Math.max(width, preferedWidth);  
		             
		         }  
		         
		         header.setResizingColumn(column); // 此行很重要  
		         
		         column.setWidth(width+myTable.getIntercellSpacing().width);  
		         
		     }  
		     
		}  
	 
	 private void executeAlogrithm(int treeWidth, int treeHeight){
		 
		 System.out.println("C4.5算法分析中...");
		 
		 DecisionTree.entireTree.clear();
		 
     	//先初始化数据，然后调用算法，然后调用画图
     	Vector<Vector<String>> d=new Vector<Vector<String>>();
     	
     	Vector<String> attribute=new Vector<String>();
     	
     	Vector<String> columnName=new Vector<String>();
     	
     	ResultSet rs;
     	
     	String tableName=MainWindow.fileNameToTableMap.get(MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode()));
     	
     	selectedCol.add(depVar);
     	//得到columnName
     	for(int i: selectedCol)
     		
     		columnName.add(ConfigParser.chnToEnColumnName.get(MainWindow.getJtableColumnVec().get(i)));
     	
     	//加入自变量数据
     	for(int i=0;i<MainWindow.getJtableValueVec().size();i++)
     		
     		d.add(new Vector<String>());
     	
     	int i=0;
     	
     	for(String s:columnName){
     		
     		
     		
     		rs=DatabaseAction.Instance().getOneResult(tableName, s,"1=1");
     		
     		try {
     			
     			ResultSetMetaData rsmd = rs.getMetaData();
     			
					attribute.addElement(rsmd.getColumnName(1));
					
					for(int j=1;rs.next();j++)
						
		        		d.get(j-1).add(i, rs.getString(1));
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
     		
     		i++;
     		
     	}
//     	for(int i=0;i<valueVec.size();i++)
//     		for(int j=0;j<CommonConfig.selectedColumn.size();j++)
//     			d.get(i).add(valueVec.get(i).get(CommonConfig.selectedColumn.get(j)));
     	//加入因变量数据
//     	for(int i=0;i<d.size();i++)
//     		d.get(i).add(CommonConfig.dependentVariable.get(i));
     	//初始化列名
     	//调用C4.5函数
//     	Vector<String> attribute2=new Vector<String>();
//     	
//     	for(int i=attribute.size()-1;i>=0;i--)
//     		attribute2.add(attribute.get(i));
     	
     	DecisionTree dt = new DecisionTree(treeHeight);
     	
     	treeNodeResultObj tree =dt.createDT(d,attribute,treeHeight);
     	
     	//System.out.println(tree);
     	TreeDataSheet.Instance().show(tree);
     	
	 }
	 
	 
	//~ Inner Classes ----------------------------------------------------------------------------------------------------
	 
	 class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
	   //~ Static fields/initializers -------------------------------------------------------------------------------------
	  
	   private static final long serialVersionUID = 1L;
	  
	   //~ Instance fields ------------------------------------------------------------------------------------------------
	  
	   protected JCheckBox checkBox;
	  
	   //~ Constructors ---------------------------------------------------------------------------------------------------
	  
	   public CheckBoxCellEditor() {
	     checkBox = new JCheckBox();
	     checkBox.setHorizontalAlignment(SwingConstants.CENTER);
	     // checkBox.setBackground( Color.white);
	   }
	  
	   //~ Methods --------------------------------------------------------------------------------------------------------
	  
	   @Override public Object getCellEditorValue() {
	     return Boolean.valueOf(checkBox.isSelected());
	   }
	  
	   //~ ----------------------------------------------------------------------------------------------------------------
	  
	   @Override public Component getTableCellEditorComponent(
	     JTable  table,
	     Object  value,
	     boolean isSelected,
	     int     row,
	     int     column) {
	     checkBox.setSelected(((Boolean) value).booleanValue());
	  
	     return checkBox;
	  
	   }
	 } // end class CheckBoxCellEditor
	  
	 class CWCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	   //~ Static fields/initializers -------------------------------------------------------------------------------------
	  
	   private static final long serialVersionUID = 1L;
	  
	   //~ Instance fields ------------------------------------------------------------------------------------------------
	  
	   Border border = new EmptyBorder(1, 2, 1, 2);
	  
	   //~ Constructors ---------------------------------------------------------------------------------------------------
	  
	   public CWCheckBoxRenderer() {
	     super();
	     setOpaque(true);
	     setHorizontalAlignment(SwingConstants.CENTER);
	   }
	  
	   //~ Methods --------------------------------------------------------------------------------------------------------
	  
	   @Override public Component getTableCellRendererComponent(
	     JTable  table,
	     Object  value,
	     boolean isSelected,
	     boolean hasFocus,
	     int     row,
	     int     column) {
	     if (value instanceof Boolean) {
	       setSelected(((Boolean) value).booleanValue());
	  
	       // setEnabled(table.isCellEditable(row, column));
	       setForeground(table.getForeground());
	       setBackground(table.getBackground());
	  
	     }
	  
	     return this;
	   }
	 } // end class CWCheckBoxRenderer
}
