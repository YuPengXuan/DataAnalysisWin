package com.xn.alex.data.login;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.window.Main;
import com.xn.alex.data.window.MainWindow;

public class LoginWindow {
	
	private JFrame frame;

	private JPanel contentPane;
	
	private JTextField userNameTextField;
	
	private JPasswordField passwordField;
	
    private JButton okBt;
 	
	private JButton cancelBt;
	
	public void process(){
		
		createLoginWindow();
		
		addListener();
		
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	private void createLoginWindow(){
		
		frame = new JFrame("�û���¼");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	    contentPane = new JPanel();
	     
	    contentPane.setLayout(new BorderLayout());
	     
	    JPanel topPanel = new JPanel();
	     
	    GridLayout layout = new GridLayout(0,2);
	     
	    topPanel.setLayout(layout);
	     
	    layout.setVgap(15);
	     
	    layout.setHgap(5);
	     
	    topPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 0, 40));
	     
	    JLabel userNameLabel = new JLabel("�û�����");
	     
	    topPanel.add(userNameLabel);
	     
	    userNameTextField = new JTextField(3);
	     
	    topPanel.add(userNameTextField);
	     
	    JLabel passwordLabel = new JLabel("�� �룺");
	     
	    topPanel.add(passwordLabel);
	     
	    passwordField = new JPasswordField(3);
	     
	    passwordField.setEchoChar('*');
	     
	    topPanel.add(passwordField);
	     
	    contentPane.add(topPanel, BorderLayout.NORTH);
	     
	    JPanel bottomPanel = new JPanel();
	    
	    GridLayout Layout2 = new GridLayout(0,2);
	    
	    bottomPanel.setLayout(Layout2);
	    
	    Layout2.setHgap(30);
	    
	    bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 40));
	    
	     
	    okBt = new JButton("ȷ��");
	     
	    bottomPanel.add(okBt);
	     
	    cancelBt = new JButton("ȡ��");
	     
	    bottomPanel.add(cancelBt);
	     
	    contentPane.add(bottomPanel, BorderLayout.SOUTH);
	     
	    frame.setContentPane(contentPane);

	    frame.pack();

	    frame.setLocationRelativeTo(null);
	     
	    frame.setSize(300,200);
	     
	    frame.setResizable(false);

	    frame.setVisible(true);
		
	}
	
	private void addListener(){
		
		cancelBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.dispose();
				
				System.exit(0);
			}
			
		});
		
		okBt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				processMainProc();								
			}
			
		});
		
		userNameTextField.addKeyListener(new  KeyAdapter(){
			
			public void keyPressed(KeyEvent event)
			{
			    if (KeyEvent.getKeyText(event.getKeyCode()).compareToIgnoreCase("Enter")==0)
			    {
			    	okBt.doClick();
			    }
			}
			
		});
		
		passwordField.addKeyListener(new  KeyAdapter(){
			
			public void keyPressed(KeyEvent event)
			{
			    if (KeyEvent.getKeyText(event.getKeyCode()).compareToIgnoreCase("Enter")==0)
			    {
			    	okBt.doClick();
			    }
			}
			
		});
		
	}
	
	private void processMainProc(){
		
		String userName = userNameTextField.getText();
		
		if("".equals(userName)|| null == userName){
			JOptionPane.showMessageDialog(null,"�������û���","������Ϣ",JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		String password = String.valueOf(passwordField.getPassword());
		
		if("".equals(password)|| null == password){
			JOptionPane.showMessageDialog(null,"�������û�����","������Ϣ",JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		LoginAction.Instance().setLoginUserName(userName);
		
		LoginAction.Instance().setLoginPassword(password);
		
		if(false == DatabaseAction.Instance().connect()){
			return;
		}
		
		frame.dispose();
		
		Main.Initialize();										
		
		MainWindow mframe = MainWindow.Instance();
		
        mframe.createWindow();
        
        mframe.setVisible(true);
		
        mframe.setTitle("�ƶ����ݷ���ϵͳ");
		
        mframe.setSize(1000, 700);
		
        mframe.setLocationRelativeTo(null);
		
        Main.setStartUpEnabledMenu();
		
	}

}
