package com.xn.alex.data.c45;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;

import javax.swing.JOptionPane;

import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.GetDistinctNumberOfColumnTask;
import com.xn.alex.data.database.GetDistinctValueTask;
import com.xn.alex.data.database.GetNumericColRangeTask;
import com.xn.alex.data.graphics.TreeDataSheet;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;
import com.xn.alex.data.threadpool.IThreadPool;
import com.xn.alex.data.threadpool.ThreadPoolImpl;
import com.xn.alex.data.window.MainWindow;
import com.xn.alex.data.worker.WorkerFactory;

public class DecisionTree {
	
	private Vector<String> columnNameVec = null;
	
	private IThreadPool threadpool = null;
	
	private Map<String, Integer> resultMap = new ConcurrentHashMap<String, Integer>();;
	
	private Map<String, List<String>> rangeValueMap = new ConcurrentHashMap<String, List<String>>();
	
	private Map<Integer, treeNodeResultObj> nodeNumToTreeNodeMap = new ConcurrentHashMap<Integer, treeNodeResultObj>();
	
	private ReadWriteLock myLock; 
	
	private int nodeNum = 0;
			
	public int getNodeNum() {
		myLock.readLock().lock();
		int val = nodeNum;
		myLock.readLock().unlock();
		return val;
	}

	public void setNodeNum(int nodeNum) {
		myLock.writeLock().lock();
		this.nodeNum = nodeNum;
		myLock.writeLock().unlock();
	}
	
	public int getNewNodeNum(){
		int nodeNo = getNodeNum();
		setNodeNum(nodeNum+1);
		return nodeNo;		
	}

