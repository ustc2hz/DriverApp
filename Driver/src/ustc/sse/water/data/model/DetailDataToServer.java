package ustc.sse.water.data.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * DetailDataToServer 将停车场收费规则数据发送至服务器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年1月29日 下午2:01:33
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 黄志恒
 * @version 1.0.0
 */
public class DetailDataToServer {

	// 发送的本地服务器的地址
	// public static final String path =
	// "http://192.168.8.199:8080/AppServerr/ServerToYuntu";
	public static final String path = "http://192.168.10.42:8080/Test3_Yuntu//ServerToYuntu";
	public static final String updatePath = "http://192.168.8.199:8080/Test3_Yuntu/ServerUpdateYuntu";
	// 接收传递回来的数据
	public String responseMsg = "";

	/*
	 * 空构造函数
	 */
	public DetailDataToServer() {

	}

	/**
	 * 发送创建表的数据
	 */
	public void postDataToServer(String data) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(path);
		// post.addHeader("Content-Type", "application/x-www-form-urluncoded");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", data));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(entity);

		HttpResponse resp = client.execute(post);

		if (resp.getStatusLine().getStatusCode() == 200) {// 这只是链接成功了，或者说是发送成功了。
			Log.v("status", "success");
			responseMsg = EntityUtils.toString(resp.getEntity());

		}
		if (client != null) {
			client.getConnectionManager().shutdown();
		}

	}

	/**
	 * 发送更新数据
	 */
	public void postUpdateDataToServer(String updateData) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(updatePath);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", updateData));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(entity);

		HttpResponse resp = client.execute(post);

		if (resp.getStatusLine().getStatusCode() == 200) {// 这只是链接成功了，或者说是发送成功了。
			Log.v("status", "success");
			responseMsg = EntityUtils.toString(resp.getEntity());

		}
		if (client != null) {
			client.getConnectionManager().shutdown();
		}

	}
}
