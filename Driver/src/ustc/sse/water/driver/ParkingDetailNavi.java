package ustc.sse.water.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.adapter.ParkingNaviVPAdapter;
import ustc.sse.water.service.GetCurrentBookNumber;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
/**
 * 
 * FragmentActivity类. <br>
 * ActionBar+ViewPager+Fragment构成顶部导航，分别导航到：停车场详细信息界面和停车场车位预定界面.
 * <p>
 * Copyright: Copyright (c) 2015-6-7 下午1:26:19
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class ParkingDetailNavi extends FragmentActivity {
	
	public final static int TAB_PARKING_INFO = 0; // 停车场详细信息 
	public final static int TAB_PARKING_BOOK = 1; // 车位预定
	public final static int TAB_COUNT = 2; // 导航总个数
	// 导航标题名
	private final static String[] tabTitles = {"车位详情","车位预定"};
	// 用来接收列表中选中的停车场
	Map<String, Object> selectParking = null;
	private int managerId = 0; // 停车场管理员id
	private int driverId = 0; // 驾驶员id
	private String parkType = null; // 停车场类型：Web和APP
	private List<Fragment> pFragments = null;
	private Context context;
	
	private ViewPager mViewPager; // ViewPager
	private ActionBar mActionBar; // ActionBar
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.parking_detail_navi);
		context = ParkingDetailNavi.this;
		
		// 接收传递过来的停车场
		selectParking = (Map<String, Object>) getIntent().getSerializableExtra(
				"select_parking");
		
		setUpActionBar(); // 设置ActionBar的属性
		setUpTab(); // 设置导航标题
		setUpViewPager(); // 设置ViewPager
	}
	
	/**
	 * 初始化ActionBar
	 * 设置ActionBar属性
	 */
	private void setUpActionBar() {
		mActionBar = getActionBar(); // 获取ActionBar
		// 以Tab方式导航
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// 禁用ActionBar标题
		mActionBar.setDisplayShowTitleEnabled(false);
		// 禁用ActionBar返回键
		mActionBar.setDisplayShowHomeEnabled(false);
		// 禁用ActionBar图标
		mActionBar.setDisplayUseLogoEnabled(false);
	}
	
	/**
	 * 设置导航标题名
	 */
	private void setUpTab() {
		for (int i = 0; i < tabTitles.length; i++) {
			mActionBar.addTab(mActionBar.newTab().setText(tabTitles[i])
					.setTabListener(mTabListener));
		}
	}
	
	/**
	 * 初始化ViewPager
	 */
	private void setUpViewPager() {
		// 获取所需要的数据
		managerId = Integer.parseInt((String) selectParking.get("managerId"));
		driverId = getSharedPreferences("userdata", Context.MODE_PRIVATE)
				.getInt("driverLoginId", 0);
		parkType = (String) selectParking.get("parkType");
		
		// 创建Fragment列表并传给Adapter
		pFragments = new ArrayList<Fragment>();
		// 创建第一个界面的Fragment
		Fragment parkInfo = new ParkingInfoFragment(ParkingDetailNavi.this,
				selectParking);
		// 创建第二个界面的Fragment
		Fragment parkBook = new ParkingBookFragment(ParkingDetailNavi.this,
				managerId, driverId, parkType);
		pFragments.add(parkInfo);
		pFragments.add(parkBook);
		
		mViewPager = (ViewPager) findViewById(R.id.parking_navi_viewPager);
		// 设置Adapter
		ParkingNaviVPAdapter adapter = new ParkingNaviVPAdapter(
				getSupportFragmentManager(), pFragments);
		mViewPager.setAdapter(adapter);
		// 当ViewPager滑动时，Tab也要相应变换，设置监听器
		mViewPager.setOnPageChangeListener(new TabPagerListener());
	}
	
	/*
	 * 必须有的Tab监听器 
	 */
	private final TabListener mTabListener = new TabListener() {
		
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Tab切换了，则ViewPager的内容也要切换
			if(mViewPager != null) { 
				mViewPager.setCurrentItem(tab.getPosition());
			}
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	};
	
	/*
	 * 监听ViewPager变换，来改变Tab标题
	 */
	class TabPagerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			if(arg0 == TAB_PARKING_INFO) {
				// 开启获取剩余可预订车位数的服务
				Intent numService = new Intent(context, GetCurrentBookNumber.class);
				numService.putExtra("parking_managerId",
						String.valueOf(managerId));
				context.startService(numService);
			}
			// ViewPager中的Fragment变化，导致ActionBar中的Tab变化
			mActionBar.selectTab(mActionBar.getTabAt(arg0));
		}
	}
	
}
