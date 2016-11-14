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
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JOptionPane;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.action.ImportRuleAction;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
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
import com.xn.alex.data.ui.IPropertyListener;
import com.xn.alex.data.ui.ProgressBar;
import com.xn.alex.data.window.MainWindow;
import com.xn.alex.data.worker.WorkerFactory;

public class DecisionTree {
	
	private Vector<String> columnNameVec = null;
	
	private IThreadPool threadpool = null;
	
	private Map<String, Integer> resultMap = new ConcurrentHashMap<String, Integer>();;
	
	private Map<String, List<String>> rangeValueMap = new ConcurrentHashMap<String, List<String>>();
	
	private ReadWriteLock myLock = new ReentrantReadWriteLock(false); 
	
	private int nodeNum = 0;
	
	private GenerateTreeByLeaf genHandler = null;	
	
	private int treeWidth = 0;
	
	private int treeDeepth = 0;
	
	private int denominator = 0;
	
	private int passedNodeNum = 0;
	
    private IPropertyListener propertyListener;
    
    private Map<String, List<String>> argumentResultUnderCondMap = new ConcurrentHashMap<String, List<String>>();
			
	public IPropertyListener getPropertyListener() {
		return propertyListener;
	}

	public void setPropertyListener(IPropertyListener propertyListener) {
		this.propertyListener = propertyListener;
	}

	public int getTreeDeepth() {
		return treeDeepth;
	}

	public void setTreeDeepth(int treeDeepth) {
		this.treeDeepth = treeDeepth;
	}

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
	
	public void setTreeWidth(int treeWidth){
		this.treeWidth = treeWidth;
	}
	
	public int getTreeWidth(){
		return treeWidth;
	}
	
	private void calculateDenominator(int treeWidth, int treeDeepth){
		denominator = 0;
		for(int i=treeDeepth-1;i>=0;i--){
			denominator += (int)Math.pow(treeWidth, i);
		}
	}

