package ustc.sse.water.tools.hzh;

import java.util.ArrayList;

import ustc.sse.water.utils.zjx.DialogUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.ProgressDialog;
import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;

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

public class NaviRouteMethod implements AMapNaviListener {
	/* 高德地图AMap */
	private AMap aMap;
	/* 传递过来的上下文 */
	private Context context;
	/* 驾车模式查询结果 */
	private DriveRouteResult driveRouteResult;
	/* 驾车默认模式 */
	private int drivingMode = RouteSearch.DrivingDefault;
	private DialogUtil du;
	/* 计算路径时的起始位置 */
	private LatLonPoint endPoint = null;
	private AMapNavi mAMapNavi;
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	private NaviLatLng mNaviEnd;
	private NaviLatLng mNaviStart;
	// 规划线路
	public RouteOverLay mRouteOverLay;
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
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
		// du = new DialogUtil();
		if (initValue()) {
			routeSearch = new RouteSearch(con);
			// du.showProgressDialog();
			calculateDriveRoute();
		} else {
			ToastUtil.show(con, "路径规划失败");

		}
	}

	// 计算驾车路线
	private void calculateDriveRoute() {
		boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (!isSuccess) {
			ToastUtil.show(this.context, "路线计算失败,检查参数情况");
		}

	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	public boolean initValue() {
		mAMapNavi = AMapNavi.getInstance(context);
		mAMapNavi.setAMapNaviListener(this);
		mRouteOverLay = new RouteOverLay(this.aMap, null);

		if (this.startPoint != null && this.endPoint != null) {
			mNaviStart = new NaviLatLng(this.startPoint.getLatitude(),
					this.startPoint.getLongitude());
			mNaviEnd = new NaviLatLng(this.endPoint.getLatitude(),
					this.endPoint.getLongitude());
			mStartPoints.add(mNaviStart);
			mEndPoints.add(mNaviEnd);
			return true;
		}
		return false;
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
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		ToastUtil.show(this.context, "路径规划出错" + arg0);
		// mIsCalculateRouteSuccess = false;
		return;
	}

	@Override
	public void onCalculateRouteSuccess() {
		// TODO Auto-generated method stub
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// du.dissmissProgressDialog();
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
		// mIsCalculateRouteSuccess = true;
		AMapNavi.getInstance(this.context).removeAMapNaviListener(this);

	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
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
	/*
	 * public void searchRouteResult(LatLonPoint startPoint, LatLonPoint
	 * endPoint) { showProgressDialog();// 显示对话框 final RouteSearch.FromAndTo
	 * fromAndTo = new RouteSearch.FromAndTo( startPoint, endPoint);
	 * DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode, null,
	 * null, "");//
	 * 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
	 * routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
	 * 
	 * }
	 */

	/**
	 * 显示进度框
	 */
	/*
	 * private void showProgressDialog() { if (progDialog == null) { progDialog
	 * = new ProgressDialog(context); }
	 * progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	 * progDialog.setIndeterminate(false); progDialog.setCancelable(true);
	 * progDialog.setMessage("正在搜索"); progDialog.show();// 将进度框显示 }
	 */

}
