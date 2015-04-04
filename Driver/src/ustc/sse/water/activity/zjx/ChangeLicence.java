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
 * 驾驶员更换车牌号的界面.
 * <p>
 * Copyright: Copyright (c) 2015-3-26 下午9:44:39
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class ChangeLicence extends Activity implements OnClickListener {
	private final static int CHANGE_LICENCE = 3;
	private final static int UPDATE_RESULT = 4; // 修改后的返回的数
	private ActionBar ab;
	private EditText inputLicence; // 新车牌号的输入框
	private Button changeLicence; // 更换按钮
	private String newLicence = null; // 新车牌号
	private int driverId = 0; // 驾驶员id
	private List<String> list = null; // 存储要提交的信息
	private SharedPreferences sp;
	private SharedPreferences.Editor spEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_function_change_licence);

		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(false);
		ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.user_button_register_normal));
		ab.setTitle("更换车牌");
		
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		spEditor = sp.edit();

		initViews();
	}

	private void initViews() {
		inputLicence = (EditText) findViewById(R.id.edit_driver_new_licence);
		changeLicence = (Button) findViewById(R.id.button_driver_change_licence);
		changeLicence.setEnabled(false);
		changeLicence.setClickable(false);
		inputLicence.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 只有输入内容时才将按钮设为可点击
				if(s.length() > 0) {
					changeLicence.setClickable(true);
					changeLicence.setEnabled(true);
				}else {
					changeLicence.setClickable(false);
					changeLicence.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		changeLicence.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		list = new ArrayList<String>();
		switch (v.getId()) {
		case R.id.button_driver_change_licence: // 更换
			newLicence = inputLicence.getText().toString();
			list.add(String.valueOf(CHANGE_LICENCE));
			driverId = sp.getInt("driverLoginId", 0);
			list.add(String.valueOf(driverId));
			list.add(newLicence);
			if (ValidatorUtils.licenceValidator(newLicence)) {
				new AlertDialog.Builder(this)
						.setTitle("提示")
						.setMessage("亲，确认换车牌吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 开启线程修改车牌号
										// 只有当各自模式中输入都符合规则才能修改
										UpdateDriverInfoThread udit = new UpdateDriverInfoThread(h, list);
										udit.start();
										dialog.dismiss();
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
			} else {
				inputLicence.setText("");
				inputLicence.setError("无效车牌号！");
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
					spEditor.putString("driverLoginLicence", newLicence);
					spEditor.commit();
					ToastUtil.show(ChangeLicence.this, "车牌更换成功");
				}else {
					ToastUtil.show(ChangeLicence.this, "车牌更换失败");
				}
				break;
			}
		};
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // ActionBar中向左箭头点击
			finish(); // 返回主菜单
			break;
		}
		return true;
	}

}
