package com.xn.alex.data.rule;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.datimport.CompareObject;
import com.xn.alex.data.resultobj.treeNodeResultObj;

public class GenerateTreeByLeaf {
	
	private static Vector<treeNodeResultObj> m_treeNodeVec = new Vector<treeNodeResultObj>();	

	private static Vector<Vector<treeNodeResultObj>> m_treeNodeByLevelVec = new Vector<Vector<treeNodeResultObj>>();

	private static treeNodeResultObj resultTree = null; 
	
	private enum NODE_RELATION{NO_RELATION, BROTHER, COUSIN};
		
	private int errLineNum = -1;
	
	public static Vector<Vector<treeNodeResultObj>> getTreeNodeByLevelVec() {
		return m_treeNodeByLevelVec;
	}
	
	public int getErrLineNum() {
		return errLineNum;
	}
	
	public static Vector<treeNodeResultObj> getTreeLeafNodeVec() {
		return m_treeNodeVec;
	}

	public boolean generateTree(Vector<String> contentVec){
		
		if(false == convertTxtContetToTreeNode(contentVec)){
			return false;
		}
		
		m_treeNodeByLevelVec.clear();
		
		//m_treeNodeVec.clear();
		
		//resultTree = null;
		
		buildRootNode(m_treeNodeByLevelVec);
		
		buildLeafNode(m_treeNodeByLevelVec);
		
		//buildEmptyNode(m_treeNodeByLevelVec);
		
		for(int i=m_treeNodeByLevelVec.size()-1;i>0;i--){		
		    deduceOtherNode(m_treeNodeByLevelVec, i);		
		}
        
        resultTree =  m_treeNodeByLevelVec.get(0).get(0);
        
        return true;
		
	}
	
	private boolean deduceOtherNode(Vector<Vector<treeNodeResultObj>> treeNodeByLevel, int startLevel){
		
		//buildEmptyNode(treeNodeByLevel);
		
		Vector<treeNodeResultObj> curLevelVec = treeNodeByLevel.get(startLevel);
		
		Vector<treeNodeResultObj> upLevelVec = treeNodeByLevel.get(startLevel-1);
		//from right side to left to deduce parent node
		int curnodeNum = curLevelVec.size();
		
        treeNodeResultObj rightNode = null;
		
		treeNodeResultObj leftNode = null;
		
		treeNodeResultObj upObj = null;
		
        for(int curIndex=curnodeNum-1;curIndex>=0;curIndex--){
        	leftNode = curLevelVec.get(curIndex);
        	
        	NODE_RELATION relation = getNodeRelation(leftNode, rightNode);
        	
        	if(relation == NODE_RELATION.BROTHER){
        		
        		if(null == upObj){
        			upObj = getNextUplevelObj(upLevelVec, curLevelVec.get(0).nodeNumber);
        			
        			upObj.childNodeVec = new Vector<treeNodeResultObj>();
        			
        			upObj.chnColumnName = getParentChnColumnVec(leftNode);
        			
        			upObj.condition = getParentCondition(leftNode);
        			
        			upObj.objectLevel = startLevel - 1;
        			
        			upObj.nodeInfo = "节点号：" + upObj.nodeNumber + "\n";
        			
        			upObj.nodeInfo += "预测结果：" + upObj.Prediction + "\n";
        	    	
        			upObj.nodeInfo += "概率：" + upObj.Probability; 
        			        			        			
        			transLateConditionToSql(upObj);
        		}
    			
    			upObj.childNodeVec.add(leftNode);
    			
    			TreeCompareObj compareObj = new TreeCompareObj();
    			
    			Collections.sort(upObj.childNodeVec, compareObj);						
    		}
        	else if(relation == NODE_RELATION.COUSIN||rightNode == null){
        		
        		if(null == upObj){
        			//for the most right node
        			upObj = getNextUplevelObj(upLevelVec, curLevelVec.get(0).nodeNumber);       		
        		}
        		else{
        		    upObj = getNextUplevelObj(upLevelVec, upObj.nodeNumber);
        		}
        		
        		upObj.childNodeVec = new Vector<treeNodeResultObj>();
      			
      			upObj.chnColumnName = getParentChnColumnVec(leftNode);
      			
      			upObj.condition = getParentCondition(leftNode);
      			
      			upObj.nodeInfo = "节点号：" + upObj.nodeNumber + "\n";
      			
      			upObj.nodeInfo += "预测结果：" + upObj.Prediction + "\n";
    	    	
    			upObj.nodeInfo += "概率：" + upObj.Probability; 
      			
      			upObj.objectLevel = startLevel - 1;
      			
      			transLateConditionToSql(upObj);
        		        		
        		upObj.childNodeVec.add(leftNode);        		
        	}
        	else{
        		System.out.println("节点" + curIndex + "所处层级不正确");
        		
        		return false;
        	}        	        	
        	
        	rightNode = leftNode;
        }
														
		return true;		
	}
	
