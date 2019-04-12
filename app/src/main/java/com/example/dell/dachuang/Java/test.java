package com.example.dell.dachuang.Java;

public class test {

	public static void main(String[] args) {
		StepVO stepVO=new StepVO();
		ProcessVO processVO = new ProcessVO();
		processVO.setProcessID("001");
		stepVO.setProcessVO(processVO);
		NetWorkClient client=new ImplNetWorkClient();
		StepVO[] response=client.request("001", stepVO,StepVO[].class);
		System.out.println(response[0].getDescription());
		
	}
}
