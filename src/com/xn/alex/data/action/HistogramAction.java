package com.xn.alex.data.action;

import java.sql.ResultSet;
import java.util.Vector;

import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.datimport.DataExport;
import com.xn.alex.data.graphics.CategoryDataSheet;
import com.xn.alex.data.resultobj.algorithmResultObj1;
import com.xn.alex.data.window.MainWindow;

public class HistogramAction extends WindowAction {

	private static int classNum = 5;
	
	private static HistogramAction histogramActionIns = null;
	
	public HistogramAction() {
		// TODO Auto-generated constructor stub
	}
	
	public static HistogramAction Instance(){
		
		if(null == histogramActionIns){
			
			histogramActionIns = new HistogramAction();
		}
		return histogramActionIns;
		
	}
	
	public void takeAction(){
		 
		System.out.println("打开直方图，数据正在统计中...");
		
		String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());

		String tableName = MainWindow.fileNameToTableMap.get(fileName);

		if(false == execution(tableName)){
			System.out.println("打开直方图失败！");
			
			return;
		}
		 
		MainWindow.setCurrentAct(CURRENT_ACTION.JTABLE_EDIT);
		 
		DataExport dataExport = new DataExport(fileName, MainWindow.Instance().getTable());
			 
		dataExport.run();
			 
	    MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
		 

	 }
    public boolean execution(String tableName){
    	
		try{
			Vector<String> colChnName = MainWindow.Instance().getSelectedChnColumnVec();
			
		    Vector<String> colNameVec = MainWindow.Instance().getSelectedEnColumnVec();
		    
		    if(null == colChnName || null == colNameVec || colNameVec.size() == 0 || colChnName.size() == 0){
		    	System.out.println("请先选择需要的列！");
		    	
		    	return false;
		    }

			DatabaseAction db = DatabaseAction.Instance();

			Vector<algorithmResultObj1> dataVec = new Vector<algorithmResultObj1>();
			
			for(int j = 0; j<colNameVec.size(); j++){
				String colName = colNameVec.get(j);
				
				ConfigElement element = ConfigParser.columnInfoMap.get(colName);
				
				float max,min;
				
				String condition;
				
				if(element.mValueType.contains("VARCHAR")){
					
					ResultSet rs1 = db.getOneResult(tableName + " group by " + colName, "count(*)," + colName, null);
					
					while(rs1.next()){
						
						dataVec.add(new algorithmResultObj1(rs1.getString(2),rs1.getInt(1),colChnName.get(j)));
					}
				}else{
					
					ResultSet rs1 = db.getOneResult(tableName, "max(" + colName + "),min(" + colName + ")", colName + " is not null");

					if(rs1.next()){

						max = rs1.getFloat(1);
						
						min = rs1.getFloat(2);
						
						float gap = (max-min)/5;
						
						for(int i = 1; i <= classNum; i++){
							
							float lowLimit = min+gap*i;
							
							float upLimit = min+gap*(i+1);
							
							if(i != classNum){
								condition = colName + " >= " + lowLimit + " and " + colName + " < " + upLimit;
							}else{
								condition = colName + " >= " + lowLimit + " and " + colName + " <= " + upLimit;
							}
							
							ResultSet rs2 = db.getOneResult(tableName, "count(*)", condition);
							
							if(rs2.next()){

								String s = String .format("%.2f~%.2f",lowLimit,upLimit);
								
								dataVec.add(new algorithmResultObj1(s,rs2.getInt(1),colChnName.get(j)));
							}
						}
					}
					
				}
				
				CategoryDataSheet.Instance().show(dataVec,null);
			}
			
			
		}
		catch(Exception e){      
			e.printStackTrace();
			System.out.println("直方图打开错误！");
		}
		return true;
		
	}
//    
//    public static void main(String args[]){
//    	Vector<String> columnName = new Vector<String>();
//    	columnName.add("currentMOU");
//		GraphData.Instance().execution("table_1", columnName.get(0));
//		
//	}
}
