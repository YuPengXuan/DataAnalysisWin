package com.xn.alex.data.datimport;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.xn.alex.data.common.CommonConfig;
import com.xn.alex.data.common.ConfigElement;
import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.database.DatabaseAction;

public class SheetHandler extends DefaultHandler{	
		private SharedStringsTable sst;
		
		private String lastContents;
		
		private boolean nextIsString;
		
		private int curRow = 0;  
		
	    private int curCol = 0;
	    
	    private Vector<String> columnNames;
	    
	    private List<Integer> missingColumnIndexList;
	    
	    private String tableName;
	    
	    private Vector<String> rowValVec = null;
	    
	    private Vector<Vector<String>> commitValVec = new Vector<Vector<String>>();
	    
	    private List<Integer> numbericIndexList = null;
	    
	    private Map<Integer, String> colNumToExcleColumIndMap = new HashMap<Integer, String>();
	    
	    private Map<String, Integer> ExcelColumnIndToColNumMap = new HashMap<String, Integer>();	   
	    
	    private boolean isAddThisRow = true;
	    
	    private String currentExcelInd = null;
	    
	    private int obsoleteRowNum = 0;
	    		
		public int getObsoleteRowNum() {
			return obsoleteRowNum;
		}

		public SheetHandler(SharedStringsTable sst, Vector<String> columnNames, List<Integer> missingColumnIndexList, String tableName) {
			this.sst = sst;
			this.columnNames = columnNames;
			this.missingColumnIndexList = missingColumnIndexList;
			this.tableName = tableName;
		}
		
		public void startDocument() throws SAXException {   
			//System.out.println("开始解析Excel");   
		}  
		
		public void endDocument() throws SAXException {
			CommitValToDataBase();
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
				if(curRow == 0){
					colNumToExcleColumIndMap.put(curCol, currentExcelInd);
					
					ExcelColumnIndToColNumMap.put(currentExcelInd, curCol);					
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
				//it means come to end of the row
				if(false == rowEndHandle()){
					try {
						
						throw new Exception("handle " + curRow + " data fail!");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
                return;
            }
			
			if("c".equals(name) && "".equals(lastContents)){
				
				if(!missingColumnIndexList.contains(curCol)){
					
				    rowValVec.add(String.valueOf(CommonConfig.IVALID_VALUE));
				    
				}
				curCol++;
				return;
			}

			// v => contents of a cell
			// Output after we've seen the string contents
			if(!name.equals("v")) {
				return;
				//System.out.println(lastContents);
			}
																	
			if(curRow!= 0){
				 								
				if(false == handleCellData()){
					try {
						throw new Exception("handle cell data fail.Column:" + curCol);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
							
			curCol++;
			
		}

		public void characters(char[] ch, int start, int length)
				throws SAXException {
			lastContents += new String(ch, start, length);
		}
				
		
		private boolean rowEndHandle(){
			if(curRow == 0){
				//come to the end of the first row

				numbericIndexList = getNumericListColumnIndex(columnNames);
				
				rowValVec = new Vector<String>();
				
				curRow++;
				
				curCol = 0;
				
				return true;
					
			}	
			
			if(true == isAddThisRow && rowValVec.size()!=0){
			    commitValVec.add(rowValVec);
			}
			else{
				obsoleteRowNum++;
			}
			
			if(curRow % 100 == 1){				
				CommitValToDataBase();
			}
			
            curRow++;  
            
            curCol = 0; 
            
            isAddThisRow = true;
            
            rowValVec = new Vector<String>();
			
			return true;			
		}
		
		private List<Integer> getNumericListColumnIndex(Vector<String> columnNameVec){
			
			List<Integer> numericIndexList = new ArrayList<Integer>();
			
			for(int i=0;i<columnNameVec.size();i++){
				
				String columnName = columnNameVec.get(i);
				
				if(false == ConfigParser.columnInfoMap.get(columnName).mValueType.contains("VARCHAR")){
					
					numericIndexList.add(i);
					
				}
				
			}
				
			return numericIndexList;
					
		}
		
        private boolean handleCellData(){
        	if(missingColumnIndexList.contains(curCol)){
        		return true;
        	}
        	
        	addLostValueToValVec();
        	
        	if("".equals(lastContents) || "#NULL!".equals(lastContents)){
				lastContents = String.valueOf(CommonConfig.IVALID_VALUE);
			}
        	else if(true == numbericIndexList.contains(curCol) && nextIsString == true){		    				    		
		    	isAddThisRow = false;
		    	return true;
        	}
        	
        	String columnName = columnNames.get(rowValVec.size());
        	ConfigElement columnElement = ConfigParser.columnInfoMap.get(columnName);
        	
        	if(columnElement.mValueType.contains("VARCHAR")){
        		
                String chnColumnName = columnElement.mExcelColumnName;
                
        	    int size = ConfigParser.columnNameToSizeMap.get(chnColumnName);
        	    
        	    if(lastContents.length()>size){
        	    	
        	    	isAddThisRow = false;
        	    	
        	    }
        	}

        	
        	rowValVec.add(lastContents);
        	return true;

        }
        
        private void addLostValueToValVec(){
        	try{
        	    int realColNum = ExcelColumnIndToColNumMap.get(currentExcelInd); 
        	    
        	    int gap = realColNum - curCol;
        	    
        	    if(gap == 0){
        		    return;
        	    }
        	
        	
        	    //should have null data
        	    for(int i=0;i<gap;i++){
        	    	
        		    rowValVec.add(String.valueOf(CommonConfig.IVALID_VALUE));
        		    
        	    }        	
        	    curCol = realColNum;  
        	}
        	catch(Exception e){
        		e.printStackTrace();
        		//System.out.println("currentExcelInd:"+currentExcelInd + "Row:" + curRow);
        	}
        	
        }

		private void CommitValToDataBase(){
			if(commitValVec.size() !=0){
			    DatabaseAction.Instance().insertTable(tableName, columnNames, commitValVec);
			    commitValVec.clear();
			}
		}

}
