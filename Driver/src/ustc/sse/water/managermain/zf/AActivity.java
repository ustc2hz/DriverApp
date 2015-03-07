package ustc.sse.water.managermain.zf;

import java.util.ArrayList;
import java.util.HashMap;

import ustc.sse.water.activity.R;
import ustc.sse.water.activity.R.id;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
	
	private TextView mtv_show1,mtv_show2,mtv_show3,mtv_show4;
	
	private ListView mlv_show;
	
	private ArrayList<String> mdata = new ArrayList<String>();
	
	private ListItemClickAdapter madapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished_order_list);
		
		//绑定Layout里面的ListView  
        ListView list = (ListView) findViewById(R.id.orderlist);	

      //生成动态数组，加入数据  
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
        for(int i=0;i<10;i++)  
        {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("CarNumber", R.id.car_number);  
            map.put("OrderNumber", R.id.order_number);
            map.put("OrderTime", R.id.orderring_time);
            map.put("Money", R.id.money);
            map.put("OrderState", R.id.complete_order);  
            listItem.add(map);  
        }  
        
        //生成适配器的Item和动态数组对应的元素  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
            R.layout.finishedorder,//ListItem的XML实现  
            //动态数组与ImageItem对应的子项          
            new String[] {"CarNumber","OrderNumber","OrderTime","Money", "OrderState"},   
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
            new int[] {R.id.car_number, R.id.order_number,R.id.orderring_time,R.id.money,R.id.complete_order}  
        );  
         
        //添加并且显示  
        list.setAdapter(listItemAdapter);  
          
        //添加点击  
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}  
		 
        });  
	}

}
