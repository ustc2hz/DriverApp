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
 * DataToYutunServer.将停车场的名称和位置发送到服务器 <br>
 * .
 * <p>
 * Copyright: Copyright (c) 2015年1月27日 下午4:10:44
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒
 * @version 1.0.0
 */
public class DataToYutunServer {
	// 高德云图key
	public static final String key = "fb132e1723bf9e8ec0967391ab6eb6f4";
	// 发送的类型，“2”表示发送名称和地址
	public static final String loctype = "2";
	// 发送的本地服务器的地址
	public static final String path = "http://192.168.64.143:8080/Test3_Yuntu/servlet/ServerToYuntu";
	// 高德指定地图的id
	public static final String tableid = "54c48b0ae4b0ff22e1f393f1";
	// 将要发送给服务器的数据
	public String data;
	// 接收传递回来的数据
	public String responseMsg = "";

	/**
	 * 构造函数，
	 * 
	 * @param data
	 *            要向服务器发送的数据
	 */
	public DataToYutunServer(String data) {
		this.data = data;
	}

	/**
	 * 发送数据
	 */
	public void create() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(path);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("data", this.data));
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
