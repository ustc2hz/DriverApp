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
 * @author 
 * @version 1.0.0
 */
public class CustomDialog {
	private Context context; // 上下文
	private AlertDialog ad; // 对话框
	private TextView orderTitle; // 订单标题
	private TextView orderContent; // 订单内容
	private Button cancelOrder; // 取消按钮

	public CustomDialog(Context con, String title, String content, boolean flag) {
		this.context = con;
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		View ll = LayoutInflater.from(con).inflate(
				R.layout.order_custom_dialog, null);
		adb.setView(ll);
		orderTitle = (TextView) ll.findViewById(R.id.order_custom_dialog_title);
		orderContent = (TextView) ll
				.findViewById(R.id.order_custom_dialog_content);
		cancelOrder = (Button) ll.findViewById(R.id.order_custom_dialog_button);
		orderTitle.setText(title);
		orderContent.setText(content);
		if (flag) {
			cancelOrder.setText("已过期");
		} else {
			cancelOrder.setText(R.string.driver_cancel_parking_order);
		}
		ad = adb.create();
		ad.show();
	}

}
