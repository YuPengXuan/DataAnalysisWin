package com.xn.alex.data.datimport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import com.xn.ales.data.datimport.csv.DataImportFactory;
import com.xn.ales.data.datimport.csv.IDataImport;
import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.ui.ProgressBar;
import com.xn.alex.data.window.MainWindow;
import com.xn.alex.encode.FileCharsetDetector;

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
		
		InputStreamReader fr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		
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
		try{
		
		String fileName = getFileName();
		File tmpFile = new File(fileName);
		FileCharsetDetector dec = new FileCharsetDetector();
		String encode = dec.guessFileEncoding(tmpFile);
		
		if("GBK".equals(encode)){			
			transformFileFromGBKToUTF8(fileName);
		}
						
		List<String> columnNames = getColumnInfo();

		loadDataIntoDatabase(getTableName(),columnNames);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("载入文件失败！");
			return;
		}
		
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
				final IDataImport csvImport = DataImportFactory.getDataImport(CommonConfig.FILE_TYPE.CSV_FILE, tableName);
	    		csvImport.setPropertyListener(this);
				if(false == csvImport.parse(fileName)){
					    System.out.println("导入大数据失败.");
					    setProgress(50,"导入数据失败:" + fileName);
					    MainWindow.treeNodeToFullPathMap.remove(MainWindow.Instance().getCurrentNode().hashCode());
					    throw new Exception("导入数据失败.");
				 }
				
				if(!tableName.equals(DatabaseConstant.TREE_DATA_IMP_TABLE)){
				
			    MainWindow.fileNameToTableMap.put(fileName, tableName);
			    
				}
				
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
	
	public boolean transformFileFromGBKToUTF8(String fileName){
    	try {
    		
    	final String userHomeDir = System.getProperty("user.home");
		String tmpDirPath = userHomeDir + File.separator + ".dataanalysis" + File.separator + "tmp";
		File tmpDir = new File(tmpDirPath);
		if(false == tmpDir.exists()){
			tmpDir.mkdirs();
		}
				
    	String newFileName = tmpDirPath + File.separator + "tmp_utf8.csv";
    	
    	File newFile = new File(newFileName);
    	
    	File oldFile = new File(fileName);
    	
    	if(newFile.exists()){
    		newFile.delete();
    	}
    	
		FileUtils.writeLines(newFile, "UTF-8", FileUtils.readLines(oldFile, "GBK"));
		
		//setFileName(newFileName);
		
		FileUtils.copyFile(oldFile, new File(fileName + ".bak"));
		
		FileUtils.copyFile(newFile, new File(fileName));
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
    	
		return true;
    }
	
	public String codeString(String fileName) throws Exception{
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        byte[] head = new byte[3];
        bin.read(head);
        String code = null;
         
        if(head[0]==-1 && head[1]==-2){
            code = "UTF-16";
        }
        else if(head[0]==-2 && head[1]==-1){
            code = "Unicode";
        }
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65){
            code = "UTF-8";
        }
        else{
             code = "GBK";
        }
        bin.close();        
        return code;
    }		
}


