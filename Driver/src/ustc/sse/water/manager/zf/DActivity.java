package ustc.sse.water.manager.zf;

import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.adapter.zjx.OrderStateProcessAdapter;
import ustc.sse.water.data.model.AdminOrderShow;
import ustc.sse.water.thread.ShowAdminOrderThread;
import ustc.sse.water.utils.zjx.AdminCustomDialog;
import ustc.sse.water.utils.zjx.ConstantKeep;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * 已完成订单类. <br>
 * 已完成订单列表的详细信息.
 * <p>
 * Copyright: Copyright (c) 2015-3-7 下午12:03:22
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@mail.ustc.edu.cn
 * 
 * @version 3.0.0
 */
public class DActivity extends Activity implements OnItemClickListener {
	private List<AdminOrderShow> aosDown = null; // 已经完成的订单
	private ListView listView;
	private TextView textView;
	private OrderStateProcessAdapter myAdapter;
	private SharedPreferences sp;
	private int adminId; // 停车场管理员的id

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_order_list);

		// 绑定Layout里面的ListView
		listView = (ListView) findViewById(R.id.orderlist);
		listView.setOnItemClickListener(this);
		aosDown = ConstantKeep.aosDown;
		textView = (TextView) findViewById(R.id.text_admin_no_order_tip);
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		adminId = sp.getInt("adminLoginId", 0);

		if (aosDown != null) {
			textView.setVisibility(View.GONE);
			myAdapter = new OrderStateProcessAdapter(DActivity.this, aosDown);
			listView.setAdapter(myAdapter);
		} else if (adminId != 0) {
			// 开启线程访问服务器获取订单数据
			ShowAdminOrderThread showOrder = new ShowAdminOrderThread(h, "2",
					String.valueOf(adminId));
			showOrder.start();
		} else {
			ToastUtil.show(this, "无效管理员");
		}

	}

	Handler h = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 44:// 获取成功
				// 重新获取数据加载
				aosDown = ConstantKeep.aosDown;
				if(aosDown != null) {
					textView.setVisibility(View.GONE);
				}
				myAdapter = new OrderStateProcessAdapter(DActivity.this, aosDown);
				listView.setAdapter(myAdapter);
				break;
			case 55:// 获取失败
				ToastUtil.show(DActivity.this, "获取失败");
				break;
			case 66:// 获取失败
				ToastUtil.show(DActivity.this, "没有订单数据");
				break;
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AdminOrderShow order = aosDown.get(position);
		new AdminCustomDialog(DActivity.this, order.getOrderDetail(), order.getDriverPhone());

	}
}
