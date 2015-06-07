package ustc.sse.water.adapter;

import java.util.List;

import ustc.sse.water.driver.ParkingDetailNavi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 
 * 自定义Adapter类. <br>
 * ViewPager的Adapter，用来辅助实现停车场预定界面的顶部导航.
 * <p>
 * Copyright: Copyright (c) 2015-6-7 下午2:54:08
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class ParkingNaviVPAdapter extends FragmentPagerAdapter {
	
	// 界面的Fragment集合
	List<Fragment> pfs;

	public ParkingNaviVPAdapter(FragmentManager fm ,List<Fragment> list) {
		super(fm);
		this.pfs = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		
		switch (arg0) {
		case ParkingDetailNavi.TAB_PARKING_INFO:
			// 跳转到“车位详情”界面
			return pfs.get(0);
		case ParkingDetailNavi.TAB_PARKING_BOOK: 
			// 跳转到“车位预定”界面
			return pfs.get(1);
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// 总的界面数
		return ParkingDetailNavi.TAB_COUNT;
	}

}
