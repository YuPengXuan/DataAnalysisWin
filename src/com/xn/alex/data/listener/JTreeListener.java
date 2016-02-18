package com.xn.alex.data.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.xn.alex.data.action.OpenAction;
import com.xn.alex.data.window.MainWindow;

public class JTreeListener {
	
	private static JTreeListener jTreeListenerHandler = null;
	
	private JTreeListener(){
		
	}
	
	public static JTreeListener Instance(){
		if(null == jTreeListenerHandler){
			jTreeListenerHandler = new JTreeListener();
		}
		return jTreeListenerHandler;
	}
	
	public void addListener(){
		
		if(null == MainWindow.Instance().getTree()){
    		return;
    	}
		
		try{
		
		addSelectionListener();
		
		addMouseListener();  
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void addSelectionListener(){
		
		MainWindow.Instance().getTree().addTreeSelectionListener(new TreeSelectionListener(){

			@Override
			public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent) {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) MainWindow.Instance().getTree().getLastSelectedPathComponent();
 
                if (node == null)
                    return;                
                
                MainWindow.Instance().setCurrentNode(node);
				
			}
    		
    	});
		
	}
	
	private void addMouseListener(){
		
		MainWindow.Instance().getTree().addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
			    DefaultMutableTreeNode node = (DefaultMutableTreeNode) MainWindow.Instance().getTree().getLastSelectedPathComponent();
					
			    if (node == null)
	                    return;
	 
					
				if(2 == paramMouseEvent.getClickCount()){
										
					if (!node.isLeaf()){						
						return;		                
		            }
					
					MainWindow.Instance().setCurrentNode(node);
					
					String fileName = MainWindow.treeNodeToFullPathMap.get(MainWindow.Instance().getCurrentNode().hashCode());
					
					OpenAction.Instance().decideWhetherLoadANewFile(fileName);
					
					return;
				     
			     }
				
				 if(1 == paramMouseEvent.getClickCount()){
					 
					 if (!node.isLeaf()){						
							return;		                
			         }
					 
					 MainWindow.Instance().setCurrentNode(node);;
					 
				 }
				
				
				
			}
					 
			@Override
			public void mouseEntered(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub				               
			}

			@Override
			public void mouseReleased(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
		
	}
		
}
