package com.xn.alex.data.login;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.xn.alex.data.database.DataAnalysisException;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.database.MySqlExecuter;
import com.xn.alex.data.database.SqlTask;
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
		
		frame = new JFrame("用户登录");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	    contentPane = new JPanel();
	     
	    contentPane.setLayout(new BorderLayout());
	     
	    JPanel topPanel = new JPanel();
	     
	    GridLayout layout = new GridLayout(0,2);
	     
	    topPanel.setLayout(layout);
	     
	    layout.setVgap(15);
	     
	    layout.setHgap(5);
	     
	    topPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 0, 40));
	     
	    JLabel userNameLabel = new JLabel("用户名：");
	     
	    topPanel.add(userNameLabel);
	     
	    userNameTextField = new JTextField(3);
	     
	    topPanel.add(userNameTextField);
	     
	    JLabel passwordLabel = new JLabel("密 码：");
	     
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
	    
	     
	    okBt = new JButton("确定");
	     
	    bottomPanel.add(okBt);
	     
	    cancelBt = new JButton("取消");
	     
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
			JOptionPane.showMessageDialog(null,"请输入用户名","错误信息",JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		String password = String.valueOf(passwordField.getPassword());
		
		if("".equals(password)|| null == password){
			JOptionPane.showMessageDialog(null,"请输入用户密码","错误信息",JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		LoginAction.Instance().setLoginUserName(userName);
		
		LoginAction.Instance().setLoginPassword(password);
		
		if(false == initDB()){
			JOptionPane.showMessageDialog(null,"用户名和密码不匹配或者没有访问数据库权限","错误信息",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		frame.dispose();
		
		Main.Initialize();										
		
		MainWindow mframe = MainWindow.Instance();
		
        mframe.createWindow();
        
        mframe.setVisible(true);
		
        mframe.setTitle("移动数据分析系统");
		
        mframe.setSize(1000, 700);
		
        mframe.setLocationRelativeTo(null);
		
        Main.setStartUpEnabledMenu();
		
	}

	private boolean initDB() {
		try {
			return MySqlExecuter.getMySqlExecuter().getConnection() != null;
		} catch (SQLException e) {
			return false;
		}
	}

}
