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
 * 订单界面、默认显示当前正在进行的订单. <br>
 * 订单界面类.
 * <p>
 * Copyright: Copyright (c) 2015-03-01 下午10:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author张芳 sa614296@mail.ustc.edu.cn
 * @version 3.0.0
 */
public class AActivity extends Activity implements OnItemClickListener{
	private ListView listView;
	private TextView textView;
	private OrderStateProcessAdapter myAdapter;
	private List<AdminOrderShow> aosIng = null; // 正在进行的订单
	private SharedPreferences sp;
	private int adminId; // 停车场管理员的id

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_order_list);

		// 绑定Layout里面的ListView
		listView = (ListView) findViewById(R.id.orderlist);
		listView.setOnItemClickListener(this);
		textView = (TextView) findViewById(R.id.text_admin_no_order_tip);
		aosIng = ConstantKeep.aosIng;
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		adminId = sp.getInt("adminLoginId", 0);

		if (aosIng != null) {
			textView.setVisibility(View.GONE);
			myAdapter = new OrderStateProcessAdapter(AActivity.this, aosIng);
			listView.setAdapter(myAdapter);
		} else if (adminId != 0) {
			// 开启线程访问服务器获取订单数据
			ShowAdminOrderThread showOrderIng = new ShowAdminOrderThread(h, "1",
					String.valueOf(adminId));
			showOrderIng.start();
			// 开启线程访问服务器获取订单数据
			ShowAdminOrderThread showOrderDown = new ShowAdminOrderThread(h, "2",
					String.valueOf(adminId));
			showOrderDown.start();
		} else {
			ToastUtil.show(this, "无效管理员");
		}

	}

	Handler h = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.arg1) {
			case 44:// 获取成功
				// 重新获取数据加载
				aosIng = ConstantKeep.aosIng;
				if(aosIng != null) {
					textView.setVisibility(View.GONE);
				}
				myAdapter = new OrderStateProcessAdapter(AActivity.this, aosIng);
				listView.setAdapter(myAdapter);
				break;
			case 55:// 获取失败
				ToastUtil.show(AActivity.this, "获取失败");
				break;
			case 66:// 获取失败
				ToastUtil.show(AActivity.this, "没有订单数据");
				break;
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AdminOrderShow order = aosIng.get(position);
		new AdminCustomDialog(AActivity.this, order.getOrderDetail(), order.getDriverPhone());
	}}
