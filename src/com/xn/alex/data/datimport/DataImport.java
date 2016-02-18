package com.xn.alex.data.datimport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.database.DatabaseSpecialAction;
import com.xn.alex.data.window.MainWindow;

public class DataImport {
	
	private String m_fileName = null;
	
	private FILE_TYPE m_fileType = FILE_TYPE.INVALID_FILE;
	
	private List<Integer> missingColumnIndexList = new ArrayList<Integer>();
	
	private InputStream is = null;
	
	private XSSFWorkbook xssWorkBook = null;
	
	private HSSFWorkbook xlsWorkBook = null;
	
	private String m_table_Name = null;
	
	public String getTableName() {
		return m_table_Name;
	}


	public void setTableName(String table_Name) {
		this.m_table_Name = table_Name;
	}


	public String getfileName() {
		
		return m_fileName;
	}


	public void setfileName(String m_fileName) {
		this.m_fileName = m_fileName;
	}
	
	public DataImport(String fileName, FILE_TYPE fileType){
		m_fileName = fileName;
		m_fileType = fileType;
	}
	
	public DataImport(String fileName, String tableName, FILE_TYPE fileType){
		m_fileName = fileName;
		m_table_Name = tableName;
		m_fileType = fileType;
	}
		
	
	public FILE_TYPE getFileType() {
		return m_fileType;
	}

	public void setFileType(FILE_TYPE m_fileType) {
		this.m_fileType = m_fileType;
	}
	
	public void loadDataForTree(){
		
		FILE_TYPE type = getFileType();
		String fileName = getfileName();
		
		Vector<String> columnNames = new Vector<String>();	
		try{
		switch(type){
		    case DBF_FILE:
		    	break;
		    
		    case XLS_FILE:        
		         getColumnXlsNames(columnNames);		 
		         break;
		    
		    case XLSX_FILE:
		    	getColumnXlsxNames(columnNames);	
		    	break;
		    	
		    default:		    	
		    	System.out.println("�Ҳ�������ļ���"+fileName+"!");
		    	break;		    
		    }
		
		    String primaryKey = "customerID";	
            Map<String, String> columnNameToTypeMap = ConfigParser.Instance().getColumnNameToTypeMap(columnNames);		
		    DatabaseAction.Instance().createTable(getTableName(), columnNameToTypeMap, primaryKey);
		    
		    if(false == loadDataIntoDatabase(getTableName(), columnNames)){
		    	JOptionPane.showMessageDialog(null,"��������ʧ��","������Ϣ",JOptionPane.ERROR_MESSAGE);
		    	return;
		    }
		    
		    System.out.print("�������ݳɹ�");
		    
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("�����ļ� "+ fileName + " ʧ�ܣ�");
		}
		
		
	}

	public void run(){
		
		FILE_TYPE type = getFileType();
		String fileName = getfileName();
		
		
		try{
		switch(type){
		    case DBF_FILE:
		    	loadDbfFile(fileName);
		    	break;
		    
		    case XLS_FILE:
				 loadXlsFile(fileName);			 
		         break;
		    
		    case XLSX_FILE:
		    	loadXlsxFile(fileName);
		    	break;
		    	
		    default:		    	
		    	System.out.println("�Ҳ�������ļ���"+fileName+"!");
		    	break;		    
		}
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("�����ļ� "+ fileName + " ʧ�ܣ�");
		}
		
	}
	
