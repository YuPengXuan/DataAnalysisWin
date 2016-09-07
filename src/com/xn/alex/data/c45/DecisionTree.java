package com.xn.alex.data.c45;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.GetDistinctNumberOfColumnTask;
import com.xn.alex.data.database.SqlTask;
import com.xn.alex.data.threadpool.IThreadPool;
import com.xn.alex.data.threadpool.ThreadPoolImpl;
import com.xn.alex.data.window.MainWindow;
import com.xn.alex.data.worker.WorkerFactory;

public class DecisionTree {
	
	private Vector<String> columnNameVec = null;
	
	private IThreadPool threadpool = null;
	
	public void createDecisionTree(String argumentName, int treeWidth, int treeDeepth){
		createThreadPool();
		
		setUneededHandleColumn(argumentName);
	}
	
	private void createThreadPool(){
		threadpool = new ThreadPoolImpl(30, 10);
	}
	
	private boolean setUneededHandleColumn(String argumentName){
		
		columnNameVec = MainWindow.getJtableColumnVec();
		
		int columnSize = columnNameVec.size();
		
		Map<String, Integer> resultMap = new ConcurrentHashMap<String, Integer>();
		
		for(int i=0;i<columnSize;i++){
			String columnName = columnNameVec.get(i);
			
			String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());
			
			String tableName = MainWindow.fileNameToTableMap.get(fileName);	
			
			String databaseName = ConfigParser.chnToEnColumnName.get(columnName);
			
			GetDistinctNumberOfColumnTask task = new GetDistinctNumberOfColumnTask();
			
			task.setColumnName(databaseName);
			
			task.setTableName(tableName);
			
			task.setKeyId(databaseName);
			
			task.setResultMap(resultMap);
			
			threadpool.submitTask(WorkerFactory.getOneTaskWorker(task));
		}
		
		
        
		return true;
	}

}
