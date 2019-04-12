package com.example.dell.dachuang.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.dell.dachuang.Adapter.TabAdapter;
import com.example.dell.dachuang.Fragment.NextStepFragment;
import com.example.dell.dachuang.Fragment.StepFragment;
import com.example.dell.dachuang.Java.ImplNetWorkClient;
import com.example.dell.dachuang.Java.NetWorkClient;
import com.example.dell.dachuang.Java.ProcessVO;
import com.example.dell.dachuang.Java.StepVO;
import com.example.dell.dachuang.R;
import com.example.dell.dachuang.Utils.ThreadPoolService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {
	private final String TAG = "MainActivity";
	private ViewPager viewPager;
	private TabLayout tabLayout;
	private List<Fragment> list;
	private StepVO[] stepVOS;
	private String procressID;
	private ThreadPoolExecutor threadPoolService;
	private NetWorkClient netWorkClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		threadPoolService = ThreadPoolService.getInstance();
		netWorkClient = new ImplNetWorkClient();
		procressID = getUser();
		stepVOS = getData();
		setContentView(R.layout.activity_main);
		initView();
		//initAR();

	}

//	private void initAR() {
//		Res.addResource(this);
//		DuMixARConfig.setAppId("15996449");
//		DuMixARConfig.setAPIKey("LpUAl8oAGFnLblseluKUB04m");
//		DuMixARConfig.setSecretKey("qlwhDCWzRIhcYzPzgn95g0iBSiFe3uQO");
//	}

	private void checkNet() {
		if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},1);
		} else {
		}
	}

	private void initView() {
		initFragmentList();
		viewPager = findViewById(R.id.view_pager);
		viewPager.setOffscreenPageLimit(1);
		tabLayout = findViewById(R.id.tab_layout);
		tabLayout.setTabMode(tabLayout.MODE_FIXED);
		TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
		tabLayout.setVisibility(tabLayout.INVISIBLE);
		adapter.setData(list);
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);
	}

	private void initFragmentList() {
		list = new ArrayList<>();
		for(int i = 0 ; i <stepVOS.length;i++){
			list.add(new StepFragment());
		}
	}

	public ViewPager getViewPager() {
		return viewPager;
	}

	public StepVO[] getData() {
		Future<StepVO[]> future = threadPoolService.submit(new Callable<StepVO[]>() {
			@Override
			public StepVO[] call() throws Exception {
				//Log.e(TAG,procressID.getUserID().toString());
				StepVO stepVO=new StepVO();
				ProcessVO processVO = new ProcessVO();
				processVO.setProcessID(procressID);
				stepVO.setProcessVO(processVO);
				NetWorkClient client=new ImplNetWorkClient();
				StepVO[] response=client.request("001", stepVO,StepVO[].class);
				System.out.println(response[0].getDescription());
				return response;
			}
		});
		try {
			return future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public StepVO getStepVOList(int pos) {
		StepVO stepVO = stepVOS[pos];
		Log.e("steplist",stepVO.toString());
		return stepVO;
	}

	public TabLayout getTabLayout() {
		return tabLayout;
	}


	public String  getUser() {
		Intent intent = getIntent();
		procressID = intent.getStringExtra("PrecessID");
		Log.e(TAG,procressID);
		return procressID;
	}
}
