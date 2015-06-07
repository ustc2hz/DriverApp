package ustc.sse.water.manager;

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

import ustc.sse.water.activity.R;
import ustc.sse.water.utils.HttpUtils;
import ustc.sse.water.utils.MD5Utils;
import ustc.sse.water.utils.ProgressDialogUtil;
import ustc.sse.water.utils.ToastUtil;
import ustc.sse.water.utils.ValidatorUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * 用户注册界面Activity. <br>
 * 提供驾驶员的注册、停车场管理员的注册.
 * <p>
 * Copyright: Copyright (c) 2015-3-17 下午4:07:13
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳，周晶鑫 修改
 * @version 2.0.0
 */
public class RegisterActivity extends Activity implements OnClickListener {

	private static final int USER_DRIVER = 0; // 驾驶员
	private static final int USER_PARKING_MANAGER = 1; // 停车场管理员
	private static final int REGISTER_SUCCESS = 0; // 注册成功
	private static final int REGISTER_FAIL = 1; // 注册失败
	private static final int REGISTER_ERROR = 2; // 服务器出错

	private int userType; // 用户类型：0表示驾驶员，1表示停车场管理员
	private String inputUsername = null; // 输入的用户名
	private String inputPassword = null; // 输入的密码
	private String inputRepassword = null; // 输入的确认密码
	private String responseMsg = null; // 注册返回信息

