package ustc.sse.water.activity;

import ustc.sse.water.utils.zjx.MyLocationSet;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;

/**
 * 
 * Driver应用的地图显示界面 <br>
 * 此类为显示高德地图的Activity； 分为：顶部导航栏，中部地图，底部导航栏三部分 在地图界面中默认定位到“我的位置”，并默认显示周围的停车场
 * <p>
 * Copyright: Copyright (c) 2014-11-12 下午2:40:50
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DriverMainScreen extends Activity implements LocationSource,
		AMapLocationListener, OnClickListener {
	/* 定义AMap */
	private AMap aMap;
	/* 定义用来显示的MapView */
	private MapView mapView;
	/* 定义设置地图 */
	private UiSettings uiSettings;
	/* 用来处理定位 */
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	/* 定位按钮 */
	private ImageButton myLocation;
	/* 我的位置 */
	private LatLng myLatlng;

	/**
	 * 必须重写onCreate方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_main);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		initMap();
		initViews();
	}

	/**
	 * 初始化地图的显示信息
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			uiSettings = aMap.getUiSettings();
			// 将高德地图的Logo设置为底部居中
			uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
			uiSettings.setMyLocationButtonEnabled(false); // 不现实系统自带的定位按钮
			aMap.setLocationSource(this);// 设置定位监听
			aMap.moveCamera(CameraUpdateFactory.zoomTo(16));// 重定义缩放级别
			new MyLocationSet(aMap).setMapLocation(); // 调用方法，设置定位属性
		}
	}

	/**
	 * 初始化Ui控件
	 */
	private void initViews() {
		myLocation = (ImageButton) findViewById(R.id.button_my_location);
		myLocation.setOnClickListener(this);
	}

	/**
	 * 必须重写onResume
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 必须重写onPause
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 必须重写onSaveInstanceState
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 必须重新onDestroy
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 定位成功后回调该函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			myLatlng = new LatLng(aLocation.getAltitude(),
					aLocation.getLongitude());// 我的位置
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

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

	@Override
	public void onClick(View v) {
		aMap.animateCamera(new CameraUpdateFactory().changeLatLng(myLatlng));
		// aMap.setMyLocationEnabled(true);
	}
}
