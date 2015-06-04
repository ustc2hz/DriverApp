package ustc.sse.water.tools;

import java.util.ArrayList;
import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.ProgressDialogUtil;
import ustc.sse.water.utils.ToastUtil;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
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
 * Poi搜索-----参考自高德地图API
 * <p>
 * Copyright: Copyright (c) 2014-11-14 上午8:57:14
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.0.0
 */
public class PoiSearchMethod implements TextWatcher, OnPoiSearchListener,
		OnEditorActionListener {

	/* 接收传递的AMap */
	private AMap aMap;
	/* 上下文 */
	private Context context;
	/* 当前搜索页 */
	private int currentPage = 0;
	/* 对话框类 */
	ProgressDialogUtil dialog;
	/* 接收传递的自动输入框 */
	private AutoCompleteTextView keyEdit;
	/* Poi搜索的关键字 */
	private String keySearch = "";
	/* 搜索显示的点 */
	LatLonPoint lp;
	List<PoiItem> poiItems;
	PoiOverlay poiOverlay;
	/* poi搜索结果 */
	private PoiResult poiResult;
	/* POI搜索 */
	private PoiSearch poiSearch;
	/* Poi查询条件类 */
	private PoiSearch.Query query;
	private TextView showInfo;

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
	public PoiSearchMethod(AMap map, Context context,
			AutoCompleteTextView edit, TextView showInfo) {
		this.aMap = map;
		this.context = context;
		this.keyEdit = edit;
		this.showInfo = showInfo;
		keyEdit.addTextChangedListener(this);// 自动填充文本框监听事件
		keyEdit.setOnEditorActionListener(this);
		keySearch = keyEdit.getText().toString().trim();
	}

	/**
	 * 有参构造函数
	 * 
	 * @param map
	 *            高德地图
	 * @param con
	 *            上下文
	 * @param keyword
	 *            Poi关键字
	 */
	public PoiSearchMethod(AMap map, Context con, String keyword) {
		this.aMap = map;
		this.context = con;
		this.keySearch = keyword;

		dialog = new ProgressDialogUtil(context, "正在搜索...");
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	/**
	 * 查单个poi详情
	 * 
	 * @param poiId
	 *            poi的id
	 */
	public void doSearchPoiDetail(String poiId) {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}

	/**
	 * 开始搜索
	 */
	public void doSearchQuery() {
		currentPage = 0;
		query = new PoiSearch.Query(keySearch, "", "苏州");// 开始在苏州按关键字搜索
		query.setPageSize(1);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页
		poiSearch = new PoiSearch(context, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();

	}

	public LatLonPoint getLp() {
		return lp;
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_DONE:
			keySearch = keyEdit.getText().toString().trim();
			this.showInfo.setVisibility(View.INVISIBLE);
			doSearchQuery();
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			// 显示或者隐藏输入法
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
	}

	/**
	 * Poi信息查询回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索Poi的结果
				if (result.getQuery().equals(query)) {// 是否同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含搜索关键字的城市信息

					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
						// 获取搜索获得的点
						lp = poiItems.get(0).getLatLonPoint();
						if (lp != null) {
							// 重新在地图上显示附近停车场
							new PoiAroundSearchMethod(this.aMap, this.context,
									"停车场", lp);
							// 重新在地图上显示云图数据——黄志恒注
							new MyCloudSearch(this.context, lp.getLatitude(),
									lp.getLongitude(), this.aMap);
						}
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
			// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
			inputTips.requestInputtips(newText, "苏州");
		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	public void setLp(LatLonPoint lp) {
		this.lp = lp;
	}

}
