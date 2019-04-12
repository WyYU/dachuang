package com.example.dell.dachuang.Java;

public interface NetWorkClient {
	
	/*
	 * descption:����������������ҷ��ط�������Ӧ����Ϣ
	 * parameter: sessionID:�����������Ĺ��ܺ�; requestData:���������������ݣ�VO����;responseClassType:������������Ϣ��VO��������
	 */
	public <T> T request(String SessionID, Object requestData, Class<T> responseClassType);
	
}
