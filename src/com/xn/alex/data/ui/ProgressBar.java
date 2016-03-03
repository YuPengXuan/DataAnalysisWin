package com.xn.alex.data.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;


public class ProgressBar extends JDialog implements IPropertyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7737913283285888535L;

	protected JProgressBar progressBar;
	
	private JLabel messageLabel;
	
	private void init(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
		messageLabel = new JLabel("处理中...请等待");
		panel.add(this.messageLabel);
		this.getContentPane().add(panel);

		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
		progressBar = new JProgressBar();
		panel.add(this.progressBar);
		this.getContentPane().add(panel);
		
		
		progressBar.setPreferredSize(new Dimension(450, 18));
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(true);
        
        this.setSize(400, 200);
		
        //pack();
		setResizable(false);
		
		progressBar.setIndeterminate(false);
        pack();
        setLocation(getCenterLocation());
	}
	public ProgressBar(Dialog owner,String title){
		super(owner,title,true);
		init();
		start();
		
	}
	public ProgressBar(Frame owner,String title){
		super(owner,title,true);
		init();
		start();
	}
	
	public void setProgress(int value, String message, long waitMills){
		if(waitMills > 0){
			try {
				Thread.sleep(waitMills);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		progressBar.setValue(value);
		messageLabel.setText(message);
	}
	
	public void setProgress(int value, long waitMills){
		if(waitMills > 0){
			try {
				Thread.sleep(waitMills);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		progressBar.setValue(value);
	}
	public void setProgress(int value, String message){
		setProgress(value,message,0);
	}
	
	
	public void setProgress(int value){
		progressBar.setValue(value);
	}
	
	protected Point getCenterLocation() {
        Component owner     = getOwner();
 		Dimension dimension = owner.getSize();
        Point     point     = owner.getLocation();
  	
 		point.x += (int)((dimension.getWidth() - getSize().getWidth()) / 2);
 		point.y += (int)((dimension.getHeight() - getSize().getHeight()) / 2);
 		return point;
 	}
		
	private void start(){
		SwingWorker<Object, Object> task = new SwingWorker<Object, Object>(){
			boolean bDone = false;
			@Override
			protected Void doInBackground() throws Exception {
				try{
					onRun();
					bDone = true;
				}catch(Exception e){
					onException(e);
					dispose();
				}
				
				return null;
			}
			
			@Override
			protected void done() {
				if(bDone){
					onClose();
				}
			}
			
		};
		task.execute();
		setVisible(true);
	}
	
	public void onRun() throws Exception{
		
	}
	
	public void onClose(){
		
	}
	
	public void onException(Exception e){
		
	}
	@Override
	public void valueChanged(String name, Object value) {
		// TODO Auto-generated method stub
		
	}
}
