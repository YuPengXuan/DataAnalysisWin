package com.xn.alex.data.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

    public class ExportRuleAction extends WindowAction {
	
    private static ExportRuleAction exportRuleActionHandler = null;
		
	private ExportRuleAction(){
		 
	 }
	 
	 public static ExportRuleAction Instance(){
		 
		 if(null == exportRuleActionHandler){
			 exportRuleActionHandler = new ExportRuleAction();
		 }
		 
		 return exportRuleActionHandler;		 
	 }
	 
	 public void takeAction(){
		 
		 treeNodeResultObj resultTree = GenerateTreeByLeaf.getResultNode();
		 
		 if(null == resultTree){
			 JOptionPane.showMessageDialog(null,"没有规则可以导出","错误信息",JOptionPane.ERROR_MESSAGE);
			 return;
		 }
		 
		 saveToFile();
	 }
	 
	 private void saveToFile(){
		 FileNameExtensionFilter filter=new FileNameExtensionFilter("*.sql","sql");
			
			JFileChooser fc=new JFileChooser();
			
			fc.setFileFilter(filter);	
			
			fc.setMultiSelectionEnabled(false);	
			
			int result=fc.showSaveDialog(null);	
			
			if (result==JFileChooser.APPROVE_OPTION) {
				
			   File file=fc.getSelectedFile();
			   
			   if (!file.getPath().endsWith(".sql")) {				
			       file=new File(file.getPath()+".sql");			
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
			       
			       fos.write("\r\n");
			       
			       writeTreeNodeSqlToFile(fos);
				   //fos.write("文件内容");
				   
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
	 
	 private void writeTreeNodeSqlToFile(BufferedWriter fos){
		 
		 treeNodeResultObj treeNode = GenerateTreeByLeaf.getResultNode();
		 
		 if(null == treeNode){
			 return;
		 }		 
		 
		 loopAndWriteTreeNode(treeNode, fos);
		 
	 }
	 
	 private void loopAndWriteTreeNode(treeNodeResultObj obj, BufferedWriter fos){
		 try{
		     if(null == obj){
			     return;
		     }
		 
		     if(obj.isLeaf == true){
			     fos.write("/* Node "+obj.nodeNumber+" */.\r\n");
			     fos.write("UPDATE <TABLE>\r\n");
			     fos.write("        SET nod_001 = "+obj.nodeNumber+",	pre_001 = "+obj.Prediction+",	prb_001 = "+obj.Probability+"\r\n");
			     fos.write(obj.condition+"\r\n");
			     return;
		     }
		     
		     if(obj.childNodeVec == null || obj.childNodeVec.size() == 0){
		    	 return;
		     }
		     
		     for(int i=0;i<obj.childNodeVec.size();i++){
		    	 treeNodeResultObj tmpObj = obj.childNodeVec.get(i);
		    	 
		    	 loopAndWriteTreeNode(tmpObj, fos);
		     }
	     } catch (IOException e) {				
		    System.err.println("文件创建失败：");	
		    
		    e.printStackTrace();
		    
		}		 
	 }

}
