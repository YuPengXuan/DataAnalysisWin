package com.xn.alex.data.c45;


import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

import com.xn.alex.data.resultobj.treeNodeResultObj;




class ContinuousEX
{
	public ContinuousEX(double value,String target)
	{                                                                                           
		this.value=value;
		
		this.target=target;
		
	}
	public double value;
	
	public String target;
	
}
class EXComparator implements Comparator<ContinuousEX>
{
	public int compare(ContinuousEX a,ContinuousEX b)
	{
		if(a.value>b.value)
			return 1;
		
		if(a.value==b.value)
			return 0;
		
		else
			return -1;
	}
}

public class DecisionTree {
	
	public static Vector<treeNodeResultObj> entireTree=new Vector<treeNodeResultObj>();
	
	public int maxSon =5;
	
	public static int nodeNum=0;
	
	public DecisionTree(int maxSon){
		
		this.maxSon=maxSon;
		
		nodeNum=0;
		
	}
	
	
	public treeNodeResultObj createDT(Vector<Vector<String>> data, Vector<String> attributeList, int length){
		length--;
		
		nodeNum++;
//		System.out.println("当前的data为");
//		for(int i=0;i<data.size();i++){
//			Vector<String> temp = data.get(i);
//			for(int j=0;j<temp.size();j++){
//				System.out.printf("%-15s",temp.get(j));
//			}
//			System.out.println();
//		}
//		System.out.println("-------------------------------------------------");
//		System.out.println("当前的attr为");
//		for(int i=0;i<attributeList.size();i++){
//			System.out.printf("%-15s",attributeList.get(i));
//		}
//		 System.out.println();
//	     System.out.println("---------------------------------");
//	     
		 
		 treeNodeResultObj node = new treeNodeResultObj();//新建一个节点
		 
	     entireTree.addElement(node);
	     
	     String result = InfoGain.IsPure(InfoGain.getTarget(data));//??????????????????????????????
	     
	     //增加节点层数信息（level）
	     node.objectLevel=maxSon-length-1;
	     
	     node.nodeName="Node"+nodeNum;
	     
	     //增加节点概率信息
	     int yesNo=0,noNo=0,pre =0;
	     
	     for(int i=0;i<data.size();i++){
	    	 
	    	 if (data.get(i).lastElement().equals("1")||data.get(i).lastElement().equals("是")||data.get(i).lastElement().equals("yes")||data.get(i).lastElement().equals("1.0"))
	    		 
	    		 yesNo++;
	    	 
	    	 else 
	    		 
	    		 noNo++;
	     }
	     
	     double pro=(double)yesNo/(double)data.size();
	     
	     if (pro>0.5)
	    	 
	    	 pre=1;
	     
	     else{
	    	 
	    	 pre=0;
	    	 
	    	 pro=1-pro;
	    	 
	     }
	     
	     BigDecimal probability= new BigDecimal(pro);
	     
	     node.Probability=String.valueOf(probability.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	     
	     node.conditionMeet=yesNo;
	     
	     node.conditionNotMeet=noNo;
	     
	     node.Prediction=String.valueOf(pre);
	     
	     if(result != null||attributeList.size()==1||length<=0){
	            //node.setNodeName(attributeList.get(attributeList.size()-1));
	    	 
	            //node.nodeName="leafnode";
	            
	            node.targetFunValue=result;
	            
	            node.nodeInfo=node.pathName+"\n"+"概率 : "+node.Probability+"\n"+"预测 : "+node.Prediction;
	            
	            return node;
	        }
	     
	     if(attributeList.size() == 0){
	            node.nodeName="Failure";
	            node.targetFunValue=result;
	            return node;
	        }
	        
	     else {
	            
		   	     for (int i = 0; i < attributeList.size(); i++) {
		   	    	 
		   	    	  Vector<String> s =new Vector<String>();
		   	    	  
		   	    	  for (int j = 0; j < data.size(); j++) {
			 				//对整列属性数据进行离散化
			 				s.addElement(data.get(j).get(i));
		   	    	  }
		   	    	  
		   	    	  s=Dispersed.dispersed(s);
		   	    	  
		   	    	for (int j = 0; j < data.size(); j++) {
		 				//对整列属性数据进行离散化
		   	    		
		 				data.get(j).set(i, s.get(j));
	   	    	  }
		   	    	
		   	     }
		   	     
		   	     for(int i=0,temp=0; i<attributeList.size();i++){
		   	    	 
		   	    	 Vector <String> s= new Vector<String>();
		   	    	 
		   	    	 for(int j=0; j<data.size();j++){
		   	    		 
		   	    		 s.add(data.get(j).get(i));
		   	    		 
		   	    	 }
		   	    	 if(Dispersed.computeCountString(s)>=maxSon){
		   	    		 
		   	    		for (int j = 0; j < data.size(); j++) {
		   	    			
	                        data.get(j).remove(temp);
	                        
	                    }
		   	    		
	                	attributeList.remove(temp);
	                	
		   	    	 }
		   	    	 
		   	    	 else
		   	    		 
		   	    		 temp++;
		   	    	 
		   	     }
		   	  InfoGain gain = new InfoGain(data,attributeList);
		   	  
	            double maxGain = 0.0;
	            
	            int attrIndex = -1;
	            
	            int size=attributeList.size();
	            
	            for(int i=0,temp=0;i<size-1;i++){
	            	
	                double tempGain = gain.getGainRatio(i);
	                
	                if(tempGain==0){
	                	
	                	for (int j = 0; j < data.size(); j++) {
	                		
	                        data.get(j).remove(temp);
	                        
	                    }
	                	
	                	attributeList.remove(temp);
	                	
	                }
	                else
	                	
	                	temp++;
	                
	            }
	            
	            if(result != null||attributeList.size()==1){
	            	
		            //node.setNodeName(attributeList.get(attributeList.size()-1));
	            	
		            //node.nodeName="leafnode";
		            
		            node.targetFunValue=result;
		            
		            node.nodeInfo=node.attributeValue+" : "+node.pathName+"\n"+"概率 : "+node.Probability+"\n"+"预测 : "+node.Prediction;
		            
		            return node;
		            
		        }
	            
	            gain = new InfoGain(data,attributeList);
	            
	            for(int i=0;i<attributeList.size()-1;i++){
	            	
	                double tempGain = gain.getGainRatio(i);
	                
	                if(maxGain < tempGain){
	                	
	                    maxGain = tempGain;
	                    
	                    attrIndex = i;
	                    
	                }
//	               System.out.println("第i列："+i);
	            }
//	            System.out.println("选择出的最大信息增益率属性为： " + attributeList.get(attrIndex))
	            node.attributeValue=attributeList.get(attrIndex);
	            
	            
	            Vector<Vector<String>> resultData = null;
	            
	            Map<String,Long> attrvalueMap = gain.getAttributeValue(attrIndex);
	            
	            /////////////////////////
	            for(Map.Entry<String, Long> entry : attrvalueMap.entrySet()){
	            	
	                resultData = gain.getData4Value(entry.getKey(), attrIndex);
	                
	                treeNodeResultObj leafNode = null;
	                
//	                System.out.println("当前为"+attributeList.get(attrIndex)+"的"+entry.getKey()+"分支。");
	                if(resultData.size() == 0||attributeList.size()==1){
	                	
	                    leafNode = new treeNodeResultObj();
	                    
	                    leafNode.nodeName=attributeList.get(attrIndex);
	                    
	                    leafNode.targetFunValue=result;
	                    
	                    leafNode.attributeValue=entry.getKey();
	                    
	                    node.nodeInfo=node.attributeValue+" : "+node.pathName+"\n"+"概率 : "+node.Probability+"\n"+"预测 : "+node.Prediction;
	                    
	                    entireTree.addElement(leafNode);
	                    
	                }
	                else{
	                	
	                    for (int j = 0; j < resultData.size(); j++) {
	                    	
	                        resultData.get(j).remove(attrIndex);
	                        
	                    }
	                    
	                    Vector<String> resultAttr = new Vector<String>(attributeList);
	                    
	                    resultAttr.remove(attrIndex);
	                    
	                    node.nodeInfo=node.attributeValue+" : "+node.pathName+"\n"+"概率 : "+node.Probability+"\n"+"预测 : "+node.Prediction;
	                    
	                    leafNode = createDT(resultData,resultAttr,length);
	                    
	                }
	                
	                node.childNodeVec.add(leafNode);
	                
	                node.pathName.add(entry.getKey());
	                
	            }
	        
	        return node;
	    }}
}
	
//	    class TreeNode{
//	        
//	        private String attributeValue;
//	        private Vector<TreeNode> childTreeNode;
//	        private Vector<String> pathName;
//	        private String targetFunValue;      //这是什么？
//	        private String nodeName;
//
//	        
//	        public TreeNode(String nodeName){
//	            
//	            this.nodeName = nodeName;
//	            this.childTreeNode = new Vector<TreeNode>();
//	            this.pathName = new Vector<String>();
//	        }
//	        
//	        public TreeNode(){
//	            this.childTreeNode = new Vector<TreeNode>();
//	            this.pathName = new Vector<String>();
//	        }
//
//	        public String getAttributeValue() {
//	            return attributeValue;
//	        }
//
//	        public void setAttributeValue(String attributeValue) {
//	            this.attributeValue = attributeValue;
//	        }
//
//	        public Vector<TreeNode> getChildTreeNode() {
//	            return childTreeNode;
//	        }
//
//	        public void setChildTreeNode(Vector<TreeNode> childTreeNode) {
//	            this.childTreeNode = childTreeNode;
//	        }
//
//	        public String getTargetFunValue() {
//	            return targetFunValue;
//	        }
//
//	        public void setTargetFunValue(String targetFunValue) {
//	            this.targetFunValue = targetFunValue;
//	        }
//
//	        public String getNodeName() {
//	            return nodeName;
//	        }
//
//	        public void setNodeName(String nodeName) {
//	            this.nodeName = nodeName;
//	        }
//
//	        public Vector<String> getPathName() {
//	            return pathName;
//	        }
//
//	        public void setPathName(Vector<String> pathName) {
//	            this.pathName = pathName;
//	        }
//	        public String toString(){
//	        	return "NodeName:"+this.getNodeName()+
//	        			"  AttrbuteValue:"+this.getAttributeValue()+
//	        			"  pathName"+this.getPathName()+
//	        			"  TargetFunValue:"+this.getTargetFunValue()+
//	        			"  childTreeNode:"+this.childTreeNode;
//	        }
//	}
//
//}
