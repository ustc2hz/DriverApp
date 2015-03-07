package ustc.sse.water.managermain.zf;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.DataToYutunServer;
import ustc.sse.water.data.model.DetailDataToServer;
import ustc.sse.water.data.model.ParkDetailObject;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.zf.MainActivity;
import ustc.sse.water.zf.ManagerMainScreen;
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
	// 停车场地址全局变量——黄志恒
	private String address;
	// button的申明
	private Button changeMess, commit, back;
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

	// 停车场名称全局变量——黄志恒
	private String name;
	// 用于获取edittext的值
	private String num, price_ten, price_twenty, price_thirty, pprice_ten,
			pprice_twenty, pprice_thirty;
	// edittext的申明
	private EditText park_number, l_price, m_price, h_price, pl_price,
			pm_price, ph_price;

	// 停车场地址——黄志恒
	private EditText parkAddress;
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

	/**
	 * 初始化停车场详细信息对象——黄志恒
	 * */
	private void initObject() {
		pdo = new ParkDetailObject();
		pdo.set_name(name.toString());
		Log.v("name tostring", name.toString());
		pdo.set_address(address.toString());
		Log.v("addr tostring", address.toString());
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
	 * 初始化输入框中显示的信息——黄志恒
	 */
	private void initText() {
		park_number.setText(preferences.getString("num", "暂无信息"));
		l_price.setText(preferences.getString("price_ten", "暂无信息"));
		m_price.setText(preferences.getString("price_twenty", "暂无信息"));
		h_price.setText(preferences.getString("price_thirty", "暂无信息"));
		pl_price.setText(preferences.getString("pprice_ten", "暂无信息"));
		pm_price.setText(preferences.getString("pprice_twenty", "暂无信息"));
		ph_price.setText(preferences.getString("pprice_thirty", "暂无信息"));
		parkName.setText(preferences.getString("name", "暂无信息"));
		parkAddress.setText(preferences.getString("address", "暂无信息"));
		parkPhone.setText(preferences.getString("phone", "暂无信息"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.bt1:
			// 点击修改,将所有的edittext设置成可编辑的
			/*
			 * if (changeMess.getText().equals("修改")) {
			 * park_number.setEnabled(true); l_price.setEnabled(true);
			 * m_price.setEnabled(true); h_price.setEnabled(true);
			 * pl_price.setEnabled(true); pm_price.setEnabled(true);
			 * ph_price.setEnabled(true); changeMess.setText("取消修改"); } else if
			 * (changeMess.getText().equals("取消修改")) { //
			 * 点击取消修改，文本框恢复为不可编辑的，按钮文本重置为修改 park_number.setEnabled(false);
			 * l_price.setEnabled(false); m_price.setEnabled(false);
			 * h_price.setEnabled(false); pl_price.setEnabled(false);
			 * pm_price.setEnabled(false); ph_price.setEnabled(false);
			 * changeMess.setText("修改"); }
			 */
			break;

		// 点击提交数据提交给服务器
		case R.id.bt2:
			SaveData1();
			showTemp();
			// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
			new Thread() {
				@Override
				public void run() {
					try {
						postData();
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

			break;
		case R.id.bt3:
			// 点击返回，返回主界面
			Intent intent1 = new Intent(BActivity.this, MainActivity.class);
			startActivity(intent1);
			break;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_distribute_messages);

		// 返回按钮
		changeMess = (Button) findViewById(R.id.bt1);
		// 编辑按钮
		commit = (Button) findViewById(R.id.bt2);
		// 提交按钮
		back = (Button) findViewById(R.id.bt3);
		// 对按钮分别做监听
		changeMess.setOnClickListener(this);
		commit.setOnClickListener(this);
		back.setOnClickListener(this);

		// 通过findViewById找到对应的
		park_number = (EditText) findViewById(R.id.parknumber);
		l_price = (EditText) findViewById(R.id.price1);
		m_price = (EditText) findViewById(R.id.price2);
		h_price = (EditText) findViewById(R.id.price3);
		pl_price = (EditText) findViewById(R.id.pprice1);
		pm_price = (EditText) findViewById(R.id.pprice2);
		ph_price = (EditText) findViewById(R.id.pprice3);
		parkName = (EditText) findViewById(R.id.manager_park_name);
		parkAddress = (EditText) findViewById(R.id.manager_park_address);
		parkPhone = (EditText) findViewById(R.id.manager_park_phone);

		// 分别设置文本框不可编辑，并且输入默认数据
		/*
		 * park_number.setEnabled(false); l_price.setEnabled(false);
		 * m_price.setEnabled(false); h_price.setEnabled(false);
		 * pl_price.setEnabled(false); pm_price.setEnabled(false);
		 * ph_price.setEnabled(false);
		 */

		// 取出preferenced的對象
		preferences = getSharedPreferences("manager_message",
				MODE_WORLD_READABLE);
		editor = preferences.edit();

		// 逻辑有问题
		// SaveData();

		name = preferences.getString("name", name);
		phone = preferences.getString("phone", phone);
		address = preferences.getString("address", address);

		initText();

	}

	/**
	 * 发送数据——黄志恒
	 * 
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	private void postData() throws JsonGenerationException,
			JsonMappingException, Exception {
		initObject();
		// initData();
		// 将ParkingData转换为Json字符串
		String data = objectMapper.writeValueAsString(this.pdo);
		Log.v("123", data);
		postData = new DetailDataToServer(data);
		postData.postDataToServer();
	}

	/**
	 * 保存输入框的数据——黄志恒
	 */
	private void SaveData1() {
		name = parkName.getText().toString().trim();
		address = parkAddress.getText().toString().trim();
		phone = parkPhone.getText().toString().trim();

		num = park_number.getText().toString().trim();
		price_ten = l_price.getText().toString().trim();
		price_twenty = m_price.getText().toString().trim();
		price_thirty = h_price.getText().toString().trim();
		pprice_ten = pl_price.getText().toString().trim();
		pprice_twenty = pm_price.getText().toString().trim();
		pprice_thirty = ph_price.getText().toString().trim();

		editor.putString("num", num);
		editor.commit();

		editor.putString("price_ten", price_ten);
		editor.commit();

		editor.putString("price_twenty", price_twenty);
		editor.commit();

		editor.putString("price_thirty", price_thirty);
		editor.commit();

		editor.putString("pprice_ten", pprice_ten);
		editor.commit();

		editor.putString("pprice_twenty", pprice_twenty);
		editor.commit();

		editor.putString("pprice_thirty", pprice_thirty);
		editor.commit();

		editor.putString("name", name);
		editor.commit();

		editor.putString("phone", phone);
		editor.commit();

		editor.putString("address", address);
		editor.commit();

	}

	private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {

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

	private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {

		return builder.setPositiveButton("提交",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// 弹出对话框后消失
						arg0.dismiss();
						// 设置跳转
						Intent it = new Intent(BActivity.this,
								ManagerMainScreen.class);
						it.putExtra("etr", "1");
						startActivity(it);
					}
				});
	}

	/**
	 * 在logCat中打印sharedPreference信息，用于测试——黄志恒
	 */
	private void showTemp() {
		Log.v("name", preferences.getString("name", "fail"));
		Log.v("address", preferences.getString("address", "fail"));
		Log.v("phone", preferences.getString("phone", "fail"));
		Log.v("sum", preferences.getString("num", "fail"));
		Log.v("orderTen", preferences.getString("price_ten", "fail"));
		Log.v("orderTw", preferences.getString("price_twenty", "fail"));
		Log.v("orderThr", preferences.getString("price_thirty", "fail"));
		Log.v("payHalf", preferences.getString("pprice_ten", "fail"));
		Log.v("payOne", preferences.getString("pprice_twenty", "fail"));
		Log.v("payMore", preferences.getString("pprice_thirty", "fail"));
	}

	public void simple(View source) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"尊敬的用户您好！").setMessage("您确定要提交吗？");
		// 为AlertDialog.Builder添加按钮
		setPositiveButton(builder);
		setNegativeButton(builder).create().show();
	}
}
