package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetNumericColRangeTask implements SqlTask{
	
    private String columnName = null;
	
	private String tableName = null;
	
	private int splitNum;
	
	private Map<String, List<String>> rangeValueMap = null;

	public void setRangeValueMap(Map<String, List<String>> rangeValueMap) {
		this.rangeValueMap = rangeValueMap;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getSplitNum() {
		return splitNum;
	}

	public void setSplitNum(int splitNum) {
		this.splitNum = splitNum;
	}
	

	@Override
	public Statement run(Connection connection) throws SQLException {
		// TODO Auto-generated method stub		
		float maxVal = 0;
		float minVal = 0;
		Statement statement = connection.createStatement();
		try{
			String sql = "select max(" + getColumnName() +") from " + getTableName();
			ResultSet rs = statement.executeQuery(sql);			
			if(rs.next()){
				maxVal = rs.getFloat(1);
			}
			
			sql = "select min(" + getColumnName() +") from " + getTableName();
			if(rs.next()){
				minVal = rs.getFloat(1);
			}
			
			float incrNum = (maxVal-minVal)/splitNum;
			List<String> valueSplitList = new ArrayList<String>();
			for(int i=0;i<=splitNum;i++){
				if(i == splitNum){
					valueSplitList.add(String.valueOf(maxVal));	
					continue;
				}				
				float tmpVal = i*incrNum + minVal;				
				valueSplitList.add(String.valueOf(tmpVal));				
			}
			
			rangeValueMap.put(columnName, valueSplitList);
			
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("切分数值列数值范围失败!");
		}
				
		return statement;
	}

}
