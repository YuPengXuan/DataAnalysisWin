package com.xn.alex.data.resultobj;

import java.util.Comparator;

public class ProfitResultNode implements Comparator<Object>{
	
	public double F0;         //hit rate
	
	public double F1;         // missing rate
	
	public double F2;         //success rate
	
	public double salePNum;   //sale people total num
	
	public double profitVal;  // profit value
	
	public double predSuccPNum; // prediction Successful sale people num
	
	public double distPNum;     // disturb people num
	
	public double peopleRatio;  // node people / total nodes people
	
	public int nodeNum;
	
	public String condition;    //store condtion
	

	@Override
	public int compare(Object param1, Object param2) {
		// TODO Auto-generated method stub
		
		ProfitResultNode obj1 = (ProfitResultNode) param1;
		
		ProfitResultNode obj2 = (ProfitResultNode) param2;
		
		if(obj1.F2 > obj2.F2){
			return -1;
		}
		
		return 1;
	}

}
