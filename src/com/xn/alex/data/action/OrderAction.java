package com.xn.alex.data.action;

import java.sql.ResultSet;
import java.util.Vector;

import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.datimport.DataExport;
import com.xn.alex.data.window.MainWindow;

public class OrderAction {

	public static OrderAction orderActionHandler = null;
	
	private String orderCol = null;
	
	private boolean isAsc = false;
	
	public OrderAction() {
		// TODO Auto-generated constructor stub
	}
	 
	public static OrderAction Instance(){
		
		if(null == orderActionHandler){
			orderActionHandler = new OrderAction();
			}
		 
		return orderActionHandler;		 
	 }
	
	 public void takeAction(boolean isAsc){
		 
//		 TextAreaOutPutControl.Instance().refresh("正在排序...");
		 
			String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());			
			
			String tableName = MainWindow.fileNameToTableMap.get(fileName);
			
			Vector<String> colName = MainWindow.Instance().getSelectedEnColumnVec();
			
			this.setAsc(isAsc);
			
			if(colName.size() == 1){
				try {	
					
				this.setOrderCol(colName.get(0));
				
				DatabaseAction handler = DatabaseAction.Instance();				
				
				ResultSet rs = handler.getOrderResult(tableName,colName.get(0), isAsc());
				
				MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);
				
				DataExport dataExport = new DataExport(fileName, MainWindow.Instance().getTable());
				
				dataExport.setM_rs(rs);
				
				dataExport.run();
				
			    MainWindow.setCurrentAct(CURRENT_ACTION.NONE);	
			    
			    handler.closeCurrentConnection();
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				
				System.out.println("请选择一个排序变量");
				
				return;
			}
									
			 
	 }

	public String getOrderCol() {
		return orderCol;
	}

	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
}
