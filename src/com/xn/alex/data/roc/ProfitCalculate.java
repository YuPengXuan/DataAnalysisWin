package com.xn.alex.data.roc;

import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.treeNodeResultObj;
import com.xn.alex.data.rule.GenerateTreeByLeaf;

public class ProfitCalculate extends ProfitModelInterface{
	
	private int maxNodeNum = 0;
	
	@Override
	public void takeAction() {
		// TODO Auto-generated method stub
		calculateProfit();
		
		drawGraph();
				
	}
	
	private void calculateProfit(){
		treeNodeResultObj rootNode = GenerateTreeByLeaf.getTreeNodeByLevelVec().get(0).get(0);
		
		long totalBuy = rootNode.conditionMeet;
		
		long totalNotBuy = rootNode.conditionNotMeet;
		
		long totalNum = totalBuy + totalNotBuy;
		
		double succRate = (double)(totalBuy) / ((double)totalBuy + (double)totalNotBuy);
		
		double maxP = -999999999999D;
		
		double incrSalP = 0;
		
		double incrSuccP = 0;
		
		for(int i=0;i<m_pResultVec.size();i++){
			ProfitResultNode obj = m_pResultVec.get(i);
												
			if(obj.F0 == 0 || obj.F1 == 0){
				continue;
			}
			
            incrSalP += obj.salePNum; 
			
			incrSuccP += obj.predSuccPNum;
			
			obj.F2 = (double)incrSuccP/(double)incrSalP; //F2
			
			if(obj.F0>=para_F0_1 && obj.F0<=para_F0_2 && obj.F1>= para_F1_1 && obj.F1<=para_F1_2 && succRate>=para_F2_1 && succRate<= para_F2_2){
				double tmpP = getProfit(succRate, obj,totalNum);
				
				if(tmpP>maxP){
					maxNodeNum = i;
				}
				obj.isShowToPGraph = true;
			}
									
		}
		
	}
	
	private double getProfit(double alfa, ProfitResultNode obj, long totalNum){
		double P = -999999999999D;
		
		switch(rocType){
		case ROC_EXT:
			P = (( para_N* alfa* obj.F0 *(para_V-para_c-para_d)) - (para_N* (1-alfa)* obj.F1* (para_b+para_d)))/10000;			
		    obj.exportProfitVal = ((obj.predSuccPNum*(para_V-para_c-para_d)) - (obj.distPNum*(para_d+para_b)))*((double)para_N/(double)totalNum);
			break;

		case ROC_SMS:
			P = (( para_N* alfa* obj.F0 *(para_V-para_c-para_d)) - (para_N* (1-alfa)* obj.F1* (para_b+para_d)))/10000;
			obj.exportProfitVal = ((obj.predSuccPNum*(para_V-para_c-para_d)) - (obj.distPNum*(para_d+para_b)))*((double)para_N/(double)totalNum);
			break;
		
		case ROC_CUST:
			P = (( para_N* alfa* obj.F0 *(para_¦Ã* para_V-para_¦Ã* para_c-para_d)) - (para_N* (1-alfa)* obj.F1* (para_d+para_c)))/10000;
			obj.exportProfitVal = ((obj.predSuccPNum*(para_¦Ã* para_V-para_¦Ã* para_c-para_d)) - (obj.distPNum*(para_d+para_c)))*((double)para_N/(double)totalNum);
			break;
			
		case ROC_APP:
			P = (( para_N* alfa* obj.F0 *para_¦Ã* (para_V-para_c)) - (para_N* (1-alfa)* obj.F1* para_c))/10000;
			obj.exportProfitVal = ((obj.predSuccPNum*para_¦Ã* (para_V-para_c)) - (obj.distPNum*para_c))*((double)para_N/(double)totalNum);
			break;
		
		case ROC_WARN:
			P = (( para_N* alfa* obj.F0 *(para_¦Ã* para_V-para_¦Ã* para_c-para_d)) - (para_N* (1-alfa)* obj.F1* (para_d+para_c)))/10000;
			obj.exportProfitVal = ((obj.predSuccPNum*(para_¦Ã* para_V-para_¦Ã* para_c-para_d)) - (obj.distPNum*(para_d+para_c)))*((double)para_N/(double)totalNum);
			break;
			
		default:

			break;			
	    }
		
		obj.profitVal = P;			
		obj.exportSalePNum = obj.salePNum * ((double)para_N/(double)totalNum);
		
		return P;
	}
	
	private void drawGraph(){
		ProfitGraph  graphHandler = new ProfitGraph(m_pResultVec,rocType,maxNodeNum);
		
		graphHandler.show();
		
	}


}
