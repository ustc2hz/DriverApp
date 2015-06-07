package ustc.sse.water.manager;

import ustc.sse.water.activity.R;
import ustc.sse.water.service.UpdateOrderService;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

/**
 * 
 * 管理底部导航. <br>
 * 点击不同导航项，跳转到不同Activity.
 * <p>
 * Copyright: Copyright (c) 2015-2-12 上午10:13:12
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳，周晶鑫修改
 * @version 1.0.0
 */
public class ManagerMainTabActivity extends TabActivity implements
		OnCheckedChangeListener {

	private TabHost mTabHost; // TabHost
	private Intent mAIntent; // 跳转正在进行订单界面
	private Intent mBIntent; // 跳转停车场信息发布界面
	private Intent mCIntent; // 跳转个人信息界面
	private Intent mDIntent; // 跳转完成订单界面

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_success);

		this.mAIntent = new Intent(this, ManagerOrderIng.class);
		this.mBIntent = new Intent(this, ParkingCreatement.class);
		this.mCIntent = new Intent(this, ManagerInfo.class);
		this.mDIntent = new Intent(this, ManagerOrderDown.class);
		((RadioButton) findViewById(R.id.radio_button0))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button1))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button2))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.radio_button3))
				.setOnCheckedChangeListener(this);
		setupIntent();

		// 管理员只要登录成功，就启动更新Service
		startService(new Intent(this, UpdateOrderService.class));
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.radio_button1:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.radio_button2:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.radio_button3:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			}
		}
	}

	/**
	 * 初始化导航栏
	 */
	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("A_TAB", R.string.main_home,
				R.drawable.icon_1_n, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", R.string.main_news,
				R.drawable.icon_2_n, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB", R.string.main_manage_date,
				R.drawable.icon_3_n, this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB", R.string.main_order,
				R.drawable.icon_1_n, this.mDIntent));
	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}
}
