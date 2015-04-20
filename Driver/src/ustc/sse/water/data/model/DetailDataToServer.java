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

import ustc.sse.water.utils.zjx.HttpUtils;

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
	public static final String path = HttpUtils.LBS_SERVER_PATH
			+ "/ServerToYuntu";
	public static final String updatePath = HttpUtils.LBS_SERVER_PATH
			+ "/ServerUpdateYuntu";
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
	public void postDataToServer(String data, String name) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(path);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", data));
		params.add(new BasicNameValuePair("name", name));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(entity);

		HttpResponse resp = client.execute(post);

		if (resp.getStatusLine().getStatusCode() == 200) {// 这只是链接成功了，或者说是发送成功了。
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
			responseMsg = EntityUtils.toString(resp.getEntity());

		}
		if (client != null) {
			client.getConnectionManager().shutdown();
		}

	}
}
