package ustc.sse.water.utils.zjx;

import ustc.sse.water.activity.DriverMainScreen;
import ustc.sse.water.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;

/**
 *
 * 自定义对话框. <br>
 * 订单详情对话框.
 * <p>
 * Copyright: Copyright (c) 2015-2-12 上午10:32:03
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
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
	private String adress;// 停车场坐标位置
	private Button callPhone; // 拨号按钮

	public DriverCustomDialog(Context con, String parkNum, String orderInfo,
			String phone, String adress) {
		this.context = con;
		this.adress = adress;
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(con).inflate(
				R.layout.order_custom_dialog, null);
		adb.setView(view);

		parkNumber = (TextView) view
				.findViewById(R.id.text_order_detail_number);
		orderContent = (TextView) view
				.findViewById(R.id.text_order_detail_info);
		parkPhone = (TextView) view.findViewById(R.id.text_order_detail_phone);
		parkNumber.setText(parkNum);
		orderContent.setText(orderInfo);
		parkPhone.setText(phone);

		gpsPark = (Button) view.findViewById(R.id.button_order_detail_gps);
		callPhone = (Button) view.findViewById(R.id.button_order_call_phone);
		gpsPark.setOnClickListener(new GPSClickListener());
		callPhone.setOnClickListener(new CallPhoneListener(phone));

		ad = adb.create();
		ad.show();
	}

	private void addChangeToPoint() {
		if (this.adress != null) {
			String point[] = this.adress.split(",");
			double latitude = Double.parseDouble(point[0]);
			double longitude = Double.parseDouble(point[1]);
			DriverMainScreen.orderLocation = new LatLng(longitude, latitude);
		}
	}

	class GPSClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// 通过停车场的坐标，在地图上定位
			addChangeToPoint();
			if (DriverMainScreen.orderLocation != null) {
				Intent intent = new Intent();
				intent.setClass(context, DriverMainScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				context.startActivity(intent);
			}
		}

	}

	class CallPhoneListener implements View.OnClickListener {

		String phoneNumber = null; // 手机号码

		public CallPhoneListener(String phone) {
			this.phoneNumber = phone;
		}

		@Override
		public void onClick(View v) {
			// 拨号
			if (v.getId() == R.id.button_order_call_phone) {
				Uri uri = Uri.parse("tel:" + phoneNumber);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(uri);
				context.startActivity(intent);
			}
		}
	}

}
