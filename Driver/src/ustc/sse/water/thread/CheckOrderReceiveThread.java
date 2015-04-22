package ustc.sse.water.thread;

import ustc.sse.water.utils.HttpUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 驾驶员端确认管理员接收到了订单的线程. <br>
 * 此线程在驾驶员提交订单3s后启动，访问服务器判断管理员是否已经接收到订单.
 * <p>
 * Copyright: Copyright (c) 2015-1-31 下午11:28:59
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class CheckOrderReceiveThread extends Thread {
	Handler h; // Handler处理线程访问服务器处理的结果
	private String orderUUID; // 订单的uuid
	private int managerId; // 预定的停车场管理员id
	private String valNumber; // 预定数

	public CheckOrderReceiveThread(Handler h, String uuid, int id, String num) {
		this.h = h;
		this.orderUUID = uuid;
		this.managerId = id;
		this.valNumber = num;
	}

	@Override
	public void run() {
		// 访问服务器，查看管理员是否接收到了订单信息
		try {
			StringBuffer path = new StringBuffer(HttpUtils.LBS_SERVER_PATH);
			path.append("/CheckSendServlet?uuid=").append(orderUUID)
					.append("&managerId=").append(managerId)
					.append("&bookNum=").append(valNumber);
			sleep(5000); // 休眠5s
			// 调用Http
			String jsonString = HttpUtils.getJsonContent(path.toString());
			Message msg = h.obtainMessage();
			msg.arg1 = 2; // 将msg的常量值赋值为2
			Bundle bundle = new Bundle();
			bundle.putString("check_result", jsonString); // 存入服务器返回的结果
			msg.setData(bundle);
			h.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
