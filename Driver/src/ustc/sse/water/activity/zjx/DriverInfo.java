package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.HashMap;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

/**
 * 
 * 驾驶员的信息展示. <br>
 * 展示驾驶员的个人信息，驾驶员可以使用相关功能按钮，如添加车牌号、查看订单和二维码.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:33:07
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class DriverInfo extends Activity implements OnItemClickListener ,OnClickListener {
	private final int[] funImages = {R.drawable.happy,R.drawable.happy};
	private final String[] funTexts = {"查看订单","二维码"};
	private GridView gridView;
	private Button logout;// 退出登录按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.driver_info);
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	public void initView() {
		gridView = (GridView)findViewById(R.id.gridView_driver_functions);
		// 初始化数据源
		ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < funImages.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("imgItem", funImages[i]);
			map.put("txtItem", funTexts[i]);
			items.add(map);
		}

		/* 创建适配器 */
		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.driver_functions_item, new String[] { "imgItem",
						"txtItem" }, new int[] {
						R.id.imageView_driver_function_order,
						R.id.textView_driver_function_order });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		
		logout = (Button) findViewById(R.id.button_logout);
		logout.setOnClickListener(this);
	}

	/**
	 * 视图点击事件处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_logout: // 点击退出登录
			finish();
			break;
		}
	}

	/**
	 * GridView的Item点击事件处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

}
