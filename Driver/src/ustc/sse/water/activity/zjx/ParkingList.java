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

public class ParkingList extends Activity implements IXListViewListener {

	private XListView mListView;
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
			items.add("ͣ���� " + (++start));
		}
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("�ո�");
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
