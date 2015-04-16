package ustc.sse.water.fragment;

import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.Admin;
import ustc.sse.water.data.model.Driver;
import ustc.sse.water.data.model.Order;
import ustc.sse.water.thread.CheckOrderReceiveThread;
import ustc.sse.water.thread.SendOrderThread;
import ustc.sse.water.utils.zjx.ProgressDialogUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.zf.LoginActivity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
 * @version 3.0.0
 */
public class ParkingBookFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener {
	public static int leftNumber; // 剩余车位数
	public static ObjectMapper objectMapper = new ObjectMapper();
	private String[] parkingInfo = new String[2]; // 预定的停车场基本信息
	private String[] parkingMoney = new String[6]; // 预定的停车场收费
	private String[] time = new String[3]; // 预定信息
	private String selectBook = ""; // 选择的预定方案
	private static String orderUUID = ""; // 订单的UUID号
	private int selectIndex = 0; // 驾驶员选择的价格序号
	private Context context; // 上下文
	private Spinner spinner; // 预定时间下拉框
	private Button submitOrder;// 提交订单按钮
	private EditText driverNumber; // 预定车位个数输入框
	private String valDriverNumber = null; // 输入的车位数值
	private int managerId; // 停车场管理员的id
	private int driverId; // 驾驶员id
	private String parkType; // 标记是web的停车场还是app的停车场
	private ProgressDialogUtil progressDialog;

	public ParkingBookFragment(Context con, int managerId, int driverId,
			String type) {
		this.context = con;
		this.managerId = managerId;
		this.driverId = driverId;
		this.parkType = type;
	}

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
					// 查看刚刚发送的订单是否被管理员接收到
					CheckOrderReceiveThread crt = new CheckOrderReceiveThread(
							h, orderUUID, managerId, valDriverNumber);
					crt.start();
				} else { // 如果订单发送失败则预定失败
					progressDialog.dissmissProgressDialog();
					ToastUtil.show(context, "发送失败");
				}
				break;
			case 2: // 发送请求判断对应的管理员是否已经接收到订单
				progressDialog.dissmissProgressDialog();
				result = msg.getData().getString("check_result");
				// 判断是否接收成功
				if ("success".equals(result)) {
					// 只有管理员接收到订单后，预定才算成功
					ToastUtil.show(context, "管理员接收成功");
					getActivity().finish();
				} else { // 如果订单接收失败，则预定失败
					ToastUtil.show(context, "管理员接收失败");
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_submit_order:// 点击提交订单
			valDriverNumber = driverNumber.getText().toString();
			if (driverNumber == null || "".equals(valDriverNumber)) {
				driverNumber.setError("请填写预定的车位数！");
			}else if(Integer.valueOf(valDriverNumber) > leftNumber) {
				// 预定的车位数大于剩余的车位数
				driverNumber.setError("没有足够的车位！");
			} else if (driverId == 0) {// 驾驶员没有登录
				new AlertDialog.Builder(context)
						.setTitle("提示")
						.setMessage("预定之前请先登录！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(context,
												LoginActivity.class);
										startActivity(intent);
										dialog.dismiss();
									}
								}).create().show();
			} else {
				Order order = createOrder();
				String jacksonString = "";
				try {
					jacksonString = objectMapper.writeValueAsString(order);
				} catch (Exception e) {
					e.printStackTrace();
				}
				final String orderInfo = jacksonString;
				// 发送订单到服务器
				SendOrderThread st = new SendOrderThread(h, orderInfo);
				st.start();

				progressDialog = new ProgressDialogUtil(context, "正在提交，请稍后...");
				progressDialog.showProgressDialog();
			}
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		time = ParkingInfoFragment.tranMoney;
		parkingInfo = ParkingInfoFragment.orderMessages;
		parkingMoney = ParkingInfoFragment.bookMoneys;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.parking_book, container, false);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item, time);
		driverNumber = (EditText) view.findViewById(R.id.edit_licenses_number);
		submitOrder = (Button) view.findViewById(R.id.button_submit_order);
		submitOrder.setClickable(false);
		submitOrder.setEnabled(false);
		driverNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 只有输入内容时才将按钮设为可点击
				if(s.length() > 0) {
					submitOrder.setClickable(true);
					submitOrder.setEnabled(true);
				}else {
					submitOrder.setClickable(false);
					submitOrder.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		spinner = (Spinner) view.findViewById(R.id.spinner_time_chose);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(this);
		submitOrder.setOnClickListener(this);
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		selectBook = parent.getItemAtPosition(position).toString();
		selectIndex = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	// 生成一个订单
	private Order createOrder() {
		Order order = new Order();
		orderUUID = order.getUuid();
		order.setParkName(parkingInfo[0]);
		order.setParkAddress(parkingInfo[1]);
		// 获取填写的数量
		int selectNum = Integer.parseInt(valDriverNumber);
		order.setDriverNum(selectNum);
		// 生存订单日期
		String orderDate = (DateFormat.format("yyyy-MM-dd", new Date()))
				.toString();
		order.setOrderDate(orderDate);
		order.setOrderInfo(selectBook);
		// 计算总价
		String selectPrice = parkingMoney[selectIndex];
		int sumPrice = selectNum * (Integer.parseInt(selectPrice));
		order.setOrderPrice(String.valueOf(sumPrice));
		// 如果是Web的停车场则直接将状态设置为1
		if("web".equals(parkType)) {
			order.setOrderStatus(1);
		} else {
			order.setOrderStatus(0);
		}
		// 设置外键
		Admin admin = new Admin();
		admin.setAdminId(managerId);
		Driver driver = new Driver();
		driver.setDriverId(driverId);
		order.setAdmin(admin);
		order.setDriver(driver);
		return order;
	}

}
