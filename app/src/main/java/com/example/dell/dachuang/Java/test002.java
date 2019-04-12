package com.example.dell.dachuang.Java;

public class test002 {
	
	public static void main(String[] args) {
		UserVO userVO=new UserVO();
		userVO.setUsername("GaoSifan");
		userVO.setPassword("230112");
		NetWorkClient client=new ImplNetWorkClient();
		UserVO response=client.request("002", userVO,UserVO.class);
		System.out.println(response.getProcessVO().getProcessID());
	}
}
