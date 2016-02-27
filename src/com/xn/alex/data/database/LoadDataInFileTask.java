package com.xn.alex.data.database;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbcp2.DelegatingPreparedStatement;

import com.mysql.jdbc.PreparedStatement;

public class LoadDataInFileTask implements SqlTask {

	private String errorHandler;
	private String tableName;
	private int batchNumber;
	private List<String[]> data;
    
    public LoadDataInFileTask(final List<String[]> data, final String errorHandler, final String tableName, final int batchNumber) {
    	this.errorHandler = errorHandler;
    	this.tableName = tableName;
    	this.batchNumber = batchNumber;
    	this.data = data;
	}
    
    @Override
	public Statement run(Connection connection) throws SQLException {
    	if(batchNumber == 0) {
    		return null;
    	}
		final String executeSql = String.format("LOAD DATA LOCAL INFILE 'sql.csv' %s INTO TABLE %s", errorHandler, tableName);
		final int dataSize = data.size();
		final int splitNumber = dataSize / batchNumber;
		final Statement statement = connection.prepareStatement(executeSql);
		
		for(int splitIndex = 0; splitIndex < splitNumber; splitIndex ++) {
			executeSql(data, statement, batchNumber, splitIndex);		
		}
		executeSql(data, statement, batchNumber, splitNumber);
		
	    return statement;
	}

	private void executeSql(final List<String[]> data, Statement statement, final int batchNumber, int splitIndex) throws SQLException {
		final int startIndex = splitIndex * batchNumber;
		int endIndex = startIndex + batchNumber;
		if(endIndex > data.size()) {
			endIndex = data.size();
		}
		
		if(startIndex != endIndex) {
		  final InputStream inputStream = buildInsertData(data, startIndex, endIndex);
		  if(statement instanceof DelegatingPreparedStatement) {
			  DelegatingPreparedStatement pooledDelgate = (DelegatingPreparedStatement)statement;
			  final DelegatingPreparedStatement delegate = (DelegatingPreparedStatement) pooledDelgate.getDelegate();
			  ((PreparedStatement)delegate.getDelegate()).setLocalInfileInputStream(inputStream);
		  }
		  
		  ((java.sql.PreparedStatement)statement).executeUpdate();
		}
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
    	
    	final ByteArrayInputStream ingputStream = new ByteArrayInputStream(String.valueOf(stringBuilder).getBytes());
    	
    	return new BufferedInputStream(ingputStream, 3*1024*1024);
    }
}
