package com.xn.alex.data.datimport;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class NewColumnHandler extends Thread implements ActionListener{
	
    protected JFrame frame;
	
    protected JPanel contentPane;
    
    protected JPanel topPanel;
    
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
		HandleNewColumnType(m_columnNames,m_issColumnIndToChnNameMap);
		
	}


	public void HandleNewColumnType(Vector<String> columnNames, Map<Integer, String> MissColumnIndToChnNameMap){    	
    	
    	createColumnSelectWindow(MissColumnIndToChnNameMap);
    	
    }

		
	
	public void createColumnSelectWindow(Map<Integer, String> MissColumnIndToChnNameMap){
		frame = new JFrame("列数据类型选择");
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
		contentPane = new JPanel();
				
		contentPane.setLayout(new BorderLayout());
		/*				 
		topPanel = new JPanel();
				
		GridLayout layout1 = new GridLayout(0,1);
			     
		topPanel.setLayout(layout1);
		
		contentPane.add(topPanel, BorderLayout.NORTH);
		*/
		frame.setContentPane(contentPane);

		frame.pack();		
		     
		frame.setSize(590,620);
		
		frame.setLocationRelativeTo(null);
		     
		frame.setResizable(false);

	    frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

}
