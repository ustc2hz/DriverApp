package ustc.sse.water.utils.zjx;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 
 * 对话框工具类 <br>
 * 显示各种对话框
 * <p>
 * Copyright: Copyright (c) 2014-11-14 上午9:06:52
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DialogUtil {
	/* 上下文 */
	private Context context;
	/* 进度对话框 */
	private ProgressDialog progressDialog = null;

	public DialogUtil() {
		// 无参构造函数
	}

	/**
	 * 有参构造函数
	 * 
	 * @param context
	 *            上下文
	 */
	public DialogUtil(Context context) {
		this.context = context;
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
			progressDialog.setMessage("正在搜索...");
			progressDialog.show();
		}
	}

	/**
	 * 取消进度对话框
	 */
	public void dissmissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

}
