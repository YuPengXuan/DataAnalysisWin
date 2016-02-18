package com.xn.alex.data.common;

public class ConfigElement {
	
    public String mExcelColumnName = null;
    
    public String mDefValue = null;
    
    public String mValueType = null;
    
    public String mDatabaseName = null;
  
    
    public int getIntValue(String value){
    	return Integer.parseInt(value);
    }
    
    public long getLongValue(String value){
    	return Long.parseLong(value);
    }
    
    public float getFloatValue(String value){
    	return Float.parseFloat(value);
    }
    

}
