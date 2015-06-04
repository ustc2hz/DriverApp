package ustc.sse.water.manager;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.DetailDataToServer;
import ustc.sse.water.data.ParkDetailObject;
import ustc.sse.water.home.MapForAddress;
import ustc.sse.water.utils.ToastUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * 订单界面、默认显示当前正在进行的订单. <br>
 * 订单界面类.
 * <p>
 * Copyright: Copyright (c) 2015-03-01 下午10:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author张芳 sa614296@mail.ustc.edu.cn
 * @version 3.0.0
 */
public class ParkingCreatement extends Activity implements OnClickListener {

	// jackson的ObjectMapper,用于在json字符串和Java对象间转换
	public static ObjectMapper objectMapper = new ObjectMapper();
	// button的申明
	private Button changeMess, commit;
	// 构造sharedPreference的编辑对象
	SharedPreferences.Editor editor;
	// 停车场地址的经纬度
	private String location;
	// 地图选点按钮
	private Button modeButton;
	// 停车场名称全局变量
	private String name;
	// 用于获取停车场停车位总数的值
	private String parking_num;
	// 用于获取停车场预定10分钟订金的值
	private String parking_price_ten;
	// 用于获取停车场预定20分钟订金的值
	private String parking_price_twenty;
	// 用于获取停车场预定30分钟订金的值
	private String parking_price_thirty;
	// 用于获取停车场停车半个小时收费金额的值
	private String parking_half_hour_price;
	// 用于获取停车场停车1个小时收费金额的值
	private String parking_one_hour_price;
	// 用于获取停车场停车超过1个小时收费金额的值
	private String parking_onemore_hour_price;
	// 停车场停车位数量输入框
	private EditText park_number;
	// 停车场10分钟预定订金输入框
	private EditText order_ten_price;
	// 停车场20分钟预定订金输入框
	private EditText order_twenty_price;
	// 停车场30分钟预定订金输入框
	private EditText order_thirty_price;
	// 停车场停车半个小时收费金额输入框
	private EditText park_half_hour_price;
	// 停车场停车一个小时收费金额输入框
	private EditText park_one_hour_price;
	// 停车场停车场超过1个小时收费金额输入框
	private EditText park_more_hour_price;
	// 停车场地理坐标——黄志恒
	private EditText parkLocation;
	// 停车场名称——黄志恒
	private EditText parkName;
	// 停车场电话——黄志恒
	private EditText parkPhone;
	// 停车场详细信息数据结构对象——黄志恒
	private ParkDetailObject pdo;
	// 停车场电话全局变量——黄志恒
	private String phone;
	// 将停车场详细数据发送到服务器的对象——黄志恒
	DetailDataToServer postData;
	// SharedPreference获取当前的发布信息
	SharedPreferences preferences;
	// 获取管理员名字
	SharedPreferences userDataPreFer;
	// 需要更新的数据
	private String updateData;
	// 保存管理员名字
	private String managerName;

