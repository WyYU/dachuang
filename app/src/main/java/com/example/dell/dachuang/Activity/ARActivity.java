package com.example.dell.dachuang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.example.dell.dachuang.R;

public class ARActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ar);
//		LatLng startPt = new LatLng(40.047416,116.312143);
//		LatLng endPt = new LatLng(40.048424, 116.313513);
//		WalkNaviLaunchParam mParam = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
//		WalkNavigateHelper.getInstance().routePlanWithParams(mParam, new IWRoutePlanListener() {
//			@Override
//			public void onRoutePlanStart() {
//				//开始算路的回调
//			}
//
//			@Override
//			public void onRoutePlanSuccess() {
//				//算路成功
//				//跳转至诱导页面
//				Intent intent = new Intent(ARActivity.this, WNaviGuideActivity.class);
//				startActivity(intent);
//			}
//
//			@Override
//			public void onRoutePlanFail(WalkRoutePlanError walkRoutePlanError) {
//				//算路失败的回调
//			}
//		});
	}

}
