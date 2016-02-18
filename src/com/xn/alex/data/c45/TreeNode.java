package com.xn.alex.data.c45;

import java.util.Vector;

public class TreeNode {
        private String attributeValue;
        
        private Vector<TreeNode> childTreeNode;
        
        private Vector<String> pathName;
        
        private String targetFunValue;      //’‚ « ≤√¥£ø
        
        private String nodeName;
        
        private double propbability;
        
        private int objLevel;
        
        public TreeNode(String nodeName){
            
            this.nodeName = nodeName;
            
            this.childTreeNode = new Vector<TreeNode>();
            
            this.pathName = new Vector<String>();
            
        }
        
        public TreeNode(){
            this.childTreeNode = new Vector<TreeNode>();
            
            this.pathName = new Vector<String>();
            
        }

        public String getAttributeValue() {
        	
            return attributeValue;
            
        }

        public void setAttributeValue(String attributeValue) {
        	
            this.attributeValue = attributeValue;
            
        }

        public Vector<TreeNode> getChildTreeNode() {
        	
            return childTreeNode;
            
        }

        public void setChildTreeNode(Vector<TreeNode> childTreeNode) {
        	
            this.childTreeNode = childTreeNode;
            
        }

        public String getTargetFunValue() {
        	
            return targetFunValue;
            
        }

        public void setTargetFunValue(String targetFunValue) {
        	
            this.targetFunValue = targetFunValue;
            
        }

        public String getNodeName() {
        	
            return nodeName;
            
        }

        public void setNodeName(String nodeName) {
        	
            this.nodeName = nodeName;
            
        }

        public Vector<String> getPathName() {
        	
            return pathName;
            
        }

        public void setPathName(Vector<String> pathName) {
        	
            this.pathName = pathName;
            
        }
        
        public void setProbability(double pro){
        	
        	this.propbability=pro;
        	
        }
        public double getProbability(){
        	
        	return this.propbability;
        	
        }
        public void setobjLevel(int lev){
        	
        	this.objLevel=lev;
        	
        }
        public int getobjLevel(){
        	
        	return this.objLevel;
        	
        }
        
        public String toString(){
        	
        	return "NodeName:"+this.getNodeName()+
        			
        			"  AttrbuteValue:"+this.getAttributeValue()+
        			
        			"  pathName"+this.getPathName()+
        			
        			"  TargetFunValue:"+this.getTargetFunValue()+
        			
        			"  childTreeNode:"+this.childTreeNode;
        	
        }
}

