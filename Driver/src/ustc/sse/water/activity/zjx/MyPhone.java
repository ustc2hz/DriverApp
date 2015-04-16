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
 * 让驾驶员可以添加和修改联系电话.
 * <p>
 * Copyright: Copyright (c) 2015-3-27 上午10:37:05
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author 周晶鑫
 * @version 1.0.0
 */
public class MyPhone extends Activity implements OnClickListener {
	private final static int ADD_UPDATE_PHONE = 1; // 修改和添加电话
	private final static int UPDATE_RESULT = 4; // 修改后的返回的数
	private ActionBar ab; 
	private EditText inputPhone; // 电话号码的输入框
	private Button addUpdate; // 添加或者修改按钮
	private String valPhone = null; // 输入的电话
	private int driverId = 0; // 驾驶员id
	private List<String> list = null; // 存储要提交的信息
	private boolean flag = false; // 标记是否已经有驾驶员的电话
	private SharedPreferences sp; 
	private SharedPreferences.Editor spEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_function_add_phone);
		
		ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setBackgroundDrawable(getResources().getDrawable(R.drawable.user_button_register_normal));
		ab.setDisplayShowHomeEnabled(false);
		ab.setTitle("我的电话");
		
		sp = getSharedPreferences("userdata", MODE_PRIVATE);
		String temp = sp.getString("driverLoginPhone", "empty");
		if(!"empty".equals(temp) && !"暂无电话".equals(temp)) {
			flag = true; // 说明已经有电话
		}
		spEditor = sp.edit();
		
		initViews();
	}

	private void initViews() {
		inputPhone = (EditText) findViewById(R.id.edit_driver_phone_add_update);
		addUpdate = (Button) findViewById(R.id.button_driver_add_update_phone);
		if(flag) { // 已有电话，则按钮为修改按钮
			addUpdate.setText(R.string.driver_sure_update_info);
		}
		addUpdate.setEnabled(false);
		addUpdate.setClickable(false);
		inputPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 只有输入内容时才将按钮设为可点击
				if(s.length() > 0) {
					addUpdate.setClickable(true);
					addUpdate.setEnabled(true);
				}else {
					addUpdate.setClickable(false);
					addUpdate.setEnabled(false);
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
		
		addUpdate.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		list = new ArrayList<String>();
		switch (v.getId()) {
		case R.id.button_driver_add_update_phone: // 更换
			valPhone = inputPhone.getText().toString();
			list.add(String.valueOf(ADD_UPDATE_PHONE));
			driverId = sp.getInt("driverLoginId", 0); // 可能需要判断是否为0
			list.add(String.valueOf(driverId));
			list.add(valPhone);
			if (ValidatorUtils.phoneValidator(valPhone)) {
				new AlertDialog.Builder(this)
						.setTitle("提示")
						.setMessage("亲，确认此手机号吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
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
				inputPhone.setText("");
				inputPhone.setError("无效手机号！");
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
					spEditor.putString("driverLoginPhone", valPhone);
					spEditor.commit();
					if(flag) {
						ToastUtil.show(MyPhone.this, "修改电话成功");
					} else {
						ToastUtil.show(MyPhone.this, "添加电话成功");
					}
				}else {
					if(flag) {
						ToastUtil.show(MyPhone.this, "修改电话失败");
					} else {
						ToastUtil.show(MyPhone.this, "添加电话失败");
					}
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
