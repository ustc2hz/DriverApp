package ustc.sse.water.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.service.GetCurrentBookNumber;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * @version 2.0.0
 */
public class ParkingInfoFragment extends Fragment {

	// 方便数据传递，之后更改——黄志恒
	public static String[] tranMoney = new String[3];
	/* 模拟数据 */
	private String[] bookInfos = { "提前20分钟预定的订金： ", "提前40分钟预定的订金： ",
			"提前60分钟预定的订金： ", "停车半个小时收费： ", "停车一个小时收费： ", "超过一个小时收费： " };
	// 存储停车场收费详细信息——黄志恒
	public static String[] bookMoneys = new String[6];
	// 用来存放生成订单所需的基本信息
	public static String[] orderMessages = new String[2];

	private List<String> list; // 收费信息
	private Context context; // 上下文
	private Map<String, Object> parking; // 停车场
	private TextView parkingName; // 停车场名字
	private TextView parkingDistance; // 停车距离
	private TextView parKingNum;// 停车场车位数
	private TextView currentNum;// 停车场车位数
	private TextView parKingPhone;// 停车场电话
	private TextView parkOrder20; // 预定20分钟
	private TextView parkOrder40; // 预定40分钟
	private TextView parkOrder60; // 预定60分钟
	private TextView parkStop30; // 停车30分钟
	private TextView parkStop60; // 停车60分钟
	private TextView parkStopMore; // 停车超过60分钟
	CurrentNumListener cnl;

	public ParkingInfoFragment(Context con, Map<String, Object> park) {
		this.context = con;
		this.parking = park;
		initBR(); // 开启广播
	}

	/**
	 * 读取停车场的预定信息数据，放入List中
	 * 
	 * @return List<String> 数据列表
	 */
	public List<String> getData() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < bookInfos.length && i < bookMoneys.length; i++) {
			String str = bookInfos[i] + bookMoneys[i] + "元";
			list.add(str);
		}
		return list;
	}

	/**
	 * 初始化收费信息
	 */
	private void initTranMoney() {
		tranMoney[0] = "提前20分钟预定收取：" + bookMoneys[0] + "元";
		tranMoney[1] = "提前40分钟预定收取：" + bookMoneys[1] + "元";
		tranMoney[2] = "提前60分钟预定收取：" + bookMoneys[2] + "元";
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bookMoneys = (String[]) parking.get("bookMoney");
		initTranMoney();
		orderMessages[0] = (String) parking.get("parkingName");
		orderMessages[1] = (String) parking.get("parkingAddress");
		list = getData();
		
		// 开启服务，为Fragment第一次创建使用
		Intent numService = new Intent(context, GetCurrentBookNumber.class);
		numService.putExtra("parking_managerId",
				(String) parking.get("managerId"));
		context.startService(numService);
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
		parKingPhone = (TextView) view.findViewById(R.id.parking_info_phone);
		currentNum = (TextView) view.findViewById(R.id.text_current_number);

		parkingDistance = (TextView) view
				.findViewById(R.id.parking_info_distance_show);
		parKingNum = (TextView) view.findViewById(R.id.text_book_number);
		parkingName.setText((String) parking.get("parkingName"));
		parKingPhone.setText((String) parking.get("phone"));
		parkingDistance.setText(parking.get("parkingDistance").toString()
				+ " 米");
		parKingNum.setText((String) parking.get("parkingSum") + " 个");

		// 各种收费信息
		parkOrder20 = (TextView) view.findViewById(R.id.text_parking_order_20);
		parkOrder40 = (TextView) view.findViewById(R.id.text_parking_order_40);
		parkOrder60 = (TextView) view.findViewById(R.id.text_parking_order_60);
		parkStop30 = (TextView) view.findViewById(R.id.text_parking_stop_30);
		parkStop60 = (TextView) view.findViewById(R.id.text_parking_stop_60);
		parkStopMore = (TextView) view
				.findViewById(R.id.text_parking_stop_more);
		parkOrder20.setText(list.get(0));
		parkOrder40.setText(list.get(1));
		parkOrder60.setText(list.get(2));
		parkStop30.setText(list.get(3));
		parkStop60.setText(list.get(4));
		parkStopMore.setText(list.get(5));

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		context.stopService(new Intent(context, GetCurrentBookNumber.class));
		context.unregisterReceiver(cnl);
	}

	// 初始化广播接收器
	public void initBR() {
		cnl = new CurrentNumListener();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.action.br.current_number");
		context.registerReceiver(cnl, filter);
	}

	class CurrentNumListener extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String current = intent.getStringExtra("current_parking_number");
			if (!"error".equals(current)) {
				ParkingBookFragment.leftNumber = Integer.parseInt(current);
				currentNum.setText(current + " 个");
			} else {
				currentNum.setText("暂无数据");
			}
		}
		
	}

}
