package com.xn.alex.data.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.xn.alex.data.common.CommonConfig.CURRENT_ACTION;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.database.DatabaseSpecialAction;
import com.xn.alex.data.process.MenuItemDisable;
import com.xn.alex.data.window.MainWindow;

public class CloseAction extends WindowAction{
	
	private static CloseAction closeActionHandler = null;
	
	 private CloseAction(){
		 
	 }
	 
	 public static CloseAction Instance(){
		 
		 if(null == closeActionHandler){
			 closeActionHandler = new CloseAction();
		 }
		 
		 return closeActionHandler;		 
	 }
	 
	 public void takeAction(){
		 
		 closeSlectedFile(MainWindow.Instance().getCurrentNode());
     	
     	 System.out.println("关闭文件成功！");
     	 
	 }
	 
	 private void closeSlectedFile(DefaultMutableTreeNode closeNode){
	    	
	    	if(closeNode == MainWindow.Instance().getRootNode()){
	    		
	    		MainWindow.Instance().setCurrentNode(MainWindow.Instance().getRootNode());	    		
	    		
	    		MainWindow.Instance().getTree().updateUI();
	    		
	    		return;
	    	}
	    	
	    	closeTreeViewInFrame(closeNode);
	    	
	    	MainWindow.setCurrentAct(CURRENT_ACTION.DB_OPERATION);	    	
	    	
	    	deleteRelatedDataInDataBase(closeNode);
	    	
	    	Vector<String> columnVec = new Vector<String>();
	    	
	    	MainWindow.Instance().getColumnNames(columnVec);
	    	
	    	DefaultTableModel dtm = (DefaultTableModel) MainWindow.Instance().getTable().getModel();
	    	
	    	dtm.setDataVector(null, columnVec);
		    
		    dtm.fireTableStructureChanged();
		    
		    dtm.fireTableDataChanged();
		    
		    MainWindow.setCurrentAct(CURRENT_ACTION.NONE);
		    
		    MenuItemDisable.Instance().disableSecondColumnMenu();
		    
		    MenuItemDisable.Instance().disableThirdColumnMenu();
	    	
	    }
	 
	 private void closeTreeViewInFrame(DefaultMutableTreeNode closeNode){
	    	
	    	if(closeNode == MainWindow.Instance().getRootNode()){
	    		
	    		MainWindow.Instance().setCurrentNode(MainWindow.Instance().getRootNode());	  
	    		
	    		MainWindow.Instance().getTree().updateUI();
	    		
	    		return;
	    	}
	    	
	    	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) closeNode.getParent();
	    	
	    	if(closeNode.isLeaf()&&(parentNode.getChildCount()>1)){
	    		
	    		parentNode.remove(closeNode);
	    		
	    		MainWindow.Instance().setCurrentNode(parentNode);	    		
	    		
	    		MainWindow.Instance().getTree().updateUI();    		
	    		
	    		return;    		
	    	}    	
	    	
	    	parentNode.remove(closeNode);
	    	
	    	closeNode = parentNode;
	    	
	    	closeTreeViewInFrame(closeNode);
	    	
	    }
	 
	 private void deleteRelatedDataInDataBase(DefaultMutableTreeNode closeNode){
	    	
	    	List<String> affectedFileList = new ArrayList<String>();
	    	
	    	getAffectedFileList(closeNode,affectedFileList);
	    	
	    	for(int i=0;i<affectedFileList.size();i++){
	    		
	    		String fileName = affectedFileList.get(i);
	    		
	    		String dataTable = MainWindow.fileNameToTableMap.get(fileName);
	    		
	    		deleteRecordFromtreeNodeToFullPathMap(fileName);
	    		
	    		DatabaseAction.Instance().dropTable(dataTable);
	    		
	    	}
	    	
	    }
	 
	 private void getAffectedFileList(DefaultMutableTreeNode node, List<String> fileList){
 	   	
	    	if(true == node.isLeaf()){
	    		   		
	    		int key = node.hashCode();
	    		
	    		String fullFileName = MainWindow.treeNodeToFullPathMap.get(key);
	   
	    		fileList.add(fullFileName);
	    	}
	    	else{
	    		
	    		int childCount = node.getChildCount();
	    		
	    		for(int i=0; i<childCount; i++){
	    			
	    			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
	    			
	    			getAffectedFileList(childNode, fileList);
	    			
	    		}
	    	
	    	}
	 }
	 
	 private void deleteRecordFromtreeNodeToFullPathMap(String fileName){
	    	
	    	Iterator<Map.Entry<Integer, String>> it = MainWindow.treeNodeToFullPathMap.entrySet().iterator(); 
	    	
	    	while(it.hasNext()){
	    		
	    		String fullFileName = it.next().getValue();
	    		
	    		if(fullFileName.equals(fileName)){
	    			
	    			it.remove();
	    			
	    			MainWindow.fileNameToTableMap.remove(fullFileName);
	    			
	    			DatabaseSpecialAction.Instance().deleteFromFileToTableTable(fullFileName);
	    			
	    			break;
	    		}
	    		
	    	}
	    	
	    }

}
