package ustc.sse.water.tools.zjx;

import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.zjx.DialogUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.content.Context;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.Cinema;
import com.amap.api.services.poisearch.Dining;
import com.amap.api.services.poisearch.Hotel;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.poisearch.Scenic;

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
 * @version 1.0.0
 */
public class PoiAroundSearchMethod implements OnMarkerClickListener,
		InfoWindowAdapter, OnPoiSearchListener, OnMapClickListener,
		OnInfoWindowClickListener {
	/* 显示搜索结果的地图 */
	private AMap aMap;
	/* Poi搜索类型 */
	private String deepType = "";
	/* Poi返回的结果 */
	private PoiResult poiResult;
	/* 当前页面，从0开始计数 */
	private int currentPage = 0;
	/* Poi查询条件类 */
	private PoiSearch.Query query;
	/* 搜索中心 */
	private LatLonPoint lp;
	/* 选择的点 */
	private Marker locationMarker;
	/* Poi搜索 */
	private PoiSearch poiSearch;
	/* poi图层 */
	private PoiOverlay poiOverlay;
	/* poi数据 */
	private List<PoiItem> poiItems;
	/* 显示Marker的详情 */
	private Marker detailMarker;
	/* 上下文 */
	private Context context;
	/* 对话框类 */
	DialogUtil dialog;

	public PoiAroundSearchMethod() {
		// 无参构造函数
	}

	/**
	 * 有参构造函数
	 * 
	 * @param map
	 *            传递的地图
	 * @param con
	 *            地图的Activity上下文
	 * @param type
	 *            搜索类型
	 * @param lp
	 *            搜索中心点
	 */
	public PoiAroundSearchMethod(AMap map, Context con, String type,
			LatLonPoint lp) {
		this.aMap = map;
		this.context = con;
		this.deepType = type;
		this.lp = lp;
		dialog = new DialogUtil(context);
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		aMap.setOnInfoWindowClickListener(this);// 点击消息窗口监听事件
		aMap.setOnMapClickListener(this);// 地图点击监听事件
		doSearchQuery(); // 开始搜索
	}

	/**
	 * 开始Poi搜索
	 */
	public void doSearchQuery() {
		dialog.showProgressDialog();// 显示对话框
		aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
		currentPage = 0;
		query = new PoiSearch.Query("", "停车场", "苏州");// Poi搜索
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页
		if (lp != null) {
			// 设置搜索区域为以lp点为圆心，其周围2000米范围
			poiSearch = new PoiSearch(context, query); // 构造PoiSearch对象
			poiSearch.setOnPoiSearchListener(this); // 设置查询监听接口
			poiSearch.setBound(new SearchBound(lp, 3000, true)); // 设置查询矩形
			poiSearch.searchPOIAsyn();// 异步搜索
		}
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

	@Override
	public void onInfoWindowClick(Marker marker) {
		locationMarker.hideInfoWindow();
		lp = new LatLonPoint(locationMarker.getPosition().latitude,
				locationMarker.getPosition().longitude);
		locationMarker.destroy();
	}

	@Override
	public void onMapClick(LatLng latng) {
		locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
				.position(latng).title("点击选择为中心点"));
		locationMarker.showInfoWindow();

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
						sb = getDeepInfo(result, sb);
						detailMarker.setSnippet(sb.toString());
					} else {
						ToastUtil.show(context, "此Poi点没有深度信息");
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
	 * POI深度信息获取 先保留，以后再修改
	 */
	private StringBuffer getDeepInfo(PoiItemDetail result,
			StringBuffer sbuBuffer) {
		switch (result.getDeepType()) {
		// 深度信息
		case DINING:
			if (result.getDining() != null) {
				Dining dining = result.getDining();
				sbuBuffer
						.append("\n菜系：" + dining.getTag() + "\n特色："
								+ dining.getRecommend() + "\n来源："
								+ dining.getDeepsrc());
			}
			break;
		// 酒店深度信息
		case HOTEL:
			if (result.getHotel() != null) {
				Hotel hotel = result.getHotel();
				sbuBuffer.append("\n价位：" + hotel.getLowestPrice() + "\n卫生："
						+ hotel.getHealthRating() + "\n来源："
						+ hotel.getDeepsrc());
			}
			break;
		// 景区深度信息
		case SCENIC:
			if (result.getScenic() != null) {
				Scenic scenic = result.getScenic();
				sbuBuffer
						.append("\n价钱：" + scenic.getPrice() + "\n推荐："
								+ scenic.getRecommend() + "\n来源："
								+ scenic.getDeepsrc());
			}
			break;
		// 影院深度信息
		case CINEMA:
			if (result.getCinema() != null) {
				Cinema cinema = result.getCinema();
				sbuBuffer.append("\n停车：" + cinema.getParking() + "\n简介："
						+ cinema.getIntro() + "\n来源：" + cinema.getDeepsrc());
			}
			break;
		default:
			break;
		}
		return sbuBuffer;
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
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {
						// aMap.clear();// 清理之前的图标
						poiOverlay = new PoiOverlay(aMap, poiItems);
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

	/**
	 * Marker点击事件，查看Marker详情
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (poiOverlay != null && poiItems != null && poiItems.size() > 0) {
			detailMarker = marker;
			doSearchPoiDetail(poiItems.get(poiOverlay.getPoiIndex(marker))
					.getPoiId());
		}
		return false;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
