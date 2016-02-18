package com.xn.alex.data.rule;

import java.util.Comparator;

import com.xn.alex.data.resultobj.treeNodeResultObj;

public class TreeCompareObj implements Comparator<Object>{

	@Override
	public int compare(Object obj_1, Object obj_2) {
		// TODO Auto-generated method stub
		treeNodeResultObj node_1 = (treeNodeResultObj)obj_1;
		
		treeNodeResultObj node_2 = (treeNodeResultObj)obj_2;
		
		if(node_1.nodeNumber>node_2.nodeNumber){			
			return 1;			
		}
		
		return -1;
	}

}