	public void createDecisionTree(String argumentName, int treeWidth, int treeDeepth){
		createThreadPool();
		
		if(false == setUneededHandleColumn(argumentName, treeWidth)){
			JOptionPane.showMessageDialog(null,"创建树失败","错误信息",JOptionPane.ERROR_MESSAGE);
			return;
		}

		createTree(argumentName, treeDeepth);
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
		
		removeUnneededDataInMap();
				
		milsecond = (System.currentTimeMillis() - milsecond)/1000;
		for(Map.Entry<String, List<String>> entry:rangeValueMap.entrySet()){
			System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());			
		}
		System.out.println("end to get distinct:" + milsecond);
		return true;
	}
	
	private void removeUnneededDataInMap(){
		Iterator<Map.Entry<String,Integer>> it = resultMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Integer> entry=it.next();
			String key = entry.getKey();
			int val = entry.getValue();
			if(val == -1 || val == -2){
				it.remove();
			}
		}
		
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
	
	private boolean calculateGainRatio(Map<String, Double> gainRatioMap, String argumentName, StringBuffer conditionBuf, Set<String> passedCol){
		    
        String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());
		
		String tableName = MainWindow.fileNameToTableMap.get(fileName);	
			
			for(Map.Entry<String, Integer> entry : resultMap.entrySet()){
			    String columnName = entry.getKey();
			    
			    if(argumentName.equals(columnName)){
			    	continue;
			    }
			    
			    if(passedCol.contains(columnName)){
			    	gainRatioMap.put(columnName, (double)(-1));
			    	continue;
			    }
			    
			    CalculateGainRatioThread threadTask = new CalculateGainRatioThread();
			    threadTask.setArgumentName(argumentName);
			    threadTask.setCondition(conditionBuf.toString());
			    threadTask.setDatabaseColName(columnName);
			    threadTask.setRangeValueMap(rangeValueMap);
			    threadTask.setTableName(tableName);
			    threadTask.setGainRatioMap(gainRatioMap);
			    threadpool.submitTask(threadTask);			
			}
			
			while(true){
				try {
					Thread.sleep(2000);
					if(resultMap.size() == gainRatioMap.size() + 1){
						break;
					}
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();					
					return false;
				}
			}
			
			return true;
		
	}
	
	private void initTreeNode(){
		GenerateTreeByLeaf genHandler = new GenerateTreeByLeaf();
		
		 Vector<treeNodeResultObj> treeNodeVec = GenerateTreeByLeaf.getTreeLeafNodeVec();
		 
		 treeNodeVec.clear();
		 
		 Vector<Vector<treeNodeResultObj>> treeNodeByLevelVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();
		 
		 treeNodeByLevelVec.clear();		 
		 
	}
	
	private void buildTreeByLoop(treeNodeResultObj parent, int level, int treeDeepth, String argumentName, String condition, Set<String> passedColSet){
		
		if(level >= treeDeepth){
			return;
		}
		
		if(null == passedColSet){
			passedColSet = new CopyOnWriteArraySet<String>();
		}
		
        StringBuffer conditionBuf = new StringBuffer(condition);
		
		Map<String, Double> gainRationMap = new ConcurrentHashMap<String, Double>();
		
		if(false == calculateGainRatio(gainRationMap, argumentName, conditionBuf, passedColSet)){
			System.out.println("建立树失败!");
			return;
		}
		
		 String maxGainRatioLine = getMaxGainRatio(gainRationMap, parent);
		 
		 if(null == maxGainRatioLine){
			 //this is leaf node
			 addToLeafNodeVec(parent);
			 return;
		 }
		 
		 List<String> valueRangeList = rangeValueMap.get(maxGainRatioLine);
		 
		 treeNodeResultObj currentNode = buildTree(parent, maxGainRatioLine, level, conditionBuf);
		 
		 passedColSet.add(maxGainRatioLine);
		 
		 level++;
		 
		 for(int i=0;i<valueRangeList.size();i++){
			 
			 conditionBuf = buildCondition(maxGainRatioLine, conditionBuf, i);
			 
			 buildTreeByLoop(currentNode, level, treeDeepth, argumentName, conditionBuf.toString(), passedColSet); 
			 
		 }
		 
		 passedColSet.remove(maxGainRatioLine); 		
	}
	
	private void addToLeafNodeVec(treeNodeResultObj treeNode){
		
        treeNode.isLeaf = true;
				
	}
	
	private void createTree(String argumentName, int treeDeepth){
		      		
		StringBuffer conditionBuf = new StringBuffer("");
							
		initTreeNode();
		
		int level = 0;
		
		String databaseName = ConfigParser.chnToEnColumnName.get(argumentName);
		
		buildTreeByLoop(null, level, treeDeepth, databaseName, conditionBuf.toString(), null);
		
		treeNodeResultObj resultTree = GenerateTreeByLeaf.getResultNode();
		
        TreeDataSheet.Instance().setPreTitle("C4.5决策树");
		
		TreeDataSheet.Instance().show(resultTree);
					
	}
	
	private StringBuffer buildCondition(String currentCol,StringBuffer conditionBuf, int index){
        
		if("".equals(conditionBuf.toString())){
			conditionBuf.append("where ");
		}
		else{
			conditionBuf.append(" and ");
		}
		
        ConfigElement element = ConfigParser.columnInfoMap.get(currentCol);
		
		boolean isRangeCondition = false;
		boolean isNumeric = false;
		
		List<String> rangeValueMapList = rangeValueMap.get(currentCol);
		
		if(false == element.mValueType.contains("VARCHAR")){
			isNumeric = true;	
			if(rangeValueMapList.size() > 5 ){
			    isRangeCondition = true;
			}
		}
		
		if(false == isRangeCondition){
			conditionBuf.append(currentCol);				
			conditionBuf.append("=");
            if(false == isNumeric){
            	conditionBuf.append("'");
			}
            conditionBuf.append(rangeValueMapList.get(index));
			if(false == isNumeric){
				conditionBuf.append("'");
			}
		}
		else{
			String tmpVal_1 = rangeValueMapList.get(index);
			String tmpVal_2 = rangeValueMapList.get(index+1);
			conditionBuf.append(" and ");
			conditionBuf.append(currentCol);
			conditionBuf.append(">=");
			conditionBuf.append(tmpVal_1);
			conditionBuf.append(" and ");
			conditionBuf.append(currentCol);
			if(index == rangeValueMapList.size()-2 ){
				conditionBuf.append("<=");
			}
			else{
				conditionBuf.append("<");
			}
			conditionBuf.append(tmpVal_2);
		}
				
		return conditionBuf;
	}
	
	private treeNodeResultObj buildTree(treeNodeResultObj parent, String maxGainRatioLine, int level, StringBuffer conditionBuf){
		
		if(level == 0){
			//it's root node
			return buildRootNode(maxGainRatioLine, level);		
		}
		
	    return buildOtherNode(parent, maxGainRatioLine, level, conditionBuf);
	}
	
	private treeNodeResultObj buildOtherNode(treeNodeResultObj parent, String nodeName, int level, StringBuffer conditionBuf){
		if(level == 0){
			return null;
		}		
		
		Vector<Vector<treeNodeResultObj>> treeNodeByLevelVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();
		
		Vector<treeNodeResultObj> treeNodeVec = treeNodeByLevelVec.get(level);
		
		if(null == treeNodeVec){
			treeNodeVec = new Vector<treeNodeResultObj>();
		}
		
		treeNodeResultObj newNode = new treeNodeResultObj();
		
		newNode.nodeName = nodeName;
		
		newNode.objectLevel = level;
				
		newNode.nodeNumber = getNewNodeNum();
		
		newNode.condition = conditionBuf.toString();
		
		newNode.sql = conditionBuf.toString();
		
		parent.childNodeVec.add(newNode);
		
		treeNodeVec.add(newNode);
		
		treeNodeByLevelVec.add(treeNodeVec);
					
		return newNode;		
	}
	
	private treeNodeResultObj buildRootNode(String nodeName, int level){
		treeNodeResultObj rootObj = new treeNodeResultObj();
		
		rootObj.nodeName = nodeName;
		
		int nodeNumber = getNodeNum();
		
		rootObj.nodeNumber = nodeNumber;
		
		nodeNumber++;
		
		setNodeNum(nodeNumber);
		
		rootObj.objectLevel = 0;
		
		Vector<treeNodeResultObj> rootLevelVec = new Vector<treeNodeResultObj>();
		
		rootLevelVec.add(rootObj);		
		//level 0 only has 1 node: root node
		Vector<Vector<treeNodeResultObj>> treeNodeByLevelVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();
		
		treeNodeByLevelVec.add(rootLevelVec);
		
		return rootObj;
	}
	
	private String getMaxGainRatio(Map<String, Double> gainRationMap, treeNodeResultObj parentNode){
		double maxGainRatio = -1;
		
		String columName = null;
		
		for(Map.Entry<String, Double> entry : gainRationMap.entrySet()){
			BigDecimal val1 = new BigDecimal(maxGainRatio);
			BigDecimal val2 = new BigDecimal(entry.getValue());
			columName = entry.getKey();
			
			if(val2.compareTo(new BigDecimal(0)) == 0){
				if(null != parentNode && columName.equals(parentNode.nodeName))
				    return null;
			}
						
			if(val2.compareTo(val1) > 0){
				maxGainRatio = entry.getValue();
			}
		}
		
		return columName;
	}
}
