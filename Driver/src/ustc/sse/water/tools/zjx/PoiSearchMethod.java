package ustc.sse.water.tools.zjx;

import java.util.ArrayList;
import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.zjx.DialogUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
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
 * @version 2.0.0
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
	/* 接收传递的自动输入框 */
	private AutoCompleteTextView keyEdit;

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
	 * @param edit
	 *            自动填充文本框
	 */
	public PoiSearchMethod(AMap map, Context context, AutoCompleteTextView edit) {
		this.aMap = map;
		this.context = context;
		this.keyEdit = edit;
		dialog = new DialogUtil(context); // 生成对话框
		keyEdit.addTextChangedListener(this);// 自动填充文本框监听事件
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
	}

	/**
	 * 开始搜索
	 */
	protected void doSearchQuery() {
		dialog.showProgressDialog();// 显示对话框
		currentPage = 0;
		query = new PoiSearch.Query(keySearch, "", "苏州");// 开始在苏州按关键字搜索
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(context, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
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
			ToastUtil.show(context, R.string.error_other + rCode + "");
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	/**
	 * 自动填充文本，正在输入
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(context, new InputtipsListener() {

			@Override
			public void onGetInputtips(List<Tip> tipList, int rCode) {
				if (rCode == 0) {// 正确返回
					List<String> listString = new ArrayList<String>();
					for (int i = 0; i < tipList.size(); i++) {
						listString.add(tipList.get(i).getName());
					}
					ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
							context, R.layout.route_inputs, listString);
					keyEdit.setAdapter(aAdapter);
					aAdapter.notifyDataSetChanged();
				}
			}
		});
		try {
			inputTips.requestInputtips(newText, "苏州");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输入信息改变之后
	 */
	@Override
	public void afterTextChanged(Editable s) {
		keySearch = s.toString();
		doSearchQuery(); // 开始搜索
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
