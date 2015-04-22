package ustc.sse.water.adapter;

import java.util.ArrayList;
import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.AdminOrderShow;
import ustc.sse.water.thread.UpdateAdminOrderThread;
import ustc.sse.water.utils.ConstantKeep;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 *
 * 订单适配器，用于显示正在进行的订单和已完成订单. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015-3-3 下午4:22:28
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 *
 * @author 张芳,周晶鑫修改
 * @version 4.0.0
 */
public class OrderStateProcessAdapter extends BaseAdapter {

	private List<AdminOrderShow> list;// 信息列表
	private Context context;// 上下文
	private LayoutInflater inflater; // 填充器

	public OrderStateProcessAdapter(Context context, List<AdminOrderShow> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (list != null) {
			// 列表的元素个数
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 每个元素的初始化
		ViewHolder vh;
		AdminOrderShow order = new AdminOrderShow();
		if (list != null) {
			order = list.get(position);
		}
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.finishedorder, null);
			vh.car_number = (TextView) convertView
					.findViewById(R.id.car_number);
			vh.order_number = (TextView) convertView
					.findViewById(R.id.order_number);
			vh.order_time = (TextView) convertView
					.findViewById(R.id.orderring_time);
			vh.money = (TextView) convertView.findViewById(R.id.money);
			vh.complete_order = (Button) convertView
					.findViewById(R.id.complete_order);
			vh.complete_order.setOnClickListener(new BookOnClickListener(order,
					position));
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (order != null) {
			vh.car_number.setText(order.getDriverLicence()); // 显示车牌号
			vh.order_number.setText(String.valueOf(order.getParkNumber())+"个"); // 显示预定的数量
			vh.order_time.setText(order.getOrderDate()); // 显示预定时间
			vh.money.setText(order.getOrderPrice()+"元");// 显示预定金额
			if (order.getOrderStatus() == 2) {
				vh.complete_order.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		public TextView car_number; // 车牌号
		public TextView order_number;// 预定数量
		public TextView order_time;// 预定时间
		public TextView money; // 预定金额
		public Button complete_order; // 确定按钮
	}

	/* 订单处理 */
	class BookOnClickListener implements OnClickListener {
		int index;

		public BookOnClickListener(AdminOrderShow current, int position) {
			index = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.complete_order:
				Dialog dialog = new AlertDialog.Builder(context)
						.setTitle("提交提示")
						.setMessage("您确定要完成订单吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// 完成订单
										AdminOrderShow order = list.get(index);
										order.setOrderStatus(2);
										list.remove(index); // 在正在进行中删除
										if (ConstantKeep.aosDown == null) {
											ConstantKeep.aosDown = new ArrayList<AdminOrderShow>();
										}
										ConstantKeep.aosDown.add(order); // 加入已完成中
										OrderStateProcessAdapter.this
												.notifyDataSetChanged();
										// 开启线程修改服务器数据库中的订单状态
										UpdateAdminOrderThread uaot = new UpdateAdminOrderThread(
												order.getOrderId());
										uaot.start();

										arg0.dismiss();
									}
								})
						.setNeutralButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										arg0.dismiss();
									}
								}).create();
				dialog.show();
				break;
			}
		}

	}

}
