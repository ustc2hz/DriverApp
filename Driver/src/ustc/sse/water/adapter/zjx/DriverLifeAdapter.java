package ustc.sse.water.adapter.zjx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 自定义Adapter类. <br>
 * 继承BaseAdapter，是车生活的GridView的Adapter类.
 * <p>
 * Copyright: Copyright (c) 2014-11-26 下午4:15:33
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.2.0
 */
public class DriverLifeAdapter extends BaseAdapter {
	/* 缓存类 */
	final class ViewHolder {
		public ImageView image; // 图片
		public TextView text; // 文字
	}

	/* 上下文 */
	private Context context;

	/* 布局填充器 */
	private LayoutInflater inflater;

	/**
	 * 有参构造函数
	 * @param con 上下文
	 */
	public DriverLifeAdapter(Context con) {
		this.context = con;
		this.inflater = LayoutInflater.from(con);
	}

	@Override
	public int getCount() {
		// GridView的元素个数
		return getDate().size();
	}

	/**
	 * 获取数据资源（图片和文字）
	 * @return Map封装的数据集合
	 */
	private List<Map<String, Object>> getDate() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 数字存储文字
		int[] textIds = { R.string.gas_station, R.string.car_repair,
				R.string.car_wash, R.string.car_rent };
		// 数组存储图片
		int[] imageIds = { R.drawable.gas_station_fb, R.drawable.car_repair_fb,
				R.drawable.car_wash_fb, R.drawable.car_rent3_fb };
		// 用Map封装图片和文字
		for (int i = 0; i < textIds.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			map.put("text", textIds[i]);
			list.add(map);
		}
		return list;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 核心方法，返回GridView每个单元显示的View
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder vh; // 缓冲类
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.driver_life_cell, null);
			vh.image = (ImageView) convertView
					.findViewById(R.id.image_driver_life_cell);
			vh.text = (TextView) convertView
					.findViewById(R.id.text_driver_life_cell);
			convertView.setTag(vh);// 加入标签
		} else {
			vh = (ViewHolder) convertView.getTag();// 取出标签
		}
		
		// 赋值
		vh.image.setImageResource((Integer) getDate().get(position)
				.get("image"));
		vh.text.setText((Integer) getDate().get(position).get("text"));
		
		return convertView;
	}

}
