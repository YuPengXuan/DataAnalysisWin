package com.xn.alex.data.process;

import com.xn.alex.data.window.MainWindow;

public class MenuItemDisable {
	
    private static MenuItemDisable menuItemDisableHandler = null;
	
	private MenuItemDisable(){
		
	}
	
	public static MenuItemDisable Instance(){
		if(null == menuItemDisableHandler){
			menuItemDisableHandler = new MenuItemDisable();
		}		
		return menuItemDisableHandler;		
	}
	
		
	public boolean disableSecondColumnMenu(){
		
        try{
        	
        	MainWindow.Instance().getMntmLostVal().setEnabled(false);
        	
        	MainWindow.Instance().getMntmHistogram().setEnabled(false);
        	
        	MainWindow.Instance().getMntmNormalization().setEnabled(false);
        	
        	MainWindow.Instance().getMntmRemoveOddValue().setEnabled(false);
        	
        	MainWindow.Instance().getMntmSkip().setEnabled(false);
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			return false;
		}
		
		return true;
		
	}
	
    public boolean disableThirdColumnMenu(){
		
        try{
        	
            MainWindow.Instance().getMntmClusterAnalyze().setEnabled(false);
        	
        	MainWindow.Instance().getMntmKaFang().setEnabled(false);
        	
        	MainWindow.Instance().getMntmC4_45().setEnabled(false);
        	
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
    
    public boolean disableFourthColumnMenu(){
		
        try{
        	
        	MainWindow.Instance().getMntmRoc().setEnabled(false);
        	
        	MainWindow.Instance().getMntmRocEx().setEnabled(false);
        	
        	MainWindow.Instance().getMntmRocKeep().setEnabled(false);
        	
        	MainWindow.Instance().getMntmRocApp().setEnabled(false);
        	
        	MainWindow.Instance().getMntmRocWarn().setEnabled(false);
        	
        	MainWindow.Instance().getMntmProfitModel().setEnabled(false);
        	
        	MainWindow.Instance().getMntmProfitModelEx().setEnabled(false);
        	
        	MainWindow.Instance().getMntmProfitModelKeep().setEnabled(false);
        	
        	MainWindow.Instance().getMntmProfitModelApp().setEnabled(false);
        	
            MainWindow.Instance().getMntmProfitModelWarn().setEnabled(false);
			
		}
		catch(Exception e){
			
			return false;
		}
		
		return true;
		
	}

}
