package com.xn.alex.data.login;

public class LoginAction {
	
	private static LoginAction loginActionHandler = null;
	
	private String loginUserName = null;

	private String loginPassword = null;
	
	private LoginAction(){
		
	}
	
	public static LoginAction Instance(){
		if(null == loginActionHandler){
			loginActionHandler = new LoginAction();
		}
		
		return loginActionHandler;
	}
	
	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

}
