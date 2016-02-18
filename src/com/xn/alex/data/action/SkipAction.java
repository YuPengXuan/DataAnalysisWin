package com.xn.alex.data.action;

import com.xn.alex.data.process.MenuItemEnable;

public class SkipAction extends WindowAction {
	
	 private static SkipAction skipActionHandler = null;
		
	 private SkipAction(){
		 
	 }
	 
	 public static SkipAction Instance(){
		 
		 if(null == skipActionHandler){
			 skipActionHandler = new SkipAction();
		 }
		 
		 return skipActionHandler;		 
	 }
	
	 public void takeAction(){
		 
		 MenuItemEnable.Instance().enableThirdColumnMenu();

	 }

}
