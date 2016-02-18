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
			           //�ļ������� �򴴽�һ��					
			           file.createNewFile();				
			       }
			       fw = new FileWriter(file);
			       
			       fos=new BufferedWriter(fw);	
			       
			       writeSalePlan(fos);
				   
				   fos.flush();			
			    } catch (IOException e) {				
				    System.err.println("�ļ�����ʧ�ܣ�");	
				    
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
			
			//������
	        String title1[]={"���Ӫ������"};
	        
	        String title2[]={"","�ڵ���","��Ⱥ����1","��Ⱥ����2","��Ⱥ����3",
	        		"Ӫ������","��Ⱥռ��","������F0","������F1","�ɹ���F2",
	        		"��Ⱥ������","����Ӫ������"};
	        //����
	      
			
			try {
				
				WritableWorkbook book = Workbook.createWorkbook(new File("Ӫ������.xls"));
				
				WritableSheet sheet = book.createSheet("��һҳ", 0);
				
				
				
				//д������
	            for(int i=0;i<1;i++){    //title
	                sheet.addCell(new Label(i,0,title1[i])); 
	            }
	            	sheet.addCell(new Label(0,title2.length,"Ӫ������"));
	            for (int i = 0; i < title2.length; i++) {
					sheet.addCell(new Label(i,1,title2[i]));
				}
	            
	            
	            //д������
	            book.write(); 
	            //�ر��ļ�
	            book.close(); 
			} catch (Exception e2) {
				// TODO: handle exception
			}
	 }
	 
}
