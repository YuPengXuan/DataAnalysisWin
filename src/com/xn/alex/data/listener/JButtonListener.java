package com.xn.alex.data.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.xn.alex.data.window.MainWindow;

public class JButtonListener {
	
    private static JButtonListener jButtonListenerHandler = null;   
	
	private JButtonListener(){
		
	}
	
	public static JButtonListener Instance(){
		if(null == jButtonListenerHandler){
			jButtonListenerHandler = new JButtonListener();
		}
		return jButtonListenerHandler;
	}
	
	public void addListener(){
		
		try{
			
			addOpenButtonMotionListener();
			
			addSaveButtonMotionListener();
			
			addCloseButtonMotionListener();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void addOpenButtonMotionListener(){
		
		MainWindow.Instance().getOpenButton().addMouseMotionListener(new MouseAdapter(){
			
			public void mouseMoved(MouseEvent event){
				MainWindow.Instance().getOpenButton().setToolTipText("��������");
			}
			
		});
		
	}
	
    private void addSaveButtonMotionListener(){
		
		MainWindow.Instance().getSaveButton().addMouseMotionListener(new MouseAdapter(){
			
			public void mouseMoved(MouseEvent event){
				MainWindow.Instance().getSaveButton().setToolTipText("����");
			}
			
		});
		
	}
    
    private void addCloseButtonMotionListener(){
		
		MainWindow.Instance().getCloseButton().addMouseMotionListener(new MouseAdapter(){
			
			public void mouseMoved(MouseEvent event){
				MainWindow.Instance().getCloseButton().setToolTipText("�ر�");
			}
			
		});
		
	}
}
