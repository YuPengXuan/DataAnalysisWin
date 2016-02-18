package com.xn.alex.data.rule;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.xn.alex.data.resultobj.ProfitResultNode;
import com.xn.alex.data.resultobj.algorithmResultObj2;
import com.xn.alex.data.resultobj.treeNodeResultObj;

public class RocParameterGenerate {
	
	private Vector<treeNodeResultObj> m_leafNodeVec = null;
	
	private Vector<algorithmResultObj2> showVec = null;
	
	public RocParameterGenerate(Vector<treeNodeResultObj> leafNodeVec){
		m_leafNodeVec = leafNodeVec;
	}
	
	public Vector<ProfitResultNode> getProfitVec(){
		Vector<ProfitResultNode> resultVec = new Vector<ProfitResultNode>();
		
		CalculateProfitVal(resultVec);
		
		return resultVec;
	}
	
	private void CalculateProfitVal(Vector<ProfitResultNode> resultVec){
		if(null == m_leafNodeVec || m_leafNodeVec.size() == 0){
			return;
		}
		
		if(null == showVec || showVec.size() == 0){			
			getShowVec();
			return;
		}
		
		Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
		
		for(int i=0;i<showVec.size();i++){
			algorithmResultObj2 obj = showVec.get(i);
			if(null == obj){
				continue;
			}			
			positionMap.put(obj.nodeNum, i);
		}
		
		for(int j=0;j<m_leafNodeVec.size();j++){
			treeNodeResultObj leafObj = m_leafNodeVec.get(j);
			if(null == leafObj){
				continue;
			}
			
			int position = positionMap.get(leafObj.nodeNumber);
			
			algorithmResultObj2 tmpObj = showVec.get(position);
			
			ProfitResultNode pObj = new ProfitResultNode();
			
			pObj.F1 = tmpObj.x.doubleValue();
			
			pObj.F0 = tmpObj.y.doubleValue();
			
			pObj.nodeNum = leafObj.nodeNumber;
			
			pObj.salePNum = leafObj.conditionMeet + leafObj.conditionNotMeet;
			
            pObj.predSuccPNum = leafObj.conditionMeet;
            
            pObj.F2 = leafObj.conditionMeet/pObj.salePNum;
            
            pObj.condition = leafObj.condition;
            
            pObj.distPNum = leafObj.conditionNotMeet;
            
            resultVec.add(pObj);
            
		}
		
		
	}
	
	public Vector<algorithmResultObj2> getShowVec(){
		if(null == showVec){
			showVec = new Vector<algorithmResultObj2>();
		}
		
		ordyByDesc(showVec);
		
		sumByOrder(showVec);
		
		normalization(showVec);
		
		algorithmResultObj2 zeroObj = new algorithmResultObj2();
		
		zeroObj.x = (float)0;
		
		zeroObj.y = (float)0;
		
		zeroObj.type = showVec.get(0).type;
		
		showVec.add(zeroObj);
		
		return showVec;
	}
	
	private void normalization(Vector<algorithmResultObj2> showVec){
		
		int size = showVec.size();
		
		if(size == 0){
			return;
		}
		
		float maxValueX = showVec.get(size-1).x.floatValue();
		
		float maxValueY = showVec.get(size-1).y.floatValue();
		
		for(int i=0;i<showVec.size();i++){
			algorithmResultObj2 obj = showVec.get(i);
			
			obj.y = obj.y.floatValue() / maxValueY;
			
			obj.x = obj.x.floatValue() / maxValueX;
			
		}	
				
	}
	
	private void sumByOrder(Vector<algorithmResultObj2> showVec){
		
		for(int i=1;i<showVec.size();i++){
			algorithmResultObj2 obj = showVec.get(i);				

			algorithmResultObj2 preObj = showVec.get(i-1);
				
			obj.x = obj.x.floatValue() + preObj.x.floatValue();
				
			obj.y = obj.y.floatValue() + preObj.y.floatValue();

		}
		
	}
	
	private void ordyByDesc(Vector<algorithmResultObj2> showVec){
		if(null == m_leafNodeVec){
			return;
		}
		
		for(int i=0;i<m_leafNodeVec.size();i++){
			treeNodeResultObj obj = m_leafNodeVec.get(i);
			
			if(null == obj){
				continue;
			}
			
			algorithmResultObj2 cmpObj = new algorithmResultObj2();
			
			cmpObj.x = obj.conditionNotMeet;
			
			if(cmpObj.x.floatValue() == 0){
				cmpObj.x = 1;
			}
			
			cmpObj.y = obj.conditionMeet;
			
			if(cmpObj.y.floatValue()  == 0){
				cmpObj.y = 1;
			}
			
			cmpObj.yDivX = cmpObj.y.floatValue() /cmpObj.x.floatValue();
			
			cmpObj.type = "F1(X)£ºF0(Y)";
			
			cmpObj.nodeNum = obj.nodeNumber;
						
			showVec.add(cmpObj);
		}
		
		Collections.sort(showVec, new algorithmResultObj2());
	}

}
