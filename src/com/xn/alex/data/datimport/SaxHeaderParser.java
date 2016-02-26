package com.xn.alex.data.datimport;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xn.alex.data.common.ConfigParser;

public class SaxHeaderParser extends DefaultHandler{
	
	private SharedStringsTable sst;
	
	private Vector<String> columnNames;
	
	private List<Integer> missingColumnIndexList;
	
	private String lastContents;
	
	private boolean nextIsString;
	
	private int curRow = 0;  
	
    private int curCol = 0;
	
	private String currentExcelInd = null;
	
	private Map<Integer, String> MissColumnIndToChnNameMap;
	
	public SaxHeaderParser(SharedStringsTable sst, Vector<String> columnNames, List<Integer> missingColumnIndexList, Map<Integer, String> MissColumnIndToChnNameMap){
		this.sst = sst;
		this.columnNames = columnNames;
		this.missingColumnIndexList = missingColumnIndexList;
		this.MissColumnIndToChnNameMap = MissColumnIndToChnNameMap;
	}
	
	public void startDocument() throws SAXException {   
		//System.out.println("开始解析Excel");   
	}  
	
	public void endDocument() throws SAXException {
		//System.out.println("Excel解析结束了");   
	} 
	
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		// c => cell
		if(name.equals("c")) {
		    // Print the cell reference
		    //System.out.print(attributes.getValue("r") + " - ");
		    // Figure out if the value is an index in the SST
		    String cellType = attributes.getValue("t");
						
		    if("s".equals(cellType)) {
		        nextIsString = true;
		    }else{
			    nextIsString = false;
		    }
						
		    currentExcelInd = attributes.getValue("r");
						
		    Pattern p1 = Pattern.compile("([a-zA-Z]+)\\d+");
						
		    Matcher m1 = p1.matcher(currentExcelInd);
			        	
		    while(m1.find()){
		        currentExcelInd = m1.group(1);
		    }
						
			attributes.getValue("s");
		}
		// Clear contents cache
		lastContents = "";
		
		
	}
	
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		// Process the last contents as required.
		// Do now, as characters() may be called more than once
		if(nextIsString) {
			int idx = Integer.parseInt(lastContents);
			
			lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			
			nextIsString = false;
		}
		
		if (name.equals("row")) {
			
			curRow++;
						        
			throw new SAXException("");			
        }
		
		if("c".equals(name) && "".equals(lastContents)){
			return;
		}

		// v => contents of a cell
		// Output after we've seen the string contents
		if(!name.equals("v")) {
			return;
			//System.out.println(lastContents);
		}
	
								
		if(curRow == 0){
			//it means first row and should be column name
			if(false == getColumnNames()){
			    throw new SAXException("get column name fail!");
			}																	
		}								
		curCol++;					
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		lastContents += new String(ch, start, length);
	}
	
	private boolean getColumnNames(){
		
		if("".equals(lastContents)){
			String msg = "第"+ (curCol+1) +"列列名不能为空";
			
			System.out.println(msg);
			
			return false;
		}
		
		String columnNameCh = lastContents.trim();
		
		String columnNameEn = ConfigParser.chnToEnColumnName.get(columnNameCh);
	    
	    if(null != columnNameEn){
	    	columnNames.add(columnNameEn);
	    }
	    else{
	    	
	    	columnNames.add(columnNameCh);
	    	
	    	missingColumnIndexList.add(curCol);
	    	
	    	MissColumnIndToChnNameMap.put(curCol, columnNameCh);
	    }
	    
	    return true;
		
	}

}
