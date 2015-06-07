package ustc.sse.water.utils;

import java.security.MessageDigest;
/**
 * 
 * MD5工具类. <br>
 * 包含MD5加密和解密.
 * <p>
 * Copyright: Copyright (c) 2015-6-5 下午2:46:12
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class MD5Utils {

	/***
	 * MD5加码 生成32位md5码
	 * @param inStr 待处理字符串
	 * @return String MD5加码后的字符串
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		
		return hexValue.toString();
	}

}
