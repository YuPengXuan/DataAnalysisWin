package com.xn.alex.data.action;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SavePicAction extends WindowAction {
	private static SavePicAction savePicActionHandler = null;
	
	private JLayeredPane currentPane = null;
	
	 private SavePicAction(){
		 
	 }
	 
	 public static SavePicAction Instance(){
		 
		 if(null == savePicActionHandler){
			 savePicActionHandler = new SavePicAction();
		 }
		 
		 return savePicActionHandler;		 
	 }
	 
	 public void clearPreviousVar(){
		 currentPane = null;
	 }
	 
	 public void setCurrentPane(JLayeredPane currentPane){
		 this.currentPane = currentPane;
	 }
	 
	 public void takeAction(){
		 
		 saveToJpgFile();   
		 
	 }
	 
	 private void saveToJpgFile(){
		 
		 BufferedImage image = new BufferedImage(currentPane.getWidth(),currentPane.getHeight(), BufferedImage.TYPE_INT_RGB);
		 
		 Graphics2D g2 =(Graphics2D) image.getGraphics();
		 
		 currentPane.paint(g2);
		 
		 saveToFile(image);
	 }
	 
	 private void saveToFile(BufferedImage bImage){
		 FileNameExtensionFilter filter=new FileNameExtensionFilter("*.jpg","jpg");
			
			JFileChooser fc=new JFileChooser();
			
			fc.setFileFilter(filter);	
			
			fc.setMultiSelectionEnabled(false);	
			
			int result=fc.showSaveDialog(null);	
			
			if (result==JFileChooser.APPROVE_OPTION) {
				
			   File file=fc.getSelectedFile();
			   
			   if (!file.getPath().endsWith(".jpg")) {				
			       file=new File(file.getPath()+".jpg");			
			   }		
			   
			   //System.out.println("file path="+file.getPath());	
			   	
			   try {				
			       if (!file.exists()) {
			           //文件不存在 则创建一个					
			           file.createNewFile();				
			       }
			       
			       ImageIO.write(bImage, "jpeg", file);
				   //fos.write("文件内容");
	
			    } catch (IOException e) {				
				    System.err.println("文件创建失败：");	
				    
				    e.printStackTrace();
				    
				}
		    }
	 }

}
