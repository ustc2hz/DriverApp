package ustc.sse.water.utils.zjx;

import com.amap.api.cloud.model.LatLonPoint;
import com.amap.api.maps.model.LatLng;
/**
 * 
 * 高德地图工具类. <br>
 * 其中的方法主要是对坐标点的操作.
 * <p>
 * Copyright: Copyright (c) 2015-2-12 上午10:29:21
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 黄志恒
 * @version 1.0.0
 */
public class AMapUtil {

	/**
	 * 将LatLonPoint转化为LatLng
	 * @param latLonPoint
	 * @return Latlng
	 */
	public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
		return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
	}

	/**
	 * 将LatLng转化为LatLonPoint
	 * @param latlon
	 * @return
	 */
	public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
		return new LatLonPoint(latlon.latitude, latlon.longitude);
	}

	/**
	 * 判断是否为空字符串
	 * @param s
	 * @return
	 */
	public static boolean isEmptyOrNullString(String s) {
		return (s == null) || (s.trim().length() == 0);
	}
}
