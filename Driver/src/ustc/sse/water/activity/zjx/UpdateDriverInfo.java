package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.thread.UpdateDriverInfoThread;
import ustc.sse.water.utils.zjx.ToastUtil;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * 修改信息类. <br>
 * 驾驶员可以此界面中修改个人信息
 * 在EditText中以hint形式默认显示当前驾驶员的个人信息，驾驶员可以相应修改.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:31:43
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.0.0
 */
public class UpdateDriverInfo extends Activity implements OnClickListener {
	private final static int UPDATE_ALL = 3; // 修改全部
	private final static int UPDATE_PHONE = 1; // 只修改电话
	private final static int UPDATE_PASSWORD = 2; // 只修改密码
	private final static int UPDATE_RESULT = 4; // 修改后的返回的数
	
	private ActionBar ab; // ActionBar
	private EditText driverLicence; // 登录的车牌号
	private EditText driverPhone; // 电话
	private EditText driverOldPwd; // 旧密码
	private EditText driverNewPwd; // 新密码
	private Button updateInfo; //修改按钮
	private SharedPreferences sp;
	private SharedPreferences.Editor spEditor;
	private String valLicence; // 车牌号输入框的值
	private String valPhone; // 电话输入框的值
	private String valPassword; // 新密码输入框的值
	private int spDriverId; // sp中的驾驶员id
	private String spDriverLicence; // sp中的车牌号
	private String spDriverPhone; // sp中的电话
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_driver_information);
		
		// 修改ActionBar样式
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(R.string.driver_update_information);
		
		// 获取存储驾驶员信息的sp
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		spEditor = sp.edit();
		
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		// 一系列EditText
		driverLicence = (EditText) findViewById(R.id.edit_driver_licence);
		driverPhone = (EditText) findViewById(R.id.edit_driver_phone);
		driverOldPwd = (EditText) findViewById(R.id.edit_input_old_pwd);
		driverNewPwd = (EditText) findViewById(R.id.edit_input_new_pwd);
		// 取出SP中的数据
		spDriverLicence = sp.getString("driverLoginLicence", "出错了...");
		spDriverPhone = sp.getString("driverLoginPhone", "暂无联系电话...");
		driverLicence.setHint(spDriverLicence);
		driverPhone.setHint(spDriverPhone);
		
		// 修改按钮
		updateInfo = (Button) findViewById(R.id.button_update_info);
		updateInfo.setOnClickListener(this);
	}
	
	/**
	 * 分三种模式：只修改电话，只修改密码，修改全部
	 * 根据从EditText获取是否为空来区分
	 */
	private int submitMode() {
		int updateMode = UPDATE_PHONE; // 默认只修改电话
		valLicence = driverLicence.getText().toString();
		valPhone = driverPhone.getText().toString();
		valPassword = driverNewPwd.getText().toString();
		String valOldPwd = driverOldPwd.getText().toString();
		// 判断空和“”，来决定是哪种修改模式
		boolean isLic = (valLicence != null) && (!valLicence.trim().equals(""));
		boolean isPwd = (valOldPwd != null) && (!valOldPwd.trim().equals(""))
				&& (valPassword != null) && (!valPassword.trim().equals(""));
		boolean isPhone = (valPhone != null) && (!valPhone.trim().equals(""));
		
		if(isLic) {
			// 只要填写了车牌号，就当作修改全部信息
			updateMode = UPDATE_ALL;
		} else if(!isPwd && isPhone) {
			// 如果只填写了电话，则当作修改电话
			updateMode = UPDATE_PHONE; 
		}else if(isPwd && !isPhone ) {
			// 如果只填写了密码，当作修改密码
			updateMode = UPDATE_PASSWORD;
		}else { // 其他情况都按修改全部处理
			updateMode = UPDATE_ALL;
		}
		
		return updateMode;
	}
	
	/**
	 * 根据修改类型决定传递哪些数据
	 * @param mode 修改模式
	 * @return List<String>
	 */
	private List<String> sendMessages(int mode) {
		List<String> list = new ArrayList<String>(); // 存储信息
		// 模式号和驾驶员Id必须存储
		list.add(String.valueOf(mode));
		spDriverId = sp.getInt("driverLoginId", 1);
		list.add(String.valueOf(spDriverId));
		
		switch(mode) {
		case UPDATE_ALL:// 修改全部
			list.add(valLicence);
			list.add(valPhone);
			list.add(valPassword);
			spDriverLicence = valLicence;
			spDriverPhone = valPhone;
			break;
		case UPDATE_PHONE:// 只修改电话
			spDriverPhone = valPhone;
			list.add(valPhone);
			break;
		case UPDATE_PASSWORD: // 只修改密码
			list.add(valPassword);
			break;
		}
		return list;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_update_info: // 点击修改按钮
			int mode = submitMode();
			List<String> messages = sendMessages(mode);
			UpdateDriverInfoThread udit = new UpdateDriverInfoThread(h, messages);
			udit.start();
			break;
		}
	}
	
	Handler h = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			String result = "error";
			switch(msg.arg1) {
			case UPDATE_RESULT: // 修改完成后的处理
				result = msg.getData().getString("update_result");
				if("success".equals(result)) {
					// 修改sp
					spEditor.putString("driverLoginLicence", spDriverLicence);
					spEditor.putString("driverLoginPhone", spDriverPhone);
					spEditor.commit();
					ToastUtil.show(UpdateDriverInfo.this, "信息修改成功");
				}else {
					ToastUtil.show(UpdateDriverInfo.this, "信息修改失败");
				}
				break;
			}
		};
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home: // ActionBar中向左箭头点击
			finish(); // 返回主菜单
			break;
		}
		return true;
	}
}