	/**
	 * 返回通知数据提交服务器是否成功
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // 创建成功
				ToastUtil.show(ParkingCreatement.this, "停车场创建成功!");
				break;
			case 0: // 创建失败
				ToastUtil.show(ParkingCreatement.this, "停车场创建成功!");
				break;
			case 2: // 更新成功
				ToastUtil.show(ParkingCreatement.this, "数据更新成功!");
				break;
			case 3: // 更新失败
				ToastUtil.show(ParkingCreatement.this, "数据更新失败!");
				break;
			}

		}
	};

	/**
	 * 检查输入框是否有更新,如有更新则保存更新的数据
	 */
	public void checkChangeAndSave() {
		StringBuilder saveChange = new StringBuilder();

		saveChange.append("{\"_id\":\"" + preferences.getString("id", "空")
				+ "\"");
		// 停车场名称不为空时进入if语句，并保存数据
		String updatePName = parkName.getText().toString().trim();
		if (!updatePName.equals(preferences.getString("name", "空"))
				&& !updatePName.equals("")) {
			saveChange.append(",\"_name\":\""
					+ parkName.getText().toString().trim() + "\"");
			editor.putString("name", parkName.getText().toString().trim());
			editor.commit();

		}
		// 停车场位置不为空时进入if语句，并保存数据
		String updatePLocation = parkLocation.getText().toString().trim();
		if (!updatePLocation.equals(preferences.getString("location", "空"))
				&& !updatePLocation.equals("")) {
			saveChange.append(",\"_location\":\""
					+ parkLocation.getText().toString().trim() + "\"");
			editor.putString("location", parkLocation.getText().toString()
					.trim());
			editor.commit();
		}

		// 停车场电话不为空时进入if语句，并保存数据
		String updatePPhone = parkPhone.getText().toString().trim();
		if (!updatePPhone.equals(preferences.getString("phone", "空"))
				&& !updatePPhone.equals("")) {
			saveChange.append(",\"phone\":\""
					+ parkPhone.getText().toString().trim() + "\"");
			editor.putString("phone", parkPhone.getText().toString().trim());
			editor.commit();
		}

		// 停车场停车位总数不为空时进入if语句，并保存数据
		String updatePNumber = park_number.getText().toString().trim();
		if (!updatePNumber.equals(preferences.getString("num", "空"))
				&& !updatePNumber.equals("")) {
			saveChange.append(",\"parkSum\":\""
					+ park_number.getText().toString().trim() + "\"");
			editor.putString("num", park_number.getText().toString().trim());
			editor.commit();

		}

		// 停车场预定20分钟订金不为空时进入if语句，并保存数据
		String updateOTenPrice = order_ten_price.getText().toString().trim();
		if (!updateOTenPrice.equals(preferences.getString("price_ten", "空"))
				&& !updateOTenPrice.equals("")) {
			saveChange.append(",\"orderTen\":\""
					+ order_ten_price.getText().toString().trim() + "\"");
			editor.putString("price_ten", order_ten_price.getText().toString()
					.trim());
			editor.commit();
		}

		// 停车场预定40分钟订金不为空时进入if语句，并保存数据
		String updateOTwentyPrice = order_twenty_price.getText().toString()
				.trim();
		if (!updateOTwentyPrice.equals(preferences.getString("price_twenty",
				"空")) && !updateOTwentyPrice.equals("")) {
			saveChange.append(",\"orderTwe\":\""
					+ order_twenty_price.getText().toString().trim() + "\"");
			editor.putString("price_twenty", order_twenty_price.getText()
					.toString().trim());
			editor.commit();
		}

		// 停车场预定60分钟订金不为空时进入if语句，并保存数据
		String updateOThirtyPrice = order_thirty_price.getText().toString()
				.trim();
		if (!updateOThirtyPrice.equals(preferences.getString("price_thirty",
				"空")) && !updateOThirtyPrice.equals("")) {
			saveChange.append(",\"orderTri\":\""
					+ order_thirty_price.getText().toString().trim() + "\"");
			editor.putString("price_thirty", order_thirty_price.getText()
					.toString().trim());
			editor.commit();
		}

		// 停车场停车半个小时金额不为空时进入if语句，并保存数据
		String updateOHourPrice = park_half_hour_price.getText().toString()
				.trim();
		if (!updateOHourPrice.equals(preferences.getString("pprice_ten", "空"))
				&& !updateOHourPrice.equals("")) {
			saveChange.append(",\"payHalPay\":\""
					+ park_half_hour_price.getText().toString().trim() + "\"");
			editor.putString("pprice_ten", park_half_hour_price.getText()
					.toString().trim());
			editor.commit();
		}

		// 停车场停车1个小时金额不为空时进入if语句，并保存数据
		String updateOOHourPrice = park_one_hour_price.getText().toString()
				.trim();
		if (!updateOOHourPrice.equals(preferences.getString("pprice_twenty",
				"空")) && !updateOOHourPrice.equals("")) {
			saveChange.append(",\"payOneHour\":\""
					+ park_one_hour_price.getText().toString().trim() + "\"");
			editor.putString("pprice_twenty", park_one_hour_price.getText()
					.toString().trim());
			editor.commit();
		}

		// 停车场停车多于1个小时金额不为空时进入if语句，并保存数据
		String updateOMHourPrice = park_more_hour_price.getText().toString()
				.trim();
		if (!updateOMHourPrice.equals(preferences.getString("pprice_thirty",
				"空")) && !updateOMHourPrice.equals("")) {
			saveChange.append(",\"payMorePay\":\""
					+ park_more_hour_price.getText().toString().trim() + "\"");
			editor.putString("pprice_thirty", park_more_hour_price.getText()
					.toString().trim());
			editor.commit();
		}

		saveChange.append("}");
		updateData = saveChange.toString();
	}

