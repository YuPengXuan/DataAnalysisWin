package com.xn.alex.data.c45;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DataAnalysisException;
import com.xn.alex.data.database.GetNumberByConditionTask;
import com.xn.alex.data.database.MySqlExecuter;

public class CalculateGainRatioThread implements Runnable{
	
	private String databaseColName = null;
	
	private String tableName = null;
	
	private String argumentName = null;
	
	private String condition = null;
	
	private int argumentTotalCount = 0;
	
	private List<Integer> argumentValueList = new CopyOnWriteArrayList<Integer>();
	
	private Map<String, List<String>> rangeValueMap = null;
	
	private Map<String, Integer> argumentResulMap = new ConcurrentHashMap<String, Integer>();
	
	private C45 c45Handler = new C45();	
	
	private Map<String, Double> gainRatioMap = null;
	
	private Map<String, Integer> partCountMap = new ConcurrentHashMap<String, Integer>();
	
	private Map<String, List<String>> argumentResultUnderCondMap;
	
	public Map<String, List<String>> getArgumentResultUnderCondMap() {
		return argumentResultUnderCondMap;
	}

	public void setArgumentResultUnderCondMap(
			Map<String, List<String>> argumentResultUnderCondMap) {
		this.argumentResultUnderCondMap = argumentResultUnderCondMap;
	}

	public Map<String, Integer> getArgumentResulMap() {
		return argumentResulMap;
	}
	
	public Map<String, Double> getGainRatioMap() {
		return gainRatioMap;
	}

