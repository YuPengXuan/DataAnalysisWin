package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class getDistinctNumberOfColumnTask implements SqlTask {
	
	private Map<String, Integer> resultMap = null;
	
	private String keyId = null;
	
	private String tableName = null;
	
	private String columnName = null;
		

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public void setResultMap(Map<String, Integer> resultMap) {
		this.resultMap = resultMap;
	}


	@Override
	public Statement run(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		int result = 0;
		Statement statement = connection.createStatement();
		String sql = "select count(distinct " +  columnName + ") from " + tableName;;
		ResultSet rs = statement.executeQuery(sql);
		
		if(rs.next()){
            
	        String resultStr = rs.getString(1);
	    
	        result = Integer.parseInt(resultStr); 		    
	    }		
		resultMap.put(keyId, result);
		
		return statement;
	}

}
