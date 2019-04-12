package com.example.dell.dachuang.Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

public class ImplNetWorkClient implements NetWorkClient{

	public static final String IP_ADDR = "39.96.28.73";//鏈嶅姟鍣ㄥ湴鍧�
    public static final int PORT = 3304;//鏈嶅姟鍣ㄧ鍙ｅ彿    
	

	public String request(String requestion) {
		// TODO Auto-generated method stub
		Socket socket = null;
		String info = null;
		try {
			socket = new Socket(IP_ADDR, PORT);
			
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(requestion);
			pw.flush();
			socket.shutdownOutput();
			
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			info = br.readLine();
			
			
			
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return info;
	}



	@Override
	public <T> T request(String SessionID, Object requestData, Class<T> responseClassType) {
		Gson gson=new Gson();
		String requestJson=gson.toJson(requestData);
		requestJson=addSessionID(requestJson,SessionID);
		String responseJson=request(requestJson);
		//System.out.println(responseJson);
		return gson.fromJson(responseJson, responseClassType);
	}



	private String addSessionID(String json,String sessionID) {
		
		JSONObject jsonObject=JSONObject.fromObject(json);
		jsonObject.put("session", sessionID);
		return jsonObject.toString();
	}
	
}
