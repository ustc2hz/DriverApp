package ustc.sse.water.managermain.zf;

import java.util.ArrayList;
import java.util.HashMap;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * 
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015-2-12 上午10:08:49
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * @author ****
 * @version 1.0.0
 */
public class AActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderrings);
		
		//绑定Layout里面的ListView  
        ListView list = (ListView) findViewById(R.id.list);	

      //生成动态数组，加入数据  
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
        for(int i=0;i<10;i++)  
        {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("CarNumber", R.id.editText1);  
            map.put("OrderTime", R.id.editText2);  
            map.put("OrderState", R.id.button1);  
            listItem.add(map);  
        }  
        
        //生成适配器的Item和动态数组对应的元素  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
            R.layout.orderring_util,//ListItem的XML实现  
            //动态数组与ImageItem对应的子项          
            new String[] {"CarNumber","OrderTime", "OrderState"},   
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
            new int[] {R.id.editText1,R.id.editText2,R.id.button1}  
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
