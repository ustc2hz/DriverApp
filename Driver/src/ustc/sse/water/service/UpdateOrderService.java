package ustc.sse.water.service;

import ustc.sse.water.utils.zjx.HttpUtils;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * 管理员刷新订单的Service. <br>
 * 此Service采用轮询的方式访问服务器，查看管理员是否有新订单。如果有则从服务器中获取后广播给订单管理的Activity.
 * <p>
 * Copyright: Copyright (c) 2015-1-31 下午10:34:20
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class UpdateOrderService extends Service {
	boolean isStop = false; // 轮询线程的循环标记

	@Override
	public IBinder onBind(Intent intent) {
		// 绑定
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		createThread(); // Service启动时，开启轮询线程
	}

	/**
	 * 创建一个轮询线程，每2s访问一次服务器查看是否有新的订单信息
	 */
	public void createThread() {
		// 启动一个线程来每2s发送一个服务器请求
		new Thread() {
			@Override
			public void run() {
				String basicpath = "http://";
				StringBuffer path = new StringBuffer(basicpath);
				path.append(HttpUtils.MY_IP).append(
						"/TestOrderServer/SendClientServlet");
				while (!isStop) {
					String jsonString = HttpUtils.getJsonContent(path
							.toString());
					if (!"old".equals(jsonString)) {
						// 如果是新数据就更新，发送广播
						Intent i = new Intent();
						i.setAction("com.action.br.update_order");
						i.putExtra("newInfo", jsonString);
						sendBroadcast(i);
					}
					try { // 每2s发一次请求
						sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public void onDestroy() {
		// Service销毁时，停止线程
		isStop = true;
		super.onDestroy();
	}

}
