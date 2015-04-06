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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * Activity类. <br>
 * 驾驶员可以此界面中修改密码
 * <p>
 * Copyright: Copyright (c) 2014-12-21 上午10:31:43
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 2.3.0
 */
public class UpdateDriverPwd extends Activity implements OnClickListener {
	private final static int UPDATE_PASSWORD = 2; // 只修改密码
	private final static int UPDATE_RESULT = 4; // 修改后的返回的数
	
	private ActionBar ab; // ActionBar
	private EditText inputOldPwd; // 旧密码
	private EditText inputNewPwd; // 新密码
	private EditText inputRePwd; // 确认密码
	private Button updatePwd; //修改按钮
	private SharedPreferences sp;
	
	private String valOldPwd; // 旧密码的值
	private String valRePwd; // 确认密码的值
	private String valNewPwd; // 新密码输入框的值
	private int spDriverId; // sp中的驾驶员id
	private boolean flag = false; // 标志输入是否符合规范
	private List<String> list = null; // 存储要提交的信息
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_driver_information);
		
		// 修改ActionBar样式
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.user_button_register_normal));
		ab.setTitle(R.string.driver_update_password);
		
		// 获取存储驾驶员信息的sp
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		
		initView();
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		// 一系列EditText
		inputOldPwd = (EditText) findViewById(R.id.edit_input_old_pwd);
		inputNewPwd = (EditText) findViewById(R.id.edit_input_new_pwd);
		inputRePwd = (EditText) findViewById(R.id.edit_input_re_pwd);
		inputOldPwd.addTextChangedListener(new TextWatchListener());
		inputNewPwd.addTextChangedListener(new TextWatchListener());
		inputRePwd.addTextChangedListener(new TextWatchListener());
		
		// 修改按钮
		updatePwd = (Button) findViewById(R.id.button_update_info);
		updatePwd.setEnabled(false);
		updatePwd.setClickable(false);
		updatePwd.setOnClickListener(this);
	}
	
	/* 只有用户在三个输入框都输入数据时才让按钮可点击 */
	class TextWatchListener implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// 只有输入内容时才将按钮设为可点击
			if (s.length() > 0) {
				updatePwd.setClickable(true);
				updatePwd.setEnabled(true);
			} else {
				updatePwd.setClickable(false);
				updatePwd.setEnabled(false);
			}
			
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
		
	}
	
	/**
	 * 验证输入是否符合规则
	 */
	private void passwordValidator() {
		valOldPwd = inputOldPwd.getText().toString();
		valNewPwd = inputNewPwd.getText().toString();
		valRePwd = inputRePwd.getText().toString();
		if (!ValidatorUtils.passwordValidator(valOldPwd)) {
			inputOldPwd.setText("");
			inputOldPwd.setError("只能由6到12位的字母和数字组成！");
			flag = false;
		} else if(!valNewPwd.equals(valRePwd)) {
			inputNewPwd.setText("");
			inputRePwd.setText("");
			inputRePwd.setError("两次密码不一致！");
			flag = false;
		} else if(!ValidatorUtils.passwordValidator(valNewPwd)) {
			inputNewPwd.setText("");
			inputRePwd.setText("");
			inputNewPwd.setError("只能由6到12位的字母和数字组成！");
			flag = false;
		} else { // 输入符合规则
			flag = true;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_update_info: // 点击修改按钮
			list = new ArrayList<String>(); // 存储信息
			// 模式号和驾驶员Id必须存储
			list.add(String.valueOf(UPDATE_PASSWORD));
			spDriverId = sp.getInt("driverLoginId", 0);
			list.add(String.valueOf(spDriverId));

			passwordValidator(); // 验证
			
			if (flag) { // 验证ok，存入新密码和旧密码
				list.add(valOldPwd);
				list.add(valNewPwd);
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
					ToastUtil.show(UpdateDriverPwd.this, "修改密码成功");
				}else if("oldError".equals(result)){
					inputOldPwd.setText("");
					inputOldPwd.setError("请输入正确的旧密码！");
					ToastUtil.show(UpdateDriverPwd.this, "旧密码不正确");
				} else {
					ToastUtil.show(UpdateDriverPwd.this, "修改密码失败");
				}
				break;
			}
		};
	};
	
	/**
	 * 修改确认对话框
	 */
	private void submitDialog() {
		Dialog dialog = new AlertDialog.Builder(UpdateDriverPwd.this)
				.setTitle("提示")
				.setMessage("是否确定修改密码？")
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
