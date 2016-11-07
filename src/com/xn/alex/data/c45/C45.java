package com.xn.alex.data.c45;

import java.util.List;
import java.util.Map;

public class C45 {
		
	/**
	 * info(D) = -E(pi*log2 pi)
	 * 
	 * @param array
	 * @param counts
	 * @return
	 */
	public double infoD(int totalCount, List<Integer> partCountList){
		double infoD = 0;
		
		for(int i=0;i<partCountList.size();i++){
			int partCount = partCountList.get(i);
			
			if(partCount == totalCount){
			    break;	
			}
			double tmpVal = 0;
			if(partCount != 0){
			   tmpVal = -1 * ((double)partCount/(double)totalCount) * logYBase2((double)partCount/(double)totalCount);
			}
			infoD +=tmpVal;
		}
		
		
		return infoD;
	}
	
	/**
	 * infoA(D) = -E(pi*log2 pi)
	 * 
	 * @param array
	 * @param counts
	 * @return
	 */
	public double infoAD(int totalCount, Map<String, Integer> partCountMap, Map<String, List<Integer>> subPartNumMap){
		double infoAD = 0;
		for(Map.Entry<String, Integer> entry : partCountMap.entrySet()){
			String key = entry.getKey();
			int PartVal = entry.getValue();
			List<Integer> subPartValList = subPartNumMap.get(key);
			if(subPartValList == null){
				System.out.println("º∆À„InfoA(D) ß∞‹");
				return 0;
			}
			
			if(PartVal == 0){
				continue;
			}
			
			double infoD = infoD(PartVal, subPartValList);
			
			infoAD += ((double)PartVal/(double)totalCount)*infoD;			
		}				
		return infoAD;
	}
	
	public double gain(double infoD, double infoAD){
		double gain = infoD - infoAD;
		return gain;
	}
	
	
	public double gainRatio(double gain, double Entropy){
		double gainRatio = gain/Entropy;
		return gainRatio;
	}
	
	public double logYBase2(double y) {
		double log2Y = Math.log(y) / Math.log(2);
		return log2Y;
	}

}
