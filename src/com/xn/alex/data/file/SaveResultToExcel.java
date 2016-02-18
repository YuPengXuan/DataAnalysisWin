package com.xn.alex.data.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class SaveResultToExcel {
	
	 public void saveToFile(){
		 FileNameExtensionFilter filter=new FileNameExtensionFilter("*.xls","xls");
			
			JFileChooser fc=new JFileChooser();
			
			fc.setFileFilter(filter);	
			
			fc.setMultiSelectionEnabled(false);	
			
			int result=fc.showSaveDialog(null);	
			
			if (result==JFileChooser.APPROVE_OPTION) {
				
			   File file=fc.getSelectedFile();
			   
			   if (!file.getPath().endsWith(".xls")) {				
			       file=new File(file.getPath()+".xls");			
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
			       
			       writeSalePlan(fos);
				   
				   fos.flush();			
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
	 
	 private void writeSalePlan(BufferedWriter fos){
			
			//标题行
	        String title1[]={"外呼营销建议"};
	        
	        String title2[]={"","节点编号","人群特征1","人群特征2","人群特征3",
	        		"营销人数","人群占比","命中率F0","误判率F1","成功率F2",
	        		"人群总利润","单次营销利润"};
	        //内容
	      
			
			try {
				
				WritableWorkbook book = Workbook.createWorkbook(new File("营销方案.xls"));
				
				WritableSheet sheet = book.createSheet("第一页", 0);
				
				
				
				//写入内容
	            for(int i=0;i<1;i++){    //title
	                sheet.addCell(new Label(i,0,title1[i])); 
	            }
	            	sheet.addCell(new Label(0,title2.length,"营销建议"));
	            for (int i = 0; i < title2.length; i++) {
					sheet.addCell(new Label(i,1,title2[i]));
				}
	            
	            
	            //写入数据
	            book.write(); 
	            //关闭文件
	            book.close(); 
			} catch (Exception e2) {
				// TODO: handle exception
			}
	 }
	 
}
