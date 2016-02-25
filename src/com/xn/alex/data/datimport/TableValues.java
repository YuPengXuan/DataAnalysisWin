package com.xn.alex.data.datimport;

import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class TableValues extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -102597615213102612L;
	
	public final static int STRING_TYPE = 0;
    public final static int NUMERIC_TYPE = 1;
    public final static String[] columnNames = {"列名", "类型"};
    
    
    public Object[][] values = null;
       
    public TableValues(Map<Integer, String> MissColumnIndToChnNameMap){
    	int row = 0;
    	
    	int size = MissColumnIndToChnNameMap.size();
    	
    	values = new Object[size][2];
    	
    	for (Map.Entry<Integer, String> entry : MissColumnIndToChnNameMap.entrySet()) {
    		String columnName = entry.getValue();
    		
    		values[row][0] = columnName;
    		
    		values[row][1] = true; 
    		
    		row++;
    	}
    	
    }

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return values[0].length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return values.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return values[rowIndex][columnIndex];
	}
	
	public void setValueAt(Object value, int row, int column){
        values[row][column]= value;
    }
	
	public boolean isCellEditable(int row, int column){  
		   return true;  
	}
	
	public String getColumnName(int column){
        return columnNames[column];
    }
	
}
