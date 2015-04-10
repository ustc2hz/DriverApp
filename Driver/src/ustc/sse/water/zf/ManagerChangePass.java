package ustc.sse.water.zf;

import ustc.sse.water.activity.R;
import ustc.sse.water.thread.UpdateAdminInfoThread;
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
 * 修改密码的Activity. <br>
 * 填写新旧密码
 * <p>
 * Copyright: Copyright (c) 2014-11-14 下午7:32:09
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn 周晶鑫 修改
 * @version 3.0.0
 */
public class ManagerChangePass extends Activity implements OnClickListener {

	private Button submitUpdate; // 提交修改按钮
	private EditText oldPassword; // 旧密码输入框
	private EditText newPassword; // 新密码输入框
	private EditText rePassword; // 确认密码输入框
	private ActionBar ab;
	
	private SharedPreferences sp;
	private int adminId;
	private String oldPwd = null; // 输入的旧密码
	private String newPwd = null; // 输入的新密码
	private String rePwd = null; // 输入的确认密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_change_password);
		// 设置ActionBar的样式
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle(R.string.driver_update_password);
		// 取出admin的id
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		adminId = sp.getInt("adminLoginId", 0);
		
		initView();
	}

	/* 初始化视图组件 */
	private void initView() {
		oldPassword = (EditText) findViewById(R.id.edit_admin_oldPassword);
		newPassword = (EditText) findViewById(R.id.edit_admin_newPassword);
		rePassword = (EditText) findViewById(R.id.edit_admin_repassword);
		
		submitUpdate = (Button) findViewById(R.id.button_update_admin_pwd);
		submitUpdate.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_update_admin_pwd: // 修改
			oldPwd = oldPassword.getText().toString().trim();
			newPwd = newPassword.getText().toString().trim();
			rePwd = rePassword.getText().toString().trim();
			
			// 检验规则检验输入
			if(!newPwd.equals(rePwd)) {
				newPassword.setText("");
				rePassword.setText("");
				rePassword.setError("两次密码不一致！");
			} else {
				if (ValidatorUtils.passwordValidator(oldPwd)
						&& ValidatorUtils.passwordValidator(newPwd)) {
					submitDialog();
				} else {
					oldPassword.setText("");
					newPassword.setText("");
					rePassword.setText("");
					oldPassword.setError("密码只能由6到12位的字母和数组组成！");
					newPassword.setError("密码只能由6到12位的字母和数组组成！");
					rePassword.setError("密码只能由6到12位的字母和数组组成！");
				}
			}
			
			break;
		}

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // ActionBar中向左箭头点击
			finish(); // 返回主菜单
			break;
		}
		return true;
	}
	
	/**
	 * 修改确认对话框
	 */
	private void submitDialog() {
		Dialog dialog = new AlertDialog.Builder(ManagerChangePass.this)
				.setTitle("密码修改")
				.setMessage("是否确定修改？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// 启动线程修改密码
						UpdateAdminInfoThread uait = new UpdateAdminInfoThread(h, adminId, oldPwd, newPwd);
						uait.start();
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

	Handler h = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.arg1) {
			case 77:
				String result = msg.getData().getString("update_result");
				if("fail".equals(result)) {
					ToastUtil.show(ManagerChangePass.this, "修改密码失败");
					oldPassword.setText("");
				} else if("oldError".equals(result)) {
					ToastUtil.show(ManagerChangePass.this, "旧密码错误");
					oldPassword.setSelectAllOnFocus(true);
				} else {
					ToastUtil.show(ManagerChangePass.this, "修改密码成功");
					oldPassword.setText("");
				} 
				newPassword.setText("");
				rePassword.setText("");
				break;
			}
			
		};
	};
	
}
