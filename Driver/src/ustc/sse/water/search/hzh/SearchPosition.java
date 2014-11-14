package ustc.sse.water.search.hzh;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

/**
 * 目的地搜索框的功能实现 <br>
 * 此类的功能包括：输入汉字后自动联想功能，确定后搜索目的地并将目的地显示到地图上的功能
 * <p>
 * Copyright: Copyright (c) 2014年11月13日 下午8:19:05
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒 sa614399@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class SearchPosition extends FragmentActivity implements
		OnMarkerClickListener, InfoWindowAdapter, TextWatcher,
		OnPoiSearchListener, OnClickListener {
	/** 要显示的地图 */
	private AMap aMap;
	/** 当前页面，从0开始计数 */
	private int currentPage = 0;
	/** 要输入的城市名字或者城市区号 */
	private EditText editCity;
	/** 要输入的目的地关键字 */
	private String keyWord = "";
	/** 目的地返回的结果 */
	private PoiResult poiResult;
	/** 目的地搜索 */
	private PoiSearch poiSearch;
	/** 搜索时进度条 */
	private ProgressDialog progDialog = null;
	/** 目的地查询条件类 */
	private PoiSearch.Query query;
	/** 输入关键字 */
	private AutoCompleteTextView searchText;

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
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

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			setUpMap();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiSearched(PoiResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

}
