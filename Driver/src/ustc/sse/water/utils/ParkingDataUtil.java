package ustc.sse.water.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ustc.sse.water.tools.MyCloudSearch;
import ustc.sse.water.tools.PoiAroundSearchMethod;

import com.amap.api.cloud.model.CloudItem;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

/**
 * 
 * 获取停车场列表数据类. <br>
 * 搜集周边停车场搜索结果和云图上停车场，并将它们的停车场名、地点和距离提取出来；如果是可以预定的停车场则存储预定信息.
 * <p>
 * Copyright: Copyright (c) 2014-12-24 下午1:39:16
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingDataUtil {

	private PoiSearch.Query poiQuery; // Poi查询条件类
	private PoiResult poiResult; // 周边搜索的结果
	private PoiSearch poiSearch; // Poi搜索
	private List<PoiItem> poiItems; // 暂存周边搜索结果的某一页
	private ArrayList<CloudItem> cloudItems; // 云图的停车场
	private List<Map<String, Object>> listParking; // 将停车场信息都统一方在这个list中

	/**
	 * 无参构造函数，初始化
	 */
	public ParkingDataUtil() {
		poiQuery = PoiAroundSearchMethod.query; // 获取周边搜索的Poi查询条件
		poiResult = PoiAroundSearchMethod.poiResult; // 获取周边搜索的搜索结果
		poiSearch = PoiAroundSearchMethod.poiSearch; // 获取周边搜索的Poi搜索
		cloudItems = MyCloudSearch.items; // 获取云图中的停车场点
		poiItems = new ArrayList<PoiItem>();
		listParking = new ArrayList<Map<String, Object>>();
	}

	/**
	 * 将周边搜索和云图搜索中的停车场中的名字、地点和距离提取出来，存放在list中
	 * 
	 * @param currentPage
	 *            周边搜索的第几页
	 * @return List<Map<String, Object>> 整理后的list
	 */
	public List<Map<String, Object>> getParkingData(int currentPage) {
		listParking.clear(); // 清空数据表
		if (poiQuery != null && poiResult != null && cloudItems != null) { // 不空的话才操作数据
			// 处理云图中的停车场点
			for (CloudItem mItem : cloudItems) {
				String phone = null, parkSum = null;
				String[] orderPrice = new String[6];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("parkingName", mItem.getTitle()); // 存储停车场的名字
				map.put("parkingDistance", mItem.getDistance()); // 存储停车场到中心点的距离
				map.put("parkingAddress", mItem.getSnippet()); // 存储停车场的地点
				Iterator iter = mItem.getCustomfield().entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					if ("phone".equals(key)) {
						phone = (String) value;
					} else if ("parkSum".equals(key)) {
						parkSum = (String) value;
					} else if ("orderTen".equals(key)) {
						orderPrice[0] = (String) value;
					} else if ("orderTwe".equals(key)) {
						orderPrice[1] = (String) value;
					} else if ("orderTri".equals(key)) {
						orderPrice[2] = (String) value;
					} else if ("payHalPay".equals(key)) {
						orderPrice[3] = (String) value;
					} else if ("payOneHour".equals(key)) {
						orderPrice[4] = (String) value;
					} else if ("payMorePay".equals(key)) {
						orderPrice[5] = (String) value;
					}
				}
				map.put("parkingSum", parkSum);
				map.put("bookMoney", orderPrice);
				map.put("phone", phone);
				map.put("isAmap", "AMapCloudPark"); // 标记是云图的停车场
				listParking.add(map); // 将每个停车场都放入列表中
			}
			if (poiResult.getPageCount() - 1 > currentPage) {
				// 处理周边搜索的停车场
				if (currentPage == 0) {
					poiItems = poiResult.getPois(); // 直接取出数据
					for (int i = 0; i < poiItems.size(); i++) {
						PoiItem poiItem = poiItems.get(i); // 取出每一个
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("parkingName", poiItem.getTitle()); // 存储停车场的名字
						map.put("parkingDistance", poiItem.getDistance()); // 存储停车场到中心点的距离
						map.put("parkingAddress", poiItem.getSnippet()); // 存储停车场的地点
						map.put("isAmap", "AMapPark"); // 标记是高德的停车场
						listParking.add(map); // 将每个停车场都放入列表中
					}
				}
			}
		}
		return listParking;
	}
}
