package ustc.sse.water.thread;

import java.net.URLEncoder;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.utils.zjx.HttpUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 修改驾驶员信息线程. <br>
 * 将UpdateDriverInfo传来的参数传到服务器处理修改驾驶员信息的业务.
 * <p>
 * Copyright: Copyright (c) 2015-3-9 下午3:38:47
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class UpdateDriverInfoThread extends Thread {
	Handler h;
	private List<String> messages; // 传递的信息放入集合中
	ObjectMapper objectMapper = new ObjectMapper();
	
	public UpdateDriverInfoThread(Handler h, List<String> mes) {
		this.h = h;
		this.messages = mes;
	}
	
	@Override
	public void run() {
		try {
			String basicpath = "http://";
			StringBuffer path = new StringBuffer(basicpath);
			path.append(HttpUtils.MY_IP)
					.append("/AppServerr/DriverUpdateServlet?messages=")
					.append(URLEncoder.encode(objectMapper.writeValueAsString(messages), "utf-8"));
			// 调用Http
			String jsonString = HttpUtils.getJsonContent(path.toString());
			Message msg = h.obtainMessage();
			msg.arg1 = 4; // 将msg的常量值赋值为4
			Bundle bundle = new Bundle();
			bundle.putString("update_result", jsonString); // 存入服务器返回的结果
			msg.setData(bundle);
			h.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