	private ProgressDialogUtil mDialog;
	private EditText userName; // 用户名输入框
	private EditText userPassword; // 密码输入框
	private EditText rePassword; // 确认密码输入框
	private Button registerBtn; // 注册按钮
	private Button clearBtn; // 重置按钮
	private ActionBar ab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register);

		userType = LoginActivity.radioStatus; // 获取用户的类型
		// 修改ActionBar样式
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.user_button_register_normal));
		if (userType == USER_DRIVER) {
			ab.setTitle(R.string.title_register_driver);
		} else if (userType == USER_PARKING_MANAGER) {
			ab.setTitle(R.string.title_register_manager);
		}

		initViews();
	}

	/* 初始化视图UI */
	private void initViews() {
		userName = (EditText) findViewById(R.id.edit_register_username);
		// 根据用户的不同，提示不同的输入信息
		if (userType == USER_DRIVER) {
			userName.setHint(R.string.register_hint_licence);
		} else {
			userName.setHint(R.string.register_hint_username);
		}
		userPassword = (EditText) findViewById(R.id.edit_register_password);
		rePassword = (EditText) findViewById(R.id.edit_register_repassword);
		registerBtn = (Button) findViewById(R.id.button_register_submit);
		clearBtn = (Button) findViewById(R.id.button_register_reset);
		registerBtn.setOnClickListener(this);
		clearBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_register_submit: // 点击注册
			boolean inputValidation = true; // 标记输入是否符合规则
			// 获取用户的输入数据
			inputUsername = userName.getText().toString();
			inputPassword = userPassword.getText().toString();
			inputRepassword = rePassword.getText().toString();

			// 验证输入的数据是否符合规则
			if (userType == USER_DRIVER) {
				// 驾驶员用户名的验证
				if (!ValidatorUtils.licenceValidator(inputUsername)) {
					userName.setText("");
					userName.setError("车牌号不符合规则！");
					inputValidation = false;
				} else {
					inputValidation = true;
				}
			} else if (userType == USER_PARKING_MANAGER) {
				// 管理员用户名的验证
				if (!ValidatorUtils.nameValidator(inputUsername)) {
					userName.setText("");
					userName.setError("用户名只能由6到16位的字母和数字组成！");
					inputValidation = false;
				} else {
					inputValidation = true;
				}
			}
			// 密码的验证
			else if (!inputPassword.equals(inputRepassword)) {
				// 密码和确认密码不一致
				inputValidation = false;
				rePassword.setText("");
				rePassword.setError("两次密码不一致！");
			} else if (!ValidatorUtils.passwordValidator(inputPassword)) {
				// 密码不符合规则
				inputValidation = false;
				userPassword.setText("");
				rePassword.setText("");
				userPassword.setError("密码只能由6到12位的字母和数组组成！");
			} else {
				inputValidation = true;
			}

			// 验证输入符合规则后，再注册
			if (inputValidation) {
				mDialog = new ProgressDialogUtil(RegisterActivity.this,
						"正在注册，请稍后...");
				mDialog.showProgressDialog();
				Thread loginThread = new Thread(new RegisterThread());
				loginThread.start();
			}
			break;
		case R.id.button_register_reset:
			// 重置输入框
			userName.setText("");
			userPassword.setText("");
			rePassword.setText("");
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // ActionBar中向左箭头点击
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class); // 返回登录界面
			startActivity(intent);
			finish();
			break;
		}
		return true;
	}

	/**
	 * 注册成功对话框，点击“确定”后跳转到登录界面
	 * 
	 * @param str
	 *            提示信息
	 */
	private void showDialog(String str) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("注册成功");
		builder.setMessage(str);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mDialog.dissmissProgressDialog();
			switch (msg.what) {
			case REGISTER_SUCCESS:

				SharedPreferences.Editor editor = getSharedPreferences(
						"manager_message", MODE_PRIVATE).edit();
				editor.putString("num", "空");
				editor.putString("price_ten", "空");
				editor.putString("price_twenty", "空");
				editor.putString("price_thirty", "空");
				editor.putString("pprice_ten", "空");
				editor.putString("pprice_twenty", "空");
				editor.putString("pprice_thirty", "空");
				editor.putString("name", "空");
				editor.putString("location", "空");
				editor.putString("phone", "空");
				editor.commit();
				showDialog("跳转到登录界面，登录系统！");
				break;
			case REGISTER_FAIL:
				ToastUtil.show(RegisterActivity.this, "注册失败！");
				break;
			case REGISTER_ERROR:
				ToastUtil.show(RegisterActivity.this, "服务器出错！");
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class); // 返回登录界面
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 注册线程类
	class RegisterThread implements Runnable {

		@Override
		public void run() {
			boolean registerValidate = registerServer(inputUsername,
					inputPassword);
			Message msg = handler.obtainMessage();
			if (registerValidate) {
				if ("success".equals(responseMsg)) {
					// 注册成功
					msg.what = REGISTER_SUCCESS;
				} else {
					// 注册失败
					msg.what = REGISTER_FAIL;
				}
			} else {
				// 服务器出错
				msg.what = REGISTER_ERROR;
			}

			handler.sendMessage(msg);
		}

		/**
		 * 访问服务器，注册该用户的信息
		 * 
		 * @param username
		 *            用户名
		 * @param password
		 *            密码
		 * @return boolean
		 */
		private boolean registerServer(String username, String password) {
			boolean regiseterResult = false;
			StringBuffer urlStr = new StringBuffer(HttpUtils.LBS_SERVER_PATH);
			// 使用apache HTTP客户端实现
			if (userType == USER_DRIVER) { // 驾驶员注册
				urlStr.append("/DriverRegisterServlet");
			} else if (userType == USER_PARKING_MANAGER) {// 管理员注册
				urlStr.append("/AdminRegisterServlet");
			}

			HttpPost request = new HttpPost(urlStr.toString());
			// 如果传递参数多的话，可以丢传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String passwordMD5 = MD5Utils.string2MD5(password); // MD5加密
			Log.i("--->>Re_MD5", passwordMD5);
			// 添加用户名和密码
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", passwordMD5));
			try {
				// 设置请求参数项
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				HttpClient client = HttpUtils.getHttpClient();
				// 执行请求返回相应
				HttpResponse response = client.execute(request);
				// 判断是否请求成功
				if (response.getStatusLine().getStatusCode() == 200) {
					regiseterResult = true;
					// 获得响应信息
					responseMsg = EntityUtils.toString(response.getEntity());
				}
			} catch (Exception e) {
				e.printStackTrace();
				regiseterResult = false;
			}
			return regiseterResult;
		}
	}
}
