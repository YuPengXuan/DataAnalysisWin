package com.xn.alex.data.resultobj;

import java.util.Vector;

public class treeNodeResultObj {
	
	public int nodeNumber = -1;
	
	public String nodeName = null;       //node name
	
	public String Prediction = "-1";     // Prediction field in txt
	
	public String Probability = "-1";    // Probability field in txt
	
    public String condition =null;    // store condition and it's value
    
    public String sql = null;        //convert condtion to sql 
	
	public int objectLevel = -1;                // the level number of the tree, top node should be 0
	
	public String nodeInfo = null;       // output node Info
	
	public boolean isLeaf = false;       // is a leaf node or not?
	
	public Vector<treeNodeResultObj> childNodeVec = null;  //store child node info
	
	public Vector<String> chnColumnName = null;
	
	public long conditionMeet = -1;
	
	public long conditionNotMeet = -1;
	
	public String targetFunValue = null;
    
    public String attributeValue = null;
    
    public Vector<String> pathName =new Vector<String>();

}
