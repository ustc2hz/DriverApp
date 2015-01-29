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
	public NaviRouteMethod() {
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
		du = new DialogUtil(this.context);
		if (initValue()) {
			// routeSearch = new RouteSearch(con);
			du.showProgressDialog();
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
	}

	@Override
	public void onArrivedWayPoint(int arg0) {
	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		ToastUtil.show(this.context, "路径规划出错" + arg0);
		// mIsCalculateRouteSuccess = false;
		return;
	}

	@Override
	public void onCalculateRouteSuccess() {
		// TODO Auto-generated method stub
		du.dissmissProgressDialog();
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
		// mAMapNavi = null;
	}

	@Override
	public void onEndEmulatorNavi() {
	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
	}

	@Override
	public void onInitNaviFailure() {
	}

	@Override
	public void onInitNaviSuccess() {
	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
	}

	@Override
	public void onReCalculateRouteForYaw() {
	}

	@Override
	public void onStartNavi(int arg0) {
	}

	@Override
	public void onTrafficStatusUpdate() {
	}

}
