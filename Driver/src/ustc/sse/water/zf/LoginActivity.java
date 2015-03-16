package ustc.sse.water.zf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.activity.zjx.DriverInfo;
import ustc.sse.water.data.model.Admin;

import ustc.sse.water.service.UpdateOrderService;
import ustc.sse.water.manager.zf.ManagerMainTabActivity;

import ustc.sse.water.utils.zjx.HttpUtils;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;


/**
 * 
 * 登录界面类 <br>
 * 该类用来显示驾驶员和管理员的登录和注册等，将登录信息记录到SharedPreference中。
 * <p>
 * Copyright: Copyright (c) 2014-11-13 下午10:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author张芳 sa614296@mail.ustc.edu.cn
 * @version 4.0.0
 */
public class LoginActivity extends Activity implements OnClickListener {
	public static int radioStatus = 0; // 默认是驾驶员
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟

	private EditText inputPassword; // 密码输入框
	private EditText inputUsername; // 用户名输入框
	private Button loginBtn; // 登录按钮
	private RadioButton manager_check, driver_check; // 驾驶员和管理员的单选按钮
	private ProgressDialog mDialog;
	private Button registerBtn; // 注册按钮
	private String responseMsg; // 访问服务器返回的结果
	private CheckBox saveInfoItem; // 保存密码
	private SharedPreferences sp;
	private SharedPreferences.Editor spEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.login);
		
		// 初始化视图组件
		loginBtn = (Button) findViewById(R.id.login_btn_login);
		registerBtn = (Button) findViewById(R.id.login_btn_register);
		inputUsername = (EditText) findViewById(R.id.login_edit_account);
		inputPassword = (EditText) findViewById(R.id.login_edit_pwd);
		saveInfoItem = (CheckBox) findViewById(R.id.login_cb_savepwd);
		manager_check = (RadioButton) findViewById(R.id.check_manager);
		driver_check = (RadioButton) findViewById(R.id.check_driver);
		driver_check.setChecked(true);

		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		spEditor = sp.edit();

		// 初始化数据
		loadUserdata();

		// 监听记住密码选项
		saveInfoItem.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) { // 如果勾选了，则赋值为true
					spEditor.putBoolean("checkstatus", true);
				} else { // 如果没有勾选，则赋值为false
					spEditor.putBoolean("checkstatus", false);
				}
				spEditor.commit();
			}
		});
		
		loginBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
	}

	/**
	 * 通过“记住密码”初始化输入框
	 */
	private void loadUserdata() {
		boolean checkstatus = sp.getBoolean("checkstatus", false);
		if (checkstatus) {
			saveInfoItem.setChecked(true);
			// 获取已经存在的用户名和密码
			String realUsername = sp.getString("username", "");
			String realPassword = sp.getString("password", "");

			if ((!realUsername.equals("")) && (!realPassword.equals(""))) {
				inputUsername.setText(realUsername);
				inputPassword.setText(realPassword);
			}
		} else { 
			saveInfoItem.setChecked(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		// 先判断是管理员还是驾驶员
		if (driver_check.isChecked()) {
			radioStatus = 0;
		} else if (manager_check.isChecked()) {
			radioStatus = 1;
		}
		
		// 按钮事件处理
		switch(v.getId()) {
		case R.id.login_btn_login:
			mDialog = new ProgressDialog(LoginActivity.this);
			mDialog.setTitle("登录");
			mDialog.setMessage("正在登录，请稍后...");
			mDialog.show();

			Thread loginThread = new Thread(new LoginThread());
			loginThread.start();
			break;
		case R.id.login_btn_register: 
			// 注册按钮，跳转到注册界面
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			break;
		}
		
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mDialog.cancel();
			switch (msg.what) {
			case 0:
				ToastUtil.show(LoginActivity.this, "登录成功！");
				Intent intent = new Intent();
				if (radioStatus == 0) { // 驾驶员登录成功
					List<String> logDriver = (List<String>)msg.getData().getSerializable("Login_Driver");
					// 保存登录的驾驶员Id和车牌号还有电话
					spEditor.putInt("driverLoginId", Integer.parseInt(logDriver.get(0)));
					spEditor.putString("driverLoginPhone", logDriver.get(2));
					spEditor.putString("driverLoginLicence", logDriver.get(1));
					spEditor.commit();
					
					intent.setClass(LoginActivity.this, DriverInfo.class);
				} else if (radioStatus == 1) { // 管理员
					intent.setClass(LoginActivity.this,
							ManagerMainTabActivity.class);
					// 管理员只要登录成功，就启动更新Service
					startService(new Intent(LoginActivity.this,
							UpdateOrderService.class));
				}
				startActivity(intent);
				finish();
				break;
			case 1:
				ToastUtil.show(LoginActivity.this, "密码错误");
				break;
			case 2:
				ToastUtil.show(LoginActivity.this, "服务器关闭");
				break;
			}
		}
	};
	
	// 登录访问的线程（收尾时再分出去）
	class LoginThread implements Runnable {
		ObjectMapper objectMapper = new ObjectMapper();
		
		@Override
		public void run() {
			String username = inputUsername.getText().toString();
			String password = inputPassword.getText().toString();
			boolean checkstatus = sp.getBoolean("checkstatus", false);
			if (checkstatus) {
				// 获取已经存在的用户名和密码
				String realUsername = sp.getString("username", "");
				String realPassword = sp.getString("password", "");
				if ((!realUsername.equals("")) && (!realPassword.equals(""))) {
					username = realUsername;
					password = realPassword;
				}
			} else {
				// password = md5(password);
			}
			// URL合法，但是这一步并不验证密码是否正确
			boolean loginValidate = loginServer(username, password);
			Message msg = handler.obtainMessage();
			Bundle bundle = new Bundle();
			if (loginValidate) {
				if (!"fail".equals(responseMsg)) {
					msg.what = 0; // 成功
					try { // 将登录成功的User信息存入msg中
						if(radioStatus == 0) {
							List<String> driverInfo = objectMapper.readValue(responseMsg, List.class);
							bundle.putSerializable("Login_Driver", (Serializable)driverInfo);
						} else if(radioStatus == 1) {
							Admin admin = objectMapper.readValue(responseMsg, Admin.class);	
							bundle.putSerializable("Login_Admin", (Serializable)admin);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					msg.what = 1;
				}
			} else {
				msg.what = 2;
			}
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
		
		// 初始化HttpClient，并设置超时
		private HttpClient getHttpClient() {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			HttpClient client = new DefaultHttpClient(httpParams);
			return client;
		}
		
		/**
		 * 登录和注册
		 * @param username 用户名
		 * @param password 密码
		 * @return boolean
		 */
		private boolean loginServer(String username, String password) {
			boolean loginValidate = false;
			String urlStr = "http://";
			if (manager_check.isChecked()) {
				// 使用apache HTTP客户端实现
				urlStr = urlStr + HttpUtils.MY_IP
						+ "/AppServerr/AdminLoginServlet";
			} else if (driver_check.isChecked()) {
				urlStr = urlStr + HttpUtils.MY_IP
						+ "/AppServerr/DriverLoginServlet";
			}
			HttpPost request = new HttpPost(urlStr);
			// 如果传递参数多的话，可以丢传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// 添加用户名和密码
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			try {
				// 设置请求参数项
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpClient client = getHttpClient();
				// 执行请求返回相应
				HttpResponse response = client.execute(request);
				// 判断是否请求成功
				if (response.getStatusLine().getStatusCode() == 200) {
					// 获得响应信息
					responseMsg = EntityUtils.toString(response.getEntity());
					loginValidate = true;
				}
			} catch (Exception e) {
				loginValidate = false;
				e.printStackTrace();
			}
			return loginValidate;
		}
	}
}