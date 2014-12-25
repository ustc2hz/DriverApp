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

import ustc.sse.water.activity.DriverMainScreen;
import ustc.sse.water.activity.R;
import ustc.sse.water.activity.zjx.DriverInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 登录界面. <br>
 * 用户登录的验证和提交数据
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:14:27
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 2.0.0
 */

public class Manager_Driver_model extends Activity implements
		View.OnClickListener, OnCheckedChangeListener {

	public RadioGroup mRadioGroup1;
	public RadioButton mRadio1, mRadio2;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private EditText et1;// 用户名
	private EditText et2;// 密码
	private EditText et3;// 确认密码
	private Button bt1; // 登录
	private Button bt2; // 返回
	private Button bt3; // 注册

	private CheckBox saveInfoItem;
	private SharedPreferences sp;

	private ProgressDialog mDialog;
	private String responseMsg = "";
	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	// 定义SharedPreferences和Editor
	public SharedPreferences sharedPreferences;
	public SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_driver_model);
		// 定义按钮以及文本等变量

		mRadioGroup1 = (RadioGroup) findViewById(R.id.gendergroup);
		mRadio1 = (RadioButton) findViewById(R.id.driver);
		mRadio2 = (RadioButton) findViewById(R.id.manager);

		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		et3 = (EditText) findViewById(R.id.editText3);
		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		bt3 = (Button) findViewById(R.id.button3);

		saveInfoItem = (CheckBox) findViewById(R.id.chkItem);
		mRadioGroup1.setOnCheckedChangeListener(radiogpchange);

		// 对radioButton做监听
		mRadio1.setOnCheckedChangeListener(this);
		mRadio2.setOnCheckedChangeListener(this);
		mRadio1.setChecked(true);
		tv1.setText("车牌号");
		// 底部加横线
		tv4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// 对button分别做监听
		tv4.setOnClickListener(this);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		et1.setOnClickListener(this);
		et2.setOnClickListener(this);
		et3.setOnClickListener(this);

		// 获取只能被本应用读、写的SharedPreferences对象
		sharedPreferences = getSharedPreferences("login_register",
				Context.MODE_PRIVATE);
		// 对数据进行编辑
		editor = sharedPreferences.edit();
		// 判断SharedPreferences中是否有用户名记录
		int login_register = sharedPreferences.getInt("login_register", 1);
		SharedPreferences shared = getSharedPreferences("loginState",
				Context.MODE_PRIVATE);
		int loginState = shared.getInt("loginState", 1); // 取不到，则默认为1
		if (login_register == 1 || loginState == 1) {
			// 用户未登录或注册
			et3.setVisibility(View.GONE);
			tv3.setVisibility(View.GONE);
			bt2.setVisibility(View.GONE);

		} else {
			// 已注册或登录过
			Intent int2 = new Intent(this, ManagerMain.class);
			startActivity(int2);
		}

		sp = getSharedPreferences("userdata", 0);

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
								et1.setText("");
								et2.setText("");
								// 设置已有值
								et1.setText(realUsername);
								et2.setText(realPassword);
							}
						} else {
							editor.putBoolean("checkstatus", false);
							editor.commit();
							// 清空输入框
							et1.setText("");
							et2.setText("");
						}

					}

				});
	}

	/**
	 * 初始化用户数据
	 */
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
				et1.setText("");
				et2.setText("");
				et1.setText(realUsername);
				et2.setText(realPassword);
			}
		} else {
			saveInfoItem.setChecked(false);
			et1.setText("");
			et2.setText("");
		}

	}

	/**
	 * 检查网络状态
	 */
	public void CheckNetworkState() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
		if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			return;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return;
		showTips();
	}

	/**
	 * 用于显示网络状态提示
	 */
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
				Manager_Driver_model.this.finish();
			}
		});
		builder.create();
		builder.show();
	}

	/**
	 * 对radioButton做监听，根据radioButton显示相应的用户UI
	 */
	private RadioGroup.OnCheckedChangeListener radiogpchange = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if (checkedId == mRadio1.getId()) {
				tv1.setText("车牌号");
				Toast.makeText(getApplicationContext(), "驾驶员", 1).show();
			} else if (checkedId == mRadio2.getId()) {
				tv1.setText("用户名");
				Toast.makeText(getApplicationContext(), "管理员", 1).show();
			}
		}
	};

	/**
	 * 对组件进行监听，设置登录注册的规则
	 */
	@Override
	public void onClick(View v) {
		// 获得edittext中的值
		String edit1 = Manager_Driver_model.this.et1.getText().toString();
		String edit2 = Manager_Driver_model.this.et2.getText().toString();
		String edit3 = Manager_Driver_model.this.et3.getText().toString();
		// 标记输入的登录名和密码是否符合要求
		int flag1 = 0;
		int flag2 = 0;
		int flag3 = 0;
		switch (v.getId()) {
		case R.id.button1:
			// 利用正则表达式来判断
			if ("".equals(edit1)) {
				// 登录名为空
				et1.setError("登录名不能为空");
			} else if (edit1.matches("^[A-Za-z0-9]+$")) {
				// 登录名只包含英文大小写和数字且大于六位数
				flag1 = 1;
			} else {
				et1.setError("登录名只能包含字母和数字");
			}
			if ("".equals(edit2) && edit1.length() >= 6) {
				// 登录密码为空
				et2.setError("登录密码不能为空");
			} else if (edit2.matches("^[A-Za-z0-9]+$")) {
				// 登录密码只包含英文大小写和数字
				flag2 = 1;
			} else {
				et2.setError("登录密码错误");
			}
			if (flag1 == 1 && flag2 == 1) {

				mDialog = new ProgressDialog(Manager_Driver_model.this);
				mDialog.setTitle("登陆");
				mDialog.setMessage("正在登陆服务器，请稍后...");
				mDialog.show();
				Thread loginThread = new Thread(new LoginThread());

				loginThread.start();
				if (mRadio1.isChecked()) { // 驾驶员登录
					Intent intent1 = new Intent(Manager_Driver_model.this,
							DriverInfo.class);
					startActivity(intent1);
					finish();
				} else { // 管理员登录
					Intent intent5 = new Intent(Manager_Driver_model.this,
							ManagerMain.class);
					startActivity(intent5);
					finish();
				}

				editor.putInt("loginState", 1);
				editor.commit();
			}
			break;
		case R.id.button2:
			// 利用正则表达式来判断
			if ("".equals(edit1)) {
				// 注册名为空
				et1.setError("注册名不能为空");
			} else if (edit1.matches("^[A-Za-z0-9]+$")) {
				// 登录名只包含英文大小写和数字
				flag1 = 1;
			} else {
				et1.setError("注册名只能包含字母和数字");
			}
			if ("".equals(edit2) && edit1.length() >= 6) {
				// 登录密码不能且大于六位数
				et2.setError("密码不能为空");
			} else if (edit2.matches("^[A-Za-z0-9]+$")) {
				// 登录密码只包含英文大小写和数字
				flag2 = 1;
			} else {
				et2.setError("密码只能包含字母和数字");
			}
			if (edit3.equals(edit2)) {
				flag3 = 1;
			} else {
				et3.setError("密码不一致");
			}
			if (flag1 == 1 && flag2 == 1 && flag3 == 1) {

				String newusername = et1.getText().toString();
				String newpassword = md5(et2.getText().toString());
				String confirmpwd = md5(et3.getText().toString());

				if (newpassword.equals(confirmpwd)) {
					SharedPreferences sp = getSharedPreferences("userdata", 0);
					Editor editor = sp.edit();
					editor.putString("username", newusername);
					editor.putString("password", newpassword);
					editor.commit();
					mDialog = new ProgressDialog(Manager_Driver_model.this);
					mDialog.setTitle("登陆");
					mDialog.setMessage("正在登陆服务器，请稍后...");
					mDialog.show();
					Thread loginThread1 = new Thread(new RegisterThread());
					loginThread1.start();
					Intent intent2 = new Intent(Manager_Driver_model.this,
							ManagerMain.class);
					startActivity(intent2);
					editor.putInt("login_register", 2);
					// 提交
					editor.commit();

				} else {
					Toast.makeText(getApplicationContext(), "您两次输入的密码不一致！",
							Toast.LENGTH_SHORT).show();
				}

			}
			break;
		// 点击返回按钮
		case R.id.button3:
			Intent intent3 = new Intent(Manager_Driver_model.this,
					DriverMainScreen.class);
			startActivity(intent3);
			finish();
			break;
		case R.id.textView4:
			String string = tv4.getText().toString();
			if (string.equals("立即注册")) {
				et3.setVisibility(View.VISIBLE);
				tv3.setVisibility(View.VISIBLE);
				bt2.setVisibility(View.VISIBLE);
				bt1.setVisibility(View.GONE);
				tv4.setText("返回登录");
			} else if (string.equals("返回登录")) {
				et3.setVisibility(View.GONE);
				tv3.setVisibility(View.GONE);
				bt2.setVisibility(View.GONE);
				bt1.setVisibility(View.VISIBLE);
				tv4.setText("立即注册");
			}
			break;
		}
	}

	private void showDialog(String str) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("注册");
		builder.setMessage(str);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.setClass(Manager_Driver_model.this, ManagerMain.class);
				startActivity(intent);
				finish();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// Handler
	Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.cancel();
				showDialog("注册成功！");
				break;
			case 1:
				mDialog.cancel();
				Toast.makeText(getApplicationContext(), "注册失败",
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

	/**
	 * Handler
	 */
	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				mDialog.cancel();

				Toast.makeText(getApplicationContext(), "登录成功！",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(Manager_Driver_model.this, ManagerMain.class);
				startActivity(intent);
				finish();
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

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	private boolean registerServer(String username, String password) {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://192.168.9.226:8080/managerwebb/loginServlet";
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
				loginValidate = true;
				// 获得响应信息
				responseMsg = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	private boolean loginServer(String username, String password) {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://192.168.1.101:8080/LoginServlet/LoginServlet";
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
				loginValidate = true;
				// 获得响应信息
				responseMsg = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	// LoginThread线程类
	class LoginThread implements Runnable {

		@Override
		public void run() {
			String username = et1.getText().toString();
			String password = et2.getText().toString();
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
						username = et1.getText().toString();
						password = et2.getText().toString();
					}
				}
			} else {
				password = md5(password);
			}
			System.out
					.println("username=" + username + ":password=" + password);

			// URL合法，但是这一步并不验证密码是否正确
			boolean loginValidate = loginServer(username, password);
			System.out.println("----------------------------bool is :"
					+ loginValidate + "----------response:" + responseMsg);
			Message msg = handler2.obtainMessage();
			if (loginValidate) {
				if (responseMsg.equals("success")) {
					msg.what = 0;
					handler2.sendMessage(msg);
				} else {
					msg.what = 1;
					handler2.sendMessage(msg);
				}

			} else {
				msg.what = 2;
				handler2.sendMessage(msg);
			}
		}

	}

	// RegisterThread线程类
	class RegisterThread implements Runnable {

		@Override
		public void run() {
			String username = et1.getText().toString();
			String password = md5(et2.getText().toString());

			// URL合法，但是这一步并不验证密码是否正确
			boolean registerValidate = registerServer(username, password);
			Message msg = handler1.obtainMessage();
			if (registerValidate) {
				if (responseMsg.equals("success")) {
					msg.what = 0;
					handler1.sendMessage(msg);
				} else {
					msg.what = 1;
					handler1.sendMessage(msg);
				}

			} else {
				msg.what = 2;
				handler1.sendMessage(msg);
			}
		}

	}

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

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

}