	public void loadXlsxFile(String fileName) throws IOException{
		
		Vector<String> columnNames = new Vector<String>();
		
		//columnNames.clear();
		
		getColumnXlsxNames(columnNames);
		
		commomLoadIntoDb(fileName, columnNames);
		
	}
	
	
	public void loadDbfFile(String fileName){
		
	}
	
	
	private String getTableName(String fileName){
        String tableName = MainWindow.fileNameToTableMap.get(fileName);
        
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
	
	public void loadXlsFile(String fileName) throws IOException{
		
        Vector<String> columnNames = new Vector<String>();
        
        //columnNames.clear();
        
        getColumnXlsNames(columnNames);
        
        commomLoadIntoDb(fileName, columnNames);
		
	}
	
	private void  commomLoadIntoDb(String fileName, Vector<String> columnNames){
		
		if(columnNames.size() == 0){
			return;
		}
        
        checkColumnNames();
        
        String primaryKey = "customerID";
        
        if(columnNames.contains(primaryKey) == false){
			
			System.out.println("�ļ�����ʧ�ܣ�");
			return;
		}        
                
        String tableName = getTableName(fileName);
        
        if(null == tableName){
        	
        	return;
        }		
		
		if(columnNames.size() == 0){
			
			MainWindow.fileNameToTableMap.remove(fileName);
			
			return;
		}
		
		Map<String, String> columnNameToTypeMap = ConfigParser.Instance().getColumnNameToTypeMap(columnNames);
		
		DatabaseAction.Instance().createTable(tableName, columnNameToTypeMap, primaryKey);
		
		if(true == loadDataIntoDatabase(tableName, columnNames)){
			
			MainWindow.fileNameToTableMap.put(fileName, tableName);
			
			updateMainWindowColumnVec(columnNames);
			
			System.out.println("�ļ���" + fileName +" �������ݿ�ɹ�");						
			
		}
		
	}
	
	private void updateMainWindowColumnVec(Vector<String> columnEnNameVec){
		
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
	
	
	private InputStream getInstanceOfIs() throws FileNotFoundException{
		
		if(null == is){				
		    is = new FileInputStream(getfileName());		    
		}
		
		return is;		
	}
	
	
	
	private void getColumnXlsxNames(Vector<String> resultColumns) throws IOException{
		
		InputStream localIs = getInstanceOfIs();
			
		
		if((null == xssWorkBook)&&(FILE_TYPE.XLSX_FILE == getFileType())){
			
		    xssWorkBook = new XSSFWorkbook(localIs);
		    
		}
								
		Sheet sheet = xssWorkBook.getSheetAt(0);
			
		if(sheet == null)
				return;
			
		Row row = sheet.getRow(0);
			
		if(row == null){
				
			System.out.println("�ļ���ʽ���󣬵�һ����������Ϊ�գ�");
				
			return;				
		}
		
		int cellNum = row.getLastCellNum();
		
		for(int i=0;i<cellNum;i++){
			
			if(row.getCell(i) == null){
				continue;
			}
			
		    Cell cell = row.getCell(i);
			
		    String columnNameCh = cell.toString();
		    
		    String columnNameEn = ConfigParser.chnToEnColumnName.get(columnNameCh);
		    
		    if(null != columnNameEn){
		        resultColumns.add(columnNameEn);
		    }
		    else{
		    	
		    	missingColumnIndexList.add(i);
		    }
		}
			
	}
	
	private void getColumnXlsNames(Vector<String> resultColumns) throws IOException{
		
		InputStream localIs = getInstanceOfIs();
		
		if((null == xlsWorkBook)&&(FILE_TYPE.XLS_FILE == getFileType())){
		
		xlsWorkBook = new HSSFWorkbook(localIs);
		
	    }					
		
		Sheet sheet = xlsWorkBook.getSheetAt(0);
		
		if(sheet == null)
			    return;
		
	    Row row = sheet.getRow(0);
		
		if(row == null){
			
			System.out.println("�ļ���ʽ���󣬵�һ����������Ϊ�գ�");
			
			return;	
		}
		
       int cellNum = row.getLastCellNum();
		
		for(int i=0;i<cellNum;i++){
			
			if(row.getCell(i) == null){
				continue;
			}

			 Cell cell = row.getCell(i);
				
			 String columnNameCh = cell.toString();
			    
			 String columnNameEn = ConfigParser.chnToEnColumnName.get(columnNameCh);
			    
			 if(null != columnNameEn){
			      resultColumns.add(columnNameEn);
			     			      
			  }
			  else{
			    	
			      missingColumnIndexList.add(i);
			  }
		}

	
	}
	
	private void checkColumnNames(){				
		
		if(missingColumnIndexList.size()!=0){
			
			String outPutMsg = missingColumnIndexList.toString() + "���������ļ���ȱʧ�������ᵼ������";
			
			System.out.println(outPutMsg);
			
			JOptionPane.showMessageDialog(null,outPutMsg,"������Ϣ",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private boolean loadDataIntoDatabase(String tableName, Vector<String> columnNames){
		
		Vector<Vector<String>> commitValueVec = MainWindow.getJtableValueVec();
		
		commitValueVec.clear();
		
		FILE_TYPE fileType = getFileType();
		
		List<Integer> numbericIndexList = getNumericListColumnIndex(columnNames);
		
		int sheetNum = 0;
		
		int obsoleteLine = 0;
		
		switch(fileType){			
			case XLSX_FILE:
				sheetNum = xssWorkBook.getNumberOfSheets();
				break;
			case XLS_FILE:
				sheetNum = xlsWorkBook.getNumberOfSheets();
				break;
			case DBF_FILE:
				break;
				
			default:
				return false;
		}
					
			for(int i = 0; i<sheetNum; i++){
				
				Sheet sheet = null;
				
				switch(fileType){			
				case XLSX_FILE:
					sheet = xssWorkBook.getSheetAt(i);
					break;
				case XLS_FILE:
					sheet = xlsWorkBook.getSheetAt(i);
					break;
				case DBF_FILE:
					break;
					
				default:
					continue;
			}
				
				
				if(sheet == null)
				    continue;			
								
				for(int rowNum=1;rowNum<=sheet.getLastRowNum();rowNum++){
					
					Row row = sheet.getRow(rowNum);
					if(null == row || missingColumnIndexList.contains(rowNum)){
						continue;
					}
					
					Vector<String> tmpVec = new Vector<String>();
					
					boolean isAddThisRow = true;
					
					int cellNum = row.getLastCellNum();
					
					for(int j=0;j<cellNum;j++){
					
					    Cell cell = row.getCell(j);
					    
					    String cellValue = null;
					    
					    if(null == cell || "#NULL!".equals(cell.toString())){
					    	
					    	//String columnName = columnNames.get(i);
					    	
					    	//String defVal = ConfigParser.columnInfoMap.get(columnName).mDefValue;
					    	
					    	//cellValue = defVal;
					    	
					    	//cellValue = "-1";
					    	cellValue = String.valueOf(CommonConfig.IVALID_VALUE);
					    	
					    }
					    else{
					    	
					    	if(true == numbericIndexList.contains(j) && cell.getCellType() != Cell.CELL_TYPE_NUMERIC){
					    		
					    		isAddThisRow = false;
					    		
					    		obsoleteLine++;
					    		
					    		break;
					    		
					    	}
					    
					        cellValue = cell.toString();
					     
					    }					    
					   
					    tmpVec.add(cellValue);
					   					  
					}
					
					if(true == isAddThisRow){
					
					    commitValueVec.add(tmpVec);
					    
					}
					
					isAddThisRow = true;
					
				}
				
			}
					
		DatabaseAction.Instance().insertTable(tableName, columnNames, commitValueVec);
		
		if(obsoleteLine > 0){
		
		  System.out.println(obsoleteLine + "�в����ϸ�ʽ�����ݽ��ᱻ������");
		  
		}
		
		return true;		
	}
	
	private List<Integer> getNumericListColumnIndex(Vector<String> columnNameVec){
		
		List<Integer> numericIndexList = new ArrayList<Integer>();
		
		for(int i=0;i<columnNameVec.size();i++){
			
			String columnName = columnNameVec.get(i);
			
			if(false == ConfigParser.columnInfoMap.get(columnName).mValueType.contains("VARCHAR")){
				
				numericIndexList.add(i);
				
			}
			
		}
			
		return numericIndexList;
				
	}

}
