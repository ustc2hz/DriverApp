package ustc.sse.water.utils;

/**
 * 
 * 验证工具类. <br>
 * 包含了针对不同输入的验证规则判断.
 * <p>
 * Copyright: Copyright (c) 2015-3-17 下午3:37:11
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class ValidatorUtils {

	/**
	 * 静态方法；对驾驶员的车牌号的验证； 规则：首汉字+大写字母+数字和大写字母组合
	 * 
	 * @param licence
	 *            车牌号
	 * @return boolean
	 */
	public static boolean licenceValidator(String licence) {
		boolean flag = false;
		// 一般车牌号的正则表单式
		String regex = "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$";
		if (licence.matches(regex)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 静态方法；对电话号码的验证； 规则：手机号码：1开头，11位数
	 * 
	 * @param phone
	 *            手机号
	 * @return boolean
	 */
	public static boolean phoneValidator(String phone) {
		boolean flag = false;

		// 由于手机号码种类比较多，这里简单验证
		String regex = "^[1]{1}[0-9]{10}$";
		if (phone.matches(regex)) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 静态方法，对密码的验证 规则：只能由大小写字母和数字组成，6到12位
	 * 
	 * @param password
	 *            密码
	 * @return boolean
	 */
	public static boolean passwordValidator(String password) {
		boolean flag = false;
		// 6到12位密码
		String regex = "^[a-zA-Z0-9]{6,12}$";
		if (password.matches(regex)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 静态方法；对普通用户名的验证 规则：只能有大小写字母和数字组成，6到16位
	 * 
	 * @param name
	 *            用户名
	 * @return boolean
	 */
	public static boolean nameValidator(String name) {
		boolean flag = false;
		// 6到12位用户名
		String regex = "^[a-zA-Z0-9]{6,16}$";
		if (name.matches(regex)) {
			flag = true;
		}
		return flag;
	}

}
