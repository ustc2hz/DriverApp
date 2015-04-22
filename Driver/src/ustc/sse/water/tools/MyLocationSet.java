package ustc.sse.water.tools;

import ustc.sse.water.activity.R;
import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

/**
 *
 * 定位类 <br>
 * 设置一些定位信息
 * <p>
 * Copyright: Copyright (c) 2014-11-14 上午8:52:28
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyLocationSet {
	/* 接收传递的AMap */
	private AMap aMap;

	public MyLocationSet(AMap map) {
		this.aMap = map;
	}

	/**
	 * 初始化定位信息
	 */
	public void setMapLocation() {
		// 将屏幕上的Marker清空——黄志恒
		aMap.clear();
		// 设置定位样式
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 更改定位小蓝点
		myLocationStyle.strokeColor(Color.BLUE);// 范围圈边框的颜色
		myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 180));// 范围圈的颜色
		myLocationStyle.strokeWidth(1.0f);// 圆形的边框宽带
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.getUiSettings().setMyLocationButtonEnabled(false);
		aMap.setMyLocationEnabled(true);// 显示定位层，触发定位

	}

}
