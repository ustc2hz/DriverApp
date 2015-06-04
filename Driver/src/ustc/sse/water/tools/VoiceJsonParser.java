package ustc.sse.water.tools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 
 * 语音识别Json解析类. <br>
 * 语音识别成功后得到的是Json封装的数据，需要使用Json解析. 将语音识别后的结果解析成完整的汉字字符串----参考自讯飞语言API
 * <p>
 * Copyright: Copyright (c) 2014-11-21 下午5:22:39
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class VoiceJsonParser {

	/**
	 * Json解析方法，解析语音识别结果字符串（此方法由讯飞官方提供）
	 * 
	 * @param json
	 *            语音识别后的结果字符串（json封装的）
	 * @return 完整的汉字字符串（正在的语音输入内容）
	 */
	public static String parseIatResult(String json) {
		StringBuffer ret = new StringBuffer(); // 字符串缓存
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);
			// 语音识别返回的字符串json结构为：ws，cw，w
			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				// 因为包含“。”，所以判断是否到达“。”，只需要“。”之前的汉字
				if (obj.getString("w").equals("。")) {
					break; // 直接退出循环
				}
				// 不是“。”时，添加到返回字符串中
				ret.append(obj.getString("w"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}

}
