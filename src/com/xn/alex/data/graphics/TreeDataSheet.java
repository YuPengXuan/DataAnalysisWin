package com.xn.alex.data.graphics;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import ict.topo.view.DrawGraph;
import ict.topo.view.TopoLink;
import ict.topo.view.TopoNode;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import com.xn.alex.data.resultobj.treeNodeResultObj;

public class TreeDataSheet{
	
	private static TreeDataSheet treeDataSheetHandler = null;
	
	private DrawGraph draw;

	private int treeMaxWidth = 0;
	
	private int currLevel = 1;
	
	private String preTitle = "";
	
	private Map<Integer, Integer> levelToNodeNumMap = new ConcurrentHashMap<Integer, Integer>();
	
	//indicate which node has been paint due to its level
	private Map<Integer, Integer> levelToNodeInPaintMap = new ConcurrentHashMap<Integer, Integer>();
	
	private Map<treeNodeResultObj, TopoNode> treeNodeHashCodeToTopoNodeMap = new HashMap<treeNodeResultObj, TopoNode>();
	
	private TreeDataSheet(){
		
	}
	
	public static TreeDataSheet Instance(){
		
		if(null == treeDataSheetHandler){
			
			treeDataSheetHandler = new TreeDataSheet();
			
		}
		
		return treeDataSheetHandler;		
	}
	
	public DrawGraph getDraw() {
		return draw;
	}
	
	public String getPreTitle() {
		return preTitle;
	}

	public void setPreTitle(String preTitle) {
		this.preTitle = preTitle;
	}

	
	private void clearPreviousVar(){
        levelToNodeNumMap.clear();
		
		levelToNodeInPaintMap.clear();
		
		treeNodeHashCodeToTopoNodeMap.clear();
		
		treeMaxWidth = 0;
		
		currLevel = 1;
		
		draw = null;
		
	}
	