	public void setGainRatioMap(Map<String, Double> gainRatioMap) {
		this.gainRatioMap = gainRatioMap;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDatabaseColName() {
		return databaseColName;
	}

	public void setDatabaseColName(String databaseColName) {
		this.databaseColName = databaseColName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getArgumentName() {
		return argumentName;
	}

	public void setArgumentName(String argumentName) {
		this.argumentName = argumentName;
	}

	public Map<String, List<String>> getRangeValueMap() {
		return rangeValueMap;
	}

	public void setRangeValueMap(Map<String, List<String>> rangeValueMap) {
		this.rangeValueMap = rangeValueMap;
	}
	
	private boolean isDoubleZero(double val){
		BigDecimal decVal = new BigDecimal(val);
		
		if(decVal.compareTo(new BigDecimal(0)) == 0){
			return false;
		}
		
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long milsecond = System.currentTimeMillis();			
		double infoD = calCulateArgumentInfoD();
		
		if(true == isDoubleZero(infoD)){
			gainRatioMap.put(databaseColName, (double)0);
			return;
		}
		
		double infoAD = calCulateColumnInfoAD();
		
		if(true == isDoubleZero(infoAD)){
			gainRatioMap.put(databaseColName, (double)0);
			return;
		}
		
		double gain = infoD - infoAD;
		
		double hv = calculateHV(); 
		
		double gainRatio = 0;
		
		if(hv > +0.0000001){
		  gainRatio = c45Handler.gainRatio(gain, hv);
		}
		
		gainRatioMap.put(databaseColName, gainRatio);
		
		milsecond = (System.currentTimeMillis() - milsecond)/1000 ;
		System.out.println("CalculateGainRatioThread tid:"+ Thread.currentThread() + " run time:" + milsecond);
		
	}	
	
	private double calculateHV(){
		double hv = 0;
		List<Integer> partValList = new CopyOnWriteArrayList<Integer>();
		for(Map.Entry<String, Integer> entry : partCountMap.entrySet()){
			int value = entry.getValue();
			if(value == 0){
				continue;
			}
			partValList.add(value);
		}
		
		if(partValList.size() != 0){
			hv = c45Handler.infoD(argumentTotalCount, partValList);
		}
		
		return hv;
	}
	
	private double calCulateColumnInfoAD(){
		double infoAD = 0;
		
        MySqlExecuter mysqlExecutor = MySqlExecuter.getMySqlExecuter();
		
		GetNumberByConditionTask task = new GetNumberByConditionTask();
		
		task.setTableName(tableName);
		
		task.setColumnName(databaseColName);
		
		List<String> rangeValueMapList = rangeValueMap.get(databaseColName);
		
		ConfigElement element = ConfigParser.columnInfoMap.get(databaseColName);
		
		boolean isRangeCondition = false;
		boolean isNumeric = false;
		
		if(false == element.mValueType.contains("VARCHAR")){
			isNumeric = true;	
			if(rangeValueMapList.size() > 5 ){
			    isRangeCondition = true;
			}
		}
		
		Map<String, List<Integer>> subPartNumMap = new ConcurrentHashMap<String, List<Integer>>();
		
        StringBuffer subConditionBuf = new StringBuffer("");
				
		for(int i=0;i<rangeValueMapList.size();i++){
			subConditionBuf.setLength(0);
			
			subConditionBuf.append(condition);
			
			String tmpVal = rangeValueMapList.get(i);
			if("".equals(subConditionBuf.toString())){
				subConditionBuf.append("where ");
			}
			else{
			    subConditionBuf.append(" and ");
			}
			
			subConditionBuf.append(databaseColName);
			
			if(false == isRangeCondition){				
				subConditionBuf.append("=");
                if(false == isNumeric){
                	subConditionBuf.append("'");
				}
				subConditionBuf.append(tmpVal);
				if(false == isNumeric){
	                	subConditionBuf.append("'");
				}
			}
			else{
				if(i == rangeValueMapList.size() -1){
					break;
				}
				String tmpVal_2 = rangeValueMapList.get(i+1);				
				subConditionBuf.append(">=");
				subConditionBuf.append(tmpVal);
				subConditionBuf.append(" and ");
				subConditionBuf.append(databaseColName);
				if(i == rangeValueMapList.size()-2 ){
					subConditionBuf.append("<=");
				}
				else{
				    subConditionBuf.append("<");
				}
				subConditionBuf.append(tmpVal_2);
			}
			
            task.setCondition(subConditionBuf.toString());
			
			try {
				mysqlExecutor.executer(task);
				
				int result = task.getResult();
				
				partCountMap.put(subConditionBuf.toString(), result);
				
				List<Integer> subValList = getArgumentNumUnderCondition(subConditionBuf.toString(), task);
				
				if(null == subValList){
					return 0;
				}
				
				subPartNumMap.put(subConditionBuf.toString(), subValList);
				
			} catch (DataAnalysisException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return 0;
			}
			
		}
				
		infoAD = c45Handler.infoAD(argumentTotalCount, partCountMap, subPartNumMap);
		
		return infoAD;
	}
	
	private List<Integer> getArgumentNumUnderCondition(String condition, GetNumberByConditionTask task){
		List<Integer> resultList = new CopyOnWriteArrayList<Integer>();
		
		List<String> argumentList = rangeValueMap.get(argumentName);
		
		ConfigElement element = ConfigParser.columnInfoMap.get(databaseColName);
		
        boolean isNumeric = false;
		
		if(false == element.mValueType.contains("VARCHAR")){
			isNumeric = true;
		}
								
		StringBuffer sb = new StringBuffer(condition);
		
		if("".equals(condition)){
			sb.append("where ");
		}
		else{
			sb.append(" and ");
		}
		sb.append(argumentName);
		sb.append("=");
		if(false == isNumeric){
			sb.append("'");
		}		
		
		for(int i=0;i<argumentList.size();i++){
			String tmpVal = argumentList.get(i);
			String tmpCondition = sb.toString() + tmpVal;
			if(false == isNumeric){
				tmpCondition +="'";
			}
	
			task.setCondition(tmpCondition);
			
			try {
				
				MySqlExecuter mysqlExecutor = MySqlExecuter.getMySqlExecuter();				
				
				mysqlExecutor.executer(task);
				
				int result = task.getResult();
																
				resultList.add(result);
			} catch (DataAnalysisException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return null;
			}
			
		}
		
		return resultList;
	}
	
	private double calCulateArgumentInfoD(){
		MySqlExecuter mysqlExecutor = MySqlExecuter.getMySqlExecuter();
		
		GetNumberByConditionTask task = new GetNumberByConditionTask();
		
		task.setTableName(tableName);
		
		task.setColumnName(argumentName);
	
		StringBuffer subConditionBuf = new StringBuffer("");
		subConditionBuf.append(condition);
		
		List<String> rangeValueMapList = rangeValueMap.get(argumentName);
		
        boolean isNumeric = false;
        
        ConfigElement element = ConfigParser.columnInfoMap.get(databaseColName);
		
		if(false == element.mValueType.contains("VARCHAR")){
			isNumeric = true;	
		}
		
		if("".equals(subConditionBuf.toString())){
			subConditionBuf.append("where ");
		}
		else{
			subConditionBuf.append(" and ");
		}
		
		subConditionBuf.append(argumentName);				
		subConditionBuf.append("=");		
		
		int tmpMaxArgumentResult = 0;
		String tmpMaxArgumentName = "";
		for(int i=0;i<rangeValueMapList.size();i++){
			String tmpVal = rangeValueMapList.get(i);
			
			String condPrefix = subConditionBuf.toString();
			
			if(false == isNumeric){
				condPrefix = condPrefix +"'";
			}
			
			condPrefix = condPrefix + tmpVal;
			if(false == isNumeric){
				condPrefix = condPrefix +"'";
			}			
			task.setCondition(condPrefix);
			
			try {
				mysqlExecutor.executer(task);
				
				int result = task.getResult();
				
				if(result>tmpMaxArgumentResult){
					tmpMaxArgumentResult = result;
					tmpMaxArgumentName = tmpVal;
				}
				
				argumentTotalCount += result;
				
				argumentValueList.add(result);
				
				argumentResulMap.put(tmpVal, result);
			} catch (DataAnalysisException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return 0;
			}						
						
		}
		
		List<String> tmpList = new CopyOnWriteArrayList<String>();
		double prb_001 = (double)tmpMaxArgumentResult/(double)argumentTotalCount;
		tmpList.add(tmpMaxArgumentName);
		tmpList.add(String.valueOf(prb_001));
		argumentResultUnderCondMap.put(databaseColName, tmpList);
		
		double infoD = c45Handler.infoD(argumentTotalCount, argumentValueList);
		
		return infoD;
				
	}

}
