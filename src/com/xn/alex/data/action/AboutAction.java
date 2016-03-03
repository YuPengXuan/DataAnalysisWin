package com.xn.alex.data.action;

import javax.swing.JOptionPane;

import com.xn.alex.data.common.SoftWareLicence;
import com.xn.alex.data.ui.AboutDialog;
import com.xn.alex.license.LicenseController;

public class AboutAction extends WindowAction {
	
	private static AboutAction aboutActionHandler = null;
	
	 private AboutAction(){
		 
	 }
	 
	 public static AboutAction Instance(){
		 
		 if(null == aboutActionHandler){
			 aboutActionHandler = new AboutAction();
		 }
		 
		 return aboutActionHandler;		 
	 }
	 
	 public void takeAction(){
		 
		 long leftDay = LicenseController.getLicenseController().getLeftUseTime();
		 
		 AboutDialog dialog = new AboutDialog();
		 dialog.setVisible(true);
//		 JOptionPane.showMessageDialog(null,"����汾��1.01\nʣ������������"+leftDay+"\n���������蹺������ϵ�������Ķ����ʦ����ϵ��ʽ54321"
//				 ,"�����Ϣ",JOptionPane.INFORMATION_MESSAGE);
//		 
	 }
	 

}
