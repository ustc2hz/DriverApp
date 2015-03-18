package ustc.sse.water.zf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.activity.zjx.DriverInfo;
import ustc.sse.water.manager.zf.ManagerMainTabActivity;
import ustc.sse.water.service.UpdateOrderService;
import ustc.sse.water.utils.zjx.HttpUtils;
import ustc.sse.water.utils.zjx.ProgressDialogUtil;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.utils.zjx.ValidatorUtils;
import android.app.Activity;
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
 * @author张芳 sa614296@mail.ustc.edu.cn 周晶鑫 修改
 * @version 4.2.0
 */
public class LoginActivity extends Activity implements OnClickListener {
	public static int radioStatus = 0; // 默认是驾驶员
	private static final int USER_DRIVER = 0; // 使用者为驾驶员
	private static final int USER_MANAGER = 1; // 使用者为驾驶员
	private static final int LOGIN_SUCCESS = 0; // 登录成功
	private static final int LOGIN_FAIL = 1; // 登录失败
	private static final int LOGIN_ERROR = 2; // 服务器出错
	
	private String valUserName = null; // 输入的用户名
	private String valPassword = null; // 输入的密码

	private EditText inputPassword; // 密码输入框
	private EditText inputUsername; // 用户名输入框
	private Button loginBtn; // 登录按钮
	private RadioButton manager_check, driver_check; // 驾驶员和管理员的单选按钮
	private ProgressDialogUtil mDialog;
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
			radioStatus = USER_DRIVER;
		} else if (manager_check.isChecked()) {
			radioStatus = USER_MANAGER;
		}
		
		// 按钮事件处理
		switch(v.getId()) {
		case R.id.login_btn_login: // 登录
			boolean loginOk = true; // 默认输入正确
			// 获取输入的内容
			valUserName = inputUsername.getText().toString();
			valPassword = inputPassword.getText().toString();
			// 验证输入是否符合规则
			if(radioStatus == USER_DRIVER) { // 驾驶员登录验证
				if(!ValidatorUtils.licenceValidator(valUserName)) {
					inputUsername.setText("");
					inputUsername.setError("车牌号不符合规则！");
					loginOk = false;
				}
			} else { // 管理员登录验证
				if(!ValidatorUtils.nameValidator(valUserName)) {
					inputUsername.setText("");
					inputUsername.setError("用户名只能由6到16位的字母和数字组成！");
					loginOk = false;
				}
			}
			if(!ValidatorUtils.passwordValidator(valPassword)) {
				inputPassword.setText("");
				inputPassword.setError("密码只能由6到12位的字母和数组组成！");
				loginOk = false;
			}
			if(loginOk) {
				mDialog = new ProgressDialogUtil(LoginActivity.this, "正在登录，请稍后...");
				mDialog.showProgressDialog();

				Thread loginThread = new Thread(new LoginThread());
				loginThread.start();
			}
			break;
		case R.id.login_btn_register: 
			// 注册按钮，跳转到注册界面
			Intent intent = new Intent();
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mDialog.dissmissProgressDialog();
			switch (msg.what) {
			case LOGIN_SUCCESS:
				ToastUtil.show(LoginActivity.this, "登录成功！");
				Intent intent = new Intent();
				if (radioStatus == USER_DRIVER) { // 驾驶员登录成功
					List<String> logDriver = (List<String>)msg.getData().getSerializable("Login_Driver");
					// 保存登录的驾驶员Id和车牌号还有电话
					spEditor.putInt("driverLoginId", Integer.parseInt(logDriver.get(0)));
					if(logDriver.get(2) != null) {
						spEditor.putString("driverLoginPhone", logDriver.get(2));
					} else {
						spEditor.putString("driverLoginPhone", "暂无电话");
					}
					spEditor.putString("driverLoginLicence", logDriver.get(1));
					spEditor.putInt("userLoginStatus", USER_DRIVER);
					
					intent.setClass(LoginActivity.this, DriverInfo.class);
				} else if (radioStatus == USER_MANAGER) { // 管理员
					List<String> logAdmin = (List<String>)msg.getData().getSerializable("Login_Admin");
					// 保存登录的驾驶员Id和车牌号还有电话
					spEditor.putInt("adminLoginId", Integer.parseInt(logAdmin.get(0)));
					if(logAdmin.get(2) != null) {
						spEditor.putString("adminLoginPhone", logAdmin.get(2));
					}else {
						spEditor.putString("adminLoginPhone", "暂无电话");
					}
					spEditor.putString("adminLoginName", logAdmin.get(1));
					spEditor.putInt("userLoginStatus", USER_MANAGER);
					
					intent.setClass(LoginActivity.this,
							ManagerMainTabActivity.class);
					// 管理员只要登录成功，就启动更新Service
					startService(new Intent(LoginActivity.this,
							UpdateOrderService.class));
				}
				if(sp.getBoolean("checkstatus", false)) {
					spEditor.putString("username", valUserName);
					spEditor.putString("password", valPassword);
				}
				spEditor.commit();
				startActivity(intent);
				finish();
				break;
			case LOGIN_FAIL:
				ToastUtil.show(LoginActivity.this, "登录失败");
				break;
			case LOGIN_ERROR:
				ToastUtil.show(LoginActivity.this, "服务器出错");
				break;
			}
		}
	};
	
	// 登录访问的线程（收尾时再分出去）
	class LoginThread implements Runnable {
		ObjectMapper objectMapper = new ObjectMapper();
		
		@Override
		public void run() {
			// 访问服务器
			boolean loginValidate = loginServer(valUserName, valPassword);
			Message msg = handler.obtainMessage();
			Bundle bundle = new Bundle();
			if (loginValidate) {
				if (!"fail".equals(responseMsg)) {
					msg.what = LOGIN_SUCCESS; // 成功
					try { // 将登录成功的User信息存入msg中
						if(radioStatus == USER_DRIVER) {
							List<String> driverInfo = objectMapper.readValue(responseMsg, List.class);
							bundle.putSerializable("Login_Driver", (Serializable)driverInfo);
						} else if(radioStatus == USER_MANAGER) {
							List<String> adminInfo = objectMapper.readValue(responseMsg, List.class);
							bundle.putSerializable("Login_Admin", (Serializable)adminInfo);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					msg.what = LOGIN_FAIL;
				}
			} else {
				msg.what = LOGIN_ERROR;
			}
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
		
		/**
		 * 登录和注册
		 * @param username 用户名
		 * @param password 密码
		 * @return boolean
		 */
		private boolean loginServer(String username, String password) {
			boolean loginValidate = false;
			StringBuffer urlStr = new StringBuffer("http://");
			if (radioStatus == USER_MANAGER) {
				// 使用apache HTTP客户端实现
				urlStr.append(HttpUtils.MY_IP).append("/AppServerr/AdminLoginServlet");
			} else if (radioStatus == USER_DRIVER) {
				urlStr.append(HttpUtils.MY_IP).append("/AppServerr/DriverLoginServlet");
			}
			HttpPost request = new HttpPost(urlStr.toString());
			// 如果传递参数多的话，可以丢传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// 添加用户名和密码
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			try {
				// 设置请求参数项
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpClient client = HttpUtils.getHttpClient();
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