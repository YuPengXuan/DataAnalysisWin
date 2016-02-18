package com.xn.alex.data.action;

import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.datimport.DataExport;
import com.xn.alex.data.window.MainWindow;

public class DeleteAction {

	public static DeleteAction deleteActionHandler = null;
	
	public DeleteAction() {
		// TODO Auto-generated constructor stub
	}
	 
	public static DeleteAction Instance(){
		
		if(null == deleteActionHandler){
			deleteActionHandler = new DeleteAction();
			}
		 
		return deleteActionHandler;		 
	 }
	
	 public void takeAction(){
		 
		 
			String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());
			
			String tableName = MainWindow.fileNameToTableMap.get(fileName);
			
			String pkColName = "customerID";
			
			int selectRows[] = MainWindow.Instance().getSelectedRowInds();
			
			for(int i = 0; i<selectRows.length; i++){

				String pkValue = MainWindow.getJtableValueVec().get(selectRows[i]).get(0);
				
				updateDatabaseData(tableName,pkColName,pkValue);
			}
			 
			MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);
			 
//			MainWindow.Instance().getTable().removeRowSelectionInterval(selectRows[0], selectRows[selectRows.length-1]);
			DataExport dataExport = new DataExport(fileName, MainWindow.Instance().getTable());
				 
			dataExport.run();
				 
		    MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
			 


	 }

	private void updateDatabaseData(String tableName,String pkColName,String pkValue) {
		// TODO Auto-generated method stub
		try{		   				
			String condition = pkColName + " = " + pkValue;
			
			DatabaseAction.Instance().deleteData(tableName, condition);
			
			System.out.println("客户编号为" + pkValue + "的用户记录已删除");
			
			
		}
		catch(Exception e){      
			e.printStackTrace();
		}
	}
}
