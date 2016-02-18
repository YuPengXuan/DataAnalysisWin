package com.xn.alex.data.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveToTxtFile {
	
	private static SaveToTxtFile saveToTxtFileHandler = null;
	
	private SaveToTxtFile(){
		
	}
	
	public static SaveToTxtFile Instance(){
		if(null == saveToTxtFileHandler){
			saveToTxtFileHandler = new SaveToTxtFile();
		}
		return saveToTxtFileHandler;
		
	}
	
	public void saveContentToFile(Vector<String> saveContentVec){
			 FileNameExtensionFilter filter=new FileNameExtensionFilter("*.txt","txt");
				
				JFileChooser fc=new JFileChooser();
				
				fc.setFileFilter(filter);	
				
				fc.setMultiSelectionEnabled(false);	
				
				int result=fc.showSaveDialog(null);	
				
				if (result==JFileChooser.APPROVE_OPTION) {
					
				   File file=fc.getSelectedFile();
				   
				   if (!file.getPath().endsWith(".txt")) {				
				       file=new File(file.getPath()+".txt");			
				   }		
				   
				   System.out.println("file path="+file.getPath());	
				   
				   FileWriter fw = null;
				   
				   BufferedWriter fos=null;			
				   try {
					   
					   if (!file.exists()) {
				           //文件不存在 则创建一个					
				           file.createNewFile();				
				       }
					   
                       fw = new FileWriter(file);
				       
				       fos=new BufferedWriter(fw);
					   
					   for(int i=0;i<saveContentVec.size();i++){
				        				      					       
				           fos.write(saveContentVec.get(i)+"\r\n");				       
					   
					       fos.flush();
					   }
				    } catch (IOException e) {				
					    System.err.println("文件创建失败：");	
					    
					    e.printStackTrace();
					    
					}finally{				
					    if (fos!=null) {	
					    	
					        try {			        	
						    fos.close();				    
						    } catch (IOException e) {						
						    e.printStackTrace();					
						    }				
					    }	
				    }
			    }
		 }

}
