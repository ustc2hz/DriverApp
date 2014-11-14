package ustc.sse.water.listener.zjx;

import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;

/**
 * 
 * 定位事件处理类 <br>
 * 自定义按钮实现定位事件处理
 * <p>
 * Copyright: Copyright (c) 2014-11-14 上午8:50:46
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyLocationListener implements OnClickListener {
	/* 接收传递的AMap */
	private AMap aMap;
	/* 接收我的位置 */
	private LatLng myLatlng;

	public MyLocationListener(AMap map, LatLng latlng) {
		this.aMap = map;
		this.myLatlng = latlng;
	}

	/**
	 * 地图跳转到我的位置
	 */
	@Override
	public void onClick(View v) {
		aMap.animateCamera(new CameraUpdateFactory()
				.newLatLngZoom(myLatlng, 16));
	}
}
