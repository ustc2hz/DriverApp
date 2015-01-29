package ustc.sse.water.zf;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.DataToYutunServer;
import ustc.sse.water.data.model.ParkingData;
import android.app.Activity;
import android.content.Intent;
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
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:15:44
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 1.0.0
 */
public class ManagerSettings extends Activity implements OnClickListener {

	// jackson的ObjectMapper,用于在json字符串和Java对象间转换——黄志恒
	public static ObjectMapper objectMapper = new ObjectMapper();
	// 停车场地址——黄志恒
	private String address;
	// 申明变量
	Button bt1, bt2, bt3, bt4, bt5, bt6;

	// 提交停车场位置信息——黄志恒注
	Button data_submit;
	EditText et1, et2, et3, et4, et5;

	/**
	 * 返回通知数据提交服务器是否成功——黄志恒
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				// txt_status.setText("success");
				Toast.makeText(getApplicationContext(), "Submit Success!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Submit Failed!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	// 向服务器发送数据的对象——黄志恒
	private DataToYutunServer mDataToServer;
	// 停车场名称——黄志恒
	private String name;
	// 存数停车场信息的数据结构对象——黄志恒
	private ParkingData pData = new ParkingData();

	// 停车场管理员手机号码——黄志恒
	private String phone;

	/**
	 * 初始化停车场数据——黄志恒
	 */
	public void initData() {
		this.name = et1.getText().toString().trim();
		this.address = et5.getText().toString().trim();
		this.phone = et4.getText().toString().trim();
		pData.set_name(this.name);
		pData.set_address(this.address);
		pData.setPhone(this.phone);
	}

	/**
	 * 按钮点击监听——黄志恒
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			// 点击返回跳转
			Intent intent = new Intent(ManagerSettings.this, ManagerMain.class);
			startActivity(intent);
			break;
		case R.id.button2:
			// 点击button2修改相关信息，edittext恢复可编辑，按钮成为取消修改，点击取下修改edittext恢复不可编辑
			if (bt2.getText().equals("修改")) {
				et1.setEnabled(true);
				bt2.setText("取消修改");
			} else if (bt2.getText().equals("取消修改")) {
				et1.setEnabled(false);
				bt2.setText("修改");
			}
			break;
		case R.id.button3:
			Intent intent1 = new Intent(ManagerSettings.this,
					ManagerChangePass.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.button4:
			if (bt4.getText().equals("修改")) {
				et3.setEnabled(true);
				bt4.setText("取消修改");
			} else if (bt4.getText().equals("取消修改")) {
				et3.setEnabled(false);
				bt4.setText("修改");
			}
			break;
		case R.id.button5:
			if (bt5.getText().equals("修改")) {
				et4.setEnabled(true);
				bt5.setText("取消修改");
			} else if (bt5.getText().equals("取消修改")) {
				et4.setEnabled(false);
				bt5.setText("修改");
			}
			break;
		case R.id.button6:
			if (bt6.getText().equals("修改")) {
				et5.setEnabled(true);
				bt6.setText("取消修改");
			} else if (bt6.getText().equals("取消修改")) {
				et5.setEnabled(false);
				bt6.setText("修改");
			}
			break;

		// 点击提交按钮的监听
		case R.id.data_submit:
			// 初始化停车场名称和地址——黄志恒
			initData();
			// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
			new Thread() {
				@Override
				public void run() {

					try {
						postData();
						if (mDataToServer.responseMsg.equals("1")) {
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

		}
	}

	/**
	 * 初始化UI界面——黄志恒
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_personal_settings);

		// 通过findViewById方法获得对应的button

		data_submit = (Button) findViewById(R.id.data_submit);

		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		bt3 = (Button) findViewById(R.id.button3);
		bt4 = (Button) findViewById(R.id.button4);
		bt5 = (Button) findViewById(R.id.button5);
		bt6 = (Button) findViewById(R.id.button6);
		// 通过findViewById方法获得对应的edittext
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		et3 = (EditText) findViewById(R.id.editText3);
		et4 = (EditText) findViewById(R.id.editText4);
		et5 = (EditText) findViewById(R.id.editText5);

		// 设置输入框基本 信息
		et1.setHint("Parking Name");
		et5.setHint("Parking Address");
		et4.setHint("Manager PhoneNumber");

		// 对button做监听
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
		bt6.setOnClickListener(this);

		data_submit.setOnClickListener(this);

		// 对edittext做监听
		et1.setOnClickListener(this);
		et2.setOnClickListener(this);
		et3.setOnClickListener(this);
		et4.setOnClickListener(this);
		et5.setOnClickListener(this);
		// 设置edittext不可编辑
		et1.setEnabled(false);
		et2.setEnabled(false);
		et3.setEnabled(false);
		et4.setEnabled(false);
		et5.setEnabled(false);

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
