package ustc.sse.water.activity;

import java.util.ArrayList;

import ustc.sse.water.tools.hzh.TTSController;
import ustc.sse.water.utils.zjx.DialogUtil;
import ustc.sse.water.utils.zjx.NaviUtils;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviLatLng;

/**
 * 
 * 实时导航界面
 * */
public class NaviStartActivity extends Activity implements OnClickListener,
		AMapNaviListener, AMapNaviViewListener {
	private DialogUtil dialog = new DialogUtil(this);
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	private NaviLatLng mNaviEnd;
	// 起点终点
	private NaviLatLng mNaviStart;
	private ProgressDialog mRouteCalculatorProgressDialog;// 路径规划过程显示状态
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();

	private void initNaviLatLng() {
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
			dialog.showProgressDialog();
			AMapNavi.getInstance(this).setAMapNaviListener(this);
		} else {
			ToastUtil.show(this, "请选择正确的起始点");
			finish();
		}
	}

	private void initView() {
		mStartPoints.add(mNaviStart);
		mEndPoints.add(mNaviEnd);

		mRouteCalculatorProgressDialog = new ProgressDialog(this);
		mRouteCalculatorProgressDialog.setCancelable(true);

		AMapNavi.getInstance(this).setAMapNaviListener(this);
	}

	// ---------------------导航回调--------------------
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
		mRouteCalculatorProgressDialog.dismiss();

	}

	@Override
	public void onCalculateRouteSuccess() {
		dialog.dissmissProgressDialog();
		Intent intent = new Intent(NaviStartActivity.this,
				DriverNaviActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		Bundle bundle = new Bundle();
		bundle.putInt(NaviUtils.ACTIVITYINDEX, NaviUtils.SIMPLEGPSNAVI);
		bundle.putBoolean(NaviUtils.ISEMULATOR, false);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();

	}

	// --------------------------------点击事件------------------
	@Override
	public void onClick(View v) {
		/*
		 * switch (v.getId()) { case R.id.gps_start_navi_button:
		 * AMapNavi.getInstance(this).calculateDriveRoute(mStartPoints,
		 * mEndPoints, null, AMapNavi.DrivingDefault);
		 * mRouteCalculatorProgressDialog.show(); break; }
		 */

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi_start);
		// 语音播报开始
		TTSController.getInstance(this).startSpeaking();
		initNaviLatLng();
		// initView();
		planRoute();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 删除导航监听

		AMapNavi.getInstance(this).removeAMapNaviListener(this);
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

	// 返回键处理事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(NaviStartActivity.this,
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

	// ---------------------导航View事件回调-----------------------------
	@Override
	public void onNaviCancel() {

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
		// TODO Auto-generated method stub

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
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

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

	private void planRoute() {
		AMapNavi.getInstance(this).calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
	}

}
