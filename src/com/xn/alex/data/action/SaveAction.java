package com.xn.alex.data.action;

import java.util.Vector;

import javax.swing.JOptionPane;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.window.MainWindow;

public class SaveAction extends WindowAction {
	
	 private static SaveAction saveActionHandler = null;
		
	 private SaveAction(){
		 
	 }
	 
	 public static SaveAction Instance(){
		 
		 if(null == saveActionHandler){
			 saveActionHandler = new SaveAction();
		 }
		 
		 return saveActionHandler;		 
	 }
	
	 public void takeAction(){
		 
		 saveSelectedFile();

	 }
	 
	 private void saveSelectedFile(){
		   
	    	if(0 == MainWindow.Instance().getUpdateObjList().size()){
	    		
	    		return;
	    		
	    	}
	    	
	        String outPutMsg = "";
	    		   		
	        outPutMsg = "文件 已保存!";
	    	
	        if(false == saveToDatabase()){
	        	
	        	JOptionPane.showMessageDialog(null,"保存数据失败","系统信息",JOptionPane.INFORMATION_MESSAGE);
	           
	        }

	    	JOptionPane.showMessageDialog(null,outPutMsg,"系统信息",JOptionPane.INFORMATION_MESSAGE);
	    	
	    	MainWindow.Instance().getUpdateObjList().clear();
	    	
	    }
	    
	    private boolean saveToDatabase(){
	    	
	    	Vector<String> columnNameVec = new Vector<String>();
	    	
	    	Vector<Vector<String>> commitValVec = new Vector<Vector<String>>();
			
			Vector<String> valVec = new Vector<String>();
			
			Vector<String> conditionVec = new Vector<String>();
			
			String primaryKey = "customerID";
			
			int pos=0;
			
			int i=0;
			
			for(i=0; i<MainWindow.getJtableColumnVec().size();i++){
				
				String columnChnName = MainWindow.getJtableColumnVec().get(i);
				
				String columnEnName = ConfigParser.chnToEnColumnName.get(columnChnName);
				
				if(primaryKey.equals(columnEnName)){
					pos = i;
					
					break;
				}
				
			}
			
			if(i == MainWindow.getJtableColumnVec().size()){
				
				return false;
			}
	    	
	    	for(int j=0;j<MainWindow.Instance().getUpdateObjList().size();j++){
	    		
	    		columnNameVec.clear();
	    		
	    		commitValVec.clear();
	    		
	    		valVec.clear();
	    		
	    		String tableName = MainWindow.Instance().getUpdateObjList().get(j).tableName;    		
	    		
	    		columnNameVec.add(MainWindow.Instance().getUpdateObjList().get(j).columnEnName);
	    		
	    		valVec.add(MainWindow.Instance().getUpdateObjList().get(j).newValue);
	    		
	    		commitValVec.add(valVec);
	    		
	    		String primaryKeyVal = MainWindow.getJtableValueVec().get(MainWindow.Instance().getUpdateObjList().get(j).rowNumIndex).get(pos);
	    		
	    		String condition = primaryKey+"="+"'"+primaryKeyVal+"'";
	    		
	    		conditionVec.add(condition);
	    		   		    		    		
	    		if(false == DatabaseAction.Instance().updateTable(tableName, columnNameVec, commitValVec, conditionVec)){
	    			
	    			return false;
	    		}
	    		
	    	}    	
	    	
	    	return true;
	    }

}
