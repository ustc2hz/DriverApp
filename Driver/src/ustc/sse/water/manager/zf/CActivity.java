package ustc.sse.water.manager.zf;

import ustc.sse.water.activity.R;
import ustc.sse.water.service.UpdateOrderService;
import ustc.sse.water.zf.ManagerChangePass;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 管理员个人信息界面类. <br>
 * 主要是显示管理员的用户名信息，提供修改密码和退出登录的功能.
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:15:44
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn 周晶鑫 修改15-03-17
 * @version 2.0.0
 */
public class CActivity extends Activity implements OnClickListener {

	private Button updatePwd; // 修改密码
	private Button adminOff; // 退出登录
	private TextView adminName; // 用户名提示

	// 用sharedPreference传递数据
	private SharedPreferences sp = null;
	private SharedPreferences.Editor spEditor = null;
	private String spAdminName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_personal_settings);

		// 取出原有的preferences
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		spAdminName = sp.getString("adminLoginName", "出错了");
		
		initViews();

	}

	/* 初始化视图UI */
	private void initViews() {
		adminName = (TextView) findViewById(R.id.text_manager_name);
		adminName.setText(spAdminName);

		adminOff = (Button) findViewById(R.id.button_admin_logoff);
		updatePwd = (Button) findViewById(R.id.button_update_password);
		adminOff.setOnClickListener(this);
		updatePwd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_update_password: // 修改密码
			Intent intent = new Intent(CActivity.this, ManagerChangePass.class);
			startActivity(intent);
			break;
		case R.id.button_admin_logoff: // 退出登录
			spEditor = sp.edit();
			new AlertDialog.Builder(this).setTitle("提示").setMessage("是否退出登录？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					spEditor.putInt("userLoginStatus", 2);
					spEditor.commit();
					dialog.dismiss();
					// 退出登录时关闭service
					stopService(new Intent(CActivity.this, UpdateOrderService.class));
					finish();
				}

			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).create().show();
			break;
		}
	}

}
