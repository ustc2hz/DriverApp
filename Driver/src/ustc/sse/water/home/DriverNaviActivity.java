package ustc.sse.water.home;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.NaviUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;

/**
 * 导航类，导航界面 <br>
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
public class DriverNaviActivity extends Activity implements
		AMapNaviViewListener {
	// 导航View
	private AMapNaviView mAmapAMapNaviView;
	// 记录有哪个页面跳转而来，处理返回键
	private int mCode = -1;
	// 是否为模拟导航
	private boolean mIsEmulatorNavi = true;

	/**
	 * 初始化
	 */
	private void init(Bundle savedInstanceState) {

		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simple_navimap);
		mAmapAMapNaviView.onCreate(savedInstanceState);
		mAmapAMapNaviView.setAMapNaviViewListener(this);
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

	/**
	 * Activity生命周期起始函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_navi);
		Bundle bundle = getIntent().getExtras();
		processBundle(bundle);
		// 初始化数据
		init(savedInstanceState);

	}

	/**
	 * Activity生命周期结束函数
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mAmapAMapNaviView.onDestroy();

	}

	/**
	 *
	 * 返回键监听事件
	 *
	 * @keyCode 返回键 @ event 事件
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mCode == NaviUtils.SIMPLEROUTENAVI) {
				Intent intent = new Intent(DriverNaviActivity.this,
						DriverMainScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				setResult(4, intent);
				finish();

			} else if (mCode == NaviUtils.SIMPLEGPSNAVI) {
				Intent intent = new Intent(DriverNaviActivity.this,
						DriverMainScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
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
		setResult(4, intent);
		finish();
	}

	@Override
	public void onNaviMapMode(int arg0) {
	}

	@Override
	public void onNaviSetting() {
	}

	@Override
	public void onNaviTurnClick() {
	}

	@Override
	public void onNextRoadClick() {
	}

	@Override
	public void onPause() {
		super.onPause();
		mAmapAMapNaviView.onPause();
		AMapNavi.getInstance(this).stopNavi();
	}

	@Override
	public void onResume() {
		super.onResume();
		mAmapAMapNaviView.onResume();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAmapAMapNaviView.onSaveInstanceState(outState);
	}

	@Override
	public void onScanViewButtonClick() {
	}

	/**
	 * 处理传入进来的数据
	 * @param bundle 存储数据的对象
	 */
	private void processBundle(Bundle bundle) {
		if (bundle != null) {
			mIsEmulatorNavi = bundle.getBoolean(NaviUtils.ISEMULATOR, true);
			mCode = bundle.getInt(NaviUtils.ACTIVITYINDEX);
		}
	}

}
