package com.xn.alex.data.c45;

import java.util.Collections;
import java.util.Vector;

public class Dispersed {
	
	public static int TypeClass=4;
	
	public static Vector<String> dispersed( Vector<String> S)
    {
		
		try{
			
		Vector<Double> T=new Vector<Double>();
		
		for(int i=0;i<S.size();i++)
			T.add( Double.parseDouble(S.get(i)));
		
        //如果只有2个值，不用做离散化
        if (computeCount(T) > TypeClass)//大于TypeClass个类型的值
        {
            //取最大和最小值
        	Collections.sort(T);
        	
            double max=T.lastElement(),min=T.firstElement();
            
//            for(double t : T)
//            {
//                if (t > max)
//                    max = t;
//                if (t < min)
//                    min = t;
//            }
            //最多分成4个
            
            double gain = Math.round((max - min) / TypeClass);
            
            //int maxmax = min + gain * TypeClass;
            
            Vector<Double> newT=new Vector<Double>();
            
            for (double t : T)
            {
            	
                for (int i = 0; i < TypeClass; i++)
                {
                	
                    if (t < min + gain * (i + 1) && t >= min + gain * i)
                    {
                    	
                        newT.add(min + gain * i);

                    }
                }
                
                if (t >= min + gain * TypeClass)
                {
                	
                    newT.add(min + gain * (TypeClass - 1));//最大值归为最后一个类
                    
                }
            }
            Vector<String> s=new Vector<String>();
            
            for(int i=0;i<newT.size();i++)
            	
            	s.add(newT.get(i)+"");
            
            return s;
            
        }
        
        Vector<String> s=new Vector<String>();
        
        for(int i=0;i<T.size();i++)
        	
        	s.add(T.get(i)+"");
        
        return s;
        
		}
		
		catch(Exception e){
			
			return S;
			
		}
    }
	public static int computeCount(Vector<Double> T) {
		
		Vector<Double> f=new Vector<Double>();
		
		for(double t:T){
			
			int isInVector=0;
			
			for(double s:f)
				
				if(s==t)
					
					isInVector=1;
			
			if(isInVector==0)
				
				f.addElement(t);
			
		}
			
		return f.size();
		
	}
	public static int computeCountString(Vector<String> T) {
		
		Vector<String> f=new Vector<String>();
		
		for(String t:T){
			
			int isInVector=0;
			
			for(String s:f)
				
				if(s.equals(t))
					
					isInVector=1;
			
			if(isInVector==0)
				
				f.addElement(t);
			
		}
			
		return f.size();
		
	}
public static void main(String args[]){
	
	Vector<String> v=new Vector<String>();
	
	for(double i=1;i<7;i++)
		
		v.add(i+"");
	
	Vector<String> a=Dispersed.dispersed(v);
	
	System.out.println(a);
	
}
}
