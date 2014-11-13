package ustc.sse.water.listener.zjx;

import android.view.View;
import android.view.View.OnClickListener;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;

/**
 * 
 * ��λ��ť�¼������� <br>
 * ����λ��ť���¼�
 * <p>
 * Copyright: Copyright (c) 2014-11-12 ����8:54:05
 * <p>
 * Company: �й���ѧ������ѧ���ѧԺ
 * <p>
 * 
 * @author �ܾ��� sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyLocationListener implements OnClickListener {
	/* �����¼��ĵ�ͼ */
	private AMap aMap;
	/* �ҵ�λ�õĵ������� */
	private LatLng myLatlng;

	public MyLocationListener(AMap map, LatLng latlng) {
		this.aMap = map;
		this.myLatlng = latlng;
	}

	/**
	 * �������λ��ťʱ����ͼ��ת�����ҵ�λ�á�
	 */
	@Override
	public void onClick(View v) {
		aMap.animateCamera(new CameraUpdateFactory()
				.newLatLngZoom(myLatlng, 16));
	}
}
