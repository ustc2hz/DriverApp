package ustc.sse.water.manager.zf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ustc.sse.water.activity.R;
import ustc.sse.water.adapter.zjx.OrderStateProcessAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 
 * 已完成订单类. <br>
 * 已完成订单列表的详细信息.
 * <p>
 * Copyright: Copyright (c) 2015-3-7 下午12:03:22
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 张芳     sa614296@mail.ustc.edu.cn
 * @version 3.0.0
 */
public class DActivity extends Activity{
	 int i=0;
	 
	 private OrderStateProcessAdapter myAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_order_list);
		
		//绑定Layout里面的ListView  
        ListView list = (ListView) findViewById(R.id.orderlist);	

      //生成动态数组，加入数据  
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();  
        for(int i=0;i<10;i++)  
        {  
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("CarNumber", R.id.car_number);  
            map.put("OrderNumber", R.id.order_number);
            map.put("OrderTime", R.id.orderring_time);
            map.put("Money", R.id.money);
            listItem.add(map);  
        }  
        
          myAdapter = new OrderStateProcessAdapter(DActivity.this, listItem);
         
        //添加并且显示  
        list.setAdapter(myAdapter);  
          
        //添加点击  
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}  
		 
        });
	
	}
}
