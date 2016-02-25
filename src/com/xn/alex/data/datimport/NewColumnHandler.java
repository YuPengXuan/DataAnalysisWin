package com.xn.alex.data.datimport;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class NewColumnHandler extends Thread implements ActionListener{
	
    protected JFrame frame;
	
    protected JPanel contentPane;
    
    protected JPanel topPanel;
    
    protected JScrollPane topScrolPanel;
    
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
		
		TableValues tv = new TableValues();
		
		table = new JTable(tv); 
		
		table.setRowHeight(30);
		
		TableColumnModel tcm= table.getColumnModel();
		
        TableColumn tc = tcm.getColumn(TableValues.NUMERIC_TYPE);
 
        tc.setCellRenderer(new GenderRenderer());
        
        tc.setCellEditor(new GenderEditor());
		
		topScrolPanel.setViewportView(table);
		
		topPanel.add(topScrolPanel);
								
		contentPane.add(topPanel, BorderLayout.CENTER);
		
		frame.setContentPane(contentPane);

		frame.pack();		
		     
		frame.setSize(300,400);
		
		frame.setLocationRelativeTo(null);
		     
		frame.setResizable(false);

	    frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

}
