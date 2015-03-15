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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 
 * 订单界面、默认显示当前正在进行的订单. <br>
 * 订单界面类.
  * <p>
 * Copyright: Copyright (c) 2015-03-01 下午10:35:02
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author张芳 sa614296@mail.ustc.edu.cn
 * @version 3.0.0
 */
public class AActivity extends Activity {
	
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
            map.put("CarNumber","aaaaa");  
            map.put("OrderNumber", 1111);
            map.put("OrderTime", "abv");
            map.put("Money", "dug");
            listItem.add(map);  
        }  
        
        //生成适配器的Item和动态数组对应的元素  
        myAdapter = new OrderStateProcessAdapter(AActivity.this,listItem);  
         
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
