package ustc.sse.water.driver;

import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.adapter.DriverOrderAdapter;
import ustc.sse.water.data.DriverOrderShow;
import ustc.sse.water.thread.RefreshDriverOrdersThread;
import ustc.sse.water.thread.ShowDriverOrderThread;
import ustc.sse.water.utils.ConstantKeep;
import ustc.sse.water.utils.DriverCustomDialog;
import ustc.sse.water.utils.ProgressDialogUtil;
import ustc.sse.water.utils.ToastUtil;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 *
 * 驾驶员的订单信息类. <br>
 * 展示驾驶员的正在进行的订单和历史订单.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:38:10
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DriverOrderInfo extends Activity implements OnItemClickListener {
	private ListView listView; // 列表
	private ProgressBar progressBar; // 环形进度条
	private DriverOrderAdapter adapter; // 适配器
	private ActionBar ab; // ActionBar
	private List<DriverOrderShow> driverOrders; // 驾驶员的订单信息
	private SharedPreferences sp;
	private int driverId; // 操作驾驶员的id
	private ProgressDialogUtil progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 修改ActionBar样式
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(R.string.driver_order_information);
		ab.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.user_button_register_normal));

		driverOrders = ConstantKeep.dos; // 获取本地保存的驾驶员订单信息
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		driverId = sp.getInt("driverLoginId", 0);

		setContentView(R.layout.driver_order_info);
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		listView = (ListView) findViewById(R.id.listview_order_info);
		listView.setOnItemClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.progressBar_waiting);
		progressDialog = new ProgressDialogUtil(this, "正在刷新...");

		if (driverOrders != null) {
			adapter = new DriverOrderAdapter(this, driverOrders);
			listView.setAdapter(adapter);
			listView.setVisibility(View.VISIBLE);
		} else if (driverId != 0) {
			// 如果显示环形进度条等待刷新
			progressBar.setVisibility(View.VISIBLE);
			// 开启线程访问服务器获取订单数据
			ShowDriverOrderThread showOrder = new ShowDriverOrderThread(h, "1",
					String.valueOf(driverId));
			showOrder.start();
		} else {
			ToastUtil.show(this, "无效驾驶员");
		}

	}

	Handler h = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 11: // 获取全部订单数据成功
				progressDialog.dissmissProgressDialog();
				// 重新获取本地保存的驾驶员订单信息
				driverOrders = ConstantKeep.dos;
				adapter.notifyDataSetChanged();
				break;
			case 22: // 获取全部订单数据成功
				// 重新获取本地保存的驾驶员订单信息
				driverOrders = ConstantKeep.dos;
				adapter = new DriverOrderAdapter(DriverOrderInfo.this,
						driverOrders);
				listView.setAdapter(adapter);
				listView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				break;
			case 33: // 获取失败
				progressDialog.dissmissProgressDialog();
				ToastUtil.show(DriverOrderInfo.this, "没有订单数据");
				progressBar.setVisibility(View.GONE);
				break;
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 获取选择的订单
		DriverOrderShow order = driverOrders.get(position);
		new DriverCustomDialog(this, String.valueOf(order.getParkNumber()),
				order.getOrderDetail(), order.getAdminPhone(),
				order.getParkAddress());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.driver_order_refresh, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // ActionBar中向左箭头点击
			finish(); // 返回主菜单
			break;
		case R.id.driver_order_list_refresh:
			// 开启线程刷新订单数据
			RefreshDriverOrdersThread refresh = new RefreshDriverOrdersThread(
					h, "1", String.valueOf(driverId));
			refresh.start();
			progressDialog.showProgressDialog();
			break;
		}
		return true;
	}
}
