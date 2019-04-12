package com.example.dell.dachuang.Activity;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.dell.dachuang.Java.ImplNetWorkClient;
import com.example.dell.dachuang.Java.NetWorkClient;
import com.example.dell.dachuang.Java.UserVO;
import com.example.dell.dachuang.R;
import com.example.dell.dachuang.Utils.ThreadPoolService;
import com.example.dell.dachuang.Utils.Tools;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
	private final String TAG = "LoginActivity";
	private EditText name_edit;
	private EditText pwd_edit;
	private Button login_btn;
	private Button regist_btn;
	private CheckBox remwd;
	private CheckBox autologin;
	private int REQUESTCODE = 1;
	private String user;
	private String password;
	private UserVO userVO ;
	private Tools tools;
	private UserVO response;
	ThreadPoolExecutor threadPoolService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		initView();
	}

	private void init() {
		threadPoolService = ThreadPoolService.getInstance();
		tools = Tools.getInstance();
	}

	private void initView() {
		name_edit = findViewById(R.id.uname_editview);
		pwd_edit = findViewById(R.id.upwd_editview);
		login_btn = findViewById(R.id.login_btn);
		regist_btn = findViewById(R.id.regist_btn);
		login_btn.setOnClickListener(this);
		regist_btn.setOnClickListener(this);
		remwd = findViewById(R.id.rempwd_cbx);
		autologin = findViewById(R.id.autologin_cbx);
		remwd.setOnClickListener(this);
		autologin.setOnClickListener(this);
		name_edit.setText("GaoSifan");
		pwd_edit.setText("230112");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_btn:
				user = name_edit.getText().toString();
				password = pwd_edit.getText().toString();
				String result = login();
				if (result == null) {
					result = "null";
				}
				Log.e(TAG,result.toString());
				if (result!="null") {
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					intent.putExtra("PrecessID",response.getProcessVO().getProcessID());
					startActivity(intent);
					finish();
				} else {
					tools.showNormalDialog(this,"用户名或密码错误!");
				}
				break;
			case R.id.regist_btn:
				break;
			case R.id.rempwd_cbx:
				break;
			case R.id.autologin_cbx:
				break;
			default:
				break;
		}
	}

	public String login() {
		Future<String> res = threadPoolService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				userVO=new UserVO();
				userVO.setUsername(user);
				userVO.setPassword(password);
				NetWorkClient client=new ImplNetWorkClient();
				response=client.request("002", userVO,UserVO.class);
				if (response.getUserID().toString()==null){
					return "null";
				}
				return response.getUserID().toString();
			}
		});
		if (res==null) {
			return "null";
		}
		else try {
			return res.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

//	public void login() {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				String code = presenter.login(name_edit.getText().toString(), pwd_edit.getText().toString());
//				Message message = new Message();
//				Bundle bundle = new Bundle();
//				bundle.putString("code", " " + code);
//				message.setData(bundle);
//				message.what = 1;
//				handler.sendMessage(message);
//			}
//		}).start();
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 4:
				break;
			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
