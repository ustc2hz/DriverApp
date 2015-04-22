package ustc.sse.water.thread;

import java.net.URLEncoder;

import ustc.sse.water.utils.HttpUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 发送订单的线程类. <br>
 * 此线程向服务器发送停车场的订单.
 * <p>
 * Copyright: Copyright (c) 2015-2-1 上午12:01:42
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class SendOrderThread extends Thread {
	Handler h; // Handler处理服务器返回的值
	private String sendMessage; // 需要发送的信息

	public SendOrderThread(Handler h, String content) {
		this.h = h;
		this.sendMessage = content;
	}

	@Override
	public void run() {
		// 使用http的url访问服务器
		try {
			// Get方式的url地址
			StringBuffer path = new StringBuffer(HttpUtils.LBS_SERVER_PATH);
			path.append("/SolveOrderServlet?order=")
					.append(URLEncoder.encode(sendMessage, "utf-8"));
			// 调用Http方法
			String jsonString = HttpUtils.getJsonContent(path.toString());
			Message msg = h.obtainMessage();
			msg.arg1 = 1; // 发送完毕后将msg的常量值赋值为1
			Bundle bundle = new Bundle();
			bundle.putString("send_result", jsonString); // 存入服务器返回的结果
			msg.setData(bundle);
			h.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
