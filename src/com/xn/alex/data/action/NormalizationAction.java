package com.xn.alex.data.action;

import java.sql.ResultSet;
import java.util.Vector;

import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.datimport.DataExport;
import com.xn.alex.data.window.MainWindow;

public class NormalizationAction extends WindowAction {
	
	private static NormalizationAction normalizationActionHandler = null;
	
	 private NormalizationAction(){
		 
	 }
	 
	 public static NormalizationAction Instance(){
		 
		 if(null == normalizationActionHandler){
			 normalizationActionHandler = new NormalizationAction();
		 }
		 
		 return normalizationActionHandler;		 
	 }
	
	 public void takeAction(){
		 
		String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());
		
		String tableName = MainWindow.fileNameToTableMap.get(fileName);
		 
		updateDatabaseData(tableName);
		 
		MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);
		 
		DataExport dataExport = new DataExport(fileName, MainWindow.Instance().getTable());
			 
		dataExport.run();
			 
	    MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
		 

	 }
	 
	 private void updateDatabaseData(String tableName){
		 
		 try{
			    Vector<String> colName = MainWindow.Instance().getSelectedEnColumnVec();
			    
			    if(null ==colName || 0 == colName.size()){
			    	return;
			    }
			   				
			    
				Vector<String> valueVec = new Vector<String>();
				
				for(int i = 0; i < colName.size(); i++){
					
					ConfigElement element = ConfigParser.columnInfoMap.get(colName.get(i));
					
					if(element.mValueType.contains("VARCHAR")){
						
						System.out.println(colName.get(i) + " is a varchart column which can not be normalized.");
						
						continue;
					}
					
					ResultSet rs = DatabaseAction.Instance().getOneResult(tableName, "MAX(" + colName.get(i) + ")", colName.get(i) + " is not null");

					if(rs.next()){
						valueVec.add(i, colName.get(i) + "/" + rs.getString(1));
					}
					
				}
				
				String condition = colName.get(0);
				
				condition += " is not null";
				
				DatabaseAction.Instance().updateColumn(tableName, colName, valueVec, condition);
				
				
			}
			catch(Exception e){      
				e.printStackTrace();
			}
	 }

}
