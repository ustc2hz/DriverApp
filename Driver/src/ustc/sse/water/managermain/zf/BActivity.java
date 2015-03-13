package ustc.sse.water.managermain.zf;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.MapForAddress;
import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.DataToYutunServer;
import ustc.sse.water.data.model.DetailDataToServer;
import ustc.sse.water.data.model.ParkDetailObject;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.zf.LoginActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class BActivity extends Activity implements OnClickListener {
	// jackson的ObjectMapper,用于在json字符串和Java对象间转换——黄志恒
	public static ObjectMapper objectMapper = new ObjectMapper();
	// button的申明
	private Button changeMess, commit;
	// 构造sharedPreference的编辑对象——黄志恒
	SharedPreferences.Editor editor;

	/**
	 * 返回通知数据提交服务器是否成功——黄志恒
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				ToastUtil.show(BActivity.this, "Submit Success!");
			} else {
				ToastUtil.show(BActivity.this, "Submit Failed!");
			}
		}
	};
	// 停车场地址的经纬度——黄志恒
	// private String address;
	private String location;
	// 地图选点按钮
	private Button modeButton;

	// 切换手工输入和地图选点的Spinner——黄志恒
	private Spinner modeSpinner;
	// 停车场名称全局变量——黄志恒
	private String name;
	// 用于获取edittext的值
	private String num, price_ten, price_twenty, price_thirty, pprice_ten,
			pprice_twenty, pprice_thirty;

	// edittext的申明
	private EditText park_number, l_price, m_price, h_price, pl_price,
			pm_price, ph_price;

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

	private DataToYutunServer post;
	// 将停车场详细数据发送到服务器的对象——黄志恒
	DetailDataToServer postData;

	// SharedPreference获取当前的发布信息
	SharedPreferences preferences;
	// 需要更新的数据
	private String updateData;

	/**
	 * 检查输入框是否有更新,如有更新则保存更新的数据
	 */
	public void checkChangeAndSave() {
		StringBuilder saveChange = new StringBuilder();

		saveChange.append("{\"_id\":\"" + preferences.getString("id", "空")
				+ "\"");
		if (!parkName.getText().toString().trim()
				.equals(preferences.getString("name", "空"))) {
			saveChange.append(",\"_name\":\""
					+ parkName.getText().toString().trim() + "\"");
			editor.putString("name", parkName.getText().toString().trim());
			editor.commit();

		}
		if (!parkLocation.getText().toString().trim()
				.equals(preferences.getString("location", "空"))) {
			saveChange.append(",\"_location\":\""
					+ parkLocation.getText().toString().trim() + "\"");
			editor.putString("location", parkLocation.getText().toString()
					.trim());
			editor.commit();
		}

		if (!parkPhone.getText().toString().trim()
				.equals(preferences.getString("phone", "空"))) {
			saveChange.append(",\"phone\":\""
					+ parkPhone.getText().toString().trim() + "\"");
			editor.putString("phone", parkPhone.getText().toString().trim());
			editor.commit();
		}

		if (!park_number.getText().toString().trim()
				.equals(preferences.getString("num", "空"))) {
			saveChange.append(",\"parkSum\":\""
					+ park_number.getText().toString().trim() + "\"");
			editor.putString("num", park_number.getText().toString().trim());
			editor.commit();

		}

		if (!l_price.getText().toString().trim()
				.equals(preferences.getString("price_ten", "空"))) {
			saveChange.append(",\"orderTen\":\""
					+ l_price.getText().toString().trim() + "\"");
			editor.putString("price_ten", l_price.getText().toString().trim());
			editor.commit();
		}

		if (!m_price.getText().toString().trim()
				.equals(preferences.getString("price_twenty", "空"))) {
			saveChange.append(",\"orderTwe\":\""
					+ m_price.getText().toString().trim() + "\"");
			editor.putString("price_twenty", m_price.getText().toString()
					.trim());
			editor.commit();
		}

		if (!h_price.getText().toString().trim()
				.equals(preferences.getString("price_thirty", "空"))) {
			saveChange.append(",\"orderTri\":\""
					+ h_price.getText().toString().trim() + "\"");
			editor.putString("price_thirty", h_price.getText().toString()
					.trim());
			editor.commit();
		}

		if (!pl_price.getText().toString().trim()
				.equals(preferences.getString("pprice_ten", "空"))) {
			saveChange.append(",\"payHalPay\":\""
					+ pl_price.getText().toString().trim() + "\"");
			editor.putString("pprice_ten", pl_price.getText().toString().trim());
			editor.commit();
		}

		if (!pm_price.getText().toString().trim()
				.equals(preferences.getString("pprice_twenty", "空"))) {
			saveChange.append(",\"payOneHour\":\""
					+ pm_price.getText().toString().trim() + "\"");
			editor.putString("pprice_twenty", pm_price.getText().toString()
					.trim());
			editor.commit();
		}

		if (!ph_price.getText().toString().trim()
				.equals(preferences.getString("pprice_thirty", "空"))) {
			saveChange.append(",\"payMorePay\":\""
					+ ph_price.getText().toString().trim() + "\"");
			editor.putString("pprice_thirty", ph_price.getText().toString()
					.trim());
			editor.commit();
		}

		saveChange.append("}");
		updateData = saveChange.toString();
	}

	/**
	 * 初始化停车场详细信息对象——黄志恒
	 * */
	public void initObject() {
		pdo = new ParkDetailObject();
		pdo.set_name(name.toString());

		Log.v("name tostring", name.toString());
		pdo.set_location(location);
		// pdo.set_address(" ");
		pdo.setPhone(phone);
		pdo.setParkSum(num);
		pdo.setOrderTen(price_ten);
		pdo.setOrderTwe(price_twenty);
		pdo.setOrderTri(price_thirty);
		pdo.setPayOneHour(pprice_ten);
		pdo.setPayHalPay(pprice_twenty);
		pdo.setPayMorePay(pprice_twenty);

	}

	/**
	 * 初始化存储——黄志恒
	 */
	public void initSharedPreference() {
		// 取出preferenced的對象
		preferences = getSharedPreferences("manager_message",
				MODE_PRIVATE);
		editor = preferences.edit();

		name = preferences.getString("name", name);
		phone = preferences.getString("phone", phone);
		location = preferences.getString("location", "0,0");
	}

	/**
	 * 初始化输入框中显示的信息——黄志恒
	 */
	public void initText() {
		park_number.setText(preferences.getString("num", "空"));
		l_price.setText(preferences.getString("price_ten", "空"));
		m_price.setText(preferences.getString("price_twenty", "空"));
		h_price.setText(preferences.getString("price_thirty", "空"));
		pl_price.setText(preferences.getString("pprice_ten", "空"));
		pm_price.setText(preferences.getString("pprice_twenty", "空"));
		ph_price.setText(preferences.getString("pprice_thirty", "空"));
		parkName.setText(preferences.getString("name", "空"));
		parkLocation.setText(preferences.getString("location", "空"));
		parkPhone.setText(preferences.getString("phone", "空"));

	}

	/**
	 * 初始化界面控件——黄志恒
	 */
	public void initViews() {

		// 返回按钮
		changeMess = (Button) findViewById(R.id.manager_update);
		// 编辑按钮
		commit = (Button) findViewById(R.id.manager_commit);
		// 提交按钮

		// 通过findViewById找到对应的
		park_number = (EditText) findViewById(R.id.parknumber);
		l_price = (EditText) findViewById(R.id.price1);
		m_price = (EditText) findViewById(R.id.price2);
		h_price = (EditText) findViewById(R.id.price3);
		pl_price = (EditText) findViewById(R.id.pprice1);
		pm_price = (EditText) findViewById(R.id.pprice2);
		ph_price = (EditText) findViewById(R.id.pprice3);
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
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.manager_update:
			// 点击更新数据
			checkChangeAndSave();
			if (12 < updateData.length()) {
				showTemp();
				// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
				new Thread() {
					@Override
					public void run() {
						try {
							postUpdateData();
							if (postData.responseMsg.equals("1")) {
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							} else {
								Message msg = Message.obtain();
								msg.what = 0;
								handler.sendMessage(msg);
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 线程逻辑
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
			} else {
				SaveData1();
				showTemp();
				// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
				new Thread() {
					@Override
					public void run() {
						try {
							postData();
							if (!"0".endsWith(postData.responseMsg)) {
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
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// 线程逻辑
					}
				}.start();
			}
			break;

		// 点击“地图选点”按钮进行地图选点
		case R.id.mode_button:
			Intent toAddMap = new Intent();
			toAddMap.setClass(BActivity.this, MapForAddress.class);
			int resultCode = 2;
			startActivityForResult(toAddMap, resultCode);
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
	 * 发送创建新停车场的数据——黄志恒
	 * 
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public void postData() throws JsonGenerationException,
			JsonMappingException, Exception {
		initObject();

		// 将ParkingData转换为Json字符串
		String data = objectMapper.writeValueAsString(this.pdo);
		Log.v("123", data);
		postData = new DetailDataToServer();
		postData.postDataToServer(data);
	}

	/**
	 * 发送创建新停车场的数据——黄志恒
	 * 
	 * @throws Exception
	 */
	public void postUpdateData() throws Exception {
		Log.v("123", updateData);
		postData = new DetailDataToServer();
		postData.postUpdateDataToServer(updateData);
	}

	/**
	 * 保存输入框的数据——黄志恒
	 */
	public void SaveData1() {
		name = parkName.getText().toString().trim();
		phone = parkPhone.getText().toString().trim();

		num = park_number.getText().toString().trim();
		price_ten = l_price.getText().toString().trim();
		price_twenty = m_price.getText().toString().trim();
		price_thirty = h_price.getText().toString().trim();
		pprice_ten = pl_price.getText().toString().trim();
		pprice_twenty = pm_price.getText().toString().trim();
		pprice_thirty = ph_price.getText().toString().trim();

		editor.putString("num", num);
		editor.putString("price_ten", price_ten);
		editor.putString("price_twenty", price_twenty);
		editor.putString("price_thirty", price_thirty);
		editor.putString("pprice_ten", pprice_ten);
		editor.putString("pprice_twenty", pprice_twenty);
		editor.putString("pprice_thirty", pprice_thirty);
		editor.putString("name", name);
		editor.putString("location", location);
		editor.putString("phone", phone);
		editor.commit();

	}

	public AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {

		// 调用setPositiveButton方法添加取消按钮
		return builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// 弹出对话框后消失
						arg0.dismiss();
						// 设置intent跳转
						// Intent it = new
						// Intent(distribute.this,distribute.class);
						// it.putExtra("etr","0");
						// startActivity(it);
					}
				});
	}

	public AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {

		return builder.setPositiveButton("提交",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// 弹出对话框后消失
						arg0.dismiss();
						// 设置跳转
						Intent it = new Intent(BActivity.this,
								LoginActivity.class);
						it.putExtra("etr", "1");
						startActivity(it);
					}
				});
	}

	/**
	 * 在logCat中打印sharedPreference信息，用于测试——黄志恒
	 */
	public void showTemp() {
		Log.v("name", preferences.getString("name", "fail"));
		Log.v("location", preferences.getString("location", "fail"));
		Log.v("phone", preferences.getString("phone", "fail"));
		Log.v("sum", preferences.getString("num", "fail"));
		Log.v("orderTen", preferences.getString("price_ten", "fail"));
		Log.v("orderTw", preferences.getString("price_twenty", "fail"));
		Log.v("orderThr", preferences.getString("price_thirty", "fail"));
		Log.v("payHalf", preferences.getString("pprice_ten", "fail"));
		Log.v("payOne", preferences.getString("pprice_twenty", "fail"));
		Log.v("payMore", preferences.getString("pprice_thirty", "fail"));
		Log.v("id", preferences.getString("id", "fail"));
	}
}
