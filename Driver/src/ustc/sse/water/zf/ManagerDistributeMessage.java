package ustc.sse.water.zf;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ustc.sse.water.activity.R;
import ustc.sse.water.data.model.DetailDataToServer;
import ustc.sse.water.data.model.ParkDetailData;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * 管理员模块、. <br>
 * 发布消息.
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:09:10
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 1.0.0
 */
public class ManagerDistributeMessage extends Activity implements
		View.OnClickListener {

	// jackson的ObjectMapper,用于在json字符串和Java对象间转换——黄志恒
	public static ObjectMapper objectMapper = new ObjectMapper();
	Button bt1, bt2, bt3;
	// 停车场详细数据的对象——黄志恒
	ParkDetailData dData;
	EditText et1, et2, et3, et4, et5, et6, et7;

	/**
	 * 返回通知数据提交服务器是否成功——黄志恒
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				// txt_status.setText("success");
				Toast.makeText(getApplicationContext(), "Submit Success!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Submit Failed!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	// 将停车场详细数据发送到服务器的对象——黄志恒
	DetailDataToServer postData;

	/*
	 * 初始化停车场详细信息数据——黄志恒
	 */
	private void initData() {
		dData = new ParkDetailData();
		if (et1.getText() != null) {
			dData.setOrderSpaceSum(et1.getText().toString().trim());
		}
		if (et2.getText() != null) {
			dData.setOrderOfTen(et2.getText().toString().trim());
		}
		if (et3.getText() != null) {
			dData.setOrderOfTwe(et3.getText().toString().trim());
		}
		if (et4.getText() != null) {
			dData.setOrderOfTri(et4.getText().toString().trim());
		}
		if (et5.getText() != null) {
			dData.setSpaceOfHal(et5.getText().toString().trim());
		}
		if (et6.getText() != null) {
			dData.setSpaceOfOne(et6.getText().toString().trim());
		}
		if (et7.getText() != null) {
			dData.setSpaceOfMor(et7.getText().toString().trim());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.bt1:
			// 点击修改,将所有的edittext设置成可编辑的
			if (bt1.getText().equals("修改")) {
				et1.setEnabled(true);
				et2.setEnabled(true);
				et3.setEnabled(true);
				et4.setEnabled(true);
				et5.setEnabled(true);
				et6.setEnabled(true);
				et7.setEnabled(true);
				bt1.setText("取消修改");
			} else if (bt1.getText().equals("取消修改")) {
				// 点击取消修改，文本框恢复为不可编辑的，按钮文本重置为修改
				et1.setEnabled(false);
				et2.setEnabled(false);
				et3.setEnabled(false);
				et4.setEnabled(false);
				et5.setEnabled(false);
				et6.setEnabled(false);
				et7.setEnabled(false);
				bt1.setText("修改");
			}
			break;

		// 点击提交数据提交给服务器
		case R.id.bt2:
			// Intent intent2 = new Intent(distribute.this, MainActivity.class);
			// startActivity(intent2);
			// simple(v);
			// 使用子线程向服务器提交数据，并对返回的数据做处理——黄志恒
			new Thread() {
				@Override
				public void run() {
					try {
						postData();
						if (postData.responseMsg.equals("1")) {
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
						} else {
							Message msg = Message.obtain();
							msg.what = 0;
							handler.sendMessage(msg);
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 线程逻辑
				}
			}.start();

			break;
		case R.id.bt3:
			// 点击返回，返回主界面
			Intent intent1 = new Intent(ManagerDistributeMessage.this,
					ManagerMain.class);
			startActivity(intent1);
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_distribute_messages);

		// 返回按钮
		bt1 = (Button) findViewById(R.id.bt1);
		// 编辑按钮
		bt2 = (Button) findViewById(R.id.bt2);
		// 提交按钮
		bt3 = (Button) findViewById(R.id.bt3);
		// 对按钮分别做监听
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);

		et1 = (EditText) findViewById(R.id.editText1);
		et2 = (EditText) findViewById(R.id.editText2);
		et3 = (EditText) findViewById(R.id.editText3);
		et4 = (EditText) findViewById(R.id.editText4);
		et5 = (EditText) findViewById(R.id.editText5);
		et6 = (EditText) findViewById(R.id.editText6);
		et7 = (EditText) findViewById(R.id.editText7);

		// 分别设置文本框不可编辑，并且输入默认数据
		et1.setEnabled(false);
		et2.setEnabled(false);
		et3.setEnabled(false);
		et4.setEnabled(false);
		et5.setEnabled(false);
		et6.setEnabled(false);
		et7.setEnabled(false);
	}

	/**
	 * 发送数据——黄志恒
	 * 
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	private void postData() throws JsonGenerationException,
			JsonMappingException, Exception {
		initData();
		// 将ParkingData转换为Json字符串
		String data = objectMapper.writeValueAsString(dData);
		postData = new DetailDataToServer(data);
		postData.postDataToServer();
	}

	private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {

		// 调用setPositiveButton方法添加取消按钮
		return builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// 弹出对话框后消失
						arg0.dismiss();
						// 设置intent跳转
						// Intent it = new
						// Intent(distribute.this,distribute.class);
						// it.putExtra("etr","0");
						// startActivity(it);
					}
				});
	}

	private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {

		return builder.setPositiveButton("提交",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						// 弹出对话框后消失
						arg0.dismiss();
						// 设置跳转
						Intent it = new Intent(ManagerDistributeMessage.this,
								ManagerMain.class);
						it.putExtra("etr", "1");
						startActivity(it);
					}
				});
	}

	public void simple(View source) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(
				"尊敬的用户您好！").setMessage("您确定要提交吗？");
		// 为AlertDialog.Builder添加按钮
		setPositiveButton(builder);
		setNegativeButton(builder).create().show();
	}

}
