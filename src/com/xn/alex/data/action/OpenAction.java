package com.xn.alex.data.action;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.datimport.DataExport;
import com.xn.alex.data.datimport.DataImport;
import com.xn.alex.data.process.MenuItemEnable;
import com.xn.alex.data.window.MainWindow;
import com.xn.alex.data.window.UpdateObject;

public class OpenAction extends WindowAction{
	
	 private static OpenAction openActionHandler = null;
	
	 private OpenAction(){
		 
	 }
	 
	 public static OpenAction Instance(){
		 
		 if(null == openActionHandler){
			 openActionHandler = new OpenAction();
		 }
		 
		 return openActionHandler;		 
	 }
	
	 public void takeAction(){
		 loadNewFileAction();
	 }

     private void loadNewFileAction(){
		
		JFileChooser fileChooser = new JFileChooser();
		
		openAFileDialog(fileChooser);
		
		File selectedFile = fileChooser.getSelectedFile();
		
		if(null == selectedFile){
			return;
		}
		
		String fileName = selectedFile.toString();   		
		
		decideWhetherLoadANewFile(fileName);
		
	}
	
    private void openAFileDialog(JFileChooser fileChooser){		
		
		fileChooser.setCurrentDirectory(new File("."));
		
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		final String[][] fileENames = {{".DBF", "数据库文件（*.DBF）",".csv"},
				{".xls","MS-Excel 2003 文件(*.xls)"},
				{".xlsx","MS-Excel 2007 文件(*.xlsx)"},
				{".csv","表格数据文件(*.csv)"}
		};
		
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
    
    public void decideWhetherLoadANewFile(String fileName){
    	
		
		String tableName = MainWindow.fileNameToTableMap.get(fileName);
		
		 if(tableName == null){
			 
			 MainWindow.Instance().setSelectedColumnInds(null);
			 
			 loadANewFile(fileName);
			 
			 return;
		     
		 }
	        	
	     int n = JOptionPane.showConfirmDialog(null, "此项操作会删除现有结果并重新加载原始数据到数据库。确认删除吗?", "确认删除框", JOptionPane.YES_NO_OPTION); 
	     
	     MainWindow.Instance().setSelectedColumnInds(null);
	        	
	     if (n == JOptionPane.NO_OPTION) {
	    	 
	     MainWindow.setCurrentAct(CURRENT_ACTION.DB_OPERATION);
	     //currentAct = CURRENT_ACTION.DB_OPERATION;
	    	 
	     JTable table = MainWindow.Instance().getTable();
	     DataExport dataExport = new DataExport(fileName, table);
	        		
	     dataExport.run();
	     
	     MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
	     //currentAct = CURRENT_ACTION.NONE;
	        		
	     MenuItemEnable.Instance().enableSecondColumnMenu();	     
	     
	     return;				        		

	     }
	     
	     DatabaseAction.Instance().deleteAll(tableName);
	     
	     loadANewFile(fileName);
		
	}
    
    private void loadANewFile(String fullFileName){
		
	    List<UpdateObject> updateObjList = MainWindow.Instance().getUpdateObjList();
	   
		updateObjList.clear();
		
		DefaultMutableTreeNode rootNode = MainWindow.Instance().getRootNode();		
						
		addToTreeNode(fullFileName, rootNode);
		
		MainWindow.treeNodeToFullPathMap.put(MainWindow.Instance().getCurrentNode().hashCode(), fullFileName);
		
		FILE_TYPE fileType = getFileType(fullFileName);
		
		if(FILE_TYPE.INVALID_FILE == fileType){
			System.out.println("文件类型不支持");			
			return;
		}
		System.out.println("开始导入数据...");
		
		DataImport dataImportHandler = new DataImport(fullFileName, fileType);
		dataImportHandler.run();		
		
		if(false == dataImportHandler.isNeedChooseColType()){
		
		    MainWindow.setCurrentAct(CURRENT_ACTION.DB_OPERATION);
		    //currentAct = CURRENT_ACTION.DB_OPERATION;
		
		    JTable table = MainWindow.Instance().getTable();
		    DataExport dataExportHandler = new DataExport(fullFileName,table);
		    dataExportHandler.run();
		    //dataExportHandler.showDataInJTable();
		
		    MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
	        //currentAct = CURRENT_ACTION.NONE;
		    MenuItemEnable.Instance().enableSecondColumnMenu();
		}
		
	}
    
   
    private FILE_TYPE getFileType(String fileName){
	
	int pos = fileName.lastIndexOf(".");
	
	if(-1 == pos){
		return FILE_TYPE.INVALID_FILE;
	}
	
	String fileExtensionName = fileName.substring(pos+1);
	
	if("xlsx".equals(fileExtensionName.toLowerCase())){
		return FILE_TYPE.XLSX_FILE;
	}
	else if("xls".equals(fileExtensionName.toLowerCase())){
		return FILE_TYPE.XLS_FILE;
	}
	else if("dbf".equals(fileExtensionName.toLowerCase())){
		return FILE_TYPE.DBF_FILE;
	}
	else if("csv".equals(fileExtensionName.toLowerCase())){
		return FILE_TYPE.CSV_FILE;
	}
	
	return FILE_TYPE.INVALID_FILE;
}
 public void addToTreeNode(String pathName, DefaultMutableTreeNode parentNode){
		
		DefaultMutableTreeNode node = null;
		
		DefaultMutableTreeNode matchedChiNode = null;
		
		if(null == pathName || "".equals(pathName))
		{
			return;
		}
	
		int pos = pathName.indexOf(File.separator);
		
		String subStr = pathName.substring(pos + 1);
		
		pos = subStr.indexOf(File.separator);
		
		String nodeName = null;
		
		if(-1 == pos){
		    
			nodeName = subStr;
			
			matchedChiNode = getChildTreeNode(nodeName, parentNode);
			
			if(null == matchedChiNode){
				
			    node = new DefaultMutableTreeNode(nodeName);
			    
			    parentNode.add(node);
			    
			    refreshTree(node);
			    
			    MainWindow.Instance().setCurrentNode(node);
			    //currentNode = node;
			    
			}
			else{
				node = matchedChiNode;
			}
			
			return;
		}

		nodeName = subStr.substring(0, pos);
		
		matchedChiNode = getChildTreeNode(nodeName, parentNode);
		
		if(null == matchedChiNode){
		
		    node = new DefaultMutableTreeNode(nodeName);
		    
		    parentNode.add(node);
		    
		    refreshTree(node);
		}
		else{
			node = matchedChiNode;
		}

		String unHandledStr = subStr.substring(pos);
		
		
		addToTreeNode(unHandledStr, node);		
		
	}
    
    protected DefaultMutableTreeNode getChildTreeNode(String nodeName, DefaultMutableTreeNode parentNode){
       	
    	DefaultMutableTreeNode resultNode = null;
    	
    	if(null == parentNode){
    		return resultNode;
    	}
    	
    	int chilNodeNum = parentNode.getChildCount();
    	
        for(int i=0;i<chilNodeNum;i++)
        {   
        	DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
        	String childNodeName = childNode.toString();
        	if(childNodeName.equals(nodeName)){
        		return childNode;
        	}
        	
        }
    	
    	return resultNode;    	
    }
    
    protected void refreshTree(DefaultMutableTreeNode treeNode){
    	
    	JTree tree = MainWindow.Instance().getTree();
    	
    	DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    	
    	TreeNode[] nodes =  model.getPathToRoot(treeNode); 
    	
        TreePath path = new TreePath(nodes); 
        
        tree.scrollPathToVisible(path);
        
        tree.updateUI();
    }
    
    
    
}
