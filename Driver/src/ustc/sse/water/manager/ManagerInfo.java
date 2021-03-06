package ustc.sse.water.manager;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.AdminOrderShow;
import ustc.sse.water.service.UpdateOrderService;
import ustc.sse.water.utils.ConstantKeep;
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
public class ManagerInfo extends Activity implements OnClickListener {

	private Button updatePwd; // 修改密码
	private Button adminOff; // 退出登录
	private TextView adminName; // 用户名提示
	private TextView moneyStatistic; // 订单预订金统计

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
		moneyStatistic = (TextView) findViewById(R.id.text_manager_orderStatistics);
		moneyStatistic.setText(statisticOrder()+"元");
		adminOff = (Button) findViewById(R.id.button_admin_logoff);
		updatePwd = (Button) findViewById(R.id.button_update_password);
		adminOff.setOnClickListener(this);
		updatePwd.setOnClickListener(this);
	}

	/**
	 * 统计已完成订单中赚取的预订金总额
	 */
	public String statisticOrder() {
		if(ConstantKeep.aosDown != null) {
			int sum = 0;
			for(AdminOrderShow aos : ConstantKeep.aosDown) {
				 sum += Integer.parseInt(aos.getOrderPrice());
			}
			return String.valueOf(sum);
		}
		return "0";
	}
	
	/**
	 * 清空SharedPreference中的注册停车场信息
	 */
	public void clearManagerParking() {
		SharedPreferences.Editor managerEditor = this.getSharedPreferences(
				"manager_message", MODE_PRIVATE).edit();
		managerEditor.putString("name", "空");
		managerEditor.putString("id", "空");
		managerEditor.putString("location", "空");
		managerEditor.putString("phone", "空");
		managerEditor.putString("num", "空");
		managerEditor.putString("price_ten", "空");
		managerEditor.putString("price_twenty", "空");
		managerEditor.putString("price_thirty", "空");
		managerEditor.putString("pprice_ten", "空");
		managerEditor.putString("pprice_twenty", "空");
		managerEditor.putString("pprice_thirty", "空");
		managerEditor.commit();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_update_password: // 修改密码
			Intent intent = new Intent(ManagerInfo.this,
					ManagerChangePass.class);
			startActivity(intent);
			break;
		case R.id.button_admin_logoff: // 退出登录
			spEditor = sp.edit();
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("是否退出登录？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									spEditor.putInt("userLoginStatus", 2);
									spEditor.commit();
									clearManagerParking();
									dialog.dismiss();
									// 退出登录时关闭service
									stopService(new Intent(ManagerInfo.this,
											UpdateOrderService.class));
									finish();
								}

							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();
			break;
		}
	}

}
