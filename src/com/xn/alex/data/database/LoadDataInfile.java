package com.xn.alex.data.database;

import java.sql.SQLException;
import java.util.List;

public class LoadDataInfile {
	private final DataBaseConnection databaseConnection = new DataBaseConnection();
	
	public LoadDataInfile() {
		databaseConnection.connect();
	}
	
    public boolean load(final List<String[]> data, final String tableName, final String errorHandler, final int batchNumber) {
    	final SqlExecuter sqlExecuter = new LoadDataInFileExecuter(data, errorHandler, tableName, batchNumber);
    	try {
			sqlExecuter.executer(databaseConnection.getConnection());
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    
    public void disconnection() {
    	databaseConnection.disconnect();
    }
    
}
