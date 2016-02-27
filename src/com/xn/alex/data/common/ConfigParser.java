package com.xn.alex.data.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigParser extends DefaultHandler{
	
	public static Map<String, ConfigElement> columnInfoMap = new HashMap<String, ConfigElement>();
	
	public static Map<String, String> chnToEnColumnName = new HashMap<String, String>();
	
	public static Map<String, Integer> columnNameToSizeMap = new HashMap<String, Integer>();
	
	public static Vector<String> columnVecInConfigOrder = new Vector<String>();
		
	private String mDataBaseName = null;
	
	private String mExcelName = null;
	
	private static ConfigParser configParser = null;
	
	private ConfigElement cfgElement = null;
	
	private long maxGenId = 0;
	
	private ConfigParser(){
		
	}
	
	public long getMaxGenId() {
		return maxGenId;
	}

	public void setMaxGenId(long maxGenId) {
		this.maxGenId = maxGenId;
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
			    if(cfgElement.mValueType.contains("VARCHAR")){
			    	Pattern pat = Pattern.compile("VARCHAR\\((\\d+)\\)");
			    	Matcher macher = pat.matcher(cfgElement.mValueType);
			    	while(macher.find()){
			    		String sizeStr = macher.group(1);
			    		int size = Integer.parseInt(sizeStr);
			    		columnNameToSizeMap.put(mExcelName, size);
			    	}
			    }
		    }
		    else if("databaseName".equals(attrName)){
			    cfgElement.mDatabaseName = attributes.getValue(attrName);
			    mDataBaseName = cfgElement.mDatabaseName;
			    if(mDataBaseName.contains("GenColumn_")){
			        Pattern numPat = Pattern.compile(CommonConfig.GEN_COLUMN_NAME_PREFIX+"\\d+");
			        Matcher macher = numPat.matcher(mDataBaseName);
			        while(macher.find()){
			        	String numStr = macher.group(1);
			        	long num = Long.parseLong(numStr);
			        	if(num > maxGenId){
			        		maxGenId = num;
			        	}
			        }
			    }
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
	
	public List<DataColumnInfo> getColumnNameToTypeMap(Vector<String> columnInfoVec){
		
		if(null == columnInfoVec)
		{
			return null;
		}
		
		List<DataColumnInfo> resultList = new ArrayList<DataColumnInfo>();
		
		for(int i=0;i<columnInfoVec.size();i++){
			
			String columnName = columnInfoVec.get(i);
			
			String columnType = columnInfoMap.get(columnName).mValueType;
			
			if(null != columnType){
			
				resultList.add(new DataColumnInfo(columnName, columnType));
			    
			}
			
		}
		
		return resultList;		
	}

	public static class DataColumnInfo {
		public String name;
		public String type;
		
		
		public DataColumnInfo(String name, String type) {
			super();
			this.name = name;
			this.type = type;
		}
		
		public String getName() {
			return name;
		}


		public String getType() {
			return type;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DataColumnInfo other = (DataColumnInfo) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		
	}
}
