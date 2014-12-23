package ustc.sse.water.tools.hzh;

import java.util.ArrayList;
import java.util.List;

import ustc.sse.water.activity.R;

import com.amap.api.cloud.model.CloudItem;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**
 * 服务器图层 <br>
 * 在地图上显示服务器中自定义的位置
 * <p>
 * Copyright: Copyright (c) 2014年12月19日 下午10:27:13
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class PoiOverlay {
	private AMap mAMap;
	private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
	private List<CloudItem> mPois;

	/**
	 * 空构造函数
	 */
	public PoiOverlay() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param amap
	 *            传递进来的地图对象
	 * @param pois
	 *            存数着服务器中节点的List
	 *
	 */
	public PoiOverlay(AMap amap, List<CloudItem> pois) {
		mAMap = amap;
		mPois = pois;
	}

	/**
	 * 将节点添加到地图上的方法
	 */
	public void addToMap() {
		for (int i = 0; i < mPois.size(); i++) {
			Marker marker = mAMap.addMarker(getMarkerOptions(i));
			marker.setObject(i);
			mPoiMarks.add(marker);
		}
	}

	/**
	 * 获取解释
	 */
	protected BitmapDescriptor getBitmapDescriptor(int index) {
		return null;
	}

	/**
	 * 获取范围
	 */
	private LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		for (int i = 0; i < mPois.size(); i++) {
			b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
					mPois.get(i).getLatLonPoint().getLongitude()));
		}
		return b.build();
	}

	/**
	 * 获取merker的设置信息
	 * 
	 * @param index
	 *            在List中的位置
	 */
	private MarkerOptions getMarkerOptions(int index) {
		return new MarkerOptions()
				.position(
						new LatLng(mPois.get(index).getLatLonPoint()
								.getLatitude(), mPois.get(index)
								.getLatLonPoint().getLongitude()))
				.title(getTitle(index)).snippet(getSnippet(index))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon1));
		// BitmapDescriptorFactory.fromResource(R.drawable.point)
	}

	/**
	 * 获取所选marker在List中的位置
	 * 
	 * @param marker
	 *            所选的marker节点
	 */
	public int getPoiIndex(Marker marker) {
		for (int i = 0; i < mPoiMarks.size(); i++) {
			if (mPoiMarks.get(i).equals(marker)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 获取对应的CloudItem对象
	 * 
	 * @param index
	 *            在List中的位置
	 */
	public CloudItem getPoiItem(int index) {
		if (index < 0 || index >= mPois.size()) {
			return null;
		}
		return mPois.get(index);
	}

	/**
	 * 获取对应的Snippet信息
	 * 
	 * @param index
	 *            在List中的位置
	 */
	protected String getSnippet(int index) {
		return mPois.get(index).getSnippet();
	}

	/**
	 * 获取对应的Title信息
	 * 
	 * @param index
	 *            在List中的位置
	 */
	protected String getTitle(int index) {
		return mPois.get(index).getTitle();
	}

	public void removeFromMap() {
		for (Marker mark : mPoiMarks) {
			mark.remove();
		}
	}

	/**
	 * 调整视角缩放
	 *
	 */
	public void zoomToSpan() {
		if (mPois != null && mPois.size() > 0) {
			if (mAMap == null) {
				return;
			}
			LatLngBounds bounds = getLatLngBounds();
			// 20是最高级别
			mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 7));
		}
	}
}