	private treeNodeResultObj getNextUplevelObj(Vector<treeNodeResultObj> upLevelVec, int previousObjNum){
		treeNodeResultObj resultObj = null;
		//from right to left search
		if(upLevelVec.size() == 0){
			resultObj = new treeNodeResultObj();
			resultObj.nodeNumber = previousObjNum - 1;
			resultObj.nodeName = "节点" + resultObj.nodeNumber;
			upLevelVec.add(resultObj);
			return resultObj;
		}
		
		int findNodeNum = previousObjNum - 1;
		if(findNodeNum == 0){
			//root node
			return upLevelVec.get(0);
		}
		
		for(int i=upLevelVec.size()-1;i>=0;i--){
			if(upLevelVec.get(i).nodeNumber == findNodeNum){				
				if(upLevelVec.get(i).isLeaf == false){
				    resultObj = upLevelVec.get(i);
				    return resultObj;
				}
				findNodeNum = findNodeNum - 1;
				if(findNodeNum==0){
				    return null;
				}
			}
			else if(findNodeNum - upLevelVec.get(i).nodeNumber > 1){
				//gap is larger than 1, not we need
				break;
			}
		}
		
		resultObj = new treeNodeResultObj();
		resultObj.nodeNumber = findNodeNum;
		resultObj.nodeName = "节点" + resultObj.nodeNumber;
		upLevelVec.add(resultObj);
		TreeCompareObj compareObj = new TreeCompareObj();				
		Collections.sort(upLevelVec, compareObj);
				
		return resultObj;		
	}
	
