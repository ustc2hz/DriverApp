package ustc.sse.water.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 
 * 停车场信息Fragment. <br>
 * 显示停车场的详细信息.
 * <p>
 * Copyright: Copyright (c) 2015-1-30 下午5:26:25
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingInfoFragment extends Fragment {
	private Context context; // 上下文
	private Map<String, Object> parking; // 停车场
	private TextView parkingName; // 停车场名
	private TextView parkingAdress; // 停车场地址
	private TextView parkingDistance; // 停车距离
	/* 模拟数据 */
	private String[] bookInfos = { "提前30分钟预定", "提前1小时预定", "提前90分钟预定", "提前2小时预定" };
	private String[] bookMoneys = { "10", "20", "30", "40" }; // just a simple

	public ParkingInfoFragment(Context con, Map<String, Object> park) {
		this.context = con;
		this.parking = park;
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 视图填充
		View view = inflater.inflate(R.layout.parking_infomation, container,
				false);
		parkingName = (TextView) view.findViewById(R.id.text_info_title);
		parkingAdress = (TextView) view
				.findViewById(R.id.parking_detail_address_show);
		parkingDistance = (TextView) view
				.findViewById(R.id.parking_info_distance_show);
		parkingName.setText((String) parking.get("parkingName"));
		parkingAdress.setText((String) parking.get("parkingAddress"));
		parkingDistance.setText(parking.get("parkingDistance").toString());
		SimpleAdapter adapter = new SimpleAdapter(context, getData(),
				R.layout.listview_book_money, new String[] { "info", "money" },
				new int[] { R.id.book_time, R.id.book_money });
		ListView listView = (ListView) view
				.findViewById(R.id.listview_book_parking);
		listView.setAdapter(adapter);
		return view;
	}

	/**
	 * 读取停车场的预定信息数据，放入List中
	 * 
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> getData() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < bookInfos.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("info", bookInfos[i]);
			map.put("money", bookMoneys[i]);
			list.add(map);
		}
		return list;
	}
}
