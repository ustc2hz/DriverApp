package ustc.sse.water.activity.zjx;

import java.util.List;

import ustc.sse.water.activity.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class XListViewAdapter extends BaseAdapter implements OnClickListener {
	private List<String> list;
	private Context context;
	private LayoutInflater inflater;

	public XListViewAdapter(Context con, List<String> list) {
		this.context = con;
		this.list = list;
		this.inflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.xlistview_cell, null);
			vh.name = (TextView) convertView
					.findViewById(R.id.text_parking_name);
			vh.distance = (TextView) convertView
					.findViewById(R.id.text_parking_distance);
			vh.address = (TextView) convertView
					.findViewById(R.id.text_parking_address);
			vh.btn2 = (Button) convertView
					.findViewById(R.id.button_parking_order);
			vh.btn2.setOnClickListener(this);
			vh.btn1 = (Button) convertView
					.findViewById(R.id.button_parking_route);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.name.setText(list.get(position));

		return convertView;
	}

	class ViewHolder {
		public TextView name;
		public TextView distance;
		public TextView address;
		public Button btn1;
		public Button btn2;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_parking_route:
			break;
		case R.id.button_parking_order:
			Intent intent = new Intent(context, ParkingInfo.class);
			context.startActivity(intent);
			break;
		}

	}

}
