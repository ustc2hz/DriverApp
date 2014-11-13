package ustc.sse.water.listener.zjx;

import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;

/**
 * 
 * 定位按钮事件处理类 <br>
 * 处理定位按钮的事件
 * <p>
 * Copyright: Copyright (c) 2014-11-12 下午8:54:05
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyLocationListener implements OnClickListener {
	/* 处理事件的地图 */
	private AMap aMap;
	/* 我的位置的地理坐标 */
	private LatLng myLatlng;

	public MyLocationListener(AMap map, LatLng latlng) {
		this.aMap = map;
		this.myLatlng = latlng;
	}

	/**
	 * 当点击定位按钮时，地图跳转到“我的位置”
	 */
	@Override
	public void onClick(View v) {
		aMap.animateCamera(new CameraUpdateFactory()
				.newLatLngZoom(myLatlng, 16));
	}
}
