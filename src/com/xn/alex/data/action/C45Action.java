package com.xn.alex.data.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.window.MainWindow;

public class C45Action extends WindowAction{
	
	private static C45Action c45ActionHandler = null;
	
	private JFrame frame;
	
	private JPanel contentPane;
	
	private JTextField treewidthTextField;
	
	private JTextField treeDeepthTextField;
	
	private JButton okBt = null;
	
	private JButton cancelBt = null;
	
	private JTable table;
	
	private JScrollPane leftscrollPane = null;
	
	private JPanel rightPane = null;
	
	private JSplitPane splitPane = null;
	
	private JButton selectBt = null;
	
	private JTextField argumentFd = null;
	
	private Vector<Vector<String>> valueVec = null;
	
	private String argumentString = null;
	
	private int treeDeepth = 0;
	
	private int treeWidth = 0;
		
	
	
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
			 
			 createC45MenuWindow();
		     
		     addListener();
		     
		     
		 }
		 catch(Exception e){
			 
			 System.out.print("生成C45树失败");
			 
			 e.printStackTrace();
		 }
		 
	 }
	 
	 private void createC45MenuWindow(){
		 
         frame = new JFrame("C4.5变量选择");		 
		 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		 frame.setPreferredSize(new Dimension(500,300));	 
         contentPane = new JPanel();		 
		 contentPane.setLayout(new BorderLayout());	 
		 frame.setContentPane(contentPane);
		 
		 leftscrollPane = new JScrollPane();	
		 rightPane = new JPanel();
		 splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftscrollPane, rightPane);
		 contentPane.add(splitPane, BorderLayout.CENTER);
		 //splitPane.setOneTouchExpandable(true);
		 splitPane.setDividerLocation(170);
		 splitPane.setEnabled(false);
		 		 		 		 
		 table = new JTable();
		 DefaultTableModel dtm = new DefaultTableModel();
		 //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		 table.setModel(dtm);
		 Vector<String> columnVec = new Vector<String>();
		 columnVec.add(" 属性名称  ");
		 Vector<String> columnNameVec = MainWindow.getJtableColumnVec();		 
		 valueVec = new Vector<Vector<String>>();
		 for(int i=0;i<columnNameVec.size();i++){
			 Vector<String> tmpVec = new Vector<String>();
			 tmpVec.add(columnNameVec.get(i));
			 valueVec.add(tmpVec);
		 }
		 dtm.setDataVector(valueVec, columnVec);
		 dtm.fireTableStructureChanged();
		 dtm.fireTableDataChanged();
		 leftscrollPane.setViewportView(table);
		 
		 rightPane = new JPanel();
		 rightPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		 //GridLayout rightlayout = new GridLayout(3,3,10,100);
		 //rightPane.setLayout(rightlayout);
		 rightPane.setLayout(null);
		 selectBt = new JButton();
		 selectBt.setBorder(BorderFactory.createRaisedBevelBorder());
		 selectBt.setBounds(40, 28, 48, 35);
		 selectBt.setIcon(new ImageIcon("resource/1.png"));
		 //selectBt.setPreferredSize(new Dimension(50,30));		 
		 rightPane.add(selectBt);
		 
		 argumentFd = new JTextField(4);
		 argumentFd.setBounds(140,30,150, 30);
		 argumentFd.setText("自变量:");
		 rightPane.add(argumentFd);
		 
		 JLabel treewidthLabel = new JLabel("树的宽度:");
		 treewidthLabel.setBounds(40, 80, 80, 30);
		 rightPane.add(treewidthLabel);	
		 		 
         treewidthTextField = new JTextField(4);
         treewidthTextField.setText("5");
         treewidthTextField.setBounds(140, 80, 150, 30);
         rightPane.add(treewidthTextField);
         
         JLabel treeDeepthLabel = new JLabel("树的深度:");
         treeDeepthLabel.setBounds(40, 130, 80, 30);
         rightPane.add(treeDeepthLabel);
         
         treeDeepthTextField = new JTextField(4);
         treeDeepthTextField.setText("5");
         treeDeepthTextField.setBounds(140, 130, 150, 30);
         rightPane.add(treeDeepthTextField);
         
         okBt = new JButton("确定");
         okBt.setBounds(50, 220, 80, 35);
         rightPane.add(okBt);
         
         cancelBt = new JButton("取消");
         cancelBt.setBounds(190, 220, 80, 35);
         rightPane.add(cancelBt);
                          
         splitPane.setRightComponent(rightPane);		 	 
		 frame.setContentPane(contentPane);		 
		 frame.pack();
	     frame.setLocationRelativeTo(null);	     
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
		 
		 
		 selectBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				int index = table.getSelectedRow();
				
				if(-1 == index){
					JOptionPane.showMessageDialog(null,"请选择自变量","错误信息",JOptionPane.ERROR_MESSAGE);
					return;
				}

				argumentString = valueVec.get(index).get(0);
				
				argumentFd.setText("自变量:"+argumentString);
				
			}
			 
		 });
		 
		 okBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				treeDeepth = Integer.parseInt(treeDeepthTextField.getText());
				
				treeWidth = Integer.parseInt(treewidthTextField.getText());
				
				if(treeDeepth > 10 || treeDeepth<0){
					
					JOptionPane.showMessageDialog(null,"树深度超出范围,请重新输入","错误信息",JOptionPane.ERROR_MESSAGE);
					
					return;
					
				}
				
				if(treeWidth>10 || treeWidth<0){
					
                    JOptionPane.showMessageDialog(null,"树宽度超出范围,请重新输入","错误信息",JOptionPane.ERROR_MESSAGE);
					
					return;
					
				}
								
								
                String databaseColumnName = ConfigParser.chnToEnColumnName.get(argumentString);
				
				String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());

				String tableName = MainWindow.fileNameToTableMap.get(fileName);				
				
				int result = DatabaseAction.Instance().getDistinctCountFromColumn(databaseColumnName, tableName);
				
				if(result != 2){
					
					JOptionPane.showMessageDialog(null,"自变量选择非法,可能性取值不等于2,请重新选择自变量","错误信息",JOptionPane.ERROR_MESSAGE);
					return;
					
				}				
				
				frame.dispose();
			}
			 
		 });
		 
		 
		 
	 }
		 
}