	/**
	 * 初始化停车场详细信息对象
	 * */
	public void initObject() {
		// 初始化pdo对象
		pdo = new ParkDetailObject();
		// 赋值停车场名称
		pdo.set_name(name.toString());
		// 赋值停车场坐标
		pdo.set_location(location);
		// 赋值停车场电话
		pdo.setPhone(phone);
		// 赋值停车场停车位数量
		pdo.setParkSum(parking_num);
		// 赋值停车场10分钟预定订金
		pdo.setOrderTen(parking_price_ten);
		// 赋值停车场20分钟预定订金
		pdo.setOrderTwe(parking_price_twenty);
		// 赋值停车场30分钟预定订金
		pdo.setOrderTri(parking_price_thirty);
		// 赋值停车场停车半个小时的收费金额
		pdo.setPayOneHour(parking_one_hour_price);
		// 赋值停车场停车1个小时的收费金额
		pdo.setPayHalPay(parking_half_hour_price);
		// 赋值停车场停车超过1个小时的收费金额
		pdo.setPayMorePay(parking_onemore_hour_price);

	}

	/**
	 * 初始化存储
	 */
	public void initSharedPreference() {
		// 取出preferenced的對象
		preferences = getSharedPreferences("manager_message", MODE_PRIVATE);
		editor = preferences.edit();

		userDataPreFer = getSharedPreferences("userdata", MODE_PRIVATE);
		managerName = userDataPreFer.getString("adminLoginName", "NULL");

		// 为停车场名称赋初值
		name = preferences.getString("name", name);
		// 为停车场电话赋初值
		phone = preferences.getString("phone", phone);
		// 为停车场坐标赋初值
		location = preferences.getString("location", "0,0");
	}

	/**
	 * 初始化输入框中显示的信息
	 */
	public void initText() {
		park_number.setHint(preferences.getString("num", "空"));
		order_ten_price.setHint(preferences.getString("price_ten", "空"));
		order_twenty_price.setHint(preferences.getString("price_twenty", "空"));
		order_thirty_price.setHint(preferences.getString("price_thirty", "空"));
		park_half_hour_price.setHint(preferences.getString("pprice_ten", "空"));
		park_one_hour_price
				.setHint(preferences.getString("pprice_twenty", "空"));
		park_more_hour_price.setHint(preferences
				.getString("pprice_thirty", "空"));
		parkName.setHint(preferences.getString("name", "空"));
		parkLocation.setHint(preferences.getString("location", "空"));
		parkPhone.setHint(preferences.getString("phone", "空"));

	}

