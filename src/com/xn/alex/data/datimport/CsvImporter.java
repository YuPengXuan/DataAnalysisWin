package com.xn.alex.data.datimport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.ui.ProgressBar;
import com.xn.alex.data.window.MainWindow;

public class CsvImporter extends  AbstractImporter{

	public CsvImporter(final String fileName){
		super(fileName,FILE_TYPE.CSV_FILE);
	}
	
	public CsvImporter(final String fileName, final String tableName){
		super(fileName,tableName, FILE_TYPE.CSV_FILE);
	}
	@Override
	public List<String> getColumnInfo() throws IOException {
		List<String> columnNames = new ArrayList<String>();
		String fileName = getFileName();
		
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
		return columnNames;
	}

	@Override
	public void load() throws IOException {
		List<String> columnNames = getColumnInfo();

		loadDataIntoDatabase(getTableName(),columnNames);
		
	}
	
	protected boolean loadDataIntoDatabase(final String tableName, final List<String> columnNames){
		
		final String fileName = getFileName();
		
		
		if(MissColumnIndToChnNameMap.size() != 0){
			
			setNeedChooseColType(true);
						
			NewColumnHandler colHandTh = new NewColumnHandler(columnNames, MissColumnIndToChnNameMap, missingColumnIndexList, this, tableName);
			
			colHandTh.setFileType(getFileType());
			colHandTh.run();
			
			if(!colHandTh.isFinished()){
				return true;
			}
		}
		
		
		setNeedChooseColType(false);
	    
		final ProgressBar progresBar = new ProgressBar(MainWindow.Instance(),"加载文件"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1609711263573769118L;

			@Override
			public void onRun() throws Exception {
				setProgress(0,"开始加载文件:" + fileName);
				
				createTable(columnNames,tableName);
				setProgress(30,200);
				final IDataImport csvImport = DataImportFactory.getDataImport(CommonConfig.FILE_TYPE.CSV_FILE, tableName);
	    		csvImport.setPropertyListener(this);
				if(false == csvImport.parse(fileName)){
					    System.out.println("导入大数据失败.");
					    setProgress(50,"导入数据失败:" + fileName);
					    MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance().getCurrentNode().hashCode());
					    throw new Exception("导入数据失败.");
				 }
				
		         MainWindow.fileNameToTableMap.put(fileName, tableName);		
				setProgress(60,500);
				
				updateMainWindowColumnVec(columnNames);
				
				System.out.println("文件：" + fileName +" 导入数据库成功");
				
				setProgress(100,"导入数据完成。",500);
			}

			@Override
			public void onClose() {
				this.dispose();
			}

			@Override
			public void onException(Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
				this.dispose();
			}
			
		};
		
	    
		
		 
		 
		 return true;
		
	}
	
}
