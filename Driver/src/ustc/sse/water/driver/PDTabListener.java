package ustc.sse.water.driver;

import ustc.sse.water.activity.R;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 *
 * ActionBar的Tab监听器. <br>
 * ParkingDetail中的ActionBar的Tab监听器.
 * <p>
 * Copyright: Copyright (c) 2015-1-30 下午5:09:01
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class PDTabListener implements TabListener {
	private Fragment mFragment; // Fragment

	/**
	 * 有参构造函数
	 *
	 * @param fragment
	 *            传递的Fragment
	 */
	public PDTabListener(Fragment fragment) {
		this.mFragment = fragment;
	}

	/**
	 * 选择Tab后，用新的Fragment替代当前Fragment
	 */
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.parking_detail_fram, mFragment, null);
	}

	/**
	 * 没选择Tab时，去除当前的Fragment
	 */
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(mFragment);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

}
