package ustc.sse.water.zf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.*;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 主界面 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014-11-7 下午8:14:49
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 张芳 sa614296@ustc.edu.cn
 * @version 1.0.0
 */
public class ManagerMain extends Activity {

	private static final Builder builder = null;
	public static String EXTRA_FROM_LOGIN;
	public static String EXTRA_LOGIN_USER;
	// GridView布局所用
	private List<Map<String, Object>> list;
	private GridView gridview;
	public String flag;
	// 使用Intent的能接收返回值的方法时使用的标记
	private int mainScreenRequest = 1;
	// 定义SharedPreferences和Editor
	public SharedPreferences sharedPreferences;
	public SharedPreferences.Editor editor;

	/** 内部类自定义BaseAdapter 重写getView()方法以方便实现显示和隐藏功能 */
	class MyAdapter extends BaseAdapter{
		private Context context; // 上下文
		private LayoutInflater mInflater; // 用来导入布局

		// 构造函数，传递flag判断是否隐藏组件
		public MyAdapter(Context context) {
			this.context = context;
			this.mInflater = LayoutInflater.from(context);
		}

		/* 得到List<Map<Sting,Object>>对象 imageIds存放功能导航图片 imageTexts存放功能文字 */
		public List<Map<String, Object>> getMyData() {
			list = new ArrayList<Map<String, Object>>();
			// 获取array_main.xml中的文字数组
			TypedArray ta = getResources().obtainTypedArray(R.array.functions);
			int[] imageIds = { R.drawable.message,
					R.drawable.setting, R.drawable.dingdan };
			for (int i = 0; i < imageIds.length; i++) {
				Map<String, Object> listItem = new HashMap<String, Object>();
				listItem.put("image", imageIds[i]);
				listItem.put("imageText", ta.getString(i));
				list.add(listItem);
			}
			return list;
		}

		@Override
		public int getCount() {
			// 返回数组的长度
			return getMyData().size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.util_gridview, null);
				holder = new ViewHolder();
				// 得到各个控件的对象
				holder.image = (ImageView) convertView
						.findViewById(R.id.image_function);
				holder.text = (TextView) convertView
						.findViewById(R.id.image_text);
				convertView.setTag(holder); // 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			/* 显示的内容，即我们存放在动态数组中的数据 */
			holder.image.setImageResource((Integer) getMyData().get(position)
					.get("image"));
			holder.text.setText(getMyData().get(position).get("imageText")
					.toString());

			return convertView;
		}

		/** 存放控件 */
		final class ViewHolder {
			public ImageView image;
			public TextView text;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_main_activity);
		// gridview初始化
		GridView gridview = (GridView) findViewById(R.id.gridview);
		// 添加自定义的Adapter
		gridview.setAdapter(new MyAdapter(this));
		
		sharedPreferences = getSharedPreferences("login_register",
				 Context.MODE_PRIVATE);
		// 对数据进行编辑
		editor = sharedPreferences.edit(); 
		// 判断SharedPreferences中是否有用户名记录
		int login_register = sharedPreferences.getInt("login_register", 1);
		
		// GridView的事件处理操作
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0: // 点击发布信息跳转
					Intent intent2 = new Intent(ManagerMain.this,
							ManagerDistributeMessage.class);
					startActivity(intent2);
					break;
				case 1: // 点击个人设置跳转
					Intent intent3 = new Intent(ManagerMain.this,
							ManagerSettings.class);
					startActivityForResult(intent3, mainScreenRequest);
					break;
				case 2: // 点击订单管理跳转
					Intent intent4 = new Intent(ManagerMain.this,
							Ordering.class);
					startActivityForResult(intent4, mainScreenRequest);
					break;
				}
			}
		});
	}

}
