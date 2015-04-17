package ustc.sse.water.thread;

import ustc.sse.water.utils.zjx.HttpUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * 线程类. <br>
 * 修改管理员的密码.
 * <p>
 * Copyright: Copyright (c) 2015-3-16 下午8:28:06
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.2.0
 */
public class UpdateAdminInfoThread extends Thread {

	private Handler h;
	private int adminId = 0; // 默认admin的id为0，表示错误管理员
	private String oldPwd = null; // 旧密码
	private String newPwd = null; // 新密码
	private StringBuffer path = new StringBuffer(HttpUtils.LBS_SERVER_PATH); // URL
	
	public UpdateAdminInfoThread(Handler h, int adminId, String oldPwd, String newPwd) {
		this.h = h;
		this.adminId = adminId;
		this.oldPwd = oldPwd;
		this.newPwd = newPwd;
	}

	@Override
	public void run() {
		if (adminId != 0) {
			// 完整的URL访问地址
			path.append("/AdminUpdateServlet?adminId=")
					.append(adminId).append("&oldPwd=").append(oldPwd)
					.append("&newPwd=").append(newPwd);
			try {
				// 调用Http
				String jsonString = HttpUtils.getJsonContent(path.toString());
				Message msg = h.obtainMessage();
				msg.arg1 = 77; // 将msg的常量值赋值为77
				Bundle bundle = new Bundle();
				bundle.putString("update_result", jsonString); // 存入服务器返回的结果
				msg.setData(bundle);
				h.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
