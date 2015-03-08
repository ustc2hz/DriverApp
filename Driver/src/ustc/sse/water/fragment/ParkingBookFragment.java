package ustc.sse.water.fragment;

import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.Admin;
import ustc.sse.water.data.model.Driver;
import ustc.sse.water.data.model.Order;
import ustc.sse.water.thread.CheckOrderReceiveThread;
import ustc.sse.water.thread.SendOrderThread;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 
 * 车位预定ParkingBookFragment. <br>
 * 展示订单表单，让用户填写.
 * <p>
 * Copyright: Copyright (c) 2015-1-30 下午6:25:42
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.0.0
 */
public class ParkingBookFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener {
	public static ObjectMapper objectMapper = new ObjectMapper();
	private String[] parkingInfo = new String[2]; // 预定的停车场基本信息
	private String[] parkingMoney = new String[3]; // 预定的停车场收费
	private String selectBook = "";
	private static String orderUUID = ""; 
	private int selectIndex = 0; // 驾驶员选择的价格序号
	private Context context; // 上下文
	
	/**
	 * 处理发送订单的线程的返回结果
	 */
	Handler h = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			String result = "error";
			switch (msg.arg1) {
			case 1: // 发送订单到服务器
				result = msg.getData().getString("send_result");
				// 判断是否发送成功
				if ("success".equals(result)) {
					ToastUtil.show(context, "发送成功");
				} else { // 如果订单发送失败则预定失败
					ToastUtil.show(context, "发送失败");
				}
				break;
			case 2: // 发送请求判断对应的管理员是否已经接收到订单
				result = msg.getData().getString("check_result");
				Log.i("--->>>result check", result);
				// 判断是否接收成功
				if ("success".equals(result)) {
					// 只有管理员接收到订单后，预定才算成功
					ToastUtil.show(context, "管理员接收成功");
				} else { // 如果订单接收失败，则预定失败
					ToastUtil.show(context, "管理员接收失败");
				}
				break;
			}
		};
	};
	
	private Spinner spinner; // 预定时间下拉框
	private Button submitOrder;// 提交订单按钮
	private EditText driverNumber; // 预定车位个数
	private String[] time = new String[3];

	public ParkingBookFragment(Context con) {
		this.context = con;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_submit_order:// 点击提交订单
			if(driverNumber == null || "".equals(driverNumber.getText().toString())){
				ToastUtil.show(context, "请填写预定车位数！");
			}else {
				Order order = createOrder();
				String jacksonString = "";
				try {
					jacksonString = objectMapper.writeValueAsString(order);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 发送订单到服务器
				SendOrderThread st = new SendOrderThread(h, jacksonString);
				st.start();
				Log.i("-->>uuid", orderUUID);
				// 查看刚刚发送的订单是否被管理员接收到
				CheckOrderReceiveThread crt = new CheckOrderReceiveThread(h,orderUUID);
				crt.start();
			}
			
			break;
		}
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		time = ParkingInfoFragment.tranMoney;
		parkingInfo = ParkingInfoFragment.orderMessages;
		parkingMoney = ParkingInfoFragment.bookMoneys;

	}

	/**
	 * 初始化视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 视图填充
		View view = inflater.inflate(R.layout.parking_book, container, false);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item, time);
		driverNumber = (EditText) view.findViewById(R.id.edit_licenses_number);
		spinner = (Spinner) view.findViewById(R.id.spinner_time_chose);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(this);
		submitOrder = (Button) view.findViewById(R.id.button_submit_order);
		submitOrder.setOnClickListener(this);
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		selectBook = parent.getItemAtPosition(position).toString();
		selectIndex = position;
		Toast.makeText(context, selectBook, 1).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	// 模拟一个订单
	private Order createOrder() {
		Order order = new Order();
		orderUUID = order.getUuid();
		order.setParkName(parkingInfo[0]);
		order.setParkAddress(parkingInfo[1]);
		// 获取填写的数量
		int selectNum = Integer.parseInt(driverNumber.getText().toString());
		order.setDriverNum(selectNum);
		// 生存订单日期
		String orderDate = (DateFormat.format("yyyy-MM-dd", new Date())).toString();
		order.setOrderDate(orderDate);
		order.setOrderInfo(selectBook);
		// 计算总价
		String selectPrice = parkingMoney[selectIndex];
		int sumPrice = selectNum*(Integer.parseInt(selectPrice));
		order.setOrderPrice(String.valueOf(sumPrice));
		order.setOrderStatus(0);
		Admin admin = new Admin();
		admin.setAdminId(1);
		Driver driver = new Driver();
		driver.setDriverId(1);
		order.setAdmin(admin);
		order.setDriver(driver);
		return order;
	}

}
