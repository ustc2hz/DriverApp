package ustc.sse.water.data.model;

import com.amap.api.maps.model.Marker;

/**
 * 标记点的距离类. <br>
 * 标记点的距离.
 * <p>
 * Copyright: Copyright (c) 2015年4月6日 下午9:27:40
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MarkerDistance {

	public Marker marker; // Marker
	public float distance; // 距离

	public MarkerDistance(Marker marker, float distance) {
		this.marker = marker;
		this.distance = distance;
	}
}
