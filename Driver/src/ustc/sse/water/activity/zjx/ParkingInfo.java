package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 
 * 停车场信息展示类. <br>
 * 展示某个停车的详细的信息.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:44:31
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingInfo extends Activity implements OnClickListener {
	private Button bookParking; // 预定按钮
	private Button back; // 返回按钮
	/* 模拟数据 */
	private String[] bookInfos = { "提前30分钟预定", "提前1小时预定", "提前90分钟预定", "提前2销售预定" };
	private String[] bookMoneys = { "10", "20", "30", "40" }; // just a simple

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.parking_infomation);
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	public void initView() {
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.listview_book_money, new String[] { "info", "money" },
				new int[] { R.id.book_time, R.id.book_money });
		ListView listView = (ListView) findViewById(R.id.listview_book_parking);
		listView.setAdapter(adapter);
		bookParking = (Button) findViewById(R.id.button_make_order);
		back = (Button) findViewById(R.id.button_back_listview);
		bookParking.setOnClickListener(this);
		back.setOnClickListener(this);
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

	/**
	 * 视图组件的点击事件处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_make_order: // 预定按钮
			Intent intent = new Intent(ParkingInfo.this, ParkingBook.class);
			startActivity(intent);
			break;
		case R.id.button_back_listview: // 返回按钮
			finish();
			break;
		}

	}

}
