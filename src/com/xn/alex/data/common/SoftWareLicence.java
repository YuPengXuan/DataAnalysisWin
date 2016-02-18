package com.xn.alex.data.common;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public class SoftWareLicence {
	
	private static SoftWareLicence licenceHandler = null;
	
	private static long leftTime = -1;
	
	private static long swStartTime = -1;
	
	boolean isStartTimeregExist = true;
	
	boolean isLeftRegExist = true;
		
	private SoftWareLicence(){
		
	}
	
	public static SoftWareLicence Instance(){
		
		if(null == licenceHandler){
			licenceHandler = new SoftWareLicence();		
		}
		
		return licenceHandler;		
	}
	
	public boolean isLicenceValid(){
		
		if(false == isRegistryCreated()){
			
			if(false == createRegistry()){
				return false;				
			}
			
		}			
		
		if(true == isLicenceExpired()){			
			return false;			
		}
						
		return true;		
	}
	
	private boolean isRegistryCreated(){
		
		try{
		
			try{
		        String productName = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis", "Key");
		
		        if(null == productName){
			        return false;
		        }
			}
			catch(Exception e){
				isStartTimeregExist = false;
			}
		    
		    String value = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis", "value");
		    
		    if(null == value){
		    	return false;
		    }
		    
		}
		catch(Exception e){
			
			isLeftRegExist = false;
			
			return false;
		}
		
		return true;
	}
	
	private boolean createRegistry(){
		try{
			
			swStartTime = System.currentTimeMillis();
			
			String currentTimeInMillSec = String.valueOf(swStartTime);
			
			if(false == isStartTimeregExist && false == isLeftRegExist){
			
			    Advapi32Util.registryCreateKey(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\AlexSoft\\DataAnalysis");
			
			}
			
			if(false == isStartTimeregExist){
				
			    Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis","key",currentTimeInMillSec);
			
			}
			
			if(false == isLeftRegExist){
			
			    Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis","value", CommonConfig.REG_EKY_VALUE);
			
			}
			
		}
		catch(Exception e){
			
			return false;
			
		}
		
		return true;
	}
	
	private boolean isLicenceExpired(){
		
		try{
			swStartTime = System.currentTimeMillis();
		
		    String regVal = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis", "value");
		    
		    long regValInlong = Long.parseLong(regVal);
		    
		    if(regValInlong<=0){
		    	return true;
		    }
		    
		    String regStarTimeStr = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis", "key");
		    
		    long regStartTimeLong = Long.parseLong(regStarTimeStr);
		    
		    long curretTimeInMillSec = System.currentTimeMillis();
		    
		    if(curretTimeInMillSec-regStartTimeLong>=Long.parseLong(CommonConfig.REG_EKY_VALUE)){
		    	return true;
		    }
		    
		    leftTime = regValInlong;
		
		}
		catch(Exception e){
			
			return true;
			
		}
		
		return false;
	}
	
	public boolean setLeftTime(){
		
		try{
						
			long currentTimeInMillSec = System.currentTimeMillis();
			
			long gap = currentTimeInMillSec - swStartTime;		
			
			long newRegValInLong = leftTime - gap;
			
			String newRegVal = String.valueOf(newRegValInLong);
			
			Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis","value",newRegVal);
			
		}
		catch(Exception e){
			
			Advapi32Util.registrySetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis","value",String.valueOf(leftTime));
			
			return false;			
		}
		
		return true;
	}
	
	public long getLeftUseTime(){
		try{
		
		long currentTimeInMillSec = System.currentTimeMillis();
		
		String regVal = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE,"SOFTWARE\\AlexSoft\\DataAnalysis", "key");
		    
		long regValInlong = Long.parseLong(regVal);
		
		long leftDay = (currentTimeInMillSec- regValInlong)/(3600*24*1000);
		
		return leftDay;
		}
		catch(Exception e){
			return -1;
		}
		
	}

}
