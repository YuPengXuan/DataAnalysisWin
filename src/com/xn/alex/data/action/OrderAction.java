package com.xn.alex.data.action;

import java.util.Vector;

import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
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
			
			Vector<String> colName = MainWindow.Instance().getSelectedEnColumnVec();
			
			if(colName.size() == 1){
				this.setOrderCol(colName.get(0));
			}else{
				
				System.out.println("请选择一个排序变量");
				
				return;
			}
			
			this.setAsc(isAsc);
			
			MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);
			
			DataExport dataExport = new DataExport(fileName, MainWindow.Instance().getTable());
			
			dataExport.run();
			
		    MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
			 
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
