package com.xn.alex.data.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ConfigWriter implements Runnable{
	
	private Vector<ConfigElement> m_elementVec;

	public ConfigWriter(Vector<ConfigElement> elementVec){
		m_elementVec = elementVec;		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		writeToConfigFile();
	}
	
	private synchronized boolean writeToConfigFile(){
		try{
			
			 SAXReader reader = new SAXReader();
			 
			 String configFileName = "config/config.xml";
			 
	         Document doc = reader.read(new File(configFileName));   
	         
             Element rootEl = doc.getRootElement();
	         
             for(int i=0;i<m_elementVec.size();i++){
            	 
            	 ConfigElement cfgEl = m_elementVec.get(i);
            	 
	             Element newElement = rootEl.addElement("column");
	             
	             newElement.addAttribute("databaseName", cfgEl.mDatabaseName);
	             
	             newElement.addAttribute("defValue", cfgEl.mDefValue);
	             
	             newElement.addAttribute("name", cfgEl.mExcelColumnName);
	             
	             newElement.addAttribute("valueType", cfgEl.mValueType);
             }
	      	                 	         
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter writer = new XMLWriter(new FileOutputStream(
	        		configFileName), format);
	        writer.write(doc);
	        writer.close();
			
			
		}catch(Exception e){
			System.out.println("¸ÄÐ´ÅäÖÃÎÄ¼þÊ§°Ü!");
			return false;
		}
		
		
		return true;
	}

}
