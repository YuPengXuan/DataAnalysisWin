package com.xn.alex.data.process;

import com.xn.alex.data.window.MainWindow;

public class MenuItemEnable {
	
	private static MenuItemEnable menuItemEnableHandler = null;
	
	private MenuItemEnable(){
		
	}
	
	public static MenuItemEnable Instance(){
		if(null == menuItemEnableHandler){
			menuItemEnableHandler = new MenuItemEnable();
		}		
		return menuItemEnableHandler;		
	}
	
	
	public boolean enableSecondColumnMenu(){
		
        try{
        	
        	MainWindow.Instance().getMntmLostVal().setEnabled(true);
        	
        	MainWindow.Instance().getMntmHistogram().setEnabled(true);
        	
        	MainWindow.Instance().getMntmNormalization().setEnabled(true);
        	
        	MainWindow.Instance().getMntmRemoveOddValue().setEnabled(true);
        	
        	MainWindow.Instance().getMntmSkip().setEnabled(true);
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			return false;
		}
		
		return true;
		
	}
	
    public boolean enableThirdColumnMenu(){
		
        try{
        	MainWindow.Instance().getMntmClusterAnalyze().setEnabled(false);
        	
        	MainWindow.Instance().getMntmKaFang().setEnabled(false);
        	
        	MainWindow.Instance().getMnC4_45().setEnabled(true);
        	
        	MainWindow.Instance().getMntmZAPTree().setEnabled(false);
        	
        	MainWindow.Instance().getMntmRegAnylyze().setEnabled(false);
        	
        	MainWindow.Instance().getMntmAssocAnalyze().setEnabled(false);
        	
        	MainWindow.Instance().getMntmTimeAnalyze().setEnabled(false);
			
		}
		catch(Exception e){
			
			return false;
		}
		
		return true;
		
	}
    
   public boolean enableFourthColumnMenu(){
		
        try{
        	
            MainWindow.Instance().getMntmRoc().setEnabled(true);
        	
        	MainWindow.Instance().getMntmRocEx().setEnabled(true);
        	
        	MainWindow.Instance().getMntmRocKeep().setEnabled(true);
        	
        	MainWindow.Instance().getMntmRocApp().setEnabled(true);
        	
        	MainWindow.Instance().getMntmProfitModel().setEnabled(true);
        	
        	MainWindow.Instance().getMntmProfitModelEx().setEnabled(true);
        	
        	MainWindow.Instance().getMntmProfitModelKeep().setEnabled(true);
        	
        	MainWindow.Instance().getMntmProfitModelApp().setEnabled(true);
			
		}
		catch(Exception e){
			
			return false;
		}
		
		return true;
		
	}

}
