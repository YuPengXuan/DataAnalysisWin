package com.xn.alex.data.datimport;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.xn.alex.data.action.OrderAction;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.window.MainWindow;

public class DataExport {
	
	private String m_fullFileName = null;
	
	private JTable m_jtable = null;
	
	private ResultSet m_rs = null;
	
	public ResultSet getM_rs() {
		return m_rs;
	}

	public void setM_rs(ResultSet m_rs) {
		this.m_rs = m_rs;
	}

	public DataExport(String fullFileName, JTable table){
		
		m_fullFileName = fullFileName;
		
		m_jtable = table;
	}
	
	public void run(){
		
		try{
		
		    String tableName = MainWindow.fileNameToTableMap.get(m_fullFileName);
		
		    DefaultTableModel dtm = (DefaultTableModel) m_jtable.getModel();
		
		    ResultSet rs;
		    
		    String orderCol = OrderAction.Instance().getOrderCol();
		    
		    if(null == m_rs){
		    
                if(orderCol == null || "".equals(orderCol)){
		    	
            	    rs = DatabaseAction.Instance().getAllResult(tableName);
		    	
		        }else{
		    	
                    boolean isAsc = OrderAction.Instance().isAsc();
		    	
		    	    rs = DatabaseAction.Instance().getOrderResult(tableName, orderCol, isAsc);
		    
		        }
		    }
		    else{
		    	rs = m_rs;
		    }
		    
		    ResultSetMetaData data = rs.getMetaData();
		    
		    Vector<String> columnInfoVec = MainWindow.getJtableColumnVec();	
		    
		    Map<String, Integer> chnColNameToIndexMap = new ConcurrentHashMap<String, Integer>();
		    
		    columnInfoVec.clear();
		    
		    for(int i=1; i<=data.getColumnCount(); i++){
		    	
		    	String databasecolumnName = data.getColumnName(i);
		    	
		    	String chnColumnName = ConfigParser.columnInfoMap.get(databasecolumnName).mExcelColumnName;
		    	
		    	chnColNameToIndexMap.put(chnColumnName,i);
		    	
		    	columnInfoVec.add(chnColumnName);
		    	
		    }
		    
		    Vector<Integer> orignIndVec = new Vector<Integer>();
		    
		    SortColumnOrderByConfigColumnOrder(columnInfoVec, orignIndVec);
		    
		    Vector<Vector<String>> dataInfoVec = MainWindow.getJtableValueVec();
		    
		    dataInfoVec.clear();
		
		    while(rs.next()){
		    	
		    	Vector<String> valVec = new Vector<String>();
		    	
		    	for(int j=0;j<data.getColumnCount();j++){
		    		
		    	    int getInd = orignIndVec.get(j);
		    		
		    		String value = rs.getString(getInd+1);
		    		
		    		valVec.add(value);
		    		
		    	}
		    	
		    	dataInfoVec.add(valVec);
			
		    }
		    
		    dtm.setDataVector(dataInfoVec, columnInfoVec);
		    
		    dtm.fireTableStructureChanged();
		    
		    dtm.fireTableDataChanged();
		    
		    DatabaseAction.Instance().closeCurrentConnection();		
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			System.out.println("¶ÁÈ¡Êý¾ÝÊ§°Ü£¡");
			
		}
		
	}
	
	public void showDataInJTable(){
		
		Vector<Vector<String>> dataInfoVec = MainWindow.getJtableValueVec();
		
		Vector<String> columnInfoVec = MainWindow.getJtableColumnVec();

		DefaultTableModel dtm = (DefaultTableModel) m_jtable.getModel();
		
		dtm.setDataVector(dataInfoVec, columnInfoVec);
	    
	    dtm.fireTableStructureChanged();
	    
	    dtm.fireTableDataChanged();
		
	}
	
	private void SortColumnOrderByConfigColumnOrder(Vector<String> columnVec, Vector<Integer> orignIndVec){
		
		List<CompareObject> compareVec = new Vector<CompareObject>();
		
		int columSize = columnVec.size();
		
		for(int i=0;i<columSize;i++){
			
		    String columnName = columnVec.get(i);
		    
		    int columnIndex = ConfigParser.columnVecInConfigOrder.indexOf(columnName);
		    
		    if(-1 == columnIndex){
		    	return;
		    }
		    
		    CompareObject obj = new CompareObject();
		    
		    obj.objName = columnName;
		    
		    obj.compareVlue = columnIndex;
		    
		    obj.origVal = i;
		    
		    compareVec.add(obj);
			
		}
		
		Comparator<Object> ct = new CompareObject();
		
		Collections.sort(compareVec, ct);
		
		columnVec.clear();
		
		for(int i=0;i<compareVec.size();i++){
			
			orignIndVec.add(compareVec.get(i).origVal);
		
		    columnVec.add(compareVec.get(i).objName);
		
		}
		
	}
	

}
