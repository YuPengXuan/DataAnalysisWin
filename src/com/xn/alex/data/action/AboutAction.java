package com.xn.alex.data.action;

import javax.swing.JOptionPane;

import com.xn.alex.data.common.SoftWareLicence;

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
		 
		 long leftDay = SoftWareLicence.Instance().getLeftUseTime();
		 
		 JOptionPane.showMessageDialog(null,"����汾��1.01\nʣ������������"+leftDay+"\n���������蹺������ϵ�������Ķ����ʦ����ϵ��ʽ54321"
				 ,"�����Ϣ",JOptionPane.INFORMATION_MESSAGE);
		 
	 }
	 

}
