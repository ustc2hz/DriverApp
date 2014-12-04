package ustc.sse.water.activity;

import java.util.ArrayList;

import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviLatLng;

/**
 * 导航类 <br>
 * 提供导航功能
 * <p>
 * Copyright: Copyright (c) 2014年11月29日 下午3:51:53
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒 sa14226399@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DriverNaviActivity extends Activity implements AMapNaviListener,
		AMapNaviViewListener {

	// 导航View
	private AMapNaviView mAmapAMapNaviView;
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	// 是否为模拟导航
	private boolean mIsEmulatorNavi = false;
	private NaviLatLng mNaviEnd;
	// 起点终点
	private NaviLatLng mNaviStart;
	private ProgressDialog mRouteCalculatorProgressDialog;// 路径规划过程显示状态
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();

	/**
	 * 初始化
	 * 
	 * @param savedInstanceState
	 */
	private void init(Bundle savedInstanceState) {
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simplenavimap);
		// mAmapAMapNaviView.onCreate(savedInstanceState);
		mAmapAMapNaviView.setAMapNaviViewListener(this);
		// TTSController.getInstance(this).startSpeaking();
		/*
		 * if (mIsEmulatorNavi) { // 设置模拟速度
		 * AMapNavi.getInstance(this).setEmulatorNaviSpeed(100); // 开启模拟导航
		 * AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);
		 * 
		 * } else { // 开启实时导航
		 * AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode); }
		 */
	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		mRouteCalculatorProgressDialog.dismiss();
	}

	@Override
	public void onCalculateRouteSuccess() {
		// TODO Auto-generated method stub

		if (mIsEmulatorNavi) {
			// 设置模拟速度
			AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
			// 开启模拟导航
			AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);

		} else {
			// 开启实时导航
			AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navi_map);
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simplenavimap);
		mAmapAMapNaviView.setAMapNaviViewListener(this);
		// mRouteCalculatorProgressDialog = new ProgressDialog(this);
		// mRouteCalculatorProgressDialog.setCancelable(true);

		Bundle bundle = getIntent().getExtras();

		if (bundle.containsKey("start_latitude")
				&& bundle.containsKey("start_longitude")) {
			mNaviStart = new NaviLatLng(bundle.getDouble("start_latitude"),
					bundle.getDouble("start_longitude"));
		}
		if (bundle.containsKey("end_latitude")
				&& bundle.containsKey("end_longitude")) {
			mNaviEnd = new NaviLatLng(bundle.getDouble("end_latitude"),
					bundle.getDouble("end_longitude"));
		}
		if (mNaviStart != null && mNaviEnd != null) {
			mStartPoints.add(mNaviStart);
			mEndPoints.add(mNaviEnd);
		}
		if (mStartPoints != null && mEndPoints != null) {
			AMapNavi.getInstance(this).calculateDriveRoute(mStartPoints,
					mEndPoints, null, AMapNavi.DrivingDefault);

			// mRouteCalculatorProgressDialog.show();
			AMapNavi.getInstance(this).setAMapNaviListener(this);

		} else {
			ToastUtil.show(this, "请选择正确的起始点");
			finish();
		}
		// init(savedInstanceState);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAmapAMapNaviView.onDestroy();

		// TTSController.getInstance(this).stopSpeaking();

	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * 返回键监听事件
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(DriverNaviActivity.this,
					DriverMainScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	// -----------------------------导航界面回调事件------------------------
	/**
	 * 导航界面返回按钮监听
	 * */
	@Override
	public void onNaviCancel() {
		Intent intent = new Intent(DriverNaviActivity.this,
				DriverMainScreen.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviSetting() {

	}

	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		super.onPause();
		mAmapAMapNaviView.onPause();
		AMapNavi.getInstance(this).stopNavi();
	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		mAmapAMapNaviView.onResume();

	}

	// ------------------------------生命周期方法---------------------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAmapAMapNaviView.onSaveInstanceState(outState);
	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

}
