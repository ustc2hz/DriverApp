package ustc.sse.water.service;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.adapter.zjx.OrderStateProcessAdapter;
import ustc.sse.water.data.model.AdminOrderShow;
import ustc.sse.water.data.model.OrderShowList;
import ustc.sse.water.manager.zf.AActivity;
import ustc.sse.water.utils.zjx.ConstantKeep;
import ustc.sse.water.utils.zjx.HttpUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.RemoteViews;

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
 * @version 2.0.0
 */
public class UpdateOrderService extends Service {
	private NotificationManager notificationManager; // 通知管理器
	private NotificationCompat.Builder mBuilder;
	private boolean isStop = false; // 轮询线程的循环标记
	private int managerId; // 登录的管理员id

	@Override
	public IBinder onBind(Intent intent) {
		// 绑定
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		managerId = getSharedPreferences("userdata", MODE_PRIVATE).getInt(
				"adminLoginId", 0);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // 得到通知管理器
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
				StringBuffer path = new StringBuffer(HttpUtils.LBS_SERVER_PATH);
				path.append("/SendClientServlet?adminId=").append(managerId);
				while (!isStop) {
					String jsonString = HttpUtils.getJsonContent(path
							.toString());
					if (!"old".equals(jsonString)) {
						ObjectMapper objectMapper = new ObjectMapper();
						OrderShowList orderShow;

						try {
							orderShow = objectMapper.readValue(jsonString,
									OrderShowList.class);
							ConstantKeep.aos = orderShow.getAdminShow(); // 存入数据结构

							List<String> ids = new ArrayList<String>();
							int notifyNumber = orderShow.getAdminShow().size();
							// 取出每个订单的Id，用来将这些订单的状态改为“1”
							for (int i = 0; i < notifyNumber; i++) {
								ids.add(String.valueOf(orderShow.getAdminShow()
										.get(i).getOrderId()));
								if (ConstantKeep.aosIng == null) {
									ConstantKeep.aosIng = new ArrayList<AdminOrderShow>();
								}
								ConstantKeep.aosIng.add(orderShow
										.getAdminShow().get(i));
							}

							// 再次请求服务器修改订单状态
							StringBuffer requestPath = new StringBuffer(HttpUtils.LBS_SERVER_PATH);
							requestPath.append("/ChangeOrderStatus?changeIds=")
									.append(objectMapper
											.writeValueAsString(ids));
							
							HttpUtils.getJsonContent(requestPath.toString());
							AActivity.myAdapter = new OrderStateProcessAdapter(
									AActivity.context, ConstantKeep.aosIng);
							Message ms = new Message();
							ms.arg1 = 44;
							AActivity.h1.sendMessage(ms);

							sendNotification("主人！又有" + notifyNumber + "个新订单来了！");
						} catch (Exception e) {
							e.printStackTrace();
						}

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

	// 发更新通知
	private void sendNotification(String content) {
		RemoteViews view_custom = new RemoteViews(getPackageName(),
				R.layout.view_notificatinon);
		view_custom.setImageViewResource(R.id.custom_icon, // 设置图标
				R.drawable.lbs_logo);
		view_custom.setTextViewText(R.id.tv_custom_title, "Driver"); // 设置标题
		view_custom.setTextViewText(R.id.tv_custom_content, // 设置内容
				content);
		/* 构建通知 */
		mBuilder = new Builder(this);
		mBuilder.setContent(view_custom).setWhen(System.currentTimeMillis())
				.setTicker("有新订单").setSmallIcon(R.drawable.lbs_logo);
		Notification notify = mBuilder.build();

		notificationManager.notify(1, notify); // 发出通知
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		notificationManager.cancel(1);
	}

	@Override
	public void onDestroy() {
		// Service销毁时，停止线程
		isStop = true;
		super.onDestroy();
	}

}
