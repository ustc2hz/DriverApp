
package ustc.sse.water.tools;

import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.ProgressDialogUtil;
import ustc.sse.water.utils.ToastUtil;
import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;

/**
 *
 * 周边搜索类. <br>
 * 包含周边搜索功能的各种方法，以完成当前位置的周边搜索，目的地位置的周边搜索，和“周边搜索”导航.
 * <p>
 * Copyright: Copyright (c) 2014-11-15 下午2:26:43
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.1.0
 */
public class PoiAroundSearchMethod implements OnPoiSearchListener {
	/* 显示Marker的详情 */
	public static Marker detailMarker;
	/* poi数据 */
	public static List<PoiItem> poiItems;
	/* poi图层 */
	public static PoiOverlay poiOverlay;
	/* Poi返回的结果，static类型方便外部类调用 */
	public static PoiResult poiResult;
	/* Poi搜索 */
	public static PoiSearch poiSearch;
	/* Poi查询条件类 */
	public static PoiSearch.Query query;
	/* 显示搜索结果的地图 */
	private AMap aMap;
	/* 上下文 */
	private Context context;
	/* 当前页面，从0开始计数 */
	private int currentPage = 0;
	/* Poi搜索类型 */
	private String deepType = "";
	/* 对话框类 */
	ProgressDialogUtil dialog;
	/* 搜索中心 */
	private LatLonPoint lp;
	/* 记录屏幕上的marker的个数 */
	public static List<Marker> list;

	public PoiAroundSearchMethod() {
		// 无参构造函数
	}

	/**
	 * 有参构造函数
	 * @param map 传递的地图
	 * @param con 地图的Activity上下文
	 * @param type 搜索类型
	 * @param lp 搜索中心点
	 */
	public PoiAroundSearchMethod(AMap map, Context con, String type,
			LatLonPoint lp) {
		this.aMap = map;
		this.context = con;
		this.deepType = type;
		this.lp = lp;
		dialog = new ProgressDialogUtil(context, "正在搜索...");
		try {
			doSearchQuery();
		} catch (AMapException e) {
			dialog.dissmissProgressDialog();
		} 

	}

	/**
	 * 查单个poi详情
	 * @param poiId poi的id
	 */
	public void doSearchPoiDetail(String poiId) throws AMapException {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);

		}
	}

	/**
	 * 开始Poi搜索
	 */
	public void doSearchQuery() throws AMapException {
		dialog.showProgressDialog();// 显示对话框
		currentPage = 0;
		query = new PoiSearch.Query("", deepType, "苏州");// Poi搜索
		query.setPageSize(6);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页
		if (lp != null) {
			// 设置搜索区域为以lp点为圆心，其周围2000米范围
			poiSearch = new PoiSearch(context, query); // 构造PoiSearch对象
			poiSearch.setOnPoiSearchListener(this); // 设置查询监听接口
			poiSearch.setBound(new SearchBound(lp, 2000, true)); // 设置查询矩形
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}

	/**
	 * POI详情回调
	 */
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		dialog.dissmissProgressDialog();// 去除对话框
		if (rCode == 0) {
			if (result != null) {// 搜索poi的结果
				if (detailMarker != null) {
					StringBuffer sb = new StringBuffer(result.getSnippet());
					// 判断poi搜索是否有深度信息
					if (result.getDeepType() != null) {
						detailMarker.setSnippet(sb.toString());
					}
				}
			}
		} else if (rCode == 27) {
			ToastUtil.show(context, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(context, R.string.error_key);
		} else {
			ToastUtil.show(context, R.string.error_other + rCode + "");
		}

	}

	/**
	 * POI搜索回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dialog.dissmissProgressDialog();// 去除对话框
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					if (poiItems != null && poiItems.size() > 0) {
						// 自定义PoiOverlay图层
						poiOverlay = new PoiOverlay(aMap, poiItems) {
							@Override
							protected BitmapDescriptor getBitmapDescriptor(
									int arg0) {
								// 返回自定义的Marker图片
								BitmapDescriptor bd = BitmapDescriptorFactory
										.fromResource(R.drawable.icon2);
								return bd;
							}
						};

						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
					} else {
						ToastUtil.show(context, R.string.no_result);

					}
				}
			}
		} else if (rCode == 27) {
			ToastUtil.show(context, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(context, R.string.error_key);
		} else {
			ToastUtil.show(context, R.string.error_other + rCode + "");
		}
	}

}
