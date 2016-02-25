package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.login.LoginAction;

public class DataBaseConnection {
    private Connection con = null;
	
	public Connection getConnection() {
		return con;
	}

	public boolean connect(){
		try{
			try{
		    Class.forName(DatabaseConstant.DRIVER);
		
		    con = DriverManager.getConnection(DatabaseConstant.URL,LoginAction.Instance().getLoginUserName(), LoginAction.Instance().getLoginPassword());
			}
			catch(SQLException sqle){
				int errorCode = sqle.getErrorCode();
				
				if(1045 == errorCode){
					JOptionPane.showMessageDialog(null,"用户名或密码不正确！","错误信息",JOptionPane.ERROR_MESSAGE);
				}
				else{
					JOptionPane.showMessageDialog(null,"数据库连接失败！","错误信息",JOptionPane.ERROR_MESSAGE);
				}
				return false;
			}
		}
		catch(Exception e){
			System.out.println("连接数据库失败！");
			return false;
			
		}
		return true;
	}
	
	public boolean disconnect(){			
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("断连数据库失败！");
				
				return false;
			}
		}
		
		return true;
	}
}
