package com.xn.alex.data.database;

import java.util.List;

public class LoadDataInfile {
    public boolean load(final DataBaseConnection connection, final List<String[]> data, final String tableName, final String errorHandler, final int batchNumber) {
    	final SqlExecuter sqlExecuter = new LoadDataInFileExecuter(data, errorHandler, tableName, batchNumber);
    	return DatabaseOperation.executer(connection, sqlExecuter);
     }
}
