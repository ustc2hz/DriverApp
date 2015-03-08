package ustc.sse.water.adapter.zjx;

import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.activity.R.color;
import ustc.sse.water.adapter.zjx.XListViewAdapter.BookOnClickListener;
import ustc.sse.water.adapter.zjx.XListViewAdapter.ViewHolder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
 * @张芳
 * @version 3.0.0
 */
public class OrderStateProcessingAdapter extends BaseAdapter{
	
	private List<Map<String, Object>> list;// 信息列表
	private Context context;//上下文
	private LayoutInflater inflater; // 填充器
	
	public OrderStateProcessingAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null){
			// 列表的元素个数
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
			// 每个元素的初始化
				ViewHolder vh;
				Map<String, Object> order = null;
				if (list != null) {
					order = list.get(position);
				}
				if (convertView == null) {
					vh = new ViewHolder();
					convertView = inflater.inflate(R.layout.driver_finished_order2, null);
					vh.car_number = (TextView) convertView
							.findViewById(R.id.car_number);
					vh.order_number   = (TextView) convertView
							.findViewById(R.id.order_number);
					vh.order_time = (TextView) convertView
							.findViewById(R.id.order_time);
					vh.money = (TextView) convertView
							.findViewById(R.id.money);
					vh.complete_order.setOnClickListener(new BookOnClickListener(order));
					convertView.setTag(vh);
				} else {
					vh = (ViewHolder) convertView.getTag();
				}

				if (order != null) {
					vh.car_number.setText((String) order.get("parkingName")); // 显示车牌号
					vh.order_number.setText((String) order.get("parkingAddress")); // 显示停车场的地址
					vh.order_time.setText(order.get("parkingDistance").toString()); // 显示停车场的距离
					vh.money.setText((String)order.get("money"));
				}
				return convertView;
	}
	
	class ViewHolder {
		public TextView car_number; // 车牌号
		public TextView order_number;// 预定数量
		public TextView order_time;// 预定时间
		public TextView money; //预定金额
		public Button complete_order; // 确定按钮
	}
	

	/* 订单处理 */
	class BookOnClickListener implements OnClickListener {
		
		
		Map<String, Object> current_order; // 选中的订单

		public BookOnClickListener(Map<String, Object> currentO) {
			this.current_order = currentO;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.complete_order:
			}
		}

	private View.OnClickListener onclick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Dialog dialog = new AlertDialog.Builder(context)
			.setTitle("提交提示")
			.setMessage("您确定要提交订单吗？")
			.setPositiveButton("确定", 
					new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
							
						}
					})
			.setNeutralButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
						}
					})
			.create();
			dialog.show();
		}
	};
}
}
