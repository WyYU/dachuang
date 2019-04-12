package com.example.dell.dachuang.Java;

import java.io.Serializable;

public class UserVO implements Serializable {
	
	private String userID;
	private String username;
	private String password;
	private ProcessVO processVO;
	
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ProcessVO getProcessVO() {
		return processVO;
	}
	public void setProcessVO(ProcessVO processVO) {
		this.processVO = processVO;
	}

}
