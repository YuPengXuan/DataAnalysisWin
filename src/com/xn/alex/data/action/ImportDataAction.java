package com.xn.alex.data.action;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.xn.alex.data.common.CommonConfig.FILE_TYPE;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.datimport.DataImport;
import com.xn.alex.data.graphics.TreeDataSheet;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

public class ImportDataAction extends WindowAction {
	
     private static ImportDataAction importDataActionHandler = null;
	
	 private ImportDataAction(){
		 
	 }
	 
	 public static ImportDataAction Instance(){
		 
		 if(null == importDataActionHandler){
			 importDataActionHandler = new ImportDataAction();
		 }
		 
		 return importDataActionHandler;		 
	 }
	 
	 public void takeAction(){
				 
		JFileChooser fileChooser = new JFileChooser();
			
		openAFileDialog(fileChooser);
			
		File selectedFile = fileChooser.getSelectedFile();
			
		if(null == selectedFile){
			return;
		}
			
		 String fullFileName = selectedFile.toString(); 
		 
		 FILE_TYPE fileType = getFileType(fullFileName);
			
		 if(FILE_TYPE.INVALID_FILE == fileType){
				System.out.println("文件类型不支持");			
				return;
		 }
		 String tableName = DatabaseConstant.TREE_DATA_IMP_TABLE;	
		 DataImport dataImportHandler = new DataImport(fullFileName, tableName, fileType);
		 dataImportHandler.loadDataForTree();
		 
		 getResultForTreeNode();
		 
		 TreeDataSheet.Instance().refresh();
		 
	 }
	 
	 private void getResultForTreeNode(){
		 
		 treeNodeResultObj resultTree = GenerateTreeByLeaf.getResultNode();
		 
		 if(null == resultTree){
			 return;
		 }
		 
		 //others
		 
         Vector<Vector<treeNodeResultObj>> treeNodeResultObjVec = GenerateTreeByLeaf.getTreeNodeByLevelVec();
		 
		 int level = treeNodeResultObjVec.size();
		 
		 for(int i=level-1;i>=0;i--){
			 Vector<treeNodeResultObj> levelObjVec = treeNodeResultObjVec.get(i);
			 if(false == loopAndSetDataIntoTree(levelObjVec)){
				 System.out.println("计算错误！");
				 return;
			 }
		 }
		 
	 }
	 
	 private boolean loopAndSetDataIntoTree(Vector<treeNodeResultObj> levelObjVec){
		 for(int i=0;i<levelObjVec.size();i++){
			 treeNodeResultObj obj = levelObjVec.get(i);
			 
			 if(obj.isLeaf == true){
				 String condition = obj.sql;
				 
				 String tableName = DatabaseConstant.TREE_DATA_IMP_TABLE;
				 
				 int count = DatabaseAction.Instance().getCountFromTable(tableName, condition);
				 
				 float probality = Float.parseFloat(obj.Probability);
				 
				 int resultNum = (int)(count * probality + 0.5);
				 
				 if("1".equals(obj.Prediction)){
					 obj.conditionMeet = resultNum;
					 obj.conditionNotMeet = count - obj.conditionMeet;
				 }
				 else{
					 obj.conditionNotMeet = resultNum;
					 obj.conditionMeet = count - obj.conditionNotMeet;
				 }
				 
				 updateObjInfo(obj);
				 
				 continue;
			 }
			 
			 Vector<treeNodeResultObj> childVec = obj.childNodeVec;
			 
			 obj.conditionMeet = 0;
			 
			 obj.conditionNotMeet = 0;
			 
			 for(int j=0;j<childVec.size();j++){
				 treeNodeResultObj childObj = childVec.get(j);
				 
				 if(childObj == null){
					 continue;
				 }
				 
				 if(childObj.conditionMeet == -1 || childObj.conditionNotMeet == -1){
					 return false;
				 }
				 
				 obj.conditionMeet += childObj.conditionMeet;
				 
				 obj.conditionNotMeet += childObj.conditionNotMeet;
				 				 
			 }
			 			 			 
			 obj.Prediction = obj.conditionMeet > obj.conditionNotMeet ? "1" : "0";
			 
			 long value = obj.conditionMeet > obj.conditionNotMeet ? obj.conditionMeet : obj.conditionNotMeet;
			 
			 obj.Probability = String.valueOf((float)value/(obj.conditionMeet + obj.conditionNotMeet));
			 
			 updateObjInfo(obj);
		 }
		 
		 return true;
	 }
	 
	 private void updateObjInfo(treeNodeResultObj obj){
		 if(null == obj){
			 return;
		 }
		 
		 obj.nodeInfo = "节点号：" + obj.nodeNumber + "\n";
		 
		 obj.nodeInfo += "预测结果：" + obj.Prediction + "\n";
		 
		 obj.nodeInfo += "概率：" + obj.Probability + "\n";
		 
		 obj.nodeInfo += "节点属性：" + obj.condition;
		 
	 }
	
	 private void openAFileDialog(JFileChooser fileChooser){		
			
			fileChooser.setCurrentDirectory(new File("."));
			
			fileChooser.setAcceptAllFileFilterUsed(false);
			
			final String[][] fileENames = {{".DBF", "数据库文件（*.DBF）"},
					{".xls","MS-Excel 2003 文件(*.xls)"},
					{".xlsx","MS-Excel 2007 文件(*.xlsx)"}
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
			
			return FILE_TYPE.INVALID_FILE;
		}
	 
}
