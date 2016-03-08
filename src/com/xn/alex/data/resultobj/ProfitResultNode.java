package com.xn.alex.data.resultobj;

import java.util.Comparator;

public class ProfitResultNode implements Comparator<Object>{
	
	public double F0;         //hit rate
	
	public double F1;         // missing rate
	
	public double F2;         //success rate
	
	public double F0divF1;    // F0/F1
	
	public double exportF0;  //exoprt F0;
	
	public double exportF1;   //export F1;
	
	public double exportF2;   //export F2;
	
	public double exportProfitVal; //export profit value
	
	public double exportSalePNum; //export sale people num
	
	public double salePNum;   //sale people total num
	
	public double showSaleNum; //show saleNum in sheet
	
	public double profitVal;  // profit value
	
	public double predSuccPNum; // prediction Successful sale people num
	
	public double distPNum;     // disturb people num
	
	public double showDistNum;  // show distNum in sheet;
	
	public double peopleRatio;  // node people / total nodes people
	
	public int nodeNum;
	
	public String condition;    //store condtion
	
	public String type;          //line data sheet show type
	
	public boolean isShowToPGraph = false;  //whether need show in profit graph

	@Override
	public int compare(Object param1, Object param2) {
		// TODO Auto-generated method stub
		
		ProfitResultNode obj1 = (ProfitResultNode) param1;
		
		ProfitResultNode obj2 = (ProfitResultNode) param2;
		
		if(obj1.F0divF1 > obj2.F0divF1){
			return -1;
		}
		
		return 1;
	}

}
