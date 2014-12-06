package ustc.sse.water.tools.hzh;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014年12月5日 下午11:27:51
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author ****
 * @version 1.0.0
 */
public class NaviVoice implements AMapNaviListener {

	public static NaviVoice mVoice;
	private static String TAG = "NaviVoice";

	public static NaviVoice getInstance(Context context) {
		if (mVoice == null) {
			mVoice = new NaviVoice(context);
		}
		return mVoice;
	}

	boolean isfinish = true;
	private Context mContext;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 语音合成对象
	private SpeechSynthesizer mTts;

	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				ToastUtil.show(mContext, "初始化失败,错误码：" + code);
			}
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {

		}

		@Override
		public void onCompleted(SpeechError error) {
			isfinish = true;
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}

		@Override
		public void onSpeakBegin() {
			isfinish = false;
		}

		@Override
		public void onSpeakPaused() {

		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {

		}

		@Override
		public void onSpeakResumed() {

		}
	};

	NaviVoice(Context context) {
		mContext = context;
		// mTts = SpeechSynthesizer.createSynthesizer(mContext,
		// mTtsInitListener);
		// 设置参数
		// setParam();
	}

	public void destroy() {
		if (mTts != null) {
			mTts.stopSpeaking();
		}
	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		this.playText("到达目的地");

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		this.playText("路径计算失败，请检查网络或输入参数");
	}

	@Override
	public void onCalculateRouteSuccess() {
		// TODO Auto-generated method stub
		String calculateResult = "路径计算就绪";

		this.playText(calculateResult);

	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub
		this.playText("导航结束");
	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		this.playText(arg1);

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

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		this.playText("前方路线拥堵，路线重新规划");

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub
		this.playText("您已偏航");

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	/**
	 * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
	 * 
	 * @param
	 */
	public void playText(String playText) {
		if (!isfinish) {
			return;
		}
		if (null == mTts) {
			// 创建合成对象.
			mTts = SpeechSynthesizer.createSynthesizer(mContext,
					mTtsInitListener);
			setParam();
		}
		// 进行语音合成.
		mTts.startSpeaking(playText, mTtsListener);

	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private void setParam() {

		// 设置合成
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE,
				"" + mContext.getString(R.string.preference_key_tts_stream));

		// 设置发音人
		mTts.setParameter(SpeechConstant.VOICE_NAME,
				mContext.getString(R.string.preference_default_tts_role));
		// 设置语速
		mTts.setParameter(SpeechConstant.SPEED,
				"" + mContext.getString(R.string.preference_key_tts_speed));
		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME,
				"" + mContext.getString(R.string.preference_key_tts_volume));
		// 设置语调
		mTts.setParameter(SpeechConstant.PITCH,
				"" + mContext.getString(R.string.preference_key_tts_pitch));

	}

	public void startSpeaking() {
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
		isfinish = true;
		setParam();
		/*
		 * int code = mTts.startSpeaking(mContext, mTtsListener); if (code !=
		 * ErrorCode.SUCCESS) { if(code ==
		 * ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){ //未安装则跳转到提示安装页面
		 * mInstaller.install(); }else { showTip("语音合成失败,错误码: " + code); } }
		 */
	}

	public void stopSpeaking() {
		if (mTts != null) {
			mTts.stopSpeaking();
		}
	}

}
