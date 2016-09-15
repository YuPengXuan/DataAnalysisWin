package com.xn.alex.data.c45;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;

import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.GetDistinctNumberOfColumnTask;
import com.xn.alex.data.database.GetDistinctValueTask;
import com.xn.alex.data.database.GetNumericColRangeTask;
import com.xn.alex.data.threadpool.IThreadPool;
import com.xn.alex.data.threadpool.ThreadPoolImpl;
import com.xn.alex.data.window.MainWindow;
import com.xn.alex.data.worker.WorkerFactory;

public class DecisionTree {
	
	private Vector<String> columnNameVec = null;
	
	private IThreadPool threadpool = null;
	
	private Map<String, Integer> resultMap = new ConcurrentHashMap<String, Integer>();;
	
	private Map<String, List<String>> rangeValueMap = new ConcurrentHashMap<String, List<String>>() ;
	
	public void createDecisionTree(String argumentName, int treeWidth, int treeDeepth){
		createThreadPool();
		
		if(false == setUneededHandleColumn(argumentName, treeWidth)){
			JOptionPane.showMessageDialog(null,"创建树失败","错误信息",JOptionPane.ERROR_MESSAGE);
			return;
		}

		createTree();
	}
	
	private void createThreadPool(){
		threadpool = new ThreadPoolImpl(10, 10);
	}
	
		
	private boolean setUneededHandleColumn(String argumentName, int treeWidth){
		
		columnNameVec = MainWindow.getJtableColumnVec();
		
		int columnSize = columnNameVec.size();		
				
		long milsecond =  System.currentTimeMillis();
		
		String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());
		
		String tableName = MainWindow.fileNameToTableMap.get(fileName);	
		
		for(int i=0;i<columnSize;i++){
			String columnName = columnNameVec.get(i);
															
			String databaseName = ConfigParser.chnToEnColumnName.get(columnName);
			
			if(columnName.equals(argumentName)){
                resultMap.put(databaseName, 0);
                continue;
			}
			
			GetDistinctNumberOfColumnTask task = new GetDistinctNumberOfColumnTask();
			
			task.setColumnName(databaseName);
			
			task.setTableName(tableName);
			
			task.setKeyId(databaseName);
			
			task.setResultMap(resultMap);
			
			threadpool.submitTask(WorkerFactory.getOneTaskWorker(task));
		}
		
		while(true){
			try {
				Thread.sleep(2000);
				if(columnSize == resultMap.size()){
					break;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		milsecond = (System.currentTimeMillis() - milsecond)/1000 ;	
		System.out.println("sql search time:" + milsecond);
		
		milsecond =  System.currentTimeMillis();
		System.out.println("begin to get distinct");
		
		int rangeValueMapSize = 0;
				
		for(Map.Entry<String, Integer> entry:resultMap.entrySet()){
			//System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());	
			//0 argument column
			//-1 invalid column
			//-2 select fail column
			if(entry.getValue() == -2){
				System.out.print("查询数据库失败");
				return false;
			}
			
			ConfigElement element = ConfigParser.columnInfoMap.get(entry.getKey());
			
			if(entry.getValue() > treeWidth && element.mValueType.contains("VARCHAR") ){								
				resultMap.put(entry.getKey(), -1);
			}
			else if(entry.getValue() > treeWidth && ! element.mValueType.contains("VARCHAR")){
				getRangeForNumericColumn(entry.getKey(), treeWidth, tableName);
				rangeValueMapSize++;
			}
			else{
				
				getDistinctValueForColumn(entry.getKey(), treeWidth, tableName);
				rangeValueMapSize++;
			}

		}
		
		while(true){
			try {
				Thread.sleep(2000);
				if(rangeValueMapSize == rangeValueMap.size()){
					break;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
				
		milsecond = (System.currentTimeMillis() - milsecond)/1000;
		for(Map.Entry<String, List<String>> entry:rangeValueMap.entrySet()){
			System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());			
		}
		System.out.println("end to get distinct:" + milsecond);
		return true;
	}
	
	private void getRangeForNumericColumn(String databaseColName, int treeWidth, String tableName){
		GetNumericColRangeTask task = new GetNumericColRangeTask();
		task.setColumnName(databaseColName);
		task.setRangeValueMap(rangeValueMap);
		task.setSplitNum(treeWidth);
		task.setTableName(tableName);		
		threadpool.submitTask(WorkerFactory.getOneTaskWorker(task));
		
	}
	
	private void getDistinctValueForColumn(String databaseColName, int treeWidth, String tableName){
		GetDistinctValueTask task = new GetDistinctValueTask();
		task.setColumnName(databaseColName);
		task.setRangeValueMap(rangeValueMap);
		task.setTableName(tableName);
		threadpool.submitTask(WorkerFactory.getOneTaskWorker(task));		
	}
	
	private void createTree(){
		
	}

}
