package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.adapter.zjx.DriverOrderAdapter;
import ustc.sse.water.utils.zjx.CustomDialog;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * 驾驶员的订单信息类. <br>
 * 展示驾驶员的正在进行的订单和历史订单.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:38:10
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DriverOrderInfo extends Activity implements OnItemClickListener {
	private ListView listView; // 列表
	private DriverOrderAdapter adapter; // 适配器
	List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_order_info);
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		adapter = new DriverOrderAdapter(this, getData());
		listView = (ListView) findViewById(R.id.listview_order_info);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	/**
	 * 获取订单数据
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getData() {
		list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("basicInfo", "2014-12-1" + i + "预定停车场" + i);
			if (i % 2 == 0) {
				map.put("status", "正在进行");
			} else {
				map.put("status", "已完成");
			}
			list.add(map);
		}
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String title;
		String content;
		boolean flag;
		// ListView单元点击事件
		if (list != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map = list.get(position);
			content = (String) map.get("basicInfo");
			if ("正在进行".equals(map.get("status"))) {
				flag = true;
			} else {
				flag = false;
			}
			CustomDialog cd = new CustomDialog(this, "预定信息", content, flag);

		}

	}
}
