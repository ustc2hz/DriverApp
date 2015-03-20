package ustc.sse.water.utils.zjx;

import ustc.sse.water.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 
 * 自定义对话框. <br>
 * 订单详情对话框.
 * <p>
 * Copyright: Copyright (c) 2015-2-12 上午10:32:03
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class DriverCustomDialog {
	private Context context; // 上下文
	private AlertDialog ad; // 对话框
	private TextView parkNumber; // 预定车位数
	private TextView orderContent; // 订单内容
	private TextView parkPhone; // 管理员电话
	private Button gpsPark; // 定位按钮

	public DriverCustomDialog(Context con, String parkNum, String orderInfo, String phone) {
		this.context = con;
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		View ll = LayoutInflater.from(con).inflate(
				R.layout.order_custom_dialog, null);
		adb.setView(ll);
		
		parkNumber = (TextView) ll.findViewById(R.id.text_order_detail_number);
		orderContent = (TextView) ll.findViewById(R.id.text_order_detail_info);
		parkPhone = (TextView) ll.findViewById(R.id.text_order_detail_phone);
		parkNumber.setText(parkNum);
		orderContent.setText(orderInfo);
		parkPhone.setText(phone);
		
		gpsPark = (Button) ll.findViewById(R.id.button_order_detail_gps);
		gpsPark.setOnClickListener(new GPSClickListener());
		
		ad = adb.create();
		ad.show();
	}
	
	class GPSClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// 通过停车场的坐标，在地图上定位
			
		}
		
	}

}
