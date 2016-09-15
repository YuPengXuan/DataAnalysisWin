package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetDistinctValueTask implements SqlTask {
	
    private String columnName = null;
	
	private String tableName = null;
		
	private Map<String, List<String>> rangeValueMap = null;

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

	public Map<String, List<String>> getRangeValueMap() {
		return rangeValueMap;
	}

	public void setRangeValueMap(Map<String, List<String>> rangeValueMap) {
		this.rangeValueMap = rangeValueMap;
	}

	@Override
	public Statement run(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		Statement statement = connection.createStatement();
		try{
			String sql = "select distinct (" + getColumnName() +") from " + getTableName();
			ResultSet rs = statement.executeQuery(sql);			
			
			List<String> valueSplitList = new ArrayList<String>();
			
			while(rs.next()){
				valueSplitList.add(rs.getString(1));
			}
						
			rangeValueMap.put(columnName, valueSplitList);
			
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("获取列取值范围失败!");
		}
				
		return statement;
	}

}
