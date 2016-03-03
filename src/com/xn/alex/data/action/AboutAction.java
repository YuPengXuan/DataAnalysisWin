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
//		 JOptionPane.showMessageDialog(null,"软件版本：1.01\n剩余试用天数："+leftDay+"\n其它：如需购买，请联系爱臭美的多多老师。联系方式54321"
//				 ,"软件信息",JOptionPane.INFORMATION_MESSAGE);
//		 
	 }
	 

}
