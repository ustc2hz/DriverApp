package ustc.sse.water.zf;

import ustc.sse.water.activity.R;
import ustc.sse.water.managermain.zf.CActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

	@Override
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
		switch (v.getId()) {
		case R.id.button1:
			Dialog dialog = new AlertDialog.Builder(ManagerChangePass.this)
			.setTitle("您确定提交吗？")
			.setMessage("确定提交？")
			.setPositiveButton("提交", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Intent intent1 = new Intent(ManagerChangePass.this,
							CActivity.class);
					startActivity(intent1);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			}).create();
			dialog.show();
			
			break;
		case R.id.button2:
			Intent intent2 = new Intent(ManagerChangePass.this,
					CActivity.class);
			startActivity(intent2);
			break;
		}

	}

}
