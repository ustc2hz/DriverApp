package ustc.sse.water.utils.zjx;

import ustc.sse.water.activity.R;
import android.graphics.Color;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;

/**
 * 
 * ��ʼ����λ���Ե��� <br>
 * ������Ҫ���������޸Ķ�λ�Ļ�����Ϣ���綨λͼ�꣬��λȦ�Ĵ�С����ɫ�ȵ�
 * <p>
 * Copyright: Copyright (c) 2014-11-12 ����8:51:57
 * <p>
 * Company: �й���ѧ������ѧ���ѧԺ
 * <p>
 * 
 * @author �ܾ��� sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyLocationSet {
	/* �������Ķ�λ���� */
	private AMap aMap;

	public MyLocationSet(AMap map) {
		this.aMap = map;
	}

	/**
	 * ����һЩAMap������
	 */
	public void setMapLocation() {
		// �Զ���ϵͳ��λС����
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLUE);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 180));// ����Բ�ε������ɫ
		myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false
	}

}
