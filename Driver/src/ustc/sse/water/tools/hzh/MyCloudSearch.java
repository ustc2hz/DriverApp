package ustc.sse.water.tools.hzh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.zjx.AMapUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;
import android.graphics.Color;
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
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014年12月19日 下午10:27:13
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author ****
 * @version 1.0.0
 */
public class MyCloudSearch implements OnCloudSearchListener {

	private Context con;
	private ArrayList<CloudItem> items = new ArrayList<CloudItem>();
	private AMap mAMap;
	private LatLonPoint mCenterPoint;
	private Marker mCloudIDMarer;
	private List<CloudItem> mCloudItems;
	private CloudSearch mCloudSearch;
	private String mKeyWord = "";
	private PoiOverlay mPoiCloudOverlay;
	private CloudSearch.Query mQuery;
	private String mTableID = "549050e6e4b0d2863fd1dba6";
	private String TAG = "DriverYunTu";

	public MyCloudSearch(Context con, double x, double y, AMap mAMap) {
		this.con = con;
		this.mCenterPoint = new LatLonPoint(x, y);
		this.mAMap = mAMap;
		init();
		searchByBound();
	}

	private void init() {
		mCloudSearch = new CloudSearch(con);
		mCloudSearch.setOnCloudSearchListener(this);
		mPoiCloudOverlay = new PoiOverlay(mAMap, mCloudItems);
	}

	@Override
	public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
		// TODO Auto-generated method stub

		if (rCode == 0 && item != null) {
			if (mCloudIDMarer != null) {
				mCloudIDMarer.destroy();
			}
			mAMap.clear();
			LatLng position = AMapUtil.convertToLatLng(item.getLatLonPoint());
			mAMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition(position, 18, 0, 30)));
			mCloudIDMarer = mAMap.addMarker(new MarkerOptions()
					.position(position)
					.title(item.getTitle())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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

	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		// TODO Auto-generated method stub

		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(mQuery)) {
					mCloudItems = result.getClouds();

					if (mCloudItems != null && mCloudItems.size() > 0) {
						mAMap.clear();
						mPoiCloudOverlay = new PoiOverlay(mAMap, mCloudItems);
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
							mAMap.addCircle(new CircleOptions()
									.center(new LatLng(mCenterPoint
											.getLatitude(), mCenterPoint
											.getLongitude())).radius(5000)
									.strokeColor(
									// Color.argb(50, 1, 1, 1)
											Color.RED)
									.fillColor(Color.argb(50, 1, 1, 1))
									.strokeWidth(25));

							mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(mCenterPoint.getLatitude(),
											mCenterPoint.getLongitude()), 12));

						} else if (mQuery.getBound().getShape()
								.equals(SearchBound.POLYGON_SHAPE)) {
							/*
							 * mAMap.addPolygon(new PolygonOptions()
							 * .add(AMapUtil.convertToLatLng(mPoint1))
							 * .add(AMapUtil.convertToLatLng(mPoint2))
							 * .add(AMapUtil.convertToLatLng(mPoint3))
							 * .add(AMapUtil.convertToLatLng(mPoint4))
							 * .fillColor(Color.LTGRAY)
							 * .strokeColor(Color.RED).strokeWidth(1));
							 */
							/*
							 * LatLngBounds bounds = new LatLngBounds.Builder()
							 * .include(AMapUtil.convertToLatLng(mPoint1))
							 * .include(AMapUtil.convertToLatLng(mPoint2))
							 * .include(AMapUtil.convertToLatLng(mPoint3))
							 * .build();
							 */
							/*
							 * mAMap.moveCamera(CameraUpdateFactory
							 * .newLatLngBounds(bounds, 50));
							 */
						} else if ((mQuery.getBound().getShape()
								.equals(SearchBound.LOCAL_SHAPE))) {
							mPoiCloudOverlay.zoomToSpan();
						}

					} else {
						ToastUtil.show(con, R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(con, R.string.no_result);
			}
		} else {
			ToastUtil.show(con, R.string.error_network);
		}
	}

	public void searchByBound() {
		// showProgressDialog("searchByBound");
		// items.clear();
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
