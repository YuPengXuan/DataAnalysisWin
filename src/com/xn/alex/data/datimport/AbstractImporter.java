package com.xn.alex.data.datimport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.common.ConfigParser.DataColumnInfo;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.database.DatabaseSpecialAction;
import com.xn.alex.data.window.MainWindow;

public abstract class AbstractImporter{
	
	private String fileName = null;
	
	private FILE_TYPE fileType = FILE_TYPE.INVALID_FILE;
	
	protected List<Integer> missingColumnIndexList = new ArrayList<Integer>();
	
	private String tableName = null;
	
	private boolean isNeedChooseColType;
	
	private boolean isLargeFile = false;
	
	protected Map<Integer, String> MissColumnIndToChnNameMap = new HashMap<Integer, String>();

	
	public AbstractImporter(final String fileName, final FILE_TYPE fileType){
		this.fileName = fileName;
		this.fileType = fileType;
		isNeedChooseColType = false;
	}
	
	public AbstractImporter(final String fileName, final String tableName, final FILE_TYPE fileType){
		this.fileName = fileName;
		this.fileType = fileType;
		this.tableName = tableName;
		isNeedChooseColType = false;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FILE_TYPE getFileType() {
		return fileType;
	}

	public void setFileType(FILE_TYPE fileType) {
		this.fileType = fileType;
	}

	public List<Integer> getMissingColumnIndexList() {
		return missingColumnIndexList;
	}

	public void setMissingColumnIndexList(List<Integer> missingColumnIndexList) {
		this.missingColumnIndexList = missingColumnIndexList;
	}

	public String getTableName() {
        
        if(tableName != null){
        	
        	return tableName;
        		                 	 
        }
        else{
        	
        	int tableId = DatabaseSpecialAction.Instance().getAvailableTableId();
        	
        	tableName = DatabaseConstant.DATABASE_TABLE_PREFIX + tableId;
        	
        	MainWindow.fileNameToTableMap.put(fileName, tableName);
        	
        	if(false == DatabaseSpecialAction.Instance().insertIntoFileToTableTable(fileName,tableName)){
        		
        		return null;
        		
            }
        	
        }
        
        return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isNeedChooseColType() {
		return isNeedChooseColType;
	}

	public void setNeedChooseColType(boolean isNeedChooseColType) {
		this.isNeedChooseColType = isNeedChooseColType;
	}

	public boolean isLargeFile() {
		return isLargeFile;
	}

	public void setLargeFile(boolean isLargeFile) {
		this.isLargeFile = isLargeFile;
	}

	public Map<Integer, String> getMissColumnIndToChnNameMap() {
		return MissColumnIndToChnNameMap;
	}

	public void setMissColumnIndToChnNameMap(
			Map<Integer, String> missColumnIndToChnNameMap) {
		MissColumnIndToChnNameMap = missColumnIndToChnNameMap;
	}

	public abstract List<String> getColumnInfo() throws IOException;
	
	public abstract void load() throws IOException;
	
	protected abstract boolean loadDataIntoDatabase(final String tableName, List<String> columnNames);
	
	
	protected void updateMainWindowColumnVec(List<String> columnEnNameVec){
		
		Vector<String> ColumnChnVec = MainWindow.getJtableColumnVec();
		
		ColumnChnVec.clear();
		
		for(int i=0;i<columnEnNameVec.size();i++){
			
			String chNColumnName = ConfigParser.columnInfoMap.get(columnEnNameVec.get(i)).mExcelColumnName;
			
			if(null == chNColumnName){
				chNColumnName = columnEnNameVec.get(i);
			}
			
			ColumnChnVec.add(chNColumnName);			
		}
		
	}
	
	public static AbstractImporter getImporter(final String fileName, final String tableName,final FILE_TYPE fileType){
		AbstractImporter importer = null;
		try{
		switch(fileType){
		    case DBF_FILE:
		    	return null; 
		    		    
		    case XLS_FILE:
				 importer = new XlsImporter(fileName,tableName);			 
				 break;
		    		    
		    case XLSX_FILE:
		    	importer = new XlsxImporter(fileName,tableName);
		    	break;
		    		    
		    case CSV_FILE:
		    	importer = new CsvImporter(fileName,tableName);
		    	break;
		   		    	
		    default:		    	
		    	System.out.println("找不到这个文件："+fileName+"!");
		    	break;		    
		}
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("导入文件 "+ fileName + " 失败！");
		}
		return importer;
	}
	public static AbstractImporter getImporter(final String fileName, final FILE_TYPE fileType){
		AbstractImporter importer = null;
		try{
		switch(fileType){
		    case DBF_FILE:
		    	return null; 
		    		    
		    case XLS_FILE:
				 importer = new XlsImporter(fileName);			 
				 break;
		    		    
		    case XLSX_FILE:
		    	importer = new XlsxImporter(fileName);
		    	break;
		    		    
		    case CSV_FILE:
		    	importer = new CsvImporter(fileName);
		    	break;
		   		    	
		    default:		    	
		    	System.out.println("找不到这个文件："+fileName+"!");
		    	break;		    
		}
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("导入文件 "+ fileName + " 失败！");
		}
		return importer;
	}
	
	protected void  commomLoadIntoDb(String fileName, List<String> columnNames){
		if(columnNames.size() == 0){
			return;
		}
        
        checkColumnNames();
        
        String primaryKey = "customerID";
        
        if(columnNames.contains(primaryKey) == false){
			
			System.out.println("文件加载失败！");
			return;
		}        
                
        String tableName = getTableName();
        
        if(null == tableName){
        	
        	return;
        }		
		
		if(columnNames.size() == 0){
			
			MainWindow.fileNameToTableMap.remove(fileName);
			
			return;
		}
		
		List<DataColumnInfo> columnNameToType = ConfigParser.Instance().getColumnNameToTypeMap(columnNames);
		
		DatabaseAction.Instance().createTable(tableName, columnNameToType, primaryKey);
		
		if(true == loadDataIntoDatabase(tableName, columnNames)){
			
			MainWindow.fileNameToTableMap.put(fileName, tableName);
			
			updateMainWindowColumnVec(columnNames);
			
			System.out.println("文件：" + fileName +" 导入数据库成功");						
			
		}
		
	}
	
	private void checkColumnNames(){				
		
		if(missingColumnIndexList.size()!=0){
			
			String outPutMsg = missingColumnIndexList.toString() + "列在配置文件中缺失，将不会导入数据";
			
			System.out.println(outPutMsg);
			
			JOptionPane.showMessageDialog(null,outPutMsg,"错误信息",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	protected List<Integer> getNumericListColumnIndex(List<String> columnNameVec){
		
		List<Integer> numericIndexList = new ArrayList<Integer>();
		
		for(int i=0;i<columnNameVec.size();i++){
			
			String columnName = columnNameVec.get(i);
			
			if(false == ConfigParser.columnInfoMap.get(columnName).mValueType.contains("VARCHAR")){
				
				numericIndexList.add(i);
				
			}
			
		}
			
		return numericIndexList;
				
	}
	
	
	public void loadDataForTree(){
		FILE_TYPE type = getFileType();
		String fileName = getFileName();
		
		File xlsxFile = new File(fileName);
		long fileSize = xlsxFile.length();
		
		if(fileSize> 8000000 && type != FILE_TYPE.CSV_FILE){			
			Vector<String> columnNames = new Vector<String>();
			
			if(false == HugeDataImport.Instance().importData(fileName, getTableName(), columnNames, missingColumnIndexList)){
				System.out.println("导入大数据失败");
				return;
			}
			
			System.out.print("导入数据成功");
			return;
		}
		try{
			List<String> columnNames = getColumnInfo();
		
		    String primaryKey = "customerID";	
		    List<DataColumnInfo> columnNameToType  = ConfigParser.Instance().getColumnNameToTypeMap(columnNames);		
		    DatabaseAction.Instance().createTable(getTableName(), columnNameToType, primaryKey);
		    
		    if(false == loadDataIntoDatabase(getTableName(), columnNames)){
		    	JOptionPane.showMessageDialog(null,"导入数据失败","错误信息",JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    
		    System.out.print("导入数据成功");
		    
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("导入文件 "+ fileName + " 失败！");
		}
	}
	
	protected void createTable(List<String> columnNames, String tableName) throws Exception{
		String primaryKey = "customerID";
		
		List<DataColumnInfo> columnNameToType = ConfigParser.Instance().getColumnNameToTypeMap(columnNames);
		
		if(false == DatabaseAction.Instance().createTable(tableName, columnNameToType, primaryKey)){
			throw new Exception("创建数据库表失败:" + tableName);
		}
	}
}
