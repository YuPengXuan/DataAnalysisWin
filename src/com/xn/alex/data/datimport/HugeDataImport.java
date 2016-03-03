package com.xn.alex.data.datimport;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class HugeDataImport extends DefaultHandler{
	
	private static HugeDataImport hugeDataImport = null;
	
	private HugeDataImport(){
		
	}
	
	public static HugeDataImport Instance(){
		if(null == hugeDataImport){
			hugeDataImport = new HugeDataImport();
		}
		return hugeDataImport;
	}
	
	public boolean importData(String fileName, String tableName, List<String> columnNames, List<Integer> missingColumnIndexList){
		
		try{
		
			OPCPackage pkg = OPCPackage.open(fileName);			
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();			
			XMLReader parser = fetchSheetParser(sst, columnNames,missingColumnIndexList,tableName);
			Iterator<InputStream> sheets = r.getSheetsData();
			while(sheets.hasNext()) {
				System.out.println("Processing new sheet:\n");
				InputStream sheet = sheets.next();
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
				SheetHandler handler = (SheetHandler)parser.getContentHandler();
				System.out.println(handler.getObsoleteRowNum() + "行不符合要求数据被丢弃！");
				sheet.close();
				System.out.println("");
			}
		}
		catch(Exception e){
				e.printStackTrace();
				return false;
		}
		
		return true;
	}
	
	private XMLReader fetchSheetParser(SharedStringsTable sst,  List<String> columnNames, List<Integer> missingColumnIndexList, String tableName) throws Exception {
		XMLReader parser =
			XMLReaderFactory.createXMLReader(
					"org.apache.xerces.parsers.SAXParser"
			);
		ContentHandler handler = new SheetHandler(sst,columnNames, missingColumnIndexList, tableName);
		parser.setContentHandler(handler);
		return parser;
	}

}
