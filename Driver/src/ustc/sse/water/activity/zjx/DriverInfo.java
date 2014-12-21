package ustc.sse.water.activity.zjx;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class DriverInfo extends Activity implements OnClickListener {
	private ListView listView;
	private ImageView addLicence;
	private ImageView checkOrder;
	private ImageView twoCode;
	private Button backMap;
	private Button logout;
	private String licences[] = { "111111", "222222", "333333" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driver_info);
		initView();
	}

	public void initView() {
		listView = (ListView) findViewById(R.id.listview_license_show);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, licences);
		listView.setAdapter(adapter);
		addLicence = (ImageView) findViewById(R.id.ibtn_add_licence);
		checkOrder = (ImageView) findViewById(R.id.ibtn_check_order);
		twoCode = (ImageView) findViewById(R.id.ibtn_two__code);
		backMap = (Button) findViewById(R.id.button_back_map);
		logout = (Button) findViewById(R.id.button_logout);
		addLicence.setOnClickListener(this);
		checkOrder.setOnClickListener(this);
		twoCode.setOnClickListener(this);
		backMap.setOnClickListener(this);
		logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_add_licence:
			Intent intent1 = new Intent(this, AddDriverLicence.class);
			startActivity(intent1);
			break;
		case R.id.ibtn_check_order:
			Intent intent2 = new Intent(this, DriverOrderInfo.class);
			startActivity(intent2);
			break;
		case R.id.ibtn_two__code:
			// Intent intent3 = new Intent(this,);
			break;
		case R.id.button_back_map:
			finish();
			break;
		case R.id.button_logout:

			break;
		}

	}

}
