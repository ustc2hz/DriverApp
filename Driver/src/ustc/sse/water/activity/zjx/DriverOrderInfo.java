package ustc.sse.water.activity.zjx;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * 驾驶员的订单信息类. <br>
 * 展示驾驶员的正在进行的订单和历史订单.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:38:10
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DriverOrderInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_order_info);
	}
}
