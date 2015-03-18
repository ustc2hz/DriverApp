package ustc.sse.water.zf;

import java.util.HashMap;

import ustc.sse.water.activity.R;
import ustc.sse.water.util.zf.DummyFragment;
import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2014-11-15 上午8:56:49
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author ****
 * @version 1.0.0
 */
public class Ordering extends Activity implements OnClickListener, TabListener {

	private static final String SELECTED_ITEM = "selected_item";

	// private PersonService service;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderrings);

		final ActionBar bar = getActionBar();
		// 设置ActionBar的导航模式为Tab模式
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// 新建2个Tab
		bar.addTab(bar.newTab().setText("正在进行的订单").setTabListener(this));
		bar.addTab(bar.newTab().setText("已经完成的订单").setTabListener(this));

	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		if (savedInstanceState.containsKey(SELECTED_ITEM)) {
			// 选中前面保存索引对应的的fragment页
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(SELECTED_ITEM));
		}
	}

	// 获取点击事件
	private final class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView listView = (ListView) parent;
			HashMap<String, Object> data = (HashMap<String, Object>) listView
					.getItemAtPosition(position);
			String personid = data.get("id").toString();
			Toast.makeText(getApplicationContext(), personid, 1).show();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		// 将当前选中的Fragment页的索引保存到Bundle中
		outState.putInt(SELECTED_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		// 创建一个新的Fragment对象
		Fragment fragment = new DummyFragment();
		// 创建一个Buddle对象，用于向Fragment传送参数
		Bundle args = new Bundle();
		args.putInt(DummyFragment.ARG_SECTION_NUMBER, tab.getPosition() + 1);

		// 向fragment传入参数
		fragment.setArguments(args);
		// 获取FragmentTransaction对象
		FragmentTransaction ft = getFragmentManager().beginTransaction();

		// 使用fragment代替该Activity中的组件
		ft.replace(R.id.container, fragment);
		// 提交事物
		ft.commit();
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onClick(View arg0) {

	}
	
}
