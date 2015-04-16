package ustc.sse.water.utils.zjx;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.provider.Settings;

/**
 * 
 * 检查网络连接类. <br>
 * 静态方法，检查手机网络是否打开.
 * <p>
 * Copyright: Copyright (c) 2015-3-9 下午8:40:32
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳
 * @version 1.0.0
 */
public class CheckNetwork {
	private Context context; // 上下文

	public CheckNetwork(Context con) {
		this.context = con;
	}

	/**
	 * 检查网络连接状态，看是否有网络连接
	 */
	public void checkNetworkState() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			return;
		}
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return;
		}
		showTips();
	}

	/**
	 * 如果没有网络连接，则提示对话框
	 */
	private void showTips() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("没有可用网络");
		builder.setMessage("当前网络不可用，是否设置网络？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 如果没有网络连接，则进入网络设置界面
				context.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create();
		builder.show();
	}

}
