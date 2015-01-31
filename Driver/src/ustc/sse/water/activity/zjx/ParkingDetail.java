package ustc.sse.water.activity.zjx;

import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.fragment.PDTabListener;
import ustc.sse.water.fragment.ParkingBookFragment;
import ustc.sse.water.fragment.ParkingInfoFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
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
		parkingBook.setTabListener(new PDTabListener(new ParkingBookFragment(
				ParkingDetail.this)));
		// 添加到ActionBar中
		actionBar.addTab(parkingInfo);
		actionBar.addTab(parkingBook);
	}

}