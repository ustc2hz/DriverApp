package ustc.sse.water.zf;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 
 * 主界面 <br>
 * 主要用于选择登录模式，分别是管理员登录模块和驾驶员登录模块
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:14:49
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 1.0.0
 */

public class ManagerMainScreen extends Activity {
	
	//checkBox用于选择登录模式
	private CheckBox login_cb_savepwd;
	//申明登录注册的按钮
	private Button login_btn_login,login_btn_zhuce;
	private RadioGroup radiogroup;
	public RadioButton mRadio1, mRadio2;
	public RadioGroup mRadioGroup1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
//		check_driver = (CheckBox)findViewById(R.id.check_driver);
//		check_manager = (CheckBox)findViewById(R.id.check_manager);
		
		radiogroup=(RadioGroup)findViewById(R.id.gendergroup);
		login_btn_login = (Button)findViewById(R.id.login_btn_login);
		login_btn_zhuce  = (Button)findViewById(R.id.login_btn_zhuce);

		
		
		/**
		 * 对登录的按钮做监听
		 */
		
		login_btn_login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		/**
		 * 对注册按钮做监听		
		 */
		login_btn_zhuce.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		/**
		 * 对radioButton做监听，根据radioButton显示相应的用户UI
		 */
		RadioGroup.OnCheckedChangeListener radiogpchange = new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == mRadio1.getId()) {
					Toast.makeText(getApplicationContext(), "驾驶员", 1).show();
				} else if (checkedId == mRadio2.getId()) {
					Toast.makeText(getApplicationContext(), "管理员", 1).show();
				}
			}
		}; 
		
	}	
}
