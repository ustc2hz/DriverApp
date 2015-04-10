package ustc.sse.water.thread;

import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.data.model.OrderShowList;
import ustc.sse.water.utils.zjx.ConstantKeep;
import ustc.sse.water.utils.zjx.HttpUtils;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 线程类. <br>
 * 访问服务器，获取管理员的正在进行的订单.
 * <p>
 * Copyright: Copyright (c) 2015-3-16 下午4:36:41
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class ShowAdminOrderThread extends Thread {
	private Handler h; // 消息处理
	ObjectMapper objectMapper = new ObjectMapper(); // 解析字符串
	private StringBuffer path = new StringBuffer("http://"); // URL
	
	public ShowAdminOrderThread(Handler h,String type, String adminId) {
		this.h = h;
		// 完整的URL访问地址
		path.append(HttpUtils.MY_IP)
		.append("/AppServerr/AdminOrderServlet?type=").append(type)
		.append("&adminId=").append(adminId);
	}
	
	@Override
	public void run() {
		try {
			// 调用Http
			String jsonString = HttpUtils.getJsonContent(path.toString());
			Message msg = h.obtainMessage();
			
			if("fail".equals(jsonString)) {// 获取失败
				msg.arg1 = 55; // 将msg的常量值赋值为22
			} else if("empty".equals(jsonString)) { 
				msg.arg1 = 66; // 空
			} else {
				// 说明获取订单数据成功
				OrderShowList osl = objectMapper.readValue(jsonString, OrderShowList.class);
				int status = osl.getAdminShow().get(0).getOrderStatus();
				if(status == 1) { // 根据订单判断放入哪个变量中
					ConstantKeep.aosIng = osl.getAdminShow(); // 赋值到全局变量中	
				} else {
					ConstantKeep.aosDown = osl.getAdminShow(); // 赋值到全局变量中
				}
				msg.arg1 = 44; // 将msg的常量值赋值为22
			}
			
			h.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
