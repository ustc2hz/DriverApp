package ustc.sse.water.driver;

import java.util.Map;

import ustc.sse.water.activity.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * 
 * 停车场详细信息类. <br>
 * 使用ActionBar的Tab效果来分别展示停车场的详细信息界面和预定界面.
 * <p>
 * Copyright: Copyright (c) 2015-1-30 下午3:44:15
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingDetail extends Activity {

	private ActionBar actionBar; // 此Activity的ActionBar
	Map<String, Object> selectParking = null; // 用来接收列表中选中的停车场

	private int managerId = 0; // 停车场管理员id
	private int driverId = 0; // 驾驶员id
	private String parkType = null; // 停车场类型：Web和APP

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking_detail_book);
		// 接收传递过来的停车场
		selectParking = (Map<String, Object>) getIntent().getSerializableExtra(
				"select_parking");
		initActionBar();
	}

	/**
	 * 初始化ActionBar
	 */
	public void initActionBar() {
		actionBar = getActionBar(); // 获取ActionBar
		actionBar.setDisplayShowHomeEnabled(false); // 不显示应用程序的图标
		// 使用ActionBar的Tab模式
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false); // 不显示标题
		// 定义Tab标签
		Tab parkingInfo = actionBar.newTab().setText(
				R.string.parking_detail_info);
		Tab parkingBook = actionBar.newTab().setText(
				R.string.parking_order_title);
		// 添加监听器
		parkingInfo.setTabListener(new PDTabListener(new ParkingInfoFragment(
				ParkingDetail.this, selectParking)));
		managerId = Integer.parseInt((String) selectParking.get("managerId"));
		driverId = getSharedPreferences("userdata", Context.MODE_PRIVATE)
				.getInt("driverLoginId", 0);
		parkType = (String) selectParking.get("parkType");
		parkingBook.setTabListener(new PDTabListener(new ParkingBookFragment(
				ParkingDetail.this, managerId, driverId, parkType)));
		// 添加到ActionBar中
		actionBar.addTab(parkingInfo);
		actionBar.addTab(parkingBook);
	}

}
