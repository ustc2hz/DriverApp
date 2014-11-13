package ustc.sse.water.utils.zjx;

import ustc.sse.water.activity.R;
import android.graphics.Color;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;

/**
 * 
 * 初始化定位属性的类 <br>
 * 此类主要方法用来修改定位的基本信息，如定位图标，定位圈的大小，颜色等等
 * <p>
 * Copyright: Copyright (c) 2014-11-12 下午8:51:57
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyLocationSet {
	/* 用来更改定位属性 */
	private AMap aMap;

	public MyLocationSet(AMap map) {
		this.aMap = map;
	}

	/**
	 * 设置一些AMap的属性
	 */
	public void setMapLocation() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLUE);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 180));// 设置圆形的填充颜色
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	}

}
