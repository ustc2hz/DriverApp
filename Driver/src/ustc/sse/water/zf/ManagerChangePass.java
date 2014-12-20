package ustc.sse.water.zf;

import ustc.sse.water.activity.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * 管理员模块、用于修改密码. <br>
 * <p>
 * Copyright: Copyright (c) 2014-11-14 下午7:32:09
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 2.0.0
 */

public class ManagerChangePass extends Activity implements OnClickListener {

	// 定义两个button变量，点击事件时用
	Button bt1, bt2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_change_password);

		bt1 = (Button) findViewById(R.id.button1);
		bt2 = (Button) findViewById(R.id.button2);

		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent intent1 = new Intent(ManagerChangePass.this,
					ManagerSettings.class);
			startActivity(intent1);
			break;
		case R.id.button2:
			Intent intent2 = new Intent(ManagerChangePass.this,
					ManagerSettings.class);
			startActivity(intent2);
			break;
		}

	}

}
