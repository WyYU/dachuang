package com.example.dell.dachuang.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.bikenavi.params.BikeRouteNodeInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.example.dell.dachuang.Activity.location.BDLocationListener;
import com.example.dell.dachuang.Activity.location.BDLocationManager;
import com.example.dell.dachuang.R;

import java.util.ArrayList;

import Listener.MyLocationListener;

public class ARActivity extends AppCompatActivity {
	private final static String TAG = BNaviMainActivity.class.getSimpleName();

	private MapView mMapView;
	private BaiduMap mBaiduMap;

	/*导航起终点Marker，可拖动改变起终点的坐标*/
	private Marker mStartMarker;
	private Marker mEndMarker;

	private LatLng startPt;
	private LatLng endPt;

	private BikeNaviLaunchParam bikeParam;
	private WalkNaviLaunchParam walkParam;

	private static boolean isPermissionRequested = false;

	private BitmapDescriptor bdStart = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_start);
	private BitmapDescriptor bdEnd = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_end);

	LocationManager locationManager;
	MyLocationListener myLocationListener;
	BDLocationListener bdLocationListener;
	BDLocationManager bdLocationManager;
	private double endx;
	private double endy;
	private double startx;
	private double starty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_main);
		mMapView = (MapView) findViewById(R.id.mapview);
		getLocation();
		initData();
		//initLocation();
		initMapStatus();

        /*骑行导航入口*/
		Button bikeBtn = (Button) findViewById(R.id.btn_bikenavi);
		bikeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startBikeNavi();
			}
		});
		bikeBtn.setVisibility(View.INVISIBLE);

        /*普通步行导航入口*/
		Button walkBtn = (Button) findViewById(R.id.btn_walknavi_normal);
		walkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				walkParam.extraNaviMode(0);
				startWalkNavi();
			}
		});
		walkBtn.setVisibility(View.INVISIBLE);

        /*AR步行导航入口*/
		Button arWalkBtn = (Button) findViewById(R.id.btn_walknavi_ar);
		arWalkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				walkParam.extraNaviMode(1);
				startWalkNavi();
			}
		});

		startPt = new LatLng(startx,starty);
		endPt = new LatLng(endx, endy);

        /*构造导航起终点参数对象*/
		BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
		bikeStartNode.setLocation(startPt);
		BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
		bikeEndNode.setLocation(endPt);
		bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

		WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
		walkStartNode.setLocation(startPt);
		WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
		walkEndNode.setLocation(endPt);
		walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

        /* 初始化起终点Marker */
		initOverlay();
	}

	private void getLocation() {
		bdLocationManager = BDLocationManager.getInstance();
		bdLocationListener = new BDLocationListener() {
			@Override
			public void onLocationChanged(Location location) {

			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}
		};
		bdLocationManager.addListener(bdLocationListener);
		bdLocationManager.startLoc();
		locationManager = bdLocationManager.getmSysLocManager();
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		starty = location.getLongitude();
		startx = location.getLatitude();
		requestPermission();
	}

	private void initData() {
		Intent intent = getIntent();
		endx = intent.getDoubleExtra("Local_x",0);
		endy = intent.getDoubleExtra("Local_y",0);
	}

	private void initLocation() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationProvider gpsProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);//1.通过GPS定位，较精确。也比較耗电
		LocationProvider netProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);//2.通过网络定位。对定位精度度不高或省点情况可考虑使用
		if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null || locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
       /*
		* 进行定位
        * provider:用于定位的locationProvider字符串:LocationManager.NETWORK_PROVIDER/LocationManager.GPS_PROVIDER
		* minTime:时间更新间隔。单位：ms
        * minDistance:位置刷新距离，单位：m
		* listener:用于定位更新的监听者locationListener
		*/
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,10, myLocationListener);

			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			starty = location.getLongitude();
			startx = location.getLatitude();
			Log.e(TAG,startx+"  "+starty);
		} else {
			//无法定位：1、提示用户打开定位服务；2、跳转到设置界面
			Toast.makeText(this, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
			Intent i = new Intent();
			i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(i);
		}
	}


	/**
	 * 初始化地图状态
	 */
	private void initMapStatus(){
		mBaiduMap = mMapView.getMap();
		MapStatus.Builder builder = new MapStatus.Builder();
		builder.target(new LatLng(startx, starty)).zoom(15);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
	}

	/**
	 * 初始化导航起终点Marker
	 */
	public void initOverlay() {

		MarkerOptions ooA = new MarkerOptions().position(startPt).icon(bdStart)
				.zIndex(9).draggable(true);

		mStartMarker = (Marker) (mBaiduMap.addOverlay(ooA));
		mStartMarker.setDraggable(true);
		MarkerOptions ooB = new MarkerOptions().position(endPt).icon(bdEnd)
				.zIndex(5);
		mEndMarker = (Marker) (mBaiduMap.addOverlay(ooB));
		mEndMarker.setDraggable(true);

		mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				if(marker == mStartMarker){
					startPt = marker.getPosition();
				}else if(marker == mEndMarker){
					endPt = marker.getPosition();
				}

				BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
				bikeStartNode.setLocation(startPt);
				BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
				bikeEndNode.setLocation(endPt);
				bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

				WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
				walkStartNode.setLocation(startPt);
				WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
				walkEndNode.setLocation(endPt);
				walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
	}

	/**
	 * 开始骑行导航
	 */
	private void startBikeNavi() {
		Log.d(TAG, "startBikeNavi");
		try {
			BikeNavigateHelper.getInstance().initNaviEngine(ARActivity.this, new IBEngineInitListener() {
				@Override
				public void engineInitSuccess() {
					Log.d(TAG, "BikeNavi engineInitSuccess");
					routePlanWithBikeParam();
				}

				@Override
				public void engineInitFail() {
					Log.d(TAG, "BikeNavi engineInitFail");
					BikeNavigateHelper.getInstance().unInitNaviEngine();
				}
			});
		} catch (Exception e) {
			Log.d(TAG, "startBikeNavi Exception");
			e.printStackTrace();
		}
	}

	/**
	 * 开始步行导航
	 */
	private void startWalkNavi() {
		Log.d(TAG, "startWalkNavi");
		try {
			WalkNavigateHelper.getInstance().initNaviEngine(ARActivity.this, new IWEngineInitListener() {
				@Override
				public void engineInitSuccess() {
					Log.d(TAG, "WalkNavi engineInitSuccess");
					routePlanWithWalkParam();
				}

				@Override
				public void engineInitFail() {
					Log.d(TAG, "WalkNavi engineInitFail");
					WalkNavigateHelper.getInstance().unInitNaviEngine();
				}
			});
		} catch (Exception e) {
			Log.d(TAG, "startBikeNavi Exception");
			e.printStackTrace();
		}
	}

	/**
	 * 发起骑行导航算路
	 */
	private void routePlanWithBikeParam() {
		BikeNavigateHelper.getInstance().routePlanWithRouteNode(bikeParam, new IBRoutePlanListener() {
			@Override
			public void onRoutePlanStart() {
				Log.d(TAG, "BikeNavi onRoutePlanStart");
			}

			@Override
			public void onRoutePlanSuccess() {
				Log.d(TAG, "BikeNavi onRoutePlanSuccess");
				Intent intent = new Intent();
				intent.setClass(ARActivity.this, BNaviGuideActivity.class);
				startActivity(intent);
			}

			@Override
			public void onRoutePlanFail(BikeRoutePlanError error) {
				Log.d(TAG, "BikeNavi onRoutePlanFail");
			}

		});
	}

	/**
	 * 发起步行导航算路
	 */
	private void routePlanWithWalkParam() {
		WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkParam, new IWRoutePlanListener() {
			@Override
			public void onRoutePlanStart() {
				Log.d(TAG, "WalkNavi onRoutePlanStart");
			}

			@Override
			public void onRoutePlanSuccess() {

				Log.d(TAG, "onRoutePlanSuccess");

				Intent intent = new Intent();
				intent.setClass(ARActivity.this, WNaviGuideActivity.class);
				startActivity(intent);

			}

			@Override
			public void onRoutePlanFail(WalkRoutePlanError error) {
				Log.d(TAG, "WalkNavi onRoutePlanFail");
			}

		});
	}

	/**
	 * Android6.0之后需要动态申请权限
	 */
	private void requestPermission() {
		if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

			isPermissionRequested = true;

			ArrayList<String> permissionsList = new ArrayList<>();

			String[] permissions = {
					Manifest.permission.RECORD_AUDIO,
					Manifest.permission.ACCESS_NETWORK_STATE,
					Manifest.permission.INTERNET,
					Manifest.permission.READ_PHONE_STATE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.MODIFY_AUDIO_SETTINGS,
					Manifest.permission.WRITE_SETTINGS,
					Manifest.permission.ACCESS_WIFI_STATE,
					Manifest.permission.CHANGE_WIFI_STATE,
					Manifest.permission.CHANGE_WIFI_MULTICAST_STATE


			};

			for (String perm : permissions) {
				if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
					permissionsList.add(perm);
					// 进入到这里代表没有权限.
				}
			}

			if (permissionsList.isEmpty()) {
				return;
			} else {
				requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), 0);
			}
		}
	}

	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		bdStart.recycle();
		bdEnd.recycle();
	}
}
