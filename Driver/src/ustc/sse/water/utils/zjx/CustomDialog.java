package ustc.sse.water.utils.zjx;

import ustc.sse.water.activity.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog {
	private Context context;
	private AlertDialog ad;
	private TextView orderTitle;
	private TextView orderContent;
	private Button cancelOrder;

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
