package ustc.sse.water.activity.zjx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ParkingInfo extends Activity implements OnClickListener {

	private Button bookParking;
	private Button back;
	private String[] bookInfos = { "��ǰ30����", "��ǰ1Сʱ", "��ǰ90����", "��ǰ2Сʱ" };
	private String[] bookMoneys = { "10", "20", "30", "40" }; // just a simple

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.parking_infomation);
		initView();
	}

	public void initView() {
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.listview_book_money, new String[] { "info", "money" },
				new int[] { R.id.book_time, R.id.book_money });
		ListView listView = (ListView) findViewById(R.id.listview_book_parking);
		listView.setAdapter(adapter);
		bookParking = (Button) findViewById(R.id.button_make_order);
		back = (Button) findViewById(R.id.button_back_listview);
		bookParking.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	public List<Map<String, String>> getData() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < bookInfos.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("info", bookInfos[i]);
			map.put("money", bookMoneys[i]);
			list.add(map);
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_make_order:
			Intent intent = new Intent(ParkingInfo.this, ParkingBook.class);
			startActivity(intent);
			break;
		case R.id.button_back_listview:
			finish();
			break;
		}

	}

}
