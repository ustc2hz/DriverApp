package ustc.sse.water.activity;

import java.util.ArrayList;

import ustc.sse.water.tools.zjx.MyLocationSet;
import ustc.sse.water.tools.zjx.PoiAroundSearchMethod;
import ustc.sse.water.tools.zjx.PoiSearchMethod;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import com.amap.api.services.core.LatLonPoint;

/**
 * 
 * 首界面类 <br>
 * 该类用来显示高德地图，并完成基本操作如：定位、导航、搜索、路线规划等
 * <p>
 * Copyright: Copyright (c) 2014-11-13 下午10:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.2.0
 */
public class DriverMainScreen extends Activity implements LocationSource,
		AMapLocationListener, OnClickListener {
	/* 高德地图AMap */
	private AMap aMap;
	/* 用来显示地图的MapView */
	private MapView mapView;
	/* 地图的基本设置 */
	private UiSettings uiSettings;
	/* 我的位置坐标 */
	private LatLng myLatlng;
	/* 定位监听 */
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	/* 自定义定位按钮 */
	private ImageButton myLocation;
	/* 输入框 */
	private AutoCompleteTextView keyEdit;
	/* 语音输入 */
	private ImageView voiceInput;
	/* 语音输入的确认码 */
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

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
			// uiSettings.setMyLocationButtonEnabled(false); // 不显示高德自带的定位按钮
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
		new PoiSearchMethod(aMap, this, keyEdit); // 显示我的位置附件的停车场
		voiceInput = (ImageButton) findViewById(R.id.button_voice_search);
		voiceInput.setOnClickListener(this);
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

			mListener.onLocationChanged(aLocation);// 显示系统小蓝点

			Log.i("go_into", ">>>>>>>changed");
			Log.i("x", "" + aLocation.getLatitude());
			Log.i("y", "" + aLocation.getLongitude());
			myLatlng = new LatLng(aLocation.getAltitude(),
					aLocation.getLongitude());// 获取我的位置
			LatLonPoint lp = new LatLonPoint(aLocation.getLatitude(),
					aLocation.getLongitude());
			new PoiAroundSearchMethod(aMap, this, "", lp);
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
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, -1, 10, this);
			Log.i("go_into", ">>>>>>>start");
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
		Log.i("go_into", ">>>>>>>stop");
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

	/**
	 * Ui控件的事件操作
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_my_location:
			// aMap.setMyLocationEnabled(true);
			deactivate();
			// aMap.animateCamera(CameraUpdateFactory.changeLatLng(myLatlng));
			// aMap.clear();
			new MyLocationSet(aMap).setMapLocation(); // 开始定位
			Log.i("click_button", "----------->>>");
			break;
		case R.id.button_voice_search:
			startVoiceInput(); // 开启Google语音服务
			break;
		}

	}

	/**
	 * 打开Google语音输入
	 */
	private void startVoiceInput() {
		try {
			// 通过Intent传递语音识别，开启语音
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			// 语言模式和自由模式的语音识别
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// 提示语音开始
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
			// 开始语音识别
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		} catch (Exception e) {
			// 无语音设备异常处理
			e.printStackTrace();
			ToastUtil.show(this, "找不到语音设备!");
		}
	}

	/**
	 * 语音输入完成后回调方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 获取语音解析后的结果
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// 取得语音的字符
			ArrayList<String> result = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.size(); i++) {
				sb.append(result.get(i));
			}
			ToastUtil.show(this, sb.toString());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