	public void createDecisionTree(final String argumentName, final int treeWidth, final int treeDeepth){
		   
		   setTreeWidth(treeWidth);
		   setTreeDeepth(treeDeepth);
		   calculateDenominator(treeWidth,treeDeepth);
		   
		  final ProgressBar progresBar = new ProgressBar(MainWindow.Instance(),"C4.5决策树算法"){

			/**
			 * 
			 */
			private static final long serialVersionUID = -426680975782762403L;
            
			@Override
			public void onRun() throws Exception {
				setPropertyListener(this);
				
				setProgress(0,"开始计算" );
				
				createThreadPool();
				setProgress(1,"数据预处理开始");
				
				if(false == setUneededHandleColumn(argumentName, treeWidth)){
					JOptionPane.showMessageDialog(null,"创建树失败","错误信息",JOptionPane.ERROR_MESSAGE);
					return;
				}
				setProgress(10,"数据预处理完成");
				setProgress(10,"C45节点计算");
				createTree(argumentName, treeDeepth);				
				setProgress(100,"建树完毕",500);
			}

			@Override
			public void onClose() {
				this.dispose();
			}

			@Override
			public void onException(Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
		
			
		};
		
		showC45Tree();
					
	}
	
	private void createThreadPool(){
		threadpool = new ThreadPoolImpl(10, 10);
	}
	
		
	private boolean setUneededHandleColumn(String argumentName, int treeWidth){		
		
		columnNameVec = MainWindow.getJtableColumnVec();
		
		int columnSize = columnNameVec.size();		
				
		//long milsecond =  System.currentTimeMillis();
		
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
				Thread.sleep(1000);
				getPropertyListener().valueChanged("calprogress", 1 + (int)((double)resultMap.size()/(double)columnSize*9));
				if(columnSize == resultMap.size()){
					break;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//milsecond = (System.currentTimeMillis() - milsecond)/1000 ;	
		//System.out.println("sql search time:" + milsecond);
		
		//milsecond =  System.currentTimeMillis();
		//System.out.println("begin to get distinct");
		
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
		/*		
		milsecond = (System.currentTimeMillis() - milsecond)/1000;
		for(Map.Entry<String, List<String>> entry:rangeValueMap.entrySet()){
			System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());			
		}
		System.out.println("end to get distinct:" + milsecond);
		 */
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
			    threadTask.setArgumentResultUnderCondMap(argumentResultUnderCondMap);
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
		 genHandler = new GenerateTreeByLeaf();
		
		 Vector<treeNodeResultObj> treeNodeVec = GenerateTreeByLeaf.getTreeLeafNodeVec();
		 
		 treeNodeVec.clear();
		 
		 Vector<Vector<treeNodeResultObj>> treeNodeByLevelVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();
		 
		 treeNodeByLevelVec.clear();		 
		 
	}
	
	private void setProgressByLoop(double passedNodeNum){				
		int progressVal =(int)(passedNodeNum/denominator * (double)90) + 10;
		getPropertyListener().valueChanged("calprogress",progressVal);
	}
	
	private void buildTreeByLoop(treeNodeResultObj parent, int level, String argumentName, String condition, Set<String> passedColSet, String chnCondition) throws Exception{
		
		passedNodeNum++;
		
		if(level >= treeDeepth){
			parent.isLeaf = true;
			if(null != parent){
				GenerateTreeByLeaf.getTreeLeafNodeVec().add(parent);
			}
			
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
			 addToLeafNodeVec(parent, level, conditionBuf, chnCondition, passedColSet);	
			 			 
			 for(int i=level+1;i<treeDeepth;i++){
				 passedNodeNum += (int)Math.pow(treeWidth, i);
		     }			 
			 return;
		 }
		 
		 List<String> valueRangeList = rangeValueMap.get(maxGainRatioLine);
		 
		 treeNodeResultObj currentNode = buildTree(parent, maxGainRatioLine, level, conditionBuf, chnCondition, passedColSet);
		 
		 passedColSet.add(maxGainRatioLine);
		 
		 level++;
		 
		 for(int i=0;i<valueRangeList.size();i++){
			 StringBuffer chnCondBuffer = new StringBuffer(chnCondition);
			 
			 String newCondition = buildCondition(maxGainRatioLine, conditionBuf, i, chnCondBuffer);
			 
			 if(null == newCondition){
				 continue;
			 }
			 
			 if(i == getTreeWidth()){
				 continue;
			 }
			 
			 setProgressByLoop(passedNodeNum);
			 
			 buildTreeByLoop(currentNode, level, argumentName, newCondition, passedColSet, chnCondBuffer.toString()); 
			 
		 }
		 
		 passedColSet.remove(maxGainRatioLine); 		
	}
	
	private void addToLeafNodeVec(treeNodeResultObj parentNode, int level, StringBuffer condition, String chnCondition, Set<String> passedColSet){
		
		String nodeName = "Node" + String.valueOf(getNodeNum()+1);
		   	      	     	       
    	treeNodeResultObj childNode = buildOtherNode(parentNode, nodeName, level, condition, true, chnCondition, passedColSet);  
    	
    	if(null != childNode){
    	
    	    GenerateTreeByLeaf.getTreeLeafNodeVec().add(childNode);
    	
    	}
				
	}	
	
	private void createTree(String argumentName, int treeDeepth){
		
		try{		      		
							
		initTreeNode();
		
		int level = 0;
		
		String databaseName = ConfigParser.chnToEnColumnName.get(argumentName);	
		
		long milsecond =  System.currentTimeMillis();
		
		buildTreeByLoop(null, level, databaseName, "", null, "");
		
		milsecond = (System.currentTimeMillis() - milsecond)/1000 ;	
		System.out.println("建树耗费时间:" + milsecond + "秒");
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
					
	}
	
	private void showC45Tree(){ 	
        genHandler.generateTree();		
		
		treeNodeResultObj resultTree = GenerateTreeByLeaf.getResultNode();
		
		//for debug
		//printResultTree(resultTree);
		//end debug
		
        TreeDataSheet.Instance().setPreTitle("C4.5决策树");
		
		TreeDataSheet.Instance().show(resultTree);
		
	}
	
	//for debug
	private void printResultTree(treeNodeResultObj resultTree){
		if(null == resultTree){
			return;
		}
		System.out.println("NodeName:" + resultTree.nodeName);		
		Vector<treeNodeResultObj> childVec = resultTree.childNodeVec;
		if(null == childVec || childVec.size() == 0){
			return;
		}
		System.out.println("childInfo:");
		String prtInfo = "";
		for(int i = 0;i<childVec.size();i++){
			treeNodeResultObj tmpObj = childVec.get(i);
			prtInfo += "child"+i + " " + tmpObj.nodeName;
			printResultTree(tmpObj);
		}
		System.out.println(prtInfo);		
	}
	//end debug
	
	private String buildCondition(String currentCol,StringBuffer inputConditionBuf, int index, StringBuffer chnCondBuffer){       		
		StringBuffer conditionBuf = new StringBuffer(inputConditionBuf.toString()); 
		
		if("".equals(conditionBuf.toString())){
			conditionBuf.append("where ");
			chnCondBuffer.append("WHERE");
		}
		else{
			conditionBuf.append(" and ");
			chnCondBuffer.append(" AND ");
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
			
			chnCondBuffer.append(element.mExcelColumnName);
			chnCondBuffer.append(" = ");
            if(false == isNumeric){
            	conditionBuf.append("'");
            	chnCondBuffer.append("'");
			}
            conditionBuf.append(rangeValueMapList.get(index));
			if(false == isNumeric){
				conditionBuf.append("'");
				chnCondBuffer.append("'");
			}
		}
		else{
			if((index+1) == rangeValueMapList.size()){
				return null;
			}
			String tmpVal_1 = rangeValueMapList.get(index);
			String tmpVal_2 = rangeValueMapList.get(index+1);
			conditionBuf.append(currentCol);
			conditionBuf.append(">=");
			conditionBuf.append(tmpVal_1);
			conditionBuf.append(" and ");
			conditionBuf.append(currentCol);
			
			chnCondBuffer.append(element.mExcelColumnName);
			chnCondBuffer.append(" >= ");;
			chnCondBuffer.append(tmpVal_1);
			chnCondBuffer.append(" AND ");
			chnCondBuffer.append(element.mExcelColumnName);
			if(index == rangeValueMapList.size()-2 ){
				conditionBuf.append("<=");
				chnCondBuffer.append(" <= ");
			}
			else{
				conditionBuf.append("<");
				chnCondBuffer.append(" < ");
			}
			conditionBuf.append(tmpVal_2);
			chnCondBuffer.append(tmpVal_2);
		}
				
		return conditionBuf.toString();
	}
	
	private treeNodeResultObj buildTree(treeNodeResultObj parent, String maxGainRatioLine, int level, StringBuffer conditionBuf, String chnCondition, Set<String> passedColSet){
		
		if(level == 0){
			//it's root node
			return buildRootNode(maxGainRatioLine, level);		
		}
		
	    return buildOtherNode(parent, maxGainRatioLine, level, conditionBuf, false, chnCondition, passedColSet);
	}
	
	private treeNodeResultObj buildOtherNode(treeNodeResultObj parent, String nodeName, int level, StringBuffer conditionBuf, boolean isLeaf, String chnCondition, Set<String> passedColSet){
		if(level == 0){
			return null;
		}		
		
		Vector<Vector<treeNodeResultObj>> treeNodeByLevelVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();
		
		Vector<treeNodeResultObj> treeNodeVec = null;
		
		if(treeNodeByLevelVec.size() < (level + 1)){
			treeNodeVec = new Vector<treeNodeResultObj>();
			treeNodeByLevelVec.add(treeNodeVec);
		}
		else{
		    treeNodeVec = treeNodeByLevelVec.get(level);
		}
				
		
		treeNodeResultObj newNode = new treeNodeResultObj();
		
		if(parent.childNodeVec == null){
		
		    parent.childNodeVec = new Vector<treeNodeResultObj>();
		
		}
		
		ConfigElement tmpElement = ConfigParser.columnInfoMap.get(nodeName);
		if(tmpElement != null){
		    newNode.nodeName = tmpElement.mExcelColumnName;
		}
		else{
			newNode.nodeName = nodeName;
		}
		
		
		newNode.objectLevel = level;
				
		newNode.nodeNumber = getNewNodeNum();
		
		newNode.condition = chnCondition;
		
		newNode.sql = conditionBuf.toString();
		
		newNode.isLeaf = isLeaf;
		
		List<String> tmpList = argumentResultUnderCondMap.get(nodeName);
		if(null == tmpList || tmpList.size() ==0 ){
			for(Map.Entry<String, List<String>> entry : argumentResultUnderCondMap.entrySet()){
				tmpList = entry.getValue();
				newNode.Prediction = tmpList.get(0);
				newNode.Probability = tmpList.get(1);	
				break;
			}
		}
		else{
		    newNode.Prediction = tmpList.get(0);
		    newNode.Probability = tmpList.get(1);
		}

		if(null == newNode.childNodeVec){			
		    newNode.childNodeVec = new Vector<treeNodeResultObj>();		    
		}
		
		newNode.chnColumnName = new Vector<String>();
		for(Iterator<String> iter = passedColSet.iterator();iter.hasNext();){
			String colName = iter.next();
			String chName = ConfigParser.columnInfoMap.get(colName).mExcelColumnName;
			if(null != chName){
			newNode.chnColumnName.add(chName);
			}
		}
				
		newNode.nodeInfo = "节点号：" + newNode.nodeNumber + "\n";
		
		newNode.nodeInfo += "预测结果：" + newNode.Prediction + "\n";
		
		newNode.nodeInfo += "概率：" + newNode.Probability; 
		
		newNode.isLeaf = isLeaf;
		
		parent.childNodeVec.add(newNode);
		
		treeNodeVec.add(newNode);
							
		return newNode;		
	}
	
	private treeNodeResultObj buildRootNode(String nodeName, int level){
		treeNodeResultObj rootObj = new treeNodeResultObj();
		
		rootObj.nodeName = nodeName;
		
		rootObj.nodeInfo = nodeName;
		
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
		
		GenerateTreeByLeaf.getTreeLeafNodeVec().clear();
		
		return rootObj;
	}
	
	private String getMaxGainRatio(Map<String, Double> gainRationMap, treeNodeResultObj parentNode){
		double maxGainRatio = -1;
		
		String columName = null;
		
		for(Map.Entry<String, Double> entry : gainRationMap.entrySet()){
			BigDecimal val1 = new BigDecimal(maxGainRatio);
			BigDecimal val2 = new BigDecimal(entry.getValue());
			String currentColName = entry.getKey();
			if(val2.compareTo(new BigDecimal(0)) == 0){
				if(null != parentNode && currentColName.equals(parentNode.nodeName)){
				    return null;
				}
				continue;
			}			
						
			if(val2.compareTo(val1) > 0){
				maxGainRatio = entry.getValue();
				columName = currentColName;
			}
		}
		
		return columName;
	}
}
