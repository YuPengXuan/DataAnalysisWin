package com.xn.alex.data.action;

import java.util.Vector;

import com.xn.alex.data.common.CommonConfig.ROC_TYPE;
import com.xn.alex.data.graphics.LineDataSheet;
import com.xn.alex.data.process.MenuItemDisable;
import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;
import com.xn.alex.data.rule.RocParameterGenerate;
import com.xn.alex.data.window.MainWindow;

public class RocAction extends WindowAction {

	private static RocAction rocActionHandler = null;
	
	 private RocAction(){
		 
	 }
	 
	 public static RocAction Instance(){
		 
		 if(null == rocActionHandler){
			 rocActionHandler = new RocAction();
		 }
		 
		 return rocActionHandler;		 
	 }
	 
	 public void takeAction(){
		 //treeNodeResultObj treeNode = GenerateTreeByLeaf.getResultNode();
		 
		 ROC_TYPE rocType = ImportRuleAction.Instance().getRocType();
		 
		 if(null == rocType){
			 return;
		 }
		 String title = "";
		 switch(rocType){
		       case ROC_EXT:
		    	   MenuItemDisable.Instance().disableFourthColumnMenu();
		    	   MainWindow.Instance().getMntmRocEx().setEnabled(true);
		    	   MainWindow.Instance().getMntmProfitModelEx().setEnabled(true);
		    	   title = "���Ӫ��";
		    	   break;
		       case ROC_SMS:
		    	   MenuItemDisable.Instance().disableFourthColumnMenu();
		    	   MainWindow.Instance().getMntmRoc().setEnabled(true);
		    	   MainWindow.Instance().getMntmProfitModel().setEnabled(true);
		    	   title ="����Ӫ��";
		           break;
		       case ROC_CUST:
		    	   MenuItemDisable.Instance().disableFourthColumnMenu();
		    	   MainWindow.Instance().getMntmRocKeep().setEnabled(true);
		    	   MainWindow.Instance().getMntmProfitModelKeep().setEnabled(true);
		    	   title ="�ͻ�����";
		           break;
		       case ROC_APP:
		    	   MenuItemDisable.Instance().disableFourthColumnMenu();
		    	   MainWindow.Instance().getMntmRocApp().setEnabled(true);
		    	   MainWindow.Instance().getMntmProfitModelApp().setEnabled(true);
		    	   title ="APP�ͻ�����";
		    	   break;
		       case ROC_WARN:
		    	   MenuItemDisable.Instance().disableFourthColumnMenu();
		    	   MainWindow.Instance().getMntmRocWarn().setEnabled(true);
		    	   MainWindow.Instance().getMntmProfitModelWarn().setEnabled(true);
		    	   title = "Ͷ�߿ͻ�Ԥ��";
		    	   break;
		       default:
		    	   System.out.println("����ROC���Ͳ���ȷ��");
		    	   return;
		 
		 }
		 
		 LineDataSheet lineDataSheet = new LineDataSheet(title);
		 
         lineDataSheet.setxLabel("F1�������ʣ�");
		 
		 lineDataSheet.setyLabel("F0�������ʣ�");
		 
		 lineDataSheet.setRocType(rocType);
		 		 
		 Vector<ProfitResultNode> showVec = constructResultVec();
		 
		 lineDataSheet.show(showVec);
		 		 		 
		 //LineDataSheet.Instance().testCategoryLineDataSheet();
	 }
	 
	 private Vector<ProfitResultNode> constructResultVec(){
		 
		 Vector<treeNodeResultObj> treeNodeVec = GenerateTreeByLeaf.getTreeLeafNodeVec();
		 
		 RocParameterGenerate genHandler = new RocParameterGenerate(treeNodeVec);
		 
		 Vector<ProfitResultNode> showVec = genHandler.getShowVec();
		 
		 return showVec;		 
	 }
	 

}