	public void show(treeNodeResultObj rootNode){
		
		if(null!=draw){
			draw.dispose();
		}
		
		if(null == rootNode){
			
			System.out.println("决策树节点树为0");
			
			return;
			
		}
		
		clearPreviousVar();
		
		levelToNodeNumMap.put(1, 1);
		
		getMaxWidthAndLevel(rootNode);
		
		initlevelToNodeInPaintMap();
		
		draw = new DrawGraph(preTitle+"决策树示例图");		
		
		JLayeredPane tempPane = draw.createPane();		
		
		draw.setCurrentPane(tempPane);		
		
		TopoNode rootTopoNode = new TopoNode(0,rootNode.nodeName);
		
		rootTopoNode.setName(rootNode.nodeName);
		
		rootTopoNode.setInfo(rootNode.nodeInfo);
		
		treeNodeHashCodeToTopoNodeMap.put(rootNode, rootTopoNode);
		
		int rootColumn = treeMaxWidth / 2;
		
		draw.addTopoData(rootTopoNode,1, rootColumn);
		
		Vector<treeNodeResultObj> nodeVec = rootNode.childNodeVec;
		
		addNodeToTopologicalGraph(nodeVec, 2, rootTopoNode);
		
		draw.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
	
	public void refresh(){
		
		for (Map.Entry<treeNodeResultObj, TopoNode> entry : treeNodeHashCodeToTopoNodeMap.entrySet()) { 
			treeNodeResultObj obj = entry.getKey();
			TopoNode node = entry.getValue();
			//node.setText(obj.conditionMeet + ":" + obj.conditionNotMeet);
			node.setText("<html>"+obj.nodeName+"<br>"+obj.conditionMeet+":"+obj.conditionNotMeet+"</html>");
			
			node.setInfo(obj.nodeInfo);
		}
		
		draw.repaint();

	}
	
	private void initlevelToNodeInPaintMap(){
		
		for(Map.Entry<Integer, Integer> entry : levelToNodeNumMap.entrySet()){
			
			levelToNodeInPaintMap.put(entry.getKey(), 0);
			
		}
		
	}
	
	private void addNodeToTopologicalGraph(Vector<treeNodeResultObj> nodeVec, int startLevel, TopoNode parentNode){
		
		if(null == nodeVec || 0 == nodeVec.size()){			
			return;			
		}
		
		int size = nodeVec.size();
		
		int inc =  treeMaxWidth/levelToNodeNumMap.get(startLevel);
		
		int beginColum = 0;
		
        for(int i=0;i<size;i++){
        	
        	treeNodeResultObj obj = nodeVec.get(i);
        	
        	if(null == obj){
        		continue;
        	}
        	
        	TopoNode node = new TopoNode(0,obj.nodeName);
        	
        	node.setName(obj.nodeName);
        	
        	treeNodeHashCodeToTopoNodeMap.put(obj, node);
        	
        	node.setInfo(obj.nodeInfo);        	
        	
        	int drawColumn = 0;
        	
        	beginColum = levelToNodeInPaintMap.get(startLevel);
        	
        	if(beginColum == 0){
        		drawColumn = 1; 
        	}
        	else{
        		drawColumn = beginColum + inc;
        	}
        	
        	draw.addTopoData(node,startLevel,drawColumn);
        	
        	levelToNodeInPaintMap.put(startLevel, drawColumn);
   		 
   		    TopoLink link = new TopoLink("link", parentNode, node);
   		    
            draw.addTopoData(link);
            
            Vector<treeNodeResultObj> childNodeVec = obj.childNodeVec;
            
            addNodeToTopologicalGraph(childNodeVec, startLevel+1, node);
        	
        }
		
		
	}
	
	private void getMaxWidthAndLevel(treeNodeResultObj rootNode){
		
		int nextLvnodeNum = 0;
		
		if(null == rootNode){	
			
			currLevel--;
			
			return;			
		}
		
		Vector<treeNodeResultObj> childNodeVec = rootNode.childNodeVec;
		
		if(null == childNodeVec){
			
			currLevel--;
			
			return;			
		}
		
	    if(null!=levelToNodeNumMap.get(currLevel+1)){
	    	
	    	nextLvnodeNum = childNodeVec.size() + levelToNodeNumMap.get(currLevel+1);
	    }
	    else{
	    	
	    	nextLvnodeNum = childNodeVec.size();
	    }
	
		
	    levelToNodeNumMap.put(currLevel+1, nextLvnodeNum);
	    
		if(nextLvnodeNum>treeMaxWidth){
			
			treeMaxWidth = nextLvnodeNum;
			
		}
		
		for(int i=0;i<childNodeVec.size();i++){
			
			treeNodeResultObj obj = childNodeVec.get(i);
			
			currLevel++;
			
			getMaxWidthAndLevel(obj);
			
		}
		
		currLevel--;
	}
	
	public void test(){				
				
		treeNodeResultObj testObj = new treeNodeResultObj();
		
		testObj.nodeName = "rootNode";
		
		testObj.nodeInfo = "名称：" + testObj.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj.childNodeVec = new Vector<treeNodeResultObj>();
		
		treeNodeResultObj testObj_2 = new treeNodeResultObj();
				
		testObj_2.nodeName = "testObj_2";
		
		testObj_2.nodeInfo = "名称：" + testObj_2.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj.childNodeVec.add(testObj_2);
		
		treeNodeResultObj testObj_3 = new treeNodeResultObj();
		
		testObj_3.nodeName = "testObj_3";
		
		testObj_3.nodeInfo = "名称：" + testObj_3.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj.childNodeVec.add(testObj_3);
		
        treeNodeResultObj testObj_8 = new treeNodeResultObj();
		
        testObj_8.nodeName = "testObj_8";
		
        testObj_8.nodeInfo = "名称：" + testObj_8.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj.childNodeVec.add(testObj_8);
		
		testObj_8.childNodeVec = new Vector<treeNodeResultObj>();
		
		treeNodeResultObj testObj_9 = new treeNodeResultObj();
		
		testObj_9.nodeName = "testObj_9";
			
		testObj_9.nodeInfo = "名称：" + testObj_9.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
			
	    testObj_8.childNodeVec.add(testObj_9);
		
		testObj_2.childNodeVec = new Vector<treeNodeResultObj>();
		
		treeNodeResultObj testObj_4 = new treeNodeResultObj();
		
		testObj_4.nodeName = "testObj_4";
		
		testObj_4.nodeInfo = "名称：" + testObj_4.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj_2.childNodeVec.add(testObj_4);
		
        treeNodeResultObj testObj_5 = new treeNodeResultObj();
		
        testObj_5.nodeName = "testObj_5";
		
        testObj_5.nodeInfo = "名称：" + testObj_5.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj_2.childNodeVec.add(testObj_5);
		
        treeNodeResultObj testObj_6 = new treeNodeResultObj();
		
        testObj_6.nodeName = "testObj_6";
		
        testObj_6.nodeInfo = "名称：" + testObj_6.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj_2.childNodeVec.add(testObj_6);
		
		testObj_4.childNodeVec = new Vector<treeNodeResultObj>();
		
        treeNodeResultObj testObj_7 = new treeNodeResultObj();
		
        testObj_7.nodeName = "testObj_7";
		
        testObj_7.nodeInfo = "名称：" + testObj_7.nodeName + "\n" + "信息： 根节点\n" + "其它： 无";
		
		testObj_4.childNodeVec.add(testObj_7);	
		
		show(testObj);
		
	}

}
