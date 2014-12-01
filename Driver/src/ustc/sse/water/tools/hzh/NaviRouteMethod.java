package ustc.sse.water.tools.hzh;

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.ProgressDialog;
import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;

/**
 * 路径规划类 <br>
 * 该类用来计算自身位置与目的地的行车路径
 * <p>
 * Copyright: Copyright (c) 2014年11月28日 下午7:51:54
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 黄志恒 sa14226399@mail.ustc.edu.cn
 * @version 2.0.0
 */

public class NaviRouteMethod implements OnRouteSearchListener {
	/* 高德地图AMap */
	private AMap aMap;
	/* 传递过来的上下文 */
	private Context context;
	/* 驾车模式查询结果 */
	private DriveRouteResult driveRouteResult;
	/* 驾车默认模式 */
	private int drivingMode = RouteSearch.DrivingDefault;
	/* 计算路径时的起始位置 */
	private LatLonPoint endPoint = null;
	/* 搜索时进度条 */
	private ProgressDialog progDialog = null;
	/* 路径搜索类的对象 */
	private RouteSearch routeSearch;
	/* 计算路径时的结束位置 */
	private LatLonPoint startPoint = null;

	// 无参构造函数
	NaviRouteMethod() {

	}

	/**
	 * 有参构造函数
	 * 
	 * @param map
	 *            传递的地图
	 * @param lp
	 *            当前位置
	 * @param con
	 *            传递来的上下文
	 * @param tp
	 *            目的地位置
	 */
	public NaviRouteMethod(AMap aMap, LatLonPoint lp, Context con,
			LatLonPoint tp) {
		this.context = con;
		this.aMap = aMap;
		this.startPoint = lp;
		this.endPoint = tp;
		routeSearch = new RouteSearch(con);
		routeSearch.setRouteSearchListener(this);// 添加计算路径的监听
		searchRouteResult(startPoint, endPoint);// 进行路径规划计算
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 驾车结果回调
	 * 
	 * @param result
	 *            计算路径后返回的结果
	 * @param rCode
	 *            计算路径后返回的参数，0代表计算路径成功
	 */
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		dissmissProgressDialog();// 显示对话框
		if (rCode == 0) {// 计算路径成功后的处理
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						context, aMap, drivePath,
						driveRouteResult.getStartPos(),
						driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();// 将计算出的规划路径显示在地图上
				drivingRouteOverlay.zoomToSpan();
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
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * 开始搜索路径规划方案
	 * 
	 * @param startPoint
	 *            路径的起始点
	 * @param endPoing
	 *            路径的终结点
	 */
	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		showProgressDialog();// 显示对话框
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode,
				null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
		routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询

	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null) {
			progDialog = new ProgressDialog(context);
		}
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在搜索");
		progDialog.show();// 将进度框显示
	}

}
