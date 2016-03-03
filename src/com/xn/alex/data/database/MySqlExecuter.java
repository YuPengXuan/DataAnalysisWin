package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.dbcp2.BasicDataSource;

import com.xn.alex.data.login.LoginAction;

public class MySqlExecuter {

	private static BasicDataSource mysqlDataSource;
	
	private static MySqlExecuter mySqlExecuter;
	
	private MySqlExecuter() {
		
	}
	
	public static MySqlExecuter getMySqlExecuter() {
	      if(mySqlExecuter == null) {
	    	  mySqlExecuter = new MySqlExecuter();
	    	  mysqlDataSource = new BasicDataSource();
	    	  mysqlDataSource.setUrl(DatabaseConstant.URL);
	    	  mysqlDataSource.setMaxTotal(2);
	    	  mysqlDataSource.setMaxIdle(1);
	    	  mysqlDataSource.setInitialSize(1);
	    	  mysqlDataSource.setDefaultAutoCommit(false);
	    	  Collection<String> initSqls = new ArrayList<String>();
	    	  Collections.addAll(initSqls, "CREATE DATABASE IF NOT EXISTS " + DatabaseConstant.DB_NAME);
	    	  Collections.addAll(initSqls, "USE " + DatabaseConstant.DB_NAME);
			  mysqlDataSource.setConnectionInitSqls(initSqls);
	    	  //MySql default 8 hours to close the live session, so we need set the hours far better less than 8 hours.
	    	  mysqlDataSource.setMaxConnLifetimeMillis(6 * 60 * 60 * 1000);
	      }
	      final LoginAction loginAction = LoginAction.Instance();
	      mysqlDataSource.setUsername(loginAction.getLoginUserName());
    	  mysqlDataSource.setPassword(loginAction.getLoginPassword());
	      return mySqlExecuter;
	}

	public Connection getConnection() throws SQLException {
		return mysqlDataSource.getConnection();
	}
	
	public boolean executer(final SqlTask sqlTask) throws DataAnalysisException {
		Connection connection = null;
		boolean result = false;
		try {
			connection = getConnection();
			if (connection != null) {
				final Statement statement = sqlTask.run(connection);
				if (statement != null) {
					statement.close();
					result = true;
				}
			}
		} catch (SQLException e) {
			result = false;
			throw new DataAnalysisException(e);
		} finally {
			if (connection != null) {
				try {
					if(result) {
					  connection.commit();
					} else {
						connection.rollback();
					}
					connection.close();
				} catch (SQLException e) {
					throw new DataAnalysisException(e);
				}
			}
		}
		
		return result;
	}
}
