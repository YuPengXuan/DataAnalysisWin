package com.xn.alex.data.window;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JOptionPane;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.xn.alex.data.common.ConfigParser;
import com.xn.alex.data.common.SoftWareLicence;
import com.xn.alex.data.database.DatabaseAction;
import com.xn.alex.data.database.DatabaseConstant;
import com.xn.alex.data.database.DatabaseSpecialAction;
import com.xn.alex.data.login.LoginWindow;
import com.xn.alex.data.process.MenuItemDisable;
import com.xn.alex.data.ui.Logo;
import com.xn.alex.license.LicenseController;



public class Main {	
	/**
	 * Launch the application.
	 */		
	public static void main(String[] args) {
		Logo logo = new Logo("resource/das.png");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoginWindow logWin = new LoginWindow();
		
		logWin.process();
		
		logo.dispose();
		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					
//					Logo logo = new Logo("resource/das.png");
//					
//					Thread.sleep(2000);
//					LoginWindow logWin = new LoginWindow();
//					
//					logWin.process();
//					
//					logo.dispose();
//					
//				} catch (Exception e) {
//					
//					e.printStackTrace();
//					
//				}
//			}
//		});
	}
	
	public static void Initialize(){
		
		loadConfigFile();
	/*	
		try {
			RSAEncrypt.test();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
		licenceCheck();
				
		createAvailableIdTable();
		
		createFileToTableTable();

		
	}
	
	private static void licenceCheck(){
		
		if(false == LicenseController.getLicenseController().validate()){
			
			JOptionPane.showMessageDialog(null,"软件证书不合法或者已过期！请联系多多老师(^_^)","错误信息",JOptionPane.ERROR_MESSAGE);
			
			System.exit(1);
		}
				
	}
	
	private static void createFileToTableTable(){
		
		if(false == DatabaseAction.Instance().isTableExist(DatabaseConstant.FILE_TO_TABLE)){
		
		    DatabaseSpecialAction.Instance().createFileToTableTable();
		
		}
		
	}
	
	private static void createAvailableIdTable(){
		
		if(false == DatabaseAction.Instance().isTableExist(DatabaseConstant.AVAILABLE_TABLEID_TABLE)){
		
           if(false == DatabaseSpecialAction.Instance().createAvailableIdTable()){
        	   
        	   System.out.println("系统表创建失败。程序初始化失败！");
        	   
           }
		}
		
	}
	
	private static void loadConfigFile(){
		
		
		File confFile = new File("config/config.xml");
		
		if(!confFile.exists()){
			
			System.out.println("config file  doesn't exist");
			
			System.exit(1);
		}
		
		try{
			ConfigParser cParser = ConfigParser.Instance();
			//parse EventDecoderCounter.xml			
		    SAXParserFactory factory = SAXParserFactory.newInstance();
		    
		    SAXParser parser = factory.newSAXParser();
		    
		    parser.parse(confFile, cParser);
		    
		}
		catch(Exception e)
		{
			System.out.println("parser fail!");
			
			System.exit(1);
		}	
	}
	
	public static void setStartUpEnabledMenu(){
				
		MenuItemDisable.Instance().disableSecondColumnMenu();
		
		MenuItemDisable.Instance().disableThirdColumnMenu();
		
		MenuItemDisable.Instance().disableFourthColumnMenu();
		
	}
}
