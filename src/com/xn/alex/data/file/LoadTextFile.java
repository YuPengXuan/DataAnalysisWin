package com.xn.alex.data.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class LoadTextFile {
	
	 public static void readFileByLines(File file, Vector<String> contentVec) {
	     //File file = new File(fileName);
		 contentVec.clear();
	     BufferedReader reader = null;
	     try {
	        reader = new BufferedReader(new FileReader(file));
	        String tempString = null;
	        // one time one line until null
	        while ((tempString = reader.readLine()) != null) {
	        	if("".equals(tempString)){
	        		continue;
	        	}
	        	contentVec.add(tempString);
	        }
	        reader.close();
	    } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	            }
	         }
	    }
	}
	 

}
