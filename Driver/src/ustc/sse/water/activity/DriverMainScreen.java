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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMapTouchListener;
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
 * Copyright: Copyright (c) 2015-01-31 下午15:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 4.0.0
 */
public class DriverMainScreen extends Activity implements LocationSource,
		AMapLocationListener, OnClickListener, OnMarkerClickListener,
		InfoWindowAdapter, OnMapClickListener, OnMapLongClickListener,
		OnMapTouchListener {
	/* 高德地图AMap */
	private AMap aMap;

	/* 汽车生活按钮——黄志恒 */
	private RadioButton Carservice;
	// 获取编辑器
	Editor editor;
	/* 静态常量，当没有停车场信息时使用 */
	private final String GREETING_WORDS = "很抱歉，附近暂无可用停车场";
	/* 是否有路径的状态判断 */
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
	/* 移动点击的点——黄志恒注 */
	private LatLonPoint molp;
	/* 自定义定位按钮 */
	private ImageButton myLocation;
	/* 获取当前停车场的名称——黄志恒 */
	private String name;
	/* 进行路径规划的处理对象——黄志恒注 */
	private NaviRouteMethod nRoute;
	/* 获取当前停车场的订金信息——黄志恒 */
	private String orderPrice;
	/* 获取当前停车场的停车收费信息——黄志恒 */
	private String parkPrice;
	/* 周边搜索的类 ——黄志恒注 */
	PoiAroundSearchMethod pas;
	/* 获取当前停车场的电话号码——黄志恒 */
	private String phone;
	/* 搜索对象——黄志恒注 */
	private PoiSearchMethod poisearch;
	/* 搜索类型——黄志恒注 */
	private String poiType;
	/* '我的'按钮——黄志恒 */
	private RadioButton RMine;
	/* '更多'按钮——黄志恒 */
	private RadioButton RMore;
	/* 导航按钮——黄志恒 */
	private RadioButton RNavi;
	/* 预定按钮——黄志恒 */
	private RadioButton ROrder;
	/* 定义sharedpreference获取用户登录注册信息 */
	SharedPreferences sharedPreferences;

	/* 设置一个文本显示区域，用来显示当前停车场的概要信息——黄志恒 */
	private TextView showInfo;

	/* 判断是否显示文字区域 */
	private boolean showText = true;
	/* 路径规划的目的地的点 ——黄志恒注 */
	private LatLonPoint targetPoint;

	/* 地图的基本设置 */
	private UiSettings uiSettings;

	/* 语音输入 */
	private ImageView voiceInput;

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

		Carservice = (RadioButton) findViewById(R.id.radio_Carservice);
		Carservice.setOnClickListener(this);
		RNavi = (RadioButton) findViewById(R.id.radio_navi);
		RNavi.setOnClickListener(this);
		ROrder = (RadioButton) findViewById(R.id.radio_order);
		ROrder.setOnClickListener(this);
		RMine = (RadioButton) findViewById(R.id.radio_mine);
		RMine.setOnClickListener(this);
		RMore = (RadioButton) findViewById(R.id.radio_more);
		RMore.setOnClickListener(this);

		showInfo = (TextView) findViewById(R.id.show_directory);

		// 如果有字符串，则向文字显示区域付初始值
		String tempStr = showParkInfo(this.name, this.phone, this.orderPrice,
				this.parkPrice);
		if (tempStr != null) {
			showInfo.setText(tempStr);
		} else {
			showInfo.setText(GREETING_WORDS);
		}

		myLocation = (ImageButton) findViewById(R.id.button_my_location);
		myLocation.setOnClickListener(this);
		keyEdit = (AutoCompleteTextView) findViewById(R.id.actv_key_search);
		poisearch = new PoiSearchMethod(aMap, this, keyEdit); // 调用显示目的地的类（类似于监听效果）
		voiceInput = (ImageButton) findViewById(R.id.button_voice_search);
		voiceInput.setOnClickListener(this);
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=54818227");

		aMap.setOnMarkerClickListener(this);
		aMap.setOnMapClickListener(this);
		aMap.setOnMapLongClickListener(this);// 对 amap 添加长按地图事件监听器
		aMap.setOnMapTouchListener(this);
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
		case R.id.radio_Carservice:
			Intent intent = new Intent(this, DriverLife.class);
			startActivityForResult(intent, 1);// 带返回值的start
			break;
		// 点击的是开始导航按钮
		case R.id.radio_navi: {
			planNavi();
			break;
		}
		// 点击的是列表按钮
		case R.id.radio_more:
			Intent intent2 = new Intent(this, ParkingList.class); // 跳转到停车场列表界面
			startActivity(intent2);
			break;
		// By Zhangfang
		// 点击用户模式
		case R.id.radio_mine:
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
		// 直接预定当前停车场的订单
		case R.id.radio_order:

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
	public void onLocationChanged(AMapLocation aLocation) {// lp wrong here
		if (mListener != null && aLocation != null) {
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
		molp = new LatLonPoint(arg0.latitude, arg0.longitude);
		// 显示高德提供的附件的停车场，并产生附近搜索对象——黄志恒注
		pas = new PoiAroundSearchMethod(aMap, this, "停车场", molp);
		// 显示云图数据
		new MyCloudSearch(this, arg0.latitude, arg0.longitude, aMap);
		// mListener.onLocationChanged(bLocation);// 显示系统小蓝点
	}

	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub
		if (showText == false) {
			this.showInfo.setVisibility(View.INVISIBLE);
		} else {
			showText = false;
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		String title = marker.getTitle();
		String val = marker.getSnippet();
		Log.v(title, val);

		marker.setSnippet("下面显示概要");
		marker.setToTop();
		marker.showInfoWindow();

		targetPoint = new LatLonPoint(marker.getPosition().latitude,
				marker.getPosition().longitude);
		this.showInfo.setVisibility(View.VISIBLE);
		showText = true;
		return true;
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

	@Override
	public void onTouch(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (showText == false) {
			this.showInfo.setVisibility(View.INVISIBLE);
		} else {
			showText = false;
		}
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
	 * 点击“路径规划按钮，调用此方法” 以后可能有用，误删——黄志恒
	 */
	/*
	 * private void planRoute() {
	 * 
	 * if (targetPoint != null) { if (!hasRouted && nRoute == null) { nRoute =
	 * new NaviRouteMethod(aMap, lp, this, targetPoint); } else {
	 * nRoute.mRouteOverLay.removeFromMap(); nRoute = new NaviRouteMethod(aMap,
	 * lp, this, targetPoint); } } else { ToastUtil.show(this, "请选择目的地"); } }
	 */

	/*
	 * 为文本区域赋值并做输出格式处理
	 * 
	 * @param name 停车场名称
	 * 
	 * @param phone 停车场电话
	 * 
	 * @param orderPrice 停车场订金价格
	 * 
	 * @param parkPrice 停车场停车收费价格
	 */

	private String showParkInfo(String name, String phone, String orderPrice,
			String parkPrice) {
		// 如果停车场名字为null，则直接退出该函数
		if (name == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if (name.length() > 17) {
			name = name.substring(0, 15) + "...";
		}
		// 对文本输出格式做处理
		sb.append("\n" + "\t名称： " + name + "\t\n" + "\t电话： " + phone + "\t\n"
				+ "\t订金： " + orderPrice + "\t\n" + "\t价格： " + parkPrice + "\t");
		return sb.toString();
	}

}
