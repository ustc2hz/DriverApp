package ustc.sse.water.activity;

import ustc.sse.water.adapter.zjx.DriverLifeAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 
 * 车生活的界面类. <br>
 * 此类是展示车生活的界面的Activity类，以GridView构建Dashboard风格界面。
 * <p>
 * Copyright: Copyright (c) 2014-11-26 下午3:56:35
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫 sa614412@mail.ustc.edu.cn
 * @version 1.2.0
 */
public class DriverLife extends Activity implements OnItemClickListener {
	
	private static final int GAS_STATION = 0; // 加油站
	private static final int CAR_REPAIR = 1; // 汽车维修
	private static final int CAR_WASH = 2; // 洗车
	private static final int CAR_RENT = 3; // 租车
	
	/* GridView */
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_life);
		
		gridView = (GridView) findViewById(R.id.gridview_driver_life);
		gridView.setOnItemClickListener(this);// GridView单元点击事件监听
		gridView.setAdapter(new DriverLifeAdapter(this));// GridView添加适配器
	}

	/**
	 * GridView单元点击事件处理
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		Intent intent = new Intent(this, DriverMainScreen.class);
		
		switch (position) {
		case GAS_STATION: // 点击加油站
			intent.putExtra("result_type", "加油站"); // 返回Poi搜索类型：加油站
			break;
		case CAR_REPAIR: // 点击维修
			intent.putExtra("result_type", "汽车维修"); // 返回Poi搜索类型：汽车维修
			break;
		case CAR_WASH: // 点击洗车
			intent.putExtra("result_type", "010500"); // 返回Poi搜索类型：洗车
			break;
		case CAR_RENT: // 点击租车
			intent.putExtra("result_type", "010900"); // 返回Poi搜索类型：租车
			break;
		}
		
		setResult(1, intent);
		finish();
	}
}
