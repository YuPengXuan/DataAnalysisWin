package com.xn.alex.data.roc;

import java.util.Comparator;

import com.xn.alex.data.resultobj.ProfitResultNode;

public class ProfitObjOrderByDesc implements Comparator<Object>{

	@Override
	public int compare(Object obj1, Object obj2) {
		// TODO Auto-generated method stub
		ProfitResultNode cmpObj1 = (ProfitResultNode) obj1;
		
		ProfitResultNode cmpObj2 = (ProfitResultNode) obj2;
		
		if(cmpObj1.exportProfitVal > cmpObj2.exportProfitVal){
			return -1;
		}
		return 1;
	}

}