	/**
	 * 初始化界面控件
	 */
	public void initViews() {

		// 返回按钮
		changeMess = (Button) findViewById(R.id.manager_update);
		// 编辑按钮
		commit = (Button) findViewById(R.id.manager_commit);

		// 通过findViewById找到对应的
		park_number = (EditText) findViewById(R.id.parknumber);
		order_ten_price = (EditText) findViewById(R.id.price1);
		order_twenty_price = (EditText) findViewById(R.id.price2);
		order_thirty_price = (EditText) findViewById(R.id.price3);
		park_half_hour_price = (EditText) findViewById(R.id.pprice1);
		park_one_hour_price = (EditText) findViewById(R.id.pprice2);
		park_more_hour_price = (EditText) findViewById(R.id.pprice3);
		parkName = (EditText) findViewById(R.id.manager_park_name);
		parkLocation = (EditText) findViewById(R.id.manager_park_location);
		// 因为坐标是地图上选点自动生成的，所以这里禁止输入
		parkLocation.setFocusable(false);
		parkPhone = (EditText) findViewById(R.id.manager_park_phone);

		modeButton = (Button) findViewById(R.id.mode_button);

		// 对按钮分别做监听
		changeMess.setOnClickListener(this);
		commit.setOnClickListener(this);
		modeButton.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == 4) {
			location = intent.getStringExtra("location");
			parkLocation.setText(location);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.manager_update:
			// 点击更新数据
			checkChangeAndSave();
			if (12 < updateData.length()) {
				// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
				new Thread() {
					@Override
					public void run() {
						try {
							// 想服务器更新数据
							postUpdateData();
							if (postData.responseMsg.equals("1")) {
								Message msg = new Message();
								msg.what = 2;
								handler.sendMessage(msg);
							} else {
								Message msg = Message.obtain();
								msg.what = 3;
								handler.sendMessage(msg);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			} else {
				Toast.makeText(this, "没有可更新的数据", Toast.LENGTH_SHORT).show();
			}
			break;

		// 点击提交数据提交给服务器
		case R.id.manager_commit:

			// 在第一次输入过自己的停车场信息后，禁止“提交”按钮可选中。
			// 此时只能进行更新修改操作
			if (!"空".equals(preferences.getString("id", "空"))) {
				Toast.makeText(this, "已创建，更新数据请点击更新按钮", Toast.LENGTH_SHORT)
						.show();
			} else if ("".equals(parkLocation.getText().toString().trim())) {
				Toast.makeText(this, "没有选定停车场地址", Toast.LENGTH_SHORT).show();
			} else {
				// 首先存储输入框中的数据
				saveEditTextData();
				// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
				new Thread() {
					@Override
					public void run() {
						try {
							// 向服务器提交创建停车场的数据
							postData();
							if (!"0".endsWith(postData.responseMsg)) {
								// 将获取来的停车场id保存下来
								editor.putString("id", postData.responseMsg);
								editor.commit();
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							} else {
								Message msg = Message.obtain();
								msg.what = 0;
								handler.sendMessage(msg);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
			break;

		// 点击“地图选点”按钮进行地图选点
		case R.id.mode_button:
			Intent toAddMap = new Intent();
			toAddMap.setClass(ParkingCreatement.this, MapForAddress.class);
			int resultCode = 2;
			startActivityForResult(toAddMap, resultCode);
			break;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_distribute_messages);

		initViews();
		initSharedPreference();
		initText();

	}

	/**
	 * 发送创建新停车场的数据
	 * 
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public void postData() throws JsonGenerationException,
			JsonMappingException, Exception {
		// 初始化要传送的数据
		initObject();

		// 将ParkingData转换为Json字符串
		String data = objectMapper.writeValueAsString(this.pdo);
		postData = new DetailDataToServer();
		postData.postDataToServer(data, managerName);
	}

	/**
	 * 发送创建新停车场的数据
	 * 
	 * @throws Exception
	 */
	public void postUpdateData() throws Exception {
		postData = new DetailDataToServer();
		postData.postUpdateDataToServer(updateData);
	}

	/**
	 * 保存输入框的数据
	 */
	public void saveEditTextData() {
		name = parkName.getText().toString().trim();
		phone = parkPhone.getText().toString().trim();

		// 停车场停车位数量
		parking_num = park_number.getText().toString().trim();
		// 停车场预定10分钟的订金
		parking_price_ten = order_ten_price.getText().toString().trim();
		// 停车场预定20分钟的订金
		parking_price_twenty = order_twenty_price.getText().toString().trim();
		// 停车场预定30分钟的订金
		parking_price_thirty = order_thirty_price.getText().toString().trim();
		// 停车场停车半个小时收费的金额
		parking_half_hour_price = park_half_hour_price.getText().toString()
				.trim();
		// 停车场停车1个小时收费的金额
		parking_one_hour_price = park_one_hour_price.getText().toString()
				.trim();
		// 停车场停车多于1个小时收费的金额
		parking_onemore_hour_price = park_more_hour_price.getText().toString()
				.trim();

		editor.putString("num", parking_num);
		editor.putString("price_ten", parking_price_ten);
		editor.putString("price_twenty", parking_price_twenty);
		editor.putString("price_thirty", parking_price_thirty);
		editor.putString("pprice_ten", parking_half_hour_price);
		editor.putString("pprice_twenty", parking_one_hour_price);
		editor.putString("pprice_thirty", parking_onemore_hour_price);
		editor.putString("name", name);
		editor.putString("location", location);
		editor.putString("phone", phone);
		editor.commit();

	}
}
