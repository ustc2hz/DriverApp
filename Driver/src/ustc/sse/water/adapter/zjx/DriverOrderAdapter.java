package ustc.sse.water.adapter.zjx;

import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DriverOrderAdapter extends BaseAdapter {
	private Context context; // 上下文
	private LayoutInflater inflater; // 填充器
	private List<Map<String, Object>> list; // 订单列表

	public DriverOrderAdapter(Context con, List<Map<String, Object>> list) {
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
			vh.basicInfo = (TextView) convertView
					.findViewById(R.id.text_driver_order_basic_info);
			vh.orderStatus = (TextView) convertView
					.findViewById(R.id.text_driver_order_status);
			convertView.setTag(vh);// 加入标签
		} else {
			vh = (ViewHolder) convertView.getTag();// 取出标签
		}
		if (list != null) {
			Map<String, Object> map = list.get(position);
			// 赋值
			vh.basicInfo.setText((String) map.get("basicInfo"));
			vh.orderStatus.setText((String) map.get("status"));
		}

		return convertView;
	}

	final class ViewHolder {
		public TextView basicInfo;
		public TextView orderStatus;
	}

}
