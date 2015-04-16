package ustc.sse.water.utils.zjx;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 
 * 对话框工具类 <br>
 * 显示进度条对话框
 * <p>
 * Copyright: Copyright (c) 2014-11-14 上午9:06:52
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.0.0
 */
public class ProgressDialogUtil {
	/* 上下文 */
	private Context context;
	/* 进度对话框 */
	private ProgressDialog progressDialog = null;
	private String message; // 提示的信息

	/**
	 * 有参构造函数
	 * 
	 * @param context
	 *            上下文
	 */
	public ProgressDialogUtil(Context context, String msg) {
		this.context = context;
		message = msg;
	}

	/**
	 * 取消进度对话框
	 */
	public void dissmissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 显示进度对话框
	 */
	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.setMessage(message);
			progressDialog.show();
		}
	}

}
