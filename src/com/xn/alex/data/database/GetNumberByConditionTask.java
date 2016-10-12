package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetNumberByConditionTask implements SqlTask {
	
	private int result = 0;
			
	private String columnName = null;
	
	private String tableName = null;
	
	private String condition = null;
	
	public int getResult() {
		return result;
	}
	
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
	
	@Override
	public Statement run(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		Statement statement = connection.createStatement();
		try{
			String sql = "select count(*) from " + getTableName() + " " + getCondition();
			ResultSet rs = statement.executeQuery(sql);
			
			if(rs.next()){
	            
		        String resultStr = rs.getString(1);
		    
		        result = Integer.parseInt(resultStr); 		    
		    }			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return statement;
	}

}
