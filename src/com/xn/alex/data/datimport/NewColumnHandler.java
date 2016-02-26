package com.xn.alex.data.datimport;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class NewColumnHandler{
	
    protected JFrame frame;
	
    protected JPanel contentPane;
    
    protected JPanel topPanel;
    
    protected JPanel bottomPanel;
    
    protected JScrollPane topScrolPanel;
    
    protected JButton okBt;
    
    protected JButton cancelBt;
    
    protected JTable table;
    
    private volatile boolean isFinished = false;
    
    private Vector<String> m_columnNames;
    
    private Map<Integer, String> m_issColumnIndToChnNameMap;
    
    private List<Integer> m_missingColumnIndexList;
         
    public boolean isFinished() {
		return isFinished;
	}


	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public NewColumnHandler(Vector<String> columnNames, Map<Integer, String> MissColumnIndToChnNameMap, List<Integer> missingColumnIndexList, DataImport dataImprtHandler){
		m_columnNames = columnNames;
		m_issColumnIndToChnNameMap = MissColumnIndToChnNameMap;
		m_missingColumnIndexList = missingColumnIndexList;
	}
	
	public void run(){
		if(false == ColumnCheck(m_columnNames)){
			return;
		}
		
		createSelectedWindow(m_issColumnIndToChnNameMap);
		
		HandleNewColumnType(m_columnNames,m_issColumnIndToChnNameMap);
		
	}
	
	private void createSelectedWindow(Map<Integer, String> MissColumnIndToChnNameMap){
		
	}
	
	private boolean ColumnCheck(Vector<String> columnNames){
		String primaryKey = "customerID";				        
		if(columnNames.contains(primaryKey) == false){	
			System.out.println("缺乏客户编号！");
			JOptionPane.showMessageDialog(null,"缺乏客户编号！","错误信息",JOptionPane.ERROR_MESSAGE);		    
		    return false;
		    	
	    } 	
		
		
		return true;
	}


	public void HandleNewColumnType(Vector<String> columnNames, Map<Integer, String> MissColumnIndToChnNameMap){    	
    	
    	createColumnSelectWindow(MissColumnIndToChnNameMap);
    	
    	addListenerForButton(MissColumnIndToChnNameMap);
    	
    }

		
	
	public void createColumnSelectWindow(Map<Integer, String> MissColumnIndToChnNameMap){
		frame = new JFrame("列数据类型选择");
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
		contentPane = new JPanel();
				
		contentPane.setLayout(new BorderLayout());
					
		topPanel = new JPanel();
		
		topScrolPanel = new JScrollPane(); 
		
		GridLayout layout1 = new GridLayout(0,1);
	     
		topPanel.setLayout(layout1);
		
		TableValues tv = new TableValues(MissColumnIndToChnNameMap);
		
		table = new JTable(tv); 
		
		table.setRowHeight(30);
		
		TableColumnModel tcm= table.getColumnModel();
		
        TableColumn tc = tcm.getColumn(TableValues.NUMERIC_TYPE);
        
        GenderRenderer cellRenderer = new GenderRenderer(); 
       
        tc.setCellRenderer(cellRenderer);
        
        tc.setCellEditor(new GenderEditor());
		
		topScrolPanel.setViewportView(table);
		
		topPanel.add(topScrolPanel);
								
		contentPane.add(topPanel, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		
        GridLayout Layout2 = new GridLayout(0,2);
	    
	    bottomPanel.setLayout(Layout2);
	    
	    Layout2.setHgap(30);
		
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));
		
		okBt = new JButton("确定");
	     
	    bottomPanel.add(okBt);
	     
	    cancelBt = new JButton("取消");
	     
	    bottomPanel.add(cancelBt);
	     
	    contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
		frame.setContentPane(contentPane);

		frame.pack();		
		     
		frame.setSize(320, 400);
		
		frame.setLocationRelativeTo(null);
		     
		frame.setResizable(false);

	    frame.setVisible(true);
		
	}
	
	private void addListenerForButton(Map<Integer, String> MissColumnIndToChnNameMap){
		okBt.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent Event) {
				// TODO Auto-generated method stub
				int row = table.getRowCount();				
				
				for(int i=0;i<row;i++){
					String columnName = table.getValueAt(i, 0).toString();
					
					String value = table.getValueAt(i, 1).toString();
					
					//System.out.println("row:" + i + " colunName:" + columnName + " value:" + value);
					
				}
												
				frame.dispose();
				
			}
			
		});
		
		cancelBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent Event){
				// TODO Auto-generated method stub
				frame.dispose();
			}
		});
	}
	
}
