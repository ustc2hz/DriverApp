package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.view.zjx.XListView;
import ustc.sse.water.view.zjx.XListView.IXListViewListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

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
	private ArrayAdapter<String> mAdapter;
	private XListViewAdapter adapter;
	private List<String> items = new ArrayList<String>();
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking_list);
		geneItems();
		adapter = new XListViewAdapter(this, items);
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);

		// mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		mListView.setAdapter(adapter);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
	}

	private void geneItems() {
		for (int i = 0; i != 10; ++i) {
			items.add("ͣ停车场 " + (++start)); // 测试
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				items.clear();
				geneItems();
				// mAdapter = new ArrayAdapter<String>(MainActivity.this,
				// R.layout.list_item, items);
				adapter = new XListViewAdapter(ParkingList.this, items);
				mListView.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
}
