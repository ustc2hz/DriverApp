package ustc.sse.water.utils.zjx;

import java.security.MessageDigest;
/**
 * 
 * MD5加密类. <br>
 * 包含一个将字符串进行MD5加密的静态方法.
 * <p>
 * Copyright: Copyright (c) 2015-2-12 下午4:14:57
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 张芳
 * @version 1.0.0
 */
public class MD5Util {
	
	/**
	 * MD5单向加密，32位，用于加密密码，因为明文密码在信道中传输不安全，明文保存在本地也不安全
	 * @param str 要加密的字符串
	 * @return String 加密后的字符串
	 */
	public static String md5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}
