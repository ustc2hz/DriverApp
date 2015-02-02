package ustc.sse.water.zf;

import java.security.MessageDigest;
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

import ustc.sse.water.activity.R;
import ustc.sse.water.managermain.zf.ManagerMainTabActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * 
 * 登录界面类 <br>
 * 该类用来显示驾驶员和管理员的登录和注册等。
 * <p>
 * Copyright: Copyright (c) 2014-11-13 下午10:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author张芳 sa614296@mail.ustc.edu.cn
 * @version 3.0.0
 */
public class LoginActivity extends Activity {
	// LoginThread线程类
	class LoginThread implements Runnable {
		@Override
		public void run() {
			String username = inputUsername.getText().toString();
			String password = inputPassword.getText().toString();
			Log.i("--->>>", password);
			boolean checkstatus = sp.getBoolean("checkstatus", false);
			if (checkstatus) {
				// 获取已经存在的用户名和密码
				String realUsername = sp.getString("username", "");
				String realPassword = sp.getString("password", "");
				if ((!realUsername.equals("")) && !(realUsername == null)
						|| (!realPassword.equals(""))
						|| !(realPassword == null)) {
					if (username.equals(realUsername)
							&& password.equals(realPassword)) {
						username = inputUsername.getText().toString();
						password = inputPassword.getText().toString();
					}
				}
			} else {
				// password = md5(password);
			}
			System.out
					.println("username=" + username + ":password=" + password);

			// URL合法，但是这一步并不验证密码是否正确
			boolean loginValidate = loginServer(username, password);
			System.out.println("----------------------------bool is :"
					+ loginValidate + "----------response:" + responseMsg);
			Message msg = handler.obtainMessage();
			if (loginValidate) {
				Log.i("msg", responseMsg);
				if (responseMsg.equals("success")) {
					Log.i("what", 0 + "");
					msg.what = 0;
					handler.sendMessage(msg);
				} else {
					Log.i("msg", responseMsg);
					Log.i("what", 1 + "");
					msg.what = 1;
					handler.sendMessage(msg);
				}

			} else {
				msg.what = 2;
				handler.sendMessage(msg);
			}
		}

	}

	private static final int LOGIN_OK = 1;
	public static int radioStatus = 0; // 默认是驾驶员
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟

	/**
	 * MD5单向加密，32位，用于加密密码，因为明文密码在信道中传输不安全，明文保存在本地也不安全
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	// Handler
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "登录成功！",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this,
						ManagerMainTabActivity.class);
				startActivity(intent);
				// finish();
				break;
			case 1:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "密码错误",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "URL验证失败",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};
	private EditText inputPassword;
	private EditText inputUsername;
	/** Called when the activity is first created. */
	private Button loginBtn;
	private RadioButton manager_check, driver_check;
	private ProgressDialog mDialog;
	private Button registerBtn;

	private String responseMsg;

	private CheckBox saveInfoItem;

	private SharedPreferences sp;

	// 检查网络状态
	public void CheckNetworkState() {
		boolean flag = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			return;
		}
		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return;
		}
		showTips();
	}

	private void checkOut() {
		// TODO Auto-generated method stub
		if (!manager_check.isChecked() || driver_check.isChecked()) {
			loginBtn.setClickable(false);
		}
	}

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	// 判断是否记住密码，默认记住
	private boolean isRemembered() {
		try {
			if (saveInfoItem.isChecked()) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	// 初始化用户数据
	private void LoadUserdata() {
		boolean checkstatus = sp.getBoolean("checkstatus", false);
		if (checkstatus) {
			saveInfoItem.setChecked(true);
			// 载入用户信息
			// 获取已经存在的用户名和密码
			String realUsername = sp.getString("username", "");
			String realPassword = sp.getString("password", "");

			if ((!realUsername.equals("")) && !(realUsername == null)
					|| (!realPassword.equals("")) || !(realPassword == null)) {
				inputUsername.setText("");
				inputPassword.setText("");
				inputUsername.setText(realUsername);
				inputPassword.setText(realPassword);
			}
		} else {
			saveInfoItem.setChecked(false);
			inputUsername.setText("");
			inputPassword.setText("");
		}

	}

	private boolean loginServer(String username, String password) {
		boolean loginValidate = false;
		String urlStr = "";
		Log.i("--->>", driver_check.isChecked() + "");
		Log.i("--->>", manager_check.isChecked() + "");
		if (manager_check.isChecked()) {
			// 使用apache HTTP客户端实现
			urlStr = "http://192.168.9.179:8080/AppServerr/AdminLoginServlet";
		} else if (driver_check.isChecked()) {
			urlStr = "http://192.168.9.179:8080/AppServerr/DriverLoginServlet";
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
			Log.i("code", response.getStatusLine().getStatusCode() + "");
			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == 200) {
				// 获得响应信息
				responseMsg = EntityUtils.toString(response.getEntity());
				Log.i("-->>msg", responseMsg);
				loginValidate = true;
			}
		} catch (Exception e) {
			loginValidate = false;
			e.printStackTrace();
		}
		return loginValidate;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.login);
		loginBtn = (Button) findViewById(R.id.login_btn_login);
		registerBtn = (Button) findViewById(R.id.login_btn_zhuce);
		inputUsername = (EditText) findViewById(R.id.login_edit_account);
		inputPassword = (EditText) findViewById(R.id.login_edit_pwd);
		saveInfoItem = (CheckBox) findViewById(R.id.login_cb_savepwd);
		manager_check = (RadioButton) findViewById(R.id.check_manager);
		driver_check = (RadioButton) findViewById(R.id.check_driver);
		/*
		 * loginBtn.setOnClickListener(this);
		 * registerBtn.setOnClickListener(this);
		 */

		checkOut();
		driver_check.setChecked(true);

		sp = getSharedPreferences("userdata", 0);

		if (driver_check.isChecked()) {
			manager_check.setChecked(false);
		}
		// 初始化数据
		LoadUserdata();

		// 检查网络
		CheckNetworkState();

		// 监听记住密码选项
		saveInfoItem
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						// 载入用户信息
						Editor editor = sp.edit();
						if (saveInfoItem.isChecked()) {
							// 获取已经存在的用户名和密码
							String realUsername = sp.getString("username", "");
							String realPassword = sp.getString("password", "");
							editor.putBoolean("checkstatus", true);
							editor.commit();

							if ((!realUsername.equals(""))
									&& !(realUsername == null)
									|| (!realPassword.equals(""))
									|| !(realPassword == null)) {
								// 清空输入框
								inputUsername.setText("");
								inputPassword.setText("");
								// 设置已有值
								inputUsername.setText(realUsername);
								inputPassword.setText(realPassword);
							}
						} else {
							editor.putBoolean("checkstatus", false);
							editor.commit();
							// 清空输入框
							inputUsername.setText("");
							inputPassword.setText("");
						}

					}

				});
		// 登录
		loginBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog = new ProgressDialog(LoginActivity.this);
				mDialog.setTitle("登陆");
				mDialog.setMessage("正在登陆服务器，请稍后...");
				mDialog.show();

				Log.i("--->>>", "登录线程");
				Thread loginThread = new Thread(new LoginThread());
				loginThread.start();

			}

		});

		registerBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (driver_check.isChecked()) {
					radioStatus = 0;
				} else if (manager_check.isChecked()) {
					radioStatus = 1;
				}
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	private void showTips() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("没有可用网络");
		builder.setMessage("当前网络不可用，是否设置网络？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 如果没有网络连接，则进入网络设置界面
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				LoginActivity.this.finish();
			}
		});
		builder.create();
		builder.show();
	}

}
