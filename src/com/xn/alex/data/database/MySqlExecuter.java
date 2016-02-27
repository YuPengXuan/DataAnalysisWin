package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
	    	  final LoginAction loginAction = LoginAction.Instance();
	    	  mysqlDataSource.setUsername(loginAction.getLoginUserName());
	    	  mysqlDataSource.setPassword(loginAction.getLoginPassword());
	    	  mysqlDataSource.setMaxTotal(2);
	    	  mysqlDataSource.setMaxIdle(1);
	    	  mysqlDataSource.setInitialSize(1);
	    	  mysqlDataSource.setDefaultAutoCommit(false);
	    	  mysqlDataSource.setEnableAutoCommitOnReturn(true);
	    	  mysqlDataSource.setMaxConnLifetimeMillis(6 * 60 * 60 * 1000);
	      }
	      
	      return mySqlExecuter;
	}

	public Connection getConnection() throws SQLException {
		return mysqlDataSource.getConnection();
	}
	
	public boolean executer(final SqlTask sqlTask) throws DataAnalysisException {
		Connection connection = null;
		try {
			connection = getConnection();
			if (connection != null) {
				final Statement statement = sqlTask.run(connection);
				if (statement != null) {
					statement.close();
					return true;
				}
			}
		} catch (SQLException e) {
			throw new DataAnalysisException(e);
		} finally {
			if (connection != null) {
				try {
					connection.commit();
					connection.close();
				} catch (SQLException e) {
					throw new DataAnalysisException(e);
				}
			}
		}
		
		return false;
	}
}
