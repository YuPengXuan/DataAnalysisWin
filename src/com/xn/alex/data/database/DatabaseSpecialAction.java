package com.xn.alex.data.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.xn.alex.data.common.ConfigParser.DataColumnInfo;

public class DatabaseSpecialAction {
			
	private static DatabaseSpecialAction databaseSpecialActionHandler = null;
	
	private DatabaseSpecialAction(){
		
	}
	
	public static DatabaseSpecialAction Instance(){
		
		if(null == databaseSpecialActionHandler){
			
			databaseSpecialActionHandler = new DatabaseSpecialAction();
		}
		
		return databaseSpecialActionHandler;		
	}
	
   public boolean createAvailableIdTable(){
		
		List<DataColumnInfo> columnInfo = new ArrayList<DataColumnInfo>();
		
		String valueType = "INT";
		
		columnInfo.add(new DataColumnInfo(DatabaseConstant.AVAILABLE_ID, valueType));
		
		columnInfo.add(new DataColumnInfo(DatabaseConstant.MAX_ID, valueType));		
		
		String primaryKey = DatabaseConstant.AVAILABLE_ID;
					
		DatabaseAction.Instance().createTable(DatabaseConstant.AVAILABLE_TABLEID_TABLE, columnInfo, primaryKey);
		
		Vector<String> columnVec = new Vector<String>();
		
		columnVec.add(DatabaseConstant.AVAILABLE_ID);
		
		columnVec.add(DatabaseConstant.MAX_ID);
		
		Vector<Vector<String>> commitValVec = new Vector<Vector<String>>();
		
		Vector<String> valueVec = new Vector<String>();
		
		valueVec.add("1");
		
		valueVec.add("1");
		
		commitValVec.add(valueVec);
		
		boolean result = DatabaseAction.Instance().insertTable(DatabaseConstant.AVAILABLE_TABLEID_TABLE, columnVec, commitValVec);
		
		return result;		
		
	}
   
   public int getAvailableTableId(){   
	    try {
	    	ResultSet rs = DatabaseAction.Instance().getAllResult(DatabaseConstant.AVAILABLE_TABLEID_TABLE);
	    	
			rs.last();
			
			int id = Integer.parseInt(rs.getString(DatabaseConstant.AVAILABLE_ID));
			
			DatabaseAction.Instance().closeCurrentConnection();
			
			updateAvailableTableId(id);
			
			return id;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			DatabaseAction.Instance().closeCurrentConnection();
		}
	    
		return 0;
	}
   
   public boolean updateAvailableTableId(int id){
	  		   
       try {
			   
		    int i=0;
		    
		    String tableName = null;
		    
			while(true){
				
				 if(i == id){
			         i++;
				     continue;
				 }
				   
		        tableName = DatabaseConstant.DATABASE_TABLE_PREFIX + i;
		        
		        if(false == DatabaseAction.Instance().isTableExist(tableName)){
		        	
		        	break;		        	
		        }			    
			    
			    i++;
		    }
			
			ResultSet rsNew = DatabaseAction.Instance().getAllResult(DatabaseConstant.AVAILABLE_TABLEID_TABLE);
	    	
			rsNew.last();
			
			int maxId = Integer.parseInt(rsNew.getString(DatabaseConstant.MAX_ID));
			
			int availableId = i;
			
			int newMaxid = maxId;
			
		    if(i > maxId){
		    	newMaxid = i;
		    }
		    
		    Vector<String> columnNameVec = new Vector<String>();
		    		    		    
		    columnNameVec.add(DatabaseConstant.AVAILABLE_ID);
		    		    		    
		    columnNameVec.add(DatabaseConstant.MAX_ID);
		    
		    Vector<String> valVec = new Vector<String>();
		    
		    valVec.add(Integer.toString(availableId));
		    
		    valVec.add(Integer.toString(newMaxid));
		    
		    Vector<Vector<String>> commitVec = new Vector<Vector<String>>();
		    
		    commitVec.add(valVec);
		    
		    Vector<String> conditionVec = new Vector<String>();
		    
		    conditionVec.add("maxId="+ maxId);
		    
		    DatabaseAction.Instance().updateTable(DatabaseConstant.AVAILABLE_TABLEID_TABLE, columnNameVec, commitVec, conditionVec);
		    
			    
		   } catch (SQLException e) {
			    // TODO Auto-generated catch block
			   DatabaseAction.Instance().closeCurrentConnection();
			   
			   return false;
		   }
       
       return true;
	}
   
   public boolean createFileToTableTable(){
	   
	   try{
		   
		   String tableName = DatabaseConstant.FILE_TO_TABLE;
		   
		   List<DataColumnInfo> columnInfo = new ArrayList<DataColumnInfo>();
			
		   columnInfo.add(new DataColumnInfo(DatabaseConstant.FILE_FULL_PATH, "VARCHAR(255)"));
			
		   columnInfo.add(new DataColumnInfo(DatabaseConstant.TABLE_NAME, "VARCHAR(100)"));		
			
		   
		   String primaryKey = DatabaseConstant.FILE_FULL_PATH;
		   
		   if(false ==  DatabaseAction.Instance().createTable(tableName, columnInfo, primaryKey)){
			   return false;
		   }
		   
		   return true;
	   }
	   catch(Exception e){
		   
	   }
	   
	   return true;
   }
   
   public boolean insertIntoFileToTableTable(String fileName, String targetTable){
	   
	   String tableName = DatabaseConstant.FILE_TO_TABLE;
	   
	   Vector<String> columnNameVec = new Vector<String>();
	   
	   columnNameVec.add(DatabaseConstant.FILE_FULL_PATH);
	   
	   columnNameVec.add(DatabaseConstant.TABLE_NAME);
	   
	   Vector<Vector<String>>commitValVec = new Vector<Vector<String>>();
	   
	   Vector<String> valVec = new Vector<String>();
	  
	   String newFileName = fileName.replaceAll("\\\\", "\\\\\\\\");
	   
	   valVec.add("'"+newFileName+"'");
	   
	   valVec.add("'"+targetTable+"'");
	   
	   commitValVec.add(valVec);
	   
	   if(false == DatabaseAction.Instance().insertTable(tableName, columnNameVec, commitValVec)){
		   
		   return false;
		   
	   }
	   
	   return true;
	   
   }
   
   public boolean deleteFromFileToTableTable(String fileName){
	   
	   fileName = fileName.replaceAll("\\\\", "\\\\\\\\");
	   
	   String tableName = DatabaseConstant.FILE_TO_TABLE;
	   
	   String condition = DatabaseConstant.FILE_FULL_PATH + "='" + fileName + "'";
	   
	   if(false == DatabaseAction.Instance().deleteData(tableName, condition)){
		   
		   return false;
		   
	   }
	   
	   return true;
	   
   }
	   
}

