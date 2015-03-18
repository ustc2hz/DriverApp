package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.List;

import ustc.sse.water.activity.R;
import ustc.sse.water.thread.UpdateDriverInfoThread;
import ustc.sse.water.utils.zjx.ToastUtil;
import ustc.sse.water.utils.zjx.ValidatorUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
 * 修改驾驶员信息Activity类. <br>
 * 驾驶员可以此界面中修改个人信息：车牌号、密码、电话
 * 在EditText中以hint形式默认显示当前驾驶员的个人信息，驾驶员可以相应修改.
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:31:43
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.3.0
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
	private List<String> list = null; // 存储要提交的信息
	
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
	
	@Override
	public void onClick(View v) {
		boolean flag = true; // 记录是否满足修改要求
		switch(v.getId()) {
		case R.id.button_update_info: // 点击修改按钮
			int mode = submitMode();
			
			list = new ArrayList<String>(); // 存储信息
			// 模式号和驾驶员Id必须存储
			list.add(String.valueOf(mode));
			spDriverId = sp.getInt("driverLoginId", 1);
			list.add(String.valueOf(spDriverId));
			
			switch(mode) {
			case UPDATE_ALL:// 修改全部
				if(!ValidatorUtils.licenceValidator(valLicence)) {
					driverLicence.setText("");
					driverLicence.setError("车牌号不符合规则！");
					flag = false;
				} else if(!ValidatorUtils.phoneValidator(valPhone)) {
					driverPhone.setText("");
					driverPhone.setError("手机号码不符合规则！");
					flag = false;
				} else if(!ValidatorUtils.passwordValidator(valPassword)) {
					driverOldPwd.setText("");
					driverNewPwd.setText("");
					driverOldPwd.setError("密码只能由6到12位的字母和数组组成！");
					flag = false;
				} else {
					list.add(valLicence);
					list.add(valPhone);
					list.add(valPassword);
					spDriverLicence = valLicence;
					spDriverPhone = valPhone;
				}
				break;
			case UPDATE_PHONE:// 只修改电话
				if(!ValidatorUtils.phoneValidator(valPhone)) {
					driverPhone.setText("");
					driverPhone.setError("手机号码不符合规则！");
					flag = false;
				} else {
					spDriverPhone = valPhone;
					list.add(valPhone);
				}
				break;
			case UPDATE_PASSWORD: // 只修改密码
				if(!ValidatorUtils.passwordValidator(valPassword)) {
					driverOldPwd.setText("");
					driverNewPwd.setText("");
					driverOldPwd.setError("密码只能由6到12位的字母和数组组成！");
					flag = false;
				} else {
					list.add(valPassword);
				}
				break;
			}
			
			if(flag) {
				submitDialog();
			}
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
	
	/**
	 * 修改确认对话框
	 */
	private void submitDialog() {
		Dialog dialog = new AlertDialog.Builder(UpdateDriverInfo.this)
				.setTitle("密码修改")
				.setMessage("是否确定修改？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// 只有当各自模式中输入都符合规则才能修改
						UpdateDriverInfoThread udit = new UpdateDriverInfoThread(h, list);
						udit.start();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).create();
		dialog.show();
	}
	
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
