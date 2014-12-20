package ustc.sse.water.zf;

import ustc.sse.water.activity.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:15:44
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 1.0.0
 */
public class ManagerSettings extends Activity implements OnClickListener {

	// 申明变量
	Button bt1, bt2, bt3, bt4, bt5, bt6;
	EditText et1, et2, et3, et4, et5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_personal_settings);

		// 通过findViewById方法获得对应的button
		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);
		bt3 = (Button) findViewById(R.id.button3);
		bt4 = (Button) findViewById(R.id.button4);
		bt5 = (Button) findViewById(R.id.button5);
		bt6 = (Button) findViewById(R.id.button6);
		// 通过findViewById方法获得对应的edittext
		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		et3 = (EditText) findViewById(R.id.editText3);
		et4 = (EditText) findViewById(R.id.editText4);
		et5 = (EditText) findViewById(R.id.editText5);
		// 对button做监听
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
		bt6.setOnClickListener(this);
		// 对edittext做监听
		et1.setOnClickListener(this);
		et2.setOnClickListener(this);
		et3.setOnClickListener(this);
		et4.setOnClickListener(this);
		et5.setOnClickListener(this);
		// 设置edittext不可编辑
		et1.setEnabled(false);
		et2.setEnabled(false);
		et3.setEnabled(false);
		et4.setEnabled(false);
		et5.setEnabled(false);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			// 点击返回跳转
			Intent intent = new Intent(ManagerSettings.this, ManagerMain.class);
			startActivity(intent);
			break;
		case R.id.button2:
			// 点击button2修改相关信息，edittext恢复可编辑，按钮成为取消修改，点击取下修改edittext恢复不可编辑
			if (bt2.getText().equals("修改")) {
				et1.setEnabled(true);
				bt2.setText("取消修改");
			} else if (bt2.getText().equals("取消修改")) {
				et1.setEnabled(false);
				bt2.setText("修改");
			}
			break;
		case R.id.button3:
			Intent intent1 = new Intent(ManagerSettings.this,
					ManagerChangePass.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.button4:
			if (bt4.getText().equals("修改")) {
				et3.setEnabled(true);
				bt4.setText("取消修改");
			} else if (bt4.getText().equals("取消修改")) {
				et3.setEnabled(false);
				bt4.setText("修改");
			}
			break;
		case R.id.button5:
			if (bt5.getText().equals("修改")) {
				et4.setEnabled(true);
				bt5.setText("取消修改");
			} else if (bt5.getText().equals("取消修改")) {
				et4.setEnabled(false);
				bt5.setText("修改");
			}
			break;
		case R.id.button6:
			if (bt6.getText().equals("修改")) {
				et5.setEnabled(true);
				bt6.setText("取消修改");
			} else if (bt6.getText().equals("取消修改")) {
				et5.setEnabled(false);
				bt6.setText("修改");
			}
			break;
		}
	}
}
