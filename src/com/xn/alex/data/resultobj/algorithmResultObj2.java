package com.xn.alex.data.resultobj;

import java.util.Comparator;

public class algorithmResultObj2 implements Comparator<Object>{
	
    public Number x = 0;
	
	public Number y = 0;
	
	public Number yDivX = 0;
	
	public String type = null;
	
	public int nodeNum = -1;
		

	@Override
	public int compare(Object paramT1, Object paramT2) {
		// TODO Auto-generated method stub
		algorithmResultObj2 obj1 = (algorithmResultObj2)paramT1;
		
		algorithmResultObj2 obj2 = (algorithmResultObj2)paramT2;
				
		if((float)obj1.yDivX>(float)obj2.yDivX){
			return -1;
		}
		
		return 1;
	}

}
