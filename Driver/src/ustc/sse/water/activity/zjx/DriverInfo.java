package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.HashMap;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 
 * 驾驶员的信息展示. <br>
 * 展示驾驶员的个人信息，驾驶员可以使用相关功能按钮，如修改信息、查看订单和二维码；驾驶员可以选择返回地图或者退出登录.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:33:07
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.0.0
 */
public class DriverInfo extends Activity implements OnItemClickListener ,OnClickListener {
	private final static int GV_UPATE_INFO = 0; // GridView中“修改信息”的position
	private final static int GV_CHECK_ORDER = 1; // “查看订单”的position
	private final static int GV_TWO_CODE = 2; // “二维码”的position
	// 导航图片
	private final int[] funImages = {R.drawable.tubiao1,R.drawable.tubiao5,R.drawable.tubiao4};
	// 导航文字
	private final String[] funTexts = {"修改信息","查看订单","二维码"};
	private GridView gridView; // 导航
	private Button logout;// 退出登录按钮
	private Button backMap; // 返回到地图
	private TextView textLicence; // 车牌号
	private TextView textPhone; // 电话
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide(); // 隐藏ActionBar
		setContentView(R.layout.driver_info);
		// 获取只能被本应用程序读、写的SharedPreference对象
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	public void initView() {
		// 初始化TextView
		textLicence = (TextView)findViewById(R.id.text_driver_licence);
		textPhone = (TextView)findViewById(R.id.text_driver_phone);
		// 取出SP中的数据
		String spDriverLicence = sp.getString("driverLoginLicence", "没有驾驶员登录...");
		String spDriverPhone = sp.getString("driverLoginPhone", "暂无联系电话...");
		// 赋值
		textLicence.setText(spDriverLicence);
		textPhone.setText(spDriverPhone);
		
		// GridView初始化
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
		
		// 按钮初始化
		backMap = (Button) findViewById(R.id.button_backToMap);
		backMap.setOnClickListener(this);
		logout = (Button) findViewById(R.id.button_logout);
		logout.setOnClickListener(this);
	}

	/**
	 * 视图点击事件处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_backToMap: // 返回地图
			finish();
			break;
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
		Intent intent = new Intent();
		switch(position) {
		case GV_UPATE_INFO: //修改信息
			intent.setClass(DriverInfo.this, UpdateDriverInfo.class);
			break;
		case GV_CHECK_ORDER: // 查看订单
			intent.setClass(DriverInfo.this, DriverOrderInfo.class);
			break;
		case GV_TWO_CODE: // 二维码
			
			break;
		}
		startActivity(intent);
	}
	
	@Override
	protected void onResume() {
		// 从“修改信息”界面返回时，刷新联系电话
		textLicence.setText(sp.getString("driverLoginLicence", "没有驾驶员登录..."));
		textPhone.setText(sp.getString("driverLoginPhone", "没有联系电话..."));
		super.onResume();
	}

}
