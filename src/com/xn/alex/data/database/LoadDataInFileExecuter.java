package com.xn.alex.data.database;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

public class LoadDataInFileExecuter implements SqlExecuter {

	private String errorHandler;
	private String tableName;
	private int batchNumber;
	private List<String[]> data;
    
    public LoadDataInFileExecuter(final List<String[]> data, final String errorHandler, final String tableName, final int batchNumber) {
    	this.errorHandler = errorHandler;
    	this.tableName = tableName;
    	this.batchNumber = batchNumber;
    	this.data = data;
	}
    
    @Override
	public void executer(Connection connection) throws SQLException {
		final String executeSql = String.format("LOAD DATA LOCAL INFILE 'sql.csv' %s INTO TABLE %s IGNORE 1 LINES", errorHandler, tableName);
		final int dataSize = data.size();
		final int splitNumber = dataSize / batchNumber;
		final Statement statement =  (Statement) connection.createStatement();
		
		for(int splitIndex = 0; splitIndex < splitNumber; splitIndex ++) {
			executeSql(data, statement, batchNumber, executeSql, splitIndex);		
		}
		executeSql(data, statement, batchNumber, executeSql, splitNumber);
		
		statement.close();
	}

	private void executeSql(final List<String[]> data, Statement statement, final int batchNumber,
			final String executeSql, int splitIndex) throws SQLException {
		final int startIndex = splitIndex * batchNumber;
		int endIndex = startIndex + batchNumber;
		if(endIndex > data.size()) {
			endIndex = data.size();
		}
		
		final InputStream inputStream = buildInsertData(data, startIndex, endIndex);
		statement.setLocalInfileInputStream(inputStream);
		statement.executeUpdate(executeSql);
	}
    
    private InputStream buildInsertData(List<String[]> data, int startIndex, int endIndex) {
    	final StringBuilder stringBuilder = new StringBuilder();
    	
    	for(int index = startIndex; index < endIndex; index++) {
    	   final String[] lines = data.get(index);
    	   stringBuilder.append(lines[0]);
    	   for(int i = 1; i < lines.length; i ++) {
    		   stringBuilder.append("\t").append(lines[i]);
    	   }
    	   stringBuilder.append("\n");
    	}
    	
    	final ByteArrayInputStream ingputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes());
    	
    	return new BufferedInputStream(ingputStream, 6*1024*1024);
    }
}
