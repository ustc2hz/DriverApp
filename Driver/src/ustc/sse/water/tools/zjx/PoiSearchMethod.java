package ustc.sse.water.tools.zjx;

import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.zjx.DialogUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

/**
 * 
 * 关键字搜索类 <br>
 * Poi搜索
 * <p>
 * Copyright: Copyright (c) 2014-11-14 上午8:57:14
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class PoiSearchMethod implements OnMarkerClickListener,
		InfoWindowAdapter, TextWatcher, OnPoiSearchListener {
	/* 接收传递的AMap */
	private AMap aMap;
	/* Poi搜索的关键字 */
	private String keySearch = "";
	/* 当前搜索页 */
	private int currentPage = 0;
	/* 上下文 */
	private Context context;
	/* Poi查询条件类 */
	private PoiSearch.Query query;
	/* POI搜索 */
	private PoiSearch poiSearch;
	/* poi搜索结果 */
	private PoiResult poiResult;
	/* 对话框类 */
	DialogUtil dialog;

	public PoiSearchMethod() {
		// 保留无参构造函数
	}

	/**
	 * 有参构造函数
	 * 
	 * @param map
	 *            操作的地图
	 * @param context
	 *            上下文
	 * @param key
	 *            搜索关键字
	 */
	public PoiSearchMethod(AMap map, Context context, String key) {
		this.aMap = map;
		this.context = context;
		this.keySearch = key;
		dialog = new DialogUtil(context);
	}

	/**
	 * 开始搜索
	 */
	protected void doSearchQuery() {
		dialog.showProgressDialog();// 显示对话框
		currentPage = 0;
		query = new PoiSearch.Query(keySearch, "", "苏州");// 开始在苏州按关键字搜索
		query.setPageSize(5);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(context, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * Poi信息查询回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dialog.dissmissProgressDialog();// 去除对话框
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索Poi的结果
				if (result.getQuery().equals(query)) {// 是否同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含搜索关键字的城市信息

					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {

					} else {
						ToastUtil.show(context, R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(context, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(context, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(context, R.string.error_key);
		} else {
			ToastUtil.show(context, R.string.error_other + rCode);
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
