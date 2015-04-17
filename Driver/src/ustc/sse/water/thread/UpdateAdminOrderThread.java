package ustc.sse.water.thread;

import ustc.sse.water.utils.zjx.HttpUtils;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;

/**
 * 
 * 线程类. <br>
 * 修改管理员订单状态为2.
 * <p>
 * Copyright: Copyright (c) 2015-3-16 下午8:20:05
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class UpdateAdminOrderThread extends Thread {
	private Context context;
	private int orderId; // 修改的订单id
	private StringBuffer path = new StringBuffer(HttpUtils.LBS_SERVER_PATH); // URL
	
	public UpdateAdminOrderThread(int id) {
		this.orderId = id;
		// 完整的URL访问地址
		path.append("/OrderStatusToTwoServlet?orderId=")
				.append(orderId);
	}

	@Override
	public void run() {
		try {
			// 调用Http
			String jsonString = HttpUtils.getJsonContent(path.toString());

			if ("success".equals(jsonString)) {
				ToastUtil.show(context, "订单完成");
			} else {
				ToastUtil.show(context, "出错了，订单无法完成！");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
