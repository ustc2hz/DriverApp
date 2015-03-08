package ustc.sse.water.managermain.zf;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.DataToYutunServer;
import ustc.sse.water.data.model.ParkingData;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.zf.ManagerChangePass;
import android.app.Activity;
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
import android.widget.TextView;

/**
 * 
 * 管理员修改信息类，. <br>
 * 主要是修改管理员的用户名密码，以及个人相关信息.
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:15:44
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 1.0.0
 */

public class CActivity extends Activity implements OnClickListener {

	// jackson的ObjectMapper,用于在json字符串和Java对象间转换——黄志恒
	public static ObjectMapper objectMapper = new ObjectMapper();
	// 停车场地址——黄志恒
	private String address;
	private Button change, data_submit, back;
	SharedPreferences.Editor editor;
	
	/**
	 * 返回通知数据提交服务器是否成功——黄志恒
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				ToastUtil.show(CActivity.this, "Submit Success!");
			} else {
				ToastUtil.show(CActivity.this, "Submit Failed!");
			}
		}
	};
	EditText manager_name, manager_phone, manager_address;
	
	TextView change_password;

	// 向服务器发送数据的对象——黄志恒
	private DataToYutunServer mDataToServer;
	// 停车场名称——黄志恒
	private String name;

	// 存数停车场信息的数据结构对象——黄志恒
	private ParkingData pData = new ParkingData();
	// 停车场管理员手机号码——黄志恒
	private String phone;

	// 用sharedPreference传递数据
	SharedPreferences preferences;

	/**
	 * 初始化停车场数据——黄志恒
	 */
	public void initData() {
		this.name = manager_name.getText().toString().trim();
		this.address = manager_address.getText().toString().trim();
		this.phone = manager_phone.getText().toString().trim();
		pData.set_name(this.name);
		pData.set_address(this.address);
		pData.setPhone(this.phone);

		editor.putString("name", name);
		editor.commit();
		editor.putString("phone", phone);
		editor.commit();
		editor.putString("address", address);
		editor.commit();

		Log.v("name", preferences.getString("name", null));
		Log.v("address", preferences.getString("address", null));
		Log.v("phone", preferences.getString("phone", null));
	}

	private void initText() {
		manager_name.setText(preferences.getString("name", "暂无信息"));
		manager_address.setText(preferences.getString("address", "暂无信息"));
		manager_phone.setText(preferences.getString("phone", "暂无信息"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			// 点击返回跳转
			Intent intent = new Intent(CActivity.this,
					ManagerMainTabActivity.class);
			startActivity(intent);
			break;
		case R.id.change:
			// 点击button2修改相关信息，edittext恢复可编辑，按钮成为取消修改，点击取下修改edittext恢复不可编辑
			/*
			 * if (change.getText().equals("修改")) {
			 * manager_name.setEnabled(true); password.setEnabled(true);
			 * manager_phone.setEnabled(true); manager_address.setEnabled(true);
			 * change.setText("取消修改"); } else if
			 * (change.getText().equals("取消修改")) {
			 * manager_name.setEnabled(false); password.setEnabled(false);
			 * manager_phone.setEnabled(false);
			 * manager_address.setEnabled(false); change.setText("修改"); }
			 */
			break;
		case R.id.change_pass:
			Intent intent1 = new Intent(CActivity.this, ManagerChangePass.class);
			startActivity(intent1);
			break;

		// 点击提交按钮的监听
		case R.id.data_submit:
			// 初始化停车场名称和地址——黄志恒
			initData();
			// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
			/*
			 * new Thread() {
			 * 
			 * @Override public void run() { try { //postData(); if
			 * (mDataToServer.responseMsg.equals("1")) { Message msg = new
			 * Message(); msg.what = 1; handler.sendMessage(msg); } else {
			 * Message msg = Message.obtain(); msg.what = 0;
			 * handler.sendMessage(msg); } } catch (Exception e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } // 线程逻辑 }
			 * }.start();
			 */
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_personal_settings);

		// 通过findViewById方法获得对应的button
		change = (Button) findViewById(R.id.change);
		data_submit = (Button) findViewById(R.id.data_submit);
		back = (Button) findViewById(R.id.back);

		// 通过findViewById方法获得对应的edittext
		manager_name = (EditText) findViewById(R.id.manager_name);
		change_password = (TextView) findViewById(R.id.change_pass);
		manager_phone = (EditText) findViewById(R.id.phone);
		manager_address = (EditText) findViewById(R.id.address);

		// 设置输入框基本 信息
		manager_name.setHint("Parking Name");
		manager_phone.setHint("Manager PhoneNumber");
		manager_address.setHint("Parking Address");

		// 对button做监听
		change.setOnClickListener(this);
		back.setOnClickListener(this);
		data_submit.setOnClickListener(this);

		// 对edittext做监听
		manager_name.setOnClickListener(this);
		change_password.setOnClickListener(this);
		manager_phone.setOnClickListener(this);
		manager_address.setOnClickListener(this);
		// 设置edittext不可编辑
		/*
		 * manager_name.setEnabled(false); password.setEnabled(false);
		 * manager_phone.setEnabled(false); manager_address.setEnabled(false);
		 */

		// 取出原有的preferenced
		preferences = getSharedPreferences("manager_message",
				MODE_WORLD_READABLE);
		editor = preferences.edit();

		initText();

		// 存储数据——逻辑有问题
		// Save();

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
		// 将ParkingData转换为Json字符串
		String data = objectMapper.writeValueAsString(pData);
		mDataToServer = new DataToYutunServer(data);
		mDataToServer.create();
	}

}
