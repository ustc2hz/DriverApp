package ustc.sse.water.home;

import java.util.ArrayList;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.NaviUtils;
import ustc.sse.water.utils.ProgressDialogUtil;
import ustc.sse.water.utils.ToastUtil;
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
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 *
 * 导航中间层类 <br>
 * 该类用来对导航类的初始化，提供导航类的上下文
 * <p>
 * Copyright: Copyright (c) 2014-12-08 下午17:12:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 2.0.0
 */
public class NaviStartActivity extends Activity implements OnClickListener,
		AMapNaviListener, AMapNaviViewListener {
	/* 对话框类对象 */
	private ProgressDialogUtil dialog = new ProgressDialogUtil(this, "正在搜索...");
	/* 终点列表 */
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	/* 终点 */
	private NaviLatLng mNaviEnd;
	/* 起点 */
	private NaviLatLng mNaviStart;
	/* 路径规划过程显示状态 */
	private ProgressDialog mRouteCalculatorProgressDialog;
	/* 起点列表 */
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	/* 创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener */
	public SpeechSynthesizer mTts;

	/**
	 * 导航语音监听
	 */
	private SynthesizerListener mSynListener = new SynthesizerListener() {

		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		}

		@Override
		public void onCompleted(SpeechError arg0) {
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		}

		@Override
		public void onSpeakBegin() {
		}

		@Override
		public void onSpeakPaused() {
		}

		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {
		}

		@Override
		public void onSpeakResumed() {
		}
	};

	/**
	 * 初始化起点终点列表
	 */
	private void initNaviLatLng() {

		mTts = SpeechSynthesizer.createSynthesizer(this, null);
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");// 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围 0~100
		mTts.startSpeaking("", mSynListener);

		// 获取传递到此页面的数据
		Bundle bundle = getIntent().getExtras();

		// 存在起始坐标，进入if方法
		if (bundle.containsKey("start_latitude")
				&& bundle.containsKey("start_longitude")) {
			mNaviStart = new NaviLatLng(bundle.getDouble("start_latitude"),
					bundle.getDouble("start_longitude"));
		}
		// 存在终点坐标，进入if方法
		if (bundle.containsKey("end_latitude")
				&& bundle.containsKey("end_longitude")) {
			mNaviEnd = new NaviLatLng(bundle.getDouble("end_latitude"),
					bundle.getDouble("end_longitude"));
		}
		// 当起始和终止坐标都不为空时，将它们加入路经计算队列
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

	// ---------------------导航回调--------------------
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2 && resultCode == 4) {
			if (mTts != null) {
				mTts.destroy();
			}
			// 返回首界面
			AMapNavi.getInstance(this).removeAMapNaviListener(this);
			Intent intent = new Intent(NaviStartActivity.this,
					DriverMainScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			AMapNavi.getInstance(this).removeAMapNaviListener(this);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onArriveDestination() {
		mTts.startSpeaking("到达目的地", mSynListener);
	}

	@Override
	public void onArrivedWayPoint(int arg0) {
	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		mTts.startSpeaking("路径计算失败，请检查网络或输入参数", mSynListener);
		mRouteCalculatorProgressDialog.dismiss();
	}

	/**
	 * 路径规划成功后的回调函数
	 */
	@Override
	public void onCalculateRouteSuccess() {
		dialog.dissmissProgressDialog();
		Intent intent = new Intent(NaviStartActivity.this,
				DriverNaviActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		Bundle bundle = new Bundle();
		bundle.putInt(NaviUtils.ACTIVITYINDEX, NaviUtils.SIMPLEGPSNAVI);
		bundle.putBoolean(NaviUtils.ISEMULATOR, false);
		intent.putExtras(bundle);
		// 跳转到导航界面
		startActivityForResult(intent, 2);
	}

	// --------------------------------点击事件------------------
	@Override
	public void onClick(View v) {
	}

	/**
	 * Activity的入口函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi_start);
		// 初始化坐标数据
		initNaviLatLng();
		// 做路径规划
		planRoute();
	}

	/**
	 * Activity生命周期结束后的方法
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTts != null) {
			mTts.destroy();
		}
		AMapNavi.getInstance(this).removeAMapNaviListener(this);
	}

	@Override
	public void onEndEmulatorNavi() {
		mTts.startSpeaking("导航结束", mSynListener);
	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		mTts.startSpeaking(arg1, mSynListener);
	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
	}

	@Override
	public void onInitNaviFailure() {
	}

	@Override
	public void onInitNaviSuccess() {
	}

	/**
	 * 返回键处理事件
	 *
	 * @param keyCode
	 *            传递的事件代码
	 * @param event
	 *            传递的事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mTts != null) {
				mTts.destroy();
			}
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
	}

	// ---------------------导航View事件回调-----------------------------
	@Override
	public void onNaviCancel() {
		if (mTts != null) {
			mTts.destroy();
		}
	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
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
	public void onReCalculateRouteForTrafficJam() {
		mTts.startSpeaking("前方道路拥挤，路线重新规划", mSynListener);
	}

	@Override
	public void onReCalculateRouteForYaw() {
		mTts.startSpeaking("您已偏离规划路线", mSynListener);
	}

	@Override
	public void onScanViewButtonClick() {
	}

	@Override
	public void onStartNavi(int arg0) {
	}

	@Override
	public void onTrafficStatusUpdate() {
	}

	/**
	 * 开始规划路径的方法
	 */
	private void planRoute() {
		AMapNavi.getInstance(this).calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
	}

}
