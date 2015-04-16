package ustc.sse.water.adapter.zjx;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * ParkingDetail的ViewPager适配器. <br>
 * ParkingDetail的标题和每一页内容设置.
 * <p>
 * Copyright: Copyright (c) 2015-1-30 下午3:54:01
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingDetailVPAdapter extends PagerAdapter {
	private List<View> viewList; // 页列表
	private List<String> titleList; // 页标题

	/**
	 * 有参构造函数
	 * @param viewList 页视图列表
	 * @param titleList 页标题列表
	 */
	public ParkingDetailVPAdapter(List<View> viewList, List<String> titleList) {
		this.viewList = viewList;
		this.titleList = titleList;
	}

	@Override
	public int getCount() {
		// 页的个数
		return viewList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// 切换页
		((ViewPager) container).removeView(viewList.get(position));
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// 标题名
		return titleList.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// 实例化当前页
		((ViewPager) container).addView(viewList.get(position));
		return viewList.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// 相等
		return arg0 == arg1;
	}

}
