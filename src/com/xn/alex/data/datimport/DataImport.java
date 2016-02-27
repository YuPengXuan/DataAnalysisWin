package com.xn.alex.data.datimport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.common.ConfigParser.DataColumnInfo;
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
	
	private boolean isNeedChooseColType;
	
	private boolean isLargeFile = false;
	
	private Map<Integer, String> MissColumnIndToChnNameMap = new HashMap<Integer, String>();
	
	public boolean isLargeFile() {
		return isLargeFile;
	}


	public void setLargeFile(boolean isLargeFile) {
		this.isLargeFile = isLargeFile;
	}
	
	public boolean isNeedChooseColType() {
		return isNeedChooseColType;
	}


	public void setNeedChooseColType(boolean isNeedChooseColType) {
		this.isNeedChooseColType = isNeedChooseColType;
	}


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
		isNeedChooseColType = false;
	}
	
	public DataImport(String fileName, String tableName, FILE_TYPE fileType){
		m_fileName = fileName;
		m_table_Name = tableName;
		m_fileType = fileType;
		isNeedChooseColType = false;
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
		
		File xlsxFile = new File(fileName);
		long fileSize = xlsxFile.length();
		
		if(fileSize> 8000000){			
			Vector<String> columnNames = new Vector<String>();
			
			if(false == HugeDataImport.Instance().importData(fileName, getTableName(), columnNames, missingColumnIndexList)){
				System.out.println("导入大数据失败");
				return;
			}
			
			System.out.print("导入数据成功");
			return;
		}
		
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
		    
		    case CSV_FILE:
		    	getCsvColumnNames(columnNames);
		    	break;
		    	
		    default:		    	
		    	System.out.println("找不到这个文件："+fileName+"!");
		    	break;		    
		    }
		
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
	
	private void getCsvColumnNames(Vector<String> columnNames) throws IOException{		
		String fileName = getfileName();
		
		InputStreamReader fr = new InputStreamReader(new FileInputStream(fileName));
		
		BufferedReader br = new BufferedReader(fr);
		
		String line = null;
		
		if((line = br.readLine())!= null){
			//System.out.println("line:"+line);
			
			String tmpArray[] = line.split(",");
			
			for(int i=0;i<tmpArray.length;i++){
				String chnColName = tmpArray[i].trim();
				
				String enColName = ConfigParser.chnToEnColumnName.get(chnColName);
				
				if(enColName != null){
					columnNames.add(enColName);
				}
				else{
					columnNames.add(chnColName);
					
					missingColumnIndexList.add(i);
			    	
			    	MissColumnIndToChnNameMap.put(i, chnColName);
				}
				
			}
						
		}
		
		br.close();
		fr.close();		
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
		    
		    case CSV_FILE:
		    	loadCsvFile(fileName);
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
		
	}
	
	public void loadCsvFile(String fileName) throws IOException{
		
		Vector<String> columnNames = new Vector<String>();
		
		getCsvColumnNames(columnNames);
		
		loadDataIntoDatabase(columnNames);
		
	}
	
	public boolean loadDataIntoDatabase(Vector<String> columnNames){
		
		String fileName = getfileName();
		
		String tableName = getTableName(fileName);
		
		if(MissColumnIndToChnNameMap.size() != 0){
			
			isNeedChooseColType = true;
			
			NewColumnHandler colHandTh = new NewColumnHandler(columnNames, MissColumnIndToChnNameMap, missingColumnIndexList, this, tableName);
			
			colHandTh.setFileType(getFileType());
			//colHandTh.start();
			colHandTh.run();
			
			return true;
			
		}
			
	    isNeedChooseColType = false;
	    
	    final IDataImport csvImport = DataImportFactory.getDataImport(CommonConfig.FILE_TYPE.CSV_FILE, tableName);
	    	    		
		if(false == csvImport.parse(fileName)){
			    System.out.println("导入大数据失败");
			
			    MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance().getCurrentNode().hashCode());
			
			    return false;
		 }
		
         MainWindow.fileNameToTableMap.put(fileName, tableName);
		
		 updateMainWindowColumnVec(columnNames);
		
		 System.out.println("文件：" + fileName +" 导入数据库成功");
		 
		 return true;
		
	}
	
	public void loadXlsxFile(String fileName) throws IOException{
		
		Vector<String> columnNames = new Vector<String>();
		
		//columnNames.clear();
		File xlsxFile = new File(fileName);
		long fileSize = xlsxFile.length();
		
		//if(fileSize>0){
		if(fileSize> 8000000){
			isLargeFile = true;
			
			String tableName = getTableName(fileName);
			
			if(false == getColumnInfoAndData(fileName, columnNames, missingColumnIndexList, MissColumnIndToChnNameMap,tableName)){
				
				System.out.println("导入大数据失败");
				
			    MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance().getCurrentNode().hashCode());
			
			    return;
				
			}
			
			if(MissColumnIndToChnNameMap.size() != 0){
				
				isNeedChooseColType = true;
				
				NewColumnHandler colHandTh = new NewColumnHandler(columnNames, MissColumnIndToChnNameMap, missingColumnIndexList, this, tableName);
				colHandTh.setFileType(getFileType());
				//colHandTh.start();
				colHandTh.run();
				
				return;
				
			}
				
		    isNeedChooseColType = false;
			
			if(false == HugeDataImport.Instance().importData(fileName, tableName, columnNames, missingColumnIndexList)){
				    System.out.println("导入大数据失败");
				
				    MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance().getCurrentNode().hashCode());
				
				    return;
			 }
			
             MainWindow.fileNameToTableMap.put(fileName, tableName);
			
			 updateMainWindowColumnVec(columnNames);
			
			 System.out.println("文件：" + fileName +" 导入数据库成功");	
			 
		}
		else{
			
			isLargeFile = false;
			
			String tableName = getTableName(fileName);
		
		    getColumnXlsxNames(columnNames);
		    
           if(MissColumnIndToChnNameMap.size() != 0){
				
				isNeedChooseColType = true;
				
				NewColumnHandler colHandTh = new NewColumnHandler(columnNames, MissColumnIndToChnNameMap, missingColumnIndexList, this, tableName);
				colHandTh.setFileType(getFileType());
				//colHandTh.start();
				colHandTh.run();
				
				return;
				
			}
		
		    commomLoadIntoDb(fileName, columnNames);
		}
		
	}
	
	public boolean getColumnInfoAndData(String fileName, Vector<String> columnNames, List<Integer> missColumnIndexList, Map<Integer, String> MissColumnIndToChnNameMap,String tableName){
		
		InputStream sheet = null;
		
		boolean isSucc = true;
		
		try{						
			OPCPackage pkg = OPCPackage.open(fileName);			
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();
			
			XMLReader parser =XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			ContentHandler handler = new SaxHeaderParser(sst,columnNames,missColumnIndexList,MissColumnIndToChnNameMap);
			parser.setContentHandler(handler);

			Iterator<InputStream> sheets = r.getSheetsData();
			while(sheets.hasNext()) {
				System.out.println("开始读取列名...:\n");
				sheet = sheets.next();
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
				sheet.close();
				System.out.println("");
			}
		}
		catch(Exception e){
			    if(!"".equals(e.getMessage())){
				    e.printStackTrace();
				    isSucc = false;
			    }
				try {
					sheet.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		}
		
		System.out.println("结束读取列名.\n");
		
		return isSucc;
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
        
        File xlsxFile = new File(fileName);
		long fileSize = xlsxFile.length();
		
		if(fileSize> 8000000){
			isLargeFile = true;
			
			String tableName = getTableName(fileName);
			
			if(false == HugeDataImport.Instance().importData(fileName, tableName, columnNames, missingColumnIndexList)){
				System.out.println("导入大数据失败");
				
				MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance().getCurrentNode().hashCode());
				return;
			}
			
            MainWindow.fileNameToTableMap.put(fileName, tableName);
			
			updateMainWindowColumnVec(columnNames);
			
			System.out.println("文件：" + fileName +" 导入数据库成功");	
		}
		else{
	    isLargeFile = false;
			
        getColumnXlsNames(columnNames);
        
        commomLoadIntoDb(fileName, columnNames);
		}
		
	}
	
	private void  commomLoadIntoDb(String fileName, Vector<String> columnNames){
		
		if(columnNames.size() == 0){
			return;
		}
        
        checkColumnNames();
        
        String primaryKey = "customerID";
        
        if(columnNames.contains(primaryKey) == false){
			
			System.out.println("文件加载失败！");
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
		
		List<DataColumnInfo> columnNameToType = ConfigParser.Instance().getColumnNameToTypeMap(columnNames);
		
		DatabaseAction.Instance().createTable(tableName, columnNameToType, primaryKey);
		
		if(true == loadDataIntoDatabase(tableName, columnNames)){
			
			MainWindow.fileNameToTableMap.put(fileName, tableName);
			
			updateMainWindowColumnVec(columnNames);
			
			System.out.println("文件：" + fileName +" 导入数据库成功");						
			
		}
		
	}
	
	public void updateMainWindowColumnVec(Vector<String> columnEnNameVec){
		
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
			
		    xssWorkBook = new XSSFWorkbook(new BufferedInputStream(localIs));
		    
		}
								
		Sheet sheet = xssWorkBook.getSheetAt(0);
			
		if(sheet == null)
				return;
			
		Row row = sheet.getRow(0);
			
		if(row == null){
				
			System.out.println("文件格式有误，第一行数据列名为空！");
				
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
		    	
		    	MissColumnIndToChnNameMap.put(i, columnNameCh);
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
			
			System.out.println("文件格式有误，第一行数据列名为空！");
			
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
			
			String outPutMsg = missingColumnIndexList.toString() + "列在配置文件中缺失，将不会导入数据";
			
			System.out.println(outPutMsg);
			
			JOptionPane.showMessageDialog(null,outPutMsg,"错误信息",JOptionPane.ERROR_MESSAGE);
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
		
		  System.out.println(obsoleteLine + "行不符合格式的数据将会被丢弃！");
		  
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
