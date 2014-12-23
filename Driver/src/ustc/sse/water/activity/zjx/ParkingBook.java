package ustc.sse.water.activity.zjx;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 
 * 停车场预定类. <br>
 * 驾驶员预定停车场，选择车牌号，选择时间.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:39:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingBook extends Activity implements OnClickListener,
		OnItemSelectedListener {
	/* 模拟车牌号和预定时间 */
	private String[] license = { "苏1234567", "苏666666", "苏888888" };
	private String[] time = { "30分钟10元", "1小时20元", "90分钟30元", "2小时40元" };
	private Button submitOrder;// 提交订单按钮
	private Button cancelOrder;// 取消订单按钮
	private ListView listView;// 车牌号的列表
	private Spinner spinner; // 预定时间下拉框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide(); // 隐藏ActionBar
		setContentView(R.layout.parking_book);
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	public void initView() {
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, license);
		listView = (ListView) findViewById(R.id.listview_license_chose);
		listView.setAdapter(listViewAdapter);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, time);
		spinner = (Spinner) findViewById(R.id.spinner_time_chose);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(this);
		submitOrder = (Button) findViewById(R.id.button_submit_order);
		cancelOrder = (Button) findViewById(R.id.button_cancel_order);
		submitOrder.setOnClickListener(this);
		cancelOrder.setOnClickListener(this);
	}

	/**
	 * 提交订单
	 */
	public void submit() {
		int count = listView.getCheckedItemCount(); // 选择的车牌号数
		Toast.makeText(this, "-->>" + count, 1).show(); // 测试
	}

	/**
	 * 视图点击事件的处理
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_submit_order:// 点击提交订单
			submit();
			break;
		case R.id.button_cancel_order: // 点击取消预定
			finish();
			break;
		}

	}

	/**
	 * 下拉框的选择
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String str = parent.getItemAtPosition(position).toString();
		Toast.makeText(this, str, 1).show();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
