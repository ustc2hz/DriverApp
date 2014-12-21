package ustc.sse.water.activity.zjx;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ParkingBook extends Activity implements OnClickListener,
		OnItemSelectedListener {
	private String[] license = { "��1234567", "��666666", "��888888" };
	private String[] time = { "30����10Ԫ", "1Сʱ20Ԫ", "90����30Ԫ", "2Сʱ40Ԫ" };
	private Button submitOrder;
	private Button cancelOrder;
	private ListView listView;
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.parking_book);
		initView();
	}

	public void initView() {
		ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, license);
		listView = (ListView) findViewById(R.id.listview_license_chose);
		listView.setAdapter(listViewAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, time);
		spinner = (Spinner) findViewById(R.id.spinner_time_chose);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(this);
		submitOrder = (Button) findViewById(R.id.button_submit_order);
		cancelOrder = (Button) findViewById(R.id.button_cancel_order);
		submitOrder.setOnClickListener(this);
		cancelOrder.setOnClickListener(this);
	}

	public void submit() {
		int count = listView.getCheckedItemCount();
		Toast.makeText(this, "-->>" + count, 1).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_submit_order:
			submit();
			break;
		case R.id.button_cancel_order:
			finish();
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String str = parent.getItemAtPosition(position).toString();
		Toast.makeText(this, str, 1).show();

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
