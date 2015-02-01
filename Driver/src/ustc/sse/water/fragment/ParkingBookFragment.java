package ustc.sse.water.fragment;

import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.thread.CheckOrderReceiveThread;
import ustc.sse.water.thread.SendOrderThread;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private Context context; // 上下文
	public static ObjectMapper objectMapper = new ObjectMapper();
	/* 模拟车牌号和预定时间 */
	private String[] license = { "苏1234567", "苏666666", "苏888888" };
	private String[] time = { "30分钟10元", "1小时20元", "90分钟30元", "2小时40元" };
	private ListView listView;// 车牌号的列表
	private Spinner spinner; // 预定时间下拉框
	private Button submitOrder;// 提交订单按钮

	public ParkingBookFragment(Context con) {
		this.context = con;
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
		View view = inflater.inflate(R.layout.parking_book, container, false);
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_list_item_multiple_choice,
				license);
		listView = (ListView) view.findViewById(R.id.listview_license_chose);
		listView.setAdapter(listViewAdapter);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item, time);
		spinner = (Spinner) view.findViewById(R.id.spinner_time_chose);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(this);
		submitOrder = (Button) view.findViewById(R.id.button_submit_order);
		submitOrder.setOnClickListener(this);
		return view;
	}

	/**
	 * 提交订单
	 */
	public void submit() {
		int count = listView.getCheckedItemCount(); // 选择的车牌号数
		Toast.makeText(context, "-->>" + count, 1).show(); // 测试
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String str = parent.getItemAtPosition(position).toString();
		Toast.makeText(context, str, 1).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_submit_order:// 点击提交订单
			submit();
			String jacksonString = "";
			/*Order order = TestOrderData.getData();*/
			try {
				/*jacksonString = objectMapper.writeValueAsString(order);*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			SendOrderThread st = new SendOrderThread(h, jacksonString);
			st.start();
			CheckOrderReceiveThread crt = new CheckOrderReceiveThread(h);
			crt.start();
			break;
		}
	}
	
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

}
