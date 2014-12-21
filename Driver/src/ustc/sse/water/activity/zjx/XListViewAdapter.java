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
public class XListViewAdapter extends BaseAdapter implements OnClickListener {
	private List<String> list;// 信息列表
	private Context context; // 上下文
	private LayoutInflater inflater; // 填充器

	public XListViewAdapter(Context con, List<String> list) {
		this.context = con;
		this.list = list;
		this.inflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount() {
		// 列表的元素个数
		return list.size();
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

	/* 缓冲类 */
	class ViewHolder {
		public TextView name; // 停车场名字
		public TextView distance;// 停车场距离
		public TextView address;// 停车场地址
		public Button btn1; // 路线按钮
		public Button btn2; // 预定按钮
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
			Intent intent = new Intent(context, ParkingInfo.class);
			context.startActivity(intent);
			break;
		}

	}

}
