package ustc.sse.water.thread;

import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.data.OrderShowList;
import ustc.sse.water.utils.ConstantKeep;
import ustc.sse.water.utils.HttpUtils;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 线程类. <br>
 * 刷新驾驶员的订单数据
 * <p>
 * Copyright: Copyright (c) 2015-3-12 下午6:42:32
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class RefreshDriverOrdersThread extends Thread {
	private Handler h; // 消息处理
	ObjectMapper objectMapper = new ObjectMapper(); // 解析字符串
	private StringBuffer path = new StringBuffer(HttpUtils.LBS_SERVER_PATH); // URL
	
	public RefreshDriverOrdersThread(Handler h,String type,String driverId) {
		this.h = h;
		// 完整的URL访问地址
		path.append("/DriverOrderServlet?type=").append(type)
				.append("&driverId=").append(driverId);
	}

	@Override
	public void run() {
		try {
			// 调用Http
			String jsonString = HttpUtils.getJsonContent(path.toString());
			Message msg = h.obtainMessage();

			if (!"fail".equals(jsonString)) {
				// 说明获取订单数据成功
				OrderShowList osl = objectMapper.readValue(jsonString,
						OrderShowList.class);
				ConstantKeep.dos = osl.getDriverShow(); // 赋值到全局变量中
				msg.arg1 = 11; // 将msg的常量值赋值为11
			} else { // 获取失败
				msg.arg1 = 33; // 将msg的常量值赋值为33
			}

			h.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
