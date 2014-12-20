package ustc.sse.water.tools.zjx;

import ustc.sse.water.json.zjx.VoiceJsonParser;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;

import com.amap.api.maps.AMap;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * 
 * 语音搜索类. <br>
 * 调用讯飞语音进行语音识别，并利用识别结果来进行Poi搜索.
 * <p>
 * Copyright: Copyright (c) 2014-11-21 下午5:02:53
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 **/
public class VoiceSearch {
	/* 接收传递的地图 */
	private AMap aMap;
	/* 上下文 */
	private Context context;
	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				ToastUtil.show(context, "初始化失败,错误码：" + code);
			}
		}
	};

	/**
	 * 语音识别对话框监听器
	 */
	RecognizerDialogListener rdlistener = new RecognizerDialogListener() {

		/**
		 * 错误回调
		 */

		@Override
		public void onError(SpeechError error) {

			ToastUtil.show(context, error.getPlainDescription(true));
		}

		/**
		 * 语音识别结果回调方法 注意：result为json字符串，需要进行解析
		 */

		@Override
		public void onResult(RecognizerResult result, boolean arg1) {
			// 取出语音识别结果，先将其转化为汉字字符串
			voiceResult = VoiceJsonParser.parseIatResult(result
					.getResultString());
			ToastUtil.show(context, "语音识别：" + voiceResult);
			if (!"".equals(voiceResult)) {
				new PoiSearchMethod(aMap, context, voiceResult);
			} else {
				ToastUtil.show(context, "无法识别，请重说！");
			}
		}
	};

	/* 语音识别结果 */
	private String voiceResult = "";

	/**
	 * 有参构造函数
	 * 
	 * @param map
	 *            高德地图
	 * @param con
	 *            上下文
	 */
	public VoiceSearch(AMap map, Context con) {
		this.aMap = map;
		this.context = con;
		// 用户语音配置对象，讯飞appid，这样才可以使用讯飞语音服务
		SpeechUtility
				.createUtility(context, SpeechConstant.APPID + "=546ee54e");
	}

	/**
	 * 调出讯飞语音，语音识别，并进行Poi搜索
	 */
	public void voicePoiSearch() {
		// 调出语音对话框
		RecognizerDialog isrDialog = new RecognizerDialog(context,
				mInitListener);
		isrDialog.setListener(rdlistener);
		isrDialog.show();
	}

}
