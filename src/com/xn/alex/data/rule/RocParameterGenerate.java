package com.xn.alex.data.rule;

import java.util.Collections;
import java.util.Vector;

import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.treeNodeResultObj;

public class RocParameterGenerate {
	
	private Vector<treeNodeResultObj> m_leafNodeVec = null;
	
	private Vector<ProfitResultNode> showVec = new Vector<ProfitResultNode>();
	
	public RocParameterGenerate(Vector<treeNodeResultObj> leafNodeVec){
		m_leafNodeVec = leafNodeVec;
	}
	
	public Vector<ProfitResultNode> getShowVec(){
		
		ordyByDesc(showVec);
		
		sumByOrder(showVec);
		
		normalization(showVec);
		
		ProfitResultNode zeroObj = new ProfitResultNode();
		
		zeroObj.F0 = (float)0;
		
		zeroObj.F1 = (float)0;
		
		zeroObj.type = showVec.get(0).type;
		
		showVec.add(zeroObj);
		
		return showVec;
	}
	
	private void normalization(Vector<ProfitResultNode> showVec){
		
		int size = showVec.size();
		
		if(size == 0){
			return;
		}
		
		double maxValueX = showVec.get(size-1).F1;
		
		double maxValueY = showVec.get(size-1).F0;
		
		for(int i=0;i<showVec.size();i++){
			ProfitResultNode obj = showVec.get(i);
			
			obj.F0 = obj.F0 / maxValueY;
			
			obj.F1 = obj.F1 / maxValueX;
			
		}	
				
	}
	
	private void sumByOrder(Vector<ProfitResultNode> showVec){
		
		for(int i=1;i<showVec.size();i++){
			ProfitResultNode obj = showVec.get(i);				

			ProfitResultNode preObj = showVec.get(i-1);
				
			obj.F1 = obj.F1 + preObj.F1;
				
			obj.F0 = obj.F0 + preObj.F0;

		}
		
	}
	
	private void ordyByDesc(Vector<ProfitResultNode> showVec){
		if(null == m_leafNodeVec){
			return;
		}
		
		treeNodeResultObj rootNode = GenerateTreeByLeaf.getTreeNodeByLevelVec().get(0).get(0);
		
		if(null == rootNode){
			return;
		}
		
		for(int i=0;i<m_leafNodeVec.size();i++){
			treeNodeResultObj obj = m_leafNodeVec.get(i);
			
			if(null == obj){
				continue;
			}
			
			ProfitResultNode cmpObj = new ProfitResultNode();
			
			cmpObj.F1 = obj.conditionNotMeet;
			
			if(cmpObj.F1 == 0){
				cmpObj.F1 = 1;
			}
			
			cmpObj.F0 = obj.conditionMeet;
			
			if(cmpObj.F0  == 0){
				cmpObj.F0 = 1;
			}
			
			cmpObj.F0divF1 = cmpObj.F0 /cmpObj.F1;
			
			cmpObj.type = "F1(X)£ºF0(Y)";
			
			cmpObj.nodeNum = obj.nodeNumber;
			
			cmpObj.salePNum = obj.conditionMeet + obj.conditionNotMeet;
			
			cmpObj.distPNum = obj.conditionNotMeet;
			
			cmpObj.predSuccPNum = obj.conditionMeet;
			
			cmpObj.peopleRatio = (double)cmpObj.salePNum/((double)rootNode.conditionMeet + (double)rootNode.conditionNotMeet);
			
			cmpObj.exportF0 = (double)obj.conditionMeet / (double)rootNode.conditionMeet;
			
			cmpObj.exportF1 = (double)obj.conditionNotMeet /(double)rootNode.conditionNotMeet;
			
			cmpObj.exportF2 = (double)obj.conditionMeet / ((double)obj.conditionMeet + (double)obj.conditionNotMeet);
						
			showVec.add(cmpObj);
		}
		
		Collections.sort(showVec, new ProfitResultNode());
	}

}
