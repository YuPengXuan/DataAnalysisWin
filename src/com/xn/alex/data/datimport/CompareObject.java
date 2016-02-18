package com.xn.alex.data.datimport;

import java.util.Comparator;

public class CompareObject implements Comparator<Object>{

	public String objName = null;
	
	public int compareVlue = -1;
	
	public int origVal = -1;

	@Override
	public int compare(Object obj_1, Object obj_2) {
		// TODO Auto-generated method stub
		CompareObject e1 = (CompareObject)obj_1;
		
		CompareObject e2 = (CompareObject)obj_2;
		
		if(e1.compareVlue>e2.compareVlue){
			return 1;
		}

		return -1;
	}

}
