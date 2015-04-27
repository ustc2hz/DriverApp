package ustc.sse.water.home;

import ustc.sse.water.activity.R;
import ustc.sse.water.tools.MyLocationSet;
import ustc.sse.water.tools.PoiSearchMethod;
import ustc.sse.water.tools.VoiceSearch;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Activity类. <br>
 * 地图选点. 该类中部分功能使用高德地图API，此APP遵守高德地图API的使用条款
 * <p>
 * Copyright: Copyright (c) 2015-4-19 上午10:17:13
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 黄志恒 sa14226399@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MapForAddress extends Activity implements LocationSource,
		AMapLocationListener, OnClickListener, InfoWindowAdapter,
		OnMarkerClickListener, OnMapClickListener {

	private AMap aMap; /* 高德地图AMap——此类由高德地图API提供 */
	private Button buttonBack;// “返回”按钮
	private Button buttonYes;// “确定”按钮
	Editor editor;// 获取编辑器
	private TextView infoShow;// 显示点击处的坐标
	private AutoCompleteTextView keyEdit;/* 输入框 */
	private double latitude;// 点击点的纬度
	public String location;// 选中点的坐标
	private double longitude;// 点击点的经度
	private LocationManagerProxy mAMapLocationManager;// 监听地图定位信息——此类由高德地图API提供
	private MapView mapView;/* 用来显示地图的MapView */
	private OnLocationChangedListener mListener;/* 定位监听——此类由高德地图API提供 */
	private ImageButton myLocation;/* 自定义定位按钮 */
	private final int resultCode = 4;/* 返回传输的识别码 */
	SharedPreferences sharedPreferences;/* 定义sharedpreference获取用户登录注册信息 */
	private UiSettings uiSettings;/* 地图的基本设置——此类由高德地图API提供 */
	private ImageView voiceInput;/* 语音输入 */

	/**
	 * 激活定位 此方法由高德地图API提供
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
	 * 停止定位 此方法由高德地图API提供
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

		myLocation = (ImageButton) findViewById(R.id.button_my_location_map);
		myLocation.setOnClickListener(this);
		keyEdit = (AutoCompleteTextView) findViewById(R.id.actv_key_search_map);
		new PoiSearchMethod(aMap, this, keyEdit); // 调用显示目的地的类（类似于监听效果）
		voiceInput = (ImageButton) findViewById(R.id.button_voice_search_map);
		voiceInput.setOnClickListener(this);
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=54818227");

		infoShow = (TextView) findViewById(R.id.show_directory_map);

		buttonBack = (Button) findViewById(R.id.button_map_back);
		buttonYes = (Button) findViewById(R.id.button_map_yes);

		aMap.setOnMapClickListener(this);
		aMap.setOnMarkerClickListener(this);
		buttonBack.setOnClickListener(this);
		buttonYes.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击的是自定义定位按钮
		case R.id.button_my_location_map:
			infoShow.setVisibility(View.INVISIBLE);
			deactivate();
			new MyLocationSet(aMap).setMapLocation(); // 开始定位
			break;
		// 点击的是语音输入按钮
		case R.id.button_voice_search_map:
			// 开启语音
			new VoiceSearch(aMap, this).voicePoiSearch();
			break;
		// 点击后确认输入坐标点
		case R.id.button_map_yes:
			// 存储数据
			saveLL();
			Intent intent = new Intent();
			intent.putExtra("location", location);
			setResult(resultCode, intent);
			finish();
			break;
		// 点击后返回之前的页面
		case R.id.button_map_back:
			finish();
			break;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_for_address);
		mapView = (MapView) findViewById(R.id.map2);
		mapView.onCreate(savedInstanceState);// 必须重写
		initMap();// 初始化地图
		initViews();// 初始化界面

		sharedPreferences = getSharedPreferences("manager_message",
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map_for_address, menu);
		return true;
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
	public void onLocationChanged(AMapLocation arg0) {
		mListener.onLocationChanged(arg0);// 显示系统小蓝点
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onMapClick(LatLng arg0) {

		aMap.clear();
		MarkerOptions mm = new MarkerOptions().position(arg0).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
		// 向地图上添加一个坐标点
		aMap.addMarker(mm);
		showLLOnDirectory(mm.getPosition());
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.showInfoWindow();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	 * 将点击的点的经纬度保存到preference中
	 */
	public void saveLL() {
		if (latitude != 0 && longitude != 0) {
			location = longitude + "," + latitude;
		} else {
			Toast.makeText(this, "no infomation", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 在一个文本区域显示点击点的坐标
	 *
	 * @param thePoint
	 *            点击的坐标点的对象
	 */
	public void showLLOnDirectory(LatLng thePoint) {

		// 将thePoint的经纬度保存
		latitude = thePoint.latitude;
		longitude = thePoint.longitude;

		StringBuilder sb = new StringBuilder();
		sb.append("经度： ");
		sb.append(thePoint.longitude);
		sb.append(" , ");
		sb.append("纬度： ");
		sb.append(thePoint.latitude);
		sb.append(" ");
		// 显示标记点的坐标
		infoShow.setVisibility(View.VISIBLE);
		infoShow.setText(sb.toString());

	}

}
