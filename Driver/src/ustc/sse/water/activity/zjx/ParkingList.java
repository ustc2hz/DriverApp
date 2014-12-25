package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.adapter.zjx.XListViewAdapter;
import ustc.sse.water.utils.zjx.ParkingDataUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.view.zjx.XListView;
import ustc.sse.water.view.zjx.XListView.IXListViewListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * 
 * 停车场类别类. <br>
 * 列出附件的停车场，可上拉和下拉刷新.取自开源社区.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:49:11
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingList extends Activity implements IXListViewListener {
	private XListView mListView; // 自定义的刷新ListView
	private XListViewAdapter adapter;
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private Handler mHandler;
	private static int start = 0;
	private static int refreshCnt = 0;
	private ParkingDataUtil parkingDate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking_list);
		start = 0;
		refreshCnt = 0;
		parkingDate = new ParkingDataUtil();
		if (!geneItems()) { // 取到停车场的列表信息
			currentItems();
			adapter = new XListViewAdapter(this, items);
			mListView = (XListView) findViewById(R.id.xListView);
			mListView.setPullLoadEnable(true);
			mListView.setAdapter(adapter);
			mListView.setXListViewListener(this);
			mHandler = new Handler();
		}

	}

	/**
	 * 列表页面更新，每次都去ParkingDataUtil类中取
	 * 
	 * @return boolean true为取到数据，false为无数据
	 */
	private boolean geneItems() {
		// list.clear();
		boolean flagNull = true;
		list = parkingDate.getParkingData(refreshCnt);
		if (list != null) { // 非空
			flagNull = false;
		} else { // 空
			ToastUtil.show(this, "无停车场列表");
		}
		return flagNull;
	}

	/**
	 * 每次页面中默认显示6个停车场，如果上拉刷新，则显示更多
	 */
	private void currentItems() {
		// 6个为单位加入items中
		for (; start < list.size(); start++) {
			items.add(list.get(start));
			if ((start + 1) % 6 == 0) {
				break;
			}
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	/**
	 * 下拉刷新列表
	 */

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// refreshCnt += 1; // 查看的页数加1
				if (!geneItems()) {
					// start = 0; // 归0
					// items.clear(); // 清空
					// currentItems();
					// adapter = new XListViewAdapter(ParkingList.this, items);
					// mListView.setAdapter(adapter);
					// adapter.notifyDataSetChanged(); onLoad();
				} else {
					// refreshCnt = 0;
				}
			}
		}, 2000);
	}

	/**
	 * 上拉加载更多
	 */
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (start == list.size()) { // 如果start已经到了list的最大值则，不再加载更多
					ToastUtil.show(ParkingList.this, "已到底");
				} else {
					start++;
					currentItems(); // 调用方法
					adapter.notifyDataSetChanged();
					onLoad();
				}
			}
		}, 2000);
	}
}
