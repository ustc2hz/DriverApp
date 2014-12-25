package ustc.sse.water.activity;

import ustc.sse.water.activity.zjx.ParkingList;
import ustc.sse.water.tools.hzh.MyCloudSearch;
import ustc.sse.water.tools.hzh.NaviRouteMethod;
import ustc.sse.water.tools.zjx.MyLocationSet;
import ustc.sse.water.tools.zjx.PoiAroundSearchMethod;
import ustc.sse.water.tools.zjx.PoiSearchMethod;
import ustc.sse.water.tools.zjx.VoiceSearch;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.zf.ManagerMain;
import ustc.sse.water.zf.Manager_Driver_model;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * 
 * 首界面类 <br>
 * 该类用来显示高德地图，并完成基本操作如：定位、导航、搜索、路线规划和停车场列表等
 * <p>
 * Copyright: Copyright (c) 2014-11-13 下午10:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 3.0.0
 */
public class DriverMainScreen extends Activity implements LocationSource,
		AMapLocationListener, OnClickListener, OnMarkerClickListener,
		InfoWindowAdapter, OnMapClickListener {
	/* 高德地图AMap */
	private AMap aMap;
	/* AMapLocation的对象，用于定位——黄志恒注 */
	private AMapLocation bLocation;
	/* 汽车生活按钮 */
	private Button btnDriverLife;
	/* 导航按钮——黄志恒注 */
	private Button btnNavi;
	/* 路径规划按钮 ——黄志恒注 */
	private Button btnRoutePlan;
	/* 列表按钮 */
	private Button btnParkingList;
	/* 登录 ——张芳注 */
	private ImageButton chooseUsers;
	// 获取编辑器
	Editor editor;
	private boolean hasRouted = false;
	/* 输入框 */
	private AutoCompleteTextView keyEdit;
	/* 导航用的点——黄志恒注 */
	private LatLonPoint lp;
	private LocationManagerProxy mAMapLocationManager;
	/* 用来显示地图的MapView */
	private MapView mapView;
	/* 定位监听 */
	private OnLocationChangedListener mListener;
	/* 我的位置坐标 */
	private LatLng myLatlng;
	/* 自定义定位按钮 */
	private ImageButton myLocation;
	/* 进行路径规划的处理对象——黄志恒注 */
	private NaviRouteMethod nRoute;
	/* 周边搜索的类 ——黄志恒注 */
	PoiAroundSearchMethod pas;
	/* 搜索对象——黄志恒注 */
	private PoiSearchMethod poisearch;
	/* 搜索类型——黄志恒注 */
	private String poiType;
	/* 定义sharedpreference获取用户登录注册信息 */
	SharedPreferences sharedPreferences;
	/* 路径规划的目的地的点 ——黄志恒注 */
	private LatLonPoint targetPoint;
	/* 地图的基本设置 */
	private UiSettings uiSettings;
	/* 语音输入 */
	private ImageView voiceInput;
	/* 搜索云图时的点——黄志恒注 */
	private LatLng yunLatlng;

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, -1, 10, this);
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
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	/**
	 * 初始化地图信息
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			uiSettings = aMap.getUiSettings();
			// 设置高德地图的logo在底部中间
			uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
			aMap.setLocationSource(this);// 监听定位
			aMap.moveCamera(CameraUpdateFactory.zoomTo(16));// 更改缩放程度
			new MyLocationSet(aMap).setMapLocation(); // 开始定位
		}
	}

	/**
	 * 初始化Ui控件
	 */
	private void initViews() {
		myLocation = (ImageButton) findViewById(R.id.button_my_location);
		myLocation.setOnClickListener(this);
		keyEdit = (AutoCompleteTextView) findViewById(R.id.actv_key_search);
		poisearch = new PoiSearchMethod(aMap, this, keyEdit); // 调用显示目的地的类（类似于监听效果）
		voiceInput = (ImageButton) findViewById(R.id.button_voice_search);
		voiceInput.setOnClickListener(this);
		btnRoutePlan = (Button) findViewById(R.id.button_route_planning);
		btnRoutePlan.setOnClickListener(this);
		btnNavi = (Button) findViewById(R.id.button_start_navigation);
		btnNavi.setOnClickListener(this);
		btnDriverLife = (Button) findViewById(R.id.button_round_search);
		btnDriverLife.setOnClickListener(this);
		btnParkingList = (Button) findViewById(R.id.button_parking_list);
		btnParkingList.setOnClickListener(this);
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=54818227");
		chooseUsers = (ImageButton) findViewById(R.id.button_chose_user);
		chooseUsers.setOnClickListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnMapClickListener(this);
	}

	/**
	 * 接收返回参数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == 1) { // 从DriverLife传递过来的
			poiType = data.getStringExtra("result_type");
			aMap.clear();
			// 重新在地图上显示附近搜索结果
			pas = new PoiAroundSearchMethod(aMap, this, poiType, lp);
			// 重新在地图上显示云图数据——黄志恒注
			new MyCloudSearch(this, lp.getLatitude(), lp.getLongitude(), aMap);
		}
	}

	/**
	 * Ui控件的事件操作
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击的是自定义定位按钮
		case R.id.button_my_location:
			deactivate();
			new MyLocationSet(aMap).setMapLocation(); // 开始定位
			break;
		// 点击的是语音输入按钮
		case R.id.button_voice_search:
			// 开启语音
			new VoiceSearch(aMap, this).voicePoiSearch();
			break;
		// 点击的是汽车生活按钮
		case R.id.button_round_search:
			Intent intent = new Intent(this, DriverLife.class);
			startActivityForResult(intent, 1);// 带返回值的start
			break;
		// 点击的是路径规划按钮——黄志恒注
		case R.id.button_route_planning:
			planRoute();
			break;
		// 点击的是开始导航按钮
		case R.id.button_start_navigation: {
			planNavi();
			break;
		}
		// 点击的是列表按钮
		case R.id.button_parking_list:
			Intent intent2 = new Intent(this, ParkingList.class); // 跳转到停车场列表界面
			startActivity(intent2);
			break;
		// By Zhangfang
		// 点击用户模式
		case R.id.button_chose_user:
			SharedPreferences shared = getSharedPreferences("loginState",
					Context.MODE_PRIVATE);
			int loginState = shared.getInt("loginState", 2); // 取不到，则默认为0
			if (loginState == 2) {
				Intent it2 = new Intent(DriverMainScreen.this,
						Manager_Driver_model.class);
				it2.putExtra("choose_model", 0);
				startActivity(it2);

			} else if (loginState == 1) {
				Intent it2 = new Intent(DriverMainScreen.this,
						ManagerMain.class);
				it2.putExtra("choose_model", 1);
				startActivity(it2);
				finish();
			}
			break;
		}
	}

	/**
	 * 必须重写onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_main);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须重写
		initMap();
		initViews();
		sharedPreferences = getSharedPreferences("zf", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	/**
	 * 必须重写onDestroy
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 定位完成后回调该方法
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			bLocation = aLocation;
			myLatlng = new LatLng(aLocation.getLatitude(),
					aLocation.getLongitude());// 获取我的位置
			lp = new LatLonPoint(aLocation.getLatitude(),
					aLocation.getLongitude());
			// 显示高德提供的附件的停车场，并产生附近搜索对象——黄志恒注
			pas = new PoiAroundSearchMethod(aMap, this, "停车场", lp);
			// 显示云图数据
			new MyCloudSearch(this, aLocation.getLatitude(),
					aLocation.getLongitude(), aMap);
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
		}
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onMapClick(LatLng arg0) {
		aMap.clear();
		aMap.addMarker(new MarkerOptions().position(arg0).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
		lp = new LatLonPoint(arg0.latitude, arg0.longitude);
		// 显示高德提供的附件的停车场，并产生附近搜索对象——黄志恒注
		pas = new PoiAroundSearchMethod(aMap, this, "停车场", lp);
		// 显示云图数据
		new MyCloudSearch(this, arg0.latitude, arg0.longitude, aMap);
		// mListener.onLocationChanged(bLocation);// 显示系统小蓝点
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		targetPoint = new LatLonPoint(marker.getPosition().latitude,
				marker.getPosition().longitude);
		return false;
	}

	/**
	 * 必须重写onPause
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate(); // 关闭定位
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
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
	 * 必须重写onSaveInstanceState
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 点击“开始导航，调用此方法”
	 */
	private void planNavi() {
		if (targetPoint != null) {
			Bundle bundle = new Bundle();
			bundle.putDouble("start_latitude", lp.getLatitude());
			bundle.putDouble("start_longitude", lp.getLongitude());
			bundle.putDouble("end_latitude", targetPoint.getLatitude());
			bundle.putDouble("end_longitude", targetPoint.getLongitude());
			Intent naviIntent = new Intent();
			naviIntent.putExtras(bundle);
			naviIntent.setClass(DriverMainScreen.this, NaviStartActivity.class);
			startActivity(naviIntent);
		} else {
			ToastUtil.show(this, "请选择目的地");
		}
	}

	/**
	 * 点击“路径规划按钮，调用此方法”
	 */
	private void planRoute() {

		if (targetPoint != null) {
			if (!hasRouted && nRoute == null) {
				nRoute = new NaviRouteMethod(aMap, lp, this, targetPoint);
			} else {
				nRoute.mRouteOverLay.removeFromMap();
				nRoute = new NaviRouteMethod(aMap, lp, this, targetPoint);
			}
		} else {
			ToastUtil.show(this, "请选择目的地");
		}
	}

}
