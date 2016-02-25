package com.xn.alex.data.database;

import java.sql.Connection;

import org.apache.log4j.Logger;

public class DatabaseOperation {
    private final static Logger logger = Logger.getLogger(DatabaseOperation.class);
    
    public static synchronized boolean executer(final DataBaseConnection databaseConnection, final SqlExecuter sqlExecuter) {
    	if(!databaseConnection.connect()) {
    		return false;
    	}
		final Connection connection = databaseConnection.getConnection();
    	try {
			connection.setAutoCommit(false);
			sqlExecuter.executer(connection);
			connection.commit();
			return true;
		} catch (Exception e) {
			logger.error("Can't load the excel/csv data", e);
			return false;
		} finally {
			databaseConnection.disconnect();
		}
    }
}
