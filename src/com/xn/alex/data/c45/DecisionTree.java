package com.xn.alex.data.c45;

import java.util.Vector;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.window.MainWindow;

public class DecisionTree {
	
	private Vector<String> columnNameVec = null;
	
	public void createDecisionTree(String argumentName, int treeWidth, int treeDeepth){
		setUneededHandleColumn(argumentName);
	}
	
	private boolean setUneededHandleColumn(String argumentName){
		
		columnNameVec = MainWindow.getJtableColumnVec();
		
		
        
		return true;
	}

}
