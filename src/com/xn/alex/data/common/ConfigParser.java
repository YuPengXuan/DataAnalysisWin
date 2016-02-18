package com.xn.alex.data.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigParser extends DefaultHandler{
	
	public static Map<String, ConfigElement> columnInfoMap = new HashMap<String, ConfigElement>();
	
	public static Map<String, String> chnToEnColumnName = new HashMap<String, String>();
	
	public static Vector<String> columnVecInConfigOrder = new Vector<String>();
	
	private String mDataBaseName = null;
	
	private String mExcelName = null;
	
	private static ConfigParser configParser = null;
	
	private ConfigElement cfgElement = null;
	
	private ConfigParser(){
		
	}
	
	public static ConfigParser Instance(){

        if(null == configParser){
        	configParser = new ConfigParser();
        }
        return configParser;
	}
	
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
				
		cfgElement = new ConfigElement();
		
		for(int i=0; i<attributes.getLength();i++){
			
			String attrName = attributes.getQName(i);
		
		    if("name".equals(attrName)){								
			    cfgElement.mExcelColumnName = attributes.getValue(attrName);
			    mExcelName = cfgElement.mExcelColumnName;
		    }
		    else if("defValue".equals(attrName)){			
			    cfgElement.mDefValue = attributes.getValue(attrName);
		    }
		    else if("valueType".equals(attrName)){
			    cfgElement.mValueType = attributes.getValue(attrName);
		    }
		    else if("databaseName".equals(attrName)){
			    cfgElement.mDatabaseName = attributes.getValue(attrName);
			    mDataBaseName = cfgElement.mDatabaseName;
		    }
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException{
		if("column" == qName){
			
			if(null == mDataBaseName){
				return;
			}
			
			columnVecInConfigOrder.add(mExcelName);
			
			columnInfoMap.put(mDataBaseName, cfgElement);
			
			chnToEnColumnName.put(mExcelName, mDataBaseName);
			
			mDataBaseName = null;
			
			mExcelName = null;
		}
	}
	
	public Map<String, String> getColumnNameToTypeMap(Vector<String> columnInfoVec){
		
		if(null == columnInfoVec)
		{
			return null;
		}
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		for(int i=0;i<columnInfoVec.size();i++){
			
			String columnName = columnInfoVec.get(i);
			
			String columnType = columnInfoMap.get(columnName).mValueType;
			
			if(null != columnType){
			
			    resultMap.put(columnName, columnType);
			    
			}
			
		}
		
		return resultMap;		
	}

}
