package ustc.sse.water.tools.hzh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.zjx.AMapUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;
import android.util.Log;

import com.amap.api.cloud.model.AMapCloudException;
import com.amap.api.cloud.model.CloudItem;
import com.amap.api.cloud.model.CloudItemDetail;
import com.amap.api.cloud.model.LatLonPoint;
import com.amap.api.cloud.search.CloudResult;
import com.amap.api.cloud.search.CloudSearch;
import com.amap.api.cloud.search.CloudSearch.OnCloudSearchListener;
import com.amap.api.cloud.search.CloudSearch.SearchBound;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**
 * 显示自定义的停车场位置 <br>
 * 显示服务器中自定义的位置
 * <p>
 * Copyright: Copyright (c) 2014年12月19日 下午10:27:13
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class MyCloudSearch implements OnCloudSearchListener {
	public static List<CloudItem> mCloudItems;// 存储从服务器中获取的点
	public static CloudSearch mCloudSearch;// 云搜索对象
	public static PoiOverlay mPoiCloudOverlay;// 显示云搜索的图层
	public static String mTableID = "549050e6e4b0d2863fd1dba6";// 服务器中的表名
	private Context con;// 上下文对象
	public ArrayList<CloudItem> items = new ArrayList<CloudItem>();// 存储从服务器中获取的节点
	private AMap mAMap;// 地图对象
	private LatLonPoint mCenterPoint;// 中心点，即手机的当前所处位置
	private Marker mCloudIDMarer;// 显示从服务器中获取的点的标记
	private String mKeyWord = "";// 搜索的关键字，可以缺省
	private CloudSearch.Query mQuery;// 云搜索的query对象
	private String TAG = "DriverYunTu";// 标签，不在前端显示

	/**
	 * 空构造函数
	 */
	public MyCloudSearch() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param con
	 *            传递来的上下文
	 * @param x
	 *            当前位置的纬度
	 * @param y
	 *            当前位置的经度
	 * @param mAMap
	 *            传递进来的地图对象
	 */
	public MyCloudSearch(Context con, double x, double y, AMap mAMap) {
		this.con = con;
		this.mCenterPoint = new LatLonPoint(x, y);
		this.mAMap = mAMap;
		init();
		searchByBound();
	}

	/**
	 * 类初始化方法
	 */
	private void init() {
		mCloudSearch = new CloudSearch(con);
		mCloudSearch.setOnCloudSearchListener(this);

		// mPoiCloudOverlay = new PoiOverlay(this.mAMap, mCloudItems);
	}

	/**
	 * OnCloudSearchListener的回调函数，用来搜索服务器的具体节点信息
	 */
	@Override
	public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
		// TODO Auto-generated method stub

		if (rCode == 0 && item != null) {
			if (mCloudIDMarer != null) {
				mCloudIDMarer.destroy();
			}
			// mAMap.clear();
			// 将坐标点转换为LatLng格式
			LatLng position = AMapUtil.convertToLatLng(item.getLatLonPoint());

			/*
			 * mAMap.animateCamera(CameraUpdateFactory .newCameraPosition(new
			 * CameraPosition(position, 18, 0, 30)));
			 */

			// 向地图添加服务器的节点
			mCloudIDMarer = mAMap
					.addMarker(new MarkerOptions()
							.position(position)
							.title(item.getTitle())
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
			// BitmapDescriptorFactory.fromResource(R.drawable.point)
			items.add(item);
			Log.d(TAG, "_id" + item.getID());
			Log.d(TAG, "_location" + item.getLatLonPoint().toString());
			Log.d(TAG, "_name" + item.getTitle());
			Log.d(TAG, "_address" + item.getSnippet());
			Log.d(TAG, "_caretetime" + item.getCreatetime());
			Log.d(TAG, "_updatetime" + item.getUpdatetime());
			Log.d(TAG, "_distance" + item.getDistance());
			Iterator iter = item.getCustomfield().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				Log.d(TAG, key + "   " + val);
			}
		}

	}

	/**
	 * OnCloudSearchListener的回调函数，用来搜索服务器的节点信息
	 */
	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		// TODO Auto-generated method stub

		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(mQuery)) {
					mCloudItems = result.getClouds();

					// 向地图添加点
					if (mCloudItems != null && mCloudItems.size() > 0) {
						// mAMap.clear();
						mPoiCloudOverlay = new PoiOverlay(this.mAMap,
								mCloudItems);
						mPoiCloudOverlay.removeFromMap();
						mPoiCloudOverlay.addToMap();
						// mPoiCloudOverlay.zoomToSpan();
						for (CloudItem item : mCloudItems) {
							items.add(item);
							Log.d(TAG, "_id " + item.getID());
							Log.d(TAG, "_location "
									+ item.getLatLonPoint().toString());
							Log.d(TAG, "_name " + item.getTitle());
							Log.d(TAG, "_address " + item.getSnippet());
							Log.d(TAG, "_caretetime " + item.getCreatetime());
							Log.d(TAG, "_updatetime " + item.getUpdatetime());
							Log.d(TAG, "_distance " + item.getDistance());
							Iterator iter = item.getCustomfield().entrySet()
									.iterator();
							while (iter.hasNext()) {
								Map.Entry entry = (Map.Entry) iter.next();
								Object key = entry.getKey();
								Object val = entry.getValue();
								Log.d(TAG, key + "   " + val);
							}
						}
						if (mQuery.getBound().getShape()
								.equals(SearchBound.BOUND_SHAPE)) {// 圆形
							/*
							 * mAMap.addCircle(new CircleOptions() .center(new
							 * LatLng(mCenterPoint .getLatitude(), mCenterPoint
							 * .getLongitude())).radius(5000) .strokeColor( //
							 * Color.argb(50, 1, 1, 1) Color.RED)
							 * .fillColor(Color.argb(50, 1, 1, 1))
							 * .strokeWidth(25));
							 */

							/*
							 * mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom
							 * ( new LatLng(mCenterPoint.getLatitude(),
							 * mCenterPoint.getLongitude()), 12));
							 */

						} else if (mQuery.getBound().getShape()
								.equals(SearchBound.POLYGON_SHAPE)) {

						} else if ((mQuery.getBound().getShape()
								.equals(SearchBound.LOCAL_SHAPE))) {
							mPoiCloudOverlay.zoomToSpan();
						}

					} else {
						// ToastUtil.show(con, R.string.no_yun_result);
					}
				}
			} else {
				// ToastUtil.show(con, R.string.no_yun_result);
			}
		} else {
			ToastUtil.show(con, R.string.error_network);
		}
	}

	// 进行服务器搜索点的函数
	public void searchByBound() {
		// showProgressDialog("searchByBound");
		items.clear();
		SearchBound bound = new SearchBound(new LatLonPoint(
				mCenterPoint.getLatitude(), mCenterPoint.getLongitude()), 4000);
		try {
			mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
			mQuery.setPageSize(10);
			CloudSearch.Sortingrules sorting = new CloudSearch.Sortingrules(
					"_id", false);
			mQuery.setSortingrules(sorting);
			mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
		} catch (AMapCloudException e) {
			e.printStackTrace();
		}

	}

}
