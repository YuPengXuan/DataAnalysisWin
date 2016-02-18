package com.xn.alex.data.action;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.file.LoadTextFile;
import com.xn.alex.data.graphics.TreeDataSheet;
import com.xn.alex.data.process.MenuItemDisable;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;
import com.xn.alex.data.window.MainWindow;

public class ImportRuleAction extends WindowAction {
	
	private static ImportRuleAction importRuleActionHandler = null;
	
	private ROC_TYPE rocType = null;

	private static Vector<String> fileContentByLine = new Vector<String>();
	
	 private ImportRuleAction(){
		 
	 }
	 
	 public ROC_TYPE getRocType() {
			return rocType;
	}

	public void setRocType(ROC_TYPE rocType) {
			this.rocType = rocType;
	}

	 
	 public static ImportRuleAction Instance(){
		 
		 if(null == importRuleActionHandler){
			 importRuleActionHandler = new ImportRuleAction();
		 }
		 
		 return importRuleActionHandler;		 
	 }
	 
	 public void takeAction(){
		 
		 fileContentByLine.clear();
		 
		 JFileChooser fileChooser = new JFileChooser();
			
		 openAFileDialog(fileChooser);
			
		 File selectedFile = fileChooser.getSelectedFile();
			
		 if(null == selectedFile){
			 return;
		 }
		 
		 LoadTextFile.readFileByLines(selectedFile, fileContentByLine);
		 
		 if(fileContentByLine.size() == 0){
			 System.out.println("文件内容为空");
			 return;
		 }
		 
		 GenerateRule(fileContentByLine);
		 
					 
	 }
	 
	 private void openAFileDialog(JFileChooser fileChooser){		
			
			fileChooser.setCurrentDirectory(new File("."));
			
			fileChooser.setAcceptAllFileFilterUsed(false);
			
			final String[][] fileENames = {{".sql", "SQL脚本文件(*.sql)"}};
			
			fileChooser.addChoosableFileFilter(new FileFilter(){
				public boolean accept(File file){
					return true;
				}
				
				public String getDescription(){
					return "";
				}
			});
			
			for (final String[] fileEName : fileENames) {
				   
				   fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
				 
				    public boolean accept(File file) {
				 
				     if (file.getName().endsWith(fileEName[0]) || file.isDirectory()) {
				 
				      return true;
				     }
				 
				     return false;
				    }
				 
				    public String getDescription() {
				 
				     return fileEName[1];
				    }
				 
				   });
				}
				  
				fileChooser.showDialog(null, null);			
	}
	 
	
	private void GenerateRule(Vector<String> contentVec){
		
		GenerateTreeByLeaf genHandler = new GenerateTreeByLeaf();
		
		if(false == genHandler.generateTree(contentVec)){
			
			String errMsg = "文件内容第"+ genHandler.getErrLineNum() +"行格式有误，请检查!";
			
			System.out.println(errMsg);
			
			JOptionPane.showMessageDialog(null,errMsg,"错误信息",JOptionPane.ERROR_MESSAGE);
			
			return;
			
		}
		
		String treeTitle = "";		
		
		switch(rocType){
		    case ROC_EXT:
		    	MenuItemDisable.Instance().disableFourthColumnMenu();
		    	MainWindow.Instance().getMntmRocEx().setEnabled(true);
		    	treeTitle = "外呼营销";
		    	break;
		    case ROC_SMS:
		    	MenuItemDisable.Instance().disableFourthColumnMenu();
		    	MainWindow.Instance().getMntmRoc().setEnabled(true);
		    	treeTitle = "短信营销";
		    	break;
		    case ROC_APP:
		    	MenuItemDisable.Instance().disableFourthColumnMenu();
		    	MainWindow.Instance().getMntmRocApp().setEnabled(true);
		    	treeTitle = "APP客户保有";
		    	break;
		    case ROC_CUST:
		    	MenuItemDisable.Instance().disableFourthColumnMenu();
		    	MainWindow.Instance().getMntmRocKeep().setEnabled(true);
		    	treeTitle = "客户保有";
		    	break;
		    case ROC_WARN:
		    	MenuItemDisable.Instance().disableFourthColumnMenu();
		    	MainWindow.Instance().getMntmRocWarn().setEnabled(true);
		    	treeTitle = "投诉客户预警";
		    	break;
		    default:
		    	System.out.println("ROC类型错误");
		    	return;
		}
				
		treeNodeResultObj resultTree = GenerateTreeByLeaf.getResultNode();
		
		TreeDataSheet.Instance().setPreTitle(treeTitle);
		
		TreeDataSheet.Instance().show(resultTree);
	}
	    
}
