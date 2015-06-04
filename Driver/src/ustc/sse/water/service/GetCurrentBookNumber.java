package ustc.sse.water.service;

import ustc.sse.water.utils.HttpUtils;
import android.app.IntentService;
import android.content.Intent;

/**
 * 
 * Service. <br>
 * 更新停车场剩余车位数.
 * <p>
 * Copyright: Copyright (c) 2015-4-3 上午9:58:09
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class GetCurrentBookNumber extends IntentService {

	public GetCurrentBookNumber() {
		super("GetCurrentBookNumber");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// 获取该停车场的剩余可预订车位数量
		String managerId = intent.getStringExtra("parking_managerId");
		if (Integer.parseInt(managerId) != 0) {
			// 访问服务器
			StringBuffer path = new StringBuffer(HttpUtils.LBS_SERVER_PATH)
					.append("/GetParkingCurrentNumber?managerId=").append(
							managerId);
			String result = HttpUtils.getJsonContent(path.toString());
			Intent i = new Intent();
			i.setAction("com.action.br.current_number");
			i.putExtra("current_parking_number", result);
			sendBroadcast(i);
		}

	}

}
