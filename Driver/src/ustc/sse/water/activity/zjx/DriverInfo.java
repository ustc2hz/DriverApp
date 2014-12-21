package ustc.sse.water.activity.zjx;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

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
public class DriverInfo extends Activity implements OnClickListener {
	private ListView listView; // 车牌号的列表
	private ImageView addLicence; // 添加车牌号的图片
	private ImageView checkOrder; // 查看订单的图片
	private ImageView twoCode; // 二维码的图片
	private Button backMap; // 返回地图按钮
	private Button logout;// 退出登录按钮
	// 模拟车牌号
	private String licences[] = { "111111", "222222", "333333" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_info);
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	public void initView() {
		listView = (ListView) findViewById(R.id.listview_license_show);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, licences);
		listView.setAdapter(adapter);
		addLicence = (ImageView) findViewById(R.id.ibtn_add_licence);
		checkOrder = (ImageView) findViewById(R.id.ibtn_check_order);
		twoCode = (ImageView) findViewById(R.id.ibtn_two__code);
		backMap = (Button) findViewById(R.id.button_back_map);
		logout = (Button) findViewById(R.id.button_logout);
		addLicence.setOnClickListener(this);
		checkOrder.setOnClickListener(this);
		twoCode.setOnClickListener(this);
		backMap.setOnClickListener(this);
		logout.setOnClickListener(this);
	}

	/**
	 * 视图点击事件处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_add_licence: // 点击添加车牌号
			Intent intent1 = new Intent(this, AddDriverLicence.class);
			startActivity(intent1);
			break;
		case R.id.ibtn_check_order: // 点击查看订单
			Intent intent2 = new Intent(this, DriverOrderInfo.class);
			startActivity(intent2);
			break;
		case R.id.ibtn_two__code: // 点击二维码
			// Intent intent3 = new Intent(this,);
			break;
		case R.id.button_back_map: // 点击返回地图
			finish();
			break;
		case R.id.button_logout: // 点击退出登录

			break;
		}

	}

}