	private boolean convertTxtContetToTreeNode(Vector<String> contentVec){
		m_treeNodeVec.clear();
		
		if(false == storeSqlContentToTreeNodeVec(contentVec)){
			return false;
		}
		
		if(0 == m_treeNodeVec.size()){
			return false;
		}
				
		return true;
	}
	
	
	private void buildLeafNode(Vector<Vector<treeNodeResultObj>> treeNodeByLevel){
		try{
		    for(int i=0;i<m_treeNodeVec.size();i++){
			    treeNodeResultObj leafNode = m_treeNodeVec.get(i);
			    
			    int leafNodeLevel = leafNode.chnColumnName.size();
			    
			    int curTreeLevel = treeNodeByLevel.size();
			    
                int gap = leafNodeLevel - curTreeLevel;
                
                while(gap>=0){
                	//fill with empty vec to construct level tree
                	Vector<treeNodeResultObj> tmpVec = new Vector<treeNodeResultObj>();
                	
                	treeNodeByLevel.add(tmpVec);
                	
                	gap--;
                }
                
                Vector<treeNodeResultObj> levelVec = treeNodeByLevel.get(leafNodeLevel);
                
                levelVec.add(leafNode);           
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	private void buildRootNode(Vector<Vector<treeNodeResultObj>> treeNodeByLevel){
		treeNodeResultObj rootObj = new treeNodeResultObj();
		
		rootObj.nodeName = "节点0";
		
		rootObj.nodeNumber = 0;
		
		rootObj.objectLevel = 0;
		
		Vector<treeNodeResultObj> rootLevelVec = new Vector<treeNodeResultObj>();
		
		rootLevelVec.add(rootObj);		
		//level 0 only has 1 node: root node
		treeNodeByLevel.add(rootLevelVec);
				
	}
	
	private NODE_RELATION getNodeRelation(treeNodeResultObj leftObj, treeNodeResultObj rightObj){
		
		NODE_RELATION relation = NODE_RELATION.NO_RELATION;
		
		if(null == leftObj || null == rightObj){
			return relation;
		}
		
		if(leftObj.chnColumnName.size() != rightObj.chnColumnName.size()){			
			return relation;
		}
		
		for(int i=0;i<leftObj.chnColumnName.size();i++){
			
			if(false == leftObj.chnColumnName.get(i).equals(rightObj.chnColumnName.get(i))){
				
				relation = NODE_RELATION.COUSIN;
				
				return relation;
			}
		}
		
		if(leftObj.chnColumnName.size() == 1){
			return NODE_RELATION.BROTHER;
		}
		
		String leftObjCond = leftObj.condition;
		
		leftObjCond = leftObjCond.replaceAll("NOT\\(","(");
		
		leftObjCond = leftObjCond.replaceAll("WHERE \\(","");
		
		leftObjCond = leftObjCond.replaceAll("\\(+","(");
		
		String rightObjCond = rightObj.condition;
		
		rightObjCond = rightObjCond.replaceAll("NOT\\(","(");
		
		rightObjCond = rightObjCond.replaceAll("WHERE \\(","");
		
		rightObjCond = rightObjCond.replaceAll("\\(+","(");
		
		String[] leftSplitArray = leftObjCond.split("\\)  AND  \\(");
		
		String[] rightSplitArray = rightObjCond.split("\\)  AND  \\(");
		
		int maxInd = rightSplitArray.length > leftSplitArray.length ? rightSplitArray.length : leftSplitArray.length;
		int fieldNum = 0;
		for(int i=0;i<maxInd;i++){
			String leftSplitTmpStr = leftSplitArray[i].trim();
			String rightSplitTmpStr = rightSplitArray[i].trim();
			
			fieldNum = getCurrentFieldNum(leftSplitTmpStr, leftObj.chnColumnName);
			
			if(leftObj.chnColumnName.size() - fieldNum < 1){
				break;
			}
			
			if(false == leftSplitTmpStr.equals(rightSplitTmpStr)){
				return NODE_RELATION.COUSIN;
			}
		}
		
		//size and name are the same then brother relation
		relation = NODE_RELATION.BROTHER;
		
		return relation;
	}
	
	private int getCurrentFieldNum(String checkStr, Vector<String> fieldVec){
		try{
		    int fieldNum = -1;
		
		    for(int i=0;i<fieldVec.size();i++){
		    	String field = fieldVec.get(i);
		    	if(field.length()>checkStr.length()){
		    		continue;
		    	}
		    	
		    	String tmpStr = checkStr.substring(0, field.length());
		    	
		    	if(tmpStr.equals(field)){
		    		fieldNum = i+1;
		    		break;
		    	}
		    }
		    return fieldNum;
		}
		catch(Exception e){
		
		    return -1;
		}
	}
	
	private boolean storeSqlContentToTreeNodeVec(Vector<String> contentVec){
        treeNodeResultObj tmpObj = null;
		
		if(contentVec.size()%4 != 0){
			errLineNum = contentVec.size();
			
			System.out.println("文件内容行数不正确");
			
			return false;
		}
		
		for(int i=0;i<contentVec.size();i++){
			int mod = (i)%4 + 1;
			
			String line = contentVec.get(i);
			
			switch(mod){
			    case 1:
			    	tmpObj = new treeNodeResultObj();
			    	if(false == handleFirstLine(line, tmpObj)){
			    		errLineNum = i;
				    	return false;
			    	}
			    	break;
			    
			    case 2:
			    	if(false == handleSecondLine(line, tmpObj)){
			    		errLineNum = i;
				    	return false;
			    	}
			    	break;
			    
			    case 3:
			    	if(false == handleThirdLine(line, tmpObj)){
			    		errLineNum = i;
				    	return false;
			    	}
			    	break;
			    
			    case 4:
			        if(false == handleFourthLine(line, tmpObj)){
			        	errLineNum = i;
				    	return false;
			        }
			        transLateConditionToSql(tmpObj);
			        m_treeNodeVec.add(tmpObj);
			        break;
			    
			    default:
			    	errLineNum = i;
			    	return false;
			
			}
						
		}		
		return true;	
	}
	
	private boolean handleFirstLine(String line, treeNodeResultObj obj){
		
		Pattern p = Pattern.compile("^\\/\\*\\s+Node\\s+(\\d+)\\s+\\*\\/");
		
		Matcher m = p.matcher(line);
		
		if(m.find()){
			int nodeNumber = Integer.parseInt(m.group(1));
			
			obj.nodeName = "节点" + nodeNumber;
			
			return true;
		}
		return false;
	}
	
	private boolean handleSecondLine(String line, treeNodeResultObj obj){
		return true;
	}
	
    private boolean handleThirdLine(String line, treeNodeResultObj obj){
    	String tmpLine = line.replace("SET", "");
    	
    	tmpLine = tmpLine.trim();
    	
    	String[] resultArray = tmpLine.split(",");
    	
    	for(int i=0;i<resultArray.length;i++){
    		String[] tmpResult = resultArray[i].split("=");
    		
    		switch(tmpResult[0].trim()){
    		    case "nod_001":
    		    	obj.nodeNumber =  Integer.parseInt(tmpResult[1].trim());
    		    	break;
    		    
    		    case "pre_001":
    		    	obj.Prediction = tmpResult[1].trim();
    		    	break;
    		    
    		    case "prb_001":
    		    	obj.Probability = tmpResult[1].trim();
    		    	break;
    		    
    		    default:
    		    	return false;
    		
    		}    		
    	}
    	
    	obj.nodeInfo = "节点号：" + obj.nodeNumber + "\n";  
    	
    	obj.nodeInfo += "预测结果：" + obj.Prediction + "\n";
    	
    	obj.nodeInfo += "概率：" + obj.Probability; 
    			
    	return true;
	}
    
    private boolean handleFourthLine(String line, treeNodeResultObj obj){
    	
    	obj.condition = line;
    	    	    	    	
    	String tmpLine = line.replace("WHERE", "");
    	
    	//remove all special characters
    	tmpLine = tmpLine.replaceAll("(NOT)|(OR)|(AND)|(IS NULL)|(<>)|(>)|(<)|\\(|\\)|;", "");
    	
    	String resultArray[] = tmpLine.split("\\s+|\\s");
    	
    	for(int i=0;i<resultArray.length;i++){
    		String tmpChnColumnName = resultArray[i].trim();
    		if("".equals(tmpChnColumnName)){
    			continue;
    		}
    		
    		if(null == ConfigParser.chnToEnColumnName.get(tmpChnColumnName)){
    			continue;
    		}
    		
    		if(null == obj.chnColumnName){
    			obj.chnColumnName = new Vector<String>();
    		}
    		
    		if(obj.chnColumnName.contains(tmpChnColumnName)){
    			continue;
    		}
    		
    		obj.chnColumnName.add(tmpChnColumnName);    		
    	}
    	
    	obj.isLeaf = true;
    	
    	obj.objectLevel = obj.chnColumnName.size();
    	
    	return true;
	}
    
    public static treeNodeResultObj getResultNode(){
    	//return root node
    	return resultTree;
    }
    
    private void transLateConditionToSql(treeNodeResultObj obj){
    	
    	List<CompareObject> compareVec = new Vector<CompareObject>();
    	//need make it in order and replace it from longest str
    	for(int i=0;i<obj.chnColumnName.size();i++){
    		CompareObject tmpObj = new CompareObject();
    		tmpObj.objName = obj.chnColumnName.get(i);
    		tmpObj.compareVlue = tmpObj.objName.length();
    		compareVec.add(tmpObj);
    	}
    	
        Comparator<Object> ct = new CompareObject();		
		Collections.sort(compareVec, ct);
		
		obj.sql = obj.condition;
		
		for(int i=compareVec.size()-1;i>=0;i--){
			String chnColumnName = compareVec.get(i).objName;
			
			String enColumnName = ConfigParser.chnToEnColumnName.get(chnColumnName);
			
			obj.sql = obj.sql.replaceAll(chnColumnName, enColumnName);
		}
    	
    }
    
    private String getParentCondition(treeNodeResultObj childObj){
    	try{
    	    String parentCond = "";
    	
    	    String childCond = childObj.condition;
    	
    	    int childCondFieldNum = childObj.chnColumnName.size();    	
    	
    	    String result[] = childCond.split("\\)  AND  \\(");
    	
    	    int parentCondFiledNum = childCondFieldNum - 1;
    	
    	    if(parentCondFiledNum == 0){
    		    return parentCond;
    	    }
    	    		
    		String field = childObj.chnColumnName.get(parentCondFiledNum);

    		int checkPos = 0;
    		for(checkPos=parentCondFiledNum;checkPos<result.length;checkPos++){
    		    String tmpSplitStr = result[checkPos];
    		    
    		    tmpSplitStr = tmpSplitStr.replaceAll("NOT\\(","");
    		    
    		    tmpSplitStr = tmpSplitStr.replaceAll("\\(+","");
    			
    		    String tmpField = tmpSplitStr.trim().substring(0, field.length());
    			
    		    if(field.equals(tmpField)){
    		    	break;
    		    }
    		}
    		
    		if(checkPos == result.length){
    			return parentCond;
    		}
    		
    		for(int i=0;i<checkPos;i++){
    			parentCond += result[i];
    			if(i != checkPos-1){
    				parentCond += ")  AND  (";
    			}
    			else{
    				parentCond += ")";
    			}
    		}
    	    
    	    return parentCond;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}    	
    }
    
    private Vector<String> getParentChnColumnVec(treeNodeResultObj obj){
    	Vector<String> parentColumnVec = new Vector<String>();
    	
    	int parentFieldNum = obj.chnColumnName.size() - 1;
    	
    	for(int i=0;i<parentFieldNum;i++){
    		parentColumnVec.add(obj.chnColumnName.get(i));
    	}
    	
    	return parentColumnVec;
    }

}
