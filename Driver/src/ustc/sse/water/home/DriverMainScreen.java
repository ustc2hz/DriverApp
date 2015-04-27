package ustc.sse.water.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.MarkerDistance;
import ustc.sse.water.data.MyComparetor;
import ustc.sse.water.driver.DriverInfo;
import ustc.sse.water.driver.ParkingDetail;
import ustc.sse.water.manager.LoginActivity;
import ustc.sse.water.manager.ManagerMainTabActivity;
import ustc.sse.water.tools.MyCloudSearch;
import ustc.sse.water.tools.MyLocationSet;
import ustc.sse.water.tools.NaviRouteMethod;
import ustc.sse.water.tools.PoiAroundSearchMethod;
import ustc.sse.water.tools.PoiSearchMethod;
import ustc.sse.water.tools.VoiceSearch;
import ustc.sse.water.utils.CheckNetwork;
import ustc.sse.water.utils.ToastUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.cloud.model.CloudItem;
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
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
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
 * 该类用来显示高德地图，并完成基本操作如：定位、导航、搜索、路线规划和停车场列表等 该类中部分功能使用高德地图API，此APP遵守高德地图API的使用条款
 * <p>
 * Copyright: Copyright (c) 2015-01-31 下午15:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 4.1.0
 */
public class DriverMainScreen extends Activity implements LocationSource,
		AMapLocationListener, OnClickListener, OnMarkerClickListener,
		InfoWindowAdapter, OnMapClickListener, OnMapLongClickListener,
		OnMapTouchListener {

	private static final int LOGIN_STATUS_DRIVER = 0;// 有驾驶员登录过，而且还没有退出
	private static final int LOGIN_STATUS_MANAGER = 1;// 有管理员登录过，而且还没有退出
	private static final int LOGIN_STATUS_NO = 2;// 没有人登录过，或者登录者已经退出
	private long exitTime = 0; // 记录退出按键时间

	public static AMap aMap; /* 高德地图AMap——此类由高德地图API提供 */
	private String[] bookMoney; /* 记录停车场收费信息的数组——黄志恒 */
	private RadioButton Carservice;/* 汽车生活按钮——黄志恒 */
	private int itemDistance; /* 选中的点距离目的位置或自身位置的距离——黄志恒 */
	private AutoCompleteTextView keyEdit; /* 输入框 */
	public static LatLonPoint lp; /* 当前位置的点——此类由高德地图API提供 */
	private LocationManagerProxy mAMapLocationManager/* 坐标监听——此类由高德地图API提供 */;

	private MapView mapView; /* 用来显示地图的MapView——此类由高德地图API提供 */
	MyCloudSearch mCloud; /* 搜索云图的对象——此类由高德地图API提供 */
	private OnLocationChangedListener mListener;/* 定位监听——此类由高德地图API提供 */
	private ImageButton myLocation; /* 自定义定位按钮 */
	private String name; /* 获取当前停车场的名称——黄志恒 */
	private String orderPrice; /* 获取当前停车场的订金信息——黄志恒 */
	private String parkPrice; /* 获取当前停车场的停车收费信息——黄志恒 */
	private String parkSum; /* 停车位数量——黄志恒 */
	PoiAroundSearchMethod pas; /* 周边搜索的类 ——此类由高德地图API提供 */
	private String phone; /* 获取当前停车场的电话号码——黄志恒 */

	private PoiSearchMethod poisearch; /* 搜索对象——此类由高德地图API提供 */
	private String poiType; /* 搜索类型——黄志恒注 */
	private RadioButton RMine; /* '我的'按钮——黄志恒 */
	private RadioButton RNavi; /* 导航按钮——黄志恒 */
	private RadioButton ROrder; /* 预定按钮——黄志恒 */
	SharedPreferences sharedPreferences; /* 定义sharedpreference获取用户登录注册信息 */
	private TextView showInfo;/* 设置一个文本显示区域，用来显示当前停车场的概要信息——黄志恒 */
	private boolean showText = true;/* 判断是否显示文字区域 */
	public static LatLonPoint targetPoint;/* 路径规划的目的地的点 ——此类由高德地图API提供 */
	private UiSettings uiSettings;/* 地图的基本设置——此类由高德地图API提供 */
	private ImageView voiceInput;/* 语音输入 */
	private String managerId;/* 停车场管理员ID */
	private String parkingAddress;/* 停车场地址（坐标） */
	private String parkType;/* 停车场类型：是APP创建或者是WEB创建 */
	private Thread th;/* 实现启动APP默认显示离用户最近的停车场 */

	public static LatLng orderLocation = null;/* 用于显示订单位置——此类由高德地图API提供 */
	private boolean isFirst = true;/* 用来判断是否是第一次启动APP */

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
		aMap.moveCamera(CameraUpdateFactory.zoomTo(16));// 更改缩放程度
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
			new MyLocationSet(aMap).setMapLocation(); // 开始定位
			aMap.moveCamera(CameraUpdateFactory.zoomTo(16));// 更改缩放程度
		}

		RMine.setOnClickListener(this);
		ROrder.setOnClickListener(this);
		RNavi.setOnClickListener(this);
		Carservice.setOnClickListener(this);
		myLocation.setOnClickListener(this);
		// 调用显示目的地的类（类似于监听效果）
		poisearch = new PoiSearchMethod(aMap, this, keyEdit);

		voiceInput.setOnClickListener(this);
		// 讯飞语音输入的key
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=54818227");
		aMap.setOnMarkerClickListener(this);
		aMap.setOnMapClickListener(this);
		aMap.setOnMapLongClickListener(this);// 对 amap 添加长按地图事件监听器
		aMap.setOnMapTouchListener(this);
		aMap.moveCamera(CameraUpdateFactory.zoomTo(16));

		// 判断是不是第一次打开APP，如果是的话，执行子线程。
		if (isFirst) {
			th = new Thread(new showFirstM());
			th.start();
		}

	}

	/**
	 * 初始化Ui控件
	 */
	private void initViews() {
		Carservice = (RadioButton) findViewById(R.id.radio_Carservice);
		RNavi = (RadioButton) findViewById(R.id.radio_navi);
		ROrder = (RadioButton) findViewById(R.id.radio_order);
		RMine = (RadioButton) findViewById(R.id.radio_mine);
		showInfo = (TextView) findViewById(R.id.show_directory);
		myLocation = (ImageButton) findViewById(R.id.button_my_location);
		keyEdit = (AutoCompleteTextView) findViewById(R.id.actv_key_search);
		voiceInput = (ImageButton) findViewById(R.id.button_voice_search);
	}

	/**
	 * 接收从DriverLife传递过来的返回参数 此方法由高德地图API提供
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == 1) {
			poiType = data.getStringExtra("result_type");
			aMap.clear();
			this.showInfo.setVisibility(View.INVISIBLE);
			// 重新在地图上显示附近搜索结果
			pas = new PoiAroundSearchMethod(aMap, this, poiType, targetPoint);
			aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
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
			// 开始定位
			new MyLocationSet(aMap).setMapLocation();
			showInfo.setVisibility(View.INVISIBLE);
			break;
		// 点击的是语音输入按钮
		case R.id.button_voice_search:
			// 开启语音
			new VoiceSearch(aMap, this).voicePoiSearch();
			break;
		// 点击的是汽车生活按钮
		case R.id.radio_Carservice:
			Intent intent = new Intent(this, DriverLife.class);
			// 带返回值的start
			startActivityForResult(intent, 1);
			break;
		// 点击的是开始导航按钮
		case R.id.radio_navi: {
			planNavi();
			break;
		}
		// 点击用户模式
		case R.id.radio_mine:
			Intent intentUser = new Intent();
			SharedPreferences shared = getSharedPreferences("userdata",
					Context.MODE_PRIVATE);

			int loginState = shared.getInt("userLoginStatus", 2); // 取不到，则默认为2
			switch (loginState) {
			case LOGIN_STATUS_DRIVER: // 有驾驶员登录过，而且还没有退出
				intentUser.setClass(DriverMainScreen.this, DriverInfo.class);
				break;
			case LOGIN_STATUS_MANAGER: // 有管理员登录过，而且还没有退出
				intentUser.setClass(DriverMainScreen.this,
						ManagerMainTabActivity.class);
				break;
			case LOGIN_STATUS_NO: // 没有人登录过，或者登录者已经退出
				intentUser.setClass(DriverMainScreen.this, LoginActivity.class);
				break;

			}
			startActivity(intentUser);
			break;
		// 直接预定当前停车场的订单
		case R.id.radio_order:
			sendDataToBook();
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

		// 检查网络
		CheckNetwork cn = new CheckNetwork(this);
		cn.checkNetworkState();

		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须重写
		initViews();
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
	 * 定位完成后回调该方法 此方法由高德地图API提供
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			lp = new LatLonPoint(aLocation.getLatitude(),
					aLocation.getLongitude());
			// 显示高德提供的附件的停车场，并产生附近搜索对象——黄志恒注
			pas = new PoiAroundSearchMethod(aMap, this, "停车场", lp);
			// 显示云图数据
			mCloud = new MyCloudSearch(this, aLocation.getLatitude(),
					aLocation.getLongitude(), aMap);
			aMap.moveCamera(CameraUpdateFactory.zoomTo(16));// 更改缩放程度
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
		}
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	/**
	 * 响应地图点击事件
	 *
	 * @param arg0
	 *            点击的点生成的对象 此方法由高德地图API提供
	 */
	@Override
	public void onMapClick(LatLng arg0) {
		aMap.clear();
		aMap.addMarker(new MarkerOptions().position(arg0).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
		targetPoint = new LatLonPoint(arg0.latitude, arg0.longitude);
		// 显示高德提供的附件的停车场，并产生附近搜索对象——黄志恒注
		pas = new PoiAroundSearchMethod(aMap, this, "停车场", targetPoint);
		// 显示云图数据
		mCloud = new MyCloudSearch(this, arg0.latitude, arg0.longitude, aMap);
		// 改变地图显示的缩放程度
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(arg0, 15);
		aMap.moveCamera(update);
		showInfo.setVisibility(View.INVISIBLE);

	}

	/**
	 * 响应地图长时间点击点击事件
	 *
	 * @param arg0
	 *            点击的点生成的对象 此方法由高德地图API提供
	 */
	@Override
	public void onMapLongClick(LatLng arg0) {
		if (showText == false) {
			this.showInfo.setVisibility(View.INVISIBLE);
		} else {
			showText = false;
		}
	}

	/**
	 * 点击地图上标记点后执行的操作 * @param marker 点击的点生成的标记对象 此方法由高德地图API提供
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		// 将标记点的内容置为空
		marker.setSnippet("");
		// 在文本区域中显示这个标记点的相关信息
		TextshowMarkerInfo(marker);
		// 让这个标记点在前段显示
		marker.setToTop();
		// 显示这个标记点的信息框
		marker.showInfoWindow();
		// 给目的地点到对象初始化
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
		locationOfOrder();
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
	protected void onStart() {
		super.onStart();
		initMap();
		sharedPreferences = getSharedPreferences("zf", Context.MODE_PRIVATE);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onTouch(MotionEvent arg0) {
	}

	/**
	 * 点击“开始导航，调用此方法”
	 */
	private void planNavi() {
		if (targetPoint != null) {
			Bundle bundle = new Bundle();
			// lp 是导航起始点
			bundle.putDouble("start_latitude", lp.getLatitude());
			bundle.putDouble("start_longitude", lp.getLongitude());
			// targetPoint是导航目的点
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
	public void planRoute() {

		if (targetPoint != null) {
			aMap.clear();
			new NaviRouteMethod(aMap, lp, this, targetPoint);

		} else {
			ToastUtil.show(this, "请选择目的地");
		}
	}

	/**
	 * 将选中的停车场的信息传递给预定页面——黄志恒
	 */
	private void sendDataToBook() {
		if (this.phone == null) {
			Toast.makeText(getApplicationContext(), "抱歉，此停车场不提供预定",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parkingName", this.name); // 存储停车场的名字
		map.put("parkingDistance", this.itemDistance); // 存储停车场到中心点的距离
		map.put("parkingSum", this.parkSum);// 停车场车位数
		map.put("bookMoney", this.bookMoney);// 停车场收费信息
		map.put("phone", this.phone);// 停车场电话
		map.put("isAmap", "AMapCloudPark"); // 标记是云图的停车
		map.put("managerId", this.managerId); // 标记是云图的停车
		map.put("parkingAddress", this.parkingAddress); // 停车场坐标
		map.put("parkType", this.parkType); // 停车场的类型：app或者web

		Intent intent = new Intent(DriverMainScreen.this, ParkingDetail.class);
		// 将选中的停车场封装到Intent中
		intent.putExtra("select_parking", (Serializable) map);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	/**
	 * 开启一个线程，当地图准备好之后，自动显示最近的停车场
	 */
	class showFirstM implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 空转方法，有条件的阻止线程运行
			while (aMap.getMapScreenMarkers() == null) {
			}
			Message ms = mShowFirst.obtainMessage();
			ms.arg1 = 1;
			mShowFirst.sendMessage(ms);

		}
	}

	/**
	 * handler对象，用来处理showFirstM线程的请求
	 */
	Handler mShowFirst = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {

				List<MarkerDistance> list = new ArrayList<MarkerDistance>();

				for (Marker marker : aMap.getMapScreenMarkers()) {
					if ("".equals(marker.getSnippet())) {
						list.add(new MarkerDistance(marker, AMapUtils
								.calculateLineDistance(
										new LatLng(lp.getLatitude(), lp
												.getLongitude()), marker
												.getPosition())));
					}
				}

				if (list.size() > 0) {
					// 用list中的对象的距离属性对list中的对象排序，使距离最短的对象排在list中的第一个位置
					Collections.sort(list, new MyComparetor());
					Marker mMarker = list.get(0).marker;
					mMarker.showInfoWindow();
					// 在文本区域中显示这个标记点的信息
					TextshowMarkerInfo(mMarker);
					targetPoint = new LatLonPoint(
							mMarker.getPosition().latitude,
							mMarker.getPosition().longitude);
					th = null;
					isFirst = false;

				} else {
					th = null;
					isFirst = false;
				}

			}
		}

	};

	/**
	 * 在首界面显示点击的marker信息
	 *
	 * @param name
	 *            停车场地址
	 * @param phone
	 *            停车场电话
	 * @param orderPrice
	 *            停车场预定价格条目
	 * @param parkPrice
	 *            停车场停车收费条目
	 */
	private String showParkInfo(String name, String phone, String orderPrice,
			String parkPrice) {
		// 如果停车场名字为null，则直接退出该函数
		if (phone == null) {
			return null;
		}
		// 新建字符容器，来存储字符
		StringBuffer sb = new StringBuffer();
		if (name.length() > 17) {
			name = name.substring(0, 15) + "...";
		}
		if (!"暂无".equals(orderPrice) && Integer.valueOf(orderPrice) < 100) {
			orderPrice = "提前10分钟预定收取" + orderPrice + "元订金";
		}
		if (!"暂无".equals(parkPrice) && Integer.valueOf(parkPrice) < 100) {
			parkPrice = "停车1个小时收取" + parkPrice + "元车费";
		}
		// 对文本输出格式做处理
		sb.append("\n" + "\t名称： " + name + "\t\n" + "\t电话： " + phone + "\t\n"
				+ "\t订金： " + orderPrice + "\t\n" + "\t价格： " + parkPrice + "\t");
		return sb.toString();
	}

	/**
	 * 在文字区域显示停车场大概信息
	 *
	 * @param marker
	 *            点击的地图上的点
	 */
	private void TextshowMarkerInfo(Marker marker) {
		this.name = marker.getTitle();
		this.phone = null;
		this.orderPrice = null;
		this.parkPrice = null;
		StringBuilder pka = new StringBuilder();
		pka.append(marker.getPosition().longitude);
		pka.append(",");
		pka.append(marker.getPosition().latitude);
		parkingAddress = pka.toString();
		String title = marker.getTitle();
		// 当从服务器中搜索到数据，就进入if语句中执行
		if (mCloud != null) {
			for (CloudItem mItem : MyCloudSearch.mCloudItems) {
				if (title.equals(mItem.getTitle())) {
					bookMoney = new String[6];
					for (int i = 0; i < 6; i++) {
						bookMoney[i] = "0";
					}
					this.itemDistance = mItem.getDistance();

					Iterator iter = mItem.getCustomfield().entrySet()
							.iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						Object key = entry.getKey();
						Object value = entry.getValue();
						// 停车场电话号码
						if ("phone".equals(key)) {
							this.phone = (String) value;
							// 停车场停车位总数
						} else if ("parkSum".equals(key)) {
							this.parkSum = (String) value;
							// 预定10分钟的订金
						} else if ("orderTen".equals(key)) {
							this.orderPrice = (String) value;
							bookMoney[0] = (String) value;
							// 预定20分钟的订金
						} else if ("orderTwe".equals(key)) {
							bookMoney[1] = (String) value;
							// 预定30分钟的订金
						} else if ("orderTri".equals(key)) {
							bookMoney[2] = (String) value;
							// 停车30分钟的收费标准
						} else if ("payHalPay".equals(key)) {
							bookMoney[3] = (String) value;
							// 停车1小时的收费标准
						} else if ("payOneHour".equals(key)) {
							this.parkPrice = (String) value;
							bookMoney[4] = (String) value;
							// 停车多于1个小时的收费标准
						} else if ("payMorePay".equals(key)) {
							bookMoney[5] = (String) value;
							// 停车场的id
						} else if ("managerId".equals(key)) {
							managerId = (String) value;
							// 停车场的类型：私人停车场或者高德停车场
						} else if ("parkType".equals(key)) {
							this.parkType = (String) value;
						}

					}
				}
			}
		}
		if (this.phone == null) {
			this.showInfo.setText("  无此停车场详细信息                 ");
			this.itemDistance = 0;
			return;

		}
		// 提取出来4条信息：停车场名称，停车场电话，预定10分钟的订金，停车费用规则放入文本区域输出
		String markerData = showParkInfo(this.name, this.phone,
				this.orderPrice, this.parkPrice);
		this.showInfo.setText(markerData);
		this.showInfo.setVisibility(View.VISIBLE);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 连续两次按回退键则退出程序
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				ToastUtil.show(this, "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 显示订单停车场的位置
	 */
	public void locationOfOrder() {

		if (orderLocation != null) {
			aMap.clear();
			// 在地图上显示离自己距离最近标记点
			aMap.addMarker(new MarkerOptions().position(orderLocation).icon(
					BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
		}
		orderLocation = null;
	}
}
