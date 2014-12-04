package ustc.sse.water.utils.zjx;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * Toast工具类 <br>
 * 定义各种Toast
 * <p>
 * Copyright: Copyright (c) 2014-11-14 上午9:08:59
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ToastUtil {

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            Toast上下文
	 * @param info
	 *            Toast信息
	 */
	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast显示
	 * 
	 * @param context
	 *            上下文
	 * @param info
	 *            Toast的信息
	 */
	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	public ToastUtil() {
		// 无参构造函数
	}

}
