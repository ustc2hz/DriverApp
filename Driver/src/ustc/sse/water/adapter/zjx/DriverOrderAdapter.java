package ustc.sse.water.adapter.zjx;

import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.DriverOrderShow;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DriverOrderAdapter extends BaseAdapter {
	private Context context; // 上下文
	private LayoutInflater inflater; // 填充器
	private List<DriverOrderShow> list; // 订单列表

	public DriverOrderAdapter(Context con,List<DriverOrderShow> list) {
		this.context = con;
		this.inflater = LayoutInflater.from(con);
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater
					.inflate(R.layout.listview_driver_order, null);
			vh.parkName = (TextView) convertView
					.findViewById(R.id.text_driver_order_parking_name);
			vh.orderPrice = (TextView) convertView
					.findViewById(R.id.text_driver_order_pay);
			vh.orderDate = (TextView) convertView
					.findViewById(R.id.text_driver_order_date);
			vh.orderStatus = (TextView) convertView
					.findViewById(R.id.text_driver_order_status);
			convertView.setTag(vh);// 加入标签
		} else {
			vh = (ViewHolder) convertView.getTag();// 取出标签
		}
		if (list != null) {
			DriverOrderShow driverOrder = list.get(position);
			// 赋值
			vh.parkName.setText(driverOrder.getParkName());
			String sumPrice = "预付："+driverOrder.getOrderPrice()+"元";
			vh.orderPrice.setText(sumPrice);
			String date = "日期："+driverOrder.getOrderDate();
			vh.orderDate.setText(date);
			String status = "正在进行中";
			if(driverOrder.getOrderStatus() == 2) {
				status = "已完成";
			}
			vh.orderStatus.setText(status);
		}

		return convertView;
	}

	final class ViewHolder {
		public TextView parkName; // 停车场名
		public TextView orderPrice; // 预付总金
		public TextView orderDate; // 订单日期
		public TextView orderStatus; // 订单日期
	}

}
