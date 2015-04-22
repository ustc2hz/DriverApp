package ustc.sse.water.utils;

import ustc.sse.water.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 *
 * 自定义对话框. <br>
 * 管理员订单详情对话框.
 * <p>
 * Copyright: Copyright (c) 2015-3-16 上午10:32:03
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 周晶鑫
 * @version 1.0.0
 */
public class AdminCustomDialog {
	private Context context; // 上下文
	private AlertDialog ad; // 对话框
	private TextView orderContent; // 订单内容
	private TextView driverPhone; // 管理员电话

	public AdminCustomDialog(Context con, String orderInfo, String phone) {
		this.context = con;
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		View ll = LayoutInflater.from(con).inflate(R.layout.admin_order_dialog,
				null);
		adb.setView(ll);

		orderContent = (TextView) ll
				.findViewById(R.id.text_admin_order_show_detail);
		driverPhone = (TextView) ll
				.findViewById(R.id.text_admin_order_show_phone);
		orderContent.setText(orderInfo);
		driverPhone.setText(phone);

		ad = adb.create();
		ad.show();
	}

}
