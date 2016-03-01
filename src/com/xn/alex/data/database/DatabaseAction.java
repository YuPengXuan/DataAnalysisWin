package com.xn.alex.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.ConfigParser.DataColumnInfo;
import com.xn.alex.data.login.LoginAction;

public class DatabaseAction {
	
	private static DatabaseAction databaseHandler = null;
	
	private Connection con = null;
	
	private PreparedStatement currentPs = null;
	
	private DatabaseAction(){		
	}
	
	public static DatabaseAction Instance(){
		
		if(null == databaseHandler){
			databaseHandler = new DatabaseAction();
		}
		
		return databaseHandler;		
	}
	
	public boolean connect(){
		
		try{
			
			try{
				Class.forName(DatabaseConstant.DRIVER);
			    con = DriverManager.getConnection(DatabaseConstant.URL,LoginAction.Instance().getLoginUserName(),LoginAction.Instance().getLoginPassword());
			    Statement statement = con.createStatement();
			    statement.executeUpdate("USE " + DatabaseConstant.DB_NAME);
			    statement.close();
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
	
	public ResultSet getOneResult(String tableName, String columnName, String condition){
		
		ResultSet rs = null;
		
		try{
			
			if(false == connect()){
				return null;
			}
			
			StringBuffer sb = new StringBuffer("SELECT ");
			
			if(null == columnName){
				sb.append("*");
			}
			else{
				sb.append(columnName);
			}
			
			sb.append(" FROM ");
			
			sb.append(tableName);
			
			if(null != condition){
			
			    sb.append(" WHERE ");
			
			    sb.append(condition);
			}
			
			String sql = sb.toString();
			
		    PreparedStatement ps = con.prepareStatement(sql);
			
			currentPs = ps;
			
			rs = ps.executeQuery();	
			
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("获取表 " + tableName + "数据失败！");			
			
			return null;
			
		}
		
		return rs;
		
	}
	
	public ResultSet getAllResult(String tableName){
		
		ResultSet rs = null;
		
        try{	
			
			if(false == connect()){
				return null;
			}
			
			StringBuffer sb = new StringBuffer("SELECT * FROM ");
			
			sb.append(tableName);
			//only get 2000 lines;
			sb.append(" limit 2000");
			
			String sql = sb.toString();
			
		    PreparedStatement ps = con.prepareStatement(sql);
		    
		    currentPs = ps;
		    		    
		    rs = ps.executeQuery();		    
		
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("获取表 " + tableName + "数据失败！");			
			
			return null;			
		}
		
		return rs;  		
		
	}
	
	public boolean deleteData(String tableName, String condition){
		
		try{
			
			if(false == connect()){
				return false;
			}
			
			StringBuffer sb = new StringBuffer("DELETE FROM ");
			
			sb.append(tableName);
			
			sb.append(" WHERE ");
			
			sb.append(condition);
			
			String sql = sb.toString();
			
            PreparedStatement ps = con.prepareStatement(sql);
		    
		    currentPs = ps;	
		    
            ps.execute();		  
			
			if(false == disconnect()){
		    	return false;
		    }
			
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("删除数据库数据失败！");			
		}
		
		return true;
	}
	
	
	public boolean deleteAll(String tableName){
		
		try{	
			
			if(false == connect()){
				return false;
			}
			
			StringBuffer sb = new StringBuffer("DELETE FROM ");
			
			sb.append(tableName);
			
			String sql = sb.toString();
			
		    PreparedStatement ps = con.prepareStatement(sql);
		    
		    currentPs = ps;	
		    
            ps.execute();
		    		    	
		    ps.close();
		    			    
		    if(false == disconnect()){
		    	return false;
		    }
		
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("删除表 " + tableName + "数据失败！");
			
			return false;			
		}
		
		return true;
	}
	
	public boolean dropTable(String tableName){
		
		try{		
		    if(false == connect()){
			    return false;
		    }
		    
		    StringBuffer sb = new StringBuffer("DROP TABLE IF EXISTS ");
		    
		    sb.append(tableName);
		    
		    String sql = sb.toString();
		    
		    PreparedStatement ps =  con.prepareStatement(sql);
		    
		    currentPs = ps;
		    
		    ps.execute();
		    
		    ps.close();
		    		    
		     if(false == disconnect()){
		         return false;
		     }		     
		     
		     return true;
		     
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("删除表"+tableName+"失败！");
		}
		
	    return true;
	}
	
	public boolean createTable(String tableName, List<DataColumnInfo> columnNameToType, String primaryKey){
		
		if(false == dropTable(tableName)){
			return false;
		}
		
		try{		
		    if(false == connect()){
			    return false;
		    }
		    		   
		    StringBuffer sb = new StringBuffer("CREATE TABLE ");
		    
		    sb.append(tableName);
		    
		    sb.append("(");
		    
            String condition = "";           
            
            for(DataColumnInfo entry : columnNameToType){
            	
            	String columnName = entry.getName();
            	
            	String columnType = entry.getType();
            	
            	String tmpStr = "";
            	
            	if(columnName.equals(primaryKey)){
            	
            	    tmpStr = columnName + " " +  columnType + "  not null,"; 
            	}
            	else{
            		tmpStr = columnName + " " +  columnType + "  null,";
            	}
            	
            	condition = condition + tmpStr;           	
            }           
            
            condition = condition + "PRIMARY KEY (" + primaryKey + ")";
            
            sb.append(condition);
            
            sb.append(") ENGINE=MYISAM DEFAULT CHARSET=utf8 ");
		    
		    String sql = sb.toString();
		    
		    PreparedStatement ps =  con.prepareStatement(sql);
		    
		    currentPs = ps;
		    
		    ps.execute();
		    
		    if(false == isTableExist(tableName)){
		    	
		    	System.out.println("创建表 " + tableName + "失败！");
		    	
		    	ps.close();
		    	
		    	return false;
		    }
				
		     if(false == disconnect()){
		         return false;
		     }
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("创建表"+tableName+"失败！");
			
		}
		
	    return true;
		
	}
	
	public boolean insertTable(String tableName, List<String> columnNameVec, Vector<Vector<String>> commitValVec){
		
		if(null == tableName || columnNameVec.size() == 0 || commitValVec.size() == 0){			
			return true;			
		}
				
		try{
			
			int commitNum = 1000;
			
			 if(false == connect()){
				    return false;
			 }
			 
			 con.setAutoCommit(false);
			 
			 PreparedStatement ps =  con.prepareStatement("USE TEST");
			 
			 currentPs = ps;
			 
			 ps.addBatch();
			
			 String strPrefix = "INSERT INTO " + tableName + "(";			 
			 
			 String columnNameCon =  constructInsertHeader(columnNameVec);
			 
			 strPrefix = strPrefix + columnNameCon + ") VALUES (";			 
			 
             String valueStr = "";
             
             StringBuffer sb = new StringBuffer("");
			 
			 for(int i=0;i<commitValVec.size();i++){
				 
				 sb.append(strPrefix);
				 
				 if(i % commitNum == 1 && i !=1 ){
					 
					 ps.executeBatch();
					 					 
					 con.commit();
					 
					 valueStr = "";
					 
				 }
				 
				 Vector<String> tmpValueVec = commitValVec.get(i);
				 
				 valueStr = constructValueStr(tmpValueVec, columnNameVec);
				 				 
				 sb.append(valueStr);
				 
				 sb.append(")");
				 
				 String sql = sb.toString();				 
							 
				 ps.addBatch(sql);
				 
				 sb.setLength(0);
			 }
			 ps.executeBatch();
			 
			 con.commit();
			 
			 con.setAutoCommit(true);
			
			 if(false == disconnect()){
				    return false;
			  }
			 				
			  return true;
			
		}
		catch(Exception e){		
			
			e.printStackTrace();
			
			System.out.println("插入数据到表 "+ tableName +"失败");
			
			try {
				
				if(!con.isClosed()){ 
				
					con.rollback();
					
				    con.setAutoCommit(true);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				
				closeCurrentConnection();
				
				e1.printStackTrace();
			}
			
			return false;
		}
		
	}
	
	private String constructInsertHeader(List<String> columnNameVec){
		
			try {
								    	         
		         String columnNameCon = "";
		         
		         for(int i=0;i<columnNameVec.size();i++){
		        	 
		        	 if(i != columnNameVec.size() -1){
		        	     columnNameCon = columnNameCon + columnNameVec.get(i)+",";
		        	 }
		        	 else{
		        	 columnNameCon = columnNameCon + columnNameVec.get(i);
		        	 }
		         }
		         
		         return columnNameCon;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
	
	
	private String constructValueStr(List<String> valueVec, List<String> columnNameVec){
		
		String resultStr = "";
		
		for(int i=0; i<valueVec.size();i ++ ){
			
			String tmpValue = valueVec.get(i);
			
			String columnName = columnNameVec.get(i);
			
			if(true == isVarcharColumn(i, columnName)){
				
				tmpValue = "'" + tmpValue + "'";
			}
			
			if(i != valueVec.size()-1){
				
			    resultStr = resultStr + tmpValue + ",";
			    
			}
			else{
				
				resultStr = resultStr + tmpValue;
			}		
			
		}
		
		return resultStr;
	}
	
	private boolean isVarcharColumn(int i, String columnName){
		
		ConfigElement element = ConfigParser.columnInfoMap.get(columnName);
		
		if(null == element){
			return false;
		}
		
		if(element.mValueType.contains("VARCHAR")){
			return true;
		}
		
		return false;
	}
	
	public void closeCurrentConnection(){
		
		try{
			if(null != currentPs){
			    currentPs.close();
			}
		}catch(Exception e){
						
		}finally{
			if(null != currentPs)
				try {
					currentPs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
        try{
        	if(null != con)
        	    con.close();
        }catch(Exception e){
        	
        }finally{
        	if(null != con)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }        
      }
	
	public boolean updateTable(String tableName, Vector<String> columnNameVec, Vector<Vector<String>> commitValVec, Vector<String> conditionVec){
		
		if(null == tableName || columnNameVec.size() == 0 || commitValVec.size() == 0){			
			return true;			
		}
		
		try{
			
			int commitNum = 1000;
			
			if(false == connect()){
			    return false;
		    }
			
			
			//StringBuffer sb = new StringBuffer("update ? set ? where ?");
			
			String sqlPrefix = "UPDATE " + tableName + " SET ";
						
			PreparedStatement ps = con.prepareStatement("USE TEST");			
			 
			currentPs = ps;
			
			ps.addBatch();
			 
			con.setAutoCommit(false);
			
			StringBuffer sb = new StringBuffer("");
			
			for(int i=0;i<commitValVec.size();i++){
				
				sb.append(sqlPrefix);
				
				Vector<String> valVec = commitValVec.get(i);
				
				
				for(int j=0;j<valVec.size();j++){
					
					String columnName = columnNameVec.get(j);
					
					String updateVal = valVec.get(j);
					
					sb.append(columnName);
					
					sb.append("=");
					
					if(true == isVarcharColumn(j,columnName)){
						
						updateVal = "'" + updateVal + "'";
					}
												
					sb.append(updateVal);						
					
					if(j!= valVec.size()-1)
					{
						sb.append(",");
					}					
					
				}
				
				sb.append(" WHERE ");
				
				String whereCondition = conditionVec.get(i);
				
				sb.append(whereCondition);
				
				String sql = sb.toString();
				
				ps.addBatch(sql);
				
				sb.setLength(0);
				
				if(i % commitNum == 1 && i !=1 ){
					
					 ps.executeBatch();
					 
					 con.commit();				 
					 
				 }
				
			}
			
			ps.executeBatch();
			
			con.commit();
			
			con.setAutoCommit(true);
			
			
			if(false == disconnect()){
			    	return false;
			}
			
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("更新表"+ tableName +"失败!");
		}
		
		
		return true;
	}
	
	public boolean isTableExist(String tableName){
				
		try{
			
			boolean isExist = false;
						
			String condition = "table_name=" + "'" + tableName + "'";
			
			ResultSet rs = getOneResult("information_schema.tables", "table_name", condition);
			   
		    if(rs.next()){
            
		        String result = rs.getString(1);
		    
		        if(tableName.toLowerCase().equals(result.toLowerCase())){
		    	    isExist = true;
		        }		 
		    
		    }
		    
		    closeCurrentConnection();
		    
		    return isExist;
		    
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("表"+tableName+"存在检测失败!");
			
			return false;			
		}
		
	}
	
	public boolean updateColumn(String tableName, Vector<String> columnNameVec, Vector<String> valVec, String condition){
		
		if(null == tableName || columnNameVec.size() == 0 || valVec.size() == 0){			
			return true;			
		}
		
		try{
			
			int commitNum = 1000;
			
			if(false == connect()){
			    return false;
		    }
			
			String sqlPrefix = "UPDATE " + tableName + " SET ";
						
			PreparedStatement ps = con.prepareStatement("USE TEST");			
			 
			currentPs = ps;
			
			ps.addBatch();
			 
			con.setAutoCommit(false);
			
			StringBuffer sb = new StringBuffer("");
				
			for(int j=0;j<valVec.size();j++){
				
				sb.append(sqlPrefix);
				
				String columnName = columnNameVec.get(j);
					
				String updateVal = valVec.get(j);
					
				sb.append(columnName);
					
				sb.append("=");
					
				if(true == isVarcharColumn(j,columnName)){
						
					updateVal = "'" + updateVal + "'";
				}
												
				sb.append(updateVal);						
					
				if(j!= valVec.size()-1)
				{
					sb.append(",");
				}
				
				sb.append(" WHERE ");
				
				String whereCondition = condition;
				
				sb.append(whereCondition);
					
				String sql = sb.toString();
				
				ps.addBatch(sql);
					
				sb.setLength(0);
					
				if(j % commitNum == 1 && j !=1 ){
						
						 ps.executeBatch();
						 
						 con.commit();
				}
			}
			
			ps.executeBatch();
			
			con.commit();
			
			con.setAutoCommit(true);
			
			
			if(false == disconnect()){
			    	return false;
			}
			
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println("更新表"+ tableName +"数据失败！");
		}
		
		
		return true;
	}
	
	public int getCountFromTable(String tableName, String condition){
		int count = 0;
		
		if(tableName == null){
			return count;
		}
		
		try{
			if(false == connect()){
			    return count;
		    }
			
			StringBuffer sb = new StringBuffer("");
			
			sb.append("SELECT COUNT(*) FROM ");
			
			sb.append(tableName);
			
			if(null != condition){
				sb.append(" ");
				sb.append(condition);
			}
			
            String sql = sb.toString();
			
		    PreparedStatement ps = con.prepareStatement(sql);
			
			currentPs = ps;
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
	            
		        String result = rs.getString(1);
		    
	            count = Integer.parseInt(result); 		    
		    }
			
			
			if(false == disconnect()){
		    	return 0;
		    }
			
		}
		catch(Exception e){
			closeCurrentConnection();
			e.printStackTrace();
			return 0;
		}
		
		
		return count;
	}
	
    public ResultSet getOrderResult(String tableName,String orderCol,boolean isAsc){
		
		ResultSet rs = null;
		
        try{	
			
			if(false == connect()){
				return null;
			}
			
			StringBuffer sb = new StringBuffer("SELECT * FROM " + tableName + " LIMIT 2000 ORDER BY " + orderCol);
			
			if(!isAsc){
				sb.append(" DESC");
			}
			
			String sql = sb.toString();
			
		    PreparedStatement ps = con.prepareStatement(sql);
		    
		    currentPs = ps;
		    		    
		    rs = ps.executeQuery();		    
		
		}
		catch(Exception e){
			
			closeCurrentConnection();
			
			System.out.println(orderCol + "排序失败！");			
			
			return null;			
		}
		
		return rs;  		
		
	}

}
