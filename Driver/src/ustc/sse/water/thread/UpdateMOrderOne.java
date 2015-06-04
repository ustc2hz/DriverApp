package ustc.sse.water.thread;

import ustc.sse.water.utils.HttpUtils;
/**
 * 
 * Thread类. <br>
 * 发起网络访问，修改预定的订单状态为1.
 * <p>
 * Copyright: Copyright (c) 2015-5-9 下午4:07:20
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class UpdateMOrderOne extends Thread {
 	String path; // URL地址
	
	public UpdateMOrderOne(String path) {
		this.path = path;
	}
	
	@Override
	public void run() {
		HttpUtils.getJsonContent(path);
	}
	
}
