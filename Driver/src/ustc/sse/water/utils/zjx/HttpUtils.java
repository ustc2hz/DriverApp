package ustc.sse.water.utils.zjx;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * 
 * Http工具类. <br>
 * 此类中给出服务器的IP和端口号；提供HttpURLConnection的方式访问服务器；接收服务器返回的数据.
 * <p>
 * Copyright: Copyright (c) 2015-1-31 下午2:05:58
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class HttpUtils {
	/* 服务器的IP地址和端口号 */
	public final static String MY_IP = "192.168.9.178:8080";

	public HttpUtils() {
		// 无参构造函数
	}

	/**
	 * 通过URL访问服务器，Get方式
	 * 
	 * @param path
	 *            url
	 * @return String 服务器返回的结果
	 */
	public static String getJsonContent(String path) {
		try {
			URL url = new URL(path); // 创建一个url
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection(); // 打开连接
			httpURLConnection.setConnectTimeout(3000); // 连接超时为3s
			httpURLConnection.setRequestProperty("encoding", "UTF-8"); // utf-8编码格式
			httpURLConnection.setRequestProperty("Charset", "UTF-8"); // utf-8字符集
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("GET"); // Get方式访问
			httpURLConnection.setDoInput(true);
			int code = httpURLConnection.getResponseCode(); // 连接结果号
			Log.i("code", "-->>" + code);
			if (code == 200) { // 200为连接成功
				// 调用方法将返回的流解析为String，并且返回
				return changeInputStream(httpURLConnection.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 将服务器返回的流转化为String
	 * 
	 * @param inputStream
	 *            输入流
	 * @return String 转化结果
	 */
	public static String changeInputStream(InputStream inputStream) {
		String jsonString = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] data = new byte[1024]; // 缓存
		try {
			while ((len = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, len); // 写入输出流
			}
			// 输出流转化为String
			jsonString = new String(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}

}