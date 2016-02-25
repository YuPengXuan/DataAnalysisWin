package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.SQLException;


public interface SqlExecuter {
	public static final String IGNORE = "IGNORE";
	public static final String REPLACE ="REPLACE";
    
	public void executer(Connection connection) throws SQLException;
}
