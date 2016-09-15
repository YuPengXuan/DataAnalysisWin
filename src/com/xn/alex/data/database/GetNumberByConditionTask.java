package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class GetNumberByConditionTask implements SqlTask {
	
	private Map<String, Integer> resultMap = null;
		
	private String columnName = null;
	
	private String tableName = null;
	
	private String condition = null;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public void setResultMap(Map<String, Integer> resultMap) {
		this.resultMap = resultMap;
	}

	@Override
	public Statement run(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		int result = 0;
		Statement statement = connection.createStatement();
		try{
			String sql = "select " + getColumnName() +" from " + getTableName() + " " + getCondition();
			ResultSet rs = statement.executeQuery(sql);
			
			if(rs.next()){
	            
		        String resultStr = rs.getString(1);
		    
		        result = Integer.parseInt(resultStr); 		    
		    }
			
			resultMap.put(columnName, result);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return statement;
	}

}
