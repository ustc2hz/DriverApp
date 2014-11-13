package ustc.sse.water.activity;

import ustc.sse.water.utils.zjx.MyLocationSet;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;

/**
 * 
 * DriverӦ�õĵ�ͼ��ʾ���� <br>
 * ����Ϊ��ʾ�ߵµ�ͼ��Activity�� ��Ϊ���������������в���ͼ���ײ������������� �ڵ�ͼ������Ĭ�϶�λ�����ҵ�λ�á�����Ĭ����ʾ��Χ��ͣ����
 * <p>
 * Copyright: Copyright (c) 2014-11-12 ����2:40:50
 * <p>
 * Company: �й���ѧ������ѧ���ѧԺ
 * <p>
 * 
 * @author �ܾ��� sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DriverMainScreen extends Activity implements LocationSource,
		AMapLocationListener, OnClickListener {
	/* ����AMap */
	private AMap aMap;
	/* ����������ʾ��MapView */
	private MapView mapView;
	/* �������õ�ͼ */
	private UiSettings uiSettings;
	/* ��������λ */
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	/* ��λ��ť */
	private ImageButton myLocation;
	/* �ҵ�λ�� */
	private LatLng myLatlng;

	/**
	 * ������дonCreate����
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_main);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		initMap();
		initViews();
	}

	/**
	 * ��ʼ����ͼ����ʾ��Ϣ
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			uiSettings = aMap.getUiSettings();
			// ���ߵµ�ͼ��Logo����Ϊ�ײ�����
			uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
			uiSettings.setMyLocationButtonEnabled(false); // ����ʵϵͳ�Դ��Ķ�λ��ť
			aMap.setLocationSource(this);// ���ö�λ����
			aMap.moveCamera(CameraUpdateFactory.zoomTo(16));// �ض������ż���
			new MyLocationSet(aMap).setMapLocation(); // ���÷��������ö�λ����
		}
	}

	/**
	 * ��ʼ��Ui�ؼ�
	 */
	private void initViews() {
		myLocation = (ImageButton) findViewById(R.id.button_my_location);
		myLocation.setOnClickListener(this);
	}

	/**
	 * ������дonResume
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ������дonPause
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * ������дonSaveInstanceState
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ��������onDestroy
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * ��λ�ɹ���ص��ú���
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// ��ʾϵͳС����
			myLatlng = new LatLng(aLocation.getAltitude(),
					aLocation.getLongitude());// �ҵ�λ��
		}
	}

	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * ֹͣ��λ
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onClick(View v) {
		aMap.animateCamera(new CameraUpdateFactory().changeLatLng(myLatlng));
		// aMap.setMyLocationEnabled(true);
	}
}
