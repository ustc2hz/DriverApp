package ustc.sse.water.adapter.zjx;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.activity.R.color;
import ustc.sse.water.activity.zjx.ParkingDetail;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 自定义ListView的适配器. <br>
 * ListView中的元素.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:54:37
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class XListViewAdapter extends BaseAdapter {
	private List<Map<String, Object>> list;// 信息列表
	private Context context; // 上下文
	private LayoutInflater inflater; // 填充器

	public XListViewAdapter(Context con, List<Map<String, Object>> list) {
		this.context = con;
		this.list = list;
		this.inflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount(){
		if (list != null){
			// 列表的元素个数
			return list.size();
		} else {
			return 0;
		}
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
		// 每个元素的初始化
		ViewHolder vh;
		Map<String, Object> parking = null;
		if (list != null) {
			parking = list.get(position);
		}
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.xlistview_cell, null);
			vh.name = (TextView) convertView
					.findViewById(R.id.text_parking_name);
			vh.distance = (TextView) convertView
					.findViewById(R.id.text_parking_distance);
			vh.address = (TextView) convertView
					.findViewById(R.id.text_parking_address);
			vh.btnBook = (Button) convertView
					.findViewById(R.id.button_parking_order);
			vh.btnBook.setOnClickListener(new BookOnClickListener(parking));
			vh.btnRoot = (Button) convertView
					.findViewById(R.id.button_parking_route);
			// vh.btnRoot.setOnClickListener(this);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (parking != null) {
			vh.name.setText((String) parking.get("parkingName")); // 显示停车场名字
			vh.address.setText((String) parking.get("parkingAddress")); // 显示停车场的地址
			vh.distance.setText(parking.get("parkingDistance").toString()); // 显示停车场的距离
		}
		// 如果是高德的停车场，则不可以预定
		if("AMapPark".equals(parking.get("isAmap"))) {
			vh.btnBook.setText("无预定信息");
			vh.btnBook.setClickable(false);
			vh.btnBook.setTextColor(color.black);
		} else if("AMapCloudPark".equals(parking.get("isAmap"))) {
			vh.btnBook.setClickable(true);
			
		}

		return convertView;
	}

	/* 缓冲类 */
	class ViewHolder {
		public TextView name; // 停车场名字
		public TextView distance;// 停车场距离
		public TextView address;// 停车场地址
		public Button btnRoot; // 路线按钮
		public Button btnBook; // 预定按钮
	}

	/* 可预定停车场预定处理 */
	class BookOnClickListener implements OnClickListener {
		Map<String, Object> selectParking; // 选中的停车场

		public BookOnClickListener(Map<String, Object> selectP) {
			this.selectParking = selectP;
		}

		/**
		 * 按钮点击事件处理
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_parking_route:// 路线按钮
				break;
			case R.id.button_parking_order:// 预定按钮
				Intent intent = new Intent(context, ParkingDetail.class);
				// 将选中的停车场封装到Intent中
				intent.putExtra("select_parking", (Serializable) selectParking);
				context.startActivity(intent);
				break;
			}
		}
	}
}
